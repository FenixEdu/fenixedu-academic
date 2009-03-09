package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class TutorateBean implements Serializable {

    private Integer personNumber;
    private Integer degreeCurricularPeriod;

    public TutorateBean() {
    }

    public TutorateBean(Person person) {
	setPersonNumber(person.getTeacher().getTeacherNumber());
    }

    public Integer getPersonNumber() {
	return personNumber;
    }

    public void setPersonNumber(Integer studentNumber) {
	this.personNumber = studentNumber;
    }

    public Integer getDegreeCurricularPeriod() {
	return degreeCurricularPeriod;
    }

    public void setDegreeCurricularPeriod(Integer degreeCurricularPeriod) {
	this.degreeCurricularPeriod = degreeCurricularPeriod;
    }

}