/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 10/Out/2003
 *
 */
package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Ana e Ricardo
 */
public class ShowExamsManagement
//	extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {
        extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
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

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixActionException,
            FenixServiceException {
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
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

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

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionCourseContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);

        Integer examID = new Integer(request.getParameter(SessionConstants.EXAM_OID));
        Object[] args = { examID };

        try {
            Boolean result = (Boolean) ServiceUtils.executeService(userView, "DeleteExamNew", args);

            if (!result.booleanValue())
                throw new Exception("error.notAuthorizedExamDelete");

        } catch (notAuthorizedServiceDeleteException exception) {
            if (exception.getMessage() != null && exception.getMessage().length() > 0) {
                actionErrors.add("error.deleteExam.withStudents",
                        new ActionError(exception.getMessage()));
            }
        } catch (Exception exception) {
            if (exception.getMessage() != null && exception.getMessage().length() > 0) {
                actionErrors.add("error.deleteExam", new ActionError(exception.getMessage()));
            }
        }

        if (!actionErrors.isEmpty()) {
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        return mapping.findForward("deleteExam");
    }
}