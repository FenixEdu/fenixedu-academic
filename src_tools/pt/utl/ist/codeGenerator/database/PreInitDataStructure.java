package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.FenixFrameworkInitializer;

public class PreInitDataStructure {

    public static void main(String[] args) {

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(FenixFrameworkInitializer.CONFIG_PATH));

	RootDomainObject.init();

	System.out.println("Initialization complete.");
	System.exit(0);
    }

}
