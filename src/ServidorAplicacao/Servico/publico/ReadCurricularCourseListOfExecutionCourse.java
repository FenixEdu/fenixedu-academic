package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse implements IServico {

	private static ReadCurricularCourseListOfExecutionCourse _servico = new ReadCurricularCourseListOfExecutionCourse();

	/**
	  * The actor of this class.
	  **/

	private ReadCurricularCourseListOfExecutionCourse() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadCurricularCourseListOfExecutionCourse";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadCurricularCourseListOfExecutionCourse getService() {
		return _servico;
	}

	public Object run(InfoExecutionCourse infoExecCourse) throws ExcepcaoInexistente, FenixServiceException {

		List infoCurricularCourseList = new ArrayList();
		List infoCurricularCourseScopeList = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecCourse);
			executionCourse =
				executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(executionCourse.getSigla(), executionCourse.getExecutionPeriod());
			if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null)
				for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
					ICurricularCourse curricularCourse = (ICurricularCourse) executionCourse.getAssociatedCurricularCourses().get(i);
					InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
					
					for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
						ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(j);
						InfoCurricularCourseScope infoCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
						infoCurricularCourseScopeList.add(infoCurricularCourseScope);
					}

					infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
					infoCurricularCourseList.add(infoCurricularCourse);
					
				}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoCurricularCourseList;
	}

}
