/*
 * Created on 4/Ago/2003, 12:14:24
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Ago/2003, 12:14:24
 * 
 */
public class ShowCandidacyForm extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        User userView = getUserView(request);
        String equivalencyIDString = request.getParameter("objectCode");
        if (equivalencyIDString == null) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }

        InfoEquivalency equivalency = null;
        ActionForward destiny = null;
        try {
            equivalency = GetEquivalency.runGetEquivalency(equivalencyIDString);
        } catch (Exception e) {
            throw new FenixActionException();
        }

        destiny = mapping.findForward("showCandidacyFormNonCompleteModality");
        request.setAttribute("equivalency", equivalency);
        return destiny;
    }
}