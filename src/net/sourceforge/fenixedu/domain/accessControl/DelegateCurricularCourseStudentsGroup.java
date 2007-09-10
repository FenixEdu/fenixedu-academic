package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class DelegateCurricularCourseStudentsGroup extends LeafGroup {

	private static final long serialVersionUID = 1L;
	
	private DomainReference<CurricularCourse> curricularCourse;
	
	private DomainReference<ExecutionYear> executionYear;
	
	private List<DomainReference<Person>> enrolledStudents;
	
	public DelegateCurricularCourseStudentsGroup (CurricularCourse curricularCourse, ExecutionYear executionYear) {
		setCurricularCourse(curricularCourse);
		setExecutionYear(executionYear);
		setEnrolledStudents();
	}
	
	@Override
	public Set<Person> getElements() {
		Set<Person> people = new HashSet<Person>();
		
		for(Student student : getStudentsEnrolledIn(getCurricularCourse(), getExecutionYear())) {
			people.add(student.getPerson());
		}
				
		return people;
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] {};
	}
	
	@Override
	public boolean isMember(Person person) {
		if(person.hasStudent()) {
			if(getElements().contains(person.getStudent())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getName() {
		return getCurricularCourse().getName() + " - " + getNumberOfEnrolledStudents() + " " +
			RenderUtils.getResourceString("DELEGATES_RESOURCES", "label.enrolledStudents");
    }
	
	public ExecutionYear getExecutionYear() {
		return (executionYear != null ? executionYear.getObject() : null);
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public CurricularCourse getCurricularCourse() {
		return (curricularCourse != null ? curricularCourse.getObject() : null);
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
		this.curricularCourse = new DomainReference<CurricularCourse>(curricularCourse);
	}
	
	private int getNumberOfEnrolledStudents() {
		if(this.enrolledStudents != null) {
			return this.enrolledStudents.size(); 
		}
		else {
			return 0;
		}
	}
	
	private void setEnrolledStudents() {
		this.enrolledStudents = new ArrayList<DomainReference<Person>>();
		for(Student student : getStudentsEnrolledIn(getCurricularCourse(), getExecutionYear())) {
			this.enrolledStudents.add(new DomainReference<Person>(student.getPerson()));
		}
	}
	
	private List<Student> getStudentsEnrolledIn(CurricularCourse curricularCourse, ExecutionYear executionYear) {
		List<Student> result = new ArrayList<Student>();
		for(Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(executionYear)) {
			Registration registration = enrolment.getRegistration();
			result.add(registration.getStudent());
		}
		return result;
	}

}

