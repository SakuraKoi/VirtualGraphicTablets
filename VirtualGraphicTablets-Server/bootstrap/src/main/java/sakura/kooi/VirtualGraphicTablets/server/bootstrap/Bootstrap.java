package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class Bootstrap {
    public static void main(String[] args) throws MalformedURLException {
        log.info("Checking classpath...");
        File libsDir = new File("libs");
        File coreJar = new File(libsDir, "vgt-core.jar");
        if (!coreJar.exists()) {
            JOptionPane.showMessageDialog(null, "VirtualGraphicTablets Core not found\nPath: ./libs/vgt-core.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File protocolJar = new File(libsDir, "thrift-protocol.jar");
        if (!protocolJar.exists()) {
            JOptionPane.showMessageDialog(null, "VirtualTablet thrift protocol not found\nPath: ./libs/thrift-protocol.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OpenURLClassLoader coreLoader = new OpenURLClassLoader(new URL[] {coreJar.toURL()}, Bootstrap.class.getClassLoader());
        // TODO create a hybrid class loader
        // TODO first lookup core then VirtualTablets apk
        // aim: maven thrift artifact packed with core module instead of dex2jared thrift from apk

    }
}
