/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CreateCompetenceCourse extends Service {

    public CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic,
            RegimeType regimeType, Integer unitID) throws ExcepcaoPersistencia, FenixServiceException {

        final Unit unit = (Unit) persistentObject.readByOID(Unit.class, unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }
        checkIfCanCreateCompetenceCourse(name.trim(), nameEn.trim(), acronym.trim());
        final CompetenceCourse competenceCourse = DomainFactory.makeCompetenceCourse(name, nameEn,
                acronym, basic, regimeType, CurricularStage.DRAFT, unit);
        return competenceCourse;
    }

    private void checkIfCanCreateCompetenceCourse(final String name, final String nameEn,
            final String acronym) throws ExcepcaoPersistencia, FenixServiceException {

        String normalizedName = StringFormatter.normalize(name);
        String normalizedNameEn = StringFormatter.normalize(nameEn);

        final List<CompetenceCourse> competenceCourses = persistentSupport
                .getIPersistentCompetenceCourse().readFromNewDegreeStructure();

        for (final CompetenceCourse competenceCourse : competenceCourses) {
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
