package ServidorAplicacao.Servico.teacher;

import Dominio.BibliographicReference;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentBibliographicReference;
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

		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
			IPersistentBibliographicReference persistentBibliographicReference =
				persistentSupport.getIPersistentBibliographicReference();

			IExecutionCourse executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(new ExecutionCourse(infoExecutionCourseCode), false);

			IBibliographicReference newBibliographicReference = new BibliographicReference();

			persistentBibliographicReference.lockWrite(newBibliographicReference);
			newBibliographicReference.setExecutionCourse(executionCourse);
			newBibliographicReference.setTitle(newBibliographyTitle);
			newBibliographicReference.setAuthors(newBibliographyAuthors);
			newBibliographicReference.setReference(newBibliographyReference);
			newBibliographicReference.setYear(newBibliographyYear);
			newBibliographicReference.setOptional(newBibliographyOptional);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}