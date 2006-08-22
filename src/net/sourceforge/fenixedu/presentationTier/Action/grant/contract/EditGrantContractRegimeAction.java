/*
 * Created on 10/Mai/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.domain.Teacher;
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
public class EditGrantContractRegimeAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantContractRegime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer idGrantContractRegime = null;
        Integer idContract = null;
        Integer loaddb = null;
        try {
            if (verifyParameterInRequest(request, "loaddb")) {
                loaddb = new Integer(request.getParameter("loaddb"));
            }
            if (!verifyParameterInRequest(request, "grantContractRegimeId")) {
                //Check if is a new contract regime
                idContract = new Integer(request.getParameter("idContract"));
            } else {
                idGrantContractRegime = new Integer(request.getParameter("grantContractRegimeId"));
            }
        } catch (Exception e)//Probably a validation error
        {
            request.setAttribute("contractNumber", request.getParameter("contractNumber"));
            request.setAttribute("idContract", request.getParameter("idContract"));
            if (verifyParameterInRequest(request, "idContractRegime")) {
                request.setAttribute("grantContractRegimeId", new Integer(request
                        .getParameter("grantContractRegimeId")));
            }
            return mapping.findForward("edit-grant-contract-regime");
        }

        if (loaddb != null) {
            IUserView userView = SessionUtils.getUserView(request);
            DynaValidatorForm grantContractRegimeForm = (DynaValidatorForm) form;
            try {
                InfoGrantContract infoGrantContract = null;

                if (idContract != null) //if is a new Contract Regime
                {
                    //Read the contract regime
                    Object[] args3 = { idContract };
                    infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
                            "ReadGrantContract", args3);

                    if (infoGrantContract != null) {
                        request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
                    }
                    grantContractRegimeForm.set("state", new Integer(-1));
                    if (infoGrantContract.getGrantCostCenterInfo()!=null){                         
	                    grantContractRegimeForm.set("grantCostCenterId",infoGrantContract.getGrantCostCenterInfo().getIdInternal());
	                    grantContractRegimeForm.set ("keyCostCenterNumber",infoGrantContract.getGrantCostCenterInfo().getNumber());
	                    grantContractRegimeForm.set ("designation",infoGrantContract.getGrantCostCenterInfo().getDesignation());
                    }
                } else {
                    //Read the subsidy
                    Object[] args = { idGrantContractRegime };
                    InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) ServiceUtils
                            .executeService(userView, "ReadGrantContractRegime", args);

                    idContract = infoGrantContractRegime.getInfoGrantContract().getIdInternal();
 
                    //Populate the form
                    if (infoGrantContractRegime != null) {
                        setFormGrantContractRegime(grantContractRegimeForm, infoGrantContractRegime);
                        request.setAttribute("grantContractRegimeId", infoGrantContractRegime
                                .getIdInternal());
                        request.setAttribute("contractNumber", infoGrantContractRegime
                                .getInfoGrantContract().getContractNumber().toString());
                    }
                }

                grantContractRegimeForm.set("idContract", idContract);
                request.setAttribute("idContract", idContract);

            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.contact.regime.read",
                        "manage-grant-contract-regime", null);
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable",
                        "manage-grant-contract-regime", null);
            }
        } else {
            request.setAttribute("grantContractRegimeId", request.getParameter("grantContractRegimeId"));
            request.setAttribute("idContract", request.getParameter("idContract"));
        }
        return mapping.findForward("edit-grant-contract-regime");

    }

    /*
     * Edit a contract regime
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            InfoGrantContractRegime infoGrantContractRegime = populateInfoFromForm((DynaValidatorForm) form);

            if (infoGrantContractRegime.getDateBeginContract().after(
                    infoGrantContractRegime.getDateEndContract())) {
                return setError(request, mapping, "errors.grant.contract.conflictdates", null, null);
            }

            IUserView userView = SessionUtils.getUserView(request);

            //Verify the teacher
            if (infoGrantContractRegime.getInfoTeacher() != null) {
                InfoTeacher infoTeacher = null;
                Object[] argsTeacher = { infoGrantContractRegime.getInfoTeacher().getTeacherNumber() };
                infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
                        argsTeacher);
                if (infoTeacher == null) {
                    return setError(request, mapping, "errors.grant.contract.regime.unknownTeacher",
                            null, infoGrantContractRegime.getInfoTeacher().getTeacherNumber());
                }
                infoGrantContractRegime.setInfoTeacher(infoTeacher);
            }

            if (infoGrantContractRegime.getState().equals(Integer.valueOf(-1))) {
                //If is a new Regime
                infoGrantContractRegime.setState(InfoGrantContractRegime.getActiveState()); //Active
            }
            //Verify the cost center
            if (infoGrantContractRegime.getGrantCostCenterInfo() != null) {          	
            	if (infoGrantContractRegime.getGrantCostCenterInfo().getNumber().length() != 0){
                    InfoGrantCostCenter infoGrantCostCenter = null;
                    Object[] argsCostCenter = { infoGrantContractRegime.getGrantCostCenterInfo().getNumber() };
                    
                    infoGrantCostCenter = (InfoGrantCostCenter) ServiceUtils.executeService(userView, "ReadCostCenterByNumber",
                    		argsCostCenter);
                    if (infoGrantCostCenter == null) {
                        return setError(request, mapping, "errors.grant.contract.regime.unknownTeacher",
                                null, infoGrantContractRegime.getInfoTeacher().getTeacherNumber());
                    }
                 
                    infoGrantContractRegime.setGrantCostCenterInfo(infoGrantCostCenter);
            	}
            }else{
                infoGrantContractRegime.setGrantCostCenterInfo(null); 
            }
                

            if (infoGrantContractRegime.getState().equals(Integer.valueOf(-1))) {
                //If is a new Regime
                infoGrantContractRegime.setState(InfoGrantContractRegime.getActiveState()); //Active
            }
            
            Object[] args = { infoGrantContractRegime };
            ServiceUtils.executeService(userView, "EditGrantContractRegime", args);
            return mapping.findForward("manage-grant-contract-regime");

        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.contract.regime.edit", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    /*
     * Populates form from InfoSubsidy
     */
    private void setFormGrantContractRegime(DynaValidatorForm form,
            InfoGrantContractRegime infoGrantContractRegime) throws Exception {
        //Grant Contract Regime

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        form.set("grantContractRegimeId", infoGrantContractRegime.getIdInternal());
        if (infoGrantContractRegime.getDateBeginContract() != null)
            form.set("dateBeginContract", sdf.format(infoGrantContractRegime.getDateBeginContract()));
        if (infoGrantContractRegime.getDateEndContract() != null)
            form.set("dateEndContract", sdf.format(infoGrantContractRegime.getDateEndContract()));
        if (infoGrantContractRegime.getDateSendDispatchCC() != null)
            form.set("dateSendDispatchCC", sdf.format(infoGrantContractRegime.getDateSendDispatchCC()));
        if (infoGrantContractRegime.getDateDispatchCC() != null)
            form.set("dateDispatchCC", sdf.format(infoGrantContractRegime.getDateDispatchCC()));
        if (infoGrantContractRegime.getDateSendDispatchCD() != null)
            form.set("dateSendDispatchCD", sdf.format(infoGrantContractRegime.getDateSendDispatchCD()));
        if (infoGrantContractRegime.getDateDispatchCD() != null)
            form.set("dateDispatchCD", sdf.format(infoGrantContractRegime.getDateDispatchCD()));
        if (infoGrantContractRegime.getInfoTeacher() != null)
            form.set("grantContractRegimeTeacherNumber", infoGrantContractRegime.getInfoTeacher()
                    .getTeacherNumber().toString());
        if (infoGrantContractRegime.getInfoGrantContract().getGrantCostCenterInfo()!=null){
	        form.set("grantCostCenterId", infoGrantContractRegime.getInfoGrantContract().getGrantCostCenterInfo().getIdInternal());
	        form.set("keyCostCenterNumber", infoGrantContractRegime.getInfoGrantContract().getGrantCostCenterInfo().getNumber()
	                .toString());
	        form.set("designation", infoGrantContractRegime.getInfoGrantContract().getGrantCostCenterInfo().getDesignation()
	                .toString());
       }
        
        if (infoGrantContractRegime.getState() != null)
            form.set("state", infoGrantContractRegime.getState());
        else
            form.set("state", new Integer(-1));
        form.set("contractNumber", infoGrantContractRegime.getInfoGrantContract().getContractNumber()
                .toString());
      
    }

    /*
     * Populates Info from Form
     */
    private InfoGrantContractRegime populateInfoFromForm(DynaValidatorForm editGrantContractRegimeForm)
            throws Exception {
        InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();

        if (verifyStringParameterInForm(editGrantContractRegimeForm, "grantContractRegimeId"))
            infoGrantContractRegime.setIdInternal((Integer) editGrantContractRegimeForm
                    .get("grantContractRegimeId"));
        infoGrantContractRegime.setState((Integer) editGrantContractRegimeForm.get("state"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateBeginContract"))
            infoGrantContractRegime.setDateBeginContract(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateBeginContract")));
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateEndContract"))
            infoGrantContractRegime.setDateEndContract(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateEndContract")));
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateSendDispatchCC"))
            infoGrantContractRegime.setDateSendDispatchCC(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateSendDispatchCC")));
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateDispatchCC"))
            infoGrantContractRegime.setDateDispatchCC(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateDispatchCC")));
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateSendDispatchCD"))
            infoGrantContractRegime.setDateSendDispatchCD(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateSendDispatchCD")));
        if (verifyStringParameterInForm(editGrantContractRegimeForm, "dateDispatchCD"))
            infoGrantContractRegime.setDateDispatchCD(sdf.parse((String) editGrantContractRegimeForm
                    .get("dateDispatchCD")));
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        infoGrantCostCenter.setNumber((String) editGrantContractRegimeForm.get("keyCostCenterNumber"));
        infoGrantContractRegime.setGrantCostCenterInfo(infoGrantCostCenter);

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal((Integer) editGrantContractRegimeForm.get("idContract"));
        infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
       

        if (verifyStringParameterInForm(editGrantContractRegimeForm, "grantContractRegimeTeacherNumber")) {
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(Teacher.readByNumber(new Integer((String) editGrantContractRegimeForm
                    .get("grantContractRegimeTeacherNumber"))));
            infoGrantContractRegime.setInfoTeacher(infoTeacher);
        }

        return infoGrantContractRegime;
    }
}