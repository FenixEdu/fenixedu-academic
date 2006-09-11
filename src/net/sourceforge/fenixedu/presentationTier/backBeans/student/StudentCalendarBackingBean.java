package net.sourceforge.fenixedu.presentationTier.backBeans.student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.MessageResources;

public class StudentCalendarBackingBean extends FenixBackingBean {

    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    private static final MessageResources messages = MessageResources
	    .getMessageResources("resources/StudentResources");

    private static final ComparatorChain executionPeriodComparator = new ComparatorChain();
    static {
	executionPeriodComparator.addComparator(new BeanComparator("executionYear.year"), true);
	executionPeriodComparator.addComparator(new BeanComparator("semester"), true);
    }

    private static final Comparator<ExecutionCourse> executionCourseComparator = new BeanComparator(
	    "nome");

    private Collection<ExecutionPeriod> executionPeriods;

    private Collection<ExecutionCourse> executionCourses;

    private ExecutionPeriod executionPeriod;

    private Registration student;

    boolean setExecutionCourse = true;

    private String evaluationTypeClassname;

    public Collection<ExecutionPeriod> getExecutionPeriods() throws FenixFilterException,
	    FenixServiceException {
	if (executionPeriods == null) {
	    final Registration registration = getStudent();

	    executionPeriods = new TreeSet<ExecutionPeriod>(executionPeriodComparator);
	    for (final Attends attends : registration.getAssociatedAttends()) {
		executionPeriods.add(attends.getDisciplinaExecucao().getExecutionPeriod());
	    }
	}
	return executionPeriods;
    }

    public Collection<ExecutionCourse> getExecutionCourses() throws FenixFilterException,
	    FenixServiceException {
	final ExecutionPeriod executionPeriod = getExecutionPeriod();

	if (executionCourses == null
		|| (!executionCourses.isEmpty() && executionPeriod != executionCourses.iterator().next()
			.getExecutionPeriod())) {
	    final Registration registration = getStudent();

	    executionCourses = new TreeSet<ExecutionCourse>(executionCourseComparator);
	    for (final Attends attends : registration.getAssociatedAttends()) {
		final ExecutionCourse executionCourse = attends.getDisciplinaExecucao();
		if (executionCourse.getExecutionPeriod() == executionPeriod) {
		    executionCourses.add(executionCourse);
		}
	    }
	}
	return executionCourses;
    }

