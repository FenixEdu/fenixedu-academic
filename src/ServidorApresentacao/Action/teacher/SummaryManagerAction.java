/*
 * Created on 16/Abr/2004
 */
package ServidorApresentacao.Action.teacher;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoProfessorship;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 */
public class SummaryManagerAction extends
        TeacherAdministrationViewerDispatchAction {

    public ActionForward showSummaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);

        Integer lessonType = null;
        if (request.getParameter("bySummaryType") != null
                && request.getParameter("bySummaryType").length() > 0) {
            lessonType = new Integer(request.getParameter("bySummaryType"));
        }

        Integer shiftId = null;
        if (request.getParameter("byShift") != null
                && request.getParameter("byShift").length() > 0) {
            shiftId = new Integer(request.getParameter("byShift"));
        }

        Integer professorShiftId = null;
        if (request.getParameter("byTeacher") != null
                && request.getParameter("byTeacher").length() > 0) {
            professorShiftId = new Integer(request.getParameter("byTeacher"));
        }

        Object[] args = { executionCourseId, lessonType, shiftId,
                professorShiftId};
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadSummaries", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add("error",
                    new ActionError("error.summary.impossible.show"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        try {
            selectChoices(request, ((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
                    .getComponent()), lessonType);
            	
            Collections.sort(
                    ((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
                            .getComponent()).getInfoSummaries(), Collections
                            .reverseOrder());
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add("error",
                    new ActionError("error.summary.impossible.show"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showSummaries");
    }

    private void selectChoices(HttpServletRequest request, InfoSiteSummaries summaries, Integer lessonType) {
        if (request.getParameter("onlyType") != null
                && request.getParameter("onlyType").length() > 0) {
            if(request.getParameter("onlyType").equals("lesson") && lessonType != null && lessonType.intValue() != 0) {
              final TipoAula lessonTypeSelect = new TipoAula(lessonType.intValue());
              List infoShiftsOnlyType = (List) CollectionUtils.select(summaries.getInfoShifts(), new Predicate() {

                public boolean evaluate(Object arg0) {
                    return ((InfoShift) arg0).getTipo().equals(lessonTypeSelect);
                }});                
            
               summaries.setInfoShifts(infoShiftsOnlyType);
            }            
        }
    }

    public ActionForward prepareInsertSummary(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session
                .getAttribute(SessionConstants.U_VIEW);

        Integer objectCode = getObjectCode(request);
        request.setAttribute("objectCode", objectCode);

        Object args[] = { objectCode, userView.getUtilizador()/*userLogged*/};
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceManagerServiceFactory.executeService(
                    userView, "PrepareInsertSummary", args);
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError(
                    "error.summary.impossible.insert"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError(
                    "error.summary.impossible.insert"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("siteView", siteView);

        try {
            choosenShift(request, ((InfoSiteSummaries) siteView.getComponent())
                    .getInfoShifts());
            choosenLesson(request);
            preSelectTeacherLogged(form, (InfoSiteSummaries) siteView.getComponent());
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.getInputForward();
        }

        return mapping.findForward("insertSummary");
    }

    private void choosenShift(HttpServletRequest request, List infoShifts) {
        if (request.getParameter("shift") != null
                && request.getParameter("shift").length() > 0) {
            if (infoShifts != null && infoShifts.size() > 0) {
                ListIterator iterator = infoShifts.listIterator();
                while (iterator.hasNext()) {
                    InfoShift infoShift = (InfoShift) iterator.next();
                    if (infoShift.getIdInternal().equals(
                            new Integer(request.getParameter("shift")))) {
                        InfoSummary infoSummaryToInsert = new InfoSummary();
                        infoSummaryToInsert.setInfoShift(infoShift);
                        request.setAttribute("summaryToInsert",
                                infoSummaryToInsert);

                        return;
                    }
                }
            }
        }

        if (infoShifts != null && infoShifts.size() > 0) {
            InfoSummary infoSummaryToInsert = new InfoSummary();
            infoSummaryToInsert.setInfoShift((InfoShift) infoShifts.get(0));
            request.setAttribute("summaryToInsert", infoSummaryToInsert);
            request.setAttribute("shift", ((InfoShift) infoShifts.get(0))
                    .getIdInternal());
        }
    }

    private void choosenLesson(HttpServletRequest request) {
        if (request.getParameter("forHidden") != null
                && request.getParameter("forHidden").length() > 0) {
              request.setAttribute("forHidden", request.getParameter("forHidden"));                 
        }
    }
    
    private void preSelectTeacherLogged(ActionForm form, InfoSiteSummaries summaries) {
        if(summaries.getTeacherId() != null) {
            DynaActionForm insertSummaryForm = (DynaActionForm) form;
            
            insertSummaryForm.set("teacher", summaries.getTeacherId().toString());
        }
    }

    public ActionForward insertSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            InfoSummary infoSummaryToInsert = buildSummaryToInsert(request);

            Object[] args = { executionCourseId, infoSummaryToInsert};
            ServiceUtils.executeService(userView, "InsertSummary", args);
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.insertSummary", new ActionError((e
                    .getMessage())));
            actionErrors.add("error.insertSummary", new ActionError(
                    ("error.summary.impossible.insert")));
            saveErrors(request, actionErrors);
            return prepareInsertSummary(mapping, form, request, response);
        }
        return showSummaries(mapping, form, request, response);
    }

    private InfoSummary buildSummaryToInsert(HttpServletRequest request) {
        InfoSummary infoSummary = new InfoSummary();

        if (request.getParameter("shift") != null
                && request.getParameter("shift").length() > 0) {
            InfoShift infoShift = new InfoShift();
            infoShift.setIdInternal(new Integer(request.getParameter("shift")));
            infoSummary.setInfoShift(infoShift);
        }

        //Summary's date
        String summaryDateString = request.getParameter("summaryDateInput");
        String[] dateTokens = summaryDateString.split("/");
        Calendar summaryDate = Calendar.getInstance();
        summaryDate.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0]))
                .intValue());
        summaryDate.set(Calendar.MONTH,
                (new Integer(dateTokens[1])).intValue() - 1);
        summaryDate.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
        infoSummary.setSummaryDate(summaryDate);

        //Summary's number of attended student
        if (request.getParameter("studentsNumber") != null
                && request.getParameter("studentsNumber").length() > 0) {
            infoSummary.setStudentsNumber(new Integer(request
                    .getParameter("studentsNumber")));
        }

        //lesson extra or not
        if (request.getParameter("lesson") != null
                && request.getParameter("lesson").length() > 0) {
            Integer lessonId = new Integer(request.getParameter("lesson"));
            //extra lesson
            if (lessonId.equals(new Integer(0))) {
                infoSummary.setIsExtraLesson(Boolean.TRUE);

                //Summary's hour
                String summaryHourString = request
                        .getParameter("summaryHourInput");
                String[] hourTokens = summaryHourString.split(":");
                Calendar summaryHour = Calendar.getInstance();
                summaryHour.set(Calendar.HOUR_OF_DAY, (new Integer(
                        hourTokens[0])).intValue());
                summaryHour.set(Calendar.MINUTE, (new Integer(hourTokens[1]))
                        .intValue());
                infoSummary.setSummaryHour(summaryHour);

                if (request.getParameter("room") != null
                        && request.getParameter("room").length() > 0) {
                    //lesson's room
                    InfoRoom infoRoom = new InfoRoom();
                    infoRoom.setIdInternal(new Integer(request
                            .getParameter("room")));
                    infoSummary.setInfoRoom(infoRoom);
                }
            } else if (lessonId.intValue() >= 0) {
                infoSummary.setIsExtraLesson(Boolean.FALSE);

                infoSummary.setLessonIdSelected(lessonId);
            }
        }

        if (request.getParameter("teacher") != null
                && request.getParameter("teacher").length() > 0) {
            Integer teacherId = new Integer(request.getParameter("teacher"));
            if (teacherId.equals(new Integer(0))) //school's teacher
            {
                InfoTeacher infoTeacher = new InfoTeacher();
                infoTeacher.setTeacherNumber(new Integer(request
                        .getParameter("teacherNumber")));
                infoSummary.setInfoTeacher(infoTeacher);
            } else if (teacherId.equals(new Integer(-1))) //external teacher
            {
                infoSummary.setTeacherName(request.getParameter("teacherName"));
            } else { //teacher belong to course
                InfoProfessorship infoProfessorship = new InfoProfessorship();
                infoProfessorship.setIdInternal(teacherId);
                infoSummary.setInfoProfessorship(infoProfessorship);
            }
        }

        infoSummary.setTitle(request.getParameter("title"));
        infoSummary.setSummaryText(request.getParameter("summaryText"));

        return infoSummary;
    }

    public ActionForward prepareEditSummary(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        String summaryIdString = request.getParameter("summaryCode");
        Integer summaryId = new Integer(summaryIdString);

        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);

        Object[] args = { executionCourseId, summaryId};
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadSummary", args);
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.editSummary", new ActionError(
                    "error.summary.impossible.preedit"));
            actionErrors.add("error.editSummary", new ActionError(e
                    .getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error",
                    new ActionError("error.summary.impossible.edit"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        try {
            shiftChanged(request, siteView);
            choosenLesson(request, (InfoSiteSummary) siteView.getComponent());
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.getInputForward();
        }

        request.setAttribute("siteView", siteView);

        return mapping.findForward("editSummary");
    }

    private void choosenLesson(HttpServletRequest request, InfoSiteSummary summary) {
        // TODO Auto-generated method stub
        if (request.getParameter("forHidden") != null
                && request.getParameter("forHidden").length() > 0) {
              request.setAttribute("forHidden", request.getParameter("forHidden"));                 
        } else {
            if(summary.getInfoSummary().getIsExtraLesson().equals(Boolean.TRUE)) {
            request.setAttribute("forHidden", "false");  
            } else if(summary.getInfoSummary().getIsExtraLesson().equals(Boolean.FALSE)) {
                request.setAttribute("forHidden", "t");  
            } 
        }
    }

    private void shiftChanged(HttpServletRequest request, SiteView siteView) {
        if (request.getParameter("shift") != null
                && request.getParameter("shift").length() > 0) {
            List infoShifts = ((InfoSiteSummary) siteView.getComponent())
                    .getInfoShifts();
            ListIterator iterator = infoShifts.listIterator();
            while (iterator.hasNext()) {
                InfoShift infoShift = (InfoShift) iterator.next();
                if (infoShift.getIdInternal().equals(
                        new Integer(request.getParameter("shift")))) {
                    ((InfoSiteSummary) siteView.getComponent())
                            .getInfoSummary().setInfoShift(infoShift);
                }
            }
        }
    }

    public ActionForward editSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);

            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            InfoSummary infoSummaryToEdit = buildSummaryToInsert(request);
            infoSummaryToEdit.setIdInternal(summaryId);

            Object[] args = { executionCourseId, infoSummaryToEdit};

            ServiceUtils.executeService(userView, "EditSummary", args);
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.editSummary", new ActionError(e
                    .getMessage()));
            actionErrors.add("error.editSummary", new ActionError(
                    "error.summary.impossible.edit"));
            saveErrors(request, actionErrors);
            return prepareEditSummary(mapping, form, request, response);//mudei
        }

        return showSummaries(mapping, form, request, response);
    }

    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);

            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);

            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);

            Object[] args = { executionCourseId, summaryId};
            ServiceUtils.executeService(userView, "DeleteSummary", args);
        } catch (Exception e) {
            e.printStackTrace();
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.deleteSummary", new ActionError(
                    "error.summary.impossible.delete"));
            saveErrors(request, actionErrors);
        }
        return showSummaries(mapping, form, request, response);
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
}