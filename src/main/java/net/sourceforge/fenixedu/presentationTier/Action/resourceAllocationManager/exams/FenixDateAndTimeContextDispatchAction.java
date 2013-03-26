/*
 * Created on 4/Mai/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ana e Ricardo
 * 
 */
public abstract class FenixDateAndTimeContextDispatchAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String date = (String) request.getAttribute(PresentationConstants.DATE);
        if (date == null) {
            date = request.getParameter(PresentationConstants.DATE);
        }
        if (date != null) {
            request.setAttribute(PresentationConstants.DATE, date);
        }

        String startTime = (String) request.getAttribute(PresentationConstants.START_TIME);
        if (startTime == null) {
            startTime = request.getParameter(PresentationConstants.START_TIME);
        }
        if (startTime != null) {
            request.setAttribute(PresentationConstants.START_TIME, startTime);
        }

        String endTime = (String) request.getAttribute(PresentationConstants.END_TIME);
        if (endTime == null) {
            endTime = request.getParameter(PresentationConstants.END_TIME);
        }
        if (endTime != null) {
            request.setAttribute(PresentationConstants.END_TIME, endTime);
        }

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}