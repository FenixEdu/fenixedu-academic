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

	public Boolean run() throws ExcepcaoPersistencia, SlideException {
		checkFenixDatabaseOps();
		checkSlideDatabaseOps();
		return new Boolean(true);
	}

	private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {
		final ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentExecutionYear persistentExecutionYear = persistenceSupport.getIPersistentExecutionYear();

		final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

		if (executionYear.getIdInternal() == null) {
			throw new RuntimeException("Problems accesing fenix database!");
		}
	}

	private void checkSlideDatabaseOps() throws SlideException {
//		final IFileSuport fileSuport = FileSuport.getInstance();
//
//		final FileSuportObject fileSuportObject = createFileSupportObject();
//		fileSuport.storeFile(fileSuportObject);
//
//		fileSuport.deleteFile("/" + fileSuportObject.getFileName());
	}

	private FileSuportObject createFileSupportObject() {
		final String sortOfRandomString = getClass().getName() + System.currentTimeMillis();

		final FileSuportObject fileSuportObject = new FileSuportObject();
		fileSuportObject.setContent(new byte[0]);
		fileSuportObject.setContentType("SomeType");
		fileSuportObject.setFileName(sortOfRandomString);
		fileSuportObject.setLinkName(sortOfRandomString);
		fileSuportObject.setRootUri(sortOfRandomString);
		fileSuportObject.setUri(sortOfRandomString);

		return fileSuportObject;
	}

}
