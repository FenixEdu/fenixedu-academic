package ServidorAplicacao.Servico.teacher;

import Dominio.BibliographicReference;
import Dominio.DisciplinaExecucao;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Fernanda Quitério
 *  
 */
public class CreateBibliographicReference implements IServico {

	private static CreateBibliographicReference service = new CreateBibliographicReference();
	/**
	 * The singleton access method of this class.
	 */
	public static CreateBibliographicReference getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateBibliographicReference() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "CreateBibliographicReference";
	}
	/**
	 * Executes the service.
	 */
	public boolean run(
		Integer infoExecutionCourseCode,
		String newBibliographyTitle,
		String newBibliographyAuthors,
		String newBibliographyReference,
		String newBibliographyYear,
		Boolean newBibliographyOptional)
		throws FenixServiceException {
		ISite site = null;

		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
			IPersistentBibliographicReference persistentBibliographicReference =
				persistentSupport.getIPersistentBibliographicReference();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode), false);
			site = persistentSite.readByExecutionCourse(executionCourse);

			IBibliographicReference newBibliographicReference =
				new BibliographicReference(
					executionCourse,
					newBibliographyTitle,
					newBibliographyAuthors,
					newBibliographyReference,
					newBibliographyYear,
					newBibliographyOptional);
			persistentBibliographicReference.lockWrite(newBibliographicReference);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}