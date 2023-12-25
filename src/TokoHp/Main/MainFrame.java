package TokoHp.Main;

import TokoHp.Objects.Variable;
import static TokoHp.Objects.Variable.installSideBarAndToast;
import TokoHp.Views.TransaksiBaru;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import raven.drawer.Drawer;

public class MainFrame extends javax.swing.JFrame {

    JInternalFrame internalFrame = new JInternalFrame();

    public MainFrame() {
        initComponents();
        init();
    }

    private void init() {
        setResizable(false);
        setSize(1216, 780);
        setContentPane(desktopPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menuBar.add(drawerOpenButton());
        switchFrame(new TransaksiBaru(), "Transaksi");
        installSideBarAndToast(MainFrame.this, new DrawerNavigation(MainFrame.this));
    }

    private JButton drawerOpenButton() {
        String directory = !Variable.isDarkTheme() ? "TokoHp/Icons/list.svg" : "TokoHp/Icons/list_dark.svg";
        JButton button = new JButton(new FlatSVGIcon(directory, 1.1f));
        button.addActionListener((l) -> {
            Drawer.getInstance().closeDrawer();
            Drawer.getInstance().showDrawer();
        });
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "background:null;");
        button.setMargin(new Insets(5, 5, 5, 5));
        return button;
    }

    public void switchFrame(JInternalFrame iFrame, String title) {
        internalFrame.dispose();
        internalFrame = iFrame;
        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
        BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI) iFrame.getUI();
        basicInternalFrameUI.setNorthPane(null);
        iFrame.setBorder(null);
        setTitle(title);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}
