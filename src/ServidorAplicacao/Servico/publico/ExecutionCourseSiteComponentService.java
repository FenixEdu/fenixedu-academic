/*
 * Created on 6/Mai/2003
 *
 *
 */
package ServidorAplicacao.Servico.publico;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ExecutionCourseSiteComponentBuilder;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ExecutionCourseSiteComponentService implements IServico {

	private static ExecutionCourseSiteComponentService _servico =
		new ExecutionCourseSiteComponentService();

	/**
	  * The actor of this class.
	  **/

	private ExecutionCourseSiteComponentService() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ExecutionCourseSiteComponentService";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ExecutionCourseSiteComponentService getService() {
		return _servico;
	}

	public Object run(
		ISiteComponent commonComponent,
		ISiteComponent bodyComponent,		
		String executionYearName,
		String executionPeriodName,
		String executionCourseCode,
		Integer sectionIndex)
		throws FenixServiceException {
			ExecutionCourseSiteView siteView = null;
			
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear persistentExecutionYear =
				sp.getIPersistentExecutionYear();
			IPersistentSite persistentSite = sp.getIPersistentSite();

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName(
					executionYearName);
			
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					executionPeriodName,
					executionYear);

			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					executionCourseCode,
					executionPeriod);

			ISite site = persistentSite.readByExecutionCourse(executionCourse);

			ExecutionCourseSiteComponentBuilder componentBuilder =
				ExecutionCourseSiteComponentBuilder.getInstance();
			commonComponent = componentBuilder.getComponent(commonComponent,site,null,null);
			bodyComponent = componentBuilder.getComponent(bodyComponent,site,commonComponent,sectionIndex);
			
			siteView = new ExecutionCourseSiteView(commonComponent,bodyComponent);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return siteView;
	}
}
