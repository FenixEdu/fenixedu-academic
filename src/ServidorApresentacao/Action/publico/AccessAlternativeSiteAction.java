package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AccessAlternativeSiteAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = getSession(request);
		
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		//remove old alternative site
		session.removeAttribute(SessionConstants.ALTERNATIVE_SITE);
		session.removeAttribute(SessionConstants.MAIL);
		
		//put new alternative site
		session.setAttribute(SessionConstants.ALTERNATIVE_SITE, infoSite.getAlternativeSite());
		session.setAttribute(SessionConstants.MAIL, infoSite.getMail());

		return mapping.findForward("AlternativeSite");
	}
}