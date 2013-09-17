package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SetCoursesAndTeachersValuationPermission {
    protected void run(String tsdId, String personId, Boolean coursesValuationPermission, Boolean teachersValuationPermission,
            Boolean coursesManagementPermission, Boolean teachersManagementPermission) {
        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        Person person = (Person) FenixFramework.getDomainObject(personId);

        if (coursesValuationPermission) {
            tsd.addCourseValuationPermission(person);
        } else {
            tsd.removeCourseValuationPermission(person);
        }

        if (teachersValuationPermission) {
            tsd.addTeacherValuationPermission(person);
        } else {
            tsd.removeTeacherValuationPermission(person);
        }

        if (coursesManagementPermission) {
            tsd.addCourseManagersPermission(person);
        } else {
            tsd.removeCourseManagersPermission(person);
        }

        if (teachersManagementPermission) {
            tsd.addTeachersManagersPermission(person);
        } else {
            tsd.removeTeachersManagersPermission(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final SetCoursesAndTeachersValuationPermission serviceInstance =
            new SetCoursesAndTeachersValuationPermission();

    @Atomic
    public static void runSetCoursesAndTeachersValuationPermission(String tsdId, String personId,
            Boolean coursesValuationPermission, Boolean teachersValuationPermission, Boolean coursesManagementPermission,
            Boolean teachersManagementPermission) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, personId, coursesValuationPermission, teachersValuationPermission,
                    coursesManagementPermission, teachersManagementPermission);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, personId, coursesValuationPermission, teachersValuationPermission,
                        coursesManagementPermission, teachersManagementPermission);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, personId, coursesValuationPermission, teachersValuationPermission,
                            coursesManagementPermission, teachersManagementPermission);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}