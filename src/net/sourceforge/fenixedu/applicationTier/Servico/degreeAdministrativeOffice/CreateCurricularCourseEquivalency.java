package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularCourseEquivalency extends Service {

    public void run(final Integer degreeCurricularPlanID, final Integer curricularCourseID, 
            final Integer oldCurricularCourseID) throws ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject
        		.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        final CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(CurricularCourse.class, curricularCourseID);
        final CurricularCourse oldCurricularCourse = (CurricularCourse) persistentObject.readByOID(CurricularCourse.class, oldCurricularCourseID);

        DomainFactory.makeCurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, oldCurricularCourse);
    }

}
