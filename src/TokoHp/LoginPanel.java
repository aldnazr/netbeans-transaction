package TokoHp;

import TokoHp.Main.MainFrame;
import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.koneksi;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.*;
import javax.swing.JOptionPane;

public class LoginPanel extends javax.swing.JFrame {

    Connection connection;
    PreparedStatement pstat;
    Statement statement;
    ResultSet rset;
    String query;
    MainFrame adminFrame;

    public LoginPanel() {
        initComponents();
        init();
    }

    private void init() {
        setResizable(false);
        connection = koneksi();
        setLocationRelativeTo(null);
        Variable.setPasswordFieldRevealButton(passwordField);
        tfUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username/email");
        tfUser.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan password");
//        Variable.setFontTitle(jLabel3);
        jLabel3.setFont(new Font(FlatRobotoFont.STYLE_BOLD, Font.BOLD, 24) {
        });
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            Variable.setLightTheme();
            new LoginPanel().setVisible(true);
        }
        );
    }

    private void checkAdmin() {
        try {
            query = "SELECT TIPE_AKUN FROM SESSIONS JOIN USERS USING (ID_USER)";
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = statement.executeQuery(query);

            if (rset.last()) {
                if (rset.getString(1).equals("Admin")) {
                    adminFrame = new MainFrame();
                    adminFrame.setVisible(true);
                } else {
                    adminFrame = new MainFrame();
                    adminFrame.setVisible(true);
                }
            }

            statement.close();
            rset.close();
        } catch (SQLException ex) {
            System.err.print("Error in checkAdmin: " + ex.getMessage());
        }
    }

    private void insertSession(String idUser) {
        try {
            Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());
            query = "INSERT INTO SESSIONS(ID_USER, WAKTU_LOGIN) VALUES (?, ?)";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, idUser);
            pstat.setTimestamp(2, currTimestamp);
            pstat.executeUpdate();

            pstat.close();
        } catch (SQLException ex) {
            System.err.println("Error in insertSession: " + ex.getMessage());
        }
    }

    private void checkLogin() {
        if (tfUser.getText().isEmpty() && passwordField.getPassword().length == 0) {
            popUpErrorMessage("Login gagal", "Harap masukkan username dan password");
            return;
        }
        try {
            String username = tfUser.getText();
            String password = String.valueOf(passwordField.getPassword());

            query = "SELECT ID_USER, PASSWORD FROM USERS WHERE USERNAME = ? OR EMAIL = ?";
            pstat = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstat.setString(1, username);
            pstat.setString(2, username);
            rset = pstat.executeQuery();

            if (!rset.first()) {
                popUpErrorMessage("Login gagal", "Pengguna tidak ditemukan");
                return;
            }

            String idFromDB = rset.getString(1);
            String passFromDB = rset.getString(2);

            if (!passFromDB.equals(password)) {
                popUpErrorMessage("Login gagal", "Kata sandi salah");
                return;
            }

            insertSession(idFromDB);
            checkAdmin();
            dispose();

            pstat.close();
            rset.close();
        } catch (SQLException ex) {
            System.err.println("Error in checkLogin: " + ex.getMessage());
        }
    }

    void popUpErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tfUser.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Username or Email");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Password");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Login");

        btLogin.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btLogin.setForeground(new java.awt.Color(235, 235, 240));
        btLogin.setText("SIGN IN");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(passwordField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(128, 128, 128))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        checkLogin();
    }//GEN-LAST:event_btLoginActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
}
