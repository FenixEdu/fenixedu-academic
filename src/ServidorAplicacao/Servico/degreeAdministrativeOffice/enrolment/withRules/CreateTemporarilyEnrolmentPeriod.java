package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.EnrolmentPeriod;
import Dominio.IEnrolmentPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos 10/Jun/2003
 */

public class CreateTemporarilyEnrolmentPeriod implements IService
{

   

    public CreateTemporarilyEnrolmentPeriod()
    {
    }

    

    public void run(InfoExecutionPeriod infoExecutionPeriod, InfoStudent infoStudent)
            throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSupport
                    .getIPersistentEnrolmentPeriod();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport
                    .getIStudentCurricularPlanPersistente();

            IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);
            IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan
                    .readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

            IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO
                    .readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                            .getDegreeCurricularPlan());
            if (enrolmentPeriod == null)
            {
                enrolmentPeriod = enrolmentPeriodDAO
                        .readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                                .getDegreeCurricularPlan());
                if (enrolmentPeriod != null)
                {
                    enrolmentPeriodDAO.simpleLockWrite(enrolmentPeriod);
                    enrolmentPeriod.setStartDate(new Date());
                }
                else
                {
                    enrolmentPeriod = new EnrolmentPeriod();
                    enrolmentPeriodDAO.simpleLockWrite(enrolmentPeriod);
                    enrolmentPeriod.setDegreeCurricularPlan(studentActiveCurricularPlan
                            .getDegreeCurricularPlan());
                    enrolmentPeriod.setExecutionPeriod(Cloner
                            .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod));
                    enrolmentPeriod.setStartDate(new Date());
                    enrolmentPeriod.setEndDate(new Date());

                }
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}