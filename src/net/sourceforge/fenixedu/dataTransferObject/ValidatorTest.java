package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Grade;

public class ValidatorTest implements Serializable {

    private Grade grade;
    private DomainReference<Degree> degree;
    private Integer number;

    public Grade getGrade() {
	return grade;
    }

    public void setGrade(Grade grade) {
	this.grade = grade;
    }

    public Degree getDegree() {
	return degree != null ? degree.getObject() : null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null ? new DomainReference<Degree>(degree) : null);
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

}
