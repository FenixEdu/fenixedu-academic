/*
 * Created on 03/Dec/2003
 *
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
 * @author  Barbosa
 * @author  Pica
 * 
 */

public class SearchGrantOwnerAction extends SearchAction 
{
    /* (non-Javadoc)
     * @see ServidorApresentacao.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
        throws Exception
    {
        //Ler os dados do form bean
        DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
		String name = (String) searchGrantOwnerForm.get("name");
        String idNumber = (String) searchGrantOwnerForm.get("idNumber");
        Integer idType = (Integer) searchGrantOwnerForm.get("idType");

        Object[] args = { name,idNumber,idType, null };
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
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