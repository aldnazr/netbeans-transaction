package TokoHp.Views;

import TokoHp.Objects.Variable;
import com.formdev.flatlaf.FlatClientProperties;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DaftarBrand extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    CallableStatement cstat;
    String query;
    ResultSet rset;
    int rsetInt;
    Object[] columnName = {"ID Brand", "Nama Brand"};
    DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);

    public DaftarBrand() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        table.setModel(tableModel);
        read();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        textIdBrand.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        Variable.setLabelFont(jLabel1);
    }

    private void read() {
        try {
            query = "SELECT * FROM BRAND WHERE LOWER(NAMA_BRAND) LIKE ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] rowData = {
                    rset.getInt(1),
                    rset.getString(2)
                };
                tableModel.addRow(rowData);
            }
            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void tambahBrand() {
        try {
            query = "CALL insertBrand (?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, tfNamaBrand.getText());
            rsetInt = cstat.executeUpdate();

            if (rsetInt >= 0) {
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
            query = "CALL updateBrand (?, ?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, textIdBrand.getText());
            cstat.setString(2, tfNamaBrand.getText());
            rsetInt = cstat.executeUpdate();

            if (rsetInt >= 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
                read();
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            }

            pstat.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteBrand() {
        try {
            query = "CALL deleteBrand (?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, textIdBrand.getText());
            rsetInt = cstat.executeUpdate();

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

            textIdBrand.setText(id_brand);
            tfNamaBrand.setText(nama_brand);
        }
    }

    private void clearText() {
        textIdBrand.setText("");
        tfPencarian.setText("");
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
        jLabel2 = new javax.swing.JLabel();
        textIdBrand = new javax.swing.JLabel();
        btDelete = new javax.swing.JButton();
        tfPencarian = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btDelete1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Brand", "Nama Brand"
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
        jLabel1.setText("DAFTAR BRAND");

        btTambah.setBackground(new java.awt.Color(10, 132, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(235, 235, 240));
        btTambah.setText("Tambah");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btUpdate.setBackground(new java.awt.Color(10, 132, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(235, 235, 240));
        btUpdate.setText("Update");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Nama Brand");

        textIdBrand.setText("0");

        btDelete.setBackground(new java.awt.Color(255, 59, 48));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(235, 235, 240));
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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("ID brand :");

        btDelete1.setBackground(new java.awt.Color(142, 142, 147));
        btDelete1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete1.setForeground(new java.awt.Color(235, 235, 240));
        btDelete1.setText("Reset");
        btDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelete1ActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textIdBrand))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btDelete1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(46, 46, 46)
                                            .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(tfNamaBrand, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(149, 149, 149)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textIdBrand)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btDelete1)
                        .addGap(93, 93, 93)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        btDelete.getAccessibleContext().setAccessibleName("");

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
        if (!textIdBrand.getText().isEmpty()) {
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

    private void tfNamaBrandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNamaBrandKeyTyped
        Variable.capitalizeFirstLetter(tfNamaBrand);
    }//GEN-LAST:event_tfNamaBrandKeyTyped

    private void btDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelete1ActionPerformed
        clearText();
        read();
    }//GEN-LAST:event_btDelete1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btDelete1;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JLabel textIdBrand;
    private javax.swing.JTextField tfNamaBrand;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
