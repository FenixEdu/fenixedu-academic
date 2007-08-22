package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class CoordinatorEvaluationsBackingBean extends FenixBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/PublicDegreeInformation");

    private static final Locale locale = new Locale("pt", "PT");

    // These are context variables
    private Integer degreeCurricularPlanID = null;

    private Integer executionPeriodID = null;

    private Integer curricularYearID = null;

    // These are context variables for creating evaluations
    private String evaluationType = null;

    private Integer executionCourseID = null;

    // These variables hold the necessary information for creating/editing the
    // evaluations
    private String name = null;

    private String begin = null;

    private String end = null;

    private String description = null;

    private String date = null;

    private String beginTime = null;

    private String endTime = null;

    private Integer evaluationID = null;

    private Evaluation evalution;

    private Boolean onlineSubmissionsAllowed = null;

    private Integer maxSubmissionsToKeep = null;

    private Integer groupingID = null;

    private List<SelectItem> executionCourseGroupings = null;

    private Evaluation evaluation;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID")
                : degreeCurricularPlanID;
    }

    public Integer getExecutionPeriodID() throws FenixFilterException, FenixServiceException {
        if (executionPeriodID == null) {
            executionPeriodID = getAndHoldIntegerParameter("executionPeriodID");
            if (executionPeriodID == null) {
                final ExecutionPeriod executionPeriod = getCurrentExecutionPeriod();
                executionPeriodID = executionPeriod.getIdInternal();
            }
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(final Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public Integer getCurricularYearID() {
        return (curricularYearID == null) ? curricularYearID = getAndHoldIntegerParameter("curricularYearID")
                : curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        return rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }

    private ExecutionPeriod getCurrentExecutionPeriod() throws FenixFilterException,
            FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                if (executionPeriod.getState().equals(PeriodState.CURRENT)) {
                    return executionPeriod;
                }
            }
        }
        return null;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixFilterException,
            FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final TreeSet<ExecutionPeriod> executionPeriods = new TreeSet<ExecutionPeriod>();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                executionPeriods.add(executionPeriod);
            }
        }
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            final SelectItem selectItem = new SelectItem();
            selectItem.setLabel(StringAppender.append(executionPeriod.getName(), " - ", executionPeriod
                    .getExecutionYear().getYear()));
            selectItem.setValue(executionPeriod.getIdInternal());
            selectItems.add(selectItem);
        }
        return selectItems;
    }

    public List<SelectItem> getCurricularYearSelectItems() throws FenixFilterException,
            FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        for (int i = 1; i <= 5; i++) {
            selectItems.add(new SelectItem(Integer.valueOf(i), String.valueOf(i)));
        }
        return selectItems;
    }

    public ExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
        final Integer executionPeriodID = getExecutionPeriodID();
        return rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
    }

    private ExecutionDegree getExecutionDegree() throws FenixFilterException, FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
                return executionDegree;
            }
        }
        return null;
    }

    public Date getCalendarBegin() throws FenixFilterException, FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        if (executionDegree != null) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getStart();
            } else {
                return executionPeriod.getBeginDate();
            }
        } else {
            return null;
        }
    }

    public Date getCalendarEnd() throws FenixFilterException, FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        if (executionDegree != null) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodExamsSecondSemester().getEnd();
            } else {
                return executionPeriod.getEndDate();
            }
        } else {
            return null;
        }
    }

    private static final Comparator executionCourseComparator = new ComparatorChain();
    
    static {
        ((ComparatorChain) executionCourseComparator).addComparator(new BeanComparator("nome"));
        ((ComparatorChain) executionCourseComparator).addComparator(new BeanComparator("idInternal"));
    }

    private static final Comparator evaluationComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            if (o1.getClass() != o2.getClass()) {
                return o1.getClass().getName().compareTo(o2.getClass().getName());
            } else if (o1 instanceof WrittenTest) {
                final WrittenTest writtenTest1 = (WrittenTest) o1;
                final WrittenTest writtenTest2 = (WrittenTest) o2;
                return writtenTest1.getDayDate().compareTo(writtenTest2.getDayDate());
            } else if (o2 instanceof Project) {
                final Project project1 = (Project) o1;
                final Project project2 = (Project) o2;
                return project1.getBegin().compareTo(project2.getBegin());
            } else {
                return -1;
            }
        }
    };

    public List<ExecutionCourse> getExecutionCourses() throws FenixFilterException,
            FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        final CurricularYear curricularYear = getCurricularYear();

        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (isActiveInExecutionPeriodAndYear(curricularCourse, executionPeriod, curricularYear)) {
                for (final ExecutionCourse executionCourse : curricularCourse
                        .getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionPeriod) {
                        executionCourses.add(executionCourse);
                    }
                }
            }
        }
        return executionCourses;
    }

    public Map<ExecutionCourse, Set<Evaluation>> getExecutionCoursesMap() throws FenixFilterException,
            FenixServiceException {
        final Map<ExecutionCourse, Set<Evaluation>> executionCourseEvaluationsMap = new TreeMap<ExecutionCourse, Set<Evaluation>>(
                executionCourseComparator);
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        final CurricularYear curricularYear = getCurricularYear();

        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (isActiveInExecutionPeriodAndYear(curricularCourse, executionPeriod, curricularYear)) {
                for (final ExecutionCourse executionCourse : curricularCourse
                        .getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionPeriod) {
                        final Set<Evaluation> evaluations = new TreeSet<Evaluation>(evaluationComparator);
                        executionCourseEvaluationsMap.put(executionCourse, evaluations);
                        evaluations.addAll(executionCourse.getAssociatedEvaluations());
                    }
                }
            }
        }
        return executionCourseEvaluationsMap;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixFilterException, FenixServiceException {
        final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
        final String evaluationType = getEvaluationType();

        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluationType == null || evaluationType.length() == 0
                        || evaluationType.equals(evaluation.getClass().getName())) {
                    if (evaluation instanceof WrittenTest) {
                        final WrittenTest writtenTest = (WrittenTest) evaluation;
                        constructCalendarLink(calendarLinks, writtenTest, executionCourse);
                    } else if (evaluation instanceof Project) {
                        final Project project = (Project) evaluation;
                        constructCalendarLink(calendarLinks, project, executionCourse);
                    } else if (evaluation instanceof Exam) {
                        final Exam exam = (Exam) evaluation;
                        if (exam.isExamsMapPublished()) {
                            constructEmptyCalendarLink(calendarLinks, exam, executionCourse);
                        }
                    }
                }
            }
        }

        return calendarLinks;
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutEvaluations() throws FenixFilterException,
            FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            if (!hasNonExamEvaluation(executionCourse)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getExecutionCoursesWithEvaluations() throws FenixFilterException,
            FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            if (hasNonExamEvaluation(executionCourse)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    private boolean hasNonExamEvaluation(final ExecutionCourse executionCourse) {
        for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
            if (!(evaluation instanceof Exam)) {
                return true;
            }
        }
        return false;
    }

    private boolean isActiveInExecutionPeriodAndYear(final CurricularCourse curricularCourse,
            final ExecutionPeriod executionPeriod, final CurricularYear curricularYear) {
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
            if (curricularSemester.getSemester().equals(executionPeriod.getSemester())
                    && (curricularCourseScope.getBeginDate().getTime().getTime() <= executionPeriod
                            .getBeginDate().getTime())
                    && ((curricularCourseScope.getEndDate() == null) || (curricularCourseScope
                            .getEndDate().getTime().getTime() >= executionPeriod.getEndDate().getTime()))) {
                if (curricularYear == null || curricularYear == curricularSemester.getCurricularYear()) {
                    return true;
                }
            }
        }
        return false;
    }

    private CurricularYear getCurricularYear() {
        final Integer curricularYearID = getCurricularYearID();
        return (curricularYearID != null) ? rootDomainObject.readCurricularYearByOID(curricularYearID) : null;
    }

    private void constructEmptyCalendarLink(final List<CalendarLink> calendarLinks,
            final WrittenEvaluation writtenEvaluation, final ExecutionCourse executionCourse) {
        CalendarLink calendarLink = new CalendarLink(executionCourse, writtenEvaluation, locale);
        // addLinkParameters(calendarLink, executionCourse, writtenEvaluation);
        // addWrittenEvaluationLinkParameters(calendarLink, writtenEvaluation);
        calendarLink.setAsLink(false);
        calendarLinks.add(calendarLink);
    }

    private void constructCalendarLink(final List<CalendarLink> calendarLinks,
            final WrittenEvaluation writtenEvaluation, final ExecutionCourse executionCourse) {
        CalendarLink calendarLink = new CalendarLink(executionCourse, writtenEvaluation, locale);
        addLinkParameters(calendarLink, executionCourse, writtenEvaluation);
        addWrittenEvaluationLinkParameters(calendarLink, writtenEvaluation);
        calendarLinks.add(calendarLink);
    }

    private void constructCalendarLink(final List<CalendarLink> calendarLinks, final Project project,
            final ExecutionCourse executionCourse) {
        final CalendarLink calendarLinkBegin = new CalendarLink(executionCourse, project, project
                .getBegin(), messages.getMessage(locale, "label.evaluation.project.begin"), locale);
        addLinkParameters(calendarLinkBegin, executionCourse, project);
        addProjectLinkParameters(calendarLinkBegin, project);
        calendarLinks.add(calendarLinkBegin);

        final CalendarLink calendarLinkEnd = new CalendarLink(executionCourse, project,
                project.getEnd(), messages.getMessage(locale, "label.evaluation.project.end"), locale);
        addLinkParameters(calendarLinkEnd, executionCourse, project);
        addProjectLinkParameters(calendarLinkEnd, project);
        calendarLinks.add(calendarLinkEnd);
    }

    private void addLinkParameters(final CalendarLink calendarLink,
            final ExecutionCourse executionCourse, final Evaluation evaluation) {
        calendarLink.addLinkParameter("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
        calendarLink.addLinkParameter("executionPeriodID", executionCourse.getExecutionPeriod()
                .getIdInternal().toString());
        calendarLink.addLinkParameter("executionCourseID", executionCourse.getIdInternal().toString());
        calendarLink.addLinkParameter("curricularYearID",
                (getCurricularYearID() != null) ? getCurricularYearID().toString() : "");
        calendarLink.addLinkParameter("evaluationID", evaluation.getIdInternal().toString());
        calendarLink.addLinkParameter("evaluationType", evaluation.getClass().getName());
    }

    private void addWrittenEvaluationLinkParameters(CalendarLink calendarLink,
            WrittenEvaluation writtenEvaluation) {
        if (writtenEvaluation instanceof WrittenTest) {
            calendarLink.addLinkParameter("description", ((WrittenTest) writtenEvaluation)
                    .getDescription());
        }
        calendarLink.addLinkParameter("date", DateFormatUtil.format("dd/MM/yyyy", writtenEvaluation
                .getDayDate()));
        calendarLink.addLinkParameter("beginTime", DateFormatUtil.format("HH:mm", writtenEvaluation
                .getBeginningDate()));
        calendarLink.addLinkParameter("endTime", DateFormatUtil.format("HH:mm", writtenEvaluation
                .getEndDate()));
    }

    private void addProjectLinkParameters(CalendarLink calendarLinkBegin, Project project) {
        calendarLinkBegin.addLinkParameter("name", project.getName());
        calendarLinkBegin.addLinkParameter("begin", DateFormatUtil.format("dd/MM/yyyy HH:mm", project
                .getBegin()));
        calendarLinkBegin.addLinkParameter("end", DateFormatUtil.format("dd/MM/yyyy HH:mm", project
                .getEnd()));
        calendarLinkBegin.addLinkParameter("description", project.getDescription());
    }

    public String getEvaluationType() {
        return (evaluationType == null) ? evaluationType = getAndHoldStringParameter("evaluationType")
                : evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public List<SelectItem> getExecutionCourseSelectItems() throws FenixFilterException,
            FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        final List<ExecutionCourse> executionCourses = getExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            selectItems.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        return selectItems;
    }

    public Integer getExecutionCourseID() {
        return (executionCourseID == null) ? executionCourseID = getAndHoldIntegerParameter("executionCourseID")
                : executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseID) {
        this.executionCourseID = executionCourseID;
    }

    public String getDescription() {
        return (description == null) ? description = getAndHoldStringParameter("description")
                : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return (name == null) ? name = getAndHoldStringParameter("name") : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegin() {
        if (begin == null) {
            begin = getAndHoldStringParameter("begin");
            if (begin == null) {
                final String parameterString = getRequestParameter("selectedDate");
                if (parameterString != null && parameterString.length() > 0) {
                    begin = parameterString + " 00:00";
                    setRequestAttribute("begin", begin);
                }
            }
        }
        return (begin == null) ? begin = getAndHoldStringParameter("begin") : begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return (end == null) ? end = getAndHoldStringParameter("end") : end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String createProject() throws FenixFilterException, FenixServiceException {
        try {
            final Object[] args = { getExecutionCourseID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBegin()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEnd()), getDescription(),
                    getOnlineSubmissionsAllowed(), getMaxSubmissionsToKeep(), getGroupingID() };
            ServiceUtils.executeService(getUserView(), "CreateProject", args);
        } catch (final ParseException e) {
            setErrorMessage("error.invalidDate");
            return "viewCreationPage";
        } catch (final DomainException e) {
            setErrorMessage(e.getKey());
            return "viewCreationPage";
        }
        return "viewCalendar";
    }

    public String createWrittenTest() throws FenixFilterException, FenixServiceException {
        final ExecutionCourse executionCourse = getExecutionCourse();

        final List<String> executionCourseIDs = new ArrayList<String>(1);
        executionCourseIDs.add(this.getExecutionCourseID().toString());

		final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);
        final List<String> curricularCourseContextIDs = new ArrayList<String>();

		try {
			final Object[] args = { getExecutionCourseID(),
					DateFormatUtil.parse("dd/MM/yyyy", getDate()),
					DateFormatUtil.parse("HH:mm", getBeginTime()),
					DateFormatUtil.parse("HH:mm", getEndTime()),
					executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs,
                    null, null, getDescription() };
			ServiceUtils.executeService(getUserView(), "CreateWrittenEvaluation", args);
		} catch (ParseException ex) {
			setErrorMessage("error.invalid.date");
			return "viewCreationPage";
		}

        return "viewCalendar";
    }

    public String editWrittenTest() throws FenixFilterException, FenixServiceException, ParseException {
        final ExecutionCourse executionCourse = getExecutionCourse();

        final List<String> executionCourseIDs = new ArrayList<String>(1);
        executionCourseIDs.add(this.getExecutionCourseID().toString());

		final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);
        List<String> curricularCourseContextIDs = new ArrayList<String>();

		try {
			final Object[] args = { executionCourse.getIdInternal(),
					DateFormatUtil.parse("dd/MM/yyyy", getDate()),
					DateFormatUtil.parse("HH:mm", getBeginTime()),
					DateFormatUtil.parse("HH:mm", getEndTime()),
					executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs,
                    null, getEvaluationID(), null, getDescription() };
			ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);
		} catch (ParseException ex) {
			setErrorMessage("error.invalid.date");
			return "viewEditPage";
		} catch (NotAuthorizedFilterException ex) {
			setErrorMessage(ex.getMessage());
			return "viewEditPage";			
		}

        return "viewCalendar";
    }

    public String editProject() throws FenixFilterException, FenixServiceException, ParseException {
        try {
            final Object[] args = { getExecutionCourseID(), getEvaluationID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBegin()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEnd()), getDescription(),
                    getOnlineSubmissionsAllowed(), getMaxSubmissionsToKeep(), getGroupingID() };
            ServiceUtils.executeService(getUserView(), "EditProject", args);
        } catch (ParseException ex) {
            setErrorMessage("error.invalid.date");
            return "viewCreationPage";
        } catch (DomainException ex) {
            setErrorMessage(ex.getKey());
            return "viewCreationPage";
        }

        return "viewCalendar";
    }

    public ExecutionCourse getExecutionCourse() {
        return rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());
    }

    private List<String> getCurricularCourseScopeIDs(final ExecutionCourse executionCourse) {
        final List<String> curricularCourseScopeIDs = new ArrayList<String>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                        executionCourse.getExecutionPeriod().getSemester())) {
                    curricularCourseScopeIDs.add(curricularCourseScope.getIdInternal().toString());
                }
            }
        }
        return curricularCourseScopeIDs;
    }

    public String getBeginTime() {
        return (beginTime == null) ? beginTime = getAndHoldStringParameter("beginTime") : beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getDate() {
        if (date == null) {
            date = getAndHoldStringParameter("date");
            if (date == null) {
                final String parameterString = getRequestParameter("selectedDate");
                if (parameterString != null && parameterString.length() > 0) {
                    date = parameterString;
                    setRequestAttribute("date", date);
                }
            }
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return (endTime == null) ? endTime = getAndHoldStringParameter("endTime") : endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getEvaluationID() {
        return (evaluationID == null) ? evaluationID = getAndHoldIntegerParameter("evaluationID")
                : evaluationID;
    }

    public void setEvaluationID(Integer evaluationID) {
        this.evaluationID = evaluationID;
    }

    public String deleteEvaluation() throws FenixFilterException, FenixServiceException {
        final String evaluationType = getEvaluationType();
        final Object[] args = { getExecutionCourseID(), getEvaluationID() };
        if (evaluationType.equals(WrittenEvaluation.class.getName())) {
            try {
                ServiceUtils.executeService(getUserView(), "DeleteWrittenEvaluation", args);
            } catch (NotAuthorizedFilterException ex) {
                setErrorMessage(ex.getMessage());
                return "viewDeletePage";
            } catch (DomainException ex) {
                setErrorMessage(ex.getKey());
                return "viewDeletePage";
            }
        } else {
            try {
                ServiceUtils.executeService(getUserView(), "DeleteEvaluation", args);
            } catch (DomainException ex) {
                setErrorMessage(ex.getKey());
                return "viewDeletePage";
            }
        }
        return "viewCalendar";
    }

    public List<SelectItem> getExecutionCourseGroupings() throws FenixFilterException,
            FenixServiceException {
        if (this.executionCourseGroupings == null) {
            this.executionCourseGroupings = new ArrayList<SelectItem>();

            for (Grouping grouping : getExecutionCourse().getGroupings()) {
                this.executionCourseGroupings.add(new SelectItem(grouping.getIdInternal(), grouping
                        .getName()));
            }

        }

        return this.executionCourseGroupings;
    }

    private Evaluation getEvaluation() {
        if (this.evalution == null && getEvaluationID() != null) {
            this.evaluation = rootDomainObject.readEvaluationByOID(getEvaluationID());
        }

        return this.evaluation;
    }

    private Project getProject() {
        Evaluation evaluation = getEvaluation();
        if (evaluation != null && evaluation.getEvaluationType().equals(EvaluationType.PROJECT_TYPE)) {
            return (Project) evaluation;
        }

        return null;

    }

    public Integer getGroupingID() {
        if (this.groupingID == null && getProject() != null) {
            Grouping grouping = getProject().getGrouping();

            this.groupingID = (grouping != null) ? grouping.getIdInternal() : null;
        }
        return groupingID;
    }

    public void setGroupingID(Integer groupingID) {
        this.groupingID = groupingID;
    }

    public Grouping getGrouping() {
        if (getProject() != null) {
            return getProject().getGrouping();
        } else {
            return null;
        }
    }

    public Integer getMaxSubmissionsToKeep() {
        if (this.maxSubmissionsToKeep == null && getProject() != null) {
            this.maxSubmissionsToKeep = getProject().getMaxSubmissionsToKeep();
        }
        return maxSubmissionsToKeep;
    }

    public void setMaxSubmissionsToKeep(Integer maxSubmissionsToKeep) {
        this.maxSubmissionsToKeep = maxSubmissionsToKeep;
    }

    public Boolean getOnlineSubmissionsAllowed() {
        if (this.onlineSubmissionsAllowed == null && getProject() != null) {
            this.onlineSubmissionsAllowed = getProject().getOnlineSubmissionsAllowed();
        }
        return onlineSubmissionsAllowed;
    }

    public void setOnlineSubmissionsAllowed(Boolean onlineSubmissionsAllowed) {
        this.onlineSubmissionsAllowed = onlineSubmissionsAllowed;
    }

    public String returnToCalendar() {
        setEvaluationType("");

        return "viewCalendar";
    }

}
