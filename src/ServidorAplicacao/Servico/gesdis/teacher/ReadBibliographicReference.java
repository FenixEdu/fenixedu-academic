/*
 * Created on 17/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.List;

import DataBeans.InfoExecutionCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadBibliographicReference implements IServico {

	private static ReadBibliographicReference service =
		new ReadBibliographicReference();

	public static ReadBibliographicReference getService() {
		return service;
	}

	private ReadBibliographicReference() {
	}

	public final String getNome() {
		return "ReadBibliographicReference";
	}

	public List run(InfoExecutionCourse infoExecutionCourse, Boolean optional)
		throws FenixServiceException {
		List references = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp.getIPersistentBibliographicReference();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =sp.getIDisciplinaExecucaoPersistente();

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName(
					infoExecutionCourse
						.getInfoExecutionPeriod()
						.getInfoExecutionYear()
						.getYear());
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					infoExecutionCourse.getInfoExecutionPeriod().getName(),
					executionYear);
			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);
			references =
				persistentBibliographicReference.readBibliographicReference(
					executionCourse);									

		} catch (ExistingPersistentException e) {
			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return references;
	}
}
