package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.util.Properties;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

import org.apache.log4j.Logger;
import org.apache.slide.common.SlideException;

public class CheckIsAliveService extends Service {

    private static final Logger logger = Logger.getLogger(CheckIsAliveService.class);

    private static final String SLIDE_CONTENT_DIR = "/EY43/EP81/EC39036/S4613/S4868/I6606";
    private static final String SLIDE_CONTENT_FILENAME = "Conceitos.pdf";
    private static boolean CHECK_DB = false;
    private static boolean CHECK_SLIDE = false;

    static {
        final String propertiesFilename = "/checkIsAlive.properties";
        final Properties properties = new Properties();
        try {
            PropertiesManager.loadProperties(properties, propertiesFilename);
            final String checkDB = properties.getProperty("script.isAlive.check.db");
            if ("true".equalsIgnoreCase(checkDB)) {
                CHECK_DB = true;
            }
            final String checkSlide = properties.getProperty("script.isAlive.check.slide");
            if ("true".equalsIgnoreCase(checkSlide)) {
                CHECK_SLIDE = true;
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }

        if (LogLevel.INFO) {
            logger.info("CheckIsAliveService - will check db: " + CHECK_DB);
            logger.info("CheckIsAliveService - will check slide: " + CHECK_SLIDE);
        }
    }

    public Boolean run() {
        try {
            if (CHECK_DB) {
                checkFenixDatabaseOps();
            }
// Don't check slide db. The data has been moved to the dspace repository.
//            if (CHECK_SLIDE) {
//                checkSlideDatabaseOps();
//            }
            return Boolean.TRUE;
        } catch (Throwable t) {
            t.printStackTrace();
            if (LogLevel.FATAL) {
                logger.fatal("Got unexepected exception in check alive service. ", t);
            }
            throw new RuntimeException(t);
        }
    }

    private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        if (executionYear == null || executionYear.getIdInternal() == null) {
            if (LogLevel.FATAL) {
                logger.fatal("Got a null result checking fenix database.");
            }
            throw new RuntimeException("Problems accesing fenix database! Got a null result.");
        }
    }

    private void checkSlideDatabaseOps() throws SlideException {
        final FileSuportObject fileSuportObject = JdbcMysqlFileSupport.retrieveFile(SLIDE_CONTENT_DIR, SLIDE_CONTENT_FILENAME);

        if (fileSuportObject == null) {
            if (LogLevel.FATAL) {
                logger.fatal("Got a null result checking slide database.");
            }
            throw new RuntimeException("Problems accesing slide database! Got a null result.");
        }
    }

}
