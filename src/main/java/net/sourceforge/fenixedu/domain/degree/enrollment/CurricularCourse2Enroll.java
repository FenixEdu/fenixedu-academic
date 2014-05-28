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
 * Created on Jun 28, 2004
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 */

public class CurricularCourse2Enroll implements Serializable {

    private CurricularCourse curricularCourse;

    private CurricularCourseEnrollmentType enrollmentType;

    private Integer accumulatedWeight;

    private Double ectsCredits;

    private Boolean optionalCurricularCourse;

    private CurricularYear curricularYear;

    public CurricularCourse2Enroll() {
    }

    public CurricularCourse2Enroll(CurricularCourse curricularCourse, CurricularCourseEnrollmentType enrollmentRuleType,
            Boolean optionalCurricularCourse) {
        this.curricularCourse = curricularCourse;
        this.enrollmentType = enrollmentRuleType;
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    public CurricularCourse2Enroll(CurricularCourse curricularCourse, CurricularCourseEnrollmentType enrollmentRuleType,
            Boolean optionalCurricularCourse, CurricularYear curricularYear) {

        this(curricularCourse, enrollmentRuleType, optionalCurricularCourse);
        this.curricularYear = curricularYear;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public Boolean getOptionalCurricularCourse() {
        return optionalCurricularCourse;
    }

    public void setOptionalCurricularCourse(Boolean optionalCurricularCourse) {
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public CurricularCourseEnrollmentType getEnrollmentType() {
        return enrollmentType;
    }

    public void setEnrollmentType(CurricularCourseEnrollmentType enrollmentRuleType) {
        this.enrollmentType = enrollmentRuleType;
    }

    public Integer getAccumulatedWeight() {
        return accumulatedWeight;
    }

    public void setAccumulatedWeight(Integer accumulatedWeight) {
        this.accumulatedWeight = accumulatedWeight;
    }

    @Override
    public String toString() {
        return getCurricularCourse().getName() + " - " + getCurricularCourse().getCode() + " - " + getEnrollmentType().toString()
                + " - " + getAccumulatedWeight().toString();
    }

    public boolean isOptionalCurricularCourse() {
        return optionalCurricularCourse.booleanValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CurricularCourse2Enroll) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
            return this.getCurricularCourse().equals(curricularCourse2Enroll.getCurricularCourse());
        }
        return false;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public Double getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

}
