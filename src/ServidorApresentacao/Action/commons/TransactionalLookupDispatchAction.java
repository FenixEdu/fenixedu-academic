package ServidorApresentacao.Action.commons;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import ServidorApresentacao.Action.exceptions.FenixTransactionException;

/**
 * Contains util functions for struts transaction management. 
 * @author Tânia Pousão
 * 12/Fev/2004
 */

public abstract class TransactionalLookupDispatchAction extends LookupDispatchAction {

	/**
	 * Creates a token and saves it on request
	 * @param request
	 */
	protected void createToken(HttpServletRequest request) {
		generateToken(request);
		saveToken(request);
	}

	/**
	 * If the token is valid it creates a new token @see #createToken(HttpServletRequest)
	 * Otherwise it resets the form and throws a <code> FenixTransactionException </code>
	 * @param request
	 * @param form
	 * @param mapping
	 * @param errorMessageKey
	 * @throws FenixTransactionException when the token is invalid.
	 */
	protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping, String errorMessageKey) throws FenixTransactionException {
		validateToken(request, form, mapping, errorMessageKey, true);
	}

	/**
	 * If the token is valid it creates a new token @see #createToken(HttpServletRequest)
	 * Otherwise it resets the form and throws a <code> FenixTransactionException </code>
	 * @param request
	 * @param form
	 * @param mapping
	 * @param errorMessageKey
	 * @param renewToken if false doesn't create a new token.
	 * @throws FenixTransactionException when the token is invalid.
	 */
	protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping, String errorMessageKey, boolean renewToken) throws FenixTransactionException {

		if (!isTokenValid(request)) {
			form.reset(mapping, request);
			throw new FenixTransactionException(errorMessageKey);
		} else {
			if (renewToken) {
				createToken(request);
			}				
		}
	}

	abstract protected Map getKeyMethodMap();
	
}