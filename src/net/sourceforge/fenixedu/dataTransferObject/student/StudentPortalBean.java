package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
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
	    private String room;
	    private Boolean registered;

	    public EvaluationAnnouncement(WrittenTest writtenTest) {
		setEvaluationType(writtenTest.getEvaluationType().toString());
		setIdentification(writtenTest.getDescription());
		setRealization(writtenTest);
		setEnrolment(writtenTest);
		setRegistered(isStudentEnrolled(writtenTest));
		setRoom(writtenTest);
	    }

	    public EvaluationAnnouncement(Exam exam) {
		setEvaluationType(exam.getEvaluationType().toString());
		setIdentification(exam.getName());
		setRealization(exam);
		setEnrolment(exam);
		setRegistered(isStudentEnrolled(exam));
		setRoom(exam);
	    }

	    public EvaluationAnnouncement(Grouping grouping) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
		setEvaluationType(resource.getString("label.grouping"));
		setIdentification(grouping.getName());
		setRealization(grouping);
		setEnrolment(grouping);
		setRegistered(isStudentEnrolled(grouping));
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

	    private boolean isStudentEnrolled(Grouping grouping) {
		for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
		    for (Attends attends : studentGroup.getAttends()) {
			if (attends.getAluno().getStudent() == getStudent()) {
			    return true;
			}
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

	    public String getRoom() {
		return room;
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

	    public void setRealization(WrittenEvaluation writtenEvaluation) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		this.realization = resource.getString("message.out.realization.date") + " "
			+ YearMonthDay.fromDateFields(writtenEvaluation.getBeginningDateTime().toDate()).toString();
	    }

	    public void setRealization(Grouping grouping) {
		this.realization = "";
	    }

	    public void setEnrolment(WrittenEvaluation writtenEvaluation) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		if (writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay() != null
			&& writtenEvaluation.getEnrollmentEndDayDateYearMonthDay() != null) {
		    this.enrolment = resource.getString("message.out.enrolment.period.normal") + " "
			    + writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay().toString() + " "
			    + resource.getString("message.out.until") + " "
			    + writtenEvaluation.getEnrollmentEndDayDateYearMonthDay().toString();
		} else {
		    this.enrolment = " " + resource.getString("message.out.enrolment.period.default");
		}
	    }

	    public void setEnrolment(Grouping grouping) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		this.enrolment = resource.getString("message.out.enrolment.period.normal") + " "
			+ YearMonthDay.fromDateFields(grouping.getEnrolmentBeginDayDate()).toString() + " "
			+ resource.getString("message.out.until") + " "
			+ YearMonthDay.fromDateFields(grouping.getEnrolmentEndDayDate()).toString();
	    }

	    public void setRoom(WrittenEvaluation writtenEvaluation) {
		ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		if (writtenEvaluation.getAssociatedRooms().isEmpty() == false) {
		    this.room = resource.getString("message.out.room") + ": " + writtenEvaluation.getAssociatedRoomsAsString();
		} else {
		    this.room = resource.getString("message.out.without.room");
		}
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
		if (evaluation.getEvaluationType() == EvaluationType.TEST_TYPE) {
		    addEvaluationAnnouncement(new EvaluationAnnouncement((WrittenTest) evaluation));
		} else if (evaluation.getEvaluationType() == EvaluationType.EXAM_TYPE) {
		    addEvaluationAnnouncement(new EvaluationAnnouncement((Exam) evaluation));
		}
	    }
	    for (Grouping grouping : executionCourse.getGroupings()) {
		addEvaluationAnnouncement(new EvaluationAnnouncement(grouping));
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
