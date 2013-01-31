/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

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

	private Student student;

	private Registration registration;

	private StudentCurricularPlan studentCurricularPlan;

	public ChooseStudentCurricularPlanBean(StudentCurricularPlan studentCurricularPlan) {
		setStudentCurricularPlan(studentCurricularPlan);
		setRegistration(studentCurricularPlan.getRegistration());
		setStudent(studentCurricularPlan.getRegistration().getStudent());
	}

	public ChooseStudentCurricularPlanBean() {
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		if (registration == null) {
			this.registration = null;
			this.studentCurricularPlan = null;
		} else {
			this.registration = registration;
		}
	}

	public StudentCurricularPlan getStudentCurricularPlan() {
		return this.studentCurricularPlan == null ? null : studentCurricularPlan;
	}

	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
		if (number != null) {
			setStudent(Registration.readRegistrationByNumberAndDegreeTypes(getNumber(), DegreeType.DEGREE).getStudent());
		}
	}

}
