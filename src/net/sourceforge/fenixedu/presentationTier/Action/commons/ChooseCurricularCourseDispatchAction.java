package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fernanda Quitério 03/07/2003
 *  
 */
public class ChooseCurricularCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChooseCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);

        // Get the Curricular Course List
        Object args[] = { executionYear, degree };
        IUserView userView = getUserView(request);
        List curricularCourseList = null;
        try {
            curricularCourseList = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCurricularCoursesByDegree", args);
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("message.public.notfound.curricularCourses"));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }
        Collections.sort(curricularCourseList, new BeanComparator("name"));
        request.setAttribute("curricularCourses", curricularCourseList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        String executionYear = getFromRequest("executionYear", request);
        Integer courseID = Integer.valueOf(getFromRequest("courseID", request));
        request.setAttribute("courseID", getFromRequest("courseID", request));

        //		parameters necessary to write in jsp
        request.setAttribute("curricularCourse", getFromRequest("curricularCourse", request));
        request.setAttribute("executionYear", getFromRequest("executionYear", request));
        request.setAttribute("degree", getFromRequest("degree", request));
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

        IUserView userView = getUserView(request);
        Object args[] = { userView, courseID, executionYear };

        List listEnrolmentEvaluation = null;
        try {
            listEnrolmentEvaluation = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentMarksListByCurricularCourse", args);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        if (listEnrolmentEvaluation.size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
            saveErrors(request, actionErrors);
            return mapping.findForward("NoStudents");
        }

        return mapping.findForward("ChooseSuccess");
    }

    public ActionForward chooseCurricularCourseByID(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        if(request.getParameter("degreeCurricularPlanID") != null){
            Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        request.setAttribute("courseID", getFromRequest("courseID", request));

        Integer courseID = new Integer(getFromRequest("courseID", request));
        IUserView userView = getUserView(request);

        List studentList = null;
        try {
            Object args[] = { userView, courseID, null };
            studentList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentListByCurricularCourse", args);
        } catch (NotAuthorizedFilterException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        InfoCurricularCourse infoCurricularCourse = null;
        try {
            Object args[] = { courseID };
            infoCurricularCourse = (InfoCurricularCourse) ServiceManagerServiceFactory.executeService(
                    userView, "ReadCurricularCourseByID", args);
        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoCurricularCourse != null) {
            request.setAttribute("infoCurricularCourse", infoCurricularCourse);
        }

        request.setAttribute("enrolment_list", studentList);

        String value = request.getParameter("viewPhoto");
        if (value != null && value.equals("true")) {
            request.setAttribute("viewPhoto", Boolean.TRUE);
        } else {
            request.setAttribute("viewPhoto", Boolean.FALSE);
        }

        return mapping.findForward("ChooseSuccess");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}