package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadStudentsByDegree implements IServico {

	private static ReadStudentsByDegree servico = new ReadStudentsByDegree();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentsByDegree getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentsByDegree() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadStudentsByDegree";
	}

	public List run(String executionYearString, String degreeName) throws FenixServiceException {
		List studentList = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			// Get the Actual Execution Year
			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);

			// Read degree
			ICursoExecucao executionDegree =
				sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(degreeName, executionYear);


			// Read Degree Curricular Plan
//			IDegreeCurricularPlan degreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan().readByExecutionDegree(executionDegree);
//System.out.println("degreeCurricularPlan" + degreeCurricularPlan);

			if (executionDegree.getCurricularPlan().getCurricularCourses() == null
				|| executionDegree.getCurricularPlan().getCurricularCourses().size() == 0) {
				throw new NonExistingServiceException();
			}

			studentList = new ArrayList();
			ListIterator iterator = executionDegree.getCurricularPlan().getCurricularCourses().listIterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourse.setInfoScopes((List) CollectionUtils.collect(curricularCourse.getScopes(), new Transformer() {

					public Object transform(Object arg0) {
						ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
						return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
					}
				}));
				studentList.add(infoCurricularCourse);
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return studentList;
	}
}
