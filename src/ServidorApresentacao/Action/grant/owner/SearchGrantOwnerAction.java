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
        
        //Caso sejam null, vamos verificar se os atributos tb são, pode ser um voltar!
        if(name.equals("") && (request.getParameter("sname") != null))
            name = request.getParameter("sname");
        if(idNumber.equals("")  && (request.getParameter("sidNumber") != null))
            idNumber = request.getParameter("sidNumber");
        if(idType!= null && idType.equals(new Integer(0))  && (request.getParameter("sidType") != null))
            idType = new Integer(request.getParameter("sidType"));
        if(idType == null)
            idType = new Integer(0);
        
//        request.setAttribute("name",name);
//		request.setAttribute("idNumber",idNumber);
//		request.setAttribute("idType",idType);
        Object[] args = { name,idNumber,idType };
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