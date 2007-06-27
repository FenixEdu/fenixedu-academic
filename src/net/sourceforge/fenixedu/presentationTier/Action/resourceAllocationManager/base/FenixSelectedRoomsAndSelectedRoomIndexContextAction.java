package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixSelectedRoomsAndSelectedRoomIndexContextAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        ContextUtils.setSelectedRoomsContext(request);
        ContextUtils.setSelectedRoomIndexContext(request);

        return actionForward;
    }

}
