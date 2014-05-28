/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class CompetenceCourseLoadBean implements Serializable {
    private Double theoreticalHours;
    private Double problemsHours;
    private Double laboratorialHours;
    private Double seminaryHours;
    private Double fieldWorkHours;
    private Double trainingPeriodHours;
    private Double tutorialOrientationHours;
    private Double autonomousWorkHours;
    private Double ectsCredits;

    private Double secondTheoreticalHours;
    private Double secondProblemsHours;
    private Double secondLaboratorialHours;
    private Double secondSeminaryHours;
    private Double secondFieldWorkHours;
    private Double secondTrainingPeriodHours;
    private Double secondTutorialOrientationHours;
    private Double secondAutonomousWorkHours;
    private Double secondEctsCredits;
    private Integer loadOrder;

    private boolean sameInformationForBothPeriods;
    private AcademicPeriod academicPeriod;

    // Values from CompetenceCourseLoad
    private Double contactLoad;
    private Double totalLoad;

    public CompetenceCourseLoadBean() {
        setAcademicPeriod(AcademicPeriod.SEMESTER);
        sameInformationForBothPeriods = true;
    }

    public CompetenceCourseLoadBean(CompetenceCourseInformationChangeRequest request) {
        setAcademicPeriod(request.getRegime() == RegimeType.SEMESTRIAL ? AcademicPeriod.SEMESTER : AcademicPeriod.YEAR);
        setTheoreticalHours(request.getTheoreticalHours());
        setProblemsHours(request.getProblemsHours());
        setLaboratorialHours(request.getLaboratorialHours());
        setSeminaryHours(request.getSeminaryHours());
        setFieldWorkHours(request.getFieldWorkHours());
        setTrainingPeriodHours(request.getTrainingPeriodHours());
        setTutorialOrientationHours(request.getTutorialOrientationHours());
        setAutonomousWorkHours(request.getAutonomousWorkHours());
        setEctsCredits(request.getEctsCredits());

        setSecondTheoreticalHours(request.getSecondTheoreticalHours());
        setSecondProblemsHours(request.getSecondProblemsHours());
        setSecondLaboratorialHours(request.getSecondLaboratorialHours());
        setSecondSeminaryHours(request.getSecondSeminaryHours());
        setSecondFieldWorkHours(request.getSecondFieldWorkHours());
        setSecondTrainingPeriodHours(request.getSecondTrainingPeriodHours());
        setSecondTutorialOrientationHours(request.getSecondTutorialOrientationHours());
        setSecondAutonomousWorkHours(request.getSecondAutonomousWorkHours());
        setSecondEctsCredits(request.getSecondEctsCredits());
    }

    public CompetenceCourseLoadBean(CompetenceCourseLoad competenceCourseLoad) {
        this();
        setTheoreticalHours(competenceCourseLoad.getTheoreticalHours());
        setProblemsHours(competenceCourseLoad.getProblemsHours());
        setLaboratorialHours(competenceCourseLoad.getLaboratorialHours());
        setSeminaryHours(competenceCourseLoad.getSeminaryHours());
        setFieldWorkHours(competenceCourseLoad.getFieldWorkHours());
        setTrainingPeriodHours(competenceCourseLoad.getTrainingPeriodHours());
        setTutorialOrientationHours(competenceCourseLoad.getTutorialOrientationHours());
        setAutonomousWorkHours(competenceCourseLoad.getAutonomousWorkHours());
        setEctsCredits(competenceCourseLoad.getEctsCredits());
        setLoadOrder(competenceCourseLoad.getLoadOrder());
        setContactLoad(competenceCourseLoad.getContactLoad());
        setTotalLoad(competenceCourseLoad.getTotalLoad());
    }

    public Double getAutonomousWorkHours() {
        return autonomousWorkHours;
    }

    public void setAutonomousWorkHours(Double autonomousWorkHours) {
        this.autonomousWorkHours = autonomousWorkHours;
    }

    public AcademicPeriod getAcademicPeriod() {
        return academicPeriod;
    }

    public void setAcademicPeriod(AcademicPeriod academicPeriod) {
        this.academicPeriod = academicPeriod;
    }

    public Double getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public Double getFieldWorkHours() {
        return fieldWorkHours;
    }

    public void setFieldWorkHours(Double fieldWorkHours) {
        this.fieldWorkHours = fieldWorkHours;
    }

    public Double getLaboratorialHours() {
        return laboratorialHours;
    }

    public void setLaboratorialHours(Double laboratorialHours) {
        this.laboratorialHours = laboratorialHours;
    }

    public Double getProblemsHours() {
        return problemsHours;
    }

    public void setProblemsHours(Double problemsHours) {
        this.problemsHours = problemsHours;
    }

    public Double getSeminaryHours() {
        return seminaryHours;
    }

    public void setSeminaryHours(Double seminaryHours) {
        this.seminaryHours = seminaryHours;
    }

    public Double getTheoreticalHours() {
        return theoreticalHours;
    }

    public void setTheoreticalHours(Double theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    public Double getTrainingPeriodHours() {
        return trainingPeriodHours;
    }

    public void setTrainingPeriodHours(Double trainingPeriodHours) {
        this.trainingPeriodHours = trainingPeriodHours;
    }

    public Double getTutorialOrientationHours() {
        return tutorialOrientationHours;
    }

    public void setTutorialOrientationHours(Double tutorialOrientationHours) {
        this.tutorialOrientationHours = tutorialOrientationHours;
    }

    public boolean isSameInformationForBothPeriods() {
        return sameInformationForBothPeriods;
    }

    public void setSameInformationForBothPeriods(boolean sameInformationForBothPeriods) {
        this.sameInformationForBothPeriods = sameInformationForBothPeriods;
    }

    public Double getSecondAutonomousWorkHours() {
        return (isSameInformationForBothPeriods()) ? getAutonomousWorkHours() : secondAutonomousWorkHours;
    }

    public void setSecondAutonomousWorkHours(Double secondAutonomousWorkHours) {
        this.secondAutonomousWorkHours = secondAutonomousWorkHours;
    }

    public Double getSecondEctsCredits() {
        return (isSameInformationForBothPeriods()) ? getEctsCredits() : secondEctsCredits;
    }

    public void setSecondEctsCredits(Double secondEctsCredits) {
        this.secondEctsCredits = secondEctsCredits;
    }

    public Double getSecondFieldWorkHours() {
        return (isSameInformationForBothPeriods()) ? getFieldWorkHours() : secondFieldWorkHours;
    }

    public void setSecondFieldWorkHours(Double secondFieldWorkHours) {
        this.secondFieldWorkHours = secondFieldWorkHours;
    }

    public Double getSecondLaboratorialHours() {
        return (isSameInformationForBothPeriods()) ? getLaboratorialHours() : secondLaboratorialHours;
    }

    public void setSecondLaboratorialHours(Double secondLaboratorialHours) {
        this.secondLaboratorialHours = secondLaboratorialHours;
    }

    public Double getSecondProblemsHours() {
        return (isSameInformationForBothPeriods()) ? getProblemsHours() : secondProblemsHours;
    }

    public void setSecondProblemsHours(Double secondProblemsHours) {
        this.secondProblemsHours = secondProblemsHours;
    }

    public Double getSecondSeminaryHours() {
        return (isSameInformationForBothPeriods()) ? getSeminaryHours() : secondSeminaryHours;
    }

    public void setSecondSeminaryHours(Double secondSeminaryHours) {
        this.secondSeminaryHours = secondSeminaryHours;
    }

    public Double getSecondTheoreticalHours() {
        return (isSameInformationForBothPeriods()) ? getTheoreticalHours() : secondTheoreticalHours;
    }

    public void setSecondTheoreticalHours(Double secondTheoreticalHours) {
        this.secondTheoreticalHours = secondTheoreticalHours;
    }

    public Double getSecondTrainingPeriodHours() {
        return (isSameInformationForBothPeriods()) ? getTrainingPeriodHours() : secondTrainingPeriodHours;
    }

    public void setSecondTrainingPeriodHours(Double secondTrainingPeriodHours) {
        this.secondTrainingPeriodHours = secondTrainingPeriodHours;
    }

    public Double getSecondTutorialOrientationHours() {
        return (isSameInformationForBothPeriods()) ? getTutorialOrientationHours() : secondTutorialOrientationHours;
    }

    public void setSecondTutorialOrientationHours(Double secondTutorialOrientationHours) {
        this.secondTutorialOrientationHours = secondTutorialOrientationHours;
    }

    public Integer getLoadOrder() {
        return loadOrder;
    }

    public void setLoadOrder(Integer loadOrder) {
        this.loadOrder = loadOrder;
    }

    public Double getContactLoad() {
        return contactLoad;
    }

    private void setContactLoad(Double contactLoad) {
        this.contactLoad = contactLoad;
    }

    public Double getTotalLoad() {
        return totalLoad;
    }

    private void setTotalLoad(Double totalLoad) {
        this.totalLoad = totalLoad;
    }

}
