package TokoHp.Function;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

//    PopUp Error
    public static void popUpErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

//    PopUp Sukses
    public static void popUpSuccessMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

//    Auto uppercase kata utama
    public static void capitalizeFirstLetter(JTextField textField) {
        String currentText = textField.getText();

        if (!currentText.isEmpty() && !Character.isUpperCase(currentText.charAt(0))) {
            currentText = Character.toUpperCase(currentText.charAt(0)) + currentText.substring(1);
            textField.setText(currentText);
        }
    }
    
    public static String sqlRiwayatBarang = "SELECT T.ID_TRANSAKSI, USR.NAMA_LENGKAP, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, BK.JUMLAH_KELUAR, SUM(PH.HARGA * BK.JUMLAH_KELUAR), T.TOTAL_BAYAR FROM TRANSAKSI T JOIN BARANG_KELUAR BK ON T.ID_TRANSAKSI = BK.ID_TRANSAKSI JOIN USERS USR ON T.ID_USER = USR.ID_USER JOIN PHONES PH ON BK.ID_PHONE = PH.ID_PHONE JOIN BRAND BR ON PH.ID_BRAND = BR.ID_BRAND WHERE T.ID_TRANSAKSI LIKE ? OR LOWER(USR.NAMA_LENGKAP) LIKE ? OR LOWER(T.NAMA_PELANGGAN) LIKE ? GROUP BY T.ID_TRANSAKSI, USR.NAMA_LENGKAP, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, BK.JUMLAH_KELUAR, T.TOTAL_BAYAR";
    
    public static String sqlTableDaftarHP = "select id_phone, nama_brand, nama_handphone, stok, harga from phones join brand using (id_brand) where stok > 0 AND LOWER(nama_brand) like ? OR LOWER(nama_handphone) like ?";
}
