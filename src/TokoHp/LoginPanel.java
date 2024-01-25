package TokoHp;

import TokoHp.Main.MainFrame;
import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.koneksi;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class LoginPanel extends javax.swing.JFrame {

    Connection connection;
    PreparedStatement pstat;
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
        jLabel3.setFont(new Font(FlatRobotoFont.STYLE_BOLD, Font.BOLD, 26));
        tfUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username/email");
        tfUser.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        tfUser.putClientProperty("JComponent.roundRect", true);
        tfUser.setMargin(new Insets(5, 11, 5, 11));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan sandi");
        passwordField.putClientProperty("JComponent.roundRect", true);
        passwordField.setMargin(new Insets(5, 11, 5, 11));
        Variable.setPasswordFieldRevealButton(passwordField);
        btLogin.putClientProperty("JButton.buttonType", "roundRect");
        menuBar.add(themeSwitcher());
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            Variable.setLightTheme();
            new LoginPanel().setVisible(true);
        });
    }

    private JButton themeSwitcher() {
        boolean isLightTheme = !Variable.isDarkTheme();
        String iconDirectory = isLightTheme ? "TokoHp/Icons/moon.svg" : "TokoHp/Icons/sun.svg";
        JButton button = new JButton(new FlatSVGIcon(iconDirectory, 0.85f));

        button.addActionListener((l) -> {
            EventQueue.invokeLater(() -> {
                if (isLightTheme) {
                    Variable.setDarkTheme();
                } else {
                    Variable.setLightTheme();
                }
                dispose();
                new LoginPanel().setVisible(true);
            });

        });

        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "background:null;"
        );

        button.setMargin(new Insets(5, 5, 5, 5));
        return button;
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
            System.err.println("Error di insertSession: " + ex.getMessage());
        }
    }

    private void checkLogin() {
        String username = tfUser.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() && password.isEmpty()) {
            popUpErrorMessage("Login gagal", "Harap masukkan username dan password");
            return;
        }

        try {
            query = "SELECT ID_USER, PASSWORD FROM USERS WHERE USERNAME = ? OR EMAIL = ?";
            pstat = connection.prepareStatement(query);
            pstat.setString(1, username);
            pstat.setString(2, username);
            rset = pstat.executeQuery();

            if (!rset.next()) {
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
            adminFrame = new MainFrame();
            adminFrame.setVisible(true);

            pstat.close();
            rset.close();
            dispose();
        } catch (SQLException ex) {
            System.err.println("Error in checkLogin: " + ex.getMessage());
        }
    }

    void popUpErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUser = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        btLogin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tfUser.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Login");

        btLogin.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.background"));
        btLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btLogin.setForeground(new java.awt.Color(235, 235, 240));
        btLogin.setText("SIGN IN");
        btLogin.setAlignmentY(0.0F);
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        jLabel1.setText("Username atau email");

        jLabel2.setText("Kata sandi");
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passwordField)
                    .addComponent(tfUser)
                    .addComponent(btLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
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
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
}
