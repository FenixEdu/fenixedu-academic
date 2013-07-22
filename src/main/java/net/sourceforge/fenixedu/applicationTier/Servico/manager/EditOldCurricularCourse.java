package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditOldCurricularCourse {

    @Atomic
    public static void run(final String curricularCourseId, final String name, final String nameEn, final String code,
            final String acronym, final Integer minimumValueForAcumulatedEnrollments,
            final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
            final Double credits, final Double ectsCredits, final Double theoreticalHours, final Double labHours,
            final Double praticalHours, final Double theoPratHours, final GradeScale gradeScale) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
        if (curricularCourse == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
        }

        curricularCourse.edit(name, nameEn, code, acronym, weigth, credits, ectsCredits, enrolmentWeigth,
                minimumValueForAcumulatedEnrollments, maximumValueForAcumulatedEnrollments, theoreticalHours, labHours,
                praticalHours, theoPratHours, gradeScale);
    }
}