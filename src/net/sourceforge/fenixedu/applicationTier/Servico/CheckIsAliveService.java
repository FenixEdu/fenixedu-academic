package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.util.Properties;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.log4j.Logger;
import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CheckIsAliveService implements IService {

    private static final Logger logger = Logger.getLogger(CheckIsAliveService.class);

    private static final String SLIDE_CONTENT_FILENAME = "/EY43/EP81/EC39036/S4613/S4868/I6606/Conceitos.pdf";
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
        }

        logger.info("CheckIsAliveService - will check db: " + CHECK_DB);
        logger.info("CheckIsAliveService - will check slide: " + CHECK_SLIDE);
    }

    public Boolean run() {
        try {
            if (CHECK_DB) {
                checkFenixDatabaseOps();
            }
            if (CHECK_SLIDE) {
                checkSlideDatabaseOps();
            }
            return new Boolean(true);
        } catch (Throwable t) {
            logger.fatal("Got unexepected exception in check alive service. ", t);
            throw new RuntimeException(t);
        }
    }

    private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (executionYear.getIdInternal() == null) {
            logger.fatal("Got a null result checking fenix database.");
            throw new RuntimeException("Problems accesing fenix database! Got a null result.");
        }
    }

    private void checkSlideDatabaseOps() throws SlideException {
        final IFileSuport fileSuport = FileSuport.getInstance();

        final FileSuportObject fileSuportObject = fileSuport.retrieveFile(SLIDE_CONTENT_FILENAME);

        if (fileSuportObject == null) {
            logger.fatal("Got a null result checking slide database.");
            throw new RuntimeException("Problems accesing slide database! Got a null result.");
        }
    }

}
