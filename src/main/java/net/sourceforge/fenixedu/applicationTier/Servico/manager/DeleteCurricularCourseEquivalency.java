package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteCurricularCourseEquivalency {

    /*
     * ACCESSCONTROL
     * 
     * This method should check if the admin office should create the
     * equivalence or not
     */
    @Service
    public static void run(final Integer curricularCourseEquivalencyID) {
        final CurricularCourseEquivalence curricularCourseEquivalence =
                AbstractDomainObject.fromExternalId(curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteCurricularCourseEquivalency serviceInstance = new DeleteCurricularCourseEquivalency();

    @Service
    public static void runDeleteCurricularCourseEquivalency(Integer curricularCourseEquivalencyID) throws NotAuthorizedException {
        try {
            DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(curricularCourseEquivalencyID);
        } catch (NotAuthorizedException ex1) {
            try {
                ManagerAuthorizationFilter.instance.execute();
                serviceInstance.run(curricularCourseEquivalencyID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}