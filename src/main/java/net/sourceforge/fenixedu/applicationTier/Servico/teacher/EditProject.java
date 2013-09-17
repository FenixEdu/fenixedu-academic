package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditProject {

    protected void run(String executionCourseID, String projectID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, String groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException {
        final Project project = (Project) FenixFramework.getDomainObject(projectID);
        if (project == null) {
            throw new FenixServiceException("error.noEvaluation");
        }

        final Grouping grouping = (groupingID != null) ? FenixFramework.<Grouping> getDomainObject(groupingID) : null;

        project.edit(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping, gradeScale,
                departments);
    }

    // Service Invokers migrated from Berserk

    private static final EditProject serviceInstance = new EditProject();

    @Atomic
    public static void runEditProject(String executionCourseID, String projectID, String name, Date begin, Date end,
            String description, Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, String groupingID,
            GradeScale gradeScale, List<Department> departments) throws FenixServiceException, NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, projectID, name, begin, end, description, onlineSubmissionsAllowed,
                    maxSubmissionsToKeep, groupingID, gradeScale, departments);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, projectID, name, begin, end, description, onlineSubmissionsAllowed,
                        maxSubmissionsToKeep, groupingID, gradeScale, departments);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}