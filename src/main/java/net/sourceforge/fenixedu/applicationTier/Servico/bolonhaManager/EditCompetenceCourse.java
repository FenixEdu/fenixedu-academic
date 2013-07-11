/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Filtro.BolonhaManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.util.StringFormatter;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class EditCompetenceCourse {

    protected void run(String competenceCourseID, String objectives, String program, String evaluationMethod,
            String objectivesEn, String programEn, String evaluationMethodEn) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.edit(objectives, program, evaluationMethod, objectivesEn, programEn, evaluationMethodEn);
    }

    protected void run(String competenceCourseID, String name, String nameEn, Boolean basic,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, CurricularStage curricularStage)
            throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        checkIfCanEditCompetenceCourse(competenceCourse, name.trim(), nameEn.trim());
        competenceCourse.edit(name, nameEn, basic, competenceCourseLevel, type, curricularStage);
    }

    protected void run(String competenceCourseID, String acronym) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.editAcronym(acronym);
    }

    protected void run(String competenceCourseID, CurricularStage curricularStage) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.changeCurricularStage(curricularStage);
    }

    protected void run(String competenceCourseID, String year, String title, String authors, String reference,
            BibliographicReferenceType bibliographicReferenceType, String url) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.createBibliographicReference(year, title, authors, reference, buildUrl(url), bibliographicReferenceType);
    }

    protected void run(String competenceCourseID, Integer bibliographicReferenceID, String year, String title, String authors,
            String reference, BibliographicReferenceType bibliographicReferenceType, String url) throws FenixServiceException {

        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.editBibliographicReference(bibliographicReferenceID, year, title, authors, reference, buildUrl(url),
                bibliographicReferenceType);
    }

    protected void run(String competenceCourseID, Integer bibliographicReferenceID) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.deleteBibliographicReference(bibliographicReferenceID);
    }

    protected void run(String competenceCourseID, Integer oldPosition, Integer newPosition) throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.switchBibliographicReferencePosition(oldPosition, newPosition);
    }

    private CompetenceCourse readCompetenceCourse(String competenceCourseID) throws FenixServiceException {
        final CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
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
            final String nameEn) throws FenixServiceException {
        final String normalizedName = StringFormatter.normalize(name);
        final String normalizedNameEn = StringFormatter.normalize(nameEn);

        for (final CompetenceCourse competenceCourse : CompetenceCourse.readBolonhaCompetenceCourses()) {
            if (competenceCourse != competenceCourseToEdit) {
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

    // Service Invokers migrated from Berserk

    private static final EditCompetenceCourse serviceInstance = new EditCompetenceCourse();

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, String objectives, String program,
            String evaluationMethod, String objectivesEn, String programEn, String evaluationMethodEn)
            throws FenixServiceException, NotAuthorizedException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, objectives, program, evaluationMethod, objectivesEn, programEn,
                    evaluationMethodEn);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, objectives, program, evaluationMethod, objectivesEn, programEn,
                        evaluationMethodEn);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, CurricularStage curricularStage)
            throws FenixServiceException, NotAuthorizedException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, curricularStage);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, curricularStage);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, String acronym) throws FenixServiceException,
            NotAuthorizedException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, acronym);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, acronym);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, String year, String title, String author,
            String reference, BibliographicReferenceType valueOf, String url) throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, year, title, author, reference, valueOf, url);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, year, title, author, reference, valueOf, url);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, Integer bibliographicReferenceID, String year,
            String title, String author, String reference, BibliographicReferenceType valueOf, String url)
            throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, bibliographicReferenceID, year, title, author, reference, valueOf, url);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, bibliographicReferenceID, year, title, author, reference, valueOf, url);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, Integer bibliographicReferenceIDToDelete)
            throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, bibliographicReferenceIDToDelete);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, bibliographicReferenceIDToDelete);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, Integer oldPosition, Integer newPosition)
            throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, oldPosition, newPosition);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, oldPosition, newPosition);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Service
    public static void runEditCompetenceCourse(String competenceCourseID, String name, String nameEn, Boolean basic,
            CompetenceCourseLevel enumCompetenceCourseLevel, CompetenceCourseType enumCompetenceCourseType,
            CurricularStage valueOf) throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, name, nameEn, basic, enumCompetenceCourseLevel, enumCompetenceCourseType,
                    valueOf);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, name, nameEn, basic, enumCompetenceCourseLevel, enumCompetenceCourseType,
                        valueOf);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }
}