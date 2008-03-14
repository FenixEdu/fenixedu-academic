/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;

public class DeleteCompetenceCourse extends Service {

    public void run(final Integer competenceCourseID) {
	final CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
	if (competenceCourse != null) {
	    competenceCourse.delete();
	}
    }
}
