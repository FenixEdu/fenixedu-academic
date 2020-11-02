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
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package org.fenixedu.academic.dto;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.curriculum.grade.GradeScale;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.util.CurricularCourseExecutionScope;
import org.fenixedu.commons.i18n.I18N;

/**
 * @author tfc130
 */
public class InfoCurricularCourse extends InfoObject {

    static final public Comparator<InfoCurricularCourse> COMPARATOR_BY_NAME_AND_ID = new Comparator<InfoCurricularCourse>() {
        @Override
        public int compare(InfoCurricularCourse o1, InfoCurricularCourse o2) {
            int result = Collator.getInstance().compare(o1.getName(), o2.getName());
            return (result == 0) ? o1.getExternalId().compareTo(o2.getExternalId()) : result;
        }
    };

    private final CurricularCourse curricularCourse;

    private final boolean showEnVersion = I18N.getLocale().equals(org.fenixedu.academic.util.LocaleUtils.EN);

    private List infoAssociatedExecutionCourses;

    private String chosen;

    public InfoCurricularCourse(final CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

//    public Boolean getBasic() {
//        return getCurricularCourse().getBasic();
//    }
//
//    public String getOwnershipType() {
//        return getBasic() == null ? "" : getBasic().booleanValue() ? "Básica" : "Não Básica";
//    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCurricularCourse && getCurricularCourse() == ((InfoCurricularCourse) obj).getCurricularCourse();
    }

    @Override
    public String toString() {
        return getCurricularCourse().toString();
    }

    public String getCode() {
        return getCurricularCourse().getCode();
    }

    public Double getCredits() {
        return getCurricularCourse().getCredits();
    }

//    public Double getLabHours() {
//        return getCurricularCourse().getLabHours();
//    }
//
//    public Double getPraticalHours() {
//        return getCurricularCourse().getPraticalHours();
//    }
//
//    public Double getTheoPratHours() {
//        return getCurricularCourse().getTheoPratHours();
//    }
//
//    public Double getTheoreticalHours() {
//        return getCurricularCourse().getTheoreticalHours();
//    }
//
//    public Double getFieldWorkHours() {
//        return getCurricularCourse().getFieldWorkHours();
//    }
//
//    public Double getProblemsHours() {
//        return getCurricularCourse().getProblemsHours();
//    }
//
//    public Double getSeminaryHours() {
//        return getCurricularCourse().getSeminaryHours();
//    }
//
//    public Double getTrainingPeriodHours() {
//        return getCurricularCourse().getTrainingPeriodHours();
//    }
//
//    public Double getTutorialOrientationHours() {
//        return getCurricularCourse().getTutorialOrientationHours();
//    }
//
//    public Double getTotalLabHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.LABORATORIAL, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalPraticalHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.PRATICA, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalTheoPratHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TEORICO_PRATICA, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalTheoreticalHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TEORICA, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalFieldWorkHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.FIELD_WORK, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalProblemsHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.PROBLEMS, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalSeminaryHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.SEMINARY, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalTrainingPeriodHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TRAINING_PERIOD, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }
//
//    public Double getTotalTutorialOrientationHours() {
//        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TUTORIAL_ORIENTATION, null);
//        return totalHours != null ? totalHours.doubleValue() : 0d;
//    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getCurricularCourse().getDegreeCurricularPlan());
    }

//    public CurricularCourseType getType() {
//        return getCurricularCourse().getType();
//    }
//
//    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
//        return getCurricularCourse().getCurricularCourseExecutionScope();
//    }
//
//    public Boolean getMandatory() {
//        return getCurricularCourse().getMandatory();
//    }

//    public boolean infoCurricularCourseIsMandatory() {
//        return getMandatory().booleanValue();
//    }

    public List getInfoAssociatedExecutionCourses() {
        return infoAssociatedExecutionCourses;
    }

    public void setInfoAssociatedExecutionCourses(List infoAssociatedExecutionCourses) {
        this.infoAssociatedExecutionCourses = infoAssociatedExecutionCourses;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public Double getEctsCredits() {
        return getCurricularCourse().getEctsCredits();
    }

//    public Integer getEnrollmentWeigth() {
//        return getCurricularCourse().getEnrollmentWeigth();
//    }
//
//    public Integer getMaximumValueForAcumulatedEnrollments() {
//        return getCurricularCourse().getMaximumValueForAcumulatedEnrollments();
//    }
//
//    public Integer getMinimumValueForAcumulatedEnrollments() {
//        return getCurricularCourse().getMinimumValueForAcumulatedEnrollments();
//    }

    public Double getWeigth() {
        return getCurricularCourse().getWeigth();
    }

//    public Boolean getMandatoryEnrollment() {
//        return getCurricularCourse().getMandatoryEnrollment();
//    }
//
//    public Boolean getEnrollmentAllowed() {
//        return getCurricularCourse().getEnrollmentAllowed();
//    }

    public String getAcronym() {
        return getCurricularCourse().getAcronym();
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getName() {
        return showEnVersion && getCurricularCourse().getNameEn() != null
                && getCurricularCourse().getNameEn().length() > 0 ? getCurricularCourse()
                        .getNameEn() : getCurricularCourse().getName();
    }

    public String getNameEn() {
        return getCurricularCourse().getNameEn();
    }

    public String getNameAndCode() {
        return getCode() + " - " + getName();
    }

    @Override
    public String getExternalId() {
        return getCurricularCourse().getExternalId();
    }

//    public RegimeType getRegimeType() {
//        return getCurricularCourse().getRegimeType();
//    }

}
