package TokoHp.Sidebar;

import TokoHp.LoginPanel;
import TokoHp.Views.DaftarBrand;
import TokoHp.Views.DaftarHandphone;
import TokoHp.Views.DaftarKaryawan;
import TokoHp.Views.Main.AdminFrame;
import TokoHp.Views.ProfileUser;
import TokoHp.Views.RiwayatTransaksi;
import TokoHp.Views.TransaksiBaru;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.SimpleMenuOption;

public class DrawerNavigationAdmin extends SimpleDrawerBuilder {

    final AdminFrame adminFrame;

    public DrawerNavigationAdmin(AdminFrame adminFrame) {
        this.adminFrame = adminFrame;
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setTitle("Abc");
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {
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
            {"Logout"}
        };

        String[] icons = {
            "person.svg",
            "bag-plus.svg",
            "clock-history.svg",
            "people.svg",
            "tags.svg",
            "phone.svg",
            "box-arrow-left.svg"
        };

        return new SimpleMenuOption()
                .setMenus(menuOPtions)
                .setBaseIconPath("TokoHp/Icons")
                .setIcons(icons)
                .addMenuEvent((MenuAction action, int i, int i1) -> {
                    switch (i) {
                        case 0 ->
                            adminFrame.switchFrame(new ProfileUser());
                        case 1 ->
                            adminFrame.switchFrame(new TransaksiBaru());
                        case 2 ->
                            adminFrame.switchFrame(new RiwayatTransaksi());
                        case 3 ->
                            adminFrame.switchFrame(new DaftarKaryawan());
                        case 4 ->
                            adminFrame.switchFrame(new DaftarBrand());
                        case 5 ->
                            adminFrame.switchFrame(new DaftarHandphone());
                        case 6 -> {
                            adminFrame.dispose();
                            new LoginPanel().setVisible(true);
                        }
                    }
        });
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

}
