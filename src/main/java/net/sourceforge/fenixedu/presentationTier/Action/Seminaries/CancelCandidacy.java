/*
 * Created on 25/Ago/2003, 14:36:59
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.DeleteCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 25/Ago/2003, 14:36:59
 * 
 */
@Mapping(module = "student", path = "/cancelCandidacy", scope = "request")
@Forwards(value = { @Forward(name = "invalidQueryString", path = "naoAutorizado.do"),
        @Forward(name = "candidacyCanceled", path = "/listAllSeminaries.do") })
public class CancelCandidacy extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        User userView = getUserView(request);
        String candidacyIDString = request.getParameter("objectCode");
        if (candidacyIDString == null) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        ActionForward destiny = null;
        try {
            DeleteCandidacy.runDeleteCandidacy(candidacyIDString);
        } catch (Exception e) {
            throw new FenixActionException();
        }

        destiny = mapping.findForward("candidacyCanceled");
        return destiny;
    }
}