    public ExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
	final Integer executionPeriodID = getExecutionPeriodID();
	if (executionPeriod == null || !executionPeriodID.equals(executionPeriod.getIdInternal())) {
	    final Collection<ExecutionPeriod> executionPeriods = getExecutionPeriods();
	    if (executionPeriods != null) {
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
		    if (executionPeriod.getIdInternal().equals(executionPeriodID)) {
			this.executionPeriod = executionPeriod;
			break;
		    }
		}
	    }
	}
	return executionPeriod;
    }

    public Registration getStudent() {
	if (student == null) {
	    final List<Registration> students = getUserView().getPerson().getStudents();
	    for (final Registration registration : students) {
		if (registration.getDegreeType() == DegreeType.MASTER_DEGREE) {
		    this.student = registration;
		    return this.student;
		}
	    }
	    for (final Registration registration : students) {
		if (registration.getDegreeType() == DegreeType.DEGREE) {
		    this.student = registration;
		    return this.student;
		}
	    }
	}
	return student;
    }

    public Date getCalendarStartDate() throws FenixFilterException, FenixServiceException {
	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	final String evaluationTypeClassname = getEvaluationTypeClassname();
	final StudentCurricularPlan studentCurricularPlan = getStudent()
		.getActiveStudentCurricularPlan();
	final DegreeCurricularPlan degreeCurricularPlan = (studentCurricularPlan != null) ? studentCurricularPlan
		.getDegreeCurricularPlan()
		: null;
	final ExecutionDegree executionDegree = findExecutinDegree(degreeCurricularPlan, executionPeriod);
	if (evaluationTypeClassname == null || evaluationTypeClassname.length() == 0
		|| executionDegree == null) {
	    if (executionDegree != null && executionPeriod.getSemester().intValue() == 1
		    && executionDegree.getPeriodLessonsFirstSemester() != null) {
		return executionDegree.getPeriodLessonsFirstSemester().getStart();
	    } else if (executionDegree != null && executionPeriod.getSemester().intValue() == 2
		    && executionDegree.getPeriodLessonsSecondSemester() != null) {
		return executionDegree.getPeriodLessonsSecondSemester().getStart();
	    } else if (executionPeriod != null) {
		return executionPeriod.getBeginDate();
	    }
	} else {
	    if (evaluationTypeClassname.equals(Exam.class.getName())) {
		if (executionPeriod.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodExamsFirstSemester().getStart();
		} else if (executionPeriod.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodExamsSecondSemester().getStart();
		}
	    } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())) {
		if (executionPeriod.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodLessonsFirstSemester().getStart();
		} else if (executionPeriod.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodLessonsSecondSemester().getStart();
		}
	    } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())
		    || evaluationTypeClassname.equals(Project.class.getName())) {
		if (executionPeriod.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodLessonsFirstSemester().getStart();
		} else if (executionPeriod.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodLessonsSecondSemester().getStart();
		}
	    }
	}
	return null;
    }

    public Date getCalendarEndDate() throws FenixFilterException, FenixServiceException {
	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	final String evaluationTypeClassname = getEvaluationTypeClassname();
	final StudentCurricularPlan studentCurricularPlan = getStudent()
		.getActiveStudentCurricularPlan();
	final DegreeCurricularPlan degreeCurricularPlan = (studentCurricularPlan != null) ? studentCurricularPlan
		.getDegreeCurricularPlan()
		: null;
	final ExecutionDegree executionDegree = findExecutinDegree(degreeCurricularPlan, executionPeriod);
	if (evaluationTypeClassname == null || evaluationTypeClassname.length() == 0
		|| executionDegree == null) {
	    if (executionDegree != null && executionPeriod.getSemester().intValue() == 1
		    && executionDegree.getPeriodExamsFirstSemester() != null) {
		return executionDegree.getPeriodExamsFirstSemester().getEnd();
	    } else if (executionDegree != null && executionPeriod.getSemester().intValue() == 2
		    && executionDegree.getPeriodExamsSecondSemester() != null) {
		return executionDegree.getPeriodExamsSecondSemester().getEnd();
	    } else if (executionPeriod != null) {
		return executionPeriod.getEndDate();
	    }
	} else {
	    if (evaluationTypeClassname.equals(Exam.class.getName())) {
		if (executionPeriod.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodExamsFirstSemester().getEnd();
		} else if (executionPeriod.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodExamsSecondSemester().getEnd();
		}
	    } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())
		    || evaluationTypeClassname.equals(Project.class.getName())) {
		if (executionPeriod.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodLessonsFirstSemester().getEnd();
		} else if (executionPeriod.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodLessonsSecondSemester().getEnd();
		}
	    }
	}
	return null;
    }

    private ExecutionDegree findExecutinDegree(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	if (degreeCurricularPlan != null) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
		if (executionPeriod != null
			&& executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
		    return executionDegree;
		}
	    }
	}
	return null;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixFilterException, FenixServiceException {
	List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();

	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	final Registration registration = getStudent();

	for (final Attends attends : registration.getAssociatedAttends()) {
	    final ExecutionCourse executionCourse = attends.getDisciplinaExecucao();
	    if (executionCourse.getExecutionPeriod() == executionPeriod
		    && (getExecutionCourseID() == null || getExecutionCourseID().equals(
			    executionCourse.getIdInternal()))) {
		for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
		    if (evaluation instanceof WrittenEvaluation) {
			if (evaluation instanceof Exam) {
			    final Exam exam = (Exam) evaluation;
			    if (!exam.isExamsMapPublished()) {
				continue;
			    }
			}

			final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
			final String evaluationTypeClassname = getEvaluationTypeClassname();
			if (evaluationTypeClassname == null
				|| evaluationTypeClassname.length() == 0
				|| evaluationTypeClassname
					.equals(writtenEvaluation.getClass().getName())) {
			    CalendarLink calendarLink = new CalendarLink();
			    calendarLinks.add(calendarLink);

			    calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
			    calendarLink.setObjectLinkLabel(constructCalendarPresentation(
				    executionCourse, writtenEvaluation));
			    calendarLink.setLinkParameters(constructLinkParameters(executionCourse));
			}
		    } else if (evaluation instanceof Project) {
			final Project project = (Project) evaluation;
			final String evaluationTypeClassname = getEvaluationTypeClassname();
			if (evaluationTypeClassname == null || evaluationTypeClassname.length() == 0
				|| evaluationTypeClassname.equals(project.getClass().getName())) {
			    CalendarLink calendarLinkBegin = new CalendarLink();
			    calendarLinks.add(calendarLinkBegin);
			    calendarLinkBegin.setObjectOccurrence(project.getBegin());
			    calendarLinkBegin.setObjectLinkLabel(constructCalendarPresentation(
				    executionCourse, project, project.getBegin(), messages
					    .getMessage("label.evaluation.project.begin")));
			    calendarLinkBegin
				    .setLinkParameters(constructLinkParameters(executionCourse));

			    CalendarLink calendarLinkEnd = new CalendarLink();
			    calendarLinks.add(calendarLinkEnd);
			    calendarLinkEnd.setObjectOccurrence(project.getEnd());
			    calendarLinkEnd.setObjectLinkLabel(constructCalendarPresentation(
				    executionCourse, project, project.getEnd(), messages
					    .getMessage("label.evaluation.project.end")));
			    calendarLinkEnd.setLinkParameters(constructLinkParameters(executionCourse));
			}
		    }
		}
	    }
	}
	return calendarLinks;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixFilterException,
	    FenixServiceException {
	final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

	for (final ExecutionPeriod executionPeriod : getExecutionPeriods()) {
	    if (executionPeriod.getState() != PeriodState.NOT_OPEN) {
		final ExecutionYear executionYear = executionPeriod.getExecutionYear();
		executionPeriodSelectItems.add(new SelectItem(executionPeriod.getIdInternal(),
			executionPeriod.getName() + " " + executionYear.getYear()));
	    }
	}

	return executionPeriodSelectItems;
    }

    public List<SelectItem> getExecutionCourseSelectItems() throws FenixFilterException,
	    FenixServiceException {
	final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

	for (final ExecutionCourse executionCourse : getExecutionCourses()) {
	    executionPeriodSelectItems.add(new SelectItem(executionCourse.getIdInternal(),
		    executionCourse.getNome()));
	}

	return executionPeriodSelectItems;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse) {
	final Site site = executionCourse.getSite();

	final Map<String, String> linkParameters = new HashMap<String, String>();
	linkParameters.put("method", "evaluations");
	linkParameters.put("objectCode", (site != null) ? site.getIdInternal().toString() : null);
	linkParameters.put("executionPeriodOID", executionCourse.getExecutionPeriod().getIdInternal()
		.toString());
	linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
	return linkParameters;
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse,
	    final Project project, final Date time, final String tail) {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(messages.getMessage("label.evaluation.shortname.project"));
	stringBuilder.append(" ");
	stringBuilder.append(executionCourse.getSigla());
	stringBuilder.append(" (");
	stringBuilder.append(hourFormat.format(time));
	stringBuilder.append(") ");
	stringBuilder.append(tail);
	return stringBuilder.toString();
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse,
	    final WrittenEvaluation writtenEvaluation) {
	final StringBuilder stringBuilder = new StringBuilder();
	if (writtenEvaluation instanceof WrittenTest) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
	} else if (writtenEvaluation instanceof Exam) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
	}
	stringBuilder.append(" ");
	stringBuilder.append(executionCourse.getSigla());
	stringBuilder.append(" (");
	stringBuilder.append(hourFormat.format(writtenEvaluation.getBeginningDate()));
	stringBuilder.append(")");
	return stringBuilder.toString();
    }

    public String getApplicationContext() {
	final String appContext = PropertiesManager.getProperty("app.context");
	return (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
    }

    public Integer getExecutionPeriodID() throws FenixFilterException, FenixServiceException {
	if (getViewState().getAttribute("executionPeriodID") == null) {
	    final Collection<ExecutionPeriod> executionPeriods = getExecutionPeriods();
	    if (executionPeriods != null) {
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
		    if (executionPeriod.getState() == PeriodState.CURRENT) {
			setExecutionPeriodID(executionPeriod.getIdInternal());
			break;
		    }
		}
	    }
	}
	return (Integer) getViewState().getAttribute("executionPeriodID");
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
	getViewState().setAttribute("executionPeriodID", executionPeriodID);
    }

    public Integer getExecutionCourseID() {
	return (Integer) getViewState().getAttribute("executionCourseID");
    }

    public void setExecutionCourseID(Integer executionCourseID) {
	if (setExecutionCourse) {
	    getViewState().setAttribute("executionCourseID", executionCourseID);
	}
    }

    public String getEvaluationTypeClassname() {
	return evaluationTypeClassname;
    }

    public void setEvaluationTypeClassname(final String evaluationTypeClassname) {
	this.evaluationTypeClassname = evaluationTypeClassname;
    }

    public void resetExecutionCourses(ValueChangeEvent event) {
	getViewState().removeAttribute("executionCourseID");
	setExecutionCourse = false;
	this.executionCourses = null;
    }

    public void resetExecutionCourse(ValueChangeEvent event) {
	if (event.getNewValue() == null) {
	    getViewState().removeAttribute("executionCourseID");
	}
    }
}