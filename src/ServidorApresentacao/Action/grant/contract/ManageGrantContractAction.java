/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantContractAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantContractForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Integer idInternal = null;
            if (request.getParameter("idInternal") != null) {
                idInternal = new Integer(request.getParameter("idInternal"));
            } else if ((Integer) request.getAttribute("idInternal") != null) {
                idInternal = (Integer) request.getAttribute("idInternal");
            }

            //Run the service
            Object[] args = { idInternal };
            IUserView userView = SessionUtils.getUserView(request);
            List infoGrantContractList = (List) ServiceUtils.executeService(userView,
                    "ReadAllContractsByGrantOwner", args);

            if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
                request.setAttribute("infoGrantContractList", infoGrantContractList);

            //Needed for return to manage contracts
            request.setAttribute("idInternal", idInternal);

            InfoGrantOwner infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService(userView,
                    "ReadGrantOwner", args);
            request.setAttribute("grantOwnerNumber", infoGrantOwner.getGrantOwnerNumber());
            request.setAttribute("grantOwnerName", infoGrantOwner.getPersonInfo().getNome());
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
                    null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
                    null);
        }
        return mapping.findForward("manage-grant-contract");
    }
}