package TokoHp.Views;

import TokoHp.Objects.Product;
import TokoHp.Objects.ShoppingCart;
import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.popUpErrorMessage;
import static TokoHp.Objects.Variable.popUpSuccessMessage;
import static TokoHp.Objects.Variable.stringToNumber;
import static TokoHp.Objects.Variable.toastFailed;
import static TokoHp.Objects.Variable.toastInfo;
import static TokoHp.Objects.Variable.toastSuccess;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.sql.*;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import raven.alerts.MessageAlerts;
import raven.popup.component.PopupController;

public class TransaksiBaru extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    private String query;
    ResultSet rset;
    Statement statement;
    private int rsetInt;
    DefaultTableModel modelTbCheckout;
    DefaultTableModel modelTbListHp;
    final ShoppingCart shoppingCart = new ShoppingCart();

    public TransaksiBaru() {
        initComponents();
        init();
    }

    private void init() {
        String clearAllCheckoutIcon = !Variable.isDarkTheme() ? "TokoHp/Icons/trash.svg" : "TokoHp/Icons/trash_dark.svg";
        connection = Variable.koneksi();
        disableEditableAndVisible();
        setTableCheckout();
        setTableListHp();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setPlaceholderTextfield(tfNamaPelanggan, "Masukkan nama pembeli");
        Variable.setSearchFieldIcon(tfPencarian);
        btKosongkan.setIcon(new FlatSVGIcon(clearAllCheckoutIcon));
    }

    private void bayarTransaksi() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int activeUserId = Variable.getActiveUserId();
        try {
            query = "INSERT INTO transaksi (ID_USER, NAMA_PELANGGAN, TANGGAL, TOTAL_BAYAR) VALUES (?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setInt(1, activeUserId);
            pstat.setString(2, tfNamaPelanggan.getText());
            pstat.setTimestamp(3, timestamp);
            pstat.setInt(4, shoppingCart.calculateTotal());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                for (Product item : shoppingCart.getItems()) {
                    String insertSql = "INSERT INTO DETAIL_TRANSAKSI (ID_PHONE, JUMLAH_PEMBELIAN) VALUES (?,?)";
                    PreparedStatement insertPstat = connection.prepareStatement(insertSql);
                    insertPstat.setInt(1, item.getIdProduk());
                    insertPstat.setInt(2, item.getStok());
                    insertPstat.executeUpdate();

                    String updateSql = "UPDATE PHONES SET STOK = STOK - ? WHERE ID_PHONE = ?";
                    PreparedStatement updatePstat = connection.prepareStatement(updateSql);
                    updatePstat.setInt(1, item.getStok());
                    updatePstat.setInt(2, item.getIdProduk());
                    updatePstat.executeUpdate();
                }
                popUpSuccessMessage("Transaksi berhasil", "Transaksi berhasil dilakukan");
            } else {
                popUpErrorMessage("Error", "Transaksi gagal");
            }
            shoppingCart.getItems().clear();
            setTableCheckout();
            setTableListHp();
            kalkulasiTotal();
            clearText();
            pstat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void disableEditableAndVisible() {
        tfNamaHp.setEditable(false);
        tfHargaItem.setEditable(false);
        textStokTersedia.setText(" ");
    }

    private void setSpinnerValue(int maximumValue) {
        int minValue = maximumValue > 0 ? 1 : 0;
        SpinnerModel spinnerModel = new SpinnerNumberModel(minValue, minValue, maximumValue, minValue);
        spJumlah.setModel(spinnerModel);
    }

    private void setSpinnerValue(String id, int stok) {
        int maximumValue = 0;
        query = "SELECT STOK FROM phones WHERE ID_PHONE = " + id;

        try {
            statement = connection.createStatement();
            rset = statement.executeQuery(query);
            if (rset.next()) {
                maximumValue = rset.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        int minValue = maximumValue > 0 ? 1 : 0;
        SpinnerModel spinnerModel = new SpinnerNumberModel(stok, minValue, maximumValue, minValue);
        spJumlah.setModel(spinnerModel);
        textStokTersedia.setText("Stok tersedia: " + maximumValue);
    }

    private void kalkulasiTotal() {
        textSubtotal.setText("Rp" + Variable.stringToNumber(String.valueOf(shoppingCart.calculateTotal())));
    }

    private void setTableListHp() {
        modelTbListHp = (DefaultTableModel) tbListHp.getModel();
        try {
            query = Variable.sqlTableDaftarHP;
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            pstat.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            modelTbListHp.setRowCount(0);

            while (rset.next()) {
                Object[] data = {
                    rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getString(4),
                    stringToNumber(rset.getString(5))
                };
                modelTbListHp.addRow(data);
            }
            tbListHp.setModel(modelTbListHp);
            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getTableListHpData() {
        int selectedRow = tbListHp.getSelectedRow();
        String id, brand, model, harga;
        int stok;

        if (selectedRow >= 0) {
            id = tbListHp.getValueAt(selectedRow, 0).toString();
            brand = tbListHp.getValueAt(selectedRow, 1).toString();
            model = tbListHp.getValueAt(selectedRow, 2).toString();
            stok = Integer.parseInt(tbListHp.getValueAt(selectedRow, 3).toString());
            harga = tbListHp.getValueAt(selectedRow, 4).toString();

            textIdHp.setText(id);
            tfNamaHp.setText(brand + " " + model);
            tfHargaItem.setText(harga);
            setSpinnerValue(stok);
            textStokTersedia.setText("Stok tersedia: " + stok);
        }
    }

    private void setTableCheckout() {
        modelTbCheckout = (DefaultTableModel) tbCheckout.getModel();
        modelTbCheckout.setRowCount(0);

        for (Product item : shoppingCart.getItems()) {
            var id = item.getIdProduk();
            var nama = item.getNamaProduk();
            var stok = item.getStok();
            var harga = item.getHarga();
            var formatHarga = stringToNumber(String.valueOf(harga));

            Object[] data = {false, id, nama, stok, formatHarga};
            modelTbCheckout.addRow(data);
        }
        tbCheckout.setModel(modelTbCheckout);
    }

    private void getTableCheckout() {
        int selectedRow = tbCheckout.getSelectedRow();
        String id, phoneName, harga;
        int stok;

        if (selectedRow >= 0) {
            id = tbCheckout.getValueAt(selectedRow, 1).toString();
            phoneName = tbCheckout.getValueAt(selectedRow, 2).toString();
            stok = Integer.parseInt(tbCheckout.getValueAt(selectedRow, 3).toString());
            harga = tbCheckout.getValueAt(selectedRow, 4).toString();

            textIdHp.setText(id);
            tfNamaHp.setText(phoneName);
            tfHargaItem.setText(Variable.stringToNumber(harga));
            setSpinnerValue(id, stok);
        }
    }

    private void tambahData() {
        int id = Integer.parseInt(textIdHp.getText());
        var namaHp = tfNamaHp.getText();
        int stok = Integer.parseInt(spJumlah.getValue().toString());
        int harga = Variable.numberToInt(tfHargaItem);
        shoppingCart.addItem(new Product(id, namaHp, stok, harga));
    }

    private void updateItem() {
        int id = Integer.parseInt(textIdHp.getText());
        int stok = Integer.parseInt(spJumlah.getValue().toString());
        shoppingCart.updateItemQuantity(new Product(id, stok), stok);
    }

    private void deleteItem() {
        int count = 0;
        int rowCount = tbCheckout.getRowCount() - 1;
        int idProduk;
        boolean isRowSelected;

        for (int i = 0; i <= rowCount; i++) {
            isRowSelected = (boolean) tbCheckout.getValueAt(i, 0);
            if (isRowSelected) {
                idProduk = (int) tbCheckout.getValueAt(i, 1);
                shoppingCart.removeItem(new Product(idProduk));
                count += 1;
            }
        }

        if (count == 0) {
            toastFailed("Tidak ada barang dihapus");
        } else {
            toastSuccess(count + " Barang dihapus");
        }
    }

    private void clearText() {
        tfNamaHp.setText("");
        tfHargaItem.setText("");
        tfPencarian.setText("");
        tfNamaPelanggan.setText("");
        spJumlah.setValue(0);
        textIdHp.setText("0");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbListHp = new javax.swing.JTable();
        btTambah = new javax.swing.JButton();
        btBayar = new javax.swing.JButton();
        textIdHp = new javax.swing.JLabel();
        spJumlah = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        tfNamaHp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCheckout = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        tfPencarian = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btUpdate = new javax.swing.JButton();
        btHapus = new javax.swing.JButton();
        btClearText = new javax.swing.JButton();
        tfNamaPelanggan = new javax.swing.JTextField();
        textSubtotal = new javax.swing.JLabel();
        tfHargaItem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btKosongkan = new javax.swing.JButton();
        textStokTersedia = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        tbListHp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Phone", "Brand", "Model", "Stok", "Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbListHp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListHpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbListHp);
        if (tbListHp.getColumnModel().getColumnCount() > 0) {
            tbListHp.getColumnModel().getColumn(0).setPreferredWidth(80);
            tbListHp.getColumnModel().getColumn(0).setMaxWidth(80);
            tbListHp.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbListHp.getColumnModel().getColumn(1).setMaxWidth(150);
            tbListHp.getColumnModel().getColumn(3).setPreferredWidth(120);
            tbListHp.getColumnModel().getColumn(3).setMaxWidth(120);
            tbListHp.getColumnModel().getColumn(4).setPreferredWidth(220);
            tbListHp.getColumnModel().getColumn(4).setMaxWidth(220);
        }

        btTambah.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(235, 235, 240));
        btTambah.setText("Tambah Item");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btBayar.setBackground(new java.awt.Color(40, 205, 65));
        btBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btBayar.setForeground(new java.awt.Color(235, 235, 240));
        btBayar.setText("Bayar");
        btBayar.setToolTipText("Bayar transaksi");
        btBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBayarActionPerformed(evt);
            }
        });

        textIdHp.setText("0");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Jumlah :");

        tfNamaHp.setColumns(1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Handphone :");

        tbCheckout.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Pilih", "ID Phone", "Nama Handphone", "Jumlah", "Harga Per-Item"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCheckout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCheckoutMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbCheckout);
        if (tbCheckout.getColumnModel().getColumnCount() > 0) {
            tbCheckout.getColumnModel().getColumn(0).setPreferredWidth(60);
            tbCheckout.getColumnModel().getColumn(0).setMaxWidth(60);
            tbCheckout.getColumnModel().getColumn(1).setPreferredWidth(80);
            tbCheckout.getColumnModel().getColumn(1).setMaxWidth(80);
            tbCheckout.getColumnModel().getColumn(3).setPreferredWidth(80);
            tbCheckout.getColumnModel().getColumn(3).setMaxWidth(80);
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Pilih barang");

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Keranjang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Harga Per-item :");

        btUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(235, 235, 240));
        btUpdate.setText("Update Jumlah Item");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btHapus.setBackground(new java.awt.Color(255, 59, 48));
        btHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btHapus.setForeground(new java.awt.Color(235, 235, 240));
        btHapus.setText("Hapus Item Terpilih");
        btHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHapusActionPerformed(evt);
            }
        });

        btClearText.setBackground(new java.awt.Color(142, 142, 147));
        btClearText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClearText.setForeground(new java.awt.Color(235, 235, 240));
        btClearText.setText("Reset");
        btClearText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearTextActionPerformed(evt);
            }
        });

        tfNamaPelanggan.setColumns(1);
        tfNamaPelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfNamaPelangganKeyReleased(evt);
            }
        });

        textSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        textSubtotal.setText("Rp0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("ID Phone :");

        btKosongkan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btKosongkan.setText("Kosongkan");
        btKosongkan.setToolTipText("Bersihkan semua keranjang checkout");
        btKosongkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKosongkanActionPerformed(evt);
            }
        });

        textStokTersedia.setText("Stok tersedia: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textIdHp))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1)
                                .addComponent(jLabel6)
                                .addComponent(jLabel2)
                                .addComponent(spJumlah)
                                .addComponent(tfHargaItem, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                .addComponent(tfNamaHp))
                            .addComponent(textStokTersedia, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btClearText, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                .addComponent(btTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(187, 187, 187)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textSubtotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btKosongkan))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(btBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(textIdHp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaHp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfHargaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textStokTersedia)
                        .addGap(29, 29, 29)
                        .addComponent(btTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClearText))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(btKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBayar)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTableListHp();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void tbListHpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListHpMouseClicked
        getTableListHpData();
    }//GEN-LAST:event_tbListHpMouseClicked

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (Integer.parseInt(textIdHp.getText()) == 0) {
            Variable.popUpErrorMessage("Checkout gagal", "Mohon memilih barang dahulu dari tabel");
            return;
        }

        if (Integer.parseInt(spJumlah.getValue().toString()) == 0) {
            Variable.popUpErrorMessage("Checkout gagal", "Stok barang tidak tersedia");
            return;
        }

        tambahData();
        setTableCheckout();
        kalkulasiTotal();
    }//GEN-LAST:event_btTambahActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        updateItem();
        setTableCheckout();
        kalkulasiTotal();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHapusActionPerformed
        deleteItem();
        setTableCheckout();
        kalkulasiTotal();
    }//GEN-LAST:event_btHapusActionPerformed

    private void btClearTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearTextActionPerformed
        clearText();
        disableEditableAndVisible();
        toastInfo("Teks dibersihkan");
    }//GEN-LAST:event_btClearTextActionPerformed

    private void btBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBayarActionPerformed
        if (shoppingCart.getItems().isEmpty()) {
            Variable.popUpErrorMessage("Keranjang kosong", "Harap masukkan barang terlebih dahulu di keranjang");
            return;
        }

        if (tfNamaPelanggan.getText().isEmpty()) {
            Variable.popUpErrorMessage("Nama pelanggan diperlukan", "Harap memasukkan nama pelanggan");
            return;
        }

        bayarTransaksi();
        disableEditableAndVisible();
    }//GEN-LAST:event_btBayarActionPerformed

    private void tfNamaPelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNamaPelangganKeyReleased
        Variable.capitalizeFirstLetter(tfNamaPelanggan);
    }//GEN-LAST:event_tfNamaPelangganKeyReleased

    private void btKosongkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKosongkanActionPerformed
        if (shoppingCart.getItems().isEmpty()) {
            MessageAlerts.getInstance().showMessage("Keranjang kosong", "Tidak ada produk di keranjang yang akan dibersihkan", MessageAlerts.MessageType.ERROR);
            return;
        }

        MessageAlerts.getInstance().showMessage("Bersihkan Keranjang Checkout?", "Tindakan ini akan menghapus semua produk yang telah Anda tambahkan.", MessageAlerts.MessageType.WARNING, MessageAlerts.YES_OPTION,
                (PopupController pc, int i) -> {
                    switch (i) {
                        case 0 -> {
                            shoppingCart.getItems().clear();
                            setTableCheckout();
                            kalkulasiTotal();
                        }
                        case 1 ->
                            pc.closePopup();
                    }
                });
    }//GEN-LAST:event_btKosongkanActionPerformed

    private void tbCheckoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCheckoutMouseClicked
        getTableCheckout();
    }//GEN-LAST:event_tbCheckoutMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBayar;
    private javax.swing.JButton btClearText;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btKosongkan;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner spJumlah;
    private javax.swing.JTable tbCheckout;
    private javax.swing.JTable tbListHp;
    private javax.swing.JLabel textIdHp;
    private javax.swing.JLabel textStokTersedia;
    private javax.swing.JLabel textSubtotal;
    private javax.swing.JTextField tfHargaItem;
    private javax.swing.JTextField tfNamaHp;
    private javax.swing.JTextField tfNamaPelanggan;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables

    private String numberToInt(Number harga) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
