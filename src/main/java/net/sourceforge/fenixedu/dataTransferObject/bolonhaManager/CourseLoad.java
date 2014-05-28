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
/*
 * Created on Jan 2, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.bolonhaManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;

public class CourseLoad implements Serializable {
    private Double theoreticalHours = Double.valueOf(0);
    private Double problemsHours = Double.valueOf(0);
    private Double laboratorialHours = Double.valueOf(0);
    private Double seminaryHours = Double.valueOf(0);
    private Double fieldWorkHours = Double.valueOf(0);
    private Double trainingPeriodHours = Double.valueOf(0);
    private Double tutorialOrientationHours = Double.valueOf(0);
    private Double autonomousWorkHours = Double.valueOf(0);
    private Double ectsCredits = Double.valueOf(0);

    private String identification = null;
    private int order;
    private String action; // create-edit-delete

    public CourseLoad(int order) {
        setAction("create");
        setOrder(order);
    }

    public CourseLoad(String action, int order) {
        setAction(action);
        setOrder(order);
    }

    public CourseLoad(String action, CompetenceCourseLoad competenceCourseLoad) {
        setAction(action);
        setIdentification(competenceCourseLoad.getExternalId());
        setTheoreticalHours(competenceCourseLoad.getTheoreticalHours());
        setProblemsHours(competenceCourseLoad.getProblemsHours());
        setLaboratorialHours(competenceCourseLoad.getLaboratorialHours());
        setSeminaryHours(competenceCourseLoad.getSeminaryHours());
        setFieldWorkHours(competenceCourseLoad.getFieldWorkHours());
        setTrainingPeriodHours(competenceCourseLoad.getTrainingPeriodHours());
        setTutorialOrientationHours(competenceCourseLoad.getTutorialOrientationHours());
        setAutonomousWorkHours(competenceCourseLoad.getAutonomousWorkHours());
        setEctsCredits(competenceCourseLoad.getEctsCredits());
        setOrder(competenceCourseLoad.getLoadOrder().intValue());
    }

    public Double getAutonomousWorkHours() {
        return autonomousWorkHours;
    }

    public void setAutonomousWorkHours(Double autonomousWorkHours) {
        this.autonomousWorkHours = autonomousWorkHours;
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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
