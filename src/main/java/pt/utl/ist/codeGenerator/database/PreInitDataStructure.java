package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.FenixFrameworkInitializer;

public class PreInitDataStructure {

    public static void main(String[] args) {

        try {
            Class.forName(FenixFrameworkInitializer.class.getName());
        } catch (ClassNotFoundException e) {
        }

        FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig());

        RootDomainObject.init();

        System.out.println("Initialization complete.");
        System.exit(0);
    }

}
