package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;

public class TutorStatisticsBean implements Serializable, Comparable {
    private Integer approvedEnrolmentsNumber = 0;
    private Integer studentsNumber = 0;
    private Integer totalStudentsNumber = 0;

    public TutorStatisticsBean() {
    }

    public TutorStatisticsBean(Integer studentsNumber, Integer approvedEnrolmentsNumber, Integer totalStudentsNumber) {
	setStudentsNumber(studentsNumber);
	setApprovedEnrolmentsNumber(approvedEnrolmentsNumber);
	setTotalStudentsNumber(totalStudentsNumber);
    }

    public Integer getStudentsNumber() {
	return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
	this.studentsNumber = studentsNumber;
    }

    public Integer getApprovedEnrolmentsNumber() {
	return approvedEnrolmentsNumber;
    }

    public void setApprovedEnrolmentsNumber(Integer approvedEnrolmentsNumber) {
	this.approvedEnrolmentsNumber = approvedEnrolmentsNumber;
    }

    public Integer getTotalStudentsNumber() {
	return totalStudentsNumber;
    }

    public void setTotalStudentsNumber(Integer totalStudentsNumber) {
	this.totalStudentsNumber = totalStudentsNumber;
    }

    public String getStudentsRatio() {
	if (this.totalStudentsNumber != 0)
	    return String.format("%.1f", ((float) studentsNumber / (float) totalStudentsNumber) * 100);
	else
	    return "0.0";
    }

    public int compareTo(Object o) {
	TutorStatisticsBean bean = (TutorStatisticsBean) o;
	if (this.approvedEnrolmentsNumber >= bean.getApprovedEnrolmentsNumber()) {
	    return -1;
	}
	return 1;
    }
}
