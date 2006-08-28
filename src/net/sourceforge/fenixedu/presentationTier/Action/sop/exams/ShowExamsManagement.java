/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ana e Ricardo
 */
public class ShowExamsManagement extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            InfoExamsMap infoExamsMap = getExamsMap(request);
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        } else {
            throw new FenixActionException();
        }

        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, ContextUtils
                .createCurricularYearList());
        request.setAttribute(SessionConstants.DEGREES, ContextUtils.createExecutionDegreeList(request));

        return mapping.findForward("viewExamsMap");
    }

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        InfoCurricularYear curricularYearObj = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);

        Integer curricularYear = curricularYearObj.getYear();

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        List curricularYearsList = new ArrayList();
        curricularYearsList.add(curricularYear);

        Object[] args = { infoExecutionDegree, curricularYearsList, infoExecutionPeriod };
        InfoExamsMap infoExamsMap;

        infoExamsMap = (InfoExamsMap) ServiceUtils
                .executeService(userView, "ReadFilteredExamsMap", args);

        return infoExamsMap;
    }

    public ActionForward createByCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        ContextUtils.setCurricularYearContext(request);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);
        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal()
                .toString());

        ContextUtils.setExecutionDegreeContext(request);

        return mapping.findForward("createExamByCourse");
    }

    public ActionForward createByDay(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setCurricularYearContext(request);

        Integer day = new Integer(request.getParameter(SessionConstants.DAY));
        Integer month = new Integer(request.getParameter(SessionConstants.MONTH));
        Integer year = new Integer(request.getParameter(SessionConstants.YEAR));
        request.setAttribute(SessionConstants.DAY, day);
        request.setAttribute(SessionConstants.MONTH, month);
        request.setAttribute(SessionConstants.YEAR, year);

        ContextUtils.setExecutionDegreeContext(request);

        return mapping.findForward("createExamByDay");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionCourseContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);

        Integer examID = new Integer(request.getParameter(SessionConstants.EXAM_OID));
        Object[] args = { examID };
        InfoExam infoExam = (InfoExam) ServiceUtils.executeService(userView, "ReadExamByOID", args);
        request.setAttribute(SessionConstants.EXAM, infoExam);
        request.setAttribute(SessionConstants.EXAM_OID, infoExam.getIdInternal());
        return mapping.findForward("editExam");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        ActionErrors actionErrors = new ActionErrors();

        IUserView userView = getUserView(request);

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionCourseContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);

        Integer examID = new Integer(request.getParameter(SessionConstants.EXAM_OID));
        Object[] args = { null, examID };

        try {
            ServiceUtils.executeService(userView, "DeleteWrittenEvaluation", args);
        } catch (FenixServiceException exception) {
            actionErrors.add(exception.getMessage(), new ActionError(exception.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
        
        return mapping.findForward("deleteExam");
    }
}