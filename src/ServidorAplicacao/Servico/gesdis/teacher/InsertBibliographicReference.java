/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertBibliographicReference implements IServico {

	private static InsertBibliographicReference service =
		new InsertBibliographicReference();

	public static InsertBibliographicReference getService() {
		return service;
	}

	private InsertBibliographicReference() {
	}

	public final String getNome() {
		return "InsertBibliographicReference";
	}

	private void verifiesBibliographicReferenceExistence(
		IDisciplinaExecucao executionCourse,
		IBibliographicReference refBibli)
		throws ExcepcaoPersistencia {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentBibliographicReference persistentBiblioRef = null;
		persistentBiblioRef = sp.getIPersistentBibliographicReference();
		IBibliographicReference readBiblioRef = null;
		readBiblioRef =
			persistentBiblioRef.readBibliographicReference(
				executionCourse,
				refBibli.getTitle(),
				refBibli.getAuthors(),
				refBibli.getReference(),
				refBibli.getYear());
		if (readBiblioRef != null) {
			throw new ExcepcaoPersistencia();
		}
	}

	public void run(
		InfoExecutionCourse infoExecutionCourse,
		String title,
		String authors,
		String reference,
		String year,
		Boolean optional)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentBibliographicReference persistentBibliographicReference =
				null;
			persistentBibliographicReference =
				sp.getIPersistentBibliographicReference();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IBibliographicReference newBibliographicReference =
				new BibliographicReference();
			newBibliographicReference.setExecutionCourse(Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse));
			newBibliographicReference.setTitle(title);
			newBibliographicReference.setAuthors(authors);
			newBibliographicReference.setReference(reference);
			newBibliographicReference.setYear(year);
			newBibliographicReference.setOptional(optional);
			verifiesBibliographicReferenceExistence(
				Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse),
				newBibliographicReference);
			persistentBibliographicReference.lockWrite(
				newBibliographicReference);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException();
			}
	}
}
