package ServidorAplicacao.Servico.enrolment.degree;

import java.util.Iterator;
import java.util.List;

import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ChangeEnrolmentStateFromTemporarilyToEnroled implements IServico {

	private static ChangeEnrolmentStateFromTemporarilyToEnroled _servico =
		new ChangeEnrolmentStateFromTemporarilyToEnroled();
	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeEnrolmentStateFromTemporarilyToEnroled getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeEnrolmentStateFromTemporarilyToEnroled() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ChangeEnrolmentStateFromTemporarilyToEnroled";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public void run(IUserView userView) throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO =	persistentSupport.getIPersistentStudent();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =	persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment =	persistentSupport.getIPersistentEnrolment();

			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(),student.getDegreeType());
			List TemporarilyEnrolemts = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentActiveCurricularPlan, EnrolmentState.TEMPORARILY_ENROLED_OBJ);
			
			Iterator iterator = TemporarilyEnrolemts.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment)iterator.next();
				enrolment.setState(EnrolmentState.ENROLED_OBJ);
				persistentEnrolment.lockWrite(enrolment);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		} catch (IllegalStateException ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		}

	}
}