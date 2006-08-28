/*
 * Created on 4/Ago/2003, 12:14:24
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Ago/2003, 12:14:24
 *  
 */
public class ShowCandidacyForm extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = this.getSession(request);
        IUserView userView = getUserView(request);
        String equivalencyIDString = request.getParameter("objectCode");
        Integer equivalencyID;
        if (equivalencyIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try {
            equivalencyID = new Integer(equivalencyIDString);
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoEquivalency equivalency = null;
        ActionForward destiny = null;
        try {
            Object[] argsReadSeminary = { equivalencyID };
            equivalency = (InfoEquivalency) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetEquivalency", argsReadSeminary);
        } catch (Exception e) {
            throw new FenixActionException();
        }

        destiny = mapping.findForward("showCandidacyFormNonCompleteModality");
        request.setAttribute("equivalency", equivalency);
        return destiny;
    }
}