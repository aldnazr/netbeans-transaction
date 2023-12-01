package TokoHp.Main;

import TokoHp.LoginPanel;
import TokoHp.Objects.Variable;
import TokoHp.Views.DaftarBrand;
import TokoHp.Views.DaftarHandphone;
import TokoHp.Views.DaftarKaryawan;
import TokoHp.Views.ProfileUser;
import TokoHp.Views.RiwayatTransaksi;
import TokoHp.Views.TransaksiBaru;
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

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return headerData();
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {
        boolean isAdmin = checkAccount();
        SimpleMenuOption simpleMenuOption = new SimpleMenuOption();

        String[][] menuOPtions = {
            {"Atur Profil"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat Transaksi"},
            {"~MANAGE~"},
            {"Karyawan"},
            {"Brand"},
            {"Produk"},
            {"~LAINNYA~"},
            {"Dark Mode"},
            {"Logout"}
        };

        String[] icons = {
            "person.svg",
            "bag_plus.svg",
            "history.svg",
            "people.svg",
            "tags.svg",
            "phone.svg",
            "moon.svg",
            "logout.svg"
        };

        String[][] menuOPtionsDark = {
            {"Atur Profil"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat Transaksi"},
            {"~MANAGE~"},
            {"Karyawan"},
            {"Brand"},
            {"Produk"},
            {"~LAINNYA~"},
            {"Light Mode"},
            {"Logout"}
        };

        String[] iconsDark = {
            "person_dark.svg",
            "bag_plus_dark.svg",
            "history_dark.svg",
            "people_dark.svg",
            "tags_dark.svg",
            "phone_dark.svg",
            "sun.svg",
            "logout_dark.svg"
        };

        String[][] menuOPtionsKaryawan = {
            {"Atur Profil"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat Transaksi"},
            {"~LAINNYA~"},
            {"Dark Mode"},
            {"Logout"}
        };

        String[] iconsKaryawan = {
            "person.svg",
            "bag_plus.svg",
            "history.svg",
            "moon.svg",
            "logout.svg"
        };

        String[][] menuOPtionsKaryawanDark = {
            {"Atur Profil"},
            {"~TRANSAKSI~"},
            {"Transaksi Baru"},
            {"Riwayat Transaksi"},
            {"~LAINNYA~"},
            {"Light Mode"},
            {"Logout"}
        };

        String[] iconsKaryawanDark = {
            "person_dark.svg",
            "bag_plus_dark.svg",
            "history_dark.svg",
            "sun.svg",
            "logout_dark.svg"
        };

        if (isAdmin) {
            if (Variable.isLightTheme()) {
                simpleMenuOption
                        .setMenus(menuOPtions)
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
                                case 3 ->
                                    mainFrame.switchFrame(new DaftarKaryawan());
                                case 4 ->
                                    mainFrame.switchFrame(new DaftarBrand());
                                case 5 ->
                                    mainFrame.switchFrame(new DaftarHandphone());
                                case 6 -> {
                                    Variable.setDarkTheme();
                                    mainFrame.dispose();
                                    new MainFrame().setVisible(true);
                                }
                                case 7 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        });
            } else {
                simpleMenuOption
                        .setMenus(menuOPtionsDark)
                        .setBaseIconPath("TokoHp/Icons")
                        .setIcons(iconsDark)
                        .addMenuEvent((MenuAction action, int i, int i1) -> {
                            switch (i) {
                                case 0 ->
                                    mainFrame.switchFrame(new ProfileUser());
                                case 1 ->
                                    mainFrame.switchFrame(new TransaksiBaru());
                                case 2 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi());
                                case 3 ->
                                    mainFrame.switchFrame(new DaftarKaryawan());
                                case 4 ->
                                    mainFrame.switchFrame(new DaftarBrand());
                                case 5 ->
                                    mainFrame.switchFrame(new DaftarHandphone());
                                case 6 -> {
                                    Variable.setLightTheme();
                                    mainFrame.dispose();
                                    new MainFrame().setVisible(true);
                                }
                                case 7 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        });
            }
        } else {
            if (Variable.isLightTheme()) {
                simpleMenuOption
                        .setMenus(menuOPtionsKaryawan)
                        .setBaseIconPath("TokoHp/Icons")
                        .setIcons(iconsKaryawan)
                        .addMenuEvent((MenuAction action, int i, int i1) -> {
                            switch (i) {
                                case 0 ->
                                    mainFrame.switchFrame(new ProfileUser());
                                case 1 ->
                                    mainFrame.switchFrame(new TransaksiBaru());
                                case 2 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi());
                                case 3 -> {
                                    Variable.setDarkTheme();
                                    mainFrame.dispose();
                                    new MainFrame().setVisible(true);
                                }
                                case 4 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        });
            } else {
                simpleMenuOption
                        .setMenus(menuOPtionsKaryawanDark)
                        .setBaseIconPath("TokoHp/Icons")
                        .setIcons(iconsKaryawanDark)
                        .addMenuEvent((MenuAction action, int i, int i1) -> {
                            switch (i) {
                                case 0 ->
                                    mainFrame.switchFrame(new ProfileUser());
                                case 1 ->
                                    mainFrame.switchFrame(new TransaksiBaru());
                                case 2 ->
                                    mainFrame.switchFrame(new RiwayatTransaksi());
                                case 3 -> {
                                    Variable.setLightTheme();
                                    mainFrame.dispose();
                                    new MainFrame().setVisible(true);
                                }
                                case 4 -> {
                                    mainFrame.dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            }
                        });
            }

        }
        return simpleMenuOption;
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

}
