package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules.depercated;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 */

public class ShowAvailableOptionalCurricularCoursesWithoutRules implements IServico {

	private static ShowAvailableOptionalCurricularCoursesWithoutRules _servico = new ShowAvailableOptionalCurricularCoursesWithoutRules();

	public static ShowAvailableOptionalCurricularCoursesWithoutRules getService() {
		return _servico;
	}

	private ShowAvailableOptionalCurricularCoursesWithoutRules() {
	}

	public final String getNome() {
		return "ShowAvailableOptionalCurricularCoursesWithoutRules";
	}

	public InfoEnrolmentContext run(InfoStudent infoStudent, InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {
		try {
			IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
			return EnrolmentContextManager.getInfoEnrolmentContext(EnrolmentContextManager.initialOptionalEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(student, executionPeriod));
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}