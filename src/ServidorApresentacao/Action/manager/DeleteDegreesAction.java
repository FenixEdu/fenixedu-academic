package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class DeleteDegreesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, 
	                             ActionForm form,
	                             HttpServletRequest request, 
	                             HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm deleteDegreesForm = (DynaActionForm) form;

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		List degreesInternalIds = Arrays.asList((Integer[]) deleteDegreesForm.get("internalIds"));

		Object args[] = { degreesInternalIds };
		GestorServicos manager = GestorServicos.manager();
		List errorNames = new ArrayList();

		try {
			errorNames = (List) manager.executar(userView, "DeleteDegreesService", args);
			session.removeAttribute(SessionConstants.INFO_DEGREES_LIST);

		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		List allInfoDegrees;
		try {

			allInfoDegrees = (List) manager.executar(userView, "ReadDegreesService", null);

			if (!errorNames.isEmpty()) {
				ActionErrors actionErrors = new ActionErrors();
				Iterator namesIter = errorNames.iterator();
				ActionError error = null;

				while (namesIter.hasNext()) {
					//						CRIO UM ACTION ERROR PARA CADA DEGREE
					error = new ActionError("errors.invalid.delete.not.empty", (String) namesIter.next());
					actionErrors.add("errors.invalid.delete.not.empty", error);

				}
				deleteDegreesForm.set("internalIds", new Integer[] {});//COM O RESET NAO DAVA/POR ISSO FACO UM SET COM NADAPA LIMPARO"internalIds" DO FORM
				saveErrors(request, actionErrors);

			}
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(allInfoDegrees);
		request.setAttribute(SessionConstants.INFO_DEGREES_LIST, allInfoDegrees);

		return mapping.findForward("readDegrees");

	}

}
