package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;

public class ResidenceEventBean implements Serializable {

    private String userName;
    private String fiscalNumber;
    private String name;
    private DomainReference<Student> student;
    private Money roomValue;
    private String statusMessage;    
    
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
    
    public String getStatusMessage() {
	return statusMessage;
    }
    
    public boolean getStatus() {
	if (!StringUtils.isNumeric(userName)) {
	    statusMessage = "label.error.invalid.student.number";
	    return false;
	}

	Student student = Student.readStudentByNumber(Integer.valueOf(userName));
	if (student == null || !student.hasPerson()) {
	    statusMessage = "label.error.invalid.student.number";
	    return false;
	}
	setStudent(student);
	
	
	ResidenceYear year = ResidenceYear.getCurrentYear();	
	ResidenceManagementUnit unit = year.getUnit();
	if (!roomValue.equals(unit.getCurrentSingleRoomValue()) && !roomValue.equals(unit.getCurrentDoubleRoomValue())) {
	    statusMessage = "label.error.invalid.payment.amount";
	    return false;
	}
	
	String socialSecurityNumber = student.getPerson().getSocialSecurityNumber();
	if (socialSecurityNumber != null && !socialSecurityNumber.equalsIgnoreCase(fiscalNumber.trim())) {
	    statusMessage = "label.error.invalid.fiscalNumber";
	    return false;
	}

	 return true;
    }
}
