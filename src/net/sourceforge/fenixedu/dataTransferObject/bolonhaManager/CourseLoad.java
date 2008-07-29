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
    
    private Integer identification = null;
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
        setIdentification(competenceCourseLoad.getIdInternal());
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

    public Integer getIdentification() {
        return identification;
    }

    public void setIdentification(Integer identification) {
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
