package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class UpdateDegreeTeachingServices {

    protected void run(Integer professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType) {

        Professorship professorship = RootDomainObject.getInstance().readProfessorshipByOID(professorshipID);
        Teacher teacher = professorship.getTeacher();
        ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }

        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.teacher.schedule.change"));

        for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
            Shift shift = RootDomainObject.getInstance().readShiftByOID(shiftIDTeachingPercentage.getShiftID());
            DegreeTeachingService degreeTeachingService =
                    teacherService.getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage(), roleType);
            } else {
                if (shiftIDTeachingPercentage.getPercentage() == null
                        || (shiftIDTeachingPercentage.getPercentage() != null && shiftIDTeachingPercentage.getPercentage() != 0)) {
                    new DegreeTeachingService(teacherService, professorship, shift, shiftIDTeachingPercentage.getPercentage(),
                            roleType);
                }
            }

            log.append(shift.getPresentationName());
            if (shiftIDTeachingPercentage.getPercentage() != null) {
                log.append("= ");
                log.append(shiftIDTeachingPercentage.getPercentage());
            }
            log.append("% ; ");
        }

        new TeacherServiceLog(teacherService, log.toString());
    }

    // Service Invokers migrated from Berserk

    private static final UpdateDegreeTeachingServices serviceInstance = new UpdateDegreeTeachingServices();

    @Service
    public static void runUpdateDegreeTeachingServices(Integer professorshipID,
            List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType) throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}