package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class CheckIsAliveService {

    private static final Logger logger = LoggerFactory.getLogger(CheckIsAliveService.class);

    private static boolean CHECK_DB = FenixConfigurationManager.getConfiguration().getScriptIsAliveCheckDB();

    static {
        if (LogLevel.INFO) {
            logger.info("CheckIsAliveService - will check db: " + CHECK_DB);
        }
    }

    @Atomic
    public static Boolean run() {
        try {
            if (CHECK_DB) {
                checkFenixDatabaseOps();
            }
            return Boolean.TRUE;
        } catch (Throwable t) {
            t.printStackTrace();
            if (LogLevel.FATAL) {
                logger.error("Got unexepected exception in check alive service. ", t);
            }
            throw new RuntimeException(t);
        }
    }

    private static void checkFenixDatabaseOps() {

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        if (executionYear == null || executionYear.getExternalId() == null) {
            if (LogLevel.FATAL) {
                logger.error("Got a null result checking fenix database.");
            }
            throw new RuntimeException("Problems accesing fenix database! Got a null result.");
        }
    }

}