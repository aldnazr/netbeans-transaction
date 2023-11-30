package TokoHp.Views;

import TokoHp.Objects.Variable;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DaftarKaryawan extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pstat;
    String query;
    ResultSet rset;
    int rsetInt;
    String gender, tipeAkun;
    String[] columntableModelValue = new String[]{"ID User", "Nama", "Tanggal lahir", "Jenis Kelamin", "Alamat", "Email", "Telepon", "Tipe akun", "Username", "Password"};
    DefaultTableModel tableModel = new DefaultTableModel(columntableModelValue, 0);
    JTextField dateChooserTextField;

    public DaftarKaryawan() {
        initComponents();
        setClosable(true);
        connection = Variable.koneksi();
        read();
        table.setModel(tableModel);
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setPasswordFieldRevealButton(passwordField);
        dateChooser.setDateFormatString("dd-MM-yyyy");
        setButtonGroup();
        dateChooserTextField = Variable.disableDateTextfield(dateChooser);
    }

    private void getSelectedRadio() {
        gender = rbLaki.isSelected() ? rbLaki.getText() : rbPerempuan.getText();
        tipeAkun = rbKaryawan.isSelected() ? rbKaryawan.getText() : rbAdmin.getText();
    }

    private void setButtonGroup() {
        buttonGroup1.add(rbLaki);
        buttonGroup1.add(rbPerempuan);
        buttonGroup2.add(rbKaryawan);
        buttonGroup2.add(rbAdmin);
        rbLaki.setSelected(true);
        rbKaryawan.setSelected(true);
    }

    private void read() {
        try {
            query = "SELECT * FROM USERS WHERE LOWER(NAMA_LENGKAP) LIKE ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, "%" + tfPencarian.getText().toLowerCase() + "%");
            rset = pstat.executeQuery();
            tableModel.setRowCount(0);

            while (rset.next()) {
                Object[] dataQuery = {
                    rset.getString(1),
                    rset.getString(2),
                    rset.getDate(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6),
                    rset.getString(7),
                    rset.getString(8),
                    rset.getString(9),
                    rset.getString(10)
                };
                tableModel.addRow(dataQuery);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void tambahKaryawan() {
        getSelectedRadio();
        try {
            query = "INSERT INTO USERS (NAMA_LENGKAP, TANGGAL_LAHIR, GENDER, ALAMAT, EMAIL, NO_TELP, TIPE_AKUN, USERNAME, PASSWORD) VALUES (?,?,?,?,?,?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            pstat.setString(3, gender);
            pstat.setString(4, taAlamat.getText());
            pstat.setString(5, tfEmail.getText());
            pstat.setString(6, tfPhone.getText());
            pstat.setString(7, tipeAkun);
            pstat.setString(8, tfUsername.getText());
            pstat.setString(9, String.valueOf(passwordField.getPassword()));
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Berhasil ditambah");
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            }

            pstat.close();
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            System.err.println(ex.getMessage());
        }
    }

    private void updateKaryawan() {
        getSelectedRadio();
        try {
            query = "UPDATE USERS SET NAMA_LENGKAP=?, TANGGAL_LAHIR=?, GENDER=?, ALAMAT=?, EMAIL=?, NO_TELP=?, TIPE_AKUN=?, USERNAME=?, PASSWORD=? WHERE ID_USER=?";
            pstat = connection.prepareStatement(query);
            pstat.setString(10, textIdKaryawan.getText());
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            pstat.setString(3, gender);
            pstat.setString(4, taAlamat.getText());
            pstat.setString(5, tfEmail.getText());
            pstat.setString(6, tfPhone.getText());
            pstat.setString(7, tipeAkun);
            pstat.setString(8, tfUsername.getText());
            pstat.setString(9, String.valueOf(passwordField.getPassword()));
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diperbarui");
            } else {
                Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void deleteKaryawan() {
        try {
            query = "DELETE USERS WHERE ID_USER = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textIdKaryawan.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil dihapus");
            } else {
                Variable.popUpErrorMessage("Error", "Tidak ada data yang dihapus");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getTableData() {
        int row = table.getSelectedRow();
        java.util.Date tgl_lahir;
        String id, nama, genderFromTable, alamat, email, telepon, tipe_akun, username, password;

        if (row >= 0) {
            id = table.getValueAt(row, 0).toString();
            nama = table.getValueAt(row, 1).toString();
            tgl_lahir = (java.util.Date) table.getValueAt(row, 2);
            genderFromTable = table.getValueAt(row, 3).toString();
            alamat = table.getValueAt(row, 4).toString();
            email = table.getValueAt(row, 5).toString();
            telepon = table.getValueAt(row, 6).toString();
            tipe_akun = table.getValueAt(row, 7).toString();
            username = table.getValueAt(row, 8).toString();
            password = table.getValueAt(row, 9).toString();

            textIdKaryawan.setText(id);
            tfNamaKaryawan.setText(nama);
            dateChooser.setDate(tgl_lahir);
            taAlamat.setText(alamat);
            tfEmail.setText(email);
            tfPhone.setText(telepon);
            tfUsername.setText(username);
            passwordField.setText(password);

            if (genderFromTable.equals("Laki-laki")) {
                rbLaki.setSelected(true);
            } else {
                rbPerempuan.setSelected(true);
            }

            if (tipe_akun.equals("Admin")) {
                rbAdmin.setSelected(true);
            } else {
                rbKaryawan.setSelected(true);
            }
        }
    }

    private void clearText() {
        textIdKaryawan.setText("0");
        tfNamaKaryawan.setText("");
        dateChooser.setDate(null);
        tfPencarian.setText("");
        taAlamat.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfUsername.setText("");
        passwordField.setText("");
        rbLaki.setSelected(true);
        rbKaryawan.setSelected(true);
    }

    private boolean checkInput() {
        return !tfNamaKaryawan.getText().isEmpty()
                && !dateChooserTextField.getText().isEmpty()
                && !taAlamat.getText().isEmpty()
                && !tfEmail.getText().isEmpty()
                && !tfPhone.getText().isEmpty()
                && !tfUsername.getText().isEmpty()
                && passwordField.getPassword().length > 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        tfNamaKaryawan = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taAlamat = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        tfUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        textIdKaryawan = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        tfPhone = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        rbLaki = new javax.swing.JRadioButton();
        rbKaryawan = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        rbAdmin = new javax.swing.JRadioButton();

        jLabel6.setText("Nama lengkap");

        setPreferredSize(new java.awt.Dimension(1200, 730));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Nama lengkap");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Jenis Kelamin");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Alamat");

        taAlamat.setColumns(20);
        taAlamat.setRows(5);
        jScrollPane1.setViewportView(taAlamat);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Email");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Username");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Password");

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
        jScrollPane2.setViewportView(table);

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Daftar Karyawan");

        btClear.setBackground(new java.awt.Color(142, 142, 147));
        btClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btClear.setForeground(new java.awt.Color(255, 255, 255));
        btClear.setText("Reset");
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        btTambah.setBackground(new java.awt.Color(0, 122, 255));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("Tambah");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btUpdate.setBackground(new java.awt.Color(0, 122, 255));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btUpdate.setText("Update");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btDelete.setBackground(new java.awt.Color(255, 59, 48));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(255, 255, 255));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        textIdKaryawan.setText("0");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Tipe akun");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Tanggal lahir");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Telepon");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("ID Karyawan");

        rbLaki.setText("Laki-laki");

        rbKaryawan.setText("Karyawan");

        rbPerempuan.setText("Perempuan");

        rbAdmin.setText("Admin");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfUsername)
                    .addComponent(tfEmail)
                    .addComponent(passwordField)
                    .addComponent(tfPhone)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfNamaKaryawan)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbKaryawan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbAdmin))
                            .addComponent(textIdKaryawan)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbLaki)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbPerempuan)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(562, 562, 562)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textIdKaryawan)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNamaKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbLaki)
                            .addComponent(jLabel2)
                            .addComponent(rbPerempuan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbKaryawan)
                            .addComponent(rbAdmin)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClear))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
        read();
    }//GEN-LAST:event_btClearActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (checkInput()) {
            tambahKaryawan();
            read();
        } else {
            Variable.popUpErrorMessage("Error", "Harap isi semua data");
        }
    }//GEN-LAST:event_btTambahActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (checkInput()) {
            updateKaryawan();
            read();
        } else {
            Variable.popUpErrorMessage("Error", "Data gagal diupdate");
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (!textIdKaryawan.getText().isEmpty()) {
            deleteKaryawan();
            read();
        } else {
            Variable.popUpErrorMessage("Error", "Tidak ada data dihapus");
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        read();
    }//GEN-LAST:event_tfPencarianKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JRadioButton rbAdmin;
    private javax.swing.JRadioButton rbKaryawan;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JTextArea taAlamat;
    private javax.swing.JTable table;
    private javax.swing.JLabel textIdKaryawan;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfNamaKaryawan;
    private javax.swing.JTextField tfPencarian;
    private javax.swing.JTextField tfPhone;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
