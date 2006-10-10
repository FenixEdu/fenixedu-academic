/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 18, 2006,11:12:24 AM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br><br>
 * Created on Jul 18, 2006,11:12:24 AM
 *
 */
public class NotAvailableYet extends FenixAction{

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("notAvailable");
    }
    
}
