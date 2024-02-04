package TokoHp.Views;

import TokoHp.Objects.PopUp;
import TokoHp.Objects.QueryBuilder;
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
    JTextField dateChooserTextField;

    public DaftarKaryawan() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        setTable();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setPasswordFieldRevealButton(passwordField);
        setButtonGroup();
        dateChooserTextField = Variable.disableDateTextfield(dateChooser);
        Variable.setSearchFieldIcon(tfPencarian);
        tfNamaKaryawan.setColumns(10);
    }

    private void setButtonGroup() {
        buttonGroup1.add(rbLaki);
        buttonGroup1.add(rbPerempuan);
        buttonGroup2.add(rbKaryawan);
        buttonGroup2.add(rbAdmin);
        rbLaki.setSelected(true);
        rbKaryawan.setSelected(true);
    }

    private void setTable() {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            query = switch (cbFilter.getSelectedIndex()) {
                case 1 ->
                    QueryBuilder.daftarKaryawan("TIPE_AKUN = 'Admin' AND");
                case 2 ->
                    QueryBuilder.daftarKaryawan("TIPE_AKUN = 'Karyawan' AND");
                default ->
                    QueryBuilder.daftarKaryawan("");
            };
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
                    rset.getString(9)
                };
                tableModel.addRow(dataQuery);
            }
            table.setModel(tableModel);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void tambahKaryawan() {
        try {
            query = """
                    INSERT INTO USERS (
                    NAMA_LENGKAP,
                    TANGGAL_LAHIR,
                    GENDER, ALAMAT,
                    EMAIL,
                    NO_TELP,
                    TIPE_AKUN,
                    USERNAME,
                    PASSWORD) VALUES (?,?,?,?,?,?,?,?,?)""";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            pstat.setString(3, rbLaki.isSelected() ? rbLaki.getText() : rbPerempuan.getText());
            pstat.setString(4, taAlamat.getText());
            pstat.setString(5, tfEmail.getText());
            pstat.setString(6, tfPhone.getText());
            pstat.setString(7, rbKaryawan.isSelected() ? rbKaryawan.getText() : rbAdmin.getText());
            pstat.setString(8, tfUsername.getText());
            pstat.setString(9, String.valueOf(passwordField.getPassword()));
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                PopUp.successMessage("Berhasil", "Berhasil ditambah");
            }

            pstat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            if (ex.getMessage().contains("ORA-00001")) {
                PopUp.errorMessage("Data Sudah Ada", "Data dengan nama yang sama sudah ada dalam sistem. Mohon pilih nama lain atau perbarui data yang sudah ada.");
            } else {
                PopUp.errorMessage("Error", "Data gagal ditambah");
            }
        }
    }

    private void updateKaryawan() {
        String isMan = rbLaki.isSelected() ? rbLaki.getText() : rbPerempuan.getText();
        String isAdmin = rbKaryawan.isSelected() ? rbKaryawan.getText() : rbAdmin.getText();
        try {
            query = """
                    UPDATE USERS SET NAMA_LENGKAP=?,
                    TANGGAL_LAHIR=?,
                    GENDER=?,
                    ALAMAT=?,
                    EMAIL=?,
                    NO_TELP=?,
                    TIPE_AKUN=?,
                    USERNAME=?,
                    PASSWORD=?
                    WHERE ID_USER=?""";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            pstat.setString(3, isMan);
            pstat.setString(4, taAlamat.getText());
            pstat.setString(5, tfEmail.getText());
            pstat.setString(6, tfPhone.getText());
            pstat.setString(7, isAdmin);
            pstat.setString(8, tfUsername.getText());
            pstat.setString(9, String.valueOf(passwordField.getPassword()));
            pstat.setString(10, textIdKaryawan.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                PopUp.successMessage("Berhasil", "Data berhasil diperbarui");
            }

            pstat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            PopUp.errorMessage("Error", "Data gagal diupdate");
        }
    }

    private void updateKaryawanWithoutPassword() {
        String isMan = rbLaki.isSelected() ? rbLaki.getText() : rbPerempuan.getText();
        String isAdmin = rbKaryawan.isSelected() ? rbKaryawan.getText() : rbAdmin.getText();
        try {
            query = """
                    UPDATE USERS SET NAMA_LENGKAP=?,
                    TANGGAL_LAHIR=?,
                    GENDER=?,
                    ALAMAT=?,
                    EMAIL=?,
                    NO_TELP=?,
                    TIPE_AKUN=?,
                    USERNAME=?
                    WHERE ID_USER=?""";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            pstat.setString(3, isMan);
            pstat.setString(4, taAlamat.getText());
            pstat.setString(5, tfEmail.getText());
            pstat.setString(6, tfPhone.getText());
            pstat.setString(7, isAdmin);
            pstat.setString(8, tfUsername.getText());
            pstat.setString(9, textIdKaryawan.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                PopUp.successMessage("Berhasil", "Data berhasil diperbarui");
            }

            pstat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            PopUp.errorMessage("Error", "Data gagal diupdate");
        }
    }

    private void deleteKaryawan() {
        try {
            query = "DELETE USERS WHERE ID_USER = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textIdKaryawan.getText());
            rsetInt = pstat.executeUpdate();

            if (rsetInt > 0) {
                PopUp.successMessage("Berhasil", "Data berhasil dihapus");
            }

            pstat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            if (ex.getMessage().contains("violated - child record found")) {
                PopUp.errorMessage("Error", "Karyawan sedang digunakan untuk riwayat transaksi");
            } else {
                PopUp.errorMessage("Error", "Tidak ada data yang dihapus");
            }
        }
    }

    private void getTableData() {
        int row = table.getSelectedRow();
        java.util.Date tgl_lahir;
        String id, nama, genderFromTable, alamat, email, telepon, tipe_akun, username;

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

            textIdKaryawan.setText(id);
            tfNamaKaryawan.setText(nama);
            dateChooser.setDate(tgl_lahir);
            taAlamat.setText(alamat);
            tfEmail.setText(email);
            tfPhone.setText(telepon);
            tfUsername.setText(username);

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
            dateChooserTextField = Variable.disableDateTextfield(dateChooser);
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
        return tfNamaKaryawan.getText().isEmpty()
                || dateChooserTextField.getText().isEmpty()
                || taAlamat.getText().isEmpty()
                || tfEmail.getText().isEmpty()
                || tfPhone.getText().isEmpty()
                || tfUsername.getText().isEmpty();
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
        cbFilter = new javax.swing.JComboBox<>();

        jLabel6.setText("Nama lengkap");

        setPreferredSize(new java.awt.Dimension(1200, 740));

        tfNamaKaryawan.setColumns(10);

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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID User", "Nama", "Tanggal Lahir", "Jenis Kelamin", "Alamat", "Email", "Telepon", "Jabatan", "Username"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(70);
            table.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
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
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btUpdate.setForeground(new java.awt.Color(235, 235, 240));
        btUpdate.setText("Update");
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

        textIdKaryawan.setText("0");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Jabatan");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Tanggal lahir");

        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateChooserMouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Telepon");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("ID Karyawan");

        rbLaki.setText("Laki-laki");

        rbKaryawan.setText("Karyawan");

        rbPerempuan.setText("Perempuan");

        rbAdmin.setText("Admin");

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Admin", "Karyawan" }));
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(29, 29, 29)
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
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
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
                        .addGap(87, 87, 87)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btClear)
                        .addGap(0, 33, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
        setTable();
    }//GEN-LAST:event_btClearActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (checkInput() || passwordField.getPassword().length == 0) {
            PopUp.errorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }

        tambahKaryawan();
        setTable();
    }//GEN-LAST:event_btTambahActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (checkInput()) {
            PopUp.errorMessage("Kesalahan Pengisian Formulir", "Mohon lengkapi semua bagian formulir sebelum melanjutkan. Pastikan setiap kolom terisi dengan informasi yang diperlukan.");
            return;
        }

        if (passwordField.getPassword().length == 0) {
            updateKaryawanWithoutPassword();
            setTable();
            return;
        }

        updateKaryawan();
        setTable();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (textIdKaryawan.getText().equals("0")) {
            PopUp.errorMessage("Akun Belum Dipilih", "Maaf, pilih akun yang ingin Anda hapus sebelum melanjutkan. Pastikan Anda memilih akun yang benar untuk dihapus.");
            return;
        }

        deleteKaryawan();
        setTable();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTable();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void dateChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooserMouseClicked
        dateChooserTextField = Variable.disableDateTextfield(dateChooser);
    }//GEN-LAST:event_dateChooserMouseClicked

    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterActionPerformed
        setTable();
    }//GEN-LAST:event_cbFilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cbFilter;
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
