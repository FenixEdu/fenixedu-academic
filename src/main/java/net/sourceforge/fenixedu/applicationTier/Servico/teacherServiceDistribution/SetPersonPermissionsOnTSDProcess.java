package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class SetPersonPermissionsOnTSDProcess {
    protected void run(String tsdProcessId, String personId, Boolean phaseManagementPermission,
            Boolean automaticValuationPermission, Boolean omissionConfigurationPermission,
            Boolean tsdCoursesAndTeachersManagementPermission) {

        TSDProcess tsdProcess = AbstractDomainObject.fromExternalId(tsdProcessId);
        Person person = (Person) AbstractDomainObject.fromExternalId(personId);

        if (phaseManagementPermission) {
            tsdProcess.addPhasesManagementPermission(person);
        } else {
            tsdProcess.removePhasesManagementPermission(person);
        }

        if (automaticValuationPermission) {
            tsdProcess.addAutomaticValuationPermission(person);
        } else {
            tsdProcess.removeAutomaticValuationPermission(person);
        }

        if (omissionConfigurationPermission) {
            tsdProcess.addOmissionConfigurationPermission(person);
        } else {
            tsdProcess.removeOmissionConfigurationPermission(person);
        }

        if (tsdCoursesAndTeachersManagementPermission) {
            tsdProcess.addCompetenceCoursesAndTeachersManagement(person);
        } else {
            tsdProcess.removeCompetenceCoursesAndTeachersManagement(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final SetPersonPermissionsOnTSDProcess serviceInstance = new SetPersonPermissionsOnTSDProcess();

    @Service
    public static void runSetPersonPermissionsOnTSDProcess(String tsdProcessId, String personId,
            Boolean phaseManagementPermission, Boolean automaticValuationPermission, Boolean omissionConfigurationPermission,
            Boolean tsdCoursesAndTeachersManagementPermission) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdProcessId, personId, phaseManagementPermission, automaticValuationPermission,
                    omissionConfigurationPermission, tsdCoursesAndTeachersManagementPermission);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdProcessId, personId, phaseManagementPermission, automaticValuationPermission,
                        omissionConfigurationPermission, tsdCoursesAndTeachersManagementPermission);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdProcessId, personId, phaseManagementPermission, automaticValuationPermission,
                            omissionConfigurationPermission, tsdCoursesAndTeachersManagementPermission);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}