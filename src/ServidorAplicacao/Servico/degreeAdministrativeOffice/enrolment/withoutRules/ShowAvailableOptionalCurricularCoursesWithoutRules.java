package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

	public InfoEnrolmentContext run(IUserView userView) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO =	persistentSupport.getIPersistentStudent();
			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());
			return EnrolmentContextManager.getInfoEnrolmentContext(EnrolmentContextManager.initialOptionalEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(student));
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}