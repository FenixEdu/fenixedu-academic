/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISummary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 21/Jul/2003
 * fenix-head
 * ServidorAplicacao.Servico.teacher
 * 
 */
public class ReadSummaries implements IServico {

	private static ReadSummaries service = new ReadSummaries();

	public static ReadSummaries getService() {

		return service;
	}

	/**
	 * 
	 */
	public ReadSummaries() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadSummaries";
	}

	public SiteView run(Integer executionCourseId) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentSummary persistentSummary =
				persistentSuport.getIPersistentSummary();

			List summaries =
				persistentSummary.readByExecutionCourse(executionCourse);
			List result = new ArrayList();
			Iterator iter = summaries.iterator();
			while (iter.hasNext()) {
				ISummary summary = (ISummary) iter.next();
				InfoSummary infoSummary =
					Cloner.copyISummary2InfoSummary(summary);
				result.add(infoSummary);
			}
			
			
			
			InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
			bodyComponent.setInfoSummaries(result);
			bodyComponent.setExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
		
			SiteView siteView = new ExecutionCourseSiteView(bodyComponent,bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
