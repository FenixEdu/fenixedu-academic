package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;

public class EditProject extends FenixService {

	public void run(Integer executionCourseID, Integer projectID, String name, Date begin, Date end, String description,
			Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID, GradeScale gradeScale,
			List<Department> departments) throws FenixServiceException {
		final Project project = (Project) rootDomainObject.readEvaluationByOID(projectID);
		if (project == null) {
			throw new FenixServiceException("error.noEvaluation");
		}

		final Grouping grouping = (groupingID != null) ? rootDomainObject.readGroupingByOID(groupingID) : null;

		project.edit(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping, gradeScale,
				departments);
	}

}
