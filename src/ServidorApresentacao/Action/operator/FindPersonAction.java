/*
 * Created on 22/Dez/2003
 *
 */
package ServidorApresentacao.Action.operator;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *
 */
public class FindPersonAction extends FenixDispatchAction
{
	public ActionForward prepareFindPerson(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{		
		return mapping.findForward("choosePerson");
	}
	
	public ActionForward findPerson(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{		
		ActionErrors errors = new ActionErrors();
		
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm findPersonForm = (DynaActionForm) actionForm;
		String username = null;		
		if(findPersonForm.get("username") != null) {
		    username = (String) findPersonForm.get("username");
		}
		String documentIdNumber = null;		
		if(findPersonForm.get("documentIdNumber") != null) {
		    documentIdNumber = (String) findPersonForm.get("documentIdNumber");
		}
		
		HashMap parametersSearch = new HashMap();
		parametersSearch.put(new String("name"), putSearchChar(null));
		parametersSearch.put(new String("email"), putSearchChar(null));
		parametersSearch.put(new String("username"), putSearchChar(username));
		parametersSearch.put(new String("documentIdNumber"), putSearchChar(documentIdNumber));
		
		Object[] args = { parametersSearch };
		
		List personListFinded = null;
		try {
			personListFinded = (List) ServiceManagerServiceFactory.executeService(userView, "SearchPerson", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
		}
		if(personListFinded == null || personListFinded.size() <= 0) {
			errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
		}
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
				
		request.setAttribute("personListFinded", personListFinded);
		return mapping.findForward("confirmPasswordChange");
	}

	private String putSearchChar(String searchElem) {		
		String newSearchElem = null;
		if(searchElem != null) {
			newSearchElem = "%".concat(searchElem.replace(' ', '%')).concat("%");
		}
		return newSearchElem;
	}
}
