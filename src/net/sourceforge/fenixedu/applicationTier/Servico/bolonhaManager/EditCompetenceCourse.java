/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StringFormatter;

public class EditCompetenceCourse extends Service {
        
    public void run(Integer competenceCourseID, String objectives, String program, String evaluationMethod,
            String objectivesEn, String programEn, String evaluationMethodEn) throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.edit(objectives, program, evaluationMethod, objectivesEn, programEn, evaluationMethodEn);        
    }

    public void run(Integer competenceCourseID, String name, String nameEn, String acronym,
            Boolean basic, CurricularStage curricularStage, Boolean scientificCouncilEdit) throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        checkIfCanEditCompetenceCourse(competenceCourse, name.trim(), nameEn.trim(), acronym.trim());
        competenceCourse.edit(name, nameEn, acronym, basic, curricularStage, scientificCouncilEdit);
    }
    
    public void run(Integer competenceCourseID, String year, String title, String authors, String reference,
            BibliographicReferenceType bibliographicReferenceType, String url)
            throws ExcepcaoPersistencia, FenixServiceException {        
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);        
        competenceCourse.createBibliographicReference(year, title, authors, reference, buildUrl(url), bibliographicReferenceType);
    }
    
    public void run(Integer competenceCourseID, Integer bibliographicReferenceID, String year, String title, String authors, String reference,
            BibliographicReferenceType bibliographicReferenceType, String url)
            throws ExcepcaoPersistencia, FenixServiceException {        
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.editBibliographicReference(bibliographicReferenceID, year, title, authors, reference, buildUrl(url), bibliographicReferenceType);
    }
    
    public void run(Integer competenceCourseID, Integer bibliographicReferenceID) throws ExcepcaoPersistencia, FenixServiceException {        
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.deleteBibliographicReference(bibliographicReferenceID);
    }
    
    public void run(Integer competenceCourseID, Integer oldPosition, Integer newPosition) throws ExcepcaoPersistencia, FenixServiceException {        
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.switchBibliographicReferencePosition(oldPosition, newPosition);
    }
    
    private CompetenceCourse readCompetenceCourse(Integer competenceCourseID) throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        return competenceCourse;
    } 
    
    private String buildUrl(String url) {
        final String httpString = "http://";
        return (!url.startsWith(httpString)) ? httpString + url : url;  
    }
    
    private void checkIfCanEditCompetenceCourse(final CompetenceCourse competenceCourseToEdit,
            final String name, final String nameEn, final String acronym) throws ExcepcaoPersistencia,
            FenixServiceException {

        final String normalizedName = StringFormatter.normalize(name);
        final String normalizedNameEn = StringFormatter.normalize(nameEn);

        for (final CompetenceCourse competenceCourse : CompetenceCourse.readFromNewDegreeStructure()) {
            if (competenceCourse != competenceCourseToEdit) {
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
}
