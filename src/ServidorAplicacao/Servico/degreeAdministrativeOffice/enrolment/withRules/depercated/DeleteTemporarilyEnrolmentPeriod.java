package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules.depercated;

import java.util.Date;

import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.EnrolmentPeriod;
import Dominio.IEnrolmentPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author David Santos
 * 10/Jun/2003
 */

public class DeleteTemporarilyEnrolmentPeriod implements IServico {

	private static DeleteTemporarilyEnrolmentPeriod _servico = new DeleteTemporarilyEnrolmentPeriod();

	public static DeleteTemporarilyEnrolmentPeriod getService() {
		return _servico;
	}

	private DeleteTemporarilyEnrolmentPeriod() {
	}

	public final String getNome() {
		return "DeleteTemporarilyEnrolmentPeriod";
	}

	public void run(InfoExecutionPeriod infoExecutionPeriod, InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSupport.getIPersistentEnrolmentPeriod();
			
			EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

			IEnrolmentPeriod enrolmentPeriod = new EnrolmentPeriod();
			enrolmentPeriod.setDegreeCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan());
			enrolmentPeriod.setExecutionPeriod(Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod));
			enrolmentPeriod.setStartDate(new Date());
			enrolmentPeriod.setEndDate(new Date());

			enrolmentPeriodDAO.deleteByCriteria(enrolmentPeriod);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}