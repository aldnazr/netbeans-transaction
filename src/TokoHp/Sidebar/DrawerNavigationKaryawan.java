package TokoHp.Sidebar;

import TokoHp.LoginPanel;
import TokoHp.Views.Main.KaryawanFrame;
import TokoHp.Views.ProfileUser;
import TokoHp.Views.RiwayatTransaksi;
import TokoHp.Views.TransaksiBaru;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.SimpleMenuOption;

public class DrawerNavigationKaryawan extends SimpleDrawerBuilder {

    final KaryawanFrame karyawanFrame;

    public DrawerNavigationKaryawan(KaryawanFrame karyawanFrame) {
        this.karyawanFrame = karyawanFrame;
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
            {"~LAINNYA~"},
            {"Logout"}
        };

        String[] icons = {
            "person.svg",
            "bag-plus.svg",
            "clock-history.svg",
            "box-arrow-left.svg"
        };

        return new SimpleMenuOption()
                .setMenus(menuOPtions)
                .setBaseIconPath("TokoHp/Icons")
                .setIcons(icons)
//                .setIconScale(0.035f)
                .setIconScale(1.1f)
                .addMenuEvent((MenuAction action, int i, int i1) -> {
                    switch (i) {
                        case 0 ->
                            karyawanFrame.switchFrame(new ProfileUser());
                        case 1 ->
                            karyawanFrame.switchFrame(new TransaksiBaru());
                        case 2 ->
                            karyawanFrame.switchFrame(new RiwayatTransaksi());
                        case 3 -> {
                            karyawanFrame.dispose();
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
