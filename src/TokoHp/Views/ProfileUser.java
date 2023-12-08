package TokoHp.Views;

import TokoHp.Main.MainFrame;
import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.koneksi;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import raven.alerts.MessageAlerts;
import raven.popup.component.PopupController;

public class ProfileUser extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pStat;
    String sql;
    ResultSet rSet;

    public ProfileUser() {
        initComponents();
        init();
    }

    private void init() {
        connection = koneksi();
        Variable.setActiveIDUser(textIdUser);
        textIdUser.setVisible(false);
        setButtongroup();
        read();
        Variable.setPasswordFieldRevealButton(pwField);
        Variable.disableDateTextfield(dateChooser);
        Variable.setFontTitle(jLabel10);
        card1.setCorner(34);
        card1.setForeground(!Variable.isDarkTheme() ? Color.decode("#D8D8DC") : Color.decode("#363638"));
        JLabel[] labels = {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9};
        setFontSubLabel(labels);
    }

    private void setFontSubLabel(JLabel[] labels) {
        for (JLabel label : labels) {
            label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
        }
    }

    private void setButtongroup() {
        buttonGroup.add(radioButtonLK);
        buttonGroup.add(radioButtonPR);
    }

    private void read() {
        try {
            sql = "SELECT NAMA_LENGKAP, TANGGAL_LAHIR, GENDER, ALAMAT, EMAIL, NO_TELP, USERNAME, PASSWORD FROM USERS WHERE ID_USER = ?";
            pStat = connection.prepareStatement(sql);
            pStat.setString(1, textIdUser.getText());
            rSet = pStat.executeQuery();

            if (rSet.next()) {
                tfNama.setText(rSet.getString(1));
                dateChooser.setDate(rSet.getDate(2));
                if (rSet.getString(3).equals("Laki-laki")) {
                    radioButtonLK.setSelected(true);
                } else {
                    radioButtonPR.setSelected(true);
                }
                taAlamat.setText(rSet.getString(4));
                tfEmail.setText(rSet.getString(5));
                tfPhone.setText(rSet.getString(6));
                tfUsername.setText(rSet.getString(7));
                pwField.setText(rSet.getString(8));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void update() {
        String gender = radioButtonLK.isSelected() ? radioButtonLK.getText() : radioButtonPR.getText();
        java.util.Date getDateChooser = dateChooser.getDate();
        java.sql.Date sqlDate = new java.sql.Date(getDateChooser.getTime());
        try {
            sql = "UPDATE USERS SET NAMA_LENGKAP = ?, TANGGAL_LAHIR = ?, GENDER = ?, ALAMAT = ?, EMAIL = ?, NO_TELP = ?, USERNAME = ?, PASSWORD = ? WHERE ID_USER = ?";
            pStat = connection.prepareStatement(sql);
            pStat.setInt(9, Integer.parseInt(textIdUser.getText()));
            pStat.setString(1, tfNama.getText());
            pStat.setDate(2, sqlDate);
            pStat.setString(3, gender);
            pStat.setString(4, taAlamat.getText());
            pStat.setString(5, tfEmail.getText());
            pStat.setString(6, tfPhone.getText());
            pStat.setString(7, tfUsername.getText());
            pStat.setString(8, String.valueOf(pwField.getPassword()));
            int executeResult = pStat.executeUpdate();

            if (executeResult > 0) {
//                Variable.popUpSuccessMessage("Sukses", "Profil berhasil diperbarui");
                MessageAlerts.getInstance().showMessage("Sukses", "Profil berhasil diperbarui", MessageAlerts.MessageType.SUCCESS, MessageAlerts.DEFAULT_OPTION, (PopupController pc, int i) -> {
                    JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getDesktopPane());
                    MainFrame mainFrame = new MainFrame();
                    jFrame.dispose();
                    mainFrame.setVisible(true);
                    mainFrame.switchFrame(new ProfileUser());
                });
            } else {
                Variable.popUpErrorMessage("Error", "Profil gagal diupdate");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private boolean validation() {
        return !tfNama.getText().isEmpty()
                && !taAlamat.getText().isEmpty()
                && !tfEmail.getText().isEmpty()
                && !tfPhone.getText().isEmpty()
                && !tfUsername.getText().isEmpty()
                && pwField.getPassword().length != 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jLabel10 = new javax.swing.JLabel();
        textIdUser = new javax.swing.JLabel();
        card1 = new TokoHp.Component.Card();
        jLabel6 = new javax.swing.JLabel();
        radioButtonPR = new javax.swing.JRadioButton();
        tfPhone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        pwField = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        tfNama = new javax.swing.JTextField();
        dateChooser = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taAlamat = new javax.swing.JTextArea();
        tfEmail = new javax.swing.JTextField();
        radioButtonLK = new javax.swing.JRadioButton();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("PENGATURAN AKUN");

        textIdUser.setText("textIdUser");

        card1.setForeground(new java.awt.Color(216, 216, 220));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setText("Email");

        radioButtonPR.setText("Perempuan");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setText("Telepon");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setText("Username");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setText("Kata sandi");

        jButton1.setBackground(new java.awt.Color(10, 132, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(235, 235, 240));
        jButton1.setText("Simpan Perubahan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        dateChooser.setDateFormatString("dd/MM/yyyy");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setText("Tanggal lahir");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setText("Nama");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setText("Kelamin");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Alamat");

        taAlamat.setColumns(20);
        taAlamat.setRows(5);
        jScrollPane1.setViewportView(taAlamat);

        radioButtonLK.setText("Laki-laki");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(card1Layout.createSequentialGroup()
                                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(39, 39, 39)
                                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(card1Layout.createSequentialGroup()
                                            .addComponent(radioButtonLK)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(radioButtonPR))
                                        .addComponent(tfUsername)
                                        .addComponent(tfPhone)
                                        .addComponent(tfEmail)
                                        .addComponent(jScrollPane1)
                                        .addComponent(pwField, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tfNama, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(card1Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jButton1)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(radioButtonLK)
                    .addComponent(radioButtonPR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(12, 12, 12)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(34, 34, 34)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(textIdUser))
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(267, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(textIdUser)
                .addGap(27, 27, 27)
                .addComponent(jLabel10)
                .addGap(47, 47, 47)
                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (validation()) {
            update();
        } else {
            Variable.popUpErrorMessage("Form tidak terpenuhi", "Harap isi semua bidang yang diperlukan");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private TokoHp.Component.Card card1;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JPasswordField pwField;
    private javax.swing.JRadioButton radioButtonLK;
    private javax.swing.JRadioButton radioButtonPR;
    private javax.swing.JTextArea taAlamat;
    private javax.swing.JLabel textIdUser;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfNama;
    private javax.swing.JTextField tfPhone;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
