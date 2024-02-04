package TokoHp.Objects;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
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
import raven.drawer.Drawer;
import raven.drawer.component.DrawerBuilder;
import raven.popup.GlassPanePopup;
import raven.toast.Notifications;

public class Variable {

//    Tema
    public static void setLightTheme() {
        FlatAnimatedLafChange.showSnapshot();
        FlatMacLightLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void setDarkTheme() {
        FlatAnimatedLafChange.showSnapshot();
        FlatMacDarkLaf.setup();
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static boolean isDarkTheme() {
        return FlatLaf.isLafDark();
    }

    public static void setFontTitle(JLabel label) {
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

    public static int numberToInt(String numberString) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        Number number = 0;
        try {
            number = numberFormat.parse(numberString);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        return number.intValue();
    }

    public static void installSideBarAndToast(JFrame jFrame, DrawerBuilder drawerBuilder) {
        GlassPanePopup.install(jFrame);
        Drawer.getInstance().setDrawerBuilder(drawerBuilder);
        Notifications.getInstance().setJFrame(jFrame);
    }

    public static int getActiveUserId() {
        Connection connection = koneksi();
        String sql;
        Statement stat;
        ResultSet rSet;
        int resultQuery = 0;
        try {
            sql = "SELECT ID_USER FROM SESSIONS WHERE ID_SESSION = (SELECT MAX(ID_SESSION) FROM SESSIONS)";
            stat = connection.createStatement();
            rSet = stat.executeQuery(sql);

            if (rSet.next()) {
                resultQuery = rSet.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return resultQuery;
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

    public static void setPlaceholderTextfield(JTextField textField, String placeholder) {
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
    }

    public static void setSearchFieldIcon(JTextField jTextField) {
        String icon = !isDarkTheme() ? "TokoHp/Icons/search.svg" : "TokoHp/Icons/search_dark.svg";
        jTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSVGIcon(icon, 0.8f));
    }

    public static JTextField disableDateTextfield(JDateChooser dateChooser) {
        JTextField dateChooserTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateChooserTextField.setEditable(false);
        dateChooserTextField.setForeground(UIManager.getColor("RootPane.foreground"));

        return dateChooserTextField;
    }
}
