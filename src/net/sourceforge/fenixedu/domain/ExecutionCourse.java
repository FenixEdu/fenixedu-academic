package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.executionCourse.SummariesSearchBean;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.Period;

import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class ExecutionCourse extends ExecutionCourse_Base {

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_NAME_COMPARATOR = new BeanComparator(
            "nome", Collator.getInstance());

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME = new ComparatorChain();

    static {
        CurricularCourseExecutionCourse.addListener(new CurricularCourseExecutionCourseListener());
        ((ComparatorChain) EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME)
                .addComparator(new BeanComparator("executionPeriod"));
        ((ComparatorChain) EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME)
                .addComparator(new BeanComparator("nome", Collator.getInstance()));

    }

    public ExecutionCourse() {
        super();
        init();
    }

    private void init() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public List<Grouping> getGroupings() {
        List<Grouping> result = new ArrayList<Grouping>();
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

    public boolean existsGroupingExecutionCourse(ExportGrouping groupPropertiesExecutionCourse) {
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

            ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iter.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }

        }
        return result;
    }

    public boolean isMasterDegreeOnly() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso() != DegreeType.MASTER_DEGREE) {
                return false;
            }
        }
        return true;
    }

    public void edit(String name, String acronym, double theoreticalHours,
            double theoreticalPraticalHours, double praticalHours, double laboratoryHours,
            double seminaryHours, double problemsHours, double fieldWorkHours,
            double trainingPeriodHours, double tutorialOrientationHours, String comment) {

        if (name == null || acronym == null || theoreticalHours < 0 || theoreticalPraticalHours < 0
                || praticalHours < 0 || laboratoryHours < 0 || seminaryHours < 0 || problemsHours < 0
                || fieldWorkHours < 0 || trainingPeriodHours < 0 || tutorialOrientationHours < 0
                || comment == null)
            throw new NullPointerException();

        setNome(name);
        setSigla(acronym);
        setTheoreticalHours(theoreticalHours);
        setTheoPratHours(theoreticalPraticalHours);
        setPraticalHours(praticalHours);
        setLabHours(laboratoryHours);
        setSeminaryHours(seminaryHours);
        setProblemsHours(problemsHours);
        setFieldWorkHours(fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours);
        setComment(comment);
    }

    public void createSite() {
        final Site site = new Site();
        site.setExecutionCourse(this);
    }

    public void createEvaluationMethod(final MultiLanguageString evaluationElements) {
        if (evaluationElements == null)
            throw new NullPointerException();

        final EvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setEvaluationElements(evaluationElements);
        evaluationMethod.setExecutionCourse(this);
    }

    public void createBibliographicReference(final String title, final String authors,
            final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null)
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

    private Summary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson) {

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

    public Summary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, Professorship professorship) {

        if (professorship == null)
            throw new NullPointerException();

        final Summary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setProfessorship(professorship);
        summary.setTeacher(null);
        summary.setTeacherName(null);

        return summary;
    }

    public Summary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, Teacher teacher) {

        if (teacher == null)
            throw new NullPointerException();

        final Summary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setTeacher(teacher);
        summary.setProfessorship(null);
        summary.setTeacherName(null);

        return summary;
    }

    public Summary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, String teacherName) {

        if (teacherName == null)
            throw new NullPointerException();

        final Summary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
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

    public Attends getAttendsByStudent(final Registration registration) {
        for (final Attends attends : getAttendsSet()) {
            if (attends.getAluno() == registration) {
                return attends;
            }
        }
        return null;
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
            if (hasSite()) {
                getSite().delete();
            }
            for (; !getProfessorships().isEmpty(); getProfessorships().get(0).delete())
                ;
            for (; !getExecutionCourseProperties().isEmpty(); getExecutionCourseProperties().get(0)
                    .delete())
                ;
            for (; !getAttends().isEmpty(); getAttends().get(0).delete())
                ;

            for (; !getForuns().isEmpty(); getForuns().get(0).delete())
                ;

            getAssociatedCurricularCourses().clear();
            getNonAffiliatedTeachers().clear();
            removeExecutionPeriod();
            removeRootDomainObject();
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.execution.course.cant.delete");
        }
    }

    public boolean canBeDeleted() {
        if (hasAnyAssociatedSummaries() || !getGroupings().isEmpty()
                || hasAnyAssociatedBibliographicReferences() || hasAnyAssociatedEvaluations()
                || hasEvaluationMethod() || hasAnyAssociatedShifts() || hasCourseReport()
                || hasAnyAttends() || (hasSite() && !getSite().canBeDeleted())) {
            return false;
        }
        for (final Professorship professorship : getProfessorships()) {
            if (!professorship.canBeDeleted()) {
                return false;
            }
        }

        for (ExecutionCourseForum forum : getForuns()) {
            if (forum.getConversationThreads().size() != 0) {
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
                result.add((net.sourceforge.fenixedu.domain.Project) evaluation);
            }
        }
        return result;
    }

    private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber) {
        int executionCourseAssociatedStudents = 0;
        ExecutionPeriod courseExecutionPeriod = getExecutionPeriod();

        for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
            for (CurriculumModule curriculumModule : curricularCourseFromExecutionCourseEntry
                    .getCurriculumModules()) {
                Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
                if (enrolmentsEntry.getExecutionPeriod() == courseExecutionPeriod) {

                    StudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry
                            .getStudentCurricularPlan();
                    int numberOfEnrolmentsForThatExecutionCourse = 0;

                    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolments()) {
                        if (enrolmentsFromStudentCPEntry.getCurricularCourse() == curricularCourseFromExecutionCourseEntry
                                && (enrolmentsFromStudentCPEntry.getExecutionPeriod().compareTo(
                                		courseExecutionPeriod) <= 0)) {
                            ++numberOfEnrolmentsForThatExecutionCourse;
                            if(numberOfEnrolmentsForThatExecutionCourse > enrolmentNumber){
                            	break;
                            }
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
        ExecutionPeriod courseExecutionPeriod = getExecutionPeriod();
        
        for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
            for (CurriculumModule curriculumModule : curricularCourseFromExecutionCourseEntry
                    .getCurriculumModules()) {
                Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
                if (enrolmentsEntry.getExecutionPeriod() == courseExecutionPeriod) {
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

        return getTotalEnrolmentStudentNumber() - getFirstTimeEnrolmentStudentNumber();
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
        int numShifts = getNumberOfShifts(shiftType);

        if (numShifts == 0)
            return 0.0;
        else
            return (double) getTotalEnrolmentStudentNumber() / numShifts;
    }

    public List<Enrolment> getActiveEnrollments() {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            List<Enrolment> enrollments = curricularCourse.getActiveEnrollments(this
                    .getExecutionPeriod());

            results.addAll(enrollments);
        }
        return results;
    }

    public boolean areAllOptionalCurricularCoursesWithLessTenEnrolments() {
        int enrolments = 0;
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            if (curricularCourse.getType() != null
                    && curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
                enrolments += curricularCourse.getEnrolmentsByExecutionPeriod(this.getExecutionPeriod())
                        .size();
                if (enrolments >= 10) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private static final Comparator<Evaluation> EVALUATION_COMPARATOR = new Comparator<Evaluation>() {

        public int compare(Evaluation evaluation1, Evaluation evaluation2) {
            final String evaluation1ComparisonString = evaluationComparisonString(evaluation1);
            final String evaluation2ComparisonString = evaluationComparisonString(evaluation2);
            return evaluation1ComparisonString.compareTo(evaluation2ComparisonString);
        }

        private String evaluationComparisonString(final Evaluation evaluation) {
            final Date evaluationComparisonDate;
            final String evaluationTypeDistinguisher;

            if (evaluation instanceof OnlineTest) {
                evaluationTypeDistinguisher = "1";
                final OnlineTest onlineTest = (OnlineTest) evaluation;
                evaluationComparisonDate = onlineTest.getDistributedTest().getBeginDateDate();
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
                final ExecutionCourse executionCourse = evaluation.getAssociatedExecutionCourses()
                        .get(0);
                evaluationComparisonDate = executionCourse.getExecutionPeriod().getEndDate();
            } else {
                throw new DomainException("unknown.evaluation.type", evaluation.getClass().getName());
            }

            return DateFormatUtil.format(evaluationTypeDistinguisher + "_yyyy/MM/dd",
                    evaluationComparisonDate);
        }
    };

    public List<Evaluation> getOrderedAssociatedEvaluations() {
        final List<Evaluation> orderedEvaluations = new ArrayList<Evaluation>(getAssociatedEvaluations());
        Collections.sort(orderedEvaluations, EVALUATION_COMPARATOR);
        return orderedEvaluations;
    }

    private static final Comparator<Attends> ATTENDS_COMPARATOR = new BeanComparator("aluno.number");

    public Set<Attends> getOrderedAttends() {
        final Set<Attends> orderedAttends = new TreeSet<Attends>(ATTENDS_COMPARATOR);
        orderedAttends.addAll(getAttends());
        return orderedAttends;
    }

    private static class CurricularCourseExecutionCourseListener extends
            dml.runtime.RelationAdapter<ExecutionCourse, CurricularCourse> {
        @Override
        public void afterAdd(ExecutionCourse execution, CurricularCourse curricular) {
            for (final CurriculumModule curriculumModule : curricular.getCurriculumModules()) {
                Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.getExecutionPeriod().equals(execution.getExecutionPeriod())) {
                    associateAttend(enrolment, execution);
                }
            }
        }

        @Override
        public void afterRemove(ExecutionCourse execution, CurricularCourse curricular) {
            if (execution != null) {
                for (Attends attends : execution.getAttends()) {
                    if ((attends.getEnrolment() != null)
                            && (attends.getEnrolment().getCurricularCourse().equals(curricular))) {
                        attends.setEnrolment(null);
                    }
                }
            }
        }

        private static void associateAttend(Enrolment enrolment, ExecutionCourse executionCourse) {
            if (!alreadyHasAttend(enrolment, executionCourse.getExecutionPeriod())) {
                Attends attends = executionCourse.getAttendsByStudent(enrolment
                        .getStudentCurricularPlan().getStudent());
                if (attends == null) {
                    attends = new Attends(enrolment.getStudentCurricularPlan().getStudent(),
                            executionCourse);
                }
                enrolment.addAttends(attends);
            }
        }

        private static boolean alreadyHasAttend(Enrolment enrolment, ExecutionPeriod executionPeriod) {
            for (Attends attends : enrolment.getAttends()) {
                if (attends.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
                    return true;
                }
            }
            return false;
        }
    }

    public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;

        final int numberOfWeeks;

        final Interval[] intervals;

        final int[] numberResponses;

        final int[] contactSum;

        final int[] autonomousStudySum;

        final int[] otherSum;

        final int[] totalSum;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks()
                    + extraWeek + 1;
            intervals = new Interval[numberOfWeeks];
            numberResponses = new int[numberOfWeeks];
            contactSum = new int[numberOfWeeks];
            autonomousStudySum = new int[numberOfWeeks];
            otherSum = new int[numberOfWeeks];
            totalSum = new int[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }
        }

        public void add(final Attends attends) {
            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
                final int weekIndex = weeklyWorkLoad.getWeekOffset();
                if (consistentAnswers(attends, weekIndex)) {
                    numberResponses[weekIndex]++;

                    final Integer contact = weeklyWorkLoad.getContact();
                    contactSum[weekIndex] += contact != null ? contact.intValue() : 0;

                    final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
                    autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy
                            .intValue() : 0;

                    final Integer other = weeklyWorkLoad.getOther();
                    otherSum[weekIndex] += other != null ? other.intValue() : 0;

                    totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex]
                            + otherSum[weekIndex];
                }
            }
        }

        private boolean consistentAnswers(final Attends attends, final int weekIndex) {
            int weeklyTotal = 0;
            for (final Attends someAttends : attends.getAluno().getAssociatedAttends()) {
                for (final WeeklyWorkLoad weeklyWorkLoad : someAttends.getWeeklyWorkLoads()) {
                    if (weeklyWorkLoad.getWeekOffset().intValue() == weekIndex) {
                        weeklyTotal += weeklyWorkLoad.getTotal();
                    }
                }
            }
            return weeklyTotal <= 140;
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getExecutionPeriodInterval() {
            return executionPeriodInterval;
        }

        public int[] getContactSum() {
            return contactSum;
        }

        public int[] getAutonomousStudySum() {
            return autonomousStudySum;
        }

        public int[] getOtherSum() {
            return otherSum;
        }

        public int[] getNumberResponses() {
            return numberResponses;
        }

        public double[] getContactAverage() {
            return average(getContactSum(), getNumberResponses());
        }

        public double[] getAutonomousStudyAverage() {
            return average(getAutonomousStudySum(), getNumberResponses());
        }

        public double[] getOtherAverage() {
            return average(getOtherSum(), getNumberResponses());
        }

        public double[] getTotalAverage() {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = Math
                        .round((0.0 + getContactSum()[i] + getAutonomousStudySum()[i] + getOtherSum()[i])
                                / getNumberResponses()[i]);
            }
            return valuesAverage;
        }

        private double[] average(final int[] values, final int[] divisor) {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = Math.round((0.0 + values[i]) / divisor[i]);
            }
            return valuesAverage;
        }

        private double add(final double[] values) {
            double total = 0;
            for (int i = 0; i < values.length; i++) {
                total += values[i];
            }
            return total;
        }

        public double getContactAverageTotal() {
            return add(getContactAverage());
        }

        public double getAutonomousStudyAverageTotal() {
            return add(getAutonomousStudyAverage());
        }

        public double getOtherAverageTotal() {
            return add(getOtherAverage());
        }

        public double getTotalAverageTotal() {
            return add(getTotalAverage());
        }

        public int getNumberResponsesTotal() {
            int total = 0;
            for (int i = 0; i < getNumberResponses().length; i++) {
                total += getNumberResponses()[i];
            }
            return total;
        }

        private int getNumberWeeksForAverageCalculation() {
            if (!getAttendsSet().isEmpty()) {
                final Attends attends = findAttendsWithEnrolment();
                if (attends != null) {
                    int currentWeekOffset = attends.calculateCurrentWeekOffset();
                    if (currentWeekOffset > 0 && currentWeekOffset < numberOfWeeks) {
                        return currentWeekOffset;
                    }
                }
            }
            return numberOfWeeks;
        }

        public double getContactAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getContactAverageTotal() / numberOfWeeks) : 0;
        }

        public double getAutonomousStudyAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getAutonomousStudyAverageTotal()
                    / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getOtherAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getOtherAverageTotal()
                    / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getTotalAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getTotalAverageTotal()
                    / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getNumberResponsesTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round((0.0 + getNumberResponsesTotal())
                    / getNumberWeeksForAverageCalculation()) : 0;
        }

    }

    public Interval getInterval() {
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        final DateTime beginningOfSemester = new DateTime(executionPeriod.getBeginDate());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(executionPeriod.getEndDate());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1)
                .plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

    public WeeklyWorkLoadView getWeeklyWorkLoadView() {
        final Attends attends = findAttendsWithEnrolment();
        if (attends != null) {
            final Interval interval = attends.getWeeklyWorkLoadInterval();
            final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(interval);
            for (final Attends attend : getAttends()) {
                weeklyWorkLoadView.add(attend);
            }
            return weeklyWorkLoadView;
        } else {
            return null;
        }
    }

    private Attends findAttendsWithEnrolment() {
        for (final Attends attends : getAttends()) {
            if (attends.getEnrolment() != null) {
                return attends;
            }
        }
        return null;
    }

    public boolean hasGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : getExportGroupings()) {
            if (grouping == exportGrouping.getGrouping()) {
                return true;
            }
        }
        return false;
    }

    public Shift findShiftByName(final String shiftName) {
        for (final Shift shift : getAssociatedShiftsSet()) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;
    }

    public Set<Shift> findShiftByType(final ShiftType shiftType) {
        final Set<Shift> shifts = new HashSet<Shift>();
        for (final Shift shift : getAssociatedShiftsSet()) {
            if (shift.getTipo() == shiftType) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public Set<SchoolClass> findSchoolClasses() {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final Shift shift : getAssociatedShifts()) {
            schoolClasses.addAll(shift.getAssociatedClasses());
        }
        return schoolClasses;
    }

    public List<Summary> readSummariesByShiftType(ShiftType summaryType) {
        List<Summary> summaries = new ArrayList<Summary>();
        for (Summary summary : this.getAssociatedSummaries()) {
            if (summary.getSummaryType().equals(summaryType)) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public List<Summary> readSummariesOfTeachersWithoutProfessorship() {

        List<Summary> summaries = new ArrayList<Summary>();
        for (Summary summary : this.getAssociatedSummaries()) {
            if (!summary.hasProfessorship()
                    && (summary.hasTeacher() || (summary.getTeacherName() != null && !summary
                            .getTeacherName().equals("")))) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public ExportGrouping getExportGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getGrouping() == grouping) {
                return exportGrouping;
            }
        }
        return null;
    }

    public boolean hasExportGrouping(final Grouping grouping) {
        return getExportGrouping(grouping) != null;
    }

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear,
            DegreeCurricularPlan degreeCurricularPlan) {
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear,
                    degreeCurricularPlan, getExecutionPeriod())) {
                return true;
            }
        }
        return false;
    }

    public Set<Metadata> findVisibleMetadata() {
        final Set<Metadata> visibleMetadata = new HashSet<Metadata>();
        for (final Metadata metadata : getMetadatasSet()) {
            if (metadata.getVisibility() != null && metadata.getVisibility().booleanValue()) {
                visibleMetadata.add(metadata);
            }
        }
        return visibleMetadata;
    }

    public void createForum(String name, String description) {

        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }

        ExecutionCourseForum executionCourseForum = new ExecutionCourseForum(name, description);
        this.addForuns(executionCourseForum);
    }

    public boolean hasForumWithName(String name) {
        ExecutionCourseForum executionCourseForum = getForumByName(name);

        return (executionCourseForum == null) ? false : true;
    }

    public ExecutionCourseForum getForumByName(String name) {
        for (ExecutionCourseForum executionCourseForum : getForuns()) {
            if (executionCourseForum.getName().equalsIgnoreCase(name)) {
                return executionCourseForum;
            }
        }

        return null;
    }

    @Override
    public void addForuns(ExecutionCourseForum forum) {
        checkIfCanAddForum(forum.getName());
        super.addForuns(forum);
    }

    public void checkIfCanAddForum(String name) {
        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
    }

    public SortedSet<Degree> getDegreesSortedByDegreeName() {
        final SortedSet<Degree> degrees = new TreeSet<Degree>(
                Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            degrees.add(degreeCurricularPlan.getDegree());
        }
        return degrees;
    }

    public SortedSet<CurricularCourse> getCurricularCoursesSortedByDegreeAndCurricularCourseName() {
        final SortedSet<CurricularCourse> curricularCourses = new TreeSet<CurricularCourse>(
                CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
        curricularCourses.addAll(getAssociatedCurricularCoursesSet());
        return curricularCourses;
    }

    public Set<CompetenceCourse> getCompetenceCourses() {
        final Set<CompetenceCourse> competenceCourses = new HashSet<CompetenceCourse>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                competenceCourses.add(competenceCourse);
            }
        }
        return competenceCourses;
    }

    public Set<CompetenceCourseInformation> getCompetenceCoursesInformations() {
        final Set<CompetenceCourseInformation> competenceCourseInformations = new HashSet<CompetenceCourseInformation>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation = competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                    competenceCourseInformations.add(competenceCourseInformation);
                }
            }
        }
        return competenceCourseInformations;
    }

    public List<CurricularCourse> getCurricularCoursesWithDegreeType() {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            Degree degree = curricularCourse.getDegree();
            if (degree.getTipoCurso() == DegreeType.DEGREE
                    || degree.getTipoCurso() == DegreeType.BOLONHA_DEGREE) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public boolean hasAnyDegreeGradeToSubmit(ExecutionPeriod period,
            DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : this.getCurricularCoursesWithDegreeType()) {
            if (degreeCurricularPlan == null
                    || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeGradeToSubmit(period)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionPeriod period,
            DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : this.getCurricularCoursesWithDegreeType()) {
            if (degreeCurricularPlan == null
                    || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeMarkSheetToConfirm(period)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String nextShiftName(final ShiftType shiftType) {
        int i = 1;
        for (final Shift otherShift : getAssociatedShiftsSet()) {
            if (otherShift.getTipo() == shiftType) {
                i++;
            }
        }
        return constructShiftName(shiftType, i);
    }

    public String constructShiftName(final ShiftType shiftType, final int n) {
        final String number = n < 10 ? "0" + n : Integer.toString(n);
        return StringAppender.append(getSigla(), shiftType.getSiglaTipoAula(), number);
    }

    public SortedSet<Shift> getShiftsByTypeOrderedByShiftName(final ShiftType shiftType) {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_NAME);
        for (final Shift shift : getAssociatedShiftsSet()) {
            shifts.add(shift);
        }
        return shifts;
    }

    public void setShiftNames() {
        final SortedSet<Shift> shifts = CollectionUtils.constructSortedSet(getAssociatedShiftsSet(),
                Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        final int[] counters = new int[ShiftType.values().length];
        for (final Shift shift : shifts) {
            final ShiftType shiftType = shift.getTipo();
            final String name = constructShiftName(shiftType, ++counters[shiftType.ordinal()]);
            shift.setNome(name);
        }
    }

    public boolean hasProjectsWithOnlineSubmission() {
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed() == true) {
                return true;
            }
        }

        return false;
    }

    public List<Project> getProjectsWithOnlineSubmission() {
        List<Project> result = new ArrayList<Project>();
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed() == true) {
                result.add(project);
            }
        }

        return result;
    }

	private Set<SchoolClass> getAllSchoolClassesOrBy(DegreeCurricularPlan degreeCurricularPlan) {
		final Set<SchoolClass> result = new HashSet<SchoolClass>();
		for (final Shift shift : getAssociatedShiftsSet()) {
			for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
				if (degreeCurricularPlan == null || schoolClass.getExecutionDegree().getDegreeCurricularPlan() == degreeCurricularPlan) {
					result.add(schoolClass);
				}
			}
		}
		return result;
	}

	public Set<SchoolClass> getSchoolClassesBy(DegreeCurricularPlan degreeCurricularPlan) {
		return getAllSchoolClassesOrBy(degreeCurricularPlan);
	}

	public Set<SchoolClass> getSchoolClasses() {
		return getAllSchoolClassesOrBy(null);
	}

    public boolean isLecturedIn(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear;
    }

    public SortedSet<Professorship> getProfessorshipsSortedAlphabetically() {
        final SortedSet<Professorship> professorhips = new TreeSet<Professorship>(Professorship.COMPARATOR_BY_PERSON_NAME);
        professorhips.addAll(getProfessorshipsSet());
        return professorhips;
    }

    public SummariesSearchBean getSummariesSearchBean() {
        return new SummariesSearchBean(this);
    }

    public Set<Lesson> getLessons() {
    	final Set<Lesson> lessons = new HashSet<Lesson>();
    	for (final Shift shift : getAssociatedShiftsSet()) {
    		lessons.addAll(shift.getAssociatedLessonsSet());
    	}
    	return lessons;
    }

    public SortedSet<WrittenEvaluation> getWrittenEvaluations() {
        final SortedSet<WrittenEvaluation> writtenEvaluations = new TreeSet<WrittenEvaluation>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
        for (final Evaluation evaluation : getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenEvaluation) {
                writtenEvaluations.add((WrittenEvaluation) evaluation);
            }
        }
        return writtenEvaluations;
    }

    public SortedSet<Shift> getShiftsOrderedByLessons() {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        shifts.addAll(getAssociatedShiftsSet());
        return shifts;
    }

    public Map<CompetenceCourse, Set<CurricularCourse>> getCurricularCoursesIndexedByCompetenceCourse() {
    	final Map<CompetenceCourse, Set<CurricularCourse>> curricularCourseMap = new HashMap<CompetenceCourse, Set<CurricularCourse>>();
    	for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
    		if (curricularCourse.isBolonha()) {
    			final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
    			final Set<CurricularCourse> curricularCourses;
    			if (curricularCourseMap.containsKey(competenceCourse)) {
    				curricularCourses = curricularCourseMap.get(competenceCourse);
    			} else {
    				curricularCourses = new TreeSet<CurricularCourse>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
    				curricularCourseMap.put(competenceCourse, curricularCourses);
    			}
    			curricularCourses.add(curricularCourse);
    		}
    	}
    	return curricularCourseMap;
    }
    
    public boolean getHasAnySecondaryBibliographicReference() {
    	for (final BibliographicReference bibliographicReference : getAssociatedBibliographicReferencesSet()) {
    		if (bibliographicReference.getOptional().booleanValue()) {
    			return true;
    		}
    	}
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation = competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                	final net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences bibliographicReferences = competenceCourseInformation.getBibliographicReferences();
                	if (bibliographicReferences != null) {
                		for (final net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference bibliographicReference : bibliographicReferences.getBibliographicReferencesList()) {
                			if (bibliographicReference.getType() == BibliographicReferenceType.SECONDARY) {
                				return true;
                			}
                		}
                	}
                }
            }
        }
        return false;
    }
    
    public List<LessonPlanning> getLessonPlanningsOrderedByOrder(ShiftType lessonType){
        final List<LessonPlanning> lessonPlannings = new ArrayList<LessonPlanning>();
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if(planning.getLessonType().equals(lessonType)) {
                lessonPlannings.add(planning);
            }
        }
        Collections.sort(lessonPlannings, LessonPlanning.COMPARATOR_BY_ORDER);
        return lessonPlannings;
    }
     
    public LessonPlanning getLessonPlanning(ShiftType lessonType, Integer order){        
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if(planning.getLessonType().equals(lessonType) && planning.getOrderOfPlanning().equals(order)) {
                return planning;              
            }
        }        
        return null;
    }
    
    public Set<ShiftType> getShiftTypes(){
        Set<ShiftType> shiftTypes = new TreeSet<ShiftType>();
        for (Shift shift : getAssociatedShiftsSet()) {
            shiftTypes.add(shift.getTipo());
        }
        return shiftTypes;
    }

    public Integer getNumberOfShifts(ShiftType shiftType) {
    	int numShifts = 0;

    	for (Shift shiftEntry : this.getAssociatedShifts()) {
    	    if (shiftEntry.getTipo() == shiftType) {
    	    	numShifts++;
    	    }
    	}
    	
    	return numShifts;
    }    
    
    public Double getCurricularCourseEnrolmentsWeight(CurricularCourse curricularCourse) {
    	Double totalEnrolmentStudentNumber = new Double(getTotalEnrolmentStudentNumber());
    	if(totalEnrolmentStudentNumber > 0d) {
    		return  curricularCourse.getTotalEnrolmentStudentNumber(getExecutionPeriod()) / totalEnrolmentStudentNumber;
    	} else {
    		return 0d;
    	}
    }
    
}