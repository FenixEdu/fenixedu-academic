package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;

public class ResidenceEventBean implements Serializable {

    String userName;
    String fiscalNumber;
    String name;
    DomainReference<Student> student;
    Money roomValue;

    public ResidenceEventBean(String userName, String fiscalNumber, String name, Double roomValue) {
	this.userName = userName;
	this.fiscalNumber = fiscalNumber;
	this.name = name;
	this.roomValue = new Money(roomValue);
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
	this.student = new DomainReference<Student>(student);
    }
    
    public Student getStudent() {
	return this.student.getObject();
    }
    
    public boolean getStatus() {
	if (!StringUtils.isNumeric(userName)) {
	    return false;
	}

	Student student = Student.readStudentByNumber(Integer.valueOf(userName));
	if (student == null || !student.hasPerson()) {
	    return false;
	}
	
	setStudent(student);
	String socialSecurityNumber = student.getPerson().getSocialSecurityNumber();
	return socialSecurityNumber == null || socialSecurityNumber.equalsIgnoreCase(fiscalNumber.trim());

    }
}
