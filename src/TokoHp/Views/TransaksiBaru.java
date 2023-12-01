package TokoHp.Views;

import TokoHp.Objects.Product;
import TokoHp.Objects.ShoppingCart;
import TokoHp.Objects.Variable;
import java.sql.*;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import raven.alerts.MessageAlerts;

public class TransaksiBaru extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    private String query;
    ResultSet rset;
    private int rsetInt;
    final Object[] columnCheckout = {"ID Phone", "Nama Handphone", "Jumlah", "Harga Per-item"};
    final Object[] columnListHp = {"ID Phone", "Brand", "Model", "Stok", "Harga"};
    DefaultTableModel modelTbCheckout = new DefaultTableModel(columnCheckout, 0);
    DefaultTableModel modelTbListHp = new DefaultTableModel(columnListHp, 0);
    final ShoppingCart shoppingCart = new ShoppingCart();

    public TransaksiBaru() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        disableEditableAndVisible();
        setTableCheckout();
        setTableListHp();
        Variable.setActiveIDUser(textIdKaryawan);
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setPlaceholderTextfield(tfNamaPelanggan, "Masukkan nama pembeli");
        Variable.setLabelFont(jLabel8);
    }

    private void bayarTransaksi() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            query = "INSERT INTO transaksi (ID_USER, NAMA_PELANGGAN, TANGGAL, TOTAL_BAYAR) VALUES (?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setInt(1, Integer.parseInt(textIdKaryawan.getText()));
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
                Variable.popUpSuccessMessage("Berhasil", "Transaksi berhasil");
            } else {
                Variable.popUpErrorMessage("Error", "Transaksi gagal");
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
        textIdKaryawan.setVisible(false);
        tfNamaHp.setEditable(false);
        tfHargaItem.setEditable(false);
        textStokTersedia.setVisible(false);
    }

    void setSpinnerValue(int maximumValue) {
        int minValue = maximumValue > 0 ? 1 : 0;
        SpinnerModel spinnerModel = new SpinnerNumberModel(minValue, minValue, maximumValue, 1);
        spJumlah.setModel(spinnerModel);
    }

    private void kalkulasiTotal() {
        textSubtotal.setText("Rp" + Variable.stringToNumber(String.valueOf(shoppingCart.calculateTotal())));
    }

    private void setTableCheckout() {
        modelTbCheckout.setRowCount(0);

        for (Product item : shoppingCart.getItems()) {
            int id = item.getIdProduk();
            String nama = item.getNamaProduk();
            int stok = item.getStok();
            int harga = item.getHarga();
            Object[] data = {id, nama, stok, harga};
            modelTbCheckout.addRow(data);
        }
        tbCheckout.setModel(modelTbCheckout);
    }

    private void setTableListHp() {
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
                    rset.getString(5)
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
            tfHargaItem.setText(Variable.stringToNumber(harga));
            setSpinnerValue(stok);
            textStokTersedia.setVisible(true);
            textStokTersedia.setText("Stok tersedia: " + stok);
        }
    }

    private void tambahData() {
        int id = Integer.parseInt(textIdHp.getText());
        String namaHp = tfNamaHp.getText();
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
        int id = Integer.parseInt(textIdHp.getText());
        shoppingCart.removeItem(new Product(id));
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
        textIdKaryawan = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textSubtotal = new javax.swing.JLabel();
        tfHargaItem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        textStokTersedia = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        tbListHp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbListHp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListHpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbListHp);

        btTambah.setBackground(new java.awt.Color(0, 122, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("Tambah item");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btBayar.setBackground(new java.awt.Color(40, 205, 65));
        btBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btBayar.setForeground(new java.awt.Color(255, 255, 255));
        btBayar.setText("Bayar");
        btBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBayarActionPerformed(evt);
            }
        });

        textIdHp.setText("0");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Jumlah :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Handphone :");

        tbCheckout.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbCheckout);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Pilih barang");

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Checkout");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Harga Per-item :");

        btUpdate.setBackground(new java.awt.Color(0, 122, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btUpdate.setText("Update jumlah item");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btHapus.setBackground(new java.awt.Color(255, 59, 48));
        btHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btHapus.setForeground(new java.awt.Color(255, 255, 255));
        btHapus.setText("Hapus item");
        btHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHapusActionPerformed(evt);
            }
        });

        btClearText.setBackground(new java.awt.Color(142, 142, 147));
        btClearText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClearText.setForeground(new java.awt.Color(255, 255, 255));
        btClearText.setText("Clear text");
        btClearText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearTextActionPerformed(evt);
            }
        });

        tfNamaPelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfNamaPelangganKeyReleased(evt);
            }
        });

        textIdKaryawan.setText("idKaryawan");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("TRANSAKSI");

        textSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        textSubtotal.setText("Rp0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("ID Phone :");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Kosongkan keranjang");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        textStokTersedia.setText("Stok tersedia: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textIdHp))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel2)
                                        .addComponent(spJumlah, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                        .addComponent(tfNamaHp)
                                        .addComponent(tfHargaItem))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textStokTersedia, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btClearText, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))))
                        .addGap(180, 180, 180)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(textIdKaryawan))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(textSubtotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)))
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
                .addGap(14, 14, 14)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textIdKaryawan, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textSubtotal)
                            .addComponent(jButton1))
                        .addGap(11, 11, 11)
                        .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        if (Integer.parseInt(textIdHp.getText()) != 0) {
            if (Integer.parseInt(spJumlah.getValue().toString()) != 0) {
                tambahData();
                setTableCheckout();
                kalkulasiTotal();
            } else {
                Variable.popUpErrorMessage("Error", "Stok barang tidak tersedia");
            }
        } else {
            Variable.popUpErrorMessage("Error", "Mohon memilih barang dahulu dari tabel");
        }

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
    }//GEN-LAST:event_btClearTextActionPerformed

    private void btBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBayarActionPerformed
        if (!shoppingCart.getItems().isEmpty()) {
            if (!tfNamaPelanggan.getText().isEmpty()) {
                bayarTransaksi();
                disableEditableAndVisible();
            } else {
                Variable.popUpErrorMessage("Nama pelanggan diperlukan!", "Harap memasukkan nama pelanggan");
            }
        } else {
            Variable.popUpErrorMessage("Tidak ada barang", "Harap masukkan barang di keranjang");
        }
    }//GEN-LAST:event_btBayarActionPerformed

    private void tfNamaPelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNamaPelangganKeyReleased
        Variable.capitalizeFirstLetter(tfNamaPelanggan);
    }//GEN-LAST:event_tfNamaPelangganKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!shoppingCart.getItems().isEmpty()) {
            shoppingCart.getItems().clear();
            setTableCheckout();
            kalkulasiTotal();
        } else {
            MessageAlerts.getInstance().showMessage("Gagal kosongkan keranjang", "Keranjang kosong tidak ada produk di keranjang", MessageAlerts.MessageType.WARNING);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBayar;
    private javax.swing.JButton btClearText;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner spJumlah;
    private javax.swing.JTable tbCheckout;
    private javax.swing.JTable tbListHp;
    private javax.swing.JLabel textIdHp;
    private javax.swing.JLabel textIdKaryawan;
    private javax.swing.JLabel textStokTersedia;
    private javax.swing.JLabel textSubtotal;
    private javax.swing.JTextField tfHargaItem;
    private javax.swing.JTextField tfNamaHp;
    private javax.swing.JTextField tfNamaPelanggan;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
