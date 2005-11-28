package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProject;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.struts.util.MessageResources;

public class CoordinatorEvaluationsBackingBean extends FenixBackingBean {

	private static final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/PublicDegreeInformation");
	private static final Locale locale = new Locale("pt", "PT");

	// These are context variables
    private Integer degreeCurricularPlanID = null;
    private Integer executionPeriodID = null;
    private Integer curricularYearID = null;

    // These are context variables for creating evaluations
    private String evaluationType = null;
    private Integer executionCourseID = null;

    // These variables hold the necessary information for creating/editing the evaluations
    private String name = null;
    private String begin = null;
    private String end = null;
    private String description = null;
    private String date = null;
    private String beginTime = null;
    private String endTime = null;
    private Integer evaluationID = null;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID") : degreeCurricularPlanID;
    }

    public Integer getExecutionPeriodID() throws FenixFilterException, FenixServiceException {
        if (executionPeriodID == null) {
        	executionPeriodID = getAndHoldIntegerParameter("executionPeriodID");
        	if (executionPeriodID == null) {
        		final IExecutionPeriod executionPeriod = getCurrentExecutionPeriod();
        		executionPeriodID = executionPeriod.getIdInternal();
        	}
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(final Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public Integer getCurricularYearID() {
        return (curricularYearID == null) ? curricularYearID = getAndHoldIntegerParameter("curricularYearID") : curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        return (IDegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, degreeCurricularPlanID);
    }

    private IExecutionPeriod getCurrentExecutionPeriod() throws FenixFilterException, FenixServiceException {
        final IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final IExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final IExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                if (executionPeriod.getState() == PeriodState.CURRENT) {
                    return executionPeriod;
                }
            }
        }
        return null;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixFilterException, FenixServiceException {
        final IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final TreeSet<IExecutionPeriod> executionPeriods = new TreeSet<IExecutionPeriod>();
        for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final IExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final IExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                executionPeriods.add(executionPeriod);
            }
        }
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (final IExecutionPeriod executionPeriod : executionPeriods) {
            final SelectItem selectItem = new SelectItem();
            selectItem.setLabel(StringAppender.append(
                    executionPeriod.getName(), " - ", executionPeriod.getExecutionYear().getYear()));
            selectItem.setValue(executionPeriod.getIdInternal());
            selectItems.add(selectItem);
        }
        return selectItems;
    }

    public List<SelectItem> getCurricularYearSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        for (int i = 1; i <= 5; i++) {
        	selectItems.add(new SelectItem(Integer.valueOf(i), String.valueOf(i)));
        }
        return selectItems;
    }

    public IExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
        final Integer executionPeriodID = getExecutionPeriodID();
        return (IExecutionPeriod) readDomainObject(ExecutionPeriod.class, executionPeriodID);
    }

    private IExecutionDegree getExecutionDegree() throws FenixFilterException, FenixServiceException {
        final IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
                return executionDegree;
            }
        }
        return null;
    }

    public Date getCalendarBegin() throws FenixFilterException, FenixServiceException {
        final IExecutionDegree executionDegree = getExecutionDegree();
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
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
        final IExecutionDegree executionDegree = getExecutionDegree();
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        if (executionDegree != null) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getEnd();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getEnd();
            } else {
                return executionPeriod.getEndDate();
            }
        } else {
            return null;
        }        
    }

    public List<IExecutionCourse> getExecutionCourses() throws FenixFilterException, FenixServiceException {
        final List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();
        final IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        final ICurricularYear curricularYear = getCurricularYear();

        for (final ICurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        	if (isActiveInExecutionPeriodAndYear(curricularCourse, executionPeriod, curricularYear)) {
        		for (final IExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
        			if (executionCourse.getExecutionPeriod() == executionPeriod) {
        				executionCourses.add(executionCourse);
        			}
        		}
        	}
        }
        return executionCourses;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixFilterException, FenixServiceException {
        final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
        final String evaluationType = getEvaluationType();

        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
			for (final IEvaluation evaluation : executionCourse.getAssociatedEvaluations()) {
				if (evaluationType == null || evaluationType.equals(evaluation.getClass().getName())) {
					if (evaluation instanceof IWrittenEvaluation) {
						final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) evaluation;
						constructCalendarLink(calendarLinks, writtenEvaluation, executionCourse);
					} else if (evaluation instanceof IProject) {
						final IProject project = (IProject) evaluation;
						constructCalendarLink(calendarLinks, project, executionCourse);
					}
				}
			}        	
        }

        return calendarLinks;
    }

    public List<IExecutionCourse> getExecutionCoursesWithoutEvaluations() throws FenixFilterException, FenixServiceException {
    	final List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();
    	for (final IExecutionCourse executionCourse : getExecutionCourses()) {
    		if (!hasNonExamEvaluation(executionCourse)) {
    			executionCourses.add(executionCourse);
    		}
    	}
    	return executionCourses;
    }

    public List<IExecutionCourse> getExecutionCoursesWithEvaluations() throws FenixFilterException, FenixServiceException {
    	final List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();
    	for (final IExecutionCourse executionCourse : getExecutionCourses()) {
    		if (hasNonExamEvaluation(executionCourse)) {
    			executionCourses.add(executionCourse);
    		}
    	}
    	return executionCourses;
    }

    private boolean hasNonExamEvaluation(final IExecutionCourse executionCourse) {
		for (final IEvaluation evaluation : executionCourse.getAssociatedEvaluations()) {
			if (!(evaluation instanceof Exam)) {
				return true;
			}
		}
		return false;
	}

	private boolean isActiveInExecutionPeriodAndYear(final ICurricularCourse curricularCourse,
    		final IExecutionPeriod executionPeriod, final ICurricularYear curricularYear) {
        for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
        	final ICurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
            if (curricularSemester.getSemester().equals(executionPeriod.getSemester())
            		&& (curricularCourseScope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime())
                    && ((curricularCourseScope.getEndDate() == null)
                    		|| (curricularCourseScope.getEndDate().getTime().getTime() >= executionPeriod.getEndDate().getTime()))) {
            	if (curricularYear == null || curricularYear == curricularSemester.getCurricularYear()) {
            		return true;
            	}
            }
        }
        return false;
	}

	private ICurricularYear getCurricularYear() throws FenixFilterException, FenixServiceException {
    	final Integer curricularYearID = getCurricularYearID();
    	return (curricularYearID != null) ? (ICurricularYear) readDomainObject(CurricularYear.class, curricularYearID) : null;
	}

	private void constructCalendarLink(final List<CalendarLink> calendarLinks, final IWrittenEvaluation writtenEvaluation, final IExecutionCourse executionCourse) {
        CalendarLink calendarLink = new CalendarLink(executionCourse, writtenEvaluation, locale);
        addLinkParameters(calendarLink, executionCourse, writtenEvaluation);
        addWrittenEvaluationLinkParameters(calendarLink, writtenEvaluation);
        calendarLinks.add(calendarLink);
    }

	private void constructCalendarLink(final List<CalendarLink> calendarLinks, final IProject project, final IExecutionCourse executionCourse) {
        final CalendarLink calendarLinkBegin = new CalendarLink(executionCourse, project, project.getBegin(), messages.getMessage(locale, "label.evaluation.project.begin"), locale);
        addLinkParameters(calendarLinkBegin, executionCourse, project);
        addProjectLinkParameters(calendarLinkBegin, project);
        calendarLinks.add(calendarLinkBegin);

        final CalendarLink calendarLinkEnd = new CalendarLink(executionCourse, project, project.getEnd(), messages.getMessage(locale, "label.evaluation.project.end"), locale);
        addLinkParameters(calendarLinkEnd, executionCourse, project);
        addProjectLinkParameters(calendarLinkEnd, project);
        calendarLinks.add(calendarLinkEnd);
    }

	private void addLinkParameters(final CalendarLink calendarLink, final IExecutionCourse executionCourse, final IEvaluation evaluation) {
        calendarLink.addLinkParameter("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
        calendarLink.addLinkParameter("executionPeriodID", executionCourse.getExecutionPeriod().getIdInternal().toString());
        calendarLink.addLinkParameter("executionCourseID", executionCourse.getIdInternal().toString());
        calendarLink.addLinkParameter("curricularYearID", (getCurricularYearID() != null) ? getCurricularYearID().toString() : "");
        calendarLink.addLinkParameter("evaluationID", evaluation.getIdInternal().toString());
        calendarLink.addLinkParameter("evaluationType", evaluation.getClass().getName());
    }

    private void addWrittenEvaluationLinkParameters(CalendarLink calendarLink, IWrittenEvaluation writtenEvaluation) {
    	if (writtenEvaluation instanceof IWrittenTest) {
    		calendarLink.addLinkParameter("description", ((IWrittenTest) writtenEvaluation).getDescription());
    	}
        calendarLink.addLinkParameter("date", DateFormatUtil.format("dd/MM/yyyy", writtenEvaluation.getDayDate()));
        calendarLink.addLinkParameter("beginTime", DateFormatUtil.format("HH:mm", writtenEvaluation.getBeginningDate()));
        calendarLink.addLinkParameter("endTime", DateFormatUtil.format("HH:mm", writtenEvaluation.getEndDate()));
	}

    private void addProjectLinkParameters(CalendarLink calendarLinkBegin, IProject project) {
    	calendarLinkBegin.addLinkParameter("name", project.getName());
    	calendarLinkBegin.addLinkParameter("begin", DateFormatUtil.format("dd/MM/yyyy HH:mm", project.getBegin()));
    	calendarLinkBegin.addLinkParameter("end", DateFormatUtil.format("dd/MM/yyyy HH:mm", project.getEnd()));
        calendarLinkBegin.addLinkParameter("description", project.getDescription());
	}

	public String getEvaluationType() {
		return (evaluationType == null) ? evaluationType = getAndHoldStringParameter("evaluationType") : evaluationType;
	}

	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}

    public List<SelectItem> getExecutionCourseSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        final List<IExecutionCourse> executionCourses = getExecutionCourses();
        for (IExecutionCourse executionCourse : executionCourses) {
        	selectItems.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        return selectItems;
    }

	public Integer getExecutionCourseID() {
		return (executionCourseID == null) ? executionCourseID = getAndHoldIntegerParameter("executionCourseID") : executionCourseID;
	}

	public void setExecutionCourseID(Integer executionCourseID) {
		this.executionCourseID = executionCourseID;
	}

	public String getDescription() {
		return (description == null) ? description = getAndHoldStringParameter("description") : description;
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
            final Object[] args = { getExecutionCourseID(),
            		getName(),
            		DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBegin()),
            		DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEnd()),
            		getDescription() };
            ServiceUtils.executeService(getUserView(), "CreateProject", args);
        } catch (final ParseException e) {
            setErrorMessage("error.invalidDate");
            return "viewCreationPage";
        }
        return "viewCalendar";
    }

    public String createWrittenTest() throws FenixFilterException, FenixServiceException, ParseException {
    	final IExecutionCourse executionCourse = getExecutionCourse();

    	final List<String> executionCourseIDs = new ArrayList<String>(1);
		executionCourseIDs.add(this.getExecutionCourseID().toString());

		final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);

		final Object[] args = { getExecutionCourseID(),
				DateFormatUtil.parse("dd/MM/yyyy", getDate()),
				DateFormatUtil.parse("HH:mm", getBeginTime()),
				DateFormatUtil.parse("HH:mm", getEndTime()),
				executionCourseIDs, curricularCourseScopeIDs, null, null, getDescription() };
		ServiceUtils.executeService(getUserView(), "CreateWrittenEvaluation", args);

		return "viewCalendar";
    }

    public String editWrittenTest() throws FenixFilterException, FenixServiceException, ParseException {
		final IExecutionCourse executionCourse = getExecutionCourse();

		final List<String> executionCourseIDs = new ArrayList<String>(1);
		executionCourseIDs.add(this.getExecutionCourseID().toString());

		final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);

		final Object[] args = { executionCourse.getIdInternal(),
				DateFormatUtil.parse("dd/MM/yyyy", getDate()),
				DateFormatUtil.parse("HH:mm", getBeginTime()),
				DateFormatUtil.parse("HH:mm", getEndTime()),
				executionCourseIDs, curricularCourseScopeIDs, null, getEvaluationID(), null, getDescription() };
		ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);

		return "viewCalendar";
	}

    public String editProject() throws FenixFilterException, FenixServiceException, ParseException {
        final Object[] args = { getExecutionCourseID(), getEvaluationID(), getName(),
                DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBegin()),
                DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEnd()), getDescription() };
        ServiceUtils.executeService(getUserView(), "EditProject", args);

		return "viewCalendar";
	}

    private IExecutionCourse getExecutionCourse() throws FenixFilterException, FenixServiceException {
    	return (IExecutionCourse) readDomainObject(ExecutionCourse.class, getExecutionCourseID());
	}

	private List<String> getCurricularCourseScopeIDs(final IExecutionCourse executionCourse) {
        final List<String> curricularCourseScopeIDs = new ArrayList<String>();
        for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
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
		return (evaluationID == null) ? evaluationID = getAndHoldIntegerParameter("evaluationID") : evaluationID;
	}

	public void setEvaluationID(Integer evaluationID) {
		this.evaluationID = evaluationID;
	}

}
