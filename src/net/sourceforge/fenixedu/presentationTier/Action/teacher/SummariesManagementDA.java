package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
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
        ExecutionCourse executionCourse = (viewState != null) ? ((SummariesManagementBean) viewState.getMetaObject().getObject()).getExecutionCourse() : readAndSaveExecutionCourse(request);        
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
        Professorship loggedProfessorship = (Professorship)request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());                      
        request.setAttribute("summariesManagementBean", new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY, executionCourse, loggedProfessorship));               
        return mapping.findForward("prepareInsertSummary");
    }
    
    public ActionForward chooseLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        LessonPlanning lessonPlanning = bean.getLessonPlanning();
        if(lessonPlanning != null) {
            bean.setSummaryText(lessonPlanning.getPlanning());
            bean.setTitle(lessonPlanning.getTitle());
            bean.setLastSummary(null);
        }
        return submit(mapping, form, request, response);
    }
    
    public ActionForward chooseLastSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        Summary lastSummary = bean.getLastSummary();
        if(lastSummary != null) {
            bean.setSummaryText(lastSummary.getSummaryText());
            bean.setTitle(lastSummary.getTitle());
            bean.setLessonPlanning(null);
        }
        return submit(mapping, form, request, response);
    }
      
    public ActionForward chooseShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        bean.setLesson(null);
        bean.setSummaryDate(null);
        return submit(mapping, form, request, response);
    }   
    
    public ActionForward chooseTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();                                                           
        Professorship professorship = getProfessorshipFromParameter(request);
        if(professorship != null) {
            bean.setProfessorship(professorship);
            bean.setTeacher(null);
            bean.setTeacherName("");
            dynaActionForm.set("teacherNumber", "");
            dynaActionForm.set("teacherName", "");
        } else if(dynaActionForm.getString("teacher").equals("-1") && !StringUtils.isEmpty(dynaActionForm.getString("teacherName"))){
            bean.setTeacherName(dynaActionForm.getString("teacherName"));
            bean.setTeacher(null);            
            bean.setProfessorship(null);
            dynaActionForm.set("teacherNumber", "");
        } else if (dynaActionForm.getString("teacher").equals("0") && !StringUtils.isEmpty(dynaActionForm.getString("teacherNumber"))) {
            Teacher teacher = null;
            try {
                teacher = Teacher.readByNumber(Integer.valueOf(dynaActionForm.getString("teacherNumber")));
            } catch (NumberFormatException e) {
                addActionMessage(request, null, null);
                return submit(mapping, form, request, response);
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
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        String service = "CreateSummaryNew";
        if(bean.getSummary() != null) {
            service = "EditSummaryNew";
        }
        final Object args[] = { bean.getExecutionCourse().getIdInternal(), bean};                      
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), service, args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
            return submit(mapping, form, request, response);
        } catch (NotAuthorizedFilterException e) {
            addActionMessage(request, null, null);
            return submit(mapping, form, request, response);
        }                
        request.setAttribute("objectCode", bean.getExecutionCourse().getIdInternal().toString());
        request.setAttribute("teacherNumber", ((Professorship)request.getAttribute("loggedTeacherProfessorship")).getTeacher().getTeacherNumber().toString());
        return mapping.findForward("showSummaries");
    }         
    
    public ActionForward createSummaryAndNew(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        final Object args[] = { bean.getExecutionCourse().getIdInternal(), bean};                      
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "CreateSummaryNew", args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
            return submit(mapping, form, request, response);
        } catch (NotAuthorizedFilterException e) {
            addActionMessage(request, null, null);
            return submit(mapping, form, request, response);
        }                 
        
        RenderUtils.invalidateViewState();
        return prepareInsertSummary(mapping, form, request, response);
    }    
    
    public ActionForward createSummaryAndSame(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();        
        final Object args[] = { bean.getExecutionCourse().getIdInternal(), bean};                      
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "CreateSummaryNew", args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
            return submit(mapping, form, request, response);
        } catch (NotAuthorizedFilterException e) {
            addActionMessage(request, null, null);
            return submit(mapping, form, request, response);
        }                 
        
        RenderUtils.invalidateViewState();
        DynaActionForm dynaActionForm = (DynaActionForm) form;                              
        Professorship loggedProfessorship = (Professorship)request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        dynaActionForm.set("teacher", loggedProfessorship.getIdInternal().toString());   
        SummariesManagementBean bean2 = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY, executionCourse, loggedProfessorship);
        bean2.setSummaryText(bean.getSummaryText());
        bean2.setTitle(bean.getTitle());
        request.setAttribute("summariesManagementBean", bean2);               
        return mapping.findForward("prepareInsertSummary");
    }    
    
    public ActionForward showSummaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        request.setAttribute("objectCode", bean.getExecutionCourse().getIdInternal().toString());
        request.setAttribute("teacherNumber", ((Professorship)request.getAttribute("loggedTeacherProfessorship")).getTeacher().getTeacherNumber().toString());
        return mapping.findForward("showSummaries");
    }
    
    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
           DynaActionForm dynaActionForm = (DynaActionForm) form;
           Summary summary = getSummaryFromParameter(request);           
           SummaryType summaryType;
           Lesson lesson = null;
           if(summary.getIsExtraLesson()) {
               summaryType = SummaryType.EXTRA_SUMMARY;               
           } else {
               summaryType = SummaryType.NORMAL_SUMMARY;
               Shift shift = summary.getShift();
               lesson = shift.getLessonByBeginTime(summary.getSummaryHourHourMinuteSecond());
           }
           
           DateTimeFieldType[] dateTimeFieldTypes = {DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()};
           HourMinuteSecond time = summary.getSummaryHourHourMinuteSecond();
           int[] timeArray = {time.getHour(), time.getMinuteOfHour()};                    
           Partial timePartial = new Partial(dateTimeFieldTypes, timeArray);           
           
           SummariesManagementBean bean = new SummariesManagementBean(summary.getTitle(), summary.getSummaryText(), summary.getStudentsNumber(), 
                   summaryType, summary.getProfessorship(), summary.getTeacherName(), summary.getTeacher(), summary.getShift(), lesson, 
                   summary.getSummaryDateYearMonthDay(), summary.getRoom(), timePartial, summary);
           
           setTeacherDataToFormBean(dynaActionForm, bean);
           request.setAttribute("summariesManagementBean", bean); 
           return mapping.findForward("prepareInsertSummary");
    }
    
    public ActionForward submit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        final IViewState viewState = RenderUtils.getViewState();       
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();                          
        setTeacherDataToFormBean(dynaActionForm, bean);
        request.setAttribute("summariesManagementBean", bean);        
        return mapping.findForward("prepareInsertSummary");
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
        bean.setShift(null);        
        setLoggedTeacherToFormBean(request, dynaActionForm, bean);
        request.setAttribute("summariesManagementBean", bean);        
        return mapping.findForward("prepareInsertSummary");
    }
              
    // -------- Private Methods --------- //    
       
    private void setLoggedTeacherToFormBean(HttpServletRequest request, DynaActionForm dynaActionForm, SummariesManagementBean bean) {
        bean.setProfessorship((Professorship)request.getAttribute("loggedTeacherProfessorship"));
        bean.setTeacherName(null);
        bean.setTeacher(null);
        dynaActionForm.set("teacher", ((Professorship)request.getAttribute("loggedTeacherProfessorship")).getIdInternal().toString());
        dynaActionForm.set("teacherNumber", "");
        dynaActionForm.set("teacherName", "");
    }
    
    private void setTeacherDataToFormBean(DynaActionForm dynaActionForm, SummariesManagementBean bean) {
        dynaActionForm.set("teacher", bean.getTeacherChoose());
        dynaActionForm.set("teacherName", (bean.getTeacherName() != null) ? bean.getTeacherName() : "");
        dynaActionForm.set("teacherNumber", (bean.getTeacher() != null) ? String.valueOf(bean.getTeacher().getTeacherNumber()) : "");
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
        if(!StringUtils.isEmpty(professorshipIDString) && !(professorshipIDString.equals("0") || professorshipIDString.equals("-1"))) {
            final Integer professorshipID = professorshipIDString != null ? Integer.valueOf(professorshipIDString) : null;
            return rootDomainObject.readProfessorshipByOID(professorshipID);
        }
        return null;
    }
}
