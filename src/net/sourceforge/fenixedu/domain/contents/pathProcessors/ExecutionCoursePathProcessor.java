package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.contents.Content;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExecutionCoursePathProcessor extends AbstractPathProcessor {

    @Override
    public Content processPath(String path) {

	String[] pathElements = path.split("/");
	if (pathElements.length > 0) {

	    ExecutionInterval executionInterval = getExecutionInterval(pathElements);
	    ExecutionCourse executionCourse = executionInterval == null ? ExecutionCourse.readLastBySigla(pathElements[0])
		    : ExecutionCourse.readLastByExecutionIntervalAndSigla(pathElements[0], executionInterval);
	    return executionCourse == null ? null : executionCourse.getSite();
	}
	return null;
    }

    @Override
    public String getTrailingPath(String path) {
	String pathWithoutCourse = super.getTrailingPath(path);
	if (pathWithoutCourse.matches("[1-9][0-9]{3}-[1-9][0-9]{3}.*")) {
	    pathWithoutCourse = pathWithoutCourse.replaceFirst("[1-9][0-9]{3}-[1-9][0-9]{3}/?", "");

	    if (pathWithoutCourse.matches("[1-2]-semestre.*")) {
		pathWithoutCourse = pathWithoutCourse.replaceFirst("[1-2]-semestre/?", "");
	    }
	}
	return pathWithoutCourse;
    }

    private ExecutionInterval getExecutionInterval(String[] pathElements) {
	if (pathElements.length > 1 && pathElements[1].matches("[1-9][0-9]{3}-[1-9][0-9]{3}")) {
	    ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(pathElements[1].replace('-', '/'));
	    if (executionYear != null) {
		if (pathElements.length > 2 && pathElements[2].matches("[1-2]-semestre")) {
		    return executionYear.readExecutionPeriodForSemester(Integer
			    .valueOf(String.valueOf(pathElements[2].charAt(0))));
		}
		return executionYear;
	    }
	}
	return null;
    }
}
