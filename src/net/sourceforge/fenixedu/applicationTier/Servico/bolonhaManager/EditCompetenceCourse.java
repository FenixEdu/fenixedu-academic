/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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
        final CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        return competenceCourse;
    } 
    
    private String buildUrl(String url) {
        final String httpString = "http://";
        return (!url.startsWith(httpString)) ? httpString + url : url;  
    }
    
    private void checkIfCanEditCompetenceCourse(final CompetenceCourse competenceCourseToEdit, final String name,
            final String nameEn, final String acronym) throws ExcepcaoPersistencia, FenixServiceException {
        
        final List<CompetenceCourse> competenceCourses = persistentSupport.getIPersistentCompetenceCourse().readFromNewDegreeStructure();
        for (final CompetenceCourse competenceCourse : competenceCourses) {
            if (competenceCourse != competenceCourseToEdit) {
                if (competenceCourse.getName().equals(name)) {
                    throw new FenixServiceException("error.existingCompetenceCourseWithSameName");
                }
                if (competenceCourse.getNameEn().equals(nameEn)) {
                    throw new FenixServiceException("error.existingCompetenceCourseWithSameNameEn");
                }
            }
        }
    }
}
