/*
 * Created on 11/Jun/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ViewSmsDeliveryReportsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        List infoSentSmsList = null;

        Object args[] = { userView };

        try {
            infoSentSmsList = (List) ServiceUtils.executeService(userView, "ReadSentSmsByPerson", args);
            request.setAttribute(SessionConstants.LIST_SMS_DELIVERY_REPORTS, infoSentSmsList);
        } catch (FenixServiceException e) {
        }

        return mapping.findForward("start");

    }

}