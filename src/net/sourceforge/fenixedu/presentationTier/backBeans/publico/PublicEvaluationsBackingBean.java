package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.util.MessageResources;

public class PublicEvaluationsBackingBean extends FenixBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources("resources/PublicDegreeInformation");

    private static final DateFormat yearFormat = new SimpleDateFormat("yyyy");

    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    private Integer degreeID;

    private Integer degreeCurricularPlanID;

    private Integer executionPeriodID;

    private Integer curricularYearID;

    private Degree degree;

    public Integer getDegreeID() {
	FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(getRequest());
	final DegreeSite site = (DegreeSite) context.getSelectedContainer();
	if (site != null) {
	    final Degree degree = site.getDegree();
	    setRequestAttribute("degreeID", degree.getIdInternal());
	    degreeID = degree.getIdInternal();
	} else if (degreeID == null) {
	    degreeID = getAndHoldIntegerParameter("degreeID");
	}
	return degreeID;
    }

    public Integer getDegreeCurricularPlanID() {
	if (degreeCurricularPlanID == null) {
	    degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID");
	    if (degreeCurricularPlanID == null) {
		degreeCurricularPlanID = getMostRecentDegreeCurricularPlan().getIdInternal();
	    }
	}
	return degreeCurricularPlanID;
    }

    public Integer getExecutionPeriodID() {
	if (executionPeriodID == null || !contains(getExecutionPeriodSelectItems(), executionPeriodID)) {
	    executionPeriodID = getAndHoldIntegerParameter("executionPeriodID");
	    if (executionPeriodID == null) {
		ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionPeriod();
		ExecutionDegree currentExecutionDegree = getDegreeCurricularPlan().getExecutionDegreeByYear(currentExecutionPeriod.getExecutionYear());

		executionPeriodID = (currentExecutionDegree != null) ? currentExecutionPeriod.getIdInternal() : getMostRecentExecutionPeriod().getIdInternal(); 
	    }
	}
	return executionPeriodID;
    }

    public Integer getCurricularYearID() {
	return (curricularYearID == null) ? curricularYearID = getAndHoldIntegerParameter("curricularYearID") : curricularYearID;
    }

    public Degree getDegree() {
	if (degree == null) {
	    degree = rootDomainObject.readDegreeByOID(getDegreeID());
	}
	return degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	final Degree degree = getDegree();
	final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
	if (degree != null && degreeCurricularPlanID != null) {
	    for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
		if (degreeCurricularPlanID.equals(degreeCurricularPlan.getIdInternal())) {
		    return degreeCurricularPlan;
		}
	    }
	}
	return null;
    }

    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
	return getDegree().getMostRecentDegreeCurricularPlan();
    }

    public ExecutionSemester getExecutionPeriod() {
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	final Integer executionPeriodID = getExecutionPeriodID();
	if (degreeCurricularPlan != null && executionPeriodID != null) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
		final ExecutionYear executionYear = executionDegree.getExecutionYear();
		for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
		    if (executionSemester.getIdInternal().equals(executionPeriodID)) {
			return executionSemester;
		    }
		}
	    }
	}
	return null;        
    }

    private boolean contains(final List<SelectItem> executionPeriodSelectItems, final Integer integer) {
	for (final SelectItem selectItem : executionPeriodSelectItems) {
	    if (selectItem.getValue().equals(integer)) {
		return true;
	    }
	}
	return false;
    }

    public CurricularYear getCurricularYear() {
	final Integer curricularYearID = getCurricularYearID();
	if (curricularYearID != null) {
	    return rootDomainObject.readCurricularYearByOID(curricularYearID);
	} else {
	    return null;
	}
    }

    public ExecutionSemester getMostRecentExecutionPeriod() {
	ExecutionSemester mostRecentExecutionPeriod = null;

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	if (degreeCurricularPlan != null) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
		final ExecutionYear executionYear = executionDegree.getExecutionYear();
		for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
		    if (executionSemester.getState() != PeriodState.CLOSED) {
			if (mostRecentExecutionPeriod == null) {
			    mostRecentExecutionPeriod = executionSemester;
			} else {
			    final ExecutionYear mostRecentExecutionYear = mostRecentExecutionPeriod.getExecutionYear();
			    if (executionYear.getYear().compareTo(mostRecentExecutionYear.getYear()) > 0
				    || (executionYear == mostRecentExecutionYear && executionSemester.getSemester().compareTo(mostRecentExecutionPeriod.getSemester()) > 0)) {
				mostRecentExecutionPeriod = executionSemester;
			    }
			}
		    }
		}
	    }
	}
	return mostRecentExecutionPeriod;
    }

    public String getDegreeName() {
	final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	return locale.getLanguage().equalsIgnoreCase("en") ? getDegree().getNameEn() : getDegree().getNome();
    }

    public List<SelectItem> getDegreeCurricularPlanSelectItems() {
	final List<SelectItem> degreeCurricularPlanSelectItems = new ArrayList<SelectItem>();

	final Degree degree = getDegree();
	if (degree != null) {
	    for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
		degreeCurricularPlanSelectItems.add(new SelectItem(degreeCurricularPlan.getIdInternal(), degreeCurricularPlan.getName()));
	    }
	}

	return degreeCurricularPlanSelectItems;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() {
	final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
	    final ExecutionYear executionYear = executionDegree.getExecutionYear();
	    for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
		if (executionSemester.getState() != PeriodState.CLOSED) {
		    executionPeriodSelectItems.add(new SelectItem(executionSemester.getIdInternal(), executionSemester.getName() + " " + executionYear.getYear()));
		}
	    }
	}

	return executionPeriodSelectItems;
    }

    public List<SelectItem> getCurricularYearSelectItems() {
	final List<SelectItem> curricularYearSelectItems = new ArrayList<SelectItem>();

	for (Integer curricularYear : getDegree().buildFullCurricularYearList()) {
	    curricularYearSelectItems.add(new SelectItem(curricularYear, String.valueOf(curricularYear)));
	}

	return curricularYearSelectItems;
    }

    public List<CalendarLink> getCalendarLinks() {
	List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	final CurricularYear curricularYear = getCurricularYear();
	final ExecutionSemester executionSemester = getExecutionPeriod();
	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
	    if (curricularYear == null || curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan, executionSemester)) {                
		for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
		    if (executionCourse.getExecutionPeriod() == executionSemester) {
			for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {

			    if (evaluation instanceof WrittenEvaluation) {
				if (!(evaluation instanceof Exam) || ((Exam) evaluation).isExamsMapPublished()) {
				    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
				    CalendarLink calendarLink = new CalendarLink();
				    calendarLinks.add(calendarLink);

				    calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
				    calendarLink.setObjectLinkLabel(constructCalendarPresentation(executionCourse, writtenEvaluation));
				    calendarLink.setLinkParameters(constructLinkParameters(executionCourse));
				}

			    } else if (evaluation instanceof Project) {
				final Project project = (Project) evaluation;
				
				CalendarLink calendarLinkBegin = new CalendarLink();
				calendarLinks.add(calendarLinkBegin);
				calendarLinkBegin.setObjectOccurrence(project.getBegin());
				calendarLinkBegin.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getBegin(), messages.getMessage("label.evaluation.project.begin")));
				calendarLinkBegin.setLinkParameters(constructLinkParameters(executionCourse));

				CalendarLink calendarLinkEnd = new CalendarLink();
				calendarLinks.add(calendarLinkEnd);
				calendarLinkEnd.setObjectOccurrence(project.getEnd());
				calendarLinkEnd.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getEnd(), messages.getMessage("label.evaluation.project.end")));
				calendarLinkEnd.setLinkParameters(constructLinkParameters(executionCourse));
			    }                            
			}
		    }
		}
	    }
	}

	return calendarLinks;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse) {
	final Map<String, String> linkParameters = new HashMap<String, String>();
	linkParameters.put("method", "evaluations");
	linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());	
	linkParameters.put(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME, executionCourse.getSite().getReversePath()); 	
	return linkParameters;
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse, final Project project, final Date time, final String tail) {
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

    private String constructCalendarPresentation(final ExecutionCourse executionCourse, final WrittenEvaluation writtenEvaluation) {
	final StringBuilder stringBuilder = new StringBuilder();
	if (writtenEvaluation instanceof WrittenTest) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
	} else if (writtenEvaluation instanceof Exam) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
	}
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

    public void setCurricularYearID(Integer curricularYearID) {
	this.curricularYearID = curricularYearID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
	this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public void setDegreeID(Integer degreeID) {
	this.degreeID = degreeID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
	this.executionPeriodID = executionPeriodID;
    }

    public Date getBeginDate() {
	final ExecutionSemester executionSemester = getExecutionPeriod();
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
		if (executionSemester.getSemester().intValue() == 1 && executionDegree.getPeriodLessonsFirstSemester() != null) {
		    return executionDegree.getPeriodLessonsFirstSemester().getStart();
		} else if (executionSemester.getSemester().intValue() == 2 && executionDegree.getPeriodLessonsSecondSemester() != null) {
		    return executionDegree.getPeriodLessonsSecondSemester().getStart();
		} else {
		    return executionSemester.getBeginDate();
		}
	    }
	}
	return null;
    }

    public Date getEndDate() {
	final ExecutionSemester executionSemester = getExecutionPeriod();
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
		if (executionSemester.getSemester().intValue() == 1 && executionDegree.getPeriodExamsFirstSemester() != null) {
		    return executionDegree.getPeriodExamsFirstSemester().getEnd();
		} else if (executionSemester.getSemester().intValue() == 2 && executionDegree.getPeriodExamsSecondSemester() != null) {
		    return executionDegree.getPeriodExamsSecondSemester().getEnd();
		} else {
		    return executionSemester.getEndDate();
		}
	    }
	}
	return null;
    }

}