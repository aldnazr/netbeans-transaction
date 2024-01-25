package TokoHp.Main;

import TokoHp.LoginPanel;
import TokoHp.Objects.Variable;
import TokoHp.Views.DaftarBrand;
import TokoHp.Views.DaftarHandphone;
import TokoHp.Views.DaftarKaryawan;
import TokoHp.Views.ProfileUser;
import TokoHp.Views.RiwayatLogin;
import TokoHp.Views.RiwayatTransaksi;
import TokoHp.Views.TransaksiBaru;
import java.awt.EventQueue;
import java.sql.*;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.drawer.component.menu.data.Item;
import raven.drawer.component.menu.data.MenuItem;

public class DrawerNavigation extends SimpleDrawerBuilder {

    MainFrame mainFrame;

    public DrawerNavigation(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private boolean isLoggedAsAdmin() {
        Connection connection = Variable.koneksi();
        Statement statement;
        String sql;
        ResultSet resultSet;

        try {
            sql = "SELECT TIPE_AKUN FROM SESSIONS JOIN USERS USING (ID_USER) WHERE ID_SESSION = (SELECT MAX(ID_SESSION) FROM SESSIONS)";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getString(1).equals("Admin");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private SimpleHeaderData headerData() {
        SimpleHeaderData simpleHeaderData = new SimpleHeaderData();
        Connection connection = Variable.koneksi();
        Statement statement;
        String sql;
        ResultSet resultSet;

        try {
            sql = "SELECT NAMA_LENGKAP, EMAIL FROM SESSIONS JOIN USERS USING (ID_USER) WHERE ID_SESSION = (SELECT MAX(ID_SESSION) FROM SESSIONS)";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                simpleHeaderData.setTitle(resultSet.getString(1));
                simpleHeaderData.setDescription(resultSet.getString(2));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return simpleHeaderData;
    }

    private SimpleMenuOption menuAdmin(MenuItem[] menus) {
        boolean isLightMode = !Variable.isDarkTheme();
        return new SimpleMenuOption()
                .setMenus(menus)
                .setBaseIconPath("TokoHp/Icons")
                .addMenuEvent((MenuAction action, int[] index) -> {
                    switch (index.length) {
                        case 1 -> {
                            switch (index[0]) {
                                case 0 ->
                                    mainFrame.switchFrame(new ProfileUser(), "Pengaturan Akun");
                                case 1 ->
                                    mainFrame.switchFrame(new TransaksiBaru(), "Transaksi");
                                case 3 ->
                                    mainFrame.switchFrame(new DaftarKaryawan(), "Daftar Karyawan");
                                case 4 ->
                                    mainFrame.switchFrame(new DaftarBrand(), "Daftar Brand");
                                case 5 ->
                                    mainFrame.switchFrame(new DaftarHandphone(), "Daftar Handphone");
                                case 6 -> {
                                    EventQueue.invokeLater(() -> {
                                        if (isLightMode) {
                                            Variable.setDarkTheme();
                                        } else {
                                            Variable.setLightTheme();
                                        }
                                        mainFrame.dispose();
                                        new MainFrame().setVisible(true);
                                    });

                                }
                                case 7 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        }
                        case 2 -> {
                            switch (index[1]) {
                                case 0 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi(), "Riwayat Transaksi");
                                case 1 ->
                                    mainFrame.switchFrame(new RiwayatLogin(), "Riwayat Login");
                            }
                        }
                    }
                }
                );
    }

    private SimpleMenuOption menuKaryawan(MenuItem[] menuOptions) {
        boolean isLightMode = !Variable.isDarkTheme();
        return new SimpleMenuOption()
                .setMenus(menuOptions)
                .setBaseIconPath("TokoHp/Icons")
                .addMenuEvent((MenuAction action, int[] index) -> {
                    switch (index.length) {
                        case 1 -> {
                            switch (index[0]) {
                                case 0 ->
                                    mainFrame.switchFrame(new ProfileUser(), "Pengaturan Akun");
                                case 1 ->
                                    mainFrame.switchFrame(new TransaksiBaru(), "Transaksi");
                                case 2 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi(), "Riwayat Transaksi");
                                case 3 -> {
                                    EventQueue.invokeLater(() -> {
                                        if (isLightMode) {
                                            Variable.setDarkTheme();
                                        } else {
                                            Variable.setLightTheme();
                                        }
                                        mainFrame.dispose();
                                        new MainFrame().setVisible(true);
                                    });
                                }
                                case 4 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return headerData();
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {
        boolean isAdmin = isLoggedAsAdmin();
        boolean isLightTheme = !Variable.isDarkTheme();
        String themeName = isLightTheme ? "Dark Mode" : "Light Mode";
        String themeIcon = isLightTheme ? "moon.svg" : "sun.svg";

        MenuItem[] menuAdmin = {
            new Item.Label("AKUN"),
            new Item("Pengaturan Akun", "person.svg"),
            new Item.Label("TRANSAKSI"),
            new Item("Transaksi Baru", "bag_plus.svg"),
            new Item("Riwayat", "history.svg")
            .subMenu("Riwayat Transaksi")
            .subMenu("Riwayat Login"),
            new Item.Label("MANAJEMEN"),
            new Item("Karyawan", "people.svg"),
            new Item("Brand", "tags.svg"),
            new Item("Handphone", "phone.svg"),
            new Item.Label("LAINNYA"),
            new Item(themeName, themeIcon),
            new Item("Logout", "logout.svg")
        };

        MenuItem[] menuKaryawan = {
            new Item.Label("AKUN"),
            new Item("Pengaturan Akun", "person.svg"),
            new Item.Label("TRANSAKSI"),
            new Item("Transaksi Baru", "bag_plus.svg"),
            new Item("Riwayat Transaksi", "history.svg"),
            new Item.Label("LAINNYA"),
            new Item(themeName, themeIcon),
            new Item("Logout", "logout.svg")
        };

        return isAdmin
                ? menuAdmin(menuAdmin)
                : menuKaryawan(menuKaryawan);

    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

}
