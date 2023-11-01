package TokoHp.Views;

import TokoHp.Function.Product;
import TokoHp.Function.ShoppingCart;
import TokoHp.Function.Variable;
import java.sql.*;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class TransaksiBaru extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    String query;
    Statement stat;
    ResultSet rset;
    int rsetInt;
    Object[] columnCheckout = {"Id Hp", "Nama Handphone", "Jumlah", "Harga"};
    Object[] columnListHp = {"Id HP", "Brand", "Model", "Stok", "Harga"};
    DefaultTableModel modelTbCheckout = new DefaultTableModel(columnCheckout, 0);
    DefaultTableModel modelTbListHp = new DefaultTableModel(columnListHp, 0);
    ShoppingCart shoppingCart = new ShoppingCart();

    public TransaksiBaru() {
        initComponents();
        setClosable(true);
        connection = Variable.koneksi();
        setEditableAndVisible();
        setTableCheckout();
        setTableListHp();
        getSessionId();
    }

    private void bayarTransaksi() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (!tfNamaPelanggan.getText().isEmpty()) {
            try {
                query = "INSERT INTO transaksi (ID_USER, NAMA_PELANGGAN, TANGGAL, TOTAL_BAYAR) VALUES (?,?,?,?)";
                pstat = connection.prepareStatement(query);
                pstat.setInt(1, Integer.parseInt(textIdKaryawan.getText()));
                pstat.setString(2, tfNamaPelanggan.getText());
                pstat.setTimestamp(3, timestamp);
                pstat.setInt(4, shoppingCart.calculateTotal());
                rsetInt = pstat.executeUpdate();

                for (Product item : shoppingCart.getItems()) {
                    String insertSql = "INSERT INTO BARANG_KELUAR VALUES (?,?,?)";
                    PreparedStatement insertPstat = connection.prepareStatement(insertSql);
                    insertPstat.setInt(1, 5);
                    insertPstat.setInt(2, item.getIdProduk());
                    insertPstat.setInt(3, item.getStok());
                    rsetInt = insertPstat.executeUpdate();

                    String updateSql = "UPDATE PHONES SET STOK = STOK - ? WHERE ID_PHONE = ?";
                    PreparedStatement updatePstat = connection.prepareStatement(updateSql);
                    updatePstat.setInt(1, item.getStok());
                    updatePstat.setInt(2, item.getIdProduk());
                    rsetInt = updatePstat.executeUpdate();
                }

                shoppingCart.getItems().clear();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            Variable.popUpErrorMessage("Error", "Masukkan nama pelanggan");
        }
    }

    private void setEditableAndVisible() {
        textIdHp.setVisible(false);
        tfNamaHp.setEditable(false);
        tfHarga.setEditable(false);
        textIdKaryawan.setVisible(false);
    }

    private void getSessionId() {
        try {
            query = "SELECT ID_USER FROM session_data";
            stat = connection.createStatement();
            rset = stat.executeQuery(query);

            if (rset.next()) {
                int idKaryawanFromDB = rset.getInt(1);
                textIdKaryawan.setText(String.valueOf(idKaryawanFromDB));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    void setSpinnerValue(int maximumValue) {
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, maximumValue, 1);
        spJumlah.setModel(spinnerModel);
    }

    private void KalkulasiTotal() {
        textSubtotal.setText("Total: " + shoppingCart.calculateTotal());
    }

    private void setTableCheckout() {
        //            query = "select id_hp, nama_handphone, jumlah from checkout join handphone using (id_hp)";
//            stat = connection.createStatement();
//            rset = stat.executeQuery(query);
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
            query = "select id_phone, nama_brand, nama_handphone, stok, harga from phones join brand using (id_brand) where stok > 0 AND LOWER(nama_brand) like ? OR LOWER(nama_handphone) like ?";
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
            tfHarga.setText(harga);
            setSpinnerValue(stok);
        }
    }

    private void tambahData() {
        int id = Integer.parseInt(textIdHp.getText());
        String namaHp = tfNamaHp.getText();
        int stok = Integer.parseInt(spJumlah.getValue().toString());
        int harga = Integer.parseInt(tfHarga.getText());
        shoppingCart.addItem(new Product(id, namaHp, stok, harga));
    }

    private void updateItem() {
        int id = Integer.parseInt(textIdHp.getText());
        String namaHp = tfNamaHp.getText();
        int stok = Integer.parseInt(spJumlah.getValue().toString());
        int harga = Integer.parseInt(tfHarga.getText());
        shoppingCart.updateItemQuantity(new Product(id, namaHp, stok, harga), stok);
    }

    private void deleteItem() {
        int id = Integer.parseInt(textIdHp.getText());
        String namaHp = tfNamaHp.getText();
        int stok = Integer.parseInt(spJumlah.getValue().toString());
        int harga = Integer.parseInt(tfHarga.getText());
        shoppingCart.removeItem(new Product(id, namaHp, stok, harga));
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textSubtotal = new javax.swing.JLabel();
        tfHarga = new javax.swing.JTextField();
        btUpdate = new javax.swing.JButton();
        btHapus = new javax.swing.JButton();
        btTambah3 = new javax.swing.JButton();
        tfNamaPelanggan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textIdKaryawan = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

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

        btTambah.setBackground(new java.awt.Color(51, 153, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("Tambah item");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btBayar.setBackground(new java.awt.Color(0, 204, 102));
        btBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btBayar.setForeground(new java.awt.Color(255, 255, 255));
        btBayar.setText("Bayar");
        btBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBayarActionPerformed(evt);
            }
        });

        textIdHp.setText("idHp");

        jLabel1.setText("Jumlah");

        jLabel2.setText("Nama HP");

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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Pilih barang");

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel4.setText("Cari");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText("Checkout");

        jLabel6.setText("Harga");

        textSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        textSubtotal.setText("Total:");

        btUpdate.setBackground(new java.awt.Color(51, 153, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btUpdate.setText("Update jumlah");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btHapus.setBackground(new java.awt.Color(255, 51, 51));
        btHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btHapus.setForeground(new java.awt.Color(255, 255, 255));
        btHapus.setText("Hapus item");
        btHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHapusActionPerformed(evt);
            }
        });

        btTambah3.setBackground(new java.awt.Color(153, 153, 153));
        btTambah3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah3.setForeground(new java.awt.Color(255, 255, 255));
        btTambah3.setText("Clear text");
        btTambah3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambah3ActionPerformed(evt);
            }
        });

        jLabel7.setText("Nama Pelanggan");

        textIdKaryawan.setText("idKaryawan");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Transaksi");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(textIdKaryawan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textIdHp))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(spJumlah)
                            .addComponent(tfHarga)
                            .addComponent(btTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfNamaHp, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(btHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btTambah3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textSubtotal)
                            .addComponent(jLabel7)
                            .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btBayar))))
                .addContainerGap())
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textIdHp)
                    .addComponent(textIdKaryawan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textSubtotal)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTambah3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btBayar)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
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
        tambahData();
        setTableCheckout();
        KalkulasiTotal();
    }//GEN-LAST:event_btTambahActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        updateItem();
        setTableCheckout();
        KalkulasiTotal();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHapusActionPerformed
        deleteItem();
        setTableCheckout();
        KalkulasiTotal();
    }//GEN-LAST:event_btHapusActionPerformed

    private void btTambah3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambah3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btTambah3ActionPerformed

    private void btBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBayarActionPerformed
        bayarTransaksi();
        setTableCheckout();
        setTableListHp();
        KalkulasiTotal();
    }//GEN-LAST:event_btBayarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBayar;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btTambah3;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner spJumlah;
    private javax.swing.JTable tbCheckout;
    private javax.swing.JTable tbListHp;
    private javax.swing.JLabel textIdHp;
    private javax.swing.JLabel textIdKaryawan;
    private javax.swing.JLabel textSubtotal;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfNamaHp;
    private javax.swing.JTextField tfNamaPelanggan;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
