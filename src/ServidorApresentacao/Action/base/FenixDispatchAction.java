package ServidorApresentacao.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;

/**
 * @author joao
 */
public abstract class FenixDispatchAction extends DispatchAction {
	/**
	 * Tests if the session is valid.
	 * @see SessionUtils#validSessionVerification(HttpServletRequest, ActionMapping)
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		return super.execute(mapping, actionForm, request, response);
	}

  protected HttpSession getSession(HttpServletRequest request) 
      throws InvalidSessionActionException {
    HttpSession result = request.getSession(false);
    if (result == null)
      throw new InvalidSessionActionException();
    
    return result;
  }
}
