package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
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

	public InfoEnrolmentContext run(InfoStudent infoStudent) throws FenixServiceException {
		try {
			IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
			return EnrolmentContextManager.getInfoEnrolmentContext(EnrolmentContextManager.initialOptionalEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(student));
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}