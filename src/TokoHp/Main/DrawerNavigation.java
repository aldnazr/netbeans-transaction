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
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.SimpleMenuOption;
import java.sql.*;

public class DrawerNavigation extends SimpleDrawerBuilder {

    MainFrame mainFrame;

    public DrawerNavigation(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private boolean checkAccount() {
        boolean isAdmin = true;
        Connection connection = Variable.koneksi();
        Statement statement;
        String sql;
        ResultSet resultSet;

        try {
            sql = "SELECT TIPE_AKUN FROM SESSIONS JOIN USERS USING (ID_USER)";
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);

            if (resultSet.last()) {
                isAdmin = resultSet.getString(1).equals("Admin");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return isAdmin;
    }

    private SimpleHeaderData headerData() {
        SimpleHeaderData simpleHeaderData = new SimpleHeaderData();
        Connection connection = Variable.koneksi();
        Statement statement;
        String sql;
        ResultSet resultSet;

        try {
            sql = "SELECT NAMA_LENGKAP, EMAIL FROM SESSIONS JOIN USERS USING (ID_USER)";
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);

            if (resultSet.last()) {
                simpleHeaderData.setTitle(resultSet.getString(1));
                simpleHeaderData.setDescription(resultSet.getString(2));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return simpleHeaderData;
    }

    private void menuAdmin(SimpleMenuOption simpleMenuOption, String[][] menuOptions, String[] icons) {
        boolean isLightMode = !Variable.isDarkTheme();
        simpleMenuOption
                .setMenus(menuOptions)
                .setBaseIconPath("TokoHp/Icons")
                .setIcons(icons)
                .addMenuEvent((MenuAction action, int i, int i1) -> {
                    switch (i) {
                        case 0 ->
                            mainFrame.switchFrame(new ProfileUser());
                        case 1 ->
                            mainFrame.switchFrame(new TransaksiBaru());
                        case 2 -> {
                            switch (i1) {
                                case 1 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi());
                                case 2 ->
                                    mainFrame.switchFrame(new RiwayatLogin());
                            }
                        }
                        case 3 ->
                            mainFrame.switchFrame(new DaftarKaryawan());
                        case 4 ->
                            mainFrame.switchFrame(new DaftarBrand());
                        case 5 ->
                            mainFrame.switchFrame(new DaftarHandphone());
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
                });
    }

    private void menuKaryawan(SimpleMenuOption simpleMenuOption, String[][] menuOptions, String[] icons) {
        boolean isLightMode = !Variable.isDarkTheme();
        simpleMenuOption
                .setMenus(menuOptions)
                .setBaseIconPath("TokoHp/Icons")
                .setIcons(icons)
                .addMenuEvent((MenuAction action, int i, int i1) -> {
                    switch (i) {
                        case 0 ->
                            mainFrame.switchFrame(new ProfileUser());
                        case 1 ->
                            mainFrame.switchFrame(new TransaksiBaru());
                        case 2 ->
                            mainFrame.switchFrame(new RiwayatTransaksi());
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

//                            mainFrame.dispose();
//                            new MainFrame().setVisible(true);
                        }
                        case 4 -> {
                            mainFrame.dispose();
                            new LoginPanel().setVisible(true);
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
        boolean isAdmin = checkAccount();
        boolean isLightTheme = !Variable.isDarkTheme();
        SimpleMenuOption simpleMenuOption = new SimpleMenuOption();

        String[][] menuOptions = {
            {"~AKUN~"},
            {"Pengaturan Akun"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat", "Riwayat Transaksi", "Riwayat Login"},
            {"~MANAJEMEN~"},
            {"Karyawan"},
            {"Brand"},
            {"Produk"},
            {"~LAINNYA~"},
            {isLightTheme ? "Dark Mode" : "Light Mode"},
            {"Logout"}
        };

        String[] icons = {
            "person.svg",
            "bag_plus.svg",
            "history.svg",
            "people.svg",
            "tags.svg",
            "phone.svg",
            isLightTheme ? "moon.svg" : "sun.svg",
            "logout.svg"
        };

        String[][] menuOptionsKaryawan = {
            {"~AKUN~"},
            {"Pengaturan Akun"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat Transaksi"},
            {"~LAINNYA~"},
            {isLightTheme ? "Dark Mode" : "Light Mode"},
            {"Logout"}
        };

        String[] iconsKaryawan = {
            "person.svg",
            "bag_plus.svg",
            "history.svg",
            isLightTheme ? "moon.svg" : "sun.svg",
            "logout.svg"
        };

        if (isAdmin) {
            menuAdmin(simpleMenuOption, menuOptions, icons);
        } else {
            menuKaryawan(simpleMenuOption, menuOptionsKaryawan, iconsKaryawan);
        }
        return simpleMenuOption;
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

}
