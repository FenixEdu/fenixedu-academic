package ServidorApresentacao.Action;

import java.util.ArrayList;
import java.util.Collection;
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

import DataBeans.InfoRole;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.mapping.ActionMappingForAuthentication;
import Util.RoleType;

/**
 * @author jorge
 */
public class AuthenticationAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		IUserView userView = null;
		try {
			DynaActionForm authenticationForm = (DynaActionForm) form;
			ActionMappingForAuthentication authenticationMapping =
				(ActionMappingForAuthentication) mapping;

			GestorServicos gestor = GestorServicos.manager();
			Object argsAutenticacao[] =
				{
					authenticationForm.get("username"),
					authenticationForm.get("password"),
					authenticationMapping.getApplication()};

			userView =
				(IUserView) gestor.executar(
					null,
					"Autenticacao",
					argsAutenticacao);
		} catch (ExcepcaoAutenticacao e) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"invalidAuthentication",
				new ActionError("errors.invalidAuthentication"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		
		if (userView.getRoles().isEmpty()) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("errors.noAuthorization",new ActionError("errors.noAuthorization"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
		

		// Invalidate existing session if it exists
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			sessao.invalidate();
		}

		// Create a new session for this user
		sessao = request.getSession(true);

		// Store the UserView into the session and return
		sessao.setAttribute(SessionConstants.U_VIEW, userView);
		sessao.setAttribute(
			SessionConstants.SESSION_IS_VALID,
			new Boolean(true));

		Collection userRoles = userView.getRoles();
		
		int numberOfSubApplications = getNumberOfSubApplications(userRoles);
		ActionForward forwardToReturn = mapping.findForward("sucess");
		
		Iterator iterator = userRoles.iterator();
		InfoRole firstInfoRole = null;
		while (iterator.hasNext())
		{
			firstInfoRole = (InfoRole) iterator.next();
			break;
		}
		InfoRole personInfoRole = new InfoRole();
		personInfoRole.setRoleType(RoleType.PERSON);
		if (numberOfSubApplications == 1 || !userRoles.contains(personInfoRole)) {
			forwardToReturn = buildRoleForward(firstInfoRole);
		}
		return forwardToReturn;
	}

	/**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(Collection userRoles)
    {
        List subApplications = new ArrayList();
        Iterator iterator = userRoles.iterator();
        while (iterator.hasNext())
        {
            InfoRole infoRole = (InfoRole) iterator.next();
			String subApplication = infoRole.getPortalSubApplication();
            if (!subApplications.contains(subApplication) && !subApplication.equals("/teacher")){
    			subApplications.add(subApplication);
			}
        }
        return subApplications.size();
    }

    /**
	 * @param infoRole
	 * @return
	 */
	private ActionForward buildRoleForward(InfoRole infoRole) {
		ActionForward actionForward = new ActionForward();
		actionForward.setContextRelative(false);
		actionForward.setRedirect(false);
		actionForward.setPath("/dotIstPortal.do?prefix="+infoRole.getPortalSubApplication()+"&page="+infoRole.getPage());
		return actionForward;
	}
}
