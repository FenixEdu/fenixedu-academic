/*
 * Created on Jul 26, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class ExecutionCourseUtils {

    public static List<BibliographicReference> copyBibliographicReference(
	    final ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
	return executionCourseTo.copyBibliographicReferencesFrom(executionCourseFrom);
    }

    public static void copyEvaluationMethod(final ExecutionCourse executionCourseFrom,
	    ExecutionCourse executionCourseTo) {
	executionCourseTo.copyEvaluationMethodFrom(executionCourseFrom);
    }

}
