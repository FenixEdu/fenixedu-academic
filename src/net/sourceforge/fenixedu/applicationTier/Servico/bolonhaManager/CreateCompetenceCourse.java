/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourseType;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.util.StringFormatter;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCompetenceCourse extends FenixService {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Service
    public static CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic, RegimeType regimeType,
	    CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, Integer unitID,
	    ExecutionSemester startSemester) throws FenixServiceException {

	final CompetenceCourseGroupUnit unit = (CompetenceCourseGroupUnit) rootDomainObject.readPartyByOID(unitID);
	if (unit == null) {
	    throw new FenixServiceException("error.invalidUnit");
	}
	checkIfCanCreateCompetenceCourse(name.trim(), nameEn.trim());
	return new CompetenceCourse(name, nameEn, basic, regimeType, competenceCourseLevel, type, CurricularStage.DRAFT, unit,
		startSemester);
    }

    private static void checkIfCanCreateCompetenceCourse(final String name, final String nameEn) throws FenixServiceException {

	final String normalizedName = StringFormatter.normalize(name);
	final String normalizedNameEn = StringFormatter.normalize(nameEn);

	for (final CompetenceCourse competenceCourse : CompetenceCourse.readBolonhaCompetenceCourses()) {
	    if (StringFormatter.normalize(competenceCourse.getName()).equals(normalizedName)) {
		throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameName",
			competenceCourse.getDepartmentUnit().getName());
	    }
	    if (StringFormatter.normalize(competenceCourse.getNameEn()).equals(normalizedNameEn)) {
		throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameNameEn",
			competenceCourse.getDepartmentUnit().getName());
	    }
	}
    }
}