/*
 * Created on 03/Dec/2003
 */

package ServidorApresentacao.Action.grant.owner;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorApresentacao.Action.framework.SearchAction;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 */
public class SearchGrantOwnerAction extends SearchAction
{
	protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
		throws Exception
	{
		DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
		String name = (String) searchGrantOwnerForm.get("name");
		String idNumber = (String) searchGrantOwnerForm.get("idNumber");
		Integer idType = (Integer) searchGrantOwnerForm.get("idType");
		
		Boolean onlyGrantOwner = new Boolean(false);
		if(searchGrantOwnerForm.get("justGrantOwner") != null)
		{
			request.setAttribute("justGrantOwner","yes");
			onlyGrantOwner = new Boolean(true);
		}
		
		Object[] args = { name, idNumber, idType, null, onlyGrantOwner };
		return args;
	}

	protected void prepareFormConstants(
		ActionMapping mapping,
		HttpServletRequest request,
		ActionForm form)
		throws Exception
	{
		List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
		request.setAttribute("documentTypeList", documentTypeList);
	}
}