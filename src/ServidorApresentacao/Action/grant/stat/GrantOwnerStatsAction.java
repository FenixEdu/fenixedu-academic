/*
 * Created on Jul 5, 2004
 *
 */
package ServidorApresentacao.Action.grant.stat;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.stat.InfoStatGrantOwner;
import Dominio.grant.contract.GrantType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantOwnerStatsAction extends FenixDispatchAction {

    public ActionForward actionStart(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
        IUserView userView = SessionUtils.getUserView(request);
		try
		{
			//Read grant types for the contract
			Object[] args = { };
			List grantTypeList = (List) ServiceUtils.executeService(userView, "ReadAllGrantTypes", args);			
			//Adding a select country line to the list (presentation reasons)
			GrantType grantType = new GrantType();
			grantType.setIdInternal(null);
			grantType.setSigla("[Escolha um tipo de bolsa]");
			grantTypeList.add(0, grantType);

			request.setAttribute("grantTypeList", grantTypeList);
			
			((DynaValidatorForm) form).set("filterType", new Integer(1));
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.type.read", "manage-grant-contract", null);
		}        
		return mapping.findForward("grantowner-stats-options");
	}
    
    public ActionForward doStat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
        InfoStatGrantOwner infoStatGrantOwner = populateInfoFromForm((DynaValidatorForm) form);

        if(infoStatGrantOwner.getDateBeginContract() != null &&
           infoStatGrantOwner.getDateEndContract() != null &&
           infoStatGrantOwner.getDateBeginContract().after(infoStatGrantOwner.getDateEndContract())) {
            return setError(request, mapping, "errors.grant.stat.beginDateBeforeEnd", null, null);
        }
        
		IUserView userView = SessionUtils.getUserView(request);
		Object[] args = { infoStatGrantOwner };
		Object[] result = (Object[])ServiceUtils.executeService(userView, "CalculateStatGrantOwnerByCriteria", args);

		//Set the request with the variables
		Integer filterType = new Integer(1);
		if(infoStatGrantOwner.getJustActiveContracts().booleanValue()) {
		    filterType = new Integer(2);
		} else if (infoStatGrantOwner.getJustInactiveContracts().booleanValue()) {
		    filterType = new Integer(3);
		}
        request.setAttribute("filterType", filterType);
        request.setAttribute("infoStatResultGrantOwner", result[0]);
		request.setAttribute("infoStatGrantOwner", result[1]);

		return mapping.findForward("grantowner-stats-results");
	}

    private InfoStatGrantOwner populateInfoFromForm(DynaValidatorForm form) throws Exception {
		InfoStatGrantOwner infoStatGrantOwner = new InfoStatGrantOwner();

		infoStatGrantOwner.setJustActiveContracts(new Boolean(false));
		infoStatGrantOwner.setJustInactiveContracts(new Boolean(false));
		Integer filter = (Integer) form.get("filterType");
		if (filter.equals(new Integer(2))) {
			infoStatGrantOwner.setJustActiveContracts(new Boolean(true));
		} else if (filter.equals(new Integer(3))) {
			infoStatGrantOwner.setJustInactiveContracts(new Boolean(true));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (verifyStringParameterInForm(form, "beginContract")) {
			infoStatGrantOwner.setDateBeginContract(sdf.parse((String) form.get("beginContract")));
		}
		if (verifyStringParameterInForm(form, "endContract")) {
			infoStatGrantOwner.setDateEndContract(sdf.parse((String) form.get("endContract")));
		}

		Integer grantType = (Integer) form.get("grantType");
		if(grantType != null && !grantType.equals(new Integer(0))) {
			infoStatGrantOwner.setGrantType(grantType);
		}		
		return infoStatGrantOwner;
	}
}
