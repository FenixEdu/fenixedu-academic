package ServidorAplicacao.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 *  
 */
public class ReadActiveDegreeCurricularPlanByExecutionDegreeCode implements IServico {

	private static ReadActiveDegreeCurricularPlanByExecutionDegreeCode service =
		new ReadActiveDegreeCurricularPlanByExecutionDegreeCode();

	public static ReadActiveDegreeCurricularPlanByExecutionDegreeCode getService() {

		return service;
	}

	private ReadActiveDegreeCurricularPlanByExecutionDegreeCode() {

	}

	public final String getNome() {

		return "ReadActiveDegreeCurricularPlanByExecutionDegreeCode";
	}

	public InfoDegreeCurricularPlan run(Integer executionDegreeCode) throws FenixServiceException {

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

			if (executionDegreeCode == null) {
				throw new FenixServiceException("nullDegree");
			}

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeCode);
			executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);

			if (executionDegree == null) {
				throw new NonExistingServiceException();
			}
			IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();
			if (degreeCurricularPlan != null) {
				List allCurricularCourses =
					sp.getIPersistentCurricularCourse().readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);

				if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {

					Iterator iterator = allCurricularCourses.iterator();
					while (iterator.hasNext()) {
						ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

						List curricularCourseScopes =
							(List) sp.getIPersistentCurricularCourseScope().readActiveCurricularCourseScopesByCurricularCourse(curricularCourse);

						if (curricularCourseScopes != null) {
							curricularCourse.setScopes(curricularCourseScopes);
						}
					}
					infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(executionDegree, allCurricularCourses);
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} 
		return infoDegreeCurricularPlan;
	}

	private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(ICursoExecucao executionDegree, List allCurricularCourses) {
		InfoDegreeCurricularPlan infoDegreeCurricularPlan;
		infoDegreeCurricularPlan = Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree.getCurricularPlan());

		CollectionUtils.transform(allCurricularCourses, new Transformer() {
			public Object transform(Object arg0) {
				ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
				CollectionUtils.transform(curricularCourse.getScopes(), new Transformer() {
					public Object transform(Object arg0) {
						ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
						return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
					}
				});

				InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourse.setInfoScopes(curricularCourse.getScopes());
				return infoCurricularCourse;
			}
		});

		infoDegreeCurricularPlan.setCurricularCourses(allCurricularCourses);
		return infoDegreeCurricularPlan;
	}
}