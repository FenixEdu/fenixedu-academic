package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class StudentPortalBean implements Serializable {

	public class ExecutionCoursesAnnouncements {

		public class EvaluationAnnouncement {
			private String evaluationType;
			private String identification;
			private String realization;
			private String register;
			private String enrolment;
			private String room;

			private boolean realizationPast;
			private boolean enrolmentElapsing;
			private boolean enrolmentPast;

			private boolean registered;
			private boolean groupEnrolment;

			public EvaluationAnnouncement(WrittenTest writtenTest) {
				setEvaluationType(writtenTest.getEvaluationType().toString());
				setIdentification(writtenTest.getDescription());
				setRegister(isStudentEnrolled(writtenTest));
				setRealization(writtenTest);
				setEnrolment(writtenTest);
				setRoom(writtenTest);
				setGroupEnrolment(false);
			}

			public EvaluationAnnouncement(Exam exam) {
				setEvaluationType(exam.getEvaluationType().toString());
				setIdentification(exam.getName());
				setRegister(isStudentEnrolled(exam));
				setRealization(exam);
				setEnrolment(exam);
				setRoom(exam);
				setGroupEnrolment(false);
			}

			public EvaluationAnnouncement(Grouping grouping) {
				ResourceBundle resource = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
				setEvaluationType(resource.getString("label.grouping"));
				setIdentification(grouping.getName());
				setRegister(isStudentEnrolled(grouping));
				setRealization(grouping);
				setEnrolment(grouping);
				setRoom("-");
				setGroupEnrolment(true);
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

			public String getRegister() {
				return register;
			}

			public boolean getRegistered() {
				return registered;
			}

			public boolean getRealizationPast() {
				return realizationPast;
			}

			public boolean getEnrolmentElapsing() {
				return enrolmentElapsing;
			}

			public boolean getEnrolmentPast() {
				return enrolmentPast;
			}

			public String getStatus() {
				/*
				 * <logic:equal name="evaluationAnnouncement"
				 * property="evaluationType" value="Agrupamento"> <logic:equal
				 * name="evaluationAnnouncement" property="registered"
				 * value="true"> <tr> </logic:equal> <logic:equal
				 * name="evaluationAnnouncement" property="registered"
				 * value="false"> <logic:equal name="evaluationAnnouncement"
				 * property="enrolmentPast" value="true"> <tr class="disabled">
				 * </logic:equal> <logic:equal name="evaluationAnnouncement"
				 * property="enrolmentPast" value="false"> <tr> </logic:equal>
				 * </logic:equal> </logic:equal> <logic:notEqual
				 * name="evaluationAnnouncement" property="evaluationType"
				 * value="Agrupamento"> <logic:equal
				 * name="evaluationAnnouncement" property="realizationPast"
				 * value="true"> <tr class="disabled"> </logic:equal>
				 * <logic:equal name="evaluationAnnouncement"
				 * property="realizationPast" value="false"> <logic:equal
				 * name="evaluationAnnouncement" property="registered"
				 * value="true"> <tr> </logic:equal> <logic:equal
				 * name="evaluationAnnouncement" property="registered"
				 * value="false"> <logic:equal name="evaluationAnnouncement"
				 * property="enrolmentElapsing" value="true"> <tr
				 * class="elapsing"> <bean:define id="evaluationElapsing"
				 * value="true" /> </logic:equal> <logic:equal
				 * name="evaluationAnnouncement" property="enrolmentElapsing"
				 * value="false"> <tr> </logic:equal> </logic:equal>
				 * </logic:equal> </logic:notEqual>
				 */

				if (getEvaluationType().equals("Agrupamento")) {
					if (getRegistered()) {

					} else {
						if (getEnrolmentPast()) {
							return "disabled";
						} else {

						}
					}
				} else {
					if (getRealizationPast()) {
						return "disabled";
					} else {
						if (getRegistered()) {

						} else {
							if (getEnrolmentElapsing()) {
								return "elapsing";
							} else {

							}
						}
					}
				}

				return "";
			}

			public void setEvaluationType(String evaluationType) {
				this.evaluationType = evaluationType;
			}

			public void setIdentification(String identification) {
				this.identification = identification;
			}

			public void setRealization(WrittenEvaluation writtenEvaluation) {
				this.realizationPast = writtenEvaluation.getBeginningDateTime().isBeforeNow();

				this.realization =
						YearMonthDay.fromDateFields(writtenEvaluation.getBeginningDateTime().toDate()).toString()
								+ " "
								+ writtenEvaluation.getBeginningDateTime().getHourOfDay()
								+ ":"
								+ (writtenEvaluation.getBeginningDateTime().getMinuteOfHour() == 0 ? "00" : writtenEvaluation
										.getBeginningDateTime().getMinuteOfHour());
			}

			public void setRealization(Grouping grouping) {
				this.realization = "-";
			}

			private DateTime toDateTime(Calendar date, Calendar time) {
				if (date == null || time == null) {
					return new DateTime((Calendar) null);
				}
				return new DateTime(date.getTimeInMillis()).withFields(new LocalTime(time.getTimeInMillis()));
			}

			public void setEnrolment(WrittenEvaluation writtenEvaluation) {
				final Calendar endDay = writtenEvaluation.getEnrollmentEndDay();

				this.enrolmentPast = new DateTime(endDay).isBeforeNow();

				final Calendar beginDay = writtenEvaluation.getEnrollmentBeginDay();

				final Calendar beginTime = writtenEvaluation.getEnrollmentBeginTime();
				final Calendar endTime = writtenEvaluation.getEnrollmentEndTime();

				final DateTime beginDateTime = toDateTime(beginDay, beginTime);
				final DateTime endDateTime = toDateTime(endDay, endTime);

				Interval interval = new Interval(beginDateTime, endDateTime);

				this.enrolmentElapsing = interval.containsNow();

				ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
				if (writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay() != null
						&& writtenEvaluation.getEnrollmentEndDayDateYearMonthDay() != null) {
					this.enrolment =
							writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay().toString() + " "
									+ resource.getString("message.out.until") + " "
									+ writtenEvaluation.getEnrollmentEndDayDateYearMonthDay().toString();
				} else {
					this.enrolment = "-";
					this.register = "-";
				}
			}

			public void setEnrolment(Grouping grouping) {
				this.enrolmentPast = new DateTime(grouping.getEnrolmentEndDay()).isBeforeNow();
				this.enrolmentElapsing =
						new DateTime(grouping.getEnrolmentBeginDay()).isBeforeNow()
								&& new DateTime(grouping.getEnrolmentEndDay()).isAfterNow();

				ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
				this.enrolment =
						YearMonthDay.fromDateFields(grouping.getEnrolmentBeginDayDate()).toString() + " "
								+ resource.getString("message.out.until") + " "
								+ YearMonthDay.fromDateFields(grouping.getEnrolmentEndDayDate()).toString();
			}

			public void setRoom(WrittenEvaluation writtenEvaluation) {
				ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
				for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
						.getWrittenEvaluationEnrolments()) {
					if (writtenEvaluationEnrolment.getStudent() != null
							&& writtenEvaluationEnrolment.getStudent().getStudent() == getStudent()) {
						if (writtenEvaluationEnrolment.getRoom() != null) {
							this.room = writtenEvaluationEnrolment.getRoom().getIdentification();
							return;
						} else {
							break;
						}
					}
				}
				if (writtenEvaluation.getAssociatedRooms().isEmpty() == false) {
					this.room = writtenEvaluation.getAssociatedRoomsAsString();
				} else {
					this.room = "-";
				}
			}

			public void setRoom(String room) {
				this.room = room;
			}

			public void setRegister(Boolean registered) {
				ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
				if (registered) {
					this.register = resource.getString("label.enroled");
					setRegistered(true);
				} else {
					this.register = resource.getString("message.out.not.enrolled");
					setRegistered(false);
				}
			}

			public void setRegistered(Boolean registered) {
				this.registered = registered;
			}

			public void setGroupEnrolment(boolean groupEnrolment) {
				this.groupEnrolment = groupEnrolment;
			}

			public boolean isGroupEnrolment() {
				return groupEnrolment;
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
			Collections.sort(evaluationAnnouncements, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					EvaluationAnnouncement e1 = (EvaluationAnnouncement) o1;
					EvaluationAnnouncement e2 = (EvaluationAnnouncement) o2;

					if (!e1.getStatus().equals("disabled") && e2.getStatus().equals("disabled")) {
						return -1;
					}

					return 1;
				}
			});

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

	public StudentPortalBean(final Degree degree, final Student student, final Set<ExecutionCourse> executionCourses,
			final DegreeCurricularPlan activeDegreeCurricularPlan) {
		super();
		setDegree(degree);
		setStudent(student);
		setExecutionCoursesAnnouncements(new ArrayList<ExecutionCoursesAnnouncements>());
		for (ExecutionCourse executionCourse : executionCourses) {
			addExecutionCoursesAnnouncement(new ExecutionCoursesAnnouncements(executionCourse));
		}
		Collections.sort(getExecutionCoursesAnnouncements(), new BeanComparator("executionCourse.name"));
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
