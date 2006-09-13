package net.sourceforge.fenixedu.presentationTier.Action.teacher;

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
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
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

public class SummariesManagementDA extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState();
	ExecutionCourse executionCourse = null;
	if(viewState != null && viewState.getMetaObject().getObject() instanceof SummariesManagementBean) {
	    executionCourse = ((SummariesManagementBean) viewState.getMetaObject().getObject()).getExecutionCourse();
	} else if(viewState != null && viewState.getMetaObject().getObject() instanceof ShowSummariesBean) {
	    executionCourse = ((ShowSummariesBean) viewState.getMetaObject().getObject()).getExecutionCourse();
	} else {
	    executionCourse = readAndSaveExecutionCourse(request); 
	}
	 
	String teacherNumber = request.getParameter("teacherNumber_");
	Teacher loggedTeacher = (StringUtils.isEmpty(teacherNumber) ? getUserView(request).getPerson().getTeacher() : Teacher.readByNumber(Integer.valueOf(teacherNumber)));
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
	request.setAttribute("summariesManagementBean",
		new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY,
			executionCourse, loggedProfessorship));
	return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward chooseLessonPlanning(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	LessonPlanning lessonPlanning = bean.getLessonPlanning();
	if (lessonPlanning != null) {
	    bean.setSummaryText(lessonPlanning.getPlanning());
	    bean.setTitle(lessonPlanning.getTitle());
	    bean.setLastSummary(null);
	}
	RenderUtils.invalidateViewState();
	return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
    }

    public ActionForward chooseLastSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	Summary lastSummary = bean.getLastSummary();
	if (lastSummary != null) {
	    bean.setSummaryText(lastSummary.getSummaryText());
	    bean.setTitle(lastSummary.getTitle());
	    bean.setLessonPlanning(null);
	}
	RenderUtils.invalidateViewState();
	return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
    }

    public ActionForward chooseShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	bean.setLesson(null);
	bean.setSummaryDate(null);
	RenderUtils.invalidateViewState();
	return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
    }
    
    public ActionForward chooseLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();	
	bean.setSummaryDate(null);
	RenderUtils.invalidateViewState();
	return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
    }
    
    public ActionForward chooseDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	RenderUtils.invalidateViewState();	
	return goToSummaryManagementPage(mapping, request, dynaActionForm, bean);
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
	RenderUtils.invalidateViewState();
	return goToSummaryManagementPage(mapping, request, dynaActionForm, bean);
    }
      
    public ActionForward chooseTeacher(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	Professorship professorship = getProfessorshipFromParameter(request);
	if (professorship != null) {
	    bean.setProfessorship(professorship);
	    bean.setTeacher(null);
	    bean.setTeacherName("");
	    dynaActionForm.set("teacherNumber", "");
	    dynaActionForm.set("teacherName", "");
	} else if (dynaActionForm.getString("teacher").equals("-1")
		&& !StringUtils.isEmpty(dynaActionForm.getString("teacherName"))) {
	    bean.setTeacherName(dynaActionForm.getString("teacherName"));
	    bean.setTeacher(null);
	    bean.setProfessorship(null);
	    dynaActionForm.set("teacherNumber", "");
	} else if (dynaActionForm.getString("teacher").equals("0")
		&& !StringUtils.isEmpty(dynaActionForm.getString("teacherNumber"))) {
	    Teacher teacher = null;
	    try {
		teacher = Teacher.readByNumber(Integer.valueOf(dynaActionForm.getString("teacherNumber")));
	    } catch (NumberFormatException e) {
		addActionMessage(request, e.getMessage());
		return goToSummaryManagementPage(mapping, request, dynaActionForm, bean);
	    }
	    bean.setTeacher(teacher);
	    bean.setTeacherName(null);
	    bean.setProfessorship(null);
	    dynaActionForm.set("teacherName", "");
	} else {
	    setLoggedTeacherToFormBean(request, dynaActionForm, bean);
	}
	request.setAttribute("summariesManagementBean", bean);
	return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward createSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	
	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	String service = "CreateSummary";
	if (bean.getSummary() != null) {
	    service = "EditSummary";
	}
	final Object args[] = { bean };
	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), service, args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	}
		
	return prepareShowSummaries(mapping, form, request, response);	
    }

    public ActionForward createSummaryAndNew(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	final Object args[] = { bean };
	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), "CreateSummary", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	}

	RenderUtils.invalidateViewState();
	return prepareInsertSummary(mapping, form, request, response);
    }

    public ActionForward createSummaryAndSame(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState();
	SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
	final Object args[] = { bean };
	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), "CreateSummary", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	} catch (NotAuthorizedFilterException e) {
	    addActionMessage(request, e.getMessage());
	    return goToSummaryManagementPage(mapping, request, (DynaActionForm)form, bean);
	}

	RenderUtils.invalidateViewState();
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());
	SummariesManagementBean bean2 = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY, executionCourse, loggedProfessorship);
	bean2.setSummaryText(bean.getSummaryText());
	bean2.setTitle(bean.getTitle());
	request.setAttribute("summariesManagementBean", bean2);
	return mapping.findForward("prepareInsertSummary");
    }  

    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	Professorship teacherLogged = ((Professorship) request.getAttribute("loggedTeacherProfessorship"));
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	Summary summary = getSummaryFromParameter(request);
	SummaryType summaryType;
	Lesson lesson = null;
	if (summary.getIsExtraLesson()) {
	    summaryType = SummaryType.EXTRA_SUMMARY;
	} else {
	    summaryType = SummaryType.NORMAL_SUMMARY;
	    Shift shift = summary.getShift();
	    lesson = shift.getLessonByBeginTime(summary.getSummaryHourHourMinuteSecond());
	}

	DateTimeFieldType[] dateTimeFieldTypes = { DateTimeFieldType.hourOfDay(),
		DateTimeFieldType.minuteOfHour() };
	HourMinuteSecond time = summary.getSummaryHourHourMinuteSecond();
	int[] timeArray = { time.getHour(), time.getMinuteOfHour() };
	Partial timePartial = new Partial(dateTimeFieldTypes, timeArray);

	SummariesManagementBean bean = new SummariesManagementBean(summary.getTitle(), summary
		.getSummaryText(), summary.getStudentsNumber(), summaryType, summary.getProfessorship(),
		summary.getTeacherName(), summary.getTeacher(), summary.getShift(), lesson, summary
			.getSummaryDateYearMonthDay(), summary.getRoom(), timePartial, summary,
		teacherLogged);

	return goToSummaryManagementPage(mapping, request, dynaActionForm, bean);
    }    
               
    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	Summary summary = getSummaryFromParameter(request);
	Professorship professorshipLogged = (Professorship) request.getAttribute("loggedTeacherProfessorship");
	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	final Object args[] = { executionCourse, summary, professorshipLogged.getTeacher() };	
	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteSummary", args);	    
	} catch (DomainException e) {	  
	    addActionMessage(request, e.getKey(), e.getArgs());
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
	request.setAttribute("showSummariesBean", new ShowSummariesBean(new SummaryTeacherBean(professorshipLogged), executionCourse, ListSummaryType.ALL));
	request.setAttribute("teacherNumber", professorshipLogged.getTeacher().getTeacherNumber().toString());
	Set<Summary> professorshipSummaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
	professorshipSummaries.addAll(professorshipLogged.getAssociatedSummaries());
	request.setAttribute("summaries", professorshipSummaries);
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
	    if((shift != null && (summary.getShift() == null || !summary.getShift().equals(shift))) ||
		    (teacher != null && (summary.getProfessorship() == null || !summary.getProfessorship().equals(teacher))) ||
		    (shiftType != null && !summary.getSummaryType().equals(shiftType)) ||
		    (otherTeachers != null && otherTeachers && summary.getProfessorship() != null)) {
		insert = false;
	    }
	    if(insert) {
		summariesToShow.add(summary);
	    }
	}
	
	RenderUtils.invalidateViewState();
	request.setAttribute("showSummariesBean", bean);
	request.setAttribute("summaries", summariesToShow);
	return mapping.findForward("prepareShowSummaries");
    }    
    
    
    
    // -------- Private Methods --------- //
    
    private ActionForward goToSummaryManagementPage(ActionMapping mapping, HttpServletRequest request, DynaActionForm dynaActionForm, SummariesManagementBean bean) {
	setTeacherDataToFormBean(dynaActionForm, bean);
	request.setAttribute("summariesManagementBean", bean);
	return mapping.findForward("prepareInsertSummary");
    }
    
    private void setLoggedTeacherToFormBean(HttpServletRequest request, DynaActionForm dynaActionForm,
	    SummariesManagementBean bean) {
	bean.setProfessorship((Professorship) request.getAttribute("loggedTeacherProfessorship"));
	bean.setTeacherName(null);
	bean.setTeacher(null);
	dynaActionForm.set("teacher", ((Professorship) request
		.getAttribute("loggedTeacherProfessorship")).getIdInternal().toString());
	dynaActionForm.set("teacherNumber", "");
	dynaActionForm.set("teacherName", "");
    }

    private void setTeacherDataToFormBean(DynaActionForm dynaActionForm, SummariesManagementBean bean) {
	dynaActionForm.set("teacher", bean.getTeacherChoose());
	dynaActionForm.set("teacherName", (bean.getTeacherName() != null) ? bean.getTeacherName() : "");
	dynaActionForm.set("teacherNumber", (bean.getTeacher() != null) ? String.valueOf(bean
		.getTeacher().getTeacherNumber()) : "");
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
	ExecutionCourse executionCourse = getExecutinCourseFromParameter(request);
	request.setAttribute("executionCourse", executionCourse);
	return executionCourse;
    }

    private ExecutionCourse getExecutinCourseFromParameter(final HttpServletRequest request) {
	final String executionCourseIDString = request.getParameterMap()
		.containsKey("executionCourseID") ? request.getParameter("executionCourseID")
		: (String) request.getAttribute("executionCourseID");
	final Integer executionCourseID = executionCourseIDString != null ? Integer
		.valueOf(executionCourseIDString) : null;
	return rootDomainObject.readExecutionCourseByOID(executionCourseID);
    }

    private Summary getSummaryFromParameter(final HttpServletRequest request) {
	final String summaryIDString = request.getParameterMap().containsKey("summaryID") ? request
		.getParameter("summaryID") : (String) request.getAttribute("summaryID");
	final Integer summaryID = summaryIDString != null ? Integer.valueOf(summaryIDString) : null;
	return rootDomainObject.readSummaryByOID(summaryID);
    }

    private Professorship getProfessorshipFromParameter(final HttpServletRequest request) {
	final String professorshipIDString = request.getParameterMap().containsKey("teacher") ? request
		.getParameter("teacher") : (String) request.getAttribute("teacher");
	if (!StringUtils.isEmpty(professorshipIDString)
		&& !(professorshipIDString.equals("0") || professorshipIDString.equals("-1"))) {
	    final Integer professorshipID = professorshipIDString != null ? Integer
		    .valueOf(professorshipIDString) : null;
	    return rootDomainObject.readProfessorshipByOID(professorshipID);
	}
	return null;
    }
}
