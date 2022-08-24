package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

@CustomLog
public class Bootstrap {
    public static void main(String[] args) {
        log.i("Checking classpath...");
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

        try {
            OpenURLClassLoader coreClassLoader = new OpenURLClassLoader(new URL[]{coreJar.toURI().toURL()}, Bootstrap.class.getClassLoader());
            LimitedClassLoader hybridClassLoader = new LimitedClassLoader(new URL[]{protocolJar.toURI().toURL()}, coreClassLoader);

            Class<?> launcher = hybridClassLoader.findClass("sakura.kooi.VirtualGraphicTablets.server.core.Launch");
        } catch (ReflectiveOperationException | MalformedURLException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(null, "An error occurred while loading\n" + sw.toString(), "Launch failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
