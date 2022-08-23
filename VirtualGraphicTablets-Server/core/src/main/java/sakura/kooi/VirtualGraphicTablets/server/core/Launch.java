package sakura.kooi.VirtualGraphicTablets.server.core;

import sakura.kooi.lib.swing.ui.CustomFlatMaterialDarkLaf;

import javax.swing.*;

public class Launch {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        CustomFlatMaterialDarkLaf.setup();
        VTabletServer window = new VTabletServer();
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
