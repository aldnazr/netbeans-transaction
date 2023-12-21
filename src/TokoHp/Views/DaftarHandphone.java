package TokoHp.Views;

import TokoHp.Objects.Variable;
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
    public static Object[] columnName = {"ID HP", "Brand", "Nama Handphone", "Deskripsi", "Harga", "Stok"};
    DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);
    DefaultComboBoxModel<String> comboBoxBrand = new DefaultComboBoxModel();
    SpinnerNumberModel spHargaModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    SpinnerNumberModel spStokModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);

    public DaftarHandphone() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        setComboBoxBrand();
        setComboBoxFilter();
        spHarga.setModel(spHargaModel);
        spStok.setModel(spStokModel);
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setSearchFieldIcon(tfPencarian);
        Variable.setFontTitle(jLabel9);
    }

    private void setComboBoxFilter() {
        cbFilter.addItem("Stok");
        cbFilter.addItem("Tersedia");
        cbFilter.addItem("Tidak Tersedia");
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
            query = switch (cbFilter.getSelectedIndex()) {
                case 1 ->
                    Variable.sqlFilterPhone("(STOK > 0) AND");
                case 2 ->
                    Variable.sqlFilterPhone("(STOK = 0) AND");
                default ->
                    Variable.sqlFilterPhone("");
            };
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            pstat.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                String[] dataQuery = {
                    rset.getString(1),
                    rset.getString(2),
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
        int idBrand = getIDBrandFromComboBox();
        try {
            query = "INSERT INTO PHONES (ID_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK) VALUES (?,?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setInt(1, idBrand);
            pstat.setString(2, tfNamaHP.getText());
            pstat.setString(3, taDeskripsi.getText());
            pstat.setInt(4, (Integer) spHarga.getValue());
            pstat.setInt(5, (Integer) spStok.getValue());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Berhasil ditambah");
            } else {
                Variable.popUpErrorMessage("Nama handphone sudah ada", "Pastikan membuat nama baru yang tidak sama dengan yang ada di tabel");
            }

            rset.close();
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            System.out.println(ex.getMessage());
        }
    }

    private void updateHp() {
        int idBrand = getIDBrandFromComboBox();
        try {
            query = "UPDATE PHONES SET ID_BRAND = ?, NAMA_HANDPHONE=?, DESKRIPSI=?, HARGA=?, STOK=? WHERE ID_PHONE = ?";
            pstat = connection.prepareStatement(query);
            pstat.setInt(1, idBrand);
            pstat.setString(2, tfNamaHP.getText());
            pstat.setString(3, taDeskripsi.getText());
            pstat.setInt(4, (Integer) spHarga.getValue());
            pstat.setInt(5, (Integer) spStok.getValue());
            pstat.setString(6, textIdHp.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
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
            } else {
                Variable.popUpErrorMessage("Error", "Tidak ada data yang dihapus");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getTableRowData() {
        int row = table.getSelectedRow();
        String idhp, namaBrand, namaHp, deskripsi;
        int harga, stok;

        if (row >= 0) {
            idhp = table.getValueAt(row, 0).toString();
            namaBrand = table.getValueAt(row, 1).toString();
            namaHp = table.getValueAt(row, 2).toString();
            deskripsi = table.getValueAt(row, 3).toString();
            harga = Integer.parseInt(table.getValueAt(row, 4).toString());
            stok = Integer.parseInt(table.getValueAt(row, 5).toString());

            textIdHp.setText(idhp);
            cbBrand.setSelectedItem(namaBrand);
            tfNamaHP.setText(namaHp);
            taDeskripsi.setText(deskripsi);
            spHarga.setValue(harga);
            spStok.setValue(stok);
        }
    }

    private int getIDBrandFromComboBox() {
        int resultQuery = 0;
        try {
            String cbSelectedItem = cbBrand.getSelectedItem().toString();
            query = "SELECT ID_BRAND FROM BRAND WHERE NAMA_BRAND = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, cbSelectedItem);
            rset = pstat.executeQuery();

            if (rset.next()) {
                resultQuery = rset.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return resultQuery;
    }

    private void clearText() {
        textIdHp.setText("0");
        cbBrand.setSelectedIndex(0);
        tfNamaHP.setText("");
        taDeskripsi.setText("");
        spHarga.setValue(0);
        spStok.setValue(0);
        tfPencarian.setText("");
    }

    private boolean cekInput() {
        return tfNamaHP.getText().isEmpty()
                || taDeskripsi.getText().isEmpty()
                || cbBrand.getSelectedIndex() == 0;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        spHarga = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spStok = new javax.swing.JSpinner();
        textIdHp = new javax.swing.JLabel();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btClear = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cbBrand = new javax.swing.JComboBox<>();
        cbFilter = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setPreferredSize(new java.awt.Dimension(1200, 740));

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Pilih Brand");

        tfNamaHP.setColumns(1);

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Nama Handphone");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Deskripsi");

        taDeskripsi.setColumns(1);
        taDeskripsi.setRows(1);
        taDeskripsi.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(taDeskripsi);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Stok");

        textIdHp.setText("0");

        btUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(235, 235, 240));
        btUpdate.setText("Update");
        btUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btDelete.setBackground(new java.awt.Color(255, 59, 48));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(235, 235, 240));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btClear.setBackground(new java.awt.Color(142, 142, 147));
        btClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClear.setForeground(new java.awt.Color(235, 235, 240));
        btClear.setText("Reset");
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        btTambah.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(235, 235, 240));
        btTambah.setText("Tambah");
        btTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DAFTAR HANDPHONE");

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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("ID Phone :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addComponent(tfNamaHP, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(cbBrand, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textIdHp)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel9)
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(textIdHp))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClear)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (cekInput()) {
            Variable.popUpErrorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }
        updateHp();
        setTableRow();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (textIdHp.getText().equals("0")) {
            Variable.popUpErrorMessage("Produk Belum Dipilih", "Maaf, pilih produk yang ingin Anda hapus sebelum melanjutkan. Pastikan Anda memilih produk yang benar untuk dihapus.");
            return;
        }
        deleteHp();
        setTableRow();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
        setTableRow();
    }//GEN-LAST:event_btClearActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (cekInput()) {
            Variable.popUpErrorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }
        tambahHp();
        setTableRow();
    }//GEN-LAST:event_btTambahActionPerformed

    private void cbBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBrandActionPerformed
        getIDBrandFromComboBox();
    }//GEN-LAST:event_cbBrandActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableRowData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTableRow();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterActionPerformed
        setTableRow();
    }//GEN-LAST:event_cbFilterActionPerformed

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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JSpinner spHarga;
    private javax.swing.JSpinner spStok;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTable table;
    private javax.swing.JLabel textIdHp;
    private javax.swing.JTextField tfNamaHP;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
