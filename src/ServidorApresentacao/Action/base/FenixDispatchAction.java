package ServidorApresentacao.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;

/**
 * @author joao
 */
public abstract class FenixDispatchAction extends DispatchAction
{
	/**
	 * Tests if the session is valid.
	 * 
	 * @see SessionUtils#validSessionVerification(HttpServletRequest, ActionMapping)
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		return super.execute(mapping, actionForm, request, response);
	}

	protected HttpSession getSession(HttpServletRequest request) throws InvalidSessionActionException
	{
		HttpSession result = request.getSession(false);
		if (result == null)
			throw new InvalidSessionActionException();

		return result;
	}
    
    /*
     * Sets an error to display later in the Browser and sets the mapping forward.
     */
    protected ActionForward setError(
            HttpServletRequest request,
            ActionMapping mapping,
            String errorMessage,
            String forwardPage,
            Object actionArg)
    {
        ActionErrors errors = new ActionErrors();
        String notMessageKey = errorMessage;
        ActionError error = new ActionError(notMessageKey, actionArg);
        errors.add(notMessageKey, error);
        saveErrors(request, errors);

        if (forwardPage != null)
            return mapping.findForward(forwardPage);
        else
            return mapping.getInputForward();
    }
    
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field)
    {
        if(dynaForm.get(field) != null && !dynaForm.get(field).equals(""))
            return true;
        else
        	return false;
    }
}
