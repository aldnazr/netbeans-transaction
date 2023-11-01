package TokoHp;

import TokoHp.Views.MainFrame.AdminFrame;
import TokoHp.Views.MainFrame.KaryawanFrame;
import TokoHp.Function.Variable;
import java.sql.*;

public class LoginPanel extends javax.swing.JFrame {

    private final Connection connection;
    PreparedStatement pstat;
    Statement statement;
    ResultSet rset;
    String query;
    AdminFrame adminFrame = new AdminFrame();
    KaryawanFrame karyawanFrame = new KaryawanFrame();

    public LoginPanel() {
        initComponents();
        connection = Variable.koneksi();
        textIdKaryawan.setVisible(false);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        Variable.setUIManager();
        new LoginPanel().setVisible(true);
    }

    private void checkAdmin() {
        try {
            query = "SELECT TIPE_AKUN FROM SESSION_DATA JOIN USERS USING (ID_USER)";
            statement = connection.createStatement();
            rset = statement.executeQuery(query);

            if (rset.next()) {
                if (rset.getString(1).equals("Admin")) {
                    adminFrame.setVisible(true);
                } else {
                    karyawanFrame.setVisible(true);
                }
            }
        } catch (SQLException ex) {
            System.err.print("Error in checkAdmin: " + ex.getMessage());
        } finally {
            closeResources();
        }
    }

    private void insertSession(String id) {
        try {
            // Delete existing session data
            String queryDeleteTable = "DELETE FROM SESSION_DATA";
            statement = connection.createStatement();
            statement.executeUpdate(queryDeleteTable);

            // Insert session data
            query = "INSERT INTO SESSION_DATA VALUES (?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, id);
            pstat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error in insertSession: " + ex.getMessage());
        } finally {
            closeResources();
        }
    }

    private void checkLogin() {
        try {
            String username = tfUser.getText();
            String password = String.valueOf(tfPassword.getPassword());

            query = "SELECT ID_USER, PASSWORD FROM USERS WHERE USERNAME = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, username);
            rset = pstat.executeQuery();

            if (rset.next()) {
                String idFromDB = rset.getString(1);
                String passFromDB = rset.getString(2);

                if (passFromDB.equals(password)) {
                    insertSession(idFromDB);
                    checkAdmin();
                    dispose();
                } else {
                    Variable.popUpErrorMessage("Login gagal", "Kata sandi salah");
                }
            } else {
                Variable.popUpErrorMessage("Login gagal", "Username tidak ditemukan");
            }
        } catch (SQLException ex) {
            System.err.println("Error in checkLogin: " + ex.getMessage());
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (pstat != null) {
                pstat.close();
            }
            if (rset != null) {
                rset.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error while closing resources: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tfPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btLogin = new javax.swing.JButton();
        textIdKaryawan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tfUser.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Username");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Password");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Login");

        btLogin.setBackground(new java.awt.Color(51, 153, 255));
        btLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btLogin.setForeground(new java.awt.Color(255, 255, 255));
        btLogin.setText("LOGIN");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        textIdKaryawan.setText("Id Karyawan");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(182, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(256, 256, 256))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(tfPassword)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfUser, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                        .addGap(175, 175, 175))
                    .addComponent(textIdKaryawan, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(textIdKaryawan)
                .addGap(36, 36, 36)
                .addComponent(jLabel3)
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        if (!tfUser.getText().isEmpty() && tfPassword.getPassword().length > 0) {
            checkLogin();
        } else {
            Variable.popUpErrorMessage("Error", "Harap Masukkan Username dan Password");
        }
    }//GEN-LAST:event_btLoginActionPerformed


    // Variable declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel textIdKaryawan;
    private javax.swing.JPasswordField tfPassword;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
}
