/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;

public class CreateProject extends FenixService {

	public void run(Integer executionCourseID, String name, Date begin, Date end, String description,
			Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID, GradeScale gradeScale,
			List<Department> departments) throws FenixServiceException {

		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
		if (executionCourse == null) {
			throw new FenixServiceException("error.noExecutionCourse");
		}

		final Grouping grouping = (groupingID != null) ? rootDomainObject.readGroupingByOID(groupingID) : null;

		final Project project =
				new Project(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping,
						executionCourse, gradeScale);
		project.getDeparmentsSet().addAll(departments);
	}
}
