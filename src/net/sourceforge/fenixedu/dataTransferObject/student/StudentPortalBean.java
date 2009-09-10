package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class StudentPortalBean implements Serializable {

    public class ExecutionCoursesAnnouncements {

	public class EvaluationAnnouncement {
	    private String evaluationType;
	    private String identification;
	    private String realization;
	    private String enrolment;
	    private Boolean registered;

	    public EvaluationAnnouncement(WrittenEvaluation writtenEvaluation) {
		setEvaluationType(writtenEvaluation.getEvaluationType().toString());
		setIdentification(writtenEvaluation.getName());
		setRealization(writtenEvaluation.getBeginningDateTime().toDate());
		setEnrolment(writtenEvaluation);
		setRegistered(isStudentEnrolled(writtenEvaluation));
	    }

	    public EvaluationAnnouncement(Project project) {
		setEvaluationType(project.getEvaluationType().toString());
		setIdentification(project.getGrouping().getName());
		setRealization(project.getBegin(), project.getEnd());
		setEnrolment(project);
		setRegistered(isStudentEnrolled(project));
	    }

	    private boolean isStudentEnrolled(WrittenEvaluation writtenEvaluation) {
		for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
			.getWrittenEvaluationEnrolments()) {
		    if (writtenEvaluationEnrolment.getStudent() != null
			    && writtenEvaluationEnrolment.getStudent().getStudent() == getStudent()) {
			return true;
		    }
		}
		return false;
	    }

	    private boolean isStudentEnrolled(Project project) {
		for (final Attends attend : project.getGrouping().getAttendsSet()) {
		    if (attend.getEnrolment() != null && attend.getEnrolment().getStudent() == getStudent()) {
			return true;
		    }
		}
		return false;
	    }

	    public String getEvaluationType() {
		return evaluationType;
	    }

	    public String getIdentification() {
		return identification;
	    }

	    public String getRealization() {
		return realization;
	    }

	    public String getEnrolment() {
		return enrolment;
	    }

	    public Boolean getRegistered() {
		return registered;
	    }

	    public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	    }

	    public void setIdentification(String identification) {
		this.identification = identification;
	    }

	    public void setRealization(Date writtenEvaluationDate) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		this.realization = resource.getString("message.out.realization.date") + " "
			+ YearMonthDay.fromDateFields(writtenEvaluationDate).toString();
	    }

	    public void setRealization(Date projectBeginDate, Date projectEndDate) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		this.realization = resource.getString("message.out.realization.period") + " "
			+ YearMonthDay.fromDateFields(projectBeginDate).toString() + " "
			+ resource.getString("message.out.until") + " " + YearMonthDay.fromDateFields(projectEndDate).toString();
	    }

	    public void setEnrolment(WrittenEvaluation writtenEvaluation) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		if (writtenEvaluation.getWrittenEvaluationEnrolmentsCount() != 0) {
		    this.enrolment = resource.getString("message.out.enrolment.period.normal") + " "
			    + writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay().toString() + " "
			    + resource.getString("message.out.until") + " "
			    + writtenEvaluation.getEnrollmentEndDayDateYearMonthDay().toString();
		} else {
		    this.enrolment = " " + resource.getString("message.out.enrolment.period.default");
		}
	    }

	    public void setEnrolment(Project project) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		this.enrolment = resource.getString("message.out.enrolment.period.normal") + " "
			+ YearMonthDay.fromDateFields(project.getGrouping().getEnrolmentBeginDayDate()).toString() + " "
			+ resource.getString("message.out.until") + " "
			+ YearMonthDay.fromDateFields(project.getGrouping().getEnrolmentEndDayDate()).toString();
	    }

	    public void setRegistered(Boolean registered) {
		this.registered = registered;
	    }
	}

	private ExecutionCourse executionCourse;
	private List<EvaluationAnnouncement> evaluationAnnouncements;

	public ExecutionCoursesAnnouncements(ExecutionCourse executionCourse) {
	    setExecutionCourse(executionCourse);
	    setEvaluationAnnouncements(new ArrayList<EvaluationAnnouncement>());
	    for (Evaluation evaluation : executionCourse.getOrderedAssociatedEvaluations()) {
		EvaluationAnnouncement evaluationAnnouncement;
		if (evaluation.getEvaluationType() == EvaluationType.TEST_TYPE
			|| evaluation.getEvaluationType() == EvaluationType.EXAM_TYPE) {
		    evaluationAnnouncement = new EvaluationAnnouncement((WrittenEvaluation) evaluation);
		    addEvaluationAnnouncement(evaluationAnnouncement);
		} else if (evaluation.getEvaluationType() == EvaluationType.PROJECT_TYPE) {
		    evaluationAnnouncement = new EvaluationAnnouncement((Project) evaluation);
		    addEvaluationAnnouncement(evaluationAnnouncement);
		}
	    }
	}

	public ExecutionCourse getExecutionCourse() {
	    return executionCourse;
	}

	public List<EvaluationAnnouncement> getEvaluationAnnouncements() {
	    return evaluationAnnouncements;
	}

	public void setEvaluationAnnouncements(List<EvaluationAnnouncement> evaluationAnnouncements) {
	    this.evaluationAnnouncements = evaluationAnnouncements;
	}

	public void addEvaluationAnnouncement(EvaluationAnnouncement evaluationAnnouncement) {
	    getEvaluationAnnouncements().add(evaluationAnnouncement);
	}

	public void setExecutionCourse(ExecutionCourse executionCourse) {
	    this.executionCourse = executionCourse;
	}
    }

    private Degree degree;
    private Student student;
    private List<ExecutionCoursesAnnouncements> executionCoursesAnnouncements;

    public StudentPortalBean(final Degree degree, final Student student, final List<ExecutionCourse> executionCourses,
	    final DegreeCurricularPlan activeDegreeCurricularPlan) {
	super();
	setDegree(degree);
	setStudent(student);
	setExecutionCoursesAnnouncements(new ArrayList<ExecutionCoursesAnnouncements>());
	for (ExecutionCourse executionCourse : executionCourses) {
	    addExecutionCoursesAnnouncement(new ExecutionCoursesAnnouncements(executionCourse));
	}
    }

    public Degree getDegree() {
	return degree;
    }

    public Student getStudent() {
	return student;
    }

    public List<ExecutionCoursesAnnouncements> getExecutionCoursesAnnouncements() {
	return executionCoursesAnnouncements;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public void setStudent(Student student) {
	this.student = student;
    }

    public void setExecutionCoursesAnnouncements(List<ExecutionCoursesAnnouncements> executionCoursesAnnouncements) {
	this.executionCoursesAnnouncements = executionCoursesAnnouncements;
    }

    public void addExecutionCoursesAnnouncement(ExecutionCoursesAnnouncements executionCoursesAnnouncement) {
	getExecutionCoursesAnnouncements().add(executionCoursesAnnouncement);
    }
}
