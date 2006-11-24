package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean.ListSummaryType;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

/**
 * @author Manuel Pinto
 * 
 * Sep/2006 Fenix
 * 
 */

public class SummariesManagementDA extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState();
	ExecutionCourse executionCourse = null;
	
	if (viewState != null && viewState.getMetaObject().getObject() instanceof SummariesManagementBean) {
	    executionCourse = ((SummariesManagementBean) viewState.getMetaObject().getObject()).getExecutionCourse();
	} else if (viewState != null && viewState.getMetaObject().getObject() instanceof ShowSummariesBean) {
	    executionCourse = ((ShowSummariesBean) viewState.getMetaObject().getObject()).getExecutionCourse();
	} else if (viewState != null && viewState.getMetaObject().getObject() instanceof NextPossibleSummaryLessonsAndDatesBean) {
	    executionCourse = ((NextPossibleSummaryLessonsAndDatesBean) viewState.getMetaObject().getObject()).getLesson().getShift().getDisciplinaExecucao();
	} else {
	    executionCourse = readAndSaveExecutionCourse(request);
	}

	String teacherNumber = request.getParameter("teacherNumber_");
	Teacher loggedTeacher = (StringUtils.isEmpty(teacherNumber) ? getLoggedPerson(request).getTeacher() : Teacher.readByNumber(Integer.valueOf(teacherNumber)));
        System.out.println("logged teacher: " + loggedTeacher.getIdInternal() + " execution course: " + executionCourse.getIdInternal());
	Professorship loggedProfessorship = loggedTeacher.getProfessorshipByExecutionCourse(executionCourse);

	request.setAttribute("loggedTeacherProfessorship", loggedProfessorship);
	request.setAttribute("loggedIsResponsible", loggedProfessorship.isResponsibleFor());
	request.setAttribute("executionCourse", executionCourse);

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareInsertSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());
	request.setAttribute("summariesManagementBean",	new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY,	executionCourse, loggedProfessorship, null));
	return mapping.findForward("prepareInsertSummary");
    }
    
    public ActionForward goToInsertSummaryAgain(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);	
	return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward chooseLessonPlanning(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLessonPlanning");
	if(summaryViewState == null) {
	    summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
	}
	
	SummariesManagementBean bean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	LessonPlanning lessonPlanning = bean.getLessonPlanning();
	if (lessonPlanning != null) {
	    bean.setSummaryText(lessonPlanning.getPlanning());
	    bean.setTitle(lessonPlanning.getTitle());
	    bean.setLastSummary(null);
	}
	
	return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseLastSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLastSummary");
	if(summaryViewState == null) {
	    summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
	}
	
	SummariesManagementBean bean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	Summary lastSummary = bean.getLastSummary();
	if (lastSummary != null) {
	    bean.setSummaryText(lastSummary.getSummaryText());
	    bean.setTitle(lastSummary.getTitle());
	    bean.setLessonPlanning(null);
	}
	
	return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithShifts");
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	bean.setLesson(null);
	bean.setSummaryDate(null);
		
	return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseLesson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithLessons");
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	bean.setSummaryDate(null);	
	
	return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithDate");
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
		
	return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward chooseSummaryType(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	
	bean.setLesson(null);
	bean.setSummaryDate(null);
	bean.setSummaryRoom(null);
	bean.setSummaryTime(null);
		
	return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward createSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

	String service = "CreateSummary";
	if (bean.getSummary() != null) {
	    service = "EditSummary";
	}
	
	final Object args[] = { bean };
	try {
	    executeService(request, service, args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	}

	return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward createSummaryAndNew(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

	final Object args[] = { bean };
	try {
	    executeService(request, "CreateSummary", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	}
	
	RenderUtils.invalidateViewState();
	return prepareInsertSummary(mapping, form, request, response);
    }

    public ActionForward createSummaryAndSame(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

	final Object args[] = { bean };
	try {
	    executeService(request, "CreateSummary", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
	}

	RenderUtils.invalidateViewState();	
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	
	Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());
	SummariesManagementBean newBean = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY, executionCourse, loggedProfessorship, null);
	newBean.setSummaryText(bean.getSummaryText());
	newBean.setTitle(bean.getTitle());
	
	request.setAttribute("summariesManagementBean", newBean);	
	return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	Professorship teacherLogged = ((Professorship) request.getAttribute("loggedTeacherProfessorship"));
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	Summary summary = getSummaryFromParameter(request);
	
	SummaryType summaryType;
	if (summary.getIsExtraLesson()) {
	    summaryType = SummaryType.EXTRA_SUMMARY;
	} else {
	    summaryType = SummaryType.NORMAL_SUMMARY;
	}

	DateTimeFieldType[] dateTimeFieldTypes = { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() };
	HourMinuteSecond time = summary.getSummaryHourHourMinuteSecond();
	int[] timeArray = { time.getHour(), time.getMinuteOfHour() };
	Partial timePartial = new Partial(dateTimeFieldTypes, timeArray);

	SummariesManagementBean bean = new SummariesManagementBean(summary.getTitle(), summary
		.getSummaryText(), summary.getStudentsNumber(), summaryType, summary.getProfessorship(),
		summary.getTeacherName(), summary.getTeacher(), summary.getShift(), summary.getLesson(),
		summary.getSummaryDateYearMonthDay(), summary.getRoom(), timePartial, summary,
		teacherLogged);

	return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	Summary summary = getSummaryFromParameter(request);
	Professorship professorshipLogged = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	
	final Object args[] = { executionCourse, summary, professorshipLogged.getTeacher() };
	try {
	    executeService(request, "DeleteSummary", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return prepareShowSummaries(mapping, form, request, response);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return prepareShowSummaries(mapping, form, request, response);
	}
	
	return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward prepareShowSummaries(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	Professorship professorshipLogged = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	Set<Summary> teacherSummaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
	teacherSummaries.addAll(professorshipLogged.getAssociatedSummaries());
	
	readAndSaveNextPossibleSummaryLessonsAndDates(request, executionCourse);	
	request.setAttribute("showSummariesBean", new ShowSummariesBean(new SummaryTeacherBean(professorshipLogged), executionCourse, ListSummaryType.ALL_CONTENT, professorshipLogged));
	request.setAttribute("teacherNumber", professorshipLogged.getTeacher().getTeacherNumber().toString());
	request.setAttribute("summaries", teacherSummaries);
	return mapping.findForward("prepareShowSummaries");
    }

    public ActionForward showSummariesPostBack(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState();
	ShowSummariesBean bean = (ShowSummariesBean) viewState.getMetaObject().getObject();

	ExecutionCourse executionCourse = bean.getExecutionCourse();
	ShiftType shiftType = bean.getShiftType();
	Shift shift = bean.getShift();

	SummaryTeacherBean summaryTeacher = bean.getSummaryTeacher();
	Professorship teacher = (summaryTeacher != null) ? summaryTeacher.getProfessorship() : null;
	Boolean otherTeachers = (summaryTeacher != null) ? summaryTeacher.getOthers() : null;

	Set<Summary> summariesToShow = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
	for (Summary summary : executionCourse.getAssociatedSummariesSet()) {
	    boolean insert = true;
	    if ((shift != null && (summary.getShift() == null || !summary.getShift().equals(shift)))
		    || (teacher != null && (summary.getProfessorship() == null || !summary.getProfessorship().equals(teacher)))
		    || (shiftType != null && !summary.getSummaryType().equals(shiftType))
		    || (otherTeachers != null && otherTeachers && summary.getProfessorship() != null)) {
		insert = false;
	    }
	    if (insert) {
		summariesToShow.add(summary);
	    }
	}

	readAndSaveNextPossibleSummaryLessonsAndDates(request, executionCourse);	
	request.setAttribute("showSummariesBean", bean);
	request.setAttribute("summaries", summariesToShow);
	return mapping.findForward("prepareShowSummaries");
    }

    public ActionForward prepareCreateComplexSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	String[] selectedLessons = request.getParameterValues("selectedLessonAndDate");

	if (selectedLessons == null || selectedLessons.length == 0) {
	     return goToInsertComplexSummaryAgain(request, mapping, response, form);
	    
	} else if (selectedLessons != null && selectedLessons.length != 0) {
	    
	    ShiftType shiftType = null;
	    Shift shift = null;
	    boolean uniqueType = true;	    
	    List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleLessonsDates = new ArrayList<NextPossibleSummaryLessonsAndDatesBean>();
	    
	    for (int i = 0; i < selectedLessons.length; i++) {
		String lessonRepresentation = selectedLessons[i];
		NextPossibleSummaryLessonsAndDatesBean nextLesson = NextPossibleSummaryLessonsAndDatesBean.getNewInstance(lessonRepresentation);
		nextPossibleLessonsDates.add(nextLesson);
		ShiftType lessonType = nextLesson.getLesson().getTipo();
		if (shiftType == null) {
		    shiftType = lessonType;
		    shift = nextLesson.getLesson().getShift();
		} else if (!shiftType.equals(lessonType)) {
		    request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
		    uniqueType = false;
		}
	    }
	    
	    SummariesManagementBean bean = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY, executionCourse, loggedProfessorship, nextPossibleLessonsDates);
	    if (uniqueType && shift != null) {
		bean.setShift(shift);
	    }

	    request.setAttribute("summariesManagementBean", bean);
	    dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());	    
	    return mapping.findForward("prepareInsertComplexSummary");
	}
	return prepareShowSummaries(mapping, form, request, response);
    }      

    public ActionForward createComplexSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
	SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	readAndSaveTeacher(summaryBean, (DynaActionForm) form, request, mapping);
	
	for (NextPossibleSummaryLessonsAndDatesBean bean : summaryBean.getNextPossibleSummaryLessonsAndDatesBean()) {
	    summaryBean.setShift(bean.getLesson().getShift());
	    summaryBean.setLesson(bean.getLesson());
	    summaryBean.setSummaryDate(bean.getDate());
	    summaryBean.setStudentsNumber(bean.getStudentsNumber());

	    final Object args[] = { summaryBean };
	    try {
		executeService(request, "CreateSummary", args);
	    } catch (DomainException e) {
		return returnToCreateComplexSummary(mapping, form, request, summaryBean, e);
	    } catch (NotAuthorizedFilterException e) {
		return returnToCreateComplexSummary(mapping, form, request, summaryBean, e);
	    }
	}

	return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward chooseLessonPlanningToCreateComplexSummary(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
	IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLessonPlanning");
	if(summaryViewState == null) {
	    summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
	}
	
	SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	LessonPlanning lessonPlanning = summaryBean.getLessonPlanning();
	if (lessonPlanning != null) {
	    summaryBean.setSummaryText(lessonPlanning.getPlanning());
	    summaryBean.setTitle(lessonPlanning.getTitle());
	    summaryBean.setLastSummary(null);
	}
	
	return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
    }

    public ActionForward chooseLastSummaryToCreateComplexSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {


	IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLastSummary");
	if(summaryViewState == null) {
	    summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
	}
	
	SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	Summary lastSummary = summaryBean.getLastSummary();
	if (lastSummary != null) {
	    summaryBean.setSummaryText(lastSummary.getSummaryText());
	    summaryBean.setTitle(lastSummary.getTitle());
	    summaryBean.setLessonPlanning(null);
	}
	
	return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
    }

    // -------- Private Methods --------- //

    private ActionForward goToInsertComplexSummaryAgain(HttpServletRequest request,
	    ActionMapping mapping, HttpServletResponse response, ActionForm form) {
	
	final IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");	
	if (summaryViewState != null) {	    	
	    SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
	    readAndSaveTeacher(summaryBean, (DynaActionForm) form, request, mapping);
	    return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
	}
	return prepareShowSummaries(mapping, form, request, response);
    }
    
    private ActionForward returnToCreateComplexSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, SummariesManagementBean summaryBean, Exception e) {

	if (e != null) {
	    addActionMessage(request, e.getMessage());
	}
	setTeacherDataToFormBean((DynaActionForm) form, summaryBean);
	request.setAttribute("summariesManagementBean", summaryBean);	
	return mapping.findForward("prepareInsertComplexSummary");
    }

    private void readAndSaveTeacher(SummariesManagementBean bean, DynaActionForm dynaActionForm,
	    HttpServletRequest request, ActionMapping mapping) {

	Professorship professorship = getProfessorshipFromParameter(request);
	if (professorship != null) {
	    bean.setProfessorship(professorship);
	    bean.setTeacher(null);
	    bean.setTeacherName("");
	
	} else if (dynaActionForm.getString("teacher").equals("-1") && !StringUtils.isEmpty(dynaActionForm.getString("teacherName"))) {
	    bean.setTeacherName(dynaActionForm.getString("teacherName"));
	    bean.setTeacher(null);
	    bean.setProfessorship(null);
	    
	} else if (dynaActionForm.getString("teacher").equals("0") && !StringUtils.isEmpty(dynaActionForm.getString("teacherNumber"))) {
	    Teacher teacher = null;
	    try {
		teacher = Teacher.readByNumber(Integer
			.valueOf(dynaActionForm.getString("teacherNumber")));
	    } catch (NumberFormatException e) {
		addActionMessage(request, "error.summary.teacherNumber.invalid");
	    }
	    
	    if (teacher == null) {
		addActionMessage(request, "error.summary.inexistent.teacher");
	    }
	    
	    bean.setTeacher(teacher);
	    bean.setTeacherName(null);
	    bean.setProfessorship(null);
	}
    }

    private void readAndSaveNextPossibleSummaryLessonsAndDates(HttpServletRequest request, ExecutionCourse executionCourse) {	
	List<NextPossibleSummaryLessonsAndDatesBean> possibleLessonsAndDates = new ArrayList<NextPossibleSummaryLessonsAndDatesBean>();
	for (Shift shift : executionCourse.getAssociatedShiftsSet()) {
	    for (Lesson lesson : shift.getAssociatedLessonsSet()) {
		YearMonthDay nextPossibleSummaryDate = lesson.getNextPossibleSummaryDate();
		if (nextPossibleSummaryDate != null) {
		    possibleLessonsAndDates.add(new NextPossibleSummaryLessonsAndDatesBean(lesson,
			    nextPossibleSummaryDate));
		}
	    }
	}
	Collections.sort(possibleLessonsAndDates, NextPossibleSummaryLessonsAndDatesBean.COMPARATOR_BY_DATE);
	request.setAttribute("nextPossibleLessonsDates", possibleLessonsAndDates);
    }

    private ActionForward goToSummaryManagementPageAgain(ActionMapping mapping, HttpServletRequest request,
	    DynaActionForm dynaActionForm, SummariesManagementBean bean) {
	
	setTeacherDataToFormBean(dynaActionForm, bean);
	request.setAttribute("summariesManagementBean", bean);
	return mapping.findForward("prepareInsertSummary");
    }

    private void setTeacherDataToFormBean(DynaActionForm dynaActionForm, SummariesManagementBean bean) {
	if (!bean.getTeacherChoose().equals("")) {
	    dynaActionForm.set("teacher", bean.getTeacherChoose());
	    dynaActionForm.set("teacherName", (bean.getTeacherName() != null) ? bean.getTeacherName() : "");
	    dynaActionForm.set("teacherNumber", (bean.getTeacher() != null) ? String.valueOf(bean.getTeacher().getTeacherNumber()) : "");
	}
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
	ExecutionCourse executionCourse = getExecutinCourseFromParameter(request);
	request.setAttribute("executionCourse", executionCourse);
	return executionCourse;
    }

    private ExecutionCourse getExecutinCourseFromParameter(final HttpServletRequest request) {
	final String executionCourseIDString = request.getParameterMap().containsKey("executionCourseID") ? request.getParameter("executionCourseID") : (String) request.getAttribute("executionCourseID");
	final Integer executionCourseID = executionCourseIDString != null ? Integer.valueOf(executionCourseIDString) : null;
	return rootDomainObject.readExecutionCourseByOID(executionCourseID);
    }

    private Summary getSummaryFromParameter(final HttpServletRequest request) {
	final String summaryIDString = request.getParameterMap().containsKey("summaryID") ? request.getParameter("summaryID") : (String) request.getAttribute("summaryID");
	final Integer summaryID = summaryIDString != null ? Integer.valueOf(summaryIDString) : null;
	return rootDomainObject.readSummaryByOID(summaryID);
    }

    private Professorship getProfessorshipFromParameter(final HttpServletRequest request) {
	final String professorshipIDString = request.getParameterMap().containsKey("teacher") ? request.getParameter("teacher") : (String) request.getAttribute("teacher");
	if (!StringUtils.isEmpty(professorshipIDString)	&& !(professorshipIDString.equals("0") || professorshipIDString.equals("-1"))) {
	    final Integer professorshipID = professorshipIDString != null ? Integer.valueOf(professorshipIDString) : null;
	    return rootDomainObject.readProfessorshipByOID(professorshipID);
	}
	return null;
    }
}
