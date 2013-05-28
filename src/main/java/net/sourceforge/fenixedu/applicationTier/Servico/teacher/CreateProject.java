/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateProject {

    protected void run(Integer executionCourseID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final Grouping grouping = (groupingID != null) ? AbstractDomainObject.fromExternalId(groupingID) : null;

        final Project project =
                new Project(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping,
                        executionCourse, gradeScale);
        project.getDeparmentsSet().addAll(departments);
    }

    // Service Invokers migrated from Berserk

    private static final CreateProject serviceInstance = new CreateProject();

    @Service
    public static void runCreateProject(Integer executionCourseID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException, NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep,
                    groupingID, gradeScale, departments);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, name, begin, end, description, onlineSubmissionsAllowed,
                        maxSubmissionsToKeep, groupingID, gradeScale, departments);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}