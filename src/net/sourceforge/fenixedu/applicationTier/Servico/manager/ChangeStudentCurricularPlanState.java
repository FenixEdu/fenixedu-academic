/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ChangeStudentCurricularPlanState implements IService {

    public void run(final Integer studentCurricularPlanId,
            final StudentCurricularPlanState studentCurricularPlanState) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);
        final IStudent student = studentCurricularPlan.getStudent();
        final IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        final List studentCurricularPlans = student.getStudentCurricularPlans();
        for (final Iterator iterator = studentCurricularPlans.iterator(); iterator.hasNext();) {
            final IStudentCurricularPlan othertudentCurricularPlan = (IStudentCurricularPlan) iterator
                    .next();
            final IDegreeCurricularPlan associatedDegreeCurricularCourse = othertudentCurricularPlan
                    .getDegreeCurricularPlan();
            if (associatedDegreeCurricularCourse.getIdInternal().equals(
                    degreeCurricularPlan.getIdInternal())
                    && othertudentCurricularPlan.getCurrentState().equals(studentCurricularPlanState)) {
                throw new ExistingServiceException("student.curricular.plan.already.exists");
            }
        }
        persistentStudentCurricularPlan.simpleLockWrite(studentCurricularPlan);

        studentCurricularPlan.setCurrentState(studentCurricularPlanState);

    }

}
