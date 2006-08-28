/*
 * Created on 25/Ago/2003, 14:36:59
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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
 * Created at 25/Ago/2003, 14:36:59
 *  
 */
public class CancelCandidacy extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = getUserView(request);
        String candidacyIDString = request.getParameter("objectCode");
        Integer candidacyID;
        if (candidacyIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try {
            candidacyID = new Integer(candidacyIDString);
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        ActionForward destiny = null;
        try {
            Object[] argsReadSeminary = { candidacyID };
            ServiceManagerServiceFactory.executeService(userView, "Seminaries.DeleteCandidacy",
                    argsReadSeminary);
        } catch (Exception e) {
            throw new FenixActionException();
        }

        destiny = mapping.findForward("candidacyCanceled");
        return destiny;
    }
}