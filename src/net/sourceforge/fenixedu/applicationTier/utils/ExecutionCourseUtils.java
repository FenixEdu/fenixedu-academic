/*
 * Created on Jul 26, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExecutionCourseUtils {

    public static void copySectionsAndItems(Site siteFrom, Site siteTo) {
	siteTo.copySectionsAndItemsFrom(siteFrom);
    }

    public static void deleteSectionsAndItemsIfExistFrom(Site siteTo) throws DomainException {
	if (siteTo.getAssociatedSectionsCount() > 0) {
	    final List<Section> associatedSections = new ArrayList<Section>();
	    associatedSections.addAll(siteTo.getAssociatedSections());
	    for (final Section section : associatedSections) {
		section.delete();
	    }
	}
    }

    public static List<BibliographicReference> copyBibliographicReference(
	    final ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
	return executionCourseTo.copyBibliographicReferencesFrom(executionCourseFrom);
    }

    public static void copyEvaluationMethod(final ExecutionCourse executionCourseFrom,
	    ExecutionCourse executionCourseTo) {
	executionCourseTo.copyEvaluationMethodFrom(executionCourseFrom);
    }

}
