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

	/**
	 * Tests if the session is valid.
	 * 
	 * @see SessionUtils#validSessionVerification(HttpServletRequest, ActionMapping)
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
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
        {
            return mapping.findForward(forwardPage);
        }
        
            return mapping.getInputForward();
        
    }
    
    /*
     * Verifies if a property of type String in a FormBean is not empty. 
     * Returns true if the field is present and not empty. False otherwhise.
     */
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field)
    {
        if(dynaForm.get(field) != null && !dynaForm.get(field).equals(""))
        {
            return true;
        }
        
        	return false;
        
    }
    
    /*
     * Verifies if a parameter in a Http Request is not empty.
     * Return true if the field is not empty. False otherwise. 
     */
    protected boolean verifyParameterInRequest(HttpServletRequest request, String field)
    {
    	if(request.getParameter(field) != null && !request.getParameter(field).equals(""))
    	{
    		return true;
    	}
    	
    		return false;
    	
    }
}
