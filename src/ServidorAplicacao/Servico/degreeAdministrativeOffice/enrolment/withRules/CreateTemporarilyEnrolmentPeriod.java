package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import java.util.Date;

import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.EnrolmentPeriod;
import Dominio.IEnrolmentPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author David Santos
 * 10/Jun/2003
 */

public class CreateTemporarilyEnrolmentPeriod implements IServico {

	private static CreateTemporarilyEnrolmentPeriod _servico = new CreateTemporarilyEnrolmentPeriod();

	public static CreateTemporarilyEnrolmentPeriod getService() {
		return _servico;
	}

	private CreateTemporarilyEnrolmentPeriod() {
	}

	public final String getNome() {
		return "CreateTemporarilyEnrolmentPeriod";
	}

	public void run(InfoExecutionPeriod infoExecutionPeriod, IUserView actor) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSupport.getIPersistentEnrolmentPeriod();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

			IStudent student = persistentStudent.readByUsername(actor.getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

			IEnrolmentPeriod enrolmentPeriod = new EnrolmentPeriod();
			enrolmentPeriod.setDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
			enrolmentPeriod.setEndDate(new Date());
			enrolmentPeriod.setExecutionPeriod(Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod));
			enrolmentPeriod.setStartDate(new Date());

			enrolmentPeriodDAO.lockWrite(enrolmentPeriod);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}