package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SpecialSeasonEnrolmentBean implements Serializable {

    private Integer studentNumber;
    private Registration student;
    private ExecutionYear executionYear;
    private Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrol;
    private Collection<SpecialSeasonToEnrolBean> specialSeasonAlreadyEnroled;
    private SpecialSeasonCode specialSeasonCode;

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? executionYear : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public Collection<SpecialSeasonToEnrolBean> getSpecialSeasonAlreadyEnroled() {
	return specialSeasonAlreadyEnroled;
    }

    public void setSpecialSeasonAlreadyEnroled(Collection<SpecialSeasonToEnrolBean> specialSeasonAlreadyEnroled) {
	this.specialSeasonAlreadyEnroled = specialSeasonAlreadyEnroled;
    }

    public Collection<SpecialSeasonToEnrolBean> getSpecialSeasonToEnrol() {
	return specialSeasonToEnrol;
    }

    public void setSpecialSeasonToEnrol(Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrol) {
	this.specialSeasonToEnrol = specialSeasonToEnrol;
    }

    public Registration getStudent() {
	return (this.student != null) ? student : null;
    }

    public void setStudent(Registration registration) {
	this.student = registration;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public Collection<Enrolment> getSpecialSeasonToEnrolSubmited() {
	return getSubmited(this.getSpecialSeasonToEnrol());
    }

    public Collection<Enrolment> getSpecialSeasonAlreadyEnroledSubmited() {
	return getSubmited(this.getSpecialSeasonAlreadyEnroled());
    }

    public SpecialSeasonCode getSpecialSeasonCode() {
	return (this.specialSeasonCode != null) ? specialSeasonCode : null;
    }

    public void setSpecialSeasonCode(SpecialSeasonCode specialSeasonCode) {
	this.specialSeasonCode = specialSeasonCode;
    }

    private Collection<Enrolment> getSubmited(Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrolBean) {
	Set<Enrolment> enrolments = new HashSet<Enrolment>();
	for (SpecialSeasonToEnrolBean bean : specialSeasonToEnrolBean) {
	    if (bean.isToSubmit()) {
		enrolments.add(bean.getEnrolment());
	    }
	}
	return enrolments;
    }

}
