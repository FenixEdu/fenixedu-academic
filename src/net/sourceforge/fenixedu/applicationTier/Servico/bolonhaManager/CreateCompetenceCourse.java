/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CreateCompetenceCourse extends Service {

    public CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic,
            RegimeType regimeType, CompetenceCourseLevel competenceCourseLevel, Integer unitID)
            throws ExcepcaoPersistencia, FenixServiceException {

        final Unit unit = (Unit) rootDomainObject.readPartyByOID(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }
        checkIfCanCreateCompetenceCourse(name.trim(), nameEn.trim(), acronym.trim());
        return DomainFactory.makeCompetenceCourse(name, nameEn, acronym, basic, regimeType,
                competenceCourseLevel, CurricularStage.DRAFT, unit);
    }

    private void checkIfCanCreateCompetenceCourse(final String name, final String nameEn,
            final String acronym) throws ExcepcaoPersistencia, FenixServiceException {

        final String normalizedName = StringFormatter.normalize(name);
        final String normalizedNameEn = StringFormatter.normalize(nameEn);

        for (final CompetenceCourse competenceCourse : CompetenceCourse.readFromNewDegreeStructure()) {
            if (StringFormatter.normalize(competenceCourse.getName()).equals(normalizedName)) {
                throw new ExistingCompetenceCourseInformationException(
                        "error.existingCompetenceCourseWithSameName", competenceCourse
                                .getDepartmentUnit().getName());
            }
            if (StringFormatter.normalize(competenceCourse.getNameEn()).equals(normalizedNameEn)) {
                throw new ExistingCompetenceCourseInformationException(
                        "error.existingCompetenceCourseWithSameNameEn", competenceCourse
                                .getDepartmentUnit().getName());
            }
        }
    }
}
