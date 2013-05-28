/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;


import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteCompetenceCourse {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Service
    public static void run(final Integer competenceCourseID) {
        final CompetenceCourse competenceCourse = AbstractDomainObject.fromExternalId(competenceCourseID);
        if (competenceCourse != null) {
            competenceCourse.delete();
        }
    }
}