package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules.depercated;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos 10/Jun/2003
 */

public class PrepareEnrolmentContext implements IService
{

    public PrepareEnrolmentContext()
    {
    }

    public InfoEnrolmentContext run(
        InfoStudent infoStudent,
        InfoExecutionPeriod infoExecutionPeriod,
        InfoExecutionDegree infoExecutionDegree,
        List listOfChosenCurricularSemesters,
        List listOfChosenCurricularYears)
        throws FenixServiceException
    {
        InfoEnrolmentContext infoEnrolmentContext = null;
        IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
        IExecutionPeriod executionPeriod =
            Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
        ICursoExecucao executionDegree =
            Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
        try
        {
            EnrolmentContext enrolmentContext =
                EnrolmentContextManager
                    .initialEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(
                    student,
                    executionPeriod,
                    executionDegree,
                    listOfChosenCurricularSemesters,
                    listOfChosenCurricularYears);
            infoEnrolmentContext = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return infoEnrolmentContext;
    }
}