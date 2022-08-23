package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;

public class LimitedClassLoader extends URLClassLoader {
    private OpenURLClassLoader parent;
    public LimitedClassLoader(URL[] urls, OpenURLClassLoader parent) {
        super(urls, parent);
        this.parent = parent;
    }

    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (name == null)
            throw new NullPointerException("name");

        if (name.startsWith("com.sunnysidesoft.VirtualTablet.core.VTService.") ||
                name.equals("com.facebook.GraphResponse"))
            return super.findClass(name);

        return parent.findClass(name);
    }
}
