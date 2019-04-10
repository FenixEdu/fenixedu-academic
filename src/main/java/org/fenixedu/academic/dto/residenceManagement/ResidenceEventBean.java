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
package org.fenixedu.academic.dto.residenceManagement;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.organizationalStructure.ResidenceManagementUnit;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.domain.residence.ResidenceYear;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Money;

public class ResidenceEventBean implements Serializable {

    private String userName;
    private String fiscalNumber;
    private String name;
    private Student student;
    private Money roomValue;
    private String statusMessage;
    private String room;
    private ResidenceMonth residenceMonth;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public ResidenceEventBean(ResidenceMonth residenceMonth, String userName, String fiscalNumber, String name, Double roomValue, String room) {
        this.userName = userName;
        this.fiscalNumber = fiscalNumber;
        this.name = name;
        this.roomValue = new Money(roomValue);
        this.room = room;
        this.residenceMonth = residenceMonth;
        setStudent(null);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }

    public void setFiscalNumber(String fiscalNumber) {
        this.fiscalNumber = fiscalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getRoomValue() {
        return roomValue;
    }

    public void setRoomValue(Money roomValue) {
        this.roomValue = roomValue;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return this.student;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean getStatus() {
        if (StringUtils.isEmpty(userName) || !StringUtils.isNumeric(userName)) {
            statusMessage = "label.error.invalid.student.number";
            return false;
        }

        Student student = Student.readStudentByNumber(Integer.valueOf(userName));
        if (student == null || student.getPerson() == null) {
            statusMessage = "label.error.invalid.student.number";
            return false;
        }
        setStudent(student);

        /*
        // Validate room values of excel vs current room values in database

        ResidenceYear year = ResidenceYear.getCurrentYear();
        ResidenceManagementUnit unit = year.getUnit();

        if (!roomValue.equals(unit.getCurrentSingleRoomValue()) && !roomValue.equals(unit.getCurrentDoubleRoomValue())) {
	        statusMessage = "label.error.invalid.payment.amount";
	        return false;
	    }
	    */

        String socialSecurityNumber = student.getPerson().getSocialSecurityNumber();
        if (socialSecurityNumber == null || !socialSecurityNumber.equalsIgnoreCase(fiscalNumber.trim())) {
            statusMessage = "label.error.invalid.fiscalNumber";
            return false;
        }

        if (residenceMonth.isEventPresent(student.getPerson())) {
            statusMessage = "label.error.invalid.eventPresent";
            return false;
        }

        return true;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ResidenceMonth getResidenceMonth() {
        return residenceMonth;
    }

    public void setResidenceMonth(final ResidenceMonth residenceMonth) {
        this.residenceMonth = residenceMonth;
    }
}
