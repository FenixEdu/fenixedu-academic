/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan implements IService {

    public void run(final Integer studentCurricularPlanId) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);
        final List enrollments = studentCurricularPlan.getEnrolments();
        for (final Iterator iterator = enrollments.iterator(); iterator.hasNext();) {
            final IEnrollment enrollment = (IEnrollment) iterator.next();
            DeleteEnrollment.deleteAssociatedEnrollmentInformation(persistentSupport, enrollment);
            persistentEnrollment.delete(enrollment);
        }

        persistentStudentCurricularPlan.delete(studentCurricularPlan);
    }

}
