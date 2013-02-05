package pt.ist.fenixframework;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import net.sourceforge.fenixedu._development.PropertiesManager;
import pt.ist.fenixWebFramework.FenixWebFramework;

public class FenixFrameworkInitializer {

    public static String CONFIG_PATH;

    private static File getApplicationDir() {
        final URL url = FenixFrameworkInitializer.class.getResource("/configuration.properties");
        try {
            return new File(url.toURI()).getParentFile();
        } catch (final URISyntaxException e) {
            throw new Error(e);
        }
    }

    static {
        System.out.println("Initializing fenix.");

        final String preInitClassnames = PropertiesManager.getProperty("pre.init.classnames");
        if (preInitClassnames != null) {
            final String[] classnames = preInitClassnames.split(",");
            for (final String classname : classnames) {
                if (classname != null && !classname.isEmpty()) {
                    try {
                        Class.forName(classname.trim());
                    } catch (final ClassNotFoundException e) {
                        throw new Error(e);
                    }
                }
            }
        }

        final File dir = getApplicationDir();
        try {
            CONFIG_PATH = dir.getCanonicalPath() + File.separatorChar + "domain_model.dml";
        } catch (final IOException e) {
            throw new Error(e);
        }

        try {
            FenixWebFramework.bootStrap(PropertiesManager.getFenixFrameworkConfig(CONFIG_PATH));
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }

    }

}
