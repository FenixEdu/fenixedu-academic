package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author jpmsit, amak
 */
public class RemoveTeacherFromTeacherServiceDistributions {

    protected void run(Integer tsdId, Integer tsdTeacherId) throws FenixServiceException {

        TeacherServiceDistribution tsd = AbstractDomainObject.fromExternalId(tsdId);
        TSDTeacher tsdTeacher = AbstractDomainObject.fromExternalId(tsdTeacherId);

        tsd.removeTSDTeacherFromAllChilds(tsdTeacher);
        if (tsd.getIsRoot()) {
            tsdTeacher.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final RemoveTeacherFromTeacherServiceDistributions serviceInstance =
            new RemoveTeacherFromTeacherServiceDistributions();

    @Service
    public static void runRemoveTeacherFromTeacherServiceDistributions(Integer tsdId, Integer tsdTeacherId)
            throws FenixServiceException, NotAuthorizedException {
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