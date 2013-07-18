/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;


import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCompetenceCourse {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Service
    public static void run(final Integer competenceCourseID) {
        final CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseID);
        if (competenceCourse != null) {
            competenceCourse.delete();
        }
    }
}