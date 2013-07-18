package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditOldCurricularCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer curricularCourseId, final String name, final String nameEn, final String code,
            final String acronym, final Integer minimumValueForAcumulatedEnrollments,
            final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
            final Double credits, final Double ectsCredits, final Double theoreticalHours, final Double labHours,
            final Double praticalHours, final Double theoPratHours, final GradeScale gradeScale) throws FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);
        if (curricularCourse == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
        }

        curricularCourse.edit(name, nameEn, code, acronym, weigth, credits, ectsCredits, enrolmentWeigth,
                minimumValueForAcumulatedEnrollments, maximumValueForAcumulatedEnrollments, theoreticalHours, labHours,
                praticalHours, theoPratHours, gradeScale);
    }
}