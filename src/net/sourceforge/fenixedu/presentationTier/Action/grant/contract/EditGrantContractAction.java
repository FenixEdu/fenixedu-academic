/*
 * Created on 20/Dec/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
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
 *  
 */

public class EditGrantContractAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantContractForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        try {
            //Read grant types for the contract
            Object[] args2 = {};
            List grantTypeList = (List) ServiceUtils
                    .executeService(userView, "ReadAllGrantTypes", args2);
            request.setAttribute("grantTypeList", grantTypeList);
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.type.read", "manage-grant-contract", null);
        }

        if (!verifyParameterInRequest(request, "loaddb")) {
            //Validation error
            request.setAttribute("idInternal", request.getParameter("idInternal"));
            return mapping.findForward("edit-grant-contract");
        }
        Integer idContract = null;
        if (verifyParameterInRequest(request, "idContract")) {
            idContract = new Integer(request.getParameter("idContract"));
        }
        DynaValidatorForm grantContractForm = (DynaValidatorForm) form;
        if (idContract != null) {
            try {
                //Read the contract
                Object[] args = { idContract };
                InfoGrantContract infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(
                        userView, "ReadGrantContract", args);

                //Read the actual Regime associated with this contract
                Object[] argregime = { idContract, new Integer(1) };
                List infoGrantContractRegimeActiveList = (List) ServiceUtils.executeService(userView,
                        "ReadGrantContractRegimeByContractAndState", argregime);
                //It should only be one active contract regime
               
                if (infoGrantContractRegimeActiveList.isEmpty()
                		
                        || infoGrantContractRegimeActiveList.size() > 1) {
             
                    setError(request, mapping, "errors.grant.contract.active.regime.read",
                            "manage-grant-contract", null);
                }
                InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoGrantContractRegimeActiveList
                        .get(0);
              
                //Populate the form
                setFormGrantContract(grantContractForm, infoGrantContract, infoGrantContractRegime);
                request
                        .setAttribute("idInternal", infoGrantContract.getGrantOwnerInfo()
                                .getIdInternal());
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.contract.read", "manage-grant-contract",
                        null);
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
                        null);
            }
        } else {
            //New contract
            try {
                grantContractForm.set("idInternal", new Integer(request.getParameter("idInternal")));
                request.setAttribute("idInternal", new Integer(request.getParameter("idInternal")));
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
                        null);
            }
        }

        return mapping.findForward("edit-grant-contract");
    }

    /*
     * Edit Grant Contract
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            DynaValidatorForm editGrantContractForm = (DynaValidatorForm) form;
            IUserView userView = SessionUtils.getUserView(request);

            InfoGrantContract infoGrantContract = populateInfoGrantContractFromForm(editGrantContractForm);
            InfoGrantContractRegime infoGrantContractRegime = populateInfoGrantContractRegimeFromForm(editGrantContractForm);

            if (infoGrantContractRegime.getDateBeginContract().after(
                    infoGrantContractRegime.getDateEndContract())) {
                return setError(request, mapping, "errors.grant.contract.conflictdates", null, null);
            }
            Teacher teacher = Teacher.readByNumber(new Integer((String) editGrantContractForm.get("grantContractOrientationTeacherNumber")));
            if(teacher==null){
            	 return setError(request, mapping, "errors.grant.contract.orientation.teacher.not.found", null, null);	
            }

            request.setAttribute("idInternal", editGrantContractForm.get("idInternal"));
            
            //Edit Grant Contract
            Object[] args = { infoGrantContract };
            ServiceUtils.executeService(userView, "CreateOrEditGrantContract", args);

            if (infoGrantContract.getIdInternal() == null
                    || infoGrantContract.getIdInternal().equals(new Integer(0))) //In
            // case of a new contract
            {
                Object[] argcontract = { infoGrantContract.getGrantOwnerInfo().getIdInternal() };
                infoGrantContract = null;
                infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
                        "ReadLastGrantContractCreatedByGrantOwner", argcontract);
                
                
               infoGrantContractRegime.setInfoTeacher(infoGrantContract
                        .getGrantOrientationTeacherInfo().getOrientationTeacherInfo());         
                 if (infoGrantContract.getGrantCostCenterInfo()!=null){
                    infoGrantContractRegime.setGrantCostCenterInfo(infoGrantContract.getGrantCostCenterInfo());             
                    infoGrantContractRegime.setCostCenterKey(infoGrantContract.getGrantCostCenterInfo().getIdInternal());
                   }
                   infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
               
            } else {               
                if (infoGrantContract.getGrantCostCenterInfo()!=null && ((infoGrantContractRegime.getGrantCostCenterInfo().getNumber()).trim()).length()>0){
                    infoGrantContractRegime.setGrantCostCenterInfo(infoGrantContract.getGrantCostCenterInfo());
                    infoGrantContractRegime.setCostCenterKey(infoGrantContract.getGrantCostCenterInfo().getIdInternal());
                }

                infoGrantContractRegime.setInfoTeacher(infoGrantContract
                        .getGrantOrientationTeacherInfo().getOrientationTeacherInfo());  
             	infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
            	
            }
            //Edit Grant Contract Regime
            Object[] argregime = { infoGrantContractRegime };
            ServiceUtils.executeService(userView, "EditGrantContractRegime", argregime);
            return mapping.findForward("manage-grant-contract");
        } catch (GrantTypeNotFoundException e) {
            return setError(request, mapping, "errors.grant.type.not.found", null, null);
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.contract.bd.create", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    /*
     * Populates form from InfoContract
     */
    private void setFormGrantContract(DynaValidatorForm form, InfoGrantContract infoGrantContract,
            InfoGrantContractRegime infoGrantContractRegime) throws Exception {
        //Grant Owner
        form.set("idInternal", infoGrantContract.getGrantOwnerInfo().getIdInternal());

        //Grant Contract
        form.set("idGrantContract", infoGrantContract.getIdInternal());
        form.set("contractNumber", infoGrantContract.getContractNumber().toString());
        if (infoGrantContract.getEndContractMotive() != null)
            form.set("endContractMotive", infoGrantContract.getEndContractMotive());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (infoGrantContract.getDateAcceptTerm() != null)
            form.set("dateAcceptTerm", sdf.format(infoGrantContract.getDateAcceptTerm()));

        //Grant Contract Orientation teacher
        form.set("grantContractOrientationTeacherNumber", infoGrantContract
                .getGrantOrientationTeacherInfo().getOrientationTeacherInfo().getTeacherNumber()
                .toString());
        form.set("grantContractOrientationTeacherId", infoGrantContract.getGrantOrientationTeacherInfo()
                .getIdInternal());
        //Grant Contract Grant Type
        form.set("grantType", infoGrantContract.getGrantTypeInfo().getSigla());

        //Grant Contract Regime
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
       if (infoGrantContract.getGrantCostCenterInfo() != null){
            form.set("keyCostCenterNumber", infoGrantContract.getGrantCostCenterInfo().getNumber());
            form.set("grantCostCenterId", infoGrantContract.getGrantCostCenterInfo().getIdInternal());
         	 
       }
    }

    /*
     * Populates InfoGrantContract from Form
     */
    private InfoGrantContract populateInfoGrantContractFromForm(DynaValidatorForm editGrantContractForm)
            throws Exception {
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        InfoGrantOrientationTeacher orientationTeacher = new InfoGrantOrientationTeacher();
        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
        InfoGrantType infoGrantType = new InfoGrantType();
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();

        if (verifyStringParameterInForm(editGrantContractForm, "idGrantContract"))
            infoGrantContract.setIdInternal((Integer) editGrantContractForm.get("idGrantContract"));
        if (verifyStringParameterInForm(editGrantContractForm, "contractNumber"))
            infoGrantContract.setContractNumber(new Integer((String) editGrantContractForm
                    .get("contractNumber")));
        infoGrantContract.setEndContractMotive((String) editGrantContractForm.get("endContractMotive"));      
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(editGrantContractForm, "dateAcceptTerm"))
            infoGrantContract.setDateAcceptTerm(sdf.parse((String) editGrantContractForm
                    .get("dateAcceptTerm")));

        //Setting InfoGrantOwner
        infoGrantOwner.setIdInternal((Integer) editGrantContractForm.get("idInternal"));
        infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

        //Setting Grant Type
        infoGrantType.setSigla((String) editGrantContractForm.get("grantType"));
        infoGrantContract.setGrantTypeInfo(infoGrantType);
        
