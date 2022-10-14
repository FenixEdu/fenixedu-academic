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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class CompetenceCourseInformationChangeRequest extends CompetenceCourseInformationChangeRequest_Base {

    public CompetenceCourseInformationChangeRequest() {
        setRootDomainObject(Bennu.getInstance());
    }

    public CompetenceCourseInformationChangeRequest(CompetenceCourseInformation information, String justification,
            Person requester) {
        this(information.getName(), information.getNameEn(), justification, information.getRegime(), information.getObjectives(),
                information.getObjectivesEn(), information.getProgram(), information.getProgramEn(),
                information.getEvaluationMethod(), information.getEvaluationMethodEn(), information.getCompetenceCourse(),
                information.getExecutionPeriod(), requester, information.getTheoreticalHours(1), information.getProblemsHours(1),
                information.getLaboratorialHours(1), information.getSeminaryHours(1), information.getFieldWorkHours(1),
                information.getTrainingPeriodHours(1), information.getTutorialOrientationHours(1), information.getOtherHours(1),
                information.getAutonomousWorkHours(1), information.getEctsCredits(1), information.getTheoreticalHours(2),
                information.getProblemsHours(2), information.getLaboratorialHours(2), information.getSeminaryHours(2),
                information.getFieldWorkHours(2), information.getTrainingPeriodHours(2),
                information.getTutorialOrientationHours(2), information.getOtherHours(2), information.getAutonomousWorkHours(2),
                information.getEctsCredits(2), information.getBibliographicReferences(),
                information.getCompetenceCourseGroupUnit());
    }

    public CompetenceCourseInformationChangeRequest(String name, String nameEn, String justification, RegimeType regime,
            String objectives, String objectivesEn, String program, String programEn, String evaluationMethod,
            String evaluationMethodEn, CompetenceCourse course, ExecutionInterval period, Person requester,
            Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours, Double fieldWorkHours,
            Double trainingPeriodHours, Double tutorialOrientationHours, Double otherHours, Double autonomousWorkHours,
            Double ectsCredits, Double secondTheoreticalHours, Double secondProblemsHours, Double secondLaboratorialHours,
            Double secondSeminaryHours, Double secondFieldWorkHours, Double secondTrainingPeriodHours,
            Double secondTutorialOrientationHours, Double secondOtherHours, Double secondAutonomousWorkHours,
            Double secondEctsCredits, BibliographicReferences references, Unit group) {
        this();
        if (course.isRequestDraftAvailable(period)) {
            throw new DomainException("error.can.only.exist.one.request.draft.per.execution.period");
        }
        if (course == null || period == null) {
            throw new DomainException("error.fields.are.required");
        }
        setCompetenceCourse(course);
        setExecutionPeriod(period);
        edit(name, nameEn, justification, regime, objectives, objectivesEn, program, programEn, evaluationMethod,
                evaluationMethodEn, requester, theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, otherHours, autonomousWorkHours, ectsCredits,
                secondTheoreticalHours, secondProblemsHours, secondLaboratorialHours, secondSeminaryHours, secondFieldWorkHours,
                secondTrainingPeriodHours, secondTutorialOrientationHours, secondOtherHours, secondAutonomousWorkHours,
                secondEctsCredits, references, group);
    }

    public void edit(String name, String nameEn, String justification, RegimeType regime, String objectives, String objectivesEn,
            String program, String programEn, String evaluationMethod, String evaluationMethodEn, Person requester,
            Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours, Double fieldWorkHours,
            Double trainingPeriodHours, Double tutorialOrientationHours, Double otherHours, Double autonomousWorkHours,
            Double ectsCredits, Double secondTheoreticalHours, Double secondProblemsHours, Double secondLaboratorialHours,
            Double secondSeminaryHours, Double secondFieldWorkHours, Double secondTrainingPeriodHours,
            Double secondTutorialOrientationHours, Double secondOtherHours, Double secondAutonomousWorkHours,
            Double secondEctsCredits, BibliographicReferences references, Unit group) {

        if (name == null || nameEn == null || justification == null || regime == null || objectives == null
                || objectivesEn == null || program == null || programEn == null || evaluationMethod == null
                || evaluationMethodEn == null || requester == null || theoreticalHours == null || problemsHours == null
                || laboratorialHours == null || seminaryHours == null || fieldWorkHours == null || trainingPeriodHours == null
                || tutorialOrientationHours == null || otherHours == null || autonomousWorkHours == null || ectsCredits == null) {

            throw new DomainException("error.fields.are.required");
        }

        setName(name);
        setNameEn(nameEn);
        setJustification(justification);
        setRegime(regime);
        setObjectives(objectives);
        setObjectivesEn(objectivesEn);
        setProgram(program);
        setProgramEn(programEn);
        setEvaluationMethod(evaluationMethod);
        setEvaluationMethodEn(evaluationMethodEn);
        setRequester(requester);
        setApproved(null);

        setTheoreticalHours(theoreticalHours);
        setProblemsHours(problemsHours);
        setLaboratorialHours(laboratorialHours);
        setSeminaryHours(seminaryHours);
        setFieldWorkHours(fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours);
        setOtherHours(otherHours);
        setAutonomousWorkHours(autonomousWorkHours);
        setEctsCredits(ectsCredits);

        setSecondTheoreticalHours(secondTheoreticalHours);
        setSecondProblemsHours(secondProblemsHours);
        setSecondLaboratorialHours(secondLaboratorialHours);
        setSecondSeminaryHours(secondSeminaryHours);
        setSecondFieldWorkHours(secondFieldWorkHours);
        setSecondTrainingPeriodHours(secondTrainingPeriodHours);
        setSecondTutorialOrientationHours(secondTutorialOrientationHours);
        setSecondOtherHours(secondOtherHours);
        setSecondAutonomousWorkHours(secondAutonomousWorkHours);
        setSecondEctsCredits(secondEctsCredits);

        setBibliographicReferences(references);

        setCompetenceCourseGroupUnit(group == null ? getCompetenceCourse().getCompetenceCourseGroupUnit() : group);
    }

    public RequestStatus getStatus() {
        if (getApproved() == null) {
            return RequestStatus.ON_HOLD;
        } else {
            return (getApproved()) ? RequestStatus.APPROVED : RequestStatus.REJECTED;
        }
    }

    public Unit getScientificAreaUnit() {
        return getCompetenceCourseGroupUnit().getAllParentUnits().stream().filter(u -> u.isScientificAreaUnit()).findFirst()
                .orElse(null);
    }

    public Unit getDepartmentUnit() {
        return getCompetenceCourseGroupUnit().getAllParentUnits().stream().filter(u -> u.isDepartmentUnit()).findFirst()
                .orElse(null);
    }

    public void delete() {
        setCompetenceCourse(null);
        setAnalizedBy(null);
        setRequester(null);
        setExecutionPeriod(null);
        setRootDomainObject(null);
        setCompetenceCourseGroupUnit(null);
        super.deleteDomainObject();
    }

    public enum RequestStatus {
        APPROVED, REJECTED, ON_HOLD;
    }

    @Atomic
    public void reject(Person analisedBy) {
        throw new UnsupportedOperationException();
//        if (!CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(this)) {
//            check(null);
//        }
//        if (getApproved() != null) {
//            throw new DomainException("error.request.already.processed");
//        }
//        setApproved(false);
//        setAnalizedBy(analisedBy);
    }

    @Atomic
    public void approve(Person analisedBy) {
        throw new UnsupportedOperationException();
//        if (!CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(this)) {
//            check(null);
//        }
//        if (getApproved() != null) {
//            throw new DomainException("error.request.already.processed");
//        }
//        setApproved(true);
//        setAnalizedBy(analisedBy);
//
//        CompetenceCourse course = getCompetenceCourse();
//
//        ExecutionInterval executionSemester = getExecutionInterval();
//        CompetenceCourseInformation information = course.findInformation(executionSemester);
//        if (information != null) {
//            information.edit(getName(), getNameEn(), information.getBasic(), getCompetenceCourseLevel(),
//                    getCompetenceCourseGroupUnit());
//            information.setAcademicPeriod(getRegime().convertToAcademicPeriod());
//            information.edit(getObjectives(), getProgram(), getEvaluationMethod(), getObjectivesEn(), getProgramEn(),
//                    getEvaluationMethodEn());
//
//            information.setBibliographicReferences(getBibliographicReferences());
//
//            for (; !information.getCompetenceCourseLoadsSet().isEmpty(); information.getCompetenceCourseLoadsSet().iterator()
//                    .next().delete()) {
//                ;
//            }
//            createLoads(information);
//
//        } else {
//            information = new CompetenceCourseInformation(getName(), getNameEn(), course.isBasic(),
//                    getRegime().convertToAcademicPeriod(), getCompetenceCourseLevel(), getExecutionInterval(),
//                    getCompetenceCourseGroupUnit());
//            information.edit(getObjectives(), getProgram(), getEvaluationMethod(), getObjectivesEn(), getProgramEn(),
//                    getEvaluationMethodEn());
//            information.setAcronym(course.getAcronym());
//            information.setBibliographicReferences(getBibliographicReferences());
//            course.addCompetenceCourseInformations(information);
//
//            createLoads(information);
//        }
    }

    private void createLoads(CompetenceCourseInformation information) {
        CompetenceCourseLoad courseLoad = new CompetenceCourseLoad(getTheoreticalHours(), getProblemsHours(),
                getLaboratorialHours(), getSeminaryHours(), getFieldWorkHours(), getTrainingPeriodHours(),
                getTutorialOrientationHours(), getOtherHours(), getAutonomousWorkHours(), getEctsCredits(), Integer.valueOf(1),
                (getRegime() == RegimeType.SEMESTRIAL) ? AcademicPeriod.SEMESTER : AcademicPeriod.YEAR);

        information.addCompetenceCourseLoads(courseLoad);

        if (getRegime() == RegimeType.ANUAL) {
            CompetenceCourseLoad secondCourseLoad = new CompetenceCourseLoad(getSecondTheoreticalHours(),
                    getSecondProblemsHours(), getSecondLaboratorialHours(), getSecondSeminaryHours(), getSecondFieldWorkHours(),
                    getSecondTrainingPeriodHours(), getSecondTutorialOrientationHours(), getSecondOtherHours(),
                    getSecondAutonomousWorkHours(), getSecondEctsCredits(), Integer.valueOf(2), AcademicPeriod.YEAR);

            information.addCompetenceCourseLoads(secondCourseLoad);
        }
    }

    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }
}
