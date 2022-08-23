package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;

public class OpenURLClassLoader extends URLClassLoader {
    public OpenURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public Class<?> findClass(final String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
