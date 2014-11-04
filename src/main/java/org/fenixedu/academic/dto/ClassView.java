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
 * @(#)ExecutionCourseView.java Created on Nov 7, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ClassView {

    private final SchoolClass schoolClass;

    public ClassView(final SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public String getClassName() {
        return getSchoolClass().getNome();
    }

    public String getClassOID() {
        return getSchoolClass().getExternalId();
    }

    public Integer getCurricularYear() {
        return getSchoolClass().getAnoCurricular();
    }

    public Integer getSemester() {
        return getSchoolClass().getExecutionPeriod().getSemester();
    }

    public String getDegreeCurricularPlanID() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getExternalId();
    }

    public String getDegreeInitials() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
    }

    public String getExecutionPeriodOID() {
        return getSchoolClass().getExecutionPeriod().getExternalId();
    }

    public String getNameDegreeCurricularPlan() {
        return getSchoolClass().getExecutionDegree().getDegreeCurricularPlan().getName();
    }

}
