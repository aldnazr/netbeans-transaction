package TokoHp.Views;

import TokoHp.Function.Variable;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class DaftarHandphone extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    String query;
    Statement stat;
    ResultSet rset;
    int rsetInt;
    public static Object[] columnName = {"ID HP", "Id Brand", "Nama Brand", "Model", "Deskripsi", "Harga", "Stok"};
    DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);
    DefaultComboBoxModel<String> comboBoxBrand = new DefaultComboBoxModel();
    SpinnerNumberModel spHargaModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    SpinnerNumberModel spStokModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);

    public DaftarHandphone() {
        initComponents();
        setClosable(true);
        connection = Variable.koneksi();
        setComboBoxBrand();
        setComboBoxFilter();
        spHarga.setModel(spHargaModel);
        spStok.setModel(spStokModel);
        textIdHp.setVisible(false);
        textIdBrand.setVisible(false);
    }

    private void setComboBoxFilter() {
        cbFilter.addItem("Semua");
        cbFilter.addItem("Tersedia");
        cbFilter.addItem("Tidak tersedia");
    }

    private void setComboBoxBrand() {
        try {
            query = "SELECT NAMA_BRAND FROM BRAND";
            stat = connection.createStatement();
            rset = stat.executeQuery(query);

            comboBoxBrand.addElement("");

            while (rset.next()) {
                String brandName = rset.getString(1);

                comboBoxBrand.addElement(brandName);
            }
            cbBrand.setModel(comboBoxBrand);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setTableRow() {
        try {
            query = "SELECT * FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            pstat.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] dataQuery = {
                    rset.getString(2),
                    rset.getString(1),
                    rset.getString(7),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6)
                };
                tableModel.addRow(dataQuery);
            }
            table.setModel(tableModel);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setTableRowAvailable() {
        try {
            query = "SELECT * FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK > 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            pstat.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] dataQuery = {
                    rset.getString(2),
                    rset.getString(1),
                    rset.getString(7),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6)
                };
                tableModel.addRow(dataQuery);
            }
            table.setModel(tableModel);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setTableRowNotAvailable() {
        try {
            query = "SELECT * FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK = 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            pstat.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] dataQuery = {
                    rset.getString(2),
                    rset.getString(1),
                    rset.getString(7),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6)
                };
                tableModel.addRow(dataQuery);
            }
            table.setModel(tableModel);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void tambahHp() {
        try {
            query = "INSERT INTO PHONES (ID_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK) VALUES (?,?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textIdBrand.getText());
            pstat.setString(2, tfNamaHP.getText());
            pstat.setString(3, taDeskripsi.getText());
            pstat.setInt(4, (Integer) spHarga.getValue());
            pstat.setInt(5, (Integer) spStok.getValue());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Berhasil ditambah");
                setTableRow();
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            }

            rset.close();
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            System.out.println(ex.getMessage());
        }
    }

    private void updateHp() {
        try {
            query = "UPDATE PHONES SET ID_BRAND = ?, NAMA_HANDPHONE=?, DESKRIPSI=?, HARGA=?, STOK=? WHERE ID_PHONE = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textIdBrand.getText());
            pstat.setString(2, tfNamaHP.getText());
            pstat.setString(3, taDeskripsi.getText());
            pstat.setInt(4, (Integer) spHarga.getValue());
            pstat.setInt(5, (Integer) spStok.getValue());
            pstat.setString(6, textIdHp.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
                setTableRow();
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            }
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            System.out.println(ex.getMessage());
        }
    }

    private void deleteHp() {
        try {
            query = "DELETE PHONES WHERE ID_PHONE = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textIdHp.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil dihapus");
                setTableRow();
            } else {
                Variable.popUpErrorMessage("Error", "Tidak ada data yang dihapus");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getTableRowData() {
        int row = table.getSelectedRow();
        String idhp, idbrand, namaBrand, namaHp, deskripsi;
        int harga, stok;

        if (row >= 0) {
            idhp = table.getValueAt(row, 0).toString();
            idbrand = table.getValueAt(row, 1).toString();
            namaBrand = table.getValueAt(row, 2).toString();
            namaHp = table.getValueAt(row, 3).toString();
            deskripsi = table.getValueAt(row, 4).toString();
            harga = Integer.parseInt(table.getValueAt(row, 5).toString());
            stok = Integer.parseInt(table.getValueAt(row, 6).toString());

            textIdHp.setText(idhp);
            textIdBrand.setText(idbrand);
            cbBrand.setSelectedItem(namaBrand);
            tfNamaHP.setText(namaHp);
            taDeskripsi.setText(deskripsi);
            spHarga.setValue(harga);
            spStok.setValue(stok);
        }
    }

    private void getBrandId() {
        try {
            String cbSelectedItem = cbBrand.getSelectedItem().toString();
            query = "SELECT ID_BRAND FROM BRAND WHERE NAMA_BRAND = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, cbSelectedItem);
            rset = pstat.executeQuery();

            if (rset.next()) {
                int brandId = rset.getInt(1);
                String brandIDString = String.valueOf(brandId);
                textIdBrand.setText(brandIDString);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getCbFilterActiveIndex() {
        switch (cbFilter.getSelectedIndex()) {
            case 0:
                setTableRow();
                break;
            case 1:
                setTableRowAvailable();
                break;
            case 2:
                setTableRowNotAvailable();
                break;
        }
    }

    private void clearText() {
        textIdHp.setText("");
        textIdBrand.setText("");
        cbBrand.setSelectedIndex(0);
        tfNamaHP.setText("");
        taDeskripsi.setText("");
        spHarga.setValue(0);
        spStok.setValue(0);
        tfPencarian.setText("");
        setTableRow();
    }

    private boolean cekInput() {
        return !tfNamaHP.getText().isEmpty()
                && !taDeskripsi.getText().isEmpty()
                && cbBrand.getSelectedIndex() != 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        tfNamaHP = new javax.swing.JTextField();
        tfPencarian = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        spHarga = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spStok = new javax.swing.JSpinner();
        textIdBrand = new javax.swing.JLabel();
        textIdHp = new javax.swing.JLabel();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btClear = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cbBrand = new javax.swing.JComboBox<>();
        cbFilter = new javax.swing.JComboBox<>();

        jTextField1.setText("jTextField1");

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jLabel1.setText("Pilih Brand");

        tfPencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPencarianActionPerformed(evt);
            }
        });
        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel8.setText("Cari");

        jLabel2.setText("Nama Handphone");

        jLabel3.setText("Deskripsi");

        taDeskripsi.setColumns(20);
        taDeskripsi.setRows(5);
        jScrollPane2.setViewportView(taDeskripsi);

        jLabel4.setText("Harga");

        jLabel5.setText("Stok");

        textIdBrand.setText("id_brand");

        textIdHp.setText("id_hp");

        btUpdate.setBackground(new java.awt.Color(102, 153, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btUpdate.setText("Update");
        btUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(255, 255, 255));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btClear.setBackground(new java.awt.Color(153, 153, 153));
        btClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClear.setForeground(new java.awt.Color(255, 255, 255));
        btClear.setText("Clear");
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        btTambah.setBackground(new java.awt.Color(102, 153, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("Tambah");
        btTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Daftar Handphone");

        cbBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBrandActionPerformed(evt);
            }
        });

        cbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(textIdHp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textIdBrand))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(spStok)
                    .addComponent(spHarga)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(cbBrand, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfNamaHP, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textIdBrand)
                    .addComponent(textIdHp))
                .addGap(30, 30, 30)
                .addComponent(jLabel9)
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btClear)
                        .addGap(0, 84, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (cekInput()) {
            updateHp();
        } else {
            Variable.popUpErrorMessage("Error", "Data gagal diupdate");
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (!textIdHp.getText().isEmpty()) {
            deleteHp();
        } else {
            Variable.popUpErrorMessage("Error", "Tidak ada data dihapus");
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
    }//GEN-LAST:event_btClearActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (cekInput()) {
            tambahHp();
        } else {
            Variable.popUpErrorMessage("Error", "Tidak ada data ditambah");
        }
    }//GEN-LAST:event_btTambahActionPerformed

    private void cbBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBrandActionPerformed
        getBrandId();
    }//GEN-LAST:event_cbBrandActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableRowData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        getCbFilterActiveIndex();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterActionPerformed
        getCbFilterActiveIndex();
    }//GEN-LAST:event_cbFilterActionPerformed

    private void tfPencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPencarianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPencarianActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<String> cbBrand;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JSpinner spHarga;
    private javax.swing.JSpinner spStok;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTable table;
    private javax.swing.JLabel textIdBrand;
    private javax.swing.JLabel textIdHp;
    private javax.swing.JTextField tfNamaHP;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
