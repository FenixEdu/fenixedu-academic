/*
 * Created on 23/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantSubsidyAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantSubsidyForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer idSubsidy = null;
        Integer idContract = null;
        try
        //Probably a validation error
        {
            if (!verifyParameterInRequest(request, "idSubsidy")) {
                //Check if is a new subsidy
                idContract = new Integer(request.getParameter("idContract"));
            } else {
                idSubsidy = new Integer(request.getParameter("idSubsidy"));
            }
        } catch (Exception e) {
            request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
            request.setAttribute("contractNumber", request.getParameter("contractNumber"));
            request.setAttribute("grantTypeName", request.getParameter("grantTypeName"));
            if (verifyParameterInRequest(request, "idSubsidy"))
                request.setAttribute("idSubsidy", new Integer(request.getParameter("idSubsidy")));
            return mapping.findForward("edit-grant-subsidy");
        }

        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm grantSubsidyForm = (DynaValidatorForm) form;

        try {
            InfoGrantContract infoGrantContract = null;

            if (idContract != null) //if is a new Subsidy
            {
                //Read the contract
                Object[] args3 = { idContract };
                infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
                        "ReadGrantContract", args3);
                if (infoGrantContract != null) {
                    request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
                    request
                            .setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo()
                                    .getName());
                    grantSubsidyForm.set("state", new Integer(-1));
                }
            } else {
                //Read the subsidy
                Object[] args = { idSubsidy };
                InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) ServiceUtils.executeService(
                        userView, "ReadGrantSubsidy", args);

                idContract = infoGrantSubsidy.getInfoGrantContract().getIdInternal();
                //Read the contract
                Object[] args2 = { idContract };
                infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
                        "ReadGrantContract", args2);
                if (infoGrantContract != null) {
                    request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
                    request
                            .setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo()
                                    .getName());
                }
                //Populate the form
                if (infoGrantSubsidy != null) {
                    setFormGrantSubsidy(grantSubsidyForm, infoGrantSubsidy);
                    request.setAttribute("idSubsidy", infoGrantSubsidy.getIdInternal());
                }
            }
            grantSubsidyForm.set("idContract", idContract);
            request.setAttribute("idContract", idContract);
            grantSubsidyForm.set("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
            request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.subsidy.read", "manage-grant-contract", null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
                    null);
        }
        return mapping.findForward("edit-grant-subsidy");
    }

    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {

            DynaValidatorForm editGrantSubsidyForm = (DynaValidatorForm) form;
            InfoGrantSubsidy infoGrantSubsidy = populateInfoFromForm(editGrantSubsidyForm);

            if (infoGrantSubsidy.getDateBeginSubsidy().after(infoGrantSubsidy.getDateEndSubsidy())) {
                return setError(request, mapping, "errors.grant.subsidy.conflictdates", null, null);
            }

            IUserView userView = SessionUtils.getUserView(request);
            if (infoGrantSubsidy.getState().equals(new Integer(-1))) {
                //If is a new Subsidy
                infoGrantSubsidy.setState(new Integer(1)); //Active the subsidy
            }

            //Save the subsidy
            Object[] args = { infoGrantSubsidy };
            ServiceUtils.executeService(userView, "EditGrantSubsidy", args);
            request.setAttribute("idInternal", editGrantSubsidyForm.get("idGrantOwner"));

        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.subsidy.edit", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
        return mapping.findForward("manage-grant-subsidy");
    }

    /*
     * Populates form from InfoSubsidy
     */
    private void setFormGrantSubsidy(DynaValidatorForm form, InfoGrantSubsidy infoGrantSubsidy)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        form.set("idGrantSubsidy", infoGrantSubsidy.getIdInternal());
        if (infoGrantSubsidy.getValue() != null)
            form.set("value", infoGrantSubsidy.getValue().toString());
        if (infoGrantSubsidy.getValueFullName() != null)
            form.set("valueFullName", infoGrantSubsidy.getValueFullName());
        if (infoGrantSubsidy.getTotalCost() != null)
            form.set("totalCost", infoGrantSubsidy.getTotalCost().toString());
        if (infoGrantSubsidy.getDateBeginSubsidy() != null)
            form.set("dateBeginSubsidy", sdf.format(infoGrantSubsidy.getDateBeginSubsidy()));
        if (infoGrantSubsidy.getDateEndSubsidy() != null)
            form.set("dateEndSubsidy", sdf.format(infoGrantSubsidy.getDateEndSubsidy()));
        //In case state is null.. than this is a new subsidy, put the state -1
        if (infoGrantSubsidy.getState() != null)
            form.set("state", infoGrantSubsidy.getState());
        else
            form.set("state", new Integer(-1));
    }

    private InfoGrantSubsidy populateInfoFromForm(DynaValidatorForm editGrantSubsidyForm)
            throws Exception {

        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        if (verifyStringParameterInForm(editGrantSubsidyForm, "idGrantSubsidy"))
            infoGrantSubsidy.setIdInternal((Integer) editGrantSubsidyForm.get("idGrantSubsidy"));
        if (verifyStringParameterInForm(editGrantSubsidyForm, "value"))
            infoGrantSubsidy.setValue(new Double((String) editGrantSubsidyForm.get("value")));
        if (verifyStringParameterInForm(editGrantSubsidyForm, "valueFullName"))
            infoGrantSubsidy.setValueFullName((String) editGrantSubsidyForm.get("valueFullName"));
        if (verifyStringParameterInForm(editGrantSubsidyForm, "totalCost"))
            infoGrantSubsidy.setTotalCost(new Double((String) editGrantSubsidyForm.get("totalCost")));
        infoGrantContract.setIdInternal((Integer) editGrantSubsidyForm.get("idContract"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(editGrantSubsidyForm, "dateBeginSubsidy"))
            infoGrantSubsidy.setDateBeginSubsidy(sdf.parse((String) editGrantSubsidyForm
                    .get("dateBeginSubsidy")));
        if (verifyStringParameterInForm(editGrantSubsidyForm, "dateEndSubsidy"))
            infoGrantSubsidy.setDateEndSubsidy(sdf.parse((String) editGrantSubsidyForm
                    .get("dateEndSubsidy")));

        infoGrantSubsidy.setInfoGrantContract(infoGrantContract);
        infoGrantSubsidy.setState((Integer) editGrantSubsidyForm.get("state"));
        return infoGrantSubsidy;
    }
}