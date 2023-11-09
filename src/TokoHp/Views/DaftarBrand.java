package TokoHp.Views;

import TokoHp.Function.Variable;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DaftarBrand extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    String query;
    ResultSet rset;
    int rsetInt;
    Object[] columnName = {"Id Brand", "Nama Brand"};
    DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);

    public DaftarBrand() {
        initComponents();
        setClosable(true);
        connection = Variable.koneksi();
        table.setModel(tableModel);
        read();
    }

    private void read() {
        try {
            query = "SELECT * FROM BRAND WHERE LOWER(NAMA_BRAND) LIKE ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] dataQuery = {
                    rset.getString(1),
                    rset.getString(2)
                };
                tableModel.addRow(dataQuery);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void tambahBrand() {
        try {
            query = "INSERT INTO BRAND (NAMA_BRAND) VALUES (?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaBrand.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Berhasil ditambah");
                read();
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            }

            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            System.out.println(ex.getMessage());
        }
    }

    private void updateBrand() {
        try {
            query = "UPDATE BRAND SET NAMA_BRAND = ? WHERE ID_BRAND = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaBrand.getText());
            pstat.setString(2, tfIdBrand.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
                read();
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            }

            pstat.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteBrand() {
        try {
            query = "DELETE BRAND WHERE ID_BRAND = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfIdBrand.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil dihapus");
                read();
            } else {
                Variable.popUpErrorMessage("Error", "Tidak ada data yang dihapus");
            }

            rset.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getTableData() {
        int row = table.getSelectedRow();
        String id_brand, nama_brand;

        if (row >= 0) {
            id_brand = table.getValueAt(row, 0).toString();
            nama_brand = table.getValueAt(row, 1).toString();

            tfIdBrand.setText(id_brand);
            tfNamaBrand.setText(nama_brand);
        }
    }

    private void clearText() {
        tfIdBrand.setText("");
        tfNamaBrand.setText("");
        tfPencarian.setText("");
        read();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tfNamaBrand = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btTambah = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        tfIdBrand = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btDelete = new javax.swing.JButton();
        tfPencarian = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id Brand", "Nama Brand"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        tfNamaBrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfNamaBrandKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Daftar Brand");

        btTambah.setBackground(new java.awt.Color(102, 153, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("Tambah");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btUpdate.setBackground(new java.awt.Color(102, 153, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btUpdate.setText("Update");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        tfIdBrand.setEditable(false);

        jLabel2.setText("Nama Brand");

        jLabel3.setText("Id Brand");

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(255, 255, 255));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel4.setText("Cari");

        btClear.setBackground(new java.awt.Color(153, 153, 153));
        btClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClear.setForeground(new java.awt.Color(255, 255, 255));
        btClear.setText("Clear");
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(tfNamaBrand)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfIdBrand)
                            .addComponent(jLabel3)
                            .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfIdBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btClear)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (!tfNamaBrand.getText().isEmpty()) {
            tambahBrand();
        } else {
            Variable.popUpErrorMessage("Error", "Tidak ada data ditambah");
        }
    }//GEN-LAST:event_btTambahActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (!tfIdBrand.getText().isEmpty()) {
            deleteBrand();
        } else {
            Variable.popUpErrorMessage("Error", "Tidak ada data dihapus");
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (!tfNamaBrand.getText().isEmpty()) {
            updateBrand();
        } else {
            Variable.popUpErrorMessage("Error", "Data gagal diupdate");
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        read();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableData();
    }//GEN-LAST:event_tableMouseClicked

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
    }//GEN-LAST:event_btClearActionPerformed

    private void tfNamaBrandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNamaBrandKeyTyped
        Variable.capitalizeFirstLetter(tfNamaBrand);
    }//GEN-LAST:event_tfNamaBrandKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JTextField tfIdBrand;
    private javax.swing.JTextField tfNamaBrand;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
