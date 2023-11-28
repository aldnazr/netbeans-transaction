package TokoHp.Objects;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import raven.alerts.MessageAlerts;

public class Variable {

//    Tema
    public static void setUIManager() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }
    }

//    Koneksi
    public static Connection koneksi() {
        Connection connection = null;
        String server = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "tokohp";
        String password = "root";

        try {
            connection = DriverManager.getConnection(server, username, password);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return connection;
    }
    
    public static void setActiveIDUser(JLabel textIdUser) {
        Connection connection = koneksi();
        String sql;
        Statement stat;
        ResultSet rSet;
        try {
            sql = "SELECT ID_USER FROM SESSIONS";
            stat = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rSet = stat.executeQuery(sql);

            if (rSet.last()) {
                textIdUser.setText(rSet.getString(1));
            }
            rSet.close();
            stat.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

//    PopUp Error
    public static void popUpErrorMessage(String title, String message) {
//        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        MessageAlerts.getInstance().showMessage(title, message, MessageAlerts.MessageType.ERROR);
    }

//    PopUp Sukses
    public static void popUpSuccessMessage(String title, String message) {
//        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        MessageAlerts.getInstance().showMessage(title, message, MessageAlerts.MessageType.SUCCESS);
    }

//    Auto uppercase kata utama
    public static void capitalizeFirstLetter(JTextField textField) {
        String currentText = textField.getText();

        if (!currentText.isEmpty() && !Character.isUpperCase(currentText.charAt(0))) {
            currentText = Character.toUpperCase(currentText.charAt(0)) + currentText.substring(1);
            textField.setText(currentText);
        }
    }
    
    public static  void setPasswordFieldRevealButton(JPasswordField passwordField){
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
    }
    
    public static void setSearchbarPlaceholder(JTextField searchTextField){
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cari");
    }

    public static String sqlRiwayatTransaksi = "SELECT T.ID_TRANSAKSI, USR.NAMA_LENGKAP AS PELAYAN, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, DT.JUMLAH_PEMBELIAN, SUM(PH.HARGA * DT.JUMLAH_PEMBELIAN) AS SUBTOTAL, T.TOTAL_BAYAR FROM TRANSAKSI T JOIN DETAIL_TRANSAKSI DT ON T.ID_TRANSAKSI = DT.ID_TRANSAKSI JOIN USERS USR ON T.ID_USER = USR.ID_USER JOIN PHONES PH ON DT.ID_PHONE = PH.ID_PHONE JOIN BRAND BR ON PH.ID_BRAND = BR.ID_BRAND WHERE T.ID_TRANSAKSI LIKE ? OR LOWER(USR.NAMA_LENGKAP) LIKE ? OR LOWER(T.NAMA_PELANGGAN) LIKE ? OR TO_CHAR(T.TANGGAL, 'YYYY-MM-DD HH24:MI:SS') LIKE ? GROUP BY T.ID_TRANSAKSI, USR.NAMA_LENGKAP, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, DT.JUMLAH_PEMBELIAN, T.TOTAL_BAYAR ORDER BY T.ID_TRANSAKSI";

    public static String sqlTableDaftarHP = "select id_phone, nama_brand, nama_handphone, stok, harga from phones join brand using (id_brand) where stok > 0 AND LOWER(nama_brand) like ? OR LOWER(nama_handphone) like ?";
    
    public static String sqlFilterPhone = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?";
   
    public static String sqlFilterPhoneAvailable = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK > 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";
    
    public static String sqlFilterPhoneNotAvailable  = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK = 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";
}
