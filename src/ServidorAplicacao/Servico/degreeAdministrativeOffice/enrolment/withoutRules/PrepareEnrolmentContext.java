package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 10/Jun/2003
 */

public class PrepareEnrolmentContext implements IServico {

	private static PrepareEnrolmentContext _servico = new PrepareEnrolmentContext();

	public static PrepareEnrolmentContext getService() {
		return _servico;
	}

	private PrepareEnrolmentContext() {
	}

	public final String getNome() {
		return "PrepareEnrolmentContext";
	}

	public InfoEnrolmentContext run(InfoStudent infoStudent, InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree/*, List listOfChosenCurricularSemesters*/, List listOfChosenCurricularYears) throws FenixServiceException {
		InfoEnrolmentContext infoEnrolmentContext = null;
		IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
		ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
		try {
			EnrolmentContext enrolmentContext = EnrolmentContextManager.initialEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(student, executionPeriod, executionDegree/*, listOfChosenCurricularSemesters*/, listOfChosenCurricularYears);
			infoEnrolmentContext = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoEnrolmentContext;
	}
}