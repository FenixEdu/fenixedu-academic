package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class PreInitDataStructure {

    public static void main(String[] args) {

        doIt();

        System.out.println("Initialization complete.");
        System.exit(0);
    }

    @Atomic
    private static void doIt() {
        RootDomainObject.initialize();
    }

}
