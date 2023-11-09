
package TokoHp.Views;

import TokoHp.Function.Variable;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DaftarKaryawan extends javax.swing.JInternalFrame {
    
    Connection connection;
    PreparedStatement pstat;
    String query;
    ResultSet rset;
    int rsetInt;
    String[] columntableModelValue = new String[] {"Id User", "Nama", "Jenis Kelamin", "Alamat", "Email", "Tipe akun", "Username", "Password"};
    DefaultTableModel tableModel = new DefaultTableModel(columntableModelValue, 0);
    
    public DaftarKaryawan() {
        initComponents();
        setClosable(true);
        connection = Variable.koneksi();
        setComboBoxItem();
        read();
        textId.setVisible(false);
        table.setModel(tableModel);
    }
    
    private void setComboBoxItem(){
        String[] JenisKelaminItems = new String[]{"Laki-laki", "Perempuan"};
        String[] TipeAkunItems = new String[]{"Karyawan", "Admin"};
        
        for (String item : JenisKelaminItems) {
            cbKelamin.addItem(item);
        }
        
        
        for (String item : TipeAkunItems) {
            cbTipeAkun.addItem(item);
        }
    }
    
    private void read (){
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
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6),
                    rset.getString(7),
                    rset.getString(8)
                };
            tableModel.addRow(dataQuery);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void tambahKaryawan(){
        try {
            query = "INSERT INTO USERS (NAMA_LENGKAP, GENDER, ALAMAT, EMAIL, TIPE_AKUN, USERNAME, PASSWORD) VALUES (?,?,?,?,?,?,?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setString(2, cbKelamin.getSelectedItem().toString());
            pstat.setString(3, taAlamat.getText());
            pstat.setString(4, tfEmail.getText());
            pstat.setString(5, cbTipeAkun.getSelectedItem().toString());
            pstat.setString(6, tfUsername.getText());
            pstat.setString(7, tfPassword.getText());
            rset = pstat.executeQuery();
            
            if (rset.next()){
                Variable.popUpSuccessMessage("Berhasil", "Berhasil ditambah");
                read();
            } else{
                Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            }
            
            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            Variable.popUpErrorMessage("Error", "Data gagal ditambah");
            System.out.println(ex.getMessage());
        }
    }
    
    private void updateKaryawan(){
        try {
            query = "UPDATE USERS SET NAMA_LENGKAP=?, GENDER=?, ALAMAT=?, EMAIL=?, TIPE_AKUN=?, USERNAME=?, PASSWORD=? WHERE ID_USER=?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, tfNamaKaryawan.getText());
            pstat.setString(2, cbKelamin.getSelectedItem().toString());
            pstat.setString(3, taAlamat.getText());
            pstat.setString(4, tfEmail.getText());
            pstat.setString(5, cbTipeAkun.getSelectedItem().toString());
            pstat.setString(6, tfUsername.getText());
            pstat.setString(7, tfPassword.getText());
            pstat.setString(8, textId.getText());
            rsetInt = pstat.executeUpdate();
            
            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil diupdate");
                read();
            } else{
                Variable.popUpErrorMessage("Error", "Data gagal diupdate");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void deleteKaryawan(){
        try {
            query = "DELETE USERS WHERE ID_USER = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, textId.getText());
            rsetInt = pstat.executeUpdate();
            
            if (rsetInt > 0) {
                Variable.popUpSuccessMessage("Berhasil", "Data berhasil dihapus");
                read();
            } else{
                Variable.popUpErrorMessage("Error", "Tidak ada data yang dihapus");
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void getTableData(){
        int row = table.getSelectedRow();
        String id, nama, gender, alamat, email, tipe_akun, username, password;
        
        if (row >=0) {
            id = table.getValueAt(row, 0).toString();
            nama = table.getValueAt(row, 1).toString();
            gender = table.getValueAt(row, 2).toString();
            alamat = table.getValueAt(row, 3).toString();
            email = table.getValueAt(row, 4).toString();
            tipe_akun = table.getValueAt(row, 5).toString();
            username = table.getValueAt(row, 6).toString();
            password = table.getValueAt(row, 7).toString();
            
            textId.setText(id);
            tfNamaKaryawan.setText(nama);
            cbKelamin.setSelectedItem(gender);
            taAlamat.setText(alamat);
            tfEmail.setText(email);
            cbTipeAkun.setSelectedItem(tipe_akun);
            tfUsername.setText(username);
            tfPassword.setText(password);
        }
    }
    
    private void clearText(){
        textId.setText("");
        tfNamaKaryawan.setText("");
        tfPencarian.setText("");
        cbKelamin.setSelectedIndex(0);
        cbTipeAkun.setSelectedIndex(0);
        taAlamat.setText("");
        tfEmail.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
        read();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfNamaKaryawan = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbKelamin = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taAlamat = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        tfUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfPassword = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        textId = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbTipeAkun = new javax.swing.JComboBox<>();

        jLabel6.setText("Nama lengkap");

        jLabel1.setText("Nama lengkap");

        jLabel2.setText("Jenis Kelamin");

        jLabel3.setText("Alamat");

        taAlamat.setColumns(20);
        taAlamat.setRows(5);
        jScrollPane1.setViewportView(taAlamat);

        jLabel4.setText("Email");

        jLabel5.setText("Username");

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

        jLabel8.setText("Cari");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Daftar Karyawan");

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

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setForeground(new java.awt.Color(255, 255, 255));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        textId.setText("id");

        jLabel10.setText("Tipe akun");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(textId))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel4)
                            .addComponent(tfEmail)
                            .addComponent(cbKelamin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfNamaKaryawan)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(tfUsername)
                            .addComponent(tfPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10)
                            .addComponent(cbTipeAkun, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(textId)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNamaKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbTipeAkun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btTambah)
                            .addComponent(btUpdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btClear)
                        .addGap(0, 34, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearText();
    }//GEN-LAST:event_btClearActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        if (!tfNamaKaryawan.getText().isEmpty() && 
                !taAlamat.getText().isEmpty() && 
                !tfEmail.getText().isEmpty() && 
                !tfUsername.getText().isEmpty() && 
                !tfPassword.getText().isEmpty()) {
            tambahKaryawan();
        } else{
            Variable.popUpErrorMessage("Error", "Tidak ada data ditambah");
        }
    }//GEN-LAST:event_btTambahActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (!tfNamaKaryawan.getText().isEmpty() && 
                !taAlamat.getText().isEmpty() && 
                !tfEmail.getText().isEmpty() && 
                !tfUsername.getText().isEmpty() && 
                !tfPassword.getText().isEmpty()) {
            updateKaryawan();
        } else{
            Variable.popUpErrorMessage("Error", "Data gagal diupdate");
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        if (!textId.getText().isEmpty()) {
            deleteKaryawan();
        } else{
            Variable.popUpErrorMessage("Error", "Tidak ada data dihapus");
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        getTableData();
    }//GEN-LAST:event_tableMouseClicked

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPencarianKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<String> cbKelamin;
    private javax.swing.JComboBox<String> cbTipeAkun;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTextArea taAlamat;
    private javax.swing.JTable table;
    private javax.swing.JLabel textId;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfNamaKaryawan;
    private javax.swing.JTextField tfPassword;
    private javax.swing.JTextField tfPencarian;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
