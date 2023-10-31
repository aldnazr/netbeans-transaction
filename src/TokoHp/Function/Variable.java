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
            System.out.println(ex.getMessage());
        }
    }

//    Koneksi
    public static Connection koneksi() {
        Connection connection = null;
        String server = "jdbc:oracle:thin:@localhost:1521:XE";

        try {
            connection = DriverManager.getConnection(server, "tokohp", "root");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
}
