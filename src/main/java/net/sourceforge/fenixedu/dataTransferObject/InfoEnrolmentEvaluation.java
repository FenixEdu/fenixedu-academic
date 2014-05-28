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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author dcs-rjao
 * 
 *         22/Abr/2003
 */
public class InfoEnrolmentEvaluation extends InfoObject {
    private String gradeValue;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Date examDate;

    private Date gradeAvailableDate;

    private String observation;

    private Date when;

    private EnrolmentEvaluationState state;

    private InfoEnrolment infoEnrolment;

    private InfoPerson infoPersonResponsibleForGrade;

    private InfoPerson infoEmployee;

    public InfoEnrolmentEvaluation() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof InfoEnrolmentEvaluation) {
            InfoEnrolmentEvaluation InfoEnrolmentEvaluation = (InfoEnrolmentEvaluation) obj;

            resultado =
                    this.getInfoEnrolment().equals(InfoEnrolmentEvaluation.getInfoEnrolment())
                            && this.getEnrolmentEvaluationType().equals(InfoEnrolmentEvaluation.getEnrolmentEvaluationType());
        }
        return resultado;
    }

    @Override
    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "grade = " + this.gradeValue + "; ";
        result += "evaluationType = " + this.enrolmentEvaluationType + "; ";
        result += "examDate = " + this.examDate + "; ";
        result += "infoPersonResponsibleForGrade = " + this.infoPersonResponsibleForGrade + "; ";
        result += "state = " + this.state + "; ";
        result += "infoEnrolment = " + this.infoEnrolment + "; ";
        result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
        result += "employee = " + this.infoEmployee + "]\n";
        result += "when = " + this.when + "]\n";
        return result;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public Date getExamDate() {
        return examDate;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public Date getGradeAvailableDate() {
        return gradeAvailableDate;
    }

    public EnrolmentEvaluationState getState() {
        return state;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
        enrolmentEvaluationType = type;
    }

    public void setExamDate(Date date) {
        examDate = date;
    }

    public void setGradeValue(String string) {
        gradeValue = string;
    }

    public void setGradeAvailableDate(Date date) {
        gradeAvailableDate = date;
    }

    public void setState(EnrolmentEvaluationState state) {
        this.state = state;
    }

    public InfoEnrolment getInfoEnrolment() {
        return infoEnrolment;
    }

    public void setInfoEnrolment(InfoEnrolment enrolment) {
        infoEnrolment = enrolment;
    }

    public InfoPerson getInfoPersonResponsibleForGrade() {
        return infoPersonResponsibleForGrade;
    }

    public void setInfoPersonResponsibleForGrade(InfoPerson person) {
        infoPersonResponsibleForGrade = person;
    }

    /**
     * @return
     */
    public String getObservation() {
        return observation;
    }

    /**
     * @param string
     */
    public void setObservation(String string) {
        observation = string;
    }

    /**
     * @return
     */
    public InfoPerson getInfoEmployee() {
        return infoEmployee;
    }

    /**
     * @param person
     */
    public void setInfoEmployee(InfoPerson person) {
        infoEmployee = person;
    }

    /**
     * @return
     */
    public Date getWhen() {
        return when;
    }

    /**
     * @param date
     */
    public void setWhen(Date date) {
        when = date;
    }

    public void copyFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
        super.copyFromDomain(enrolmentEvaluation);
        if (enrolmentEvaluation != null) {
            setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
            setState(enrolmentEvaluation.getEnrolmentEvaluationState());
            setGradeValue(enrolmentEvaluation.getGradeValue());
            setExamDate(enrolmentEvaluation.getExamDate());
            setGradeAvailableDate(enrolmentEvaluation.getGradeAvailableDate());
            setObservation(enrolmentEvaluation.getObservation());
            setWhen(enrolmentEvaluation.getWhen());
        }
    }

    public static InfoEnrolmentEvaluation newInfoFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;
        if (enrolmentEvaluation != null) {
            infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
            infoEnrolmentEvaluation.copyFromDomain(enrolmentEvaluation);
        }

        return infoEnrolmentEvaluation;
    }
}