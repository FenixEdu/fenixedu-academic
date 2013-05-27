package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class AssociateTSDTeacherWithTeacherServiceDistribution {
    protected void run(Integer tsdId, Integer tsdTeacherId) {
        TeacherServiceDistribution tsd = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdId);

        if (tsdTeacherId == null) {
            tsd.getTSDTeachersSet().addAll(tsd.getParent().getTSDTeachers());
        } else {
            tsd.addTSDTeachers(RootDomainObject.getInstance().readTSDTeacherByOID(tsdTeacherId));
        }
    }

    // Service Invokers migrated from Berserk

    private static final AssociateTSDTeacherWithTeacherServiceDistribution serviceInstance =
            new AssociateTSDTeacherWithTeacherServiceDistribution();

    @Service
    public static void runAssociateTSDTeacherWithTeacherServiceDistribution(Integer tsdId, Integer tsdTeacherId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, tsdTeacherId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, tsdTeacherId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, tsdTeacherId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}