package ServidorAplicacao
	.Servico
	.masterDegree
	.administrativeOffice
	.student
	.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;

import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author João Mota
 * 2/Out/2003
 */

public class ReadPosGradStudentCurricularPlans implements IServico {

	private static ReadPosGradStudentCurricularPlans servico =
		new ReadPosGradStudentCurricularPlans();

	public static ReadPosGradStudentCurricularPlans getService() {
		return servico;
	}

	private ReadPosGradStudentCurricularPlans() {
	}

	public final String getNome() {
		return "ReadPosGradStudentCurricularPlans";
	}

	public List run(Integer studentId) throws FenixServiceException {
		List result = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = new Student(studentId);
			if (student.getDegreeType().getTipoCurso().intValue()
				== TipoCurso.MESTRADO) {
				student =
					(IStudent) persistentStudent.readByOId(student, false);
				if (student == null) {
					throw new InvalidArgumentsServiceException("invalidStudentId");
				}

				result.addAll(
					persistentStudentCurricularPlan
						.readByStudentNumberAndDegreeType(
						student.getNumber(),
						new TipoCurso(TipoCurso.MESTRADO)));
			} else {
				throw new NotAuthorizedException();
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return result;
	}
}