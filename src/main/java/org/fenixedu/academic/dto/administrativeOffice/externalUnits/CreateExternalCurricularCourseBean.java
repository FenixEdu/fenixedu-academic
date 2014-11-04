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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalCurricularCourseBean implements Serializable {

    private Unit parentUnit;
    private String name;
    private String code;
    private boolean enrolStudent;
    private CreateExternalEnrolmentBean externalEnrolmentBean;

    protected CreateExternalCurricularCourseBean() {

    }

    public CreateExternalCurricularCourseBean(final Unit parentUnit) {
        setParentUnit(parentUnit);
        setExternalEnrolmentBean(new CreateExternalEnrolmentBean());
    }

    public Unit getParentUnit() {
        return this.parentUnit;
    }

    public void setParentUnit(Unit parentUnit) {
        this.parentUnit = parentUnit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnrolStudent() {
        return enrolStudent;
    }

    public boolean isToEnrolStudent() {
        return isEnrolStudent();
    }

    public void setEnrolStudent(boolean enrolStudent) {
        this.enrolStudent = enrolStudent;
    }

    public CreateExternalEnrolmentBean getExternalEnrolmentBean() {
        return externalEnrolmentBean;
    }

    public void setExternalEnrolmentBean(CreateExternalEnrolmentBean externalEnrolmentBean) {
        this.externalEnrolmentBean = externalEnrolmentBean;
    }
}
