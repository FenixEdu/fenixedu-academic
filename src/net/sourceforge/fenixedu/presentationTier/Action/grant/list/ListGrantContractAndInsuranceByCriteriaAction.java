/*
 * Created on Jun 27, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.list;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerComplete;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanByCriteriaListGrantContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantContractAndInsuranceByCriteriaAction extends FenixDispatchAction {

    public ActionForward actionStart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm listForm = (DynaValidatorForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        request.setAttribute("grantTypeList", createGrantTypeList(userView));

        listForm.set("filterType", new Integer(1));
        return mapping.findForward("select-criteria");
    }

    public ActionForward prepareListGrantContractByCriteria(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	IUserView userView = SessionUtils.getUserView(request);
        InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner = null;
        if (verifyParameterInRequest(request, "argsInRequest")) {
            infoSpanByCriteriaListGrantOwner = populateInfoFromRequest(request);
        } else {
            DynaValidatorForm listForm = (DynaValidatorForm) form;
            infoSpanByCriteriaListGrantOwner = populateInfoFromForm(listForm);
        }

        if (infoSpanByCriteriaListGrantOwner.getTotalElements() != null
                && infoSpanByCriteriaListGrantOwner.getSpanNumber().intValue() > infoSpanByCriteriaListGrantOwner
                        .getNumberOfSpans().intValue()) {
            return setError(request, mapping, "errors.grant.list.invalidSpan", "select-criteria", null);
        }
        
        if (infoSpanByCriteriaListGrantOwner.getValidToTheDate() == null || infoSpanByCriteriaListGrantOwner.getValidToTheDate().equals("")){
	       
        	if (infoSpanByCriteriaListGrantOwner.getBeginContract() != null && !infoSpanByCriteriaListGrantOwner.getBeginContract().equals("") ){
	       
	                if (infoSpanByCriteriaListGrantOwner.getEndContract() == null ){
			    		request.setAttribute("grantTypeList", createGrantTypeList(userView));
			    		return setError(request, mapping, "errors.grant.list.beginDateNotNullAndEndNull", "select-criteria",
			               null);
	    			 }else if (infoSpanByCriteriaListGrantOwner.getEndContract().equals("")){
	    				 request.setAttribute("grantTypeList", createGrantTypeList(userView));
	 		    		 return setError(request, mapping, "errors.grant.list.beginDateNotNullAndEndNull", "select-criteria",
	 		               null);
	    			 }
	    	}
	        if (infoSpanByCriteriaListGrantOwner.getBeginContract() == null || infoSpanByCriteriaListGrantOwner.getBeginContract().equals("") ){
	            
	            if (infoSpanByCriteriaListGrantOwner.getEndContract() != null && !infoSpanByCriteriaListGrantOwner.getEndContract().equals("")){
		    		request.setAttribute("grantTypeList", createGrantTypeList(userView));
		    		return setError(request, mapping, "errors.grant.list.beginDateNullAndEndNotNull", "select-criteria",
		               null);
	            }
				
	        }
	        if (infoSpanByCriteriaListGrantOwner.getBeginContract() != null
	                && infoSpanByCriteriaListGrantOwner.getEndContract() != null
	                && infoSpanByCriteriaListGrantOwner.getBeginContract().after(
	                        infoSpanByCriteriaListGrantOwner.getEndContract())) {
	            return setError(request, mapping, "errors.grant.list.beginDateBeforeEnd", "select-criteria",
	                    null);
	        }
        }
        
        if(infoSpanByCriteriaListGrantOwner.getValidToTheDate() != null && !infoSpanByCriteriaListGrantOwner.getValidToTheDate().equals("")){
        	
        	if(infoSpanByCriteriaListGrantOwner.getBeginContract() != null 
        			&& !infoSpanByCriteriaListGrantOwner.getBeginContract().equals("")){
        		request.setAttribute("grantTypeList", createGrantTypeList(userView));
	    		return setError(request, mapping, "errors.grant.list.beginDateNotPossible", "select-criteria",
	               null);
        	}
        	if(infoSpanByCriteriaListGrantOwner.getEndContract() != null 
        			&& !infoSpanByCriteriaListGrantOwner.getEndContract().equals("")){
        		request.setAttribute("grantTypeList", createGrantTypeList(userView));
	    		return setError(request, mapping, "errors.grant.list.beginDateNotPossible", "select-criteria",
	               null);
        	}
        	
        }
         
//        if (infoSpanByCriteriaListGrantOwner.getBeginContract() != null
//                && infoSpanByCriteriaListGrantOwner.getEndContract() != null
//                && infoSpanByCriteriaListGrantOwner.getBeginContract().after(
//                        infoSpanByCriteriaListGrantOwner.getEndContract())) {
//            return setError(request, mapping, "errors.grant.list.beginDateBeforeEnd", "select-criteria",
//                    null);
//        }
        return listGrantContract(mapping, request, form, response, infoSpanByCriteriaListGrantOwner);
    }

    private ActionForward listGrantContract(ActionMapping mapping, HttpServletRequest request,
            ActionForm form, HttpServletResponse response,
            InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner) {

        try {
            IUserView userView = SessionUtils.getUserView(request);

            // Read the grant contracts
            Object[] args = { infoSpanByCriteriaListGrantOwner };
 

            Object[] result = (Object[]) ServiceUtils.executeService(userView,
                    "ListGrantContractAndInsuranceByCriteria", args);

            List listGrantContracts = (List) result[0];
     
            infoSpanByCriteriaListGrantOwner = (InfoSpanByCriteriaListGrantContract) result[1];
            if (listGrantContracts != null && listGrantContracts.size() != 0) {
                // Setting the request
                DynaValidatorForm listForm = (DynaValidatorForm) form;
                setForm(listForm, infoSpanByCriteriaListGrantOwner);
                request.setAttribute("listGrantContract", listGrantContracts);

                if (infoSpanByCriteriaListGrantOwner.hasBeforeSpan()) {
                    request.setAttribute("beforeSpan", infoSpanByCriteriaListGrantOwner.getBeforeSpan());
                }
                if (infoSpanByCriteriaListGrantOwner.hasAfterSpan()) {
                    request.setAttribute("afterSpan", infoSpanByCriteriaListGrantOwner.getAfterSpan());
                }
                request.setAttribute("numberOfSpans", infoSpanByCriteriaListGrantOwner
                        .getNumberOfSpans());
            } else {
                request.setAttribute("grantTypeList", createGrantTypeList(userView));
                return setError(request, mapping, "errors.grant.list.noResults", "select-criteria", null);
            }
            return mapping.findForward("list-byCriteria-grant-contract-and-insurance");
        } catch (FenixServiceException e) {
        	e.printStackTrace();
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        } catch (Exception e) {
        	e.printStackTrace();
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }

    public ActionForward showGrantOwner(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer grantOwnerId = null;
        if (verifyParameterInRequest(request, "grantOwnerId")) {
            grantOwnerId = new Integer(request.getParameter("grantOwnerId"));
        }

        IUserView userView = SessionUtils.getUserView(request);

        if (grantOwnerId != null) {
            try {
                // Read all the information about the grant owner
                Object[] args = { grantOwnerId };
                InfoListGrantOwnerComplete listGrantOwnerCompleteInfo = (InfoListGrantOwnerComplete) ServiceUtils
                        .executeService(userView, "ShowGrantOwner", args);

                if (listGrantOwnerCompleteInfo != null) {
                    // Set the request
                    request.setAttribute("infoGrantOwner", listGrantOwnerCompleteInfo
                            .getInfoGrantOwner());
                    request.setAttribute("infoQualificationList", listGrantOwnerCompleteInfo
                            .getInfoQualifications());
                    request.setAttribute("infoListGrantContractList", listGrantOwnerCompleteInfo
                            .getInfoListGrantContracts());
                }
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.unrecoverable", "show-grant-owner", null);
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable", "show-grant-owner", null);
            }
        } else {
            return setError(request, mapping, "errors.grant.unrecoverable", "show-grant-owner", null);
        }
        return mapping.findForward("show-grant-owner");
    }

    private void setForm(DynaValidatorForm form,
            InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner) throws Exception {
        form.set("spanNumber", infoSpanByCriteriaListGrantOwner.getSpanNumber());
        form.set("totalElements", infoSpanByCriteriaListGrantOwner.getTotalElements());
        form.set("orderBy", infoSpanByCriteriaListGrantOwner.getOrderBy());
       
        if (infoSpanByCriteriaListGrantOwner.getJustActiveContract().booleanValue()) {
            form.set("filterType", new Integer(2));
        } else if (infoSpanByCriteriaListGrantOwner.getJustActiveContract().booleanValue()) {
            form.set("filterType", new Integer(3));
        } else {
            form.set("filterType", new Integer(1));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (infoSpanByCriteriaListGrantOwner.getBeginContract() != null)
            form.set("beginContract", sdf.format(infoSpanByCriteriaListGrantOwner.getBeginContract()));
        if (infoSpanByCriteriaListGrantOwner.getEndContract() != null)
            form.set("endContract", sdf.format(infoSpanByCriteriaListGrantOwner.getEndContract()));
        if (infoSpanByCriteriaListGrantOwner.getValidToTheDate() != null)
            form.set("validToTheDate", sdf.format(infoSpanByCriteriaListGrantOwner.getValidToTheDate()));
        if (infoSpanByCriteriaListGrantOwner.getGrantTypeId() != null)
            form.set("grantTypeId", infoSpanByCriteriaListGrantOwner.getGrantTypeId());
        else
            form.set("grantTypeId", new Integer(0));
    }

    private InfoSpanByCriteriaListGrantContract populateInfoFromForm(DynaValidatorForm form)
            throws Exception {
        InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner = new InfoSpanByCriteriaListGrantContract();

        infoSpanByCriteriaListGrantOwner.setSpanNumber((Integer) form.get("spanNumber"));
        if (verifyStringParameterInForm(form, "totalElements")) {
            infoSpanByCriteriaListGrantOwner.setTotalElements((Integer) form.get("totalElements"));
        }
        infoSpanByCriteriaListGrantOwner.setOrderBy((String) form.get("orderBy"));

        infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(false));
        infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(false));
        Integer filter = (Integer) form.get("filterType");
        if (filter.equals(new Integer(2))) {
            infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(true));
        } else if (filter.equals(new Integer(3))) {
            infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(true));
        }

      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       
        if (verifyStringParameterInForm(form, "beginContract")) {
            infoSpanByCriteriaListGrantOwner.setBeginContract(sdf.parse((String) form
                    .get("beginContract")));
        }
        if (verifyStringParameterInForm(form, "endContract")) {
            infoSpanByCriteriaListGrantOwner.setEndContract(sdf.parse((String) form.get("endContract")));
        }
        if (verifyStringParameterInForm(form, "validToTheDate")) {
            infoSpanByCriteriaListGrantOwner.setValidToTheDate(sdf.parse((String)form.get("validToTheDate")));
        }
        Integer grantTypeId = (Integer) form.get("grantTypeId");
        if (!grantTypeId.equals(new Integer(0))) {
            infoSpanByCriteriaListGrantOwner.setGrantTypeId(grantTypeId);
        }
        return infoSpanByCriteriaListGrantOwner;
    }

    private InfoSpanByCriteriaListGrantContract populateInfoFromRequest(HttpServletRequest request)
            throws Exception {

        InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner = new InfoSpanByCriteriaListGrantContract();

        infoSpanByCriteriaListGrantOwner.setSpanNumber(new Integer(request.getParameter("spanNumber")));
        infoSpanByCriteriaListGrantOwner.setTotalElements(new Integer(request
                .getParameter("totalElements")));
        infoSpanByCriteriaListGrantOwner.setOrderBy(request.getParameter("orderBy"));

        infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(false));
        infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(false));
        String filter = request.getParameter("filterType");
        if (filter.equals("2")) {
            infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(true));
        } else if (filter.equals("3")) {
            infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(true));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyParameterInRequest(request, "beginContract")) {
            infoSpanByCriteriaListGrantOwner.setBeginContract(sdf.parse(request
                    .getParameter("beginContract")));
        }
        if (verifyParameterInRequest(request, "endContract")) {
            infoSpanByCriteriaListGrantOwner.setEndContract(sdf.parse(request
                    .getParameter("endContract")));
        }
        if (verifyParameterInRequest(request, "validToTheDate")) {
            infoSpanByCriteriaListGrantOwner.setValidToTheDate(sdf.parse(request
                    .getParameter("validToTheDate")));
        }

        Integer grantType = new Integer(request.getParameter("grantTypeId"));
        if (!grantType.equals(new Integer(0))) {
            infoSpanByCriteriaListGrantOwner.setGrantTypeId(grantType);
        }
        return infoSpanByCriteriaListGrantOwner;
    }

    private List createGrantTypeList(IUserView userView) throws FenixServiceException,
            FenixFilterException {
        // Read grant types for the contract
        Object[] args = {};
        List grantTypeList = (List) ServiceUtils.executeService(userView, "ReadAllGrantTypes", args);
        // Adding a select country line to the list (presentation reasons)
        InfoGrantType grantType = new InfoGrantType();
        grantType.setIdInternal(null);
        grantType.setSigla("[Escolha um tipo de bolsa]");
        grantTypeList.add(0, grantType);
        return grantTypeList;
    }

}