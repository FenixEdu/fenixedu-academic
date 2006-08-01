package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.Student;

public class SpecialSeasonEnrolmentBean implements Serializable{

	private Integer studentNumber;
	private DomainReference<Student> student;
	private DomainReference<ExecutionYear> executionYear;
	private Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrol;
	private Collection<SpecialSeasonToEnrolBean> specialSeasonAlreadyEnroled;
	private DomainReference<SpecialSeasonCode> specialSeasonCode;
	
	public ExecutionYear getExecutionYear() {
		return (this.executionYear != null) ? executionYear.getObject() : null;
	}
	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	public Collection<SpecialSeasonToEnrolBean> getSpecialSeasonAlreadyEnroled() {
		return specialSeasonAlreadyEnroled;
	}
	public void setSpecialSeasonAlreadyEnroled(
			Collection<SpecialSeasonToEnrolBean> specialSeasonAlreadyEnroled) {
		this.specialSeasonAlreadyEnroled = specialSeasonAlreadyEnroled;
	}
	public Collection<SpecialSeasonToEnrolBean> getSpecialSeasonToEnrol() {
		return specialSeasonToEnrol;
	}
	public void setSpecialSeasonToEnrol(
			Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrol) {
		this.specialSeasonToEnrol = specialSeasonToEnrol;
	}
	public Student getStudent() {
		return (this.student != null ) ? student.getObject() : null;
	}
	public void setStudent(Student student) {
		this.student = new DomainReference<Student>(student);
	}
	public Integer getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(Integer studentNumber) {
		this.studentNumber = studentNumber;
	}
	
	public Collection<Enrolment> getSpecialSeasonToEnrolSubmited(){
		return getSubmited(this.getSpecialSeasonToEnrol());
	}
	
	public Collection<Enrolment> getSpecialSeasonAlreadyEnroledSubmited(){
		return getSubmited(this.getSpecialSeasonAlreadyEnroled());
	}

	public SpecialSeasonCode getSpecialSeasonCode() {
		return (this.specialSeasonCode != null) ? specialSeasonCode.getObject() : null;
	}
	
	public void setSpecialSeasonCode(SpecialSeasonCode specialSeasonCode) {
		this.specialSeasonCode = new DomainReference<SpecialSeasonCode>(specialSeasonCode);
	}

	private Collection<Enrolment> getSubmited(Collection<SpecialSeasonToEnrolBean> specialSeasonToEnrolBean){
		Set<Enrolment> enrolments = new HashSet<Enrolment>();
		for (SpecialSeasonToEnrolBean bean : specialSeasonToEnrolBean) {
			if(bean.isToSubmit()) {
				enrolments.add(bean.getEnrolment());
			}
		}
		return enrolments;
	}
	
	
}
