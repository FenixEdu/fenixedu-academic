import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrolment.degree.ShowAvailableCurricularCourses;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 31/Mar/2003
 */

public class InscTeste {

	public static void main(String[] args) {
		ShowAvailableCurricularCourses servico = ShowAvailableCurricularCourses.getService();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			try {
				IUserView userview = new UserView("user", null);
				InfoEnrolmentContext infoEnrolmentContext = servico.run(userview, new Integer(1));
				sp.confirmarTransaccao();
				System.out.println(infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
	}
}