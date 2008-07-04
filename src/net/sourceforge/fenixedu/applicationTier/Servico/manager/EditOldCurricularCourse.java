package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;

public class EditOldCurricularCourse extends Service {

    public void run(final Integer curricularCourseId, final String name, final String nameEn, final String code,
	    final String acronym, final Integer minimumValueForAcumulatedEnrollments,
	    final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
	    final Double credits, final Double ectsCredits, final Double theoreticalHours, final Double labHours,
	    final Double praticalHours, final Double theoPratHours) throws FenixServiceException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	if (curricularCourse == null) {
	    throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
	}

	curricularCourse.edit(name, nameEn, code, acronym, weigth, credits, ectsCredits, enrolmentWeigth,
		minimumValueForAcumulatedEnrollments, maximumValueForAcumulatedEnrollments, theoreticalHours, labHours,
		praticalHours, theoPratHours);
    }
}
