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
package org.fenixedu.academic.ui.struts.action.BolonhaManager;

import java.io.Serializable;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.StringFormatter;

public class CompetenceCourseInformationRequestBean implements Serializable {

    private CompetenceCourse competenceCourse;

    private ExecutionSemester executionSemester;

    private String justification;

    private String name;

    private String nameEn;

    private RegimeType regime;

    private String objectives;

    private String objectivesEn;

    private String program;

    private String programEn;

    private String evaluationMethod;

    private String evaluationMethodEn;

    private String prerequisites;
    private String prerequisitesEn;
    private String laboratorialComponent;
    private String laboratorialComponentEn;
    private String programmingAndComputingComponent;
    private String programmingAndComputingComponentEn;
    private String crossCompetenceComponent;
    private String crossCompetenceComponentEn;
    private String ethicalPrinciples;
    private String ethicalPrinciplesEn;

    private CompetenceCourseLevel competenceCourseLevel;

    private BibliographicReferences references;

    private boolean showOldCompetenceCourses;

    public CompetenceCourseInformationRequestBean(CompetenceCourseInformationChangeRequest request) {
        setCompetenceCourse(request.getCompetenceCourse());
        setRegime(request.getRegime());
        setObjectives(request.getObjectives());
        setObjectivesEn(request.getObjectivesEn());
        setProgram(request.getProgram());
        setProgramEn(request.getProgramEn());
        setEvaluationMethod(request.getEvaluationMethod());
        setEvaluationMethodEn(request.getEvaluationMethodEn());
        setCompetenceCourseLevel(request.getCompetenceCourseLevel());
        setExecutionPeriod(request.getExecutionPeriod());
        setReferences(request.getBibliographicReferences());
        setName(request.getName());
        setNameEn(request.getNameEn());
        setPrerequisites(request.getPrerequisites());
        setPrerequisitesEn(request.getPrerequisitesEn());
        setLaboratorialComponent(request.getLaboratorialComponent());
        setLaboratorialComponentEn(request.getLaboratorialComponentEn());
        setProgrammingAndComputingComponent(request.getProgrammingAndComputingComponent());
        setProgrammingAndComputingComponentEn(request.getProgrammingAndComputingComponentEn());
        setCrossCompetenceComponent(request.getCrossCompetenceComponent());
        setCrossCompetenceComponentEn(request.getCrossCompetenceComponentEn());
        setEthicalPrinciples(request.getEthicalPrinciples());
        setEthicalPrinciplesEn(request.getEthicalPrinciplesEn());
    }

    public CompetenceCourseInformationRequestBean(CompetenceCourseInformation information) {
        setCompetenceCourse(information.getCompetenceCourse());
        setRegime(information.getRegime());
        setObjectives(information.getObjectives());
        setObjectivesEn(information.getObjectivesEn());
        setProgram(information.getProgram());
        setProgramEn(information.getProgramEn());
        setEvaluationMethod(information.getEvaluationMethod());
        setEvaluationMethodEn(information.getEvaluationMethodEn());
        setCompetenceCourseLevel(information.getCompetenceCourseLevel());
        setExecutionPeriod(information.getExecutionPeriod());
        setReferences(information.getBibliographicReferences());
        setName(information.getName());
        setNameEn(information.getNameEn());
        setPrerequisites(information.getPrerequisites());
        setPrerequisitesEn(information.getPrerequisitesEn());
        setLaboratorialComponent(information.getLaboratorialComponent());
        setLaboratorialComponentEn(information.getLaboratorialComponentEn());
        setProgrammingAndComputingComponent(information.getProgrammingAndComputingComponent());
        setProgrammingAndComputingComponentEn(information.getProgrammingAndComputingComponentEn());
        setCrossCompetenceComponent(information.getCrossCompetenceComponent());
        setCrossCompetenceComponentEn(information.getCrossCompetenceComponentEn());
        setEthicalPrinciples(information.getEthicalPrinciples());
        setEthicalPrinciplesEn(information.getEthicalPrinciplesEn());
    }

