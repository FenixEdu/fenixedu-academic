package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoWebSiteSection;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;

/**
 * @author Fernanda Quitério
 * 02/09/2003
 * 
 */
public class ViewWebSiteSectionAction extends FenixContextDispatchAction {

	public ActionForward viewLimitedSection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		String sectionName = request.getParameter("sectionName");

		System.out.println("nome da seccao: " + sectionName);
		Object[] args = { sectionName };
		GestorServicos gestorServicos = GestorServicos.manager();
		InfoWebSiteSection infoWebSiteSection = null;
		try {
			infoWebSiteSection = (InfoWebSiteSection) gestorServicos.executar(null, "ReadLimitedWebSiteSectionByName", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExisting", sectionName);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		request.setAttribute("infoWebSiteSection", infoWebSiteSection);
		return mapping.findForward("viewWebSiteSection");
	}
}