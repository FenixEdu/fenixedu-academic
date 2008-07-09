package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;

public class ResidenceEventBean implements Serializable {

    String userName;
    String fiscalNumber;
    String name;
    Double roomValue;

    public ResidenceEventBean(String userName, String fiscalNumber, String name, Double roomValue) {
	this.userName = userName;
	this.fiscalNumber = fiscalNumber;
	this.name = name;
	this.roomValue = roomValue;
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

    public Double getRoomValue() {
	return roomValue;
    }

    public void setRoomValue(Double roomValue) {
	this.roomValue = roomValue;
    }

    public boolean getStatus() {
	if (!StringUtils.isNumeric(userName)) {
	    return false;
	}

	Student student = Student.readStudentByNumber(Integer.valueOf(userName));
	if (student == null || !student.hasPerson()) {
	    return false;
	}
	String socialSecurityNumber = student.getPerson().getSocialSecurityNumber();
	return socialSecurityNumber == null || socialSecurityNumber.equalsIgnoreCase(fiscalNumber.trim());

    }
}
