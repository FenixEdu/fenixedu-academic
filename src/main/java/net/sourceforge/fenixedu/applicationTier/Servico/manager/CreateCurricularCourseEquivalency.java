package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateCurricularCourseEquivalency {

    /*
     * ACCESSCONTROL
     * 
     * This method should check if the admin office should create the
     * equivalence or not
     */
    @Service
    public static void run(final Integer degreeCurricularPlanID, final Integer curricularCourseID,
            final Integer oldCurricularCourseID) {
        final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
        final CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseID);
        final CurricularCourse oldCurricularCourse =
                (CurricularCourse) AbstractDomainObject.fromExternalId(oldCurricularCourseID);

        new CurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, Collections.singleton(oldCurricularCourse));
    }

    // Service Invokers migrated from Berserk

    private static final CreateCurricularCourseEquivalency serviceInstance = new CreateCurricularCourseEquivalency();

    @Service
    public static void runCreateCurricularCourseEquivalency(Integer degreeCurricularPlanID, Integer curricularCourseID,
            Integer oldCurricularCourseID) throws NotAuthorizedException {
        try {
            DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(degreeCurricularPlanID, curricularCourseID, oldCurricularCourseID);
        } catch (NotAuthorizedException ex1) {
            try {
                ManagerAuthorizationFilter.instance.execute();
                serviceInstance.run(degreeCurricularPlanID, curricularCourseID, oldCurricularCourseID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}