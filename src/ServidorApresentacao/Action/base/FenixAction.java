package ServidorApresentacao.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.ExcepcaoSessaoInexistente;

/**
 * @author jorge
 */
public abstract class FenixAction extends Action {

	protected HttpSession getSession(HttpServletRequest request)
		throws ExcepcaoSessaoInexistente {
		HttpSession result = request.getSession(false);
		if (result == null)
			throw new ExcepcaoSessaoInexistente();

		return result;
	}
	
	/**
	 * 
	 * @see SessionUtils#validSessionVerification(HttpServletRequest, ActionMapping)
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		return super.execute(mapping, actionForm, request, response);
	}

}
