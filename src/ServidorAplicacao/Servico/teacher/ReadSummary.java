/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
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
public class ReadSummary implements IServico {

	private static ReadSummary service = new ReadSummary();

	public static ReadSummary getService() {

		return service;
	}

	/**
	 * 
	 */
	public ReadSummary() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadSummary";
	}

	public SiteView run(Integer executionCourseId,Integer summaryId) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentSummary persistentSummary =
				persistentSuport.getIPersistentSummary();

			ISummary summary = new Summary(summaryId);
			summary = (ISummary) persistentSummary.readByOId(summary, false);

			InfoSiteSummary bodyComponent = new InfoSiteSummary();
			bodyComponent.setInfoSummary(
				Cloner.copyISummary2InfoSummary(summary));
			bodyComponent.setExecutionCourse(
				(InfoExecutionCourse) Cloner.get(
					summary.getExecutionCourse()));

			SiteView siteView =
				new ExecutionCourseSiteView(bodyComponent, bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
