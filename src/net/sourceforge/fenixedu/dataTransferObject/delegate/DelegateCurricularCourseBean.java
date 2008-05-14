package net.sourceforge.fenixedu.dataTransferObject.delegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class DelegateCurricularCourseBean implements Serializable {
	private DomainReference<CurricularCourse> curricularCourse;
	
	private DomainReference<ExecutionYear> executionYear;
	
	private DomainReference<ExecutionSemester> executionSemester;
	
	private List<DomainReference<Student>> enrolledStudents;
	
	private Integer curricularYear;
	
	static final public Comparator CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER = new ComparatorChain();
    static {
	((ComparatorChain) CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER)
		.addComparator(new BeanComparator("curricularYear"));
	((ComparatorChain) CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER)
		.addComparator(new BeanComparator("curricularSemester"));
    }

	public DelegateCurricularCourseBean(CurricularCourse curricularCourse, ExecutionYear executionYear,
			Integer curricularYear, ExecutionSemester executionSemester) {
		setCurricularCourse(curricularCourse);
		setExecutionYear(executionYear);
		setCurricularYear(curricularYear);
		setExecutionPeriod(executionSemester);
	}
	
	public CurricularCourse getCurricularCourse() {
		return (curricularCourse == null ? null : curricularCourse.getObject());
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
		this.curricularCourse = new DomainReference<CurricularCourse>(curricularCourse);
	}
	
	public ExecutionYear getExecutionYear() {
		return (executionYear == null ? null : executionYear.getObject());
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public ExecutionSemester getExecutionPeriod() {
		return (executionSemester == null ? null : executionSemester.getObject());
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionSemester = new DomainReference<ExecutionSemester>(executionSemester);
	}
	
	public List<Student> getEnrolledStudents() {
		List<Student> result = new ArrayList<Student>();
		for(DomainReference<Student> studentDR : this.enrolledStudents) {
			result.add(studentDR.getObject());
		}
		return result;
	}
	
	public void setEnrolledStudents (List<Student> students) {
		this.enrolledStudents = new ArrayList<DomainReference<Student>>();
		for(Student student : students) {
			this.enrolledStudents.add(new DomainReference<Student>(student));
		}
	}
	
	public void calculateEnrolledStudents() {
		List<Student> enrolledStudents = new ArrayList<Student>();
		for(Enrolment enrolment : getCurricularCourse().getEnrolmentsByExecutionPeriod(getExecutionPeriod())) {
			Registration registration = enrolment.getRegistration();
			
			if(registration.getCurricularYear(getExecutionYear()) == getCurricularYear()) {
				enrolledStudents.add(registration.getStudent());
			}
		}
		Collections.sort(enrolledStudents,Student.NUMBER_COMPARATOR);
		setEnrolledStudents(enrolledStudents);
	}

	public Integer getSemester() {
		return getCurricularSemester();
	}

	public Integer getCurricularSemester() {
		return getExecutionPeriod().getSemester();
	}

	public Integer getCurricularYear() {
		return curricularYear;
	}

	public void setCurricularYear(Integer curricularYear) {
		this.curricularYear = curricularYear;
	}
	
	public Integer getEnrolledStudentsNumber() {
		if (this.enrolledStudents != null) {
			return this.enrolledStudents.size();
		}
		return 0;
	}
	
}