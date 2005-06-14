package net.sourceforge.fenixedu.applicationTier.Servico;

import org.apache.slide.common.SlideException;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CheckIsAliveService implements IService {

    private static final String SLIDE_CONTENT_FILENAME = "/EY43/EP81/EC39036/S4613/S4868/I6606/Conceitos.pdf";

    public Boolean run() throws ExcepcaoPersistencia, SlideException {
        checkFenixDatabaseOps();
        checkSlideDatabaseOps();
        return new Boolean(true);
    }

    private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (executionYear.getIdInternal() == null) {
            throw new RuntimeException("Problems accesing fenix database!");
        }
    }

    private void checkSlideDatabaseOps() throws SlideException {
        final IFileSuport fileSuport = FileSuport.getInstance();

        final FileSuportObject fileSuportObject = fileSuport.retrieveFile(SLIDE_CONTENT_FILENAME);

        if (fileSuportObject == null) {
            throw new RuntimeException("Problems accesing slide database!");
        }
    }

}
