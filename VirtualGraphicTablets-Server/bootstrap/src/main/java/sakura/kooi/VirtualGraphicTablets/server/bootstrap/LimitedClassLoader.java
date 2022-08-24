package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;

public class LimitedClassLoader extends URLClassLoader {
    public LimitedClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name == null)
            throw new NullPointerException("name");

        if (name.startsWith("com.sunnysidesoft.VirtualTablet.core.VTService.") ||
                name.equals("com.facebook.GraphResponse"))
            return super.loadClass(name, resolve);

        return getParent().loadClass(name);
    }
}
