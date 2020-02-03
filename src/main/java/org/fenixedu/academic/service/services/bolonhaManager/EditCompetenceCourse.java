/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Dec 9, 2005
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CompetenceCourseType;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.service.filter.BolonhaManagerAuthorizationFilter;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.ExistingCompetenceCourseInformationException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.StringFormatter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCompetenceCourse {

    protected void run(String competenceCourseID, String objectives, String program, String evaluationMethod,
            String prerequisites, String laboratorialComponent, String programmingAndComputingComponent,
            String crossCompetenceComponent, String ethicalPrinciples, String objectivesEn, String programEn,
            String evaluationMethodEn, String prerequisitesEn, String laboratorialComponentEn,
            String programmingAndComputingComponentEn, String crossCompetenceComponentEn, String ethicalPrinciplesEn)
            throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        competenceCourse.edit(objectives, program, evaluationMethod, prerequisites, laboratorialComponent,
                programmingAndComputingComponent, crossCompetenceComponent, ethicalPrinciples, objectivesEn, programEn,
                evaluationMethodEn, prerequisitesEn, laboratorialComponentEn, programmingAndComputingComponentEn,
                crossCompetenceComponentEn, ethicalPrinciplesEn);
    }

    protected void run(String competenceCourseID, String name, String nameEn, Boolean basic,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, CurricularStage curricularStage, String code)
            throws FenixServiceException {
        final CompetenceCourse competenceCourse = readCompetenceCourse(competenceCourseID);
        checkIfCanEditCompetenceCourse(competenceCourse, name.trim(), nameEn.trim());
        competenceCourse.edit(name, nameEn, basic, competenceCourseLevel, type, curricularStage);
        competenceCourse.setCode(code);
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

    @Atomic
    public static void runEditCompetenceCourse(String competenceCourseID, String objectives, String program,
            String evaluationMethod, String prerequisites, String laboratorialComponent, String programmingAndComputingComponent,
            String crossCompetenceComponent, String ethicalPrinciples, String objectivesEn, String programEn,
            String evaluationMethodEn, String prerequisitesEn, String laboratorialComponentEn,
            String programmingAndComputingComponentEn, String crossCompetenceComponentEn, String ethicalPrinciplesEn)
            throws FenixServiceException, NotAuthorizedException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, objectives, program, evaluationMethod, prerequisites, laboratorialComponent,
                    programmingAndComputingComponent, crossCompetenceComponent, ethicalPrinciples, objectivesEn, programEn,
                    evaluationMethodEn, prerequisitesEn, laboratorialComponentEn, programmingAndComputingComponentEn,
                    crossCompetenceComponentEn, ethicalPrinciplesEn);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, objectives, program, evaluationMethod, prerequisites,
                        laboratorialComponent, programmingAndComputingComponent, crossCompetenceComponent, ethicalPrinciples,
                        objectivesEn, programEn, evaluationMethodEn, prerequisitesEn, laboratorialComponentEn,
                        programmingAndComputingComponentEn, crossCompetenceComponentEn, ethicalPrinciplesEn);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    @Atomic
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

    @Atomic
    public static void runEditCompetenceCourse(String competenceCourseID, String acronym)
            throws FenixServiceException, NotAuthorizedException {
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

    @Atomic
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

    @Atomic
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

    @Atomic
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

    @Atomic
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

    @Atomic
    public static void runEditCompetenceCourse(String competenceCourseID, String name, String nameEn, Boolean basic,
            CompetenceCourseLevel enumCompetenceCourseLevel, CompetenceCourseType enumCompetenceCourseType,
            CurricularStage valueOf, String code) throws FenixServiceException {
        try {
            BolonhaManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseID, name, nameEn, basic, enumCompetenceCourseLevel, enumCompetenceCourseType,
                    valueOf, code);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseID, name, nameEn, basic, enumCompetenceCourseLevel, enumCompetenceCourseType,
                        valueOf, code);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}