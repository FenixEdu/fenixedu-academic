/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author CIIST
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PrepareInsertSectionAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSection parentSection =
			(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

		ArrayList sections;
		Object args[] = { infoSite, parentSection };
		GestorServicos manager = GestorServicos.manager();
		System.out.println("antes do servico");
		try {
			sections = 
				(ArrayList)manager.executar(
					userView,
					"ReadSectionsBySiteAndSuperiorSection",
					args);
			System.out.println("SECTIONS"+sections);
			
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		System.out.println("depois do servico");
			
		if (sections.size() != 0)
			session.setAttribute(SessionConstants.CHILDREN_SECTIONS, sections);
		else
			session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);

		return mapping.findForward("createSection");
	}
}
