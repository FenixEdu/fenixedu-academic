package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class CheckIsAliveService {

    private static final Logger logger = LoggerFactory.getLogger(CheckIsAliveService.class);

    @Atomic
    public static Boolean run() {
        try {
            checkFenixDatabaseOps();
            return Boolean.TRUE;
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error("Got unexepected exception in check alive service. ", t);
            throw new RuntimeException(t);
        }
    }

    private static void checkFenixDatabaseOps() {
//
//        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
//
//        if (executionYear == null || executionYear.getExternalId() == null) {
//            logger.error("Got a null result checking fenix database.");
//            throw new RuntimeException("Problems accesing fenix database! Got a null result.");
//        }
    }

}