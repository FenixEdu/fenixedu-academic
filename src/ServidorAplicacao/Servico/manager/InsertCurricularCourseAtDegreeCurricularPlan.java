/*
 * Created on 8/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertCurricularCourseAtDegreeCurricularPlan implements IService
{

    public InsertCurricularCourseAtDegreeCurricularPlan()
    {
    }

    public void run(InfoCurricularCourse infoCurricularCourse) throws FenixServiceException
    {

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            Integer degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan()
                    .getIdInternal();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            if (degreeCurricularPlan == null)
                throw new NonExistingServiceException();

            String name = infoCurricularCourse.getName();
            String code = infoCurricularCourse.getCode();

            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();

            ICurricularCourse curricularCourse = new CurricularCourse();
            persistentCurricularCourse.simpleLockWrite(curricularCourse);
            curricularCourse.setBasic(infoCurricularCourse.getBasic());
            curricularCourse.setCode(code);
            curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
            curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
            curricularCourse.setName(name);
            curricularCourse.setType(infoCurricularCourse.getType());
            curricularCourse.setTheoreticalHours(infoCurricularCourse.getTheoreticalHours());
            curricularCourse.setTheoPratHours(infoCurricularCourse.getTheoPratHours());
            curricularCourse.setPraticalHours(infoCurricularCourse.getPraticalHours());
            curricularCourse.setLabHours(infoCurricularCourse.getLabHours());
            curricularCourse.setMaximumValueForAcumulatedEnrollments(infoCurricularCourse
                    .getMaximumValueForAcumulatedEnrollments());
            curricularCourse.setMinimumValueForAcumulatedEnrollments(infoCurricularCourse
                    .getMinimumValueForAcumulatedEnrollments());
            curricularCourse.setCredits(infoCurricularCourse.getCredits());
            curricularCourse.setEctsCredits(infoCurricularCourse.getEctsCredits());
            curricularCourse.setEnrollmentWeigth(infoCurricularCourse.getEnrollmentWeigth());

            curricularCourse.setAssociatedExecutionCourses(new ArrayList());

        }
        catch (ExistingPersistentException existingException)
        {
            throw new ExistingServiceException("A disciplina curricular "
                    + infoCurricularCourse.getName() + ", com código " + infoCurricularCourse.getCode(),
                    existingException);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}