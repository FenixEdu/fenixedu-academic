package ServidorAplicacao.Servico.enrolment.degree;

import Dominio.IStudent;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ShowAvailableCurricularCourses implements IServico {

	private static ShowAvailableCurricularCourses _servico = new ShowAvailableCurricularCourses();
	/**
	 * The singleton access method of this class.
	 **/
	public static ShowAvailableCurricularCourses getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ShowAvailableCurricularCourses() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ShowAvailableCurricularCourses";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(IUserView userView, Integer semester) throws FenixServiceException {

		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());

			//			FIXME: David-Ricardo: ler o semestre do execution Period quando este tiver esta informacao
			IEnrolmentStrategy strategy =
				EnrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.initialEnrolmentContext(student, semester));

			return EnrolmentContextManager.getInfoEnrolmentContext(strategy.getAvailableCurricularCourses());

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}
	}
}