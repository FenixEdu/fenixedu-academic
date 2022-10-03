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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;

public class CompetenceCourseInformationRequestBean implements Serializable {

    private CompetenceCourse competenceCourse;

    private ExecutionInterval executionSemester;

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
        setExecutionPeriod(request.getExecutionPeriod());
        setReferences(request.getBibliographicReferences());
        setName(request.getName());
        setNameEn(request.getNameEn());
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
        setExecutionPeriod(information.getExecutionPeriod());
        setReferences(information.getBibliographicReferences());
        setName(information.getName());
        setNameEn(information.getNameEn());
    }

    public CompetenceCourseInformationRequestBean(CompetenceCourse course, ExecutionInterval period) {
        setExecutionPeriod(period);
        setCompetenceCourse(course);
    }

    public CompetenceCourseInformationRequestBean() {
        this(null, null);
    }

    public boolean isCompetenceCourseDefinedForExecutionPeriod() {
        if (getCompetenceCourse() != null && getExecutionPeriod() != null) {
            return getCompetenceCourse().findInformation(getExecutionPeriod()) != null;
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

    public ExecutionInterval getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionInterval period) {
        executionSemester = period;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public void setCompetenceCourse(CompetenceCourse course) {
        competenceCourse = course;
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
        setReferences(information.getBibliographicReferences());
        setName(information.getName());
        setNameEn(information.getNameEn());
    }

    public void reset() {
        setObjectives(null);
        setObjectivesEn(null);
        setProgram(null);
        setProgramEn(null);
        setEvaluationMethod(null);
        setEvaluationMethodEn(null);
        setReferences(null);
        setName(null);
        setNameEn(null);
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
}
