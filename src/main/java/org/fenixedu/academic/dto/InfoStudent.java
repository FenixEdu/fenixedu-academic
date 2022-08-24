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
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.joda.time.LocalDate;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

    private final Registration registration;

    public InfoStudent(final Registration registration) {
        this.registration = registration;
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getRegistration().getPerson());
    }

    public Integer getNumber() {
        return getRegistration().getNumber();
    }

    public DegreeType getDegreeType() {
        return getRegistration().getDegreeType();
    }

    public Boolean getPayedTuition() {
        return !TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(registration.getPerson(), new LocalDate());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoStudent && getRegistration() == ((InfoStudent) obj).getRegistration();
    }

    @Override
    public String toString() {
        return getRegistration().toString();
    }

    public static InfoStudent newInfoFromDomain(Registration registration) {
        return registration == null ? null : new InfoStudent(registration);
    }

//    public Boolean getFlunked() {
//        return getRegistration().getFlunked();
//    }

//    public Boolean getInterruptedStudies() {
//        return getRegistration().getInterruptedStudies();
//    }

    @Override
    public String getExternalId() {
        return getRegistration().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    private Registration getRegistration() {
        return registration;
    }
}