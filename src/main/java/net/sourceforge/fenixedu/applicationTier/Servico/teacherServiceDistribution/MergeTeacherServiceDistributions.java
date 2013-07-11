package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class MergeTeacherServiceDistributions {
    protected void run(String tsdId, String otherGroupingId) {

        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        TeacherServiceDistribution otherGrouping = FenixFramework.getDomainObject(otherGroupingId);

        tsd.mergeWithGrouping(otherGrouping);
    }

    // Service Invokers migrated from Berserk

    private static final MergeTeacherServiceDistributions serviceInstance = new MergeTeacherServiceDistributions();

    @Service
    public static void runMergeTeacherServiceDistributions(String tsdId, String otherGroupingId) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, otherGroupingId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, otherGroupingId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, otherGroupingId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}