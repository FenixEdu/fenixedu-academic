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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class EditProject {

    protected void run(Integer executionCourseID, Integer projectID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException {
        final Project project = (Project) AbstractDomainObject.fromExternalId(projectID);
        if (project == null) {
            throw new FenixServiceException("error.noEvaluation");
        }

        final Grouping grouping = (groupingID != null) ? AbstractDomainObject.fromExternalId(groupingID) : null;

        project.edit(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping, gradeScale,
                departments);
    }

    // Service Invokers migrated from Berserk

    private static final EditProject serviceInstance = new EditProject();

    @Service
    public static void runEditProject(Integer executionCourseID, Integer projectID, String name, Date begin, Date end,
            String description, Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID,
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