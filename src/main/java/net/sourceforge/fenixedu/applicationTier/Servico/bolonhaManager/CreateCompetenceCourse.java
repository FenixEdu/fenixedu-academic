/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourseType;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.StringFormatter;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCompetenceCourse {

    @Atomic
    public static CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, String unitID, ExecutionSemester startSemester)
            throws FenixServiceException {
        check(RolePredicates.BOLONHA_MANAGER_PREDICATE);

        final CompetenceCourseGroupUnit unit = (CompetenceCourseGroupUnit) FenixFramework.getDomainObject(unitID);
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

            if (StringFormatter.normalize(competenceCourse.getName()) != null) {
                if (StringFormatter.normalize(competenceCourse.getName()).equals(normalizedName)) {
                    throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameName",
                            competenceCourse.getDepartmentUnit().getName());
                }
            }
            if (StringFormatter.normalize(competenceCourse.getNameEn()) != null) {
                if (StringFormatter.normalize(competenceCourse.getNameEn()).equals(normalizedNameEn)) {
                    throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameNameEn",
                            competenceCourse.getDepartmentUnit().getName());
                }
            }
        }
    }
}