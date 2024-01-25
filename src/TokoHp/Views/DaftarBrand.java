package TokoHp.Views;

import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.capitalizeFirstLetter;
import static TokoHp.Objects.Variable.koneksi;
import static TokoHp.Objects.Variable.popUpErrorMessage;
import static TokoHp.Objects.Variable.popUpSuccessMessage;
import static TokoHp.Objects.Variable.setPlaceholderTextfield;
import static TokoHp.Objects.Variable.setSearchFieldIcon;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DaftarBrand extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    CallableStatement cstat;
    String query;
    ResultSet rset;
    int rsetInt;
    DefaultTableModel tableModel;

    public DaftarBrand() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = koneksi();
        setTable();
        setPlaceholderTextfield(tfPencarian, "Cari");
        tfNamaBrand.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        setSearchFieldIcon(tfPencarian);
        card1.setForeground(!Variable.isDarkTheme() ? Color.decode("#E6E6E6") : Color.decode("#303030"));
    }

    private void setTable() {
        tableModel = (DefaultTableModel) table.getModel();
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
            table.setModel(tableModel);

            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void tambahBrand() {
        try {
            query = "CALL insertBrand(?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, tfNamaBrand.getText());
            rsetInt = cstat.executeUpdate();

            if (rsetInt >= 0) {
                popUpSuccessMessage("Berhasil", "Berhasil ditambah");
                setTable();
            }

            cstat.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            if (ex.getMessage().contains("ORA-00001")) {
                popUpErrorMessage("Data Sudah Ada", "Data dengan nama yang sama sudah ada dalam sistem. Mohon pilih nama lain atau perbarui data yang sudah ada.");
            } else {
                popUpErrorMessage("Error", "Data gagal ditambah");
            }
        }
    }

    private void updateBrand() {
        try {
            query = "CALL updateBrand(?, ?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, textIdBrand.getText());
            cstat.setString(2, tfNamaBrand.getText());
            rsetInt = cstat.executeUpdate();

            if (rsetInt >= 0) {
                popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
                setTable();
            }

            cstat.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            popUpErrorMessage("Error", "Data gagal diupdate");
        }
    }

    private void deleteBrand() {
        try {
            query = "CALL deleteBrand(?)";
            cstat = connection.prepareCall(query);
            cstat.setString(1, textIdBrand.getText());
            rsetInt = cstat.executeUpdate();

            if (rsetInt > 0) {
                popUpSuccessMessage("Berhasil", "Data berhasil dihapus");
                setTable();
            }

            cstat.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            if (ex.getMessage().contains("violated - child record found")) {
                popUpErrorMessage("Error", "Brand sedang digunakan untuk produk, Anda bisa menghapus setelah menghapus seluruh produk yang ketergantungan");
            } else {
                popUpErrorMessage("Error", "Data gagal dihapus");
            }
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
        textIdBrand.setText("0");
        tfNamaBrand.setText("");
        tfPencarian.setText("");
    }

    private boolean checkInput() {
        return textIdBrand.getText().equals("0")
                || tfNamaBrand.getText().isEmpty();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        card1 = new TokoHp.Component.Card();
        jLabel2 = new javax.swing.JLabel();
        textIdBrand = new javax.swing.JLabel();
        btDelete = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btDelete1 = new javax.swing.JButton();
        tfNamaBrand = new javax.swing.JTextField();
        btTambah = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();

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
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(0).setMaxWidth(120);
        }

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        card1.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.pressedBackground"));
        card1.setCorner(26);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nama Brand :");

        textIdBrand.setText("0");

        btDelete.setBackground(new java.awt.Color(255, 59, 48));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.default.foreground"));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ID brand :");

        btDelete1.setBackground(new java.awt.Color(142, 142, 147));
        btDelete1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete1.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.default.foreground"));
        btDelete1.setText("Reset");
        btDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelete1ActionPerformed(evt);
            }
        });

        tfNamaBrand.setColumns(1);
        tfNamaBrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfNamaBrandKeyTyped(evt);
            }
        });

        btTambah.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.default.foreground"));
        btTambah.setText("Tambah");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.default.foreground"));
        btUpdate.setText("Update");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textIdBrand))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(tfNamaBrand, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(card1Layout.createSequentialGroup()
                            .addComponent(btTambah)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btUpdate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btDelete1))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textIdBrand)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNamaBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btUpdate)
                    .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btTambah)
                        .addComponent(btDelete)
                        .addComponent(btDelete1)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        btDelete.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (checkInput()) {
            popUpErrorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }

        tambahBrand();
        setTable();
    }//GEN-LAST:event_btTambahActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (textIdBrand.getText().equals("0")) {
            popUpErrorMessage("Brand Belum Dipilih", "Maaf, pilih brand yang ingin Anda hapus sebelum melanjutkan. Pastikan Anda memilih brand yang benar untuk dihapus.");
            return;
        }

        deleteBrand();
        setTable();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (checkInput()) {
            popUpErrorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }

        updateBrand();
        setTable();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTable();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfNamaBrandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNamaBrandKeyTyped
        capitalizeFirstLetter(tfNamaBrand);
    }//GEN-LAST:event_tfNamaBrandKeyTyped

    private void btDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelete1ActionPerformed
        clearText();
        setTable();
    }//GEN-LAST:event_btDelete1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btDelete1;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private TokoHp.Component.Card card1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JLabel textIdBrand;
    private javax.swing.JTextField tfNamaBrand;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
