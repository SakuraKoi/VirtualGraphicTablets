package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import lombok.CustomLog;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@CustomLog
public class Bootstrap {
    public static void main(String[] args) {
        log.i("Checking classpath...");
        File libsDir = new File("libs");
        File coreJar = new File(libsDir, "vgt-core.jar");
        if (!coreJar.exists()) {
            log.c("vgt-core.jar not found in 'libs' directory.");
            JOptionPane.showMessageDialog(null, "VirtualGraphicTablets Core not found\nPath: ./libs/vgt-core.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File protocolJar = new File(libsDir, "thrift-protocol.jar");
        if (!protocolJar.exists()) {
            log.c("thrift-protocol.jar not found in 'libs' directory.");
            JOptionPane.showMessageDialog(null, "VirtualTablet thrift protocol not found\nPath: ./libs/thrift-protocol.jar", "Launch failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            log.i("Creating hybrid classloader...");
            URLClassLoader hybridClassLoader = new URLClassLoader(new URL[]{coreJar.toURI().toURL(), protocolJar.toURI().toURL()}, Bootstrap.class.getClassLoader());

            log.i("Initializing core module...");
            Class<?> launcher = Class.forName("sakura.kooi.VirtualGraphicTablets.server.core.Launch", true, hybridClassLoader);
            Method start = launcher.getMethod("start");
            log.s("Invoking Launch.start() ...");
            start.invoke(null);
        } catch (ReflectiveOperationException | MalformedURLException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(null, "An error occurred while loading\n" + sw.toString(), "Launch failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
