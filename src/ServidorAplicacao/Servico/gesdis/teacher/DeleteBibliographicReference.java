/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoBibliographicReference;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IBibliographicReference;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeleteBibliographicReference implements IServico {

	private static DeleteBibliographicReference servico =
		new DeleteBibliographicReference();

	public static DeleteBibliographicReference getService() {
		return servico;
	}

	private DeleteBibliographicReference() {
	}

	public final String getNome() {
		return "DeleteBibliographicReference";
	}

	public Boolean run(
		InfoExecutionCourse infoExecutionCourse,
		InfoBibliographicReference infoBibliographicReference)
		throws FenixServiceException {
		try {
			String title = infoBibliographicReference.getTitle();
			String authors = infoBibliographicReference.getAuthors();
			String reference = infoBibliographicReference.getReference();
			String year = infoBibliographicReference.getYear();
			Boolean optional = infoBibliographicReference.getOptional();
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentBibliographicReference persistentBibliographicReference =
				null;
			persistentBibliographicReference =
				sp.getIPersistentBibliographicReference();
			IBibliographicReference bibliographicReference = null;
			bibliographicReference =
				persistentBibliographicReference.readBibliographicReference(
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse),
					title,
					authors,
					reference,
					year);
			if (bibliographicReference != null)
				persistentBibliographicReference.delete(bibliographicReference);								
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return new Boolean(true);
	}
}
