/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
public class InsertSectionAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String sectionName = (String) dynaForm.get("name");
		String order = (String) dynaForm.get("sectionOrder");

		HttpSession session = request.getSession();

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSection parentSection =
			(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

		Integer sectionOrder = new Integer(0);
		ArrayList childrenSections =
			(ArrayList) session.getAttribute(
				SessionConstants.CHILDREN_SECTIONS);
		if (childrenSections != null) {
			Iterator iterator = childrenSections.iterator();
			while (iterator.hasNext()) {
				InfoSection infoSection = (InfoSection) iterator.next();
				if (infoSection.getName().equals(order)) {
					sectionOrder = infoSection.getSectionOrder();
					break;
				}
			}
		}

		if (parentSection == null) {

			InfoSection infoSection =
				new InfoSection(sectionName, sectionOrder, infoSite);

			Object args[] = { infoSection };
			GestorServicos manager = GestorServicos.manager();
			try {
				manager.executar(userView, "InsertSection", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}
			Object args1[] = { infoSite };
			ArrayList sections;
			try {
				sections =
					(ArrayList) manager.executar(
						userView,
						"ReadSections",
						args1);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}

			Collections.sort(sections);
			session.setAttribute(SessionConstants.SECTIONS, sections);

			return mapping.findForward("viewSite");

		} else {

			InfoSection infoSection =
				new InfoSection(
					sectionName,
					sectionOrder,
					infoSite,
					parentSection);

			Object args[] = { infoSection };
			GestorServicos manager = GestorServicos.manager();
			try {
				manager.executar(userView, "InsertSection", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}

			Object args1[] = { infoSite };
			ArrayList sections;
			try {
				sections =
					(ArrayList) manager.executar(
						userView,
						"ReadSections",
						args1);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}

			Collections.sort(sections);
			session.setAttribute(SessionConstants.SECTIONS, sections);
			return mapping.findForward("viewSite");
		}
	}
}
