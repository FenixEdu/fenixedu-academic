package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalDegreesRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 * 
 * 9/Abr/2003
 */

public class ShowAvailableDegreesForOptionWithoutRules implements IService
{

    public ShowAvailableDegreesForOptionWithoutRules()
    {
    }

    public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext)
        throws FenixServiceException
    {

    	EnrolmentFilterAllOptionalDegreesRule enrolmentRule = new EnrolmentFilterAllOptionalDegreesRule();
        EnrolmentContext enrolmentContext =
            enrolmentRule.apply(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
        InfoEnrolmentContext infoEnrolmentContext2 =
            EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
        infoEnrolmentContext2.setInfoDegreesForOptionalCurricularCourses(
            this.filterByExecutionDegrees(
                infoEnrolmentContext2.getInfoDegreesForOptionalCurricularCourses(),
                infoEnrolmentContext2.getInfoExecutionPeriod()));
        return infoEnrolmentContext2;
    }

    // FIXME [DAVID]: Eventualmente esta filtragem deverá ser feita dentro da própria regra.
    private List filterByExecutionDegrees(List infoDegreesList, InfoExecutionPeriod infoExecutionPeriod)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree =
                persistentSupport.getICursoExecucaoPersistente();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
                persistentSupport.getIPersistentDegreeCurricularPlan();
            IExecutionPeriod executionPeriod =
                Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            List infoDegreesToRemove = new ArrayList();

            Iterator iterator = infoDegreesList.iterator();
            while (iterator.hasNext())
            {
                InfoDegree infoDegree = (InfoDegree) iterator.next();

                ICurso degreeCriteria = new Curso();
                degreeCriteria.setNome(infoDegree.getNome());
                degreeCriteria.setSigla(infoDegree.getSigla());
                degreeCriteria.setTipoCurso(infoDegree.getTipoCurso());
                IDegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
                degreeCurricularPlanCriteria.setDegree(degreeCriteria);
                degreeCurricularPlanCriteria.setState(DegreeCurricularPlanState.ACTIVE_OBJ);
                IDegreeCurricularPlan degreeCurricularPlan =
                    (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
                        degreeCurricularPlanCriteria);

                ICursoExecucao executionDegree =
                    persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(
                        degreeCurricularPlan,
                        executionPeriod.getExecutionYear());

                if (executionDegree == null)
                {
                    infoDegreesToRemove.add(infoDegree);
                }
            }

            infoDegreesList.removeAll(infoDegreesToRemove);

            return infoDegreesList;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}