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
 * InfoClass.java
 * 
 * Created on 31 de Outubro de 2002, 12:27
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

/**
 * @author Luis Cruz &amp; Sara Ribeiro
 */
public class InfoClass extends InfoObject {

    private final SchoolClass schoolClass;

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public InfoClass(final SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getNome() {
        return getSchoolClass().getNome();
    }

    public Integer getAnoCurricular() {
        return getSchoolClass().getAnoCurricular();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getSchoolClass() == ((InfoClass) obj).getSchoolClass();
    }

    @Override
    public String toString() {
        return getSchoolClass().toString();
    }

//    public InfoExecutionDegree getInfoExecutionDegree() {
//        return InfoExecutionDegree.newInfoFromDomain(getSchoolClass().getExecutionDegree());
//    }

    public AcademicInterval getAcademicInterval() {
        return getSchoolClass().getAcademicInterval();
    }

//    @Deprecated
//    public InfoExecutionPeriod getInfoExecutionPeriod() {
//        return InfoExecutionPeriod.newInfoFromDomain(getSchoolClass().getExecutionPeriod());
//    }

    public static InfoClass newInfoFromDomain(final SchoolClass schoolClass) {
        return schoolClass == null ? null : new InfoClass(schoolClass);
    }

    @Override
    public String getExternalId() {
        return getSchoolClass().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
