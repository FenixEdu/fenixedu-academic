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
import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import Dominio.ISummary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

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

	public SiteView run(Integer executionCourseId,TipoAula summaryType) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse =
				new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentSummary persistentSummary =
				persistentSuport.getIPersistentSummary();
			List summaries;
			if (summaryType==null) {
				summaries =
								persistentSummary.readByExecutionCourse(executionCourse);
			}else {
				summaries =
								persistentSummary.readByExecutionCourseAndType(executionCourse,summaryType);
			}
			 
			List result = new ArrayList();
			Iterator iter = summaries.iterator();
			while (iter.hasNext()) {
				ISummary summary = (ISummary) iter.next();
				InfoSummary infoSummary =
					Cloner.copyISummary2InfoSummary(summary);
				result.add(infoSummary);
			}
			IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
			ISite site = persistentSite.readByExecutionCourse(executionCourse);
			
			InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
			bodyComponent.setInfoSummaries(result);
			bodyComponent.setExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
			bodyComponent.setSummaryType(summaryType);
            TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder.getInstance();
            ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);
			SiteView siteView = new ExecutionCourseSiteView(commonComponent,bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
