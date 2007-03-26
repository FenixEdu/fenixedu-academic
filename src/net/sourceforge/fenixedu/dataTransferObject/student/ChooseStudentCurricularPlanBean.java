/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ChooseStudentCurricularPlanBean implements Serializable {

    private Integer number;

    private DomainReference<Student> student;

    private DomainReference<Registration> registration;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    public ChooseStudentCurricularPlanBean(StudentCurricularPlan studentCurricularPlan) {
	setStudentCurricularPlan(studentCurricularPlan);
	setRegistration(studentCurricularPlan.getRegistration());
	setStudent(studentCurricularPlan.getRegistration().getStudent());
    }

    public ChooseStudentCurricularPlanBean() {
    }

    public Student getStudent() {
	return student == null ? null : student.getObject();
    }

    public void setStudent(Student student) {
	this.student = student == null ? null : new DomainReference<Student>(student);
    }

    public Registration getRegistration() {
	return registration == null ? null : registration.getObject();
    }

    public void setRegistration(Registration registration) {
	if (registration == null) {
	    this.registration = null;
	    this.studentCurricularPlan = null;
	} else {
	    this.registration = new DomainReference<Registration>(registration);
	}
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return this.studentCurricularPlan == null ? null : studentCurricularPlan.getObject();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan == null ? null
		: new DomainReference<StudentCurricularPlan>(studentCurricularPlan);
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
	if (number != null) {
	    setStudent(Registration.readRegistrationByNumberAndDegreeTypes(getNumber(),
		    DegreeType.DEGREE).getStudent());
	}
    }

}
