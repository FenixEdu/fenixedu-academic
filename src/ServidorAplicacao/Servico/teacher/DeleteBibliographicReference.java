package ServidorAplicacao.Servico.teacher;

import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteBibliographicReference implements IServico {

	private static DeleteBibliographicReference service = new DeleteBibliographicReference();

	public static DeleteBibliographicReference getService() {
		return service;
	}

	private DeleteBibliographicReference() {
	}

	public final String getNome() {
		return "DeleteBibliographicReference";
	}

	public boolean run(Integer infoExecutionCourseCode, Integer bibliographicReferenceCode) throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentBibliographicReference persistentBibliographicReference =
				persistentSupport.getIPersistentBibliographicReference();

			BibliographicReference bibliographicReference = new BibliographicReference(bibliographicReferenceCode);
			IBibliographicReference ibibliographicReference =
				(IBibliographicReference) persistentBibliographicReference.readByOId(bibliographicReference, false);

			if (ibibliographicReference != null)
				persistentBibliographicReference.delete(ibibliographicReference);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}