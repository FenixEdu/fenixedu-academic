package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ListIterator;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 01/07/2003
 * 
 */
public class ReadExecutionCourseByCurricularCourseAndYear implements IServico {

	private static ReadExecutionCourseByCurricularCourseAndYear servico = new ReadExecutionCourseByCurricularCourseAndYear();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionCourseByCurricularCourseAndYear getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExecutionCourseByCurricularCourseAndYear() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadExecutionCourseByCurricularCourseAndYear";
	}

	public IDisciplinaExecucao run(String executionYearString, Integer curricularCourseCode) throws FenixServiceException {

		IDisciplinaExecucao executionCourse = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			// Get the Actual Execution Year
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

			// Read Execution Course
			ListIterator iterExecutionCourse = curricularCourse.getAssociatedExecutionCourses().listIterator();
			while (iterExecutionCourse.hasNext()) {
				IDisciplinaExecucao elem = (IDisciplinaExecucao) iterExecutionCourse.next();
				if (elem.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
					executionCourse = elem;
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return executionCourse;
	}
}
