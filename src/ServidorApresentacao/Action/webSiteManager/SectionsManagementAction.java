package ServidorApresentacao.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoWebSite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Fernanda Quitério
 * 24/Set/2003
 * 
 */
public class SectionsManagementAction extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite webSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		try {
			Object args[] = { objectCode };
			webSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteByObjectCode", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoWebSite", webSite);
		
		return mapping.findForward("sectionsMenu");
	}
	public ActionForward getSection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite infoWebSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		try {
			Object args[] = { objectCode };
			infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteBySectionCode", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoWebSite", infoWebSite);
		request.setAttribute("objectCode", request.getParameter("objectCode"));
		
		return mapping.findForward("sectionPage");
	}
}