    public CompetenceCourseInformationRequestBean(CompetenceCourse course, ExecutionSemester period) {
        setExecutionPeriod(period);
        setCompetenceCourse(course);
    }

    public CompetenceCourseInformationRequestBean() {
        this(null, null);
    }

    public boolean isCompetenceCourseDefinedForExecutionPeriod() {
        if (getCompetenceCourse() != null && getExecutionPeriod() != null) {
            return getCompetenceCourse().isCompetenceCourseInformationDefinedAtExecutionPeriod(getExecutionPeriod());
        }
        return false;
    }

    public boolean isRequestDraftAvailable() {
        if (getCompetenceCourse() != null && getExecutionPeriod() != null) {
            return getCompetenceCourse().isRequestDraftAvailable(getExecutionPeriod());
        }
        return false;
    }

    public boolean isLoggedPersonAllowedToCreateChangeRequests() {
        if (getCompetenceCourse() != null && getExecutionPeriod() != null) {
            return getCompetenceCourse().isLoggedPersonAllowedToCreateChangeRequests(getExecutionPeriod());
        }
        return false;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester period) {
        executionSemester = period;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public void setCompetenceCourse(CompetenceCourse course) {
        competenceCourse = course;
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
        return competenceCourseLevel;
    }

    public void setCompetenceCourseLevel(CompetenceCourseLevel competenceCourseLevel) {
        this.competenceCourseLevel = competenceCourseLevel;
    }

    public String getEvaluationMethod() {
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getEvaluationMethodEn() {
        return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getObjectivesEn() {
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgramEn() {
        return programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }

    public RegimeType getRegime() {
        return regime;
    }

    public void setRegime(RegimeType regime) {
        this.regime = regime;
    }

    public void update(CompetenceCourseInformation information) {
        setObjectives(information.getObjectives());
        setObjectivesEn(information.getObjectivesEn());
        setProgram(information.getProgram());
        setProgramEn(information.getProgramEn());
        setEvaluationMethod(information.getEvaluationMethod());
        setEvaluationMethodEn(information.getEvaluationMethodEn());
        setCompetenceCourseLevel(information.getCompetenceCourseLevel());
        setReferences(information.getBibliographicReferences());
        setName(information.getName());
        setNameEn(information.getNameEn());
        setPrerequisites(information.getPrerequisites());
        setPrerequisitesEn(information.getPrerequisitesEn());
        setLaboratorialComponent(information.getLaboratorialComponent());
        setLaboratorialComponentEn(information.getLaboratorialComponentEn());
        setProgrammingAndComputingComponent(information.getProgrammingAndComputingComponent());
        setProgrammingAndComputingComponentEn(information.getProgrammingAndComputingComponentEn());
        setCrossCompetenceComponent(information.getCrossCompetenceComponent());
        setCrossCompetenceComponentEn(information.getCrossCompetenceComponentEn());
        setEthicalPrinciples(information.getEthicalPrinciples());
        setEthicalPrinciplesEn(information.getEthicalPrinciplesEn());
    }

    public void reset() {
        setObjectives(null);
        setObjectivesEn(null);
        setProgram(null);
        setProgramEn(null);
        setEvaluationMethod(null);
        setEvaluationMethodEn(null);
        setCompetenceCourseLevel(null);
        setReferences(null);
        setName(null);
        setNameEn(null);
        setPrerequisites(null);
        setPrerequisitesEn(null);
        setLaboratorialComponent(null);
        setLaboratorialComponentEn(null);
        setProgrammingAndComputingComponent(null);
        setProgrammingAndComputingComponentEn(null);
        setCrossCompetenceComponent(null);
        setCrossCompetenceComponentEn(null);
        setEthicalPrinciples(null);
        setEthicalPrinciplesEn(null);
    }

    public BibliographicReferences getReferences() {
        if (references == null) {
            references = new BibliographicReferences();
        }
        return references;
    }

    public void setReferences(BibliographicReferences references) {
        this.references = references;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setShowOldCompetenceCourses(boolean showOldCompetenceCourses) {
        this.showOldCompetenceCourses = showOldCompetenceCourses;
    }

    public boolean isShowOldCompetenceCourses() {
        return showOldCompetenceCourses;
    }

    public boolean getIsShowOldCompetenceCourses() {
        return isShowOldCompetenceCourses();
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getPrerequisitesEn() {
        return prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public String getLaboratorialComponent() {
        return laboratorialComponent;
    }

    public void setLaboratorialComponent(String laboratorialComponent) {
        this.laboratorialComponent = laboratorialComponent;
    }

    public String getLaboratorialComponentEn() {
        return laboratorialComponentEn;
    }

    public void setLaboratorialComponentEn(String laboratorialComponentEn) {
        this.laboratorialComponentEn = laboratorialComponentEn;
    }

    public String getProgrammingAndComputingComponent() {
        return programmingAndComputingComponent;
    }

    public void setProgrammingAndComputingComponent(String programmingAndComputingComponent) {
        this.programmingAndComputingComponent = programmingAndComputingComponent;
    }

    public String getProgrammingAndComputingComponentEn() {
        return programmingAndComputingComponentEn;
    }

    public void setProgrammingAndComputingComponentEn(String programmingAndComputingComponentEn) {
        this.programmingAndComputingComponentEn = programmingAndComputingComponentEn;
    }

    public String getCrossCompetenceComponent() {
        return crossCompetenceComponent;
    }

    public void setCrossCompetenceComponent(String crossCompetenceComponent) {
        this.crossCompetenceComponent = crossCompetenceComponent;
    }

    public String getCrossCompetenceComponentEn() {
        return crossCompetenceComponentEn;
    }

    public void setCrossCompetenceComponentEn(String crossCompetenceComponentEn) {
        this.crossCompetenceComponentEn = crossCompetenceComponentEn;
    }

    public String getEthicalPrinciples() {
        return ethicalPrinciples;
    }

    public void setEthicalPrinciples(String ethicalPrinciples) {
        this.ethicalPrinciples = ethicalPrinciples;
    }

    public String getEthicalPrinciplesEn() {
        return ethicalPrinciplesEn;
    }

    public void setEthicalPrinciplesEn(String ethicalPrinciplesEn) {
        this.ethicalPrinciplesEn = ethicalPrinciplesEn;
    }
    
    public void checkCompetenceCourseName() {
        final String normalizedName = StringFormatter.normalize(getName());
        final String normalizedNameEn = StringFormatter.normalize(getNameEn());
        if (!getName().equals(getCompetenceCourse().getName()) || !getNameEn().equals(getCompetenceCourse().getNameEn())) {
            Department thisDepartment = getCompetenceCourse().getDepartmentUnit().getDepartment();
            for (final CompetenceCourse competenceCourse : CompetenceCourse.readBolonhaCompetenceCourses()) {
                Department otherDepartment = competenceCourse.getDepartmentUnit().getDepartment();
                if ((!thisDepartment.getIgnoreNameValidation() && !otherDepartment.getIgnoreNameValidation())
                        || (thisDepartment.getIgnoreNameValidation() && thisDepartment.equals(otherDepartment))) {
                    if (!getCompetenceCourse().equals(competenceCourse)) {
                        if (StringFormatter.normalize(competenceCourse.getName()) != null) {
                            if (StringFormatter.normalize(competenceCourse.getName()).equals(normalizedName)) {
                                throw new DomainException("error.existingCompetenceCourseWithSameName",
                                        competenceCourse.getDepartmentUnit().getName());
                            }
                        }
                        if (StringFormatter.normalize(competenceCourse.getNameEn()) != null) {
                            if (StringFormatter.normalize(competenceCourse.getNameEn()).equals(normalizedNameEn)) {
                                throw new DomainException("error.existingCompetenceCourseWithSameNameEn",
                                        competenceCourse.getDepartmentUnit().getName());
                            }
                        }
                    }
                }
            }
        }
    }

}
