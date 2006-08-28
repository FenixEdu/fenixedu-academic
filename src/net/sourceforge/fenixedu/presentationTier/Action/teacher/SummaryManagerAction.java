/*
 * Created on 16/Abr/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Tânia Pousão
 */
public class SummaryManagerAction extends TeacherAdministrationViewerDispatchAction {

    public ActionForward showSummaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        DynaActionForm actionForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);

        String lessonType = getLessonType(request);

        Integer shiftId = getShiftId(request);

        Integer professorShipId = getProfessorShipId(request, actionForm, userView, executionCourseId);

        SiteView siteView = getSiteView(userView, executionCourseId, lessonType, shiftId,
                professorShipId);

        selectChoices(request,
                ((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView).getComponent()), lessonType);

        Collections.sort(((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView).getComponent())
                .getInfoSummaries(), Collections.reverseOrder());

        final InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
                .getComponent();
        for (final InfoShift infoShift : (List<InfoShift>) infoSiteSummaries.getInfoShifts()) {
            Collections.sort(infoShift.getInfoLessons());
        }
        Collections.sort(infoSiteSummaries.getInfoShifts(), new BeanComparator("lessons"));
        request.setAttribute("siteView", siteView);

        return mapping.findForward("showSummaries");
    }

    protected SiteView getSiteView(IUserView userView, Integer executionCourseId, String lessonType,
            Integer shiftId, Integer professorShipId) throws FenixServiceException, FenixFilterException {

        Object[] args = { executionCourseId, lessonType, shiftId, professorShipId };
        SiteView siteView = null;

        siteView = (SiteView) ServiceUtils.executeService(userView, "ReadSummaries", args);

        return siteView;
    }

    protected Integer getProfessorShipId(HttpServletRequest request, DynaActionForm actionForm,
            IUserView userView, Integer executionCourseId) throws FenixServiceException,
            FenixFilterException {

        Integer professorShipId = null;

        if (request.getParameter("byTeacher") != null && request.getParameter("byTeacher").length() > 0) {
            professorShipId = new Integer(request.getParameter("byTeacher"));

        } else {
            Object[] args = { userView.getUtilizador(), executionCourseId };
            InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceUtils.executeService(
                    userView, "ReadProfessorshipByTeacherNumberAndExecutionCourseID", args);
            professorShipId = infoProfessorship.getIdInternal();
            actionForm.set("byTeacher", professorShipId.toString());
        }
        return professorShipId;
    }

    /**
     * @param request
     * @return
     */
    protected Integer getShiftId(HttpServletRequest request) {
        Integer shiftId = null;
        if (request.getParameter("byShift") != null && request.getParameter("byShift").length() > 0) {
            shiftId = new Integer(request.getParameter("byShift"));
        }
        return shiftId;
    }

    /**
     * @param request
     * @return
     */
    protected String getLessonType(HttpServletRequest request) {
        String lessonType = null;
        if (request.getParameter("bySummaryType") != null
                && request.getParameter("bySummaryType").length() > 0) {
            lessonType = request.getParameter("bySummaryType");
        }
        return lessonType;
    }

    private void selectChoices(HttpServletRequest request, InfoSiteSummaries summaries, String lessonType) {
        // && lessonType.intValue() != 0
        if (lessonType != null && !lessonType.equals("0")) {
            final ShiftType lessonTypeSelect = ShiftType.valueOf(lessonType);
            List infoShiftsOnlyType = (List) CollectionUtils.select(summaries.getInfoShifts(),
                    new Predicate() {

                        public boolean evaluate(Object arg0) {
                            return ((InfoShift) arg0).getTipo().equals(lessonTypeSelect);
                        }
                    });

            summaries.setInfoShifts(infoShiftsOnlyType);
        }
    }

    public ActionForward prepareInsertSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer objectCode = getObjectCode(request);
        request.setAttribute("objectCode", objectCode);

        processAnotherDate(request, form);

        boolean loggedIsResponsible = false;
        List responsibleTeachers = null;
        Object argsReadResponsibleTeachers[] = { objectCode };
        try {
            responsibleTeachers = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeachersByExecutionCourseResponsibility", argsReadResponsibleTeachers);
            for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
                InfoTeacher infoTeacher = (InfoTeacher) iter.next();
                if (infoTeacher.getInfoPerson().getUsername().equals(userView.getUtilizador())) {
                    loggedIsResponsible = true;
                    break;
                }
            }

            request.setAttribute("loggedIsResponsible", new Boolean(loggedIsResponsible));

            if (!loggedIsResponsible) {
                InfoTeacher infoTeacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(
                        userView, "ReadTeacherByUsername", new Object[] { userView.getUtilizador() });

                InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceManagerServiceFactory
                        .executeService(userView, "ReadProfessorshipByTeacherIDandExecutionCourseID",
                                new Object[] { infoTeacher.getIdInternal(), objectCode });

                request.setAttribute("loggedTeacherProfessorship", infoProfessorship.getIdInternal());
            }

        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("Can't find course's responsible teacher"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }

        Object args[] = { objectCode, userView.getUtilizador() /* userLogged */};
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareInsertSummary", args);
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.insert"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.insert"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }

        DynaActionForm actionForm = (DynaActionForm) form;
        
        try {
            final InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
                    .getComponent();
            for (final InfoShift infoShift : (List<InfoShift>) infoSiteSummaries.getInfoShifts()) {
                Collections.sort(infoShift.getInfoLessons());
            }
            Collections.sort(infoSiteSummaries.getInfoShifts(), new BeanComparator("lessons"));
            choosenShift(request, infoSiteSummaries.getInfoShifts(), actionForm);
            choosenLesson(request, (InfoSummary) request.getAttribute("summaryToInsert"));
            preSelectTeacherLogged(request, form, (InfoSiteSummaries) siteView.getComponent());
        } catch (Exception e) {
            e.printStackTrace();
            return showSummaries(mapping, form, request, response);
        }      

        if (actionForm.get("summaryDateInputOption").equals("on"))
            request.setAttribute("checked", "");

        htmlEditorConfigurations(request, actionForm);
        request.setAttribute("siteView", siteView);

        //--- Planning                
        setLessonPlannings(request, objectCode, actionForm);
        fillSummaryWithPlanning(request, actionForm);
        
        return mapping.findForward("insertSummary");
    }

    private void fillSummaryWithPlanning(HttpServletRequest request, DynaActionForm dynaActionForm) {
        String lessonPlanningID = dynaActionForm.getString("lessonPlanning");
        if(!StringUtils.isEmpty(lessonPlanningID)) {
            LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(Integer.valueOf(lessonPlanningID));
            dynaActionForm.set("summaryText", lessonPlanning.getPlanning().getContent(LanguageUtils.getLanguage().pt));
            dynaActionForm.set("title", lessonPlanning.getTitle().getContent(LanguageUtils.getLanguage().pt));
        }
    }

    private void setLessonPlannings(HttpServletRequest request, Integer objectCode, DynaActionForm actionForm) {                       
        Integer shiftID = Integer.valueOf(actionForm.getString("shift"));                      
        Shift shift = rootDomainObject.readShiftByOID(shiftID);        
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
        Set<LabelValueBean> lessonPlannings = new TreeSet<LabelValueBean>(new BeanComparator("label"));
        if(shift != null) {
            getLabelValueBeansOfLessonPlannings(executionCourse, shift.getTipo(), lessonPlannings);                     
        }
        request.setAttribute("lessonPlannings", lessonPlannings);
    }
    
    protected void processAnotherDate(HttpServletRequest request, ActionForm form) {
        DynaActionForm actionForm = (DynaActionForm) form;
        String summaryDateInputOption = request.getParameter("summaryDateInputOption");
        String summaryDateInput = (String) actionForm.get("summaryDateInput");

        if ((summaryDateInput != null) && (summaryDateInput.equals(""))) {
            actionForm.set("dateEmpty", "");
        }

        if ((summaryDateInputOption != null) && (summaryDateInputOption.equals("on"))) {
            actionForm.set("dateEmpty", "");
        }

        else if ((summaryDateInputOption == null) && (actionForm.get("dateEmpty") != null)
                && (actionForm.get("dateEmpty").equals("")))
            actionForm.set("summaryDateInput", "");
    }

    private void choosenShift(HttpServletRequest request, List infoShifts, DynaActionForm actionForm) {
        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            if (infoShifts != null && infoShifts.size() > 0) {
                ListIterator iterator = infoShifts.listIterator();
                while (iterator.hasNext()) {
                    InfoShift infoShift = (InfoShift) iterator.next();
                    if (infoShift.getIdInternal().equals(new Integer(request.getParameter("shift")))) {
                        InfoSummary infoSummaryToInsert = new InfoSummary();
                        infoSummaryToInsert.setInfoShift(infoShift);
                        request.setAttribute("summaryToInsert", infoSummaryToInsert);
                        return;
                    }
                }
            }
        }
        if (infoShifts != null && infoShifts.size() > 0) {
            InfoSummary infoSummaryToInsert = new InfoSummary();
            infoSummaryToInsert.setInfoShift((InfoShift) infoShifts.get(0));
            request.setAttribute("summaryToInsert", infoSummaryToInsert);            
            actionForm.set("shift", ((InfoShift) infoShifts.get(0)).getIdInternal().toString());
        }
    }

    private void choosenLesson(HttpServletRequest request, InfoSummary infoSummary) throws Exception {
        if (request.getParameter("forHidden") != null && request.getParameter("forHidden").length() > 0) {
            request.setAttribute("forHidden", request.getParameter("forHidden"));
        }
        if (request.getAttribute("teste") == null && request.getParameter("lesson") != null
                && request.getParameter("lesson").length() > 0) {
            if (!request.getParameter("lesson").equals("0")) {
                request.setAttribute("forHidden", "true");
                Integer lessonSelected = new Integer(request.getParameter("lesson"));
                findNextSummaryDate(request, infoSummary, lessonSelected);
                request.setAttribute("datesVisible", "true");
            } else {
                request.setAttribute("forHidden", "false");
                request.setAttribute("datesVisible", "false");
            }
        }
    }

    protected void findNextSummaryDate(HttpServletRequest request, InfoSummary infoSummary,
            Integer lessonSelected) throws FenixServiceException, FenixFilterException {
        List lessons = infoSummary.getInfoShift().getInfoLessons();
        for (Iterator iter = lessons.iterator(); iter.hasNext();) {
            InfoLesson element = (InfoLesson) iter.next();
            if (element.getIdInternal().equals(lessonSelected)) {
                GregorianCalendar calendar = new GregorianCalendar();
                InfoSummary summaryBefore;
                IUserView userView = SessionUtils.getUserView(request);
                Object args[] = { getObjectCode(request), infoSummary.getInfoShift().getIdInternal(),
                        lessonSelected };
                summaryBefore = (InfoSummary) ServiceManagerServiceFactory.executeService(userView,
                        "ReadLastSummary", args);
                List dates = new ArrayList();
                if (summaryBefore != null) {
                    calendar.setTime(summaryBefore.getSummaryDate().getTime());
                    calendar.set(Calendar.DAY_OF_WEEK, element.getDiaSemana().getDiaSemana().intValue());
                    calendar.add(Calendar.DATE, 7);
                    dates.add(calendar.getTime());
                    request.setAttribute("dates", dates);
                } else {
                    Object argsLesson[] = { element.getIdInternal() };
                    Calendar lessonStartDate = (Calendar) ServiceManagerServiceFactory.executeService(
                            userView, "ReadLessonStartDate", argsLesson);
                    lessonStartDate.set(Calendar.DAY_OF_WEEK, element.getDiaSemana().getDiaSemana()
                            .intValue());
                    dates.add(lessonStartDate.getTime());
                    request.setAttribute("dates", dates);
                }
                break;
            }
        }
        if (request.getAttribute("dates") == null) {
            request.setAttribute("dates", new ArrayList());
        }
    }

    private void preSelectTeacherLogged(HttpServletRequest request, ActionForm form,
            InfoSiteSummaries summaries) {
        if (request.getParameter("teacher") == null || request.getParameter("teacher").length() == 0) {
            if (summaries.getTeacherId() != null) {
                DynaActionForm insertSummaryForm = (DynaActionForm) form;

                insertSummaryForm.set("teacher", summaries.getTeacherId().toString());
            }
        }
    }

    public ActionForward insertSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = getUserView(request);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            InfoSummary infoSummaryToInsert = buildSummaryToInsert(request);

            if (infoSummaryToInsert.getSummaryDate() != null
                    && DateFormatUtil.isAfter("yyyyMMdd",
                            infoSummaryToInsert.getSummaryDate().getTime(), Calendar.getInstance()
                                    .getTime())) {
                ActionErrors errors = new ActionErrors();
                errors.add("error", new ActionError("error.summary.date.after.current.date"));
                saveErrors(request, errors);
                return prepareInsertSummary(mapping, form, request, response);
            }

            Object[] args = { executionCourseId, infoSummaryToInsert };
            ServiceUtils.executeService(userView, "InsertSummary", args);

        } catch (Exception e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.insertSummary", new ActionError((e.getMessage())));
            actionErrors
                    .add("error.insertSummary", new ActionError(("error.summary.impossible.insert")));
            saveErrors(request, actionErrors);
            return mapping.findForward("doPrepareInsertSummary");
        }

        DynaActionForm actionForm = (DynaActionForm) form;
        Integer saveValue = (Integer) actionForm.get("save");

        if (saveValue.equals(new Integer(0)))
            return mapping.findForward("doShowSummariesAction");
        else if (saveValue.equals(new Integer(1))) {
            resetActionForm(actionForm, true);
            request.setAttribute("teste", "true");
            return prepareInsertSummary(mapping, actionForm, request, response);
        } else {
            resetActionForm(actionForm, false);
            request.setAttribute("teste", "true");
            return prepareInsertSummary(mapping, actionForm, request, response);
        }
    }

    protected void resetActionForm(DynaActionForm actionForm, boolean teste) {
        if (teste) {
            actionForm.set("summaryText", "");
            actionForm.set("title", "");
        }
        actionForm.set("studentsNumber", "");
        actionForm.set("summaryDateInput", "");
        actionForm.set("summaryDateInputOption", "");
        actionForm.set("summaryHourInput", "");
        actionForm.set("lesson", "");
        actionForm.set("room", "");
    }

    private InfoSummary buildSummaryToInsert(HttpServletRequest request) {
        InfoSummary infoSummary = new InfoSummary();

        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            InfoShift infoShift = new InfoShift(rootDomainObject.readShiftByOID(new Integer(request.getParameter("shift"))));
            infoSummary.setInfoShift(infoShift);
        }

        if (request.getParameter("summaryDateInput") != null
                && request.getParameter("summaryDateInput").length() > 0) {
            String summaryDateString = request.getParameter("summaryDateInput");
            String[] dateTokens = summaryDateString.split("/");
            Calendar summaryDate = Calendar.getInstance();
            summaryDate.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0])).intValue());
            summaryDate.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
            summaryDate.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
            infoSummary.setSummaryDate(summaryDate);
        }

        // Summary's number of attended student
        if (request.getParameter("studentsNumber") != null
                && request.getParameter("studentsNumber").length() > 0) {
            infoSummary.setStudentsNumber(new Integer(request.getParameter("studentsNumber")));
        }

        // lesson extra or not
        if (request.getParameter("lesson") != null && request.getParameter("lesson").length() > 0) {
            Integer lessonId = new Integer(request.getParameter("lesson"));
            // extra lesson

            if (lessonId.equals(new Integer(0))) {
                infoSummary.setIsExtraLesson(Boolean.TRUE);
                // Summary's hour
                String summaryHourString = request.getParameter("summaryHourInput");
                String[] hourTokens = summaryHourString.split(":");
                Calendar summaryHour = Calendar.getInstance();
                summaryHour.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
                summaryHour.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
                infoSummary.setSummaryHour(summaryHour);

                if (request.getParameter("room") != null && request.getParameter("room").length() > 0) {
                    // lesson's room
                    InfoRoom infoRoom = new InfoRoom((OldRoom) rootDomainObject.readSpaceByOID(new Integer(request.getParameter("room"))));
                    infoSummary.setInfoRoom(infoRoom);
                }
            } else if (lessonId.intValue() >= 0) {
                infoSummary.setIsExtraLesson(Boolean.FALSE);
                infoSummary.setLessonIdSelected(lessonId);
            }
        }

        if (request.getParameter("teacher") != null && request.getParameter("teacher").length() > 0) {
            Integer teacherId = new Integer(request.getParameter("teacher"));
            if (teacherId.equals(new Integer(0))) // school's teacher
            {
                InfoTeacher infoTeacher = new InfoTeacher(Teacher.readByNumber(new Integer(request.getParameter("teacherNumber"))));
                infoSummary.setInfoTeacher(infoTeacher);
            } else if (teacherId.equals(new Integer(-1))) // external teacher
            {
                infoSummary.setTeacherName(request.getParameter("teacherName"));
            } else { // teacher belong to course
        	final Professorship professorship = rootDomainObject.readProfessorshipByOID(teacherId);
                infoSummary.setInfoProfessorship(InfoProfessorship.newInfoFromDomain(professorship));
            }
        }

        infoSummary.setTitle(request.getParameter("title"));
        infoSummary.setSummaryText(request.getParameter("summaryText"));

        return infoSummary;
    }

    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        DynaActionForm summaryForm = (DynaActionForm) form;

        String summaryIdString = request.getParameter("summaryCode");
        Integer summaryId = new Integer(summaryIdString);

        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);

        Object[] args = { executionCourseId, summaryId, userView.getUtilizador() };
        SiteView siteView = null;
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);  
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadSummary", args);

            if (request.getAttribute("summaryTextFlag") == null) {
                String summaryText = ((InfoSiteSummary) siteView.getComponent()).getInfoSummary()
                        .getSummaryText();
                if (summaryText != null)
                    summaryForm.set("summaryText", summaryText);
            } else
                summaryForm.set("summaryText", request.getAttribute("summaryTextFlag"));
                        
            boolean loggedIsResponsible = (userView.getPerson().getTeacher().responsibleFor(executionCourse) != null);
            request.setAttribute("loggedIsResponsible", new Boolean(loggedIsResponsible));

            if (!loggedIsResponsible) {
                InfoTeacher infoTeacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(
                        userView, "ReadTeacherByUsername", new Object[] { userView.getUtilizador() });

                InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceManagerServiceFactory
                        .executeService(userView, "ReadProfessorshipByTeacherIDandExecutionCourseID",
                                new Object[] { infoTeacher.getIdInternal(), executionCourseId });

                request.setAttribute("loggedTeacherProfessorship", infoProfessorship.getIdInternal());
            } else {

                if ((((InfoSiteSummary) siteView.getComponent())).getInfoSummary().getInfoTeacher() != null) {
                    summaryForm.set("teacherNumber", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getInfoTeacher().getTeacherNumber().toString());
                    summaryForm.set("teacher", "0");
                } else if ((((InfoSiteSummary) siteView.getComponent())).getInfoSummary()
                        .getInfoProfessorship() != null) {

                    summaryForm.set("teacher", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getInfoProfessorship().getIdInternal().toString());
                } else {
                    summaryForm.set("teacher", "-1");
                    summaryForm.set("teacherName", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getTeacherName());
                }
            }

        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.editSummary", new ActionError("error.summary.impossible.preedit"));
            actionErrors.add("error.editSummary", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return showSummaries(mapping, form, request, response);
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.edit"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }

        try {
            final InfoSiteSummary infoSiteSummary = (InfoSiteSummary) ((ExecutionCourseSiteView) siteView)
                    .getComponent();
            for (final InfoShift infoShift : (List<InfoShift>) infoSiteSummary.getInfoShifts()) {
                Collections.sort(infoShift.getInfoLessons());
            }
            Collections.sort(infoSiteSummary.getInfoShifts(), new BeanComparator("lessons"));
            Collections.sort(infoSiteSummary.getInfoSummary().getInfoShift().getInfoLessons());
            shiftChanged(request, siteView);
            choosenLesson(request, infoSiteSummary);
        } catch (Exception e) {
            return showSummaries(mapping, form, request, response);
        }

        htmlEditorConfigurations(request, summaryForm);
        request.setAttribute("siteView", siteView);

        //--- Planning                
        setLessonPlannings(request, executionCourse, siteView);
        fillSummaryWithPlanning(request, siteView, summaryForm);
        
        return mapping.findForward("editSummary");
    }
    
    private void setLessonPlannings(HttpServletRequest request, ExecutionCourse executionCourse, SiteView siteView) {                                      
        InfoShift shift = ((InfoSiteSummary) siteView.getComponent()).getInfoSummary().getInfoShift();        
        Set<LabelValueBean> lessonPlannings = new TreeSet<LabelValueBean>(new BeanComparator("label"));
        if(shift != null) {
            getLabelValueBeansOfLessonPlannings(executionCourse, shift.getTipo(), lessonPlannings);                      
        }
        request.setAttribute("lessonPlannings", lessonPlannings);
    }

    private void getLabelValueBeansOfLessonPlannings(ExecutionCourse executionCourse, ShiftType shiftType, Set<LabelValueBean> lessonPlannings) {        
        String shiftTypeLabel = RenderUtils.getEnumString(shiftType, null);                     
        for (LessonPlanning planning : executionCourse.getLessonPlanningsOrderedByOrder(shiftType)) {
            String label = "Aula " + planning.getOrderOfPlanning() + " (" + shiftTypeLabel + ")";
            lessonPlannings.add(new LabelValueBean(label, planning.getIdInternal().toString()));
        }
    }
    
    private void fillSummaryWithPlanning(HttpServletRequest request, SiteView siteView, DynaActionForm summaryForm) {
        String lessonPlanningID = summaryForm.getString("lessonPlanning");
        if(!StringUtils.isEmpty(lessonPlanningID)) {
            LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(Integer.valueOf(lessonPlanningID));
            summaryForm.set("summaryText", lessonPlanning.getPlanning().getContent(LanguageUtils.getLanguage().pt));
            ((InfoSiteSummary) siteView.getComponent()).getInfoSummary().setTitle(lessonPlanning.getTitle().getContent(LanguageUtils.getLanguage().pt));
        }
    }

    private void shiftChanged(HttpServletRequest request, SiteView siteView) {
        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            List infoShifts = ((InfoSiteSummary) siteView.getComponent()).getInfoShifts();
            ListIterator iterator = infoShifts.listIterator();
            while (iterator.hasNext()) {
                InfoShift infoShift = (InfoShift) iterator.next();
                if (infoShift.getIdInternal().equals(new Integer(request.getParameter("shift")))) {
                    ((InfoSiteSummary) siteView.getComponent()).getInfoSummary().setInfoShift(infoShift);
                }
            }
        }
    }

    private void choosenLesson(HttpServletRequest request, InfoSiteSummary siteSummary) {
        if (request.getParameter("lesson") != null && request.getParameter("lesson").length() > 0) {
            if (!request.getParameter("lesson").equals("0")) {
                // normal lesson
                request.setAttribute("forHidden", "true");
                siteSummary.getInfoSummary().setIsExtraLesson(Boolean.FALSE);
                siteSummary.getInfoSummary().setLessonIdSelected(
                        new Integer(request.getParameter("lesson")));
            } else {
                // extra lesson
                request.setAttribute("forHidden", "false");
                siteSummary.getInfoSummary().setIsExtraLesson(Boolean.TRUE);
                siteSummary.getInfoSummary().setLessonIdSelected(new Integer(0));
            }
        } else {
            if (siteSummary.getInfoSummary().getIsExtraLesson().equals(Boolean.TRUE)) {
                request.setAttribute("forHidden", "false");
            } else if (siteSummary.getInfoSummary().getIsExtraLesson().equals(Boolean.FALSE)) {
                request.setAttribute("forHidden", "true");
            }
        }
    }

    public ActionForward editSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = getUserView(request);

            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            InfoSummary infoSummaryToEdit = buildSummaryToInsert(request);
            infoSummaryToEdit.setIdInternal(summaryId);

            Object[] args = { executionCourseId, infoSummaryToEdit };

            ServiceUtils.executeService(userView, "EditSummary", args);

        } catch (Exception e) {

            ActionErrors actionErrors = new ActionErrors();
            if (e.getMessage() == null && e instanceof NotAuthorizedFilterException) {
                actionErrors.add("error.editSummary", new ActionError("error.summary.not.authorized"));
            } else {
                actionErrors.add("error.editSummary", new ActionError(e.getMessage()));
            }
            actionErrors.add("error.editSummary", new ActionError("error.summary.impossible.edit"));
            saveErrors(request, actionErrors);

            String text = request.getParameter("summaryText");
            request.setAttribute("summaryTextFlag", text);

            return prepareEditSummary(mapping, form, request, response);// mudei
        }

        return mapping.findForward("doShowSummariesAction");
    }

    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = getUserView(request);

            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            Object[] args = { executionCourseId, summaryId };
            ServiceUtils.executeService(userView, "DeleteSummary", args);
        } catch (Exception e) {

            ActionErrors actionErrors = new ActionErrors();
            if (e instanceof NotAuthorizedException) {
                actionErrors.add("error.editSummary", new ActionError("error.summary.not.authorized"));
            }
            actionErrors.add("error.deleteSummary", new ActionError("error.summary.impossible.delete"));
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("doShowSummariesAction");
    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null && objectCodeString.length() > 0) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    private void htmlEditorConfigurations(HttpServletRequest request, DynaActionForm actionForm) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("Safari/") == -1 && header.indexOf("Opera/") == -1
                && header.indexOf("Konqueror/") == -1) {

            if (actionForm.get("editor").equals("") || (actionForm.get("editor").equals("true")))
                request.setAttribute("verEditor", "true");

        } else
            request.setAttribute("naoVerEditor", "true");
    }
}