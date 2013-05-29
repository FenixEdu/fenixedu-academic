package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteTSDProfessorship {
    protected void run(String tsdProfessorshipId) {
        TSDProfessorship tsdProfessorship = AbstractDomainObject.fromExternalId(tsdProfessorshipId);
        TSDTeacher tsdTeacher = tsdProfessorship.getTSDTeacher();
        TSDCourse tsdCourse = tsdProfessorship.getTSDCourse();

        for (TSDProfessorship professorship : tsdTeacher.getTSDProfessorShipsByCourse(tsdCourse)) {
            professorship.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTSDProfessorship serviceInstance = new DeleteTSDProfessorship();

    @Service
    public static void runDeleteTSDProfessorship(String tsdProfessorshipId) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdProfessorshipId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdProfessorshipId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdProfessorshipId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}