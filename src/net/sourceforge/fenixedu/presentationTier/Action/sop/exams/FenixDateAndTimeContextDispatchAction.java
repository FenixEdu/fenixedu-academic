/*
 * Created on 4/Mai/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author Ana e Ricardo
 *  
 */
public abstract class FenixDateAndTimeContextDispatchAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String date = (String) request.getAttribute(SessionConstants.DATE);
        if (date == null) {
            date = request.getParameter(SessionConstants.DATE);
        }
        if (date != null) {
            request.setAttribute(SessionConstants.DATE, date);
        }

        String startTime = (String) request.getAttribute(SessionConstants.START_TIME);
        if (startTime == null) {
            startTime = request.getParameter(SessionConstants.START_TIME);
        }
        if (startTime != null) {
            request.setAttribute(SessionConstants.START_TIME, startTime);
        }

        String endTime = (String) request.getAttribute(SessionConstants.END_TIME);
        if (endTime == null) {
            endTime = request.getParameter(SessionConstants.END_TIME);
        }
        if (endTime != null) {
            request.setAttribute(SessionConstants.END_TIME, endTime);
        }

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}