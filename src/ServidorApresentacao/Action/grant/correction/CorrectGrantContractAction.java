/*
 * Created on May 17, 2004
 */

package ServidorApresentacao.Action.grant.correction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class CorrectGrantContractAction extends FenixDispatchAction {

    public ActionForward prepareForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            String action = null;
            if (verifyParameterInRequest(request, "action"))
                action = request.getParameter("action");

            if (action.equals("deleteContract")) {
                return mapping.findForward("correct-grant-contract-delete");
            } else if (action.equals("changeNumberContract")) {
                return mapping.findForward("correct-grant-contract-change-number");
            } else if (action.equals("moveContract")) {
                return mapping.findForward("correct-grant-contract-move");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    public ActionForward deleteContract(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm
                    .get("grantContractNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        try {
            IUserView userView = SessionUtils.getUserView(request);
            //Read the grant owner
            Object[] argsGrantOwner = { null, null, null, grantOwnerNumber, new Boolean(false), null };
            List infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsGrantOwner);
            if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null,
                        null);
            }

            InfoGrantOwner infoGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

            //Read the contracts
            Object[] argsContracts = { infoGrantOwner.getIdInternal() };
            List infoGrantContractList = (List) ServiceUtils.executeService(userView,
                    "ReadAllContractsByGrantOwner", argsContracts);
            InfoGrantContract infoGrantContract = null;
            if (!infoGrantContractList.isEmpty()) {
                //Find the contract
                for (int i = 0; i < infoGrantContractList.size(); i++) {
                    InfoGrantContract temp = (InfoGrantContract) infoGrantContractList.get(i);
                    if (temp.getContractNumber().equals(grantContractNumber)) {
                        infoGrantContract = temp;
                        break;
                    }
                }
            }
            if (infoGrantContract == null) {
                return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
            }
            //Delete the contract
            Object[] argsDeleteGrantContract = { infoGrantContract.getIdInternal() };
            ServiceUtils.executeService(userView, "DeleteGrantContract", argsDeleteGrantContract);
            //Set of the request variables and return
            request.setAttribute("correctionNumber2", "yes");
            return mapping.findForward("correct-grant-contract-delete");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    public ActionForward changeNumberContract(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;
        Integer newGrantContractNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm
                    .get("grantContractNumber"));
            newGrantContractNumber = new Integer((String) correctGrantContractForm
                    .get("newGrantContractNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        try {
            IUserView userView = SessionUtils.getUserView(request);
            //Read the grant owner
            Object[] argsGrantOwner = { null, null, null, grantOwnerNumber, new Boolean(false), null };
            List infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsGrantOwner);
            if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null,
                        null);
            }
            InfoGrantOwner infoGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);
            //Read the contracts
            Object[] argsContracts = { infoGrantOwner.getIdInternal() };
            List infoGrantContractList = (List) ServiceUtils.executeService(userView,
                    "ReadAllContractsByGrantOwner", argsContracts);
            InfoGrantContract infoGrantContract = null;
            if (!infoGrantContractList.isEmpty()) {
                //Find the contract
                for (int i = 0; i < infoGrantContractList.size(); i++) {
                    InfoGrantContract temp = (InfoGrantContract) infoGrantContractList.get(i);
                    if (temp.getContractNumber().equals(newGrantContractNumber)) {
                        //There is already a contract with the future number
                        return setError(request, mapping,
                                "errors.grant.correction.contractWithSameNumberExists", null, null);
                    }
                    if (temp.getContractNumber().equals(grantContractNumber)) {
                        infoGrantContract = temp;
                    }
                }
            }
            if (infoGrantContract == null) {
                return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
            }
            //Change the number, save the contract
            infoGrantContract.setContractNumber(newGrantContractNumber);
            Object[] argsNewGrantContract = { infoGrantContract };
            ServiceUtils.executeService(userView, "EditGrantContract", argsNewGrantContract);
            //Set of the request variables and return
            request.setAttribute("correctionNumber3", "yes");
            return mapping.findForward("correct-grant-contract-change-number");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    public ActionForward moveContract(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;
        Integer newGrantOwnerNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm
                    .get("grantContractNumber"));
            newGrantOwnerNumber = new Integer((String) correctGrantContractForm
                    .get("newGrantOwnerNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        try {
            IUserView userView = SessionUtils.getUserView(request);
            //Read the original grant owner
            Object[] argsOriginalGrantOwner = { null, null, null, grantOwnerNumber, new Boolean(false),
                    null };
            List infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsOriginalGrantOwner);
            if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null,
                        null);
            }

            InfoGrantOwner originalGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

            //Read the new grant owner
            Object[] argsNewGrantOwner = { null, null, null, newGrantOwnerNumber, new Boolean(false),
                    null };
            infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsNewGrantOwner);
            if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null,
                        null);
            }

            InfoGrantOwner newGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

            //Read the contracts of the original grant owner
            Object[] argsOriginalContracts = { originalGrantOwner.getIdInternal() };
            List originalGrantContractList = (List) ServiceUtils.executeService(userView,
                    "ReadAllContractsByGrantOwner", argsOriginalContracts);

            //Read the contracts of the original grant owner
            Object[] argsNewContracts = { newGrantOwner.getIdInternal() };
            List newGrantContractList = (List) ServiceUtils.executeService(userView,
                    "ReadAllContractsByGrantOwner", argsNewContracts);

            //Find the contract to move
            InfoGrantContract infoGrantContractToMove = null;
            if (!originalGrantContractList.isEmpty()) {
                for (int i = 0; i < originalGrantContractList.size(); i++) {
                    InfoGrantContract temp = (InfoGrantContract) originalGrantContractList.get(i);
                    if (temp.getContractNumber().equals(grantContractNumber)) {
                        infoGrantContractToMove = temp;
                    }
                }
            }
            if (infoGrantContractToMove == null) {
                return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
            }

            //Find biggest number of contract in new Grant Owner
            //so that there aren't conflicts moving the contract
            int numeroMaxContrato = 0;
            if (!newGrantContractList.isEmpty()) {
                for (int i = 0; i < newGrantContractList.size(); i++) {

                    InfoGrantContract temp = (InfoGrantContract) newGrantContractList.get(i);
                    if (numeroMaxContrato < temp.getContractNumber().intValue()) {
                        numeroMaxContrato = temp.getContractNumber().intValue();
                    }
                }
            }

            //Change the number and the grant owner, save the contract
            infoGrantContractToMove.setContractNumber(new Integer(++numeroMaxContrato));
            infoGrantContractToMove.setGrantOwnerInfo(newGrantOwner);

            Object[] argsNewGrantContract = { infoGrantContractToMove };
            ServiceUtils.executeService(userView, "EditGrantContract", argsNewGrantContract);

            //Set of the request variables and return
            request.setAttribute("correctionNumber4", "yes");
            return mapping.findForward("correct-grant-contract-move");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }
}