//      Setting Grant Cost Center

        if (verifyStringParameterInForm(editGrantContractForm, "grantCostCenterId")){
            if ((Integer)editGrantContractForm.get("grantCostCenterId")!=0)
                infoGrantCostCenter.setIdInternal((Integer) editGrantContractForm.get("grantCostCenterId"));
        }
   
        if (verifyStringParameterInForm(editGrantContractForm, "keyCostCenterNumber"))
            infoGrantCostCenter.setNumber((String) editGrantContractForm.get("keyCostCenterNumber"));
        infoGrantContract.setGrantCostCenterInfo(infoGrantCostCenter);
     	
        //Setting Grant Orientation Teacher
        if (verifyStringParameterInForm(editGrantContractForm, "dateBeginContract")) {
            orientationTeacher.setBeginDate(sdf.parse((String) editGrantContractForm
                    .get("dateBeginContract")));
        }
        if (verifyStringParameterInForm(editGrantContractForm, "dateEndContract")) {
            orientationTeacher.setEndDate(sdf.parse((String) editGrantContractForm
                    .get("dateEndContract")));
        }
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(Teacher.readByNumber(new Integer((String) editGrantContractForm.get("grantContractOrientationTeacherNumber"))));
        orientationTeacher.setOrientationTeacherInfo(infoTeacher);
//        orientationTeacher.setIdInternal((Integer) editGrantContractForm
//                .get("grantContractOrientationTeacherId"));
        infoGrantContract.setGrantOrientationTeacherInfo(orientationTeacher);

        return infoGrantContract;
    }

    /*
     * Populate InfoGrantContractRegime from Form
     */
    private InfoGrantContractRegime populateInfoGrantContractRegimeFromForm(
            DynaValidatorForm editGrantContractForm) throws Exception {
        InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        InfoGrantOrientationTeacher orientationTeacher = new InfoGrantOrientationTeacher();

        if (verifyStringParameterInForm(editGrantContractForm, "grantContractRegimeId"))
            infoGrantContractRegime.setIdInternal((Integer) editGrantContractForm
                    .get("grantContractRegimeId"));
        infoGrantContractRegime.setState(new Integer(1));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(editGrantContractForm, "dateBeginContract"))
            infoGrantContractRegime.setDateBeginContract(sdf.parse((String) editGrantContractForm
                    .get("dateBeginContract")));
        if (verifyStringParameterInForm(editGrantContractForm, "dateEndContract"))
            infoGrantContractRegime.setDateEndContract(sdf.parse((String) editGrantContractForm
                    .get("dateEndContract")));
        if (verifyStringParameterInForm(editGrantContractForm, "dateSendDispatchCC"))
            infoGrantContractRegime.setDateSendDispatchCC(sdf.parse((String) editGrantContractForm
                    .get("dateSendDispatchCC")));
        if (verifyStringParameterInForm(editGrantContractForm, "dateDispatchCC"))
            infoGrantContractRegime.setDateDispatchCC(sdf.parse((String) editGrantContractForm
                    .get("dateDispatchCC")));
        if (verifyStringParameterInForm(editGrantContractForm, "dateSendDispatchCD"))
            infoGrantContractRegime.setDateSendDispatchCD(sdf.parse((String) editGrantContractForm
                    .get("dateSendDispatchCD")));
        if (verifyStringParameterInForm(editGrantContractForm, "dateDispatchCD"))
            infoGrantContractRegime.setDateDispatchCD(sdf.parse((String) editGrantContractForm
                    .get("dateDispatchCD")));
        
        infoGrantCostCenter.setNumber((String) editGrantContractForm.get("keyCostCenterNumber"));
        infoGrantCostCenter.setIdInternal((Integer) editGrantContractForm.get("grantCostCenterId"));
        infoGrantContractRegime.setGrantCostCenterInfo(infoGrantCostCenter);
        
        
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(Teacher.readByNumber(new Integer((String) editGrantContractForm.get("grantContractOrientationTeacherNumber"))));
        orientationTeacher.setOrientationTeacherInfo(infoTeacher);
        orientationTeacher.setIdInternal((Integer) editGrantContractForm
                .get("grantContractOrientationTeacherId"));
        infoGrantContractRegime.setInfoTeacher(infoTeacher);
  
        return infoGrantContractRegime;
    }
}