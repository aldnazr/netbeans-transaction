package TokoHp.Objects;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Font;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import raven.alerts.MessageAlerts;
import raven.drawer.Drawer;
import raven.drawer.component.DrawerBuilder;
import raven.popup.GlassPanePopup;

public class Variable {

//    Tema
    public static void setLightTheme() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            FlatAnimatedLafChange.showSnapshot();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void labelTitleCard(JLabel title, JLabel subtitle) {
        title.putClientProperty(FlatClientProperties.STYLE, "font:$h2.font");
        subtitle.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font");
    }

    public static void setDarkTheme() {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            FlatAnimatedLafChange.showSnapshot();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static boolean isLightTheme() {
        return UIManager.getLookAndFeel() instanceof FlatMacLightLaf;
    }

    public static void setLabelFont(JLabel label) {
        label.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font");
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

    public static String stringToNumber(String textfieldNumber) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        Number number = 0;
        try {
            number = numberFormat.parse(textfieldNumber);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        return numberFormat.format(number);
    }

    public static int numberToInt(JTextField textField) {
        String formattedText = textField.getText().replaceAll(",", "");
        NumberFormat numberFormat = NumberFormat.getInstance();
        Number number = 0;
        try {
            number = numberFormat.parse(formattedText);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        return number.intValue();
    }

    public static void setSideBar(JFrame jFrame, DrawerBuilder drawerBuilder) {
        GlassPanePopup.install(jFrame);
        Drawer.getInstance().setDrawerBuilder(drawerBuilder);
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

    public static void setPasswordFieldRevealButton(JPasswordField passwordField) {
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
    }

    public static void setPlaceholderTextfield(JTextField textField, String value) {
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, value);
    }

    public static JTextField disableDateTextfield(JDateChooser dateChooser) {
        JTextField dateChooserTextField = ((JTextField) dateChooser.getDateEditor().getUiComponent());
        dateChooserTextField.setEditable(false);
        return dateChooserTextField;
    }
    public static String sqlRiwayatTransaksi = "SELECT T.ID_TRANSAKSI, USR.NAMA_LENGKAP AS PELAYAN, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, DT.JUMLAH_PEMBELIAN, SUM(PH.HARGA * DT.JUMLAH_PEMBELIAN) AS SUBTOTAL, T.TOTAL_BAYAR FROM TRANSAKSI T JOIN DETAIL_TRANSAKSI DT ON T.ID_TRANSAKSI = DT.ID_TRANSAKSI JOIN USERS USR ON T.ID_USER = USR.ID_USER JOIN PHONES PH ON DT.ID_PHONE = PH.ID_PHONE JOIN BRAND BR ON PH.ID_BRAND = BR.ID_BRAND WHERE T.ID_TRANSAKSI LIKE ? OR LOWER(USR.NAMA_LENGKAP) LIKE ? OR LOWER(T.NAMA_PELANGGAN) LIKE ? OR TO_CHAR(T.TANGGAL, 'YYYY-MM-DD HH24:MI:SS') LIKE ? GROUP BY T.ID_TRANSAKSI, USR.NAMA_LENGKAP, T.NAMA_PELANGGAN, T.TANGGAL, BR.NAMA_BRAND, PH.NAMA_HANDPHONE, PH.HARGA, DT.JUMLAH_PEMBELIAN, T.TOTAL_BAYAR ORDER BY T.ID_TRANSAKSI DESC";

    public static String sqlTableDaftarHP = "select id_phone, nama_brand, nama_handphone, stok, harga from phones join brand using (id_brand) where stok > 0 AND LOWER(nama_brand) LIKE ? OR LOWER(nama_handphone) LIKE ?";

    public static String sqlFilterPhone = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?";

    public static String sqlFilterPhoneAvailable = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK > 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";

    public static String sqlFilterPhoneNotAvailable = "SELECT ID_PHONE, NAMA_BRAND, NAMA_HANDPHONE, DESKRIPSI, HARGA, STOK FROM PHONES JOIN BRAND USING (ID_BRAND) WHERE STOK = 0 AND (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)";
}
