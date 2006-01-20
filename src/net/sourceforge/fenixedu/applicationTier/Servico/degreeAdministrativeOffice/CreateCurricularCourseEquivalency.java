package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;

public class CreateCurricularCourseEquivalency extends Service {

    public void run(final Integer degreeCurricularPlanID, final Integer curricularCourseID, 
            final Integer oldCurricularCourseID) throws ExcepcaoPersistencia {
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
        final IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
        		.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        final CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseID);
        final CurricularCourse oldCurricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, oldCurricularCourseID);

        DomainFactory.makeCurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, oldCurricularCourse);
    }

}
