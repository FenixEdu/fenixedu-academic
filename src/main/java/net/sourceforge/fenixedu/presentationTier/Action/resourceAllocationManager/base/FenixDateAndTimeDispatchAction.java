package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixDateAndTimeDispatchAction extends FenixContextDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Calendar examDateAndTime = Calendar.getInstance();
        Long dateAndTime = null;
        try {
            dateAndTime = new Long(request.getParameter(PresentationConstants.EXAM_DATEANDTIME));
        } catch (NumberFormatException ex) {
            examDateAndTime = (Calendar) request.getAttribute(PresentationConstants.EXAM_DATEANDTIME);
            if (examDateAndTime != null) {
                request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, examDateAndTime);
            } else {
                request.removeAttribute(PresentationConstants.EXAM_DATEANDTIME);
                request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, null);
            }
        }

        if (dateAndTime != null) {
            examDateAndTime.setTimeInMillis(dateAndTime.longValue());
            request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, examDateAndTime);
        }

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}
