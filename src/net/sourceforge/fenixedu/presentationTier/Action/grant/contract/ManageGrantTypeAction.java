/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantTypeAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantTypeForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Object[] args = {};
            IUserView userView = SessionUtils.getUserView(request);
            List infoGrantTypeList = (List) ServiceUtils.executeService(userView, "ReadAllGrantTypes",
                    args);

            if (infoGrantTypeList != null && !infoGrantTypeList.isEmpty())
                request.setAttribute("infoGrantTypeList", infoGrantTypeList);

            return mapping.findForward("manage-grant-type");
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-type", null);
        }
    }
}