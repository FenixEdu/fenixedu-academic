package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules.depercated;

import java.util.Date;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.EnrolmentPeriod;
import Dominio.IEnrolmentPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
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

	public void run(InfoExecutionPeriod infoExecutionPeriod, InfoStudent infoStudent) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSupport.getIPersistentEnrolmentPeriod();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();

			IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

			IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
			if(enrolmentPeriod == null) {
				enrolmentPeriod = enrolmentPeriodDAO.readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
				if(enrolmentPeriod != null) {
					enrolmentPeriodDAO.lockWrite(enrolmentPeriod);
					enrolmentPeriod.setStartDate(new Date());
				} else {
					enrolmentPeriod = new EnrolmentPeriod();
					enrolmentPeriod.setDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
					enrolmentPeriod.setExecutionPeriod(Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod));
					enrolmentPeriod.setStartDate(new Date());
					enrolmentPeriod.setEndDate(new Date());
					enrolmentPeriodDAO.lockWrite(enrolmentPeriod);
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}