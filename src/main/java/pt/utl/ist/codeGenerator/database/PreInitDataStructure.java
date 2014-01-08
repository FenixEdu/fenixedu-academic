package pt.utl.ist.codeGenerator.database;

import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class PreInitDataStructure {

    private static final Logger logger = LoggerFactory.getLogger(PreInitDataStructure.class);

    public static void main(String[] args) {

        doIt();

        logger.info("Initialization complete.");
        System.exit(0);
    }

    @Atomic
    private static void doIt() {
        Bennu.getInstance();
    }

}
