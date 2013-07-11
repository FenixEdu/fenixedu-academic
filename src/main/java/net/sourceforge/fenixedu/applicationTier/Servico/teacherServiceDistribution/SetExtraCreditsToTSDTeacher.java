package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class SetExtraCreditsToTSDTeacher {
    protected void run(String tsdTeacherId, String extraCreditsName, Double extraCreditsValue, Boolean usingExtraCredits) {

        TSDTeacher tsdTeacher = FenixFramework.getDomainObject(tsdTeacherId);

        tsdTeacher.setExtraCreditsName(extraCreditsName);
        tsdTeacher.setExtraCreditsValue(extraCreditsValue);
        tsdTeacher.setUsingExtraCredits(usingExtraCredits);
    }

    // Service Invokers migrated from Berserk

    private static final SetExtraCreditsToTSDTeacher serviceInstance = new SetExtraCreditsToTSDTeacher();

    @Service
    public static void runSetExtraCreditsToTSDTeacher(String tsdTeacherId, String extraCreditsName, Double extraCreditsValue,
            Boolean usingExtraCredits) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdTeacherId, extraCreditsName, extraCreditsValue, usingExtraCredits);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdTeacherId, extraCreditsName, extraCreditsValue, usingExtraCredits);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdTeacherId, extraCreditsName, extraCreditsValue, usingExtraCredits);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}