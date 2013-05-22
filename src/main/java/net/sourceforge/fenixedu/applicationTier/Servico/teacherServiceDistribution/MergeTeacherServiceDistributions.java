package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class MergeTeacherServiceDistributions extends FenixService {
    protected void run(Integer tsdId, Integer otherGroupingId) {

        TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
        TeacherServiceDistribution otherGrouping = rootDomainObject.readTeacherServiceDistributionByOID(otherGroupingId);

        tsd.mergeWithGrouping(otherGrouping);
    }

    // Service Invokers migrated from Berserk

    private static final MergeTeacherServiceDistributions serviceInstance = new MergeTeacherServiceDistributions();

    @Service
    public static void runMergeTeacherServiceDistributions(Integer tsdId, Integer otherGroupingId) throws NotAuthorizedException {
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