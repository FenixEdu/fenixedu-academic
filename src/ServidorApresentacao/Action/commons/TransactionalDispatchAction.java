package ServidorApresentacao.Action.commons;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.exceptions.FenixTransactionException;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class TransactionalDispatchAction extends DispatchAction {

	protected void createToken(HttpServletRequest request) {
		generateToken(request);
		saveToken(request);
	}

	protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping, String errorMessage) throws FenixTransactionException {

		if (!isTokenValid(request)) {
			form.reset(mapping, request);
			throw new FenixTransactionException(errorMessage);
		} else {
			createToken(request);
		}
	}
}