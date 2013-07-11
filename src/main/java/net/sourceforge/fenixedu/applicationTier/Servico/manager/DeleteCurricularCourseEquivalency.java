package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteCurricularCourseEquivalency {

    /*
     * ACCESSCONTROL
     * 
     * This method should check if the admin office should create the
     * equivalence or not
     */
    @Service
    public static void run(final String curricularCourseEquivalencyID) {
        final CurricularCourseEquivalence curricularCourseEquivalence =
                FenixFramework.getDomainObject(curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

    // Service Invokers migrated from Berserk

    @Service
    public static void runDeleteCurricularCourseEquivalency(String curricularCourseEquivalencyID) throws NotAuthorizedException {
        try {
            DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
            run(curricularCourseEquivalencyID);
        } catch (NotAuthorizedException ex1) {
            try {
                ManagerAuthorizationFilter.instance.execute();
                run(curricularCourseEquivalencyID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}