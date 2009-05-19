package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

public class QualificationBean implements Serializable {

    static private final long serialVersionUID = 3778404795808964270L;
    
    private QualificationType type;
    private String school;
    private String degree;
    private String year;
    private String mark;

    public QualificationBean() {
    }

    public QualificationType getType() {
	return type;
    }

    public void setType(QualificationType type) {
	this.type = type;
    }

    public String getSchool() {
	return school;
    }

    public void setSchool(String school) {
	this.school = school;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public String getMark() {
	return mark;
    }

    public void setMark(String mark) {
	this.mark = mark;
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(String degree) {
	this.degree = degree;
    }

}
