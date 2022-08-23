package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;

@Slf4j
public class Bootstrap {
    public static void main(String[] args) {
        log.info("Checking classpath...");
        File libsDir = new File("libs");
        File coreJar = new File(libsDir, "vgt-core.jar");
        if (!coreJar.exists()) {
            JOptionPane.showMessageDialog(null, "VirtualGraphicTablets Core not found\nPath: ./libs/vgt-core.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File protocolJar = new File(libsDir, "classes-dex2jar.jar");
        if (!protocolJar.exists()) {
            JOptionPane.showMessageDialog(null, "VirtualTablet thrift protocol not found\nPath: ./libs/classes-dex2jar.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO create a hybrid class loader
        // TODO first lookup core then VirtualTablets apk
        // aim: maven thrift artifact packed with core module instead of dex2jared thrift from apk

    }
}
