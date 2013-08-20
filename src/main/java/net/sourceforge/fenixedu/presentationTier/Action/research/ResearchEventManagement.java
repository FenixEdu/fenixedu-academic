package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ResearchEventManagement extends FenixDispatchAction {

    public ActionForward showEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String eventId = request.getParameter("eventId");
        if (eventId != null) {
            ResearchEvent event = AbstractDomainObject.fromExternalId(eventId);
            request.setAttribute("event", event);
        }

        return mapping.findForward("showEvent");
    }

}
