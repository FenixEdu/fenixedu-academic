/*
 * Created on 6/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.publico;

import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.SiteView;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ExamSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 *
 */
public class ExamSiteComponentService implements IServico {

	private static ExamSiteComponentService _servico =
		new ExamSiteComponentService();

	/**
	  * The actor of this class.
	  **/

	private ExamSiteComponentService() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ExamSiteComponentService";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ExamSiteComponentService getService() {
		return _servico;
	}

	public Object run(
		ISiteComponent bodyComponent,
		String executionYearName,
		String executionPeriodName,
		String degreeInitials,
		String nameDegreeCurricularPlan,
		List curricularYears)
		throws FenixServiceException {

		SiteView siteView = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear persistentExecutionYear =
				sp.getIPersistentExecutionYear();

			ICursoExecucaoPersistente executionDegreeDAO =
				sp.getICursoExecucaoPersistente();

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName(
					executionYearName);

			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					executionPeriodName,
					executionYear);

			ICursoExecucao executionDegree =
				executionDegreeDAO
					.readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
					degreeInitials,
					nameDegreeCurricularPlan,
					executionYear);
//			if (executionDegree != null) {
//				infoExecutionDegree =
//					Cloner.copyIExecutionDegree2InfoExecutionDegree(
//						executionDegree);
//			}
			ExamSiteComponentBuilder componentBuilder =
				ExamSiteComponentBuilder.getInstance();

			bodyComponent =
				componentBuilder.getComponent(bodyComponent,executionPeriod, executionDegree,curricularYears);
			siteView = new SiteView(bodyComponent);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return siteView;
	}

	private ITurma getDomainClass(
		String className,
		Integer curricularYear,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree,
		ISuportePersistente sp)
		throws ExcepcaoPersistencia, FenixServiceException {

		ITurmaPersistente persistentClass = sp.getITurmaPersistente();
		ITurma domainClass = null;

		if (curricularYear == null) {
			domainClass =
				persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod(
					className,
					executionDegree,
					executionPeriod);

		} else {
			domainClass = new Turma();
			domainClass.setAnoCurricular(curricularYear);
			domainClass.setExecutionDegree(executionDegree);
			domainClass.setExecutionPeriod(executionPeriod);

		}
		return domainClass;
	}
}
