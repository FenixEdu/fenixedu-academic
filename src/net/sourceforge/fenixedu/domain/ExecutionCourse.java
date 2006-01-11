package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExecutionCourse extends ExecutionCourse_Base implements INode {
	public String toString() {
		String result = "[EXECUTION_COURSE";
		result += ", codInt=" + getIdInternal();
		result += ", sigla=" + getSigla();
		result += ", nome=" + getNome();
		result += ", theoreticalHours=" + getTheoreticalHours();
		result += ", praticalHours=" + getPraticalHours();
		result += ", theoPratHours=" + getTheoPratHours();
		result += ", labHours=" + getLabHours();
		result += ", executionPeriod=" + getExecutionPeriod();
		result += "]";
		return result;
	}

	public String getSlideName() {
		String result = getParentNode().getSlideName() + "/EC"
				+ getIdInternal();
		return result;
	}

	public INode getParentNode() {
		ExecutionPeriod executionPeriod = getExecutionPeriod();
		return executionPeriod;
	}

	public List<Grouping> getGroupings() {
		List<Grouping> result = new ArrayList();
		for (final ExportGrouping exportGrouping : this.getExportGroupings()) {
			if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
					|| exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
				result.add(exportGrouping.getGrouping());
			}
		}
		return result;
	}

	public Grouping getGroupingByName(String groupingName) {
		for (final Grouping grouping : this.getGroupings()) {
			if (grouping.getName().equals(groupingName)) {
				return grouping;
			}
		}
		return null;
	}

	public boolean existsGroupingExecutionCourse(
			ExportGrouping groupPropertiesExecutionCourse) {
		return getExportGroupings().contains(groupPropertiesExecutionCourse);
	}

	public boolean existsGroupingExecutionCourse() {
		return getExportGroupings().isEmpty();
	}

	public boolean hasProposals() {
		boolean result = false;
		boolean found = false;
		List groupPropertiesExecutionCourseList = getExportGroupings();
		Iterator iter = groupPropertiesExecutionCourseList.iterator();
		while (iter.hasNext() && !found) {

			ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iter
					.next();
			if (groupPropertiesExecutionCourseAux.getProposalState().getState()
					.intValue() == 3) {
				result = true;
				found = true;
			}

		}
		return result;
	}

	public boolean isMasterDegreeOnly() {
		for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
			if (curricularCourse.getDegreeCurricularPlan().getDegree()
					.getTipoCurso() != DegreeType.MASTER_DEGREE) {
				return false;
			}
		}
		return true;
	}

	public void edit(String name, String acronym, double theoreticalHours,
			double theoreticalPraticalHours, double praticalHours,
			double laboratoryHours, String comment) {

		if (name == null || acronym == null || theoreticalHours < 0
				|| theoreticalPraticalHours < 0 || praticalHours < 0
				|| laboratoryHours < 0 || comment == null)
			throw new NullPointerException();

		setNome(name);
		setSigla(acronym);
		setTheoreticalHours(theoreticalHours);
		setTheoPratHours(theoreticalPraticalHours);
		setPraticalHours(praticalHours);
		setLabHours(laboratoryHours);
		setComment(comment);
	}

	public void createSite() {
		final Site site = new Site();
		site.setExecutionCourse(this);
	}

	public void createEvaluationMethod(final String evaluationElements,
			final String evaluationElementsEng) {
		if (evaluationElements == null || evaluationElementsEng == null)
			throw new NullPointerException();

		final EvaluationMethod evaluationMethod = new EvaluationMethod();
		evaluationMethod.setEvaluationElements(evaluationElements);
		evaluationMethod.setEvaluationElementsEn(evaluationElementsEng);
		evaluationMethod.setExecutionCourse(this);
	}

	public void createBibliographicReference(final String title,
			final String authors, final String reference, final String year,
			final Boolean optional) {
		if (title == null || authors == null || reference == null
				|| year == null || optional == null)
			throw new NullPointerException();

		final BibliographicReference bibliographicReference = new BibliographicReference();
		bibliographicReference.setTitle(title);
		bibliographicReference.setAuthors(authors);
		bibliographicReference.setReference(reference);
		bibliographicReference.setYear(year);
		bibliographicReference.setOptional(optional);
		bibliographicReference.setExecutionCourse(this);
	}

	public CourseReport createCourseReport(String report) {
		if (report == null)
			throw new NullPointerException();

		final CourseReport courseReport = new CourseReport();
		courseReport.setReport(report);
		courseReport.setLastModificationDate(Calendar.getInstance().getTime());
		courseReport.setExecutionCourse(this);

		return courseReport;
	}

	private Summary createSummary(String title, String summaryText,
			Integer studentsNumber, Boolean isExtraLesson) {

		if (title == null || summaryText == null || isExtraLesson == null)
			throw new NullPointerException();

		final Summary summary = new Summary();
		summary.setTitle(title);
		summary.setSummaryText(summaryText);
		summary.setStudentsNumber(studentsNumber);
		summary.setIsExtraLesson(isExtraLesson);
		summary.setLastModifiedDate(Calendar.getInstance().getTime());
		summary.setExecutionCourse(this);

		return summary;
	}

	public Summary createSummary(String title, String summaryText,
			Integer studentsNumber, Boolean isExtraLesson,
			Professorship professorship) {

		if (professorship == null)
			throw new NullPointerException();

		final Summary summary = createSummary(title, summaryText,
				studentsNumber, isExtraLesson);
		summary.setProfessorship(professorship);
		summary.setTeacher(null);
		summary.setTeacherName(null);

		return summary;
	}

	public Summary createSummary(String title, String summaryText,
			Integer studentsNumber, Boolean isExtraLesson, Teacher teacher) {

		if (teacher == null)
			throw new NullPointerException();

		final Summary summary = createSummary(title, summaryText,
				studentsNumber, isExtraLesson);
		summary.setTeacher(teacher);
		summary.setProfessorship(null);
		summary.setTeacherName(null);

		return summary;
	}

	public Summary createSummary(String title, String summaryText,
			Integer studentsNumber, Boolean isExtraLesson, String teacherName) {

		if (teacherName == null)
			throw new NullPointerException();

		final Summary summary = createSummary(title, summaryText,
				studentsNumber, isExtraLesson);
		summary.setTeacherName(teacherName);
		summary.setTeacher(null);
		summary.setProfessorship(null);

		return summary;
	}

	public List<Professorship> responsibleFors() {
		final List<Professorship> res = new ArrayList<Professorship>();
		for (final Professorship professorship : this.getProfessorships()) {
			if (professorship.getResponsibleFor())
				res.add(professorship);
		}
		return res;
	}

	public Attends getAttendsByStudent(final Student student) {

		return (Attends) CollectionUtils.find(getAttends(), new Predicate() {

			public boolean evaluate(Object o) {
				Attends attends = (Attends) o;
				return attends.getAluno().equals(student);
			}

		});
	}

	public List<Exam> getAssociatedExams() {
		List<Exam> associatedExams = new ArrayList<Exam>();

		for (Evaluation evaluation : this.getAssociatedEvaluations()) {
			if (evaluation instanceof Exam) {
				associatedExams.add((Exam) evaluation);
			}
		}

		return associatedExams;
	}

	public List<WrittenTest> getAssociatedWrittenTests() {
		List<WrittenTest> associatedWrittenTests = new ArrayList<WrittenTest>();

		for (Evaluation evaluation : this.getAssociatedEvaluations()) {
			if (evaluation instanceof WrittenTest) {
				associatedWrittenTests.add((WrittenTest) evaluation);
			}
		}

		return associatedWrittenTests;
	}

	public List<OnlineTest> getAssociatedOnlineTests() {
		List<OnlineTest> associatedOnlineTests = new ArrayList<OnlineTest>();

		for (Evaluation evaluation : this.getAssociatedEvaluations()) {
			if (evaluation instanceof OnlineTest) {
				associatedOnlineTests.add((OnlineTest) evaluation);
			}
		}

		return associatedOnlineTests;
	}

	// Delete Method

	public void delete() {
		if (canBeDeleted()) {
			setExecutionPeriod(null);

			if (getSite() != null) {
				getSite().delete();
			}

			for (; !getProfessorships().isEmpty(); getProfessorships().get(0)
					.delete())
				;

			getAssociatedCurricularCourses().clear();

			for (; !getExecutionCourseProperties().isEmpty(); getExecutionCourseProperties()
					.get(0).delete())
				;

			for (; !getAttends().isEmpty(); getAttends().get(0).delete())
				;

			getNonAffiliatedTeachers().clear();

			for (UserGroup userGroup : this.getHookedGroups()) {
				userGroup.delete();
			}

			super.deleteDomainObject();
		} else
			throw new DomainException("error.execution.course.cant.delete");
	}

	private boolean canBeDeleted() {
		if (hasAnyAssociatedSummaries()) {
			return false;
		}
		if (!getGroupings().isEmpty()) {
			return false;
		}
		if (hasAnyAssociatedBibliographicReferences()) {
			return false;
		}
		if (hasAnyAssociatedEvaluations()) {
			return false;
		}
		if (hasEvaluationMethod()) {
			return false;
		}
		if (hasAnyAssociatedShifts()) {
			return false;
		}
		if (hasCourseReport()) {
			return false;
		}
		final Site site = getSite();
		if (site != null) {
			if (site.hasAnyAssociatedAnnouncements()) {
				return false;
			}
			if (site.hasAnyAssociatedSections()) {
				return false;
			}
		}

		for (final Professorship professorship : getProfessorships()) {
			if (professorship.hasAnyAssociatedShiftProfessorship()) {
				return false;
			}
			if (professorship.hasAnyAssociatedSummaries()) {
				return false;
			}
			if (professorship.hasAnySupportLessons()) {
				return false;
			}
		}

		return true;
	}

	public boolean teacherLecturesExecutionCourse(Teacher teacher) {
		for (Professorship professorship : this.getProfessorships()) {
			if (teacher.getProfessorships().contains(professorship)) {
				return true;
			}
		}
		return false;
	}

	public List<net.sourceforge.fenixedu.domain.Project> getAssociatedProjects() {
		final List<net.sourceforge.fenixedu.domain.Project> result = new ArrayList<net.sourceforge.fenixedu.domain.Project>();

		for (Evaluation evaluation : this.getAssociatedEvaluations()) {
			if (evaluation instanceof net.sourceforge.fenixedu.domain.Project) {
				result
						.add((net.sourceforge.fenixedu.domain.Project) evaluation);
			}
		}
		return result;
	}

	private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber) {
		int executionCourseAssociatedStudents = 0;

		for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
			for (Enrolment enrolmentsEntry : curricularCourseFromExecutionCourseEntry
					.getEnrolments()) {
				if (enrolmentsEntry.getExecutionPeriod() == getExecutionPeriod()) {

					StudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry
							.getStudentCurricularPlan();
					int numberOfEnrolmentsForThatExecutionCourse = 0;

					for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry
							.getEnrolments()) {
						if (enrolmentsFromStudentCPEntry.getCurricularCourse() == curricularCourseFromExecutionCourseEntry
								&& (enrolmentsFromStudentCPEntry
										.getExecutionPeriod().compareTo(getExecutionPeriod()) <= 0)) {
							++numberOfEnrolmentsForThatExecutionCourse;
						}
					}

					if (numberOfEnrolmentsForThatExecutionCourse == enrolmentNumber) {
						executionCourseAssociatedStudents++;
					}
				}
			}
		}

		return executionCourseAssociatedStudents;
	}

	public Integer getTotalEnrolmentStudentNumber() {
		int executionCourseStudentNumber = 0;

		for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
			for (Enrolment enrolmentsEntry : curricularCourseFromExecutionCourseEntry
					.getEnrolments()) {
				if (enrolmentsEntry.getExecutionPeriod() == getExecutionPeriod()) {
					executionCourseStudentNumber++;
				}
			}
		}

		return executionCourseStudentNumber;
	}

	public Integer getFirstTimeEnrolmentStudentNumber() {

		return countAssociatedStudentsByEnrolmentNumber(1);
	}

	public Integer getSecondOrMoreTimeEnrolmentStudentNumber() {

		return getTotalEnrolmentStudentNumber()
				- getFirstTimeEnrolmentStudentNumber();
	}

	public Double getTotalHours(ShiftType shiftType) {
		double totalTime = 0;

		for (Shift shiftEntry : this.getAssociatedShifts()) {
			if (shiftEntry.getTipo() == shiftType) {
				totalTime += shiftEntry.hours();
			}
		}

		return totalTime;
	}

	public Double getStudentsNumberByShift(ShiftType shiftType) {
		int numShifts = 0;
		int executionCourseStudentNumber = getTotalEnrolmentStudentNumber();

		for (Shift shiftEntry : this.getAssociatedShifts()) {
			if (shiftEntry.getTipo() == shiftType) {
				numShifts++;
			}
		}

		if (numShifts == 0)
			return 0.0;
		else
			return (double) executionCourseStudentNumber / numShifts;
	}

	public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations() {
		List<EnrolmentEvaluation> results = new ArrayList<EnrolmentEvaluation>();

		for (CurricularCourse curricularCourse : this
				.getAssociatedCurricularCourses()) {
			List<EnrolmentEvaluation> evaluations = curricularCourse
					.getActiveEnrollmentEvaluations(this.getExecutionPeriod());

			results.addAll(evaluations);
		}

		return results;
	}

	private static final Comparator<Evaluation> EVALUATION_COMPARATOR = new Comparator<Evaluation>() {

		public int compare(Evaluation evaluation1, Evaluation evaluation2) {
			final String evaluation1ComparisonString = evaluationComparisonString(evaluation1);
			final String evaluation2ComparisonString = evaluationComparisonString(evaluation2);
			return evaluation1ComparisonString
					.compareTo(evaluation2ComparisonString);
		}

		private String evaluationComparisonString(final Evaluation evaluation) {
			final Date evaluationComparisonDate;
			final String evaluationTypeDistinguisher;

			if (evaluation instanceof OnlineTest) {
				evaluationTypeDistinguisher = "1";
				final OnlineTest onlineTest = (OnlineTest) evaluation;
				evaluationComparisonDate = onlineTest.getDistributedTest()
						.getBeginDateDate();
			} else if (evaluation instanceof Project) {
				evaluationTypeDistinguisher = "2";
				final Project project = (Project) evaluation;
				evaluationComparisonDate = project.getBegin();
			} else if (evaluation instanceof WrittenEvaluation) {
				evaluationTypeDistinguisher = "3";
				final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
				evaluationComparisonDate = writtenEvaluation.getDayDate();
			} else if (evaluation instanceof FinalEvaluation) {
				evaluationTypeDistinguisher = "4";
				final ExecutionCourse executionCourse = evaluation
						.getAssociatedExecutionCourses().get(0);
				evaluationComparisonDate = executionCourse.getExecutionPeriod()
						.getEndDate();
			} else {
				throw new DomainException("unknown.evaluation.type", evaluation
						.getClass().getName());
			}

			return DateFormatUtil.format(evaluationTypeDistinguisher
					+ "_yyyy/MM/dd", evaluationComparisonDate);
		}
	};

	public List<Evaluation> getOrderedAssociatedEvaluations() {
		final List<Evaluation> orderedEvaluations = new ArrayList<Evaluation>(
				getAssociatedEvaluations());
		Collections.sort(orderedEvaluations, EVALUATION_COMPARATOR);
		return orderedEvaluations;
	}

	private static final Comparator<Attends> ATTENDS_COMPARATOR = new BeanComparator(
			"aluno.number");

	public Set<Attends> getOrderedAttends() {
		final Set<Attends> orderedAttends = new TreeSet<Attends>(
				ATTENDS_COMPARATOR);
		orderedAttends.addAll(getAttends());
		return orderedAttends;
	}

}
