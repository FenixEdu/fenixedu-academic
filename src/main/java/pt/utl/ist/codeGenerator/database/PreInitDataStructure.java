package pt.utl.ist.codeGenerator.database;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class PreInitDataStructure {

    public static void main(String[] args) {

        doIt();

        System.out.println("Initialization complete.");
        System.exit(0);
    }

    @Atomic
    private static void doIt() {
        Bennu.getInstance();
    }

}
