package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadAllCompetenceCourses {

    @Atomic
    public static List<InfoCompetenceCourse> run() {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
        for (final CompetenceCourse competenceCourse : CompetenceCourse.readOldCompetenceCourses()) {
            result.add(InfoCompetenceCourse.newInfoFromDomain(competenceCourse));
        }
        return result;
    }
}