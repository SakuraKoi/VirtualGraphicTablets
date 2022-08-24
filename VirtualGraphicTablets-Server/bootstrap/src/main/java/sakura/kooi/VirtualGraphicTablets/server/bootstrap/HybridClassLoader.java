package sakura.kooi.VirtualGraphicTablets.server.bootstrap;

public class HybridClassLoader extends ClassLoader {
    private final ClassLoader clPreload;
    private final ClassLoader clBase;
    public HybridClassLoader(ClassLoader x, ClassLoader ctx) {
        clPreload = x;
        clBase = ctx;
    }



    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return Bootstrap.class.getClassLoader().loadClass(name);
        } catch (ClassNotFoundException ignored) {
        }

        if (clPreload != null) {
            try {
                return clPreload.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (clBase != null) {
            try {
                return clBase.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(name);
    }

}
