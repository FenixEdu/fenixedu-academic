/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import DataBeans.gesdis.InfoTeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadSite extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = getSession(request);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoTeacher infoTeacher =
			(InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);
		List infoSites =
			(List) session.getAttribute(SessionConstants.INFO_SITES_LIST);

		InfoSite site = null;
		String index = (String) request.getParameter("index");
		if (index != null) {

			site = (InfoSite) infoSites.get((new Integer(index)).intValue());
		} else {
			site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		}
		session.setAttribute(SessionConstants.INFO_SITE, site);
		session.setAttribute(
			SessionConstants.ALTERNATIVE_SITE,
			site.getAlternativeSite());
		session.setAttribute(SessionConstants.MAIL, site.getMail());
		
		//Read last Anouncement
		Object args[] = new Object[1];
		args[0] = site;

		InfoAnnouncement lastAnnouncement = null;
		GestorServicos manager = GestorServicos.manager();
		try {
			lastAnnouncement =
				(InfoAnnouncement) manager.executar(
					userView,
					"ReadLastAnnouncement",
					args);
			session.setAttribute(
				SessionConstants.LAST_ANNOUNCEMENT,
				lastAnnouncement);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}


		// read sections		
		ArrayList sections = null;
		try {
			sections =
				(ArrayList) manager.executar(
					userView,
					"ReadSections",
					args);
			Collections.sort(sections);
			session.setAttribute(
				SessionConstants.SECTIONS,
				sections);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return mapping.findForward("viewSite");
	}

}
