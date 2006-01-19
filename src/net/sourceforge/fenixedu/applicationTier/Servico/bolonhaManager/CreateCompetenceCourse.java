/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class CreateCompetenceCourse extends Service {

    public CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic,
            RegimeType regimeType, Integer unitID) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final Unit unit = (Unit) persistentSupport.getIPersistentObject().readByOID(Unit.class, unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }
        checkIfCanCreateCompetenceCourse(persistentSupport, name, nameEn, acronym);
        final CompetenceCourse competenceCourse = 
            DomainFactory.makeCompetenceCourse(name, nameEn, acronym, basic, regimeType, CurricularStage.DRAFT, unit);
        return competenceCourse;
    }

    private void checkIfCanCreateCompetenceCourse(final ISuportePersistente persistentSupport, final String name,
            final String nameEn, final String acronym) throws ExcepcaoPersistencia, FenixServiceException {
        final List<CompetenceCourse> competenceCourses = persistentSupport.getIPersistentCompetenceCourse().readFromNewDegreeStructure();
        for (final CompetenceCourse competenceCourse : competenceCourses) {
            if (competenceCourse.getName().equals(name)) {
                throw new FenixServiceException("error.existingCompetenceCourseWithSameName");
            }
            if (competenceCourse.getNameEn().equals(nameEn)) {
                throw new FenixServiceException("error.existingCompetenceCourseWithSameNameEn");
            }
            if (competenceCourse.getAcronym().equals(acronym)) {
                throw new FenixServiceException("error.existingCompetenceCourseWithSameAcronym");
            }
        }
    }
}
