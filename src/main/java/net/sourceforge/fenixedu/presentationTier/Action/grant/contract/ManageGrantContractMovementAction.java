/*
 * Created on 3/Jul/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadAllGrantMovementsByContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Barbosa
 * @author Pica
 */
@Mapping(module = "facultyAdmOffice", path = "/manageGrantContractMovement",
        input = "/manageGrantContractMovement.do?page=0&method=prepareManageGrantContractMovement", attribute = "voidForm",
        formBean = "voidForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "manage-grant-contract-movement",
        path = "/facultyAdmOffice/grant/contract/manageGrantMovement.jsp") })
public class ManageGrantContractMovementAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareManageGrantContractMovement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer idContract = null;
        try {
            if (request.getAttribute("idContract") != null) {
                idContract = (Integer) request.getAttribute("idContract");
            } else {
                idContract = new Integer(request.getParameter("idContract"));
            }
        } catch (Exception e) {
            request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
            request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract-movement", null);
        }

        IUserView userView = UserView.getUser();
        InfoGrantContract infoGrantContract =
                (InfoGrantContract) ServiceUtils.executeService("ReadGrantContract", new Object[] { idContract });

        request.setAttribute("idContract", idContract);
        request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());

        List infoGrantContractMovementsList = ReadAllGrantMovementsByContract.run(idContract);

        if (infoGrantContractMovementsList != null && !infoGrantContractMovementsList.isEmpty()) {
            request.setAttribute("infoGrantContractMovementsList", infoGrantContractMovementsList);
        }

        // Presenting adittional information
        request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
        request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo().getGrantOwnerNumber());
        return mapping.findForward("manage-grant-contract-movement");
    }
}