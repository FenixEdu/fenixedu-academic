package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class EditSectionDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

			HttpSession session = request.getSession();

			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

			InfoSection currentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
			List allSections = (List) session.getAttribute(SessionConstants.SECTIONS);
			
			session.removeAttribute("ALL_SECTIONS");
			
			//remove parent section, current section and all of it's daughters
			allSections.remove(currentSection.getSuperiorInfoSection());
			allSections.remove(currentSection);
			
			try {
				allSections = this.removeDaughters(userView, infoSite, currentSection, allSections);
			} catch (FenixActionException fenixActionException) {
				throw fenixActionException;
			}

			//TODO: add to SessionConstants w/different name
			session.setAttribute("ALL_SECTIONS", allSections);

			return mapping.findForward("editSection");
		}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm sectionForm = (DynaValidatorForm) form;
		String sectionName = (String) sectionForm.get("name");
		Integer order = (Integer) sectionForm.get("sectionOrder");

		HttpSession session = request.getSession();

		String parentName = (String) sectionForm.get("parentSection");

		System.out.println("parentSection: " + parentName);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		InfoSection oldSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

		InfoSection newSection = new InfoSection(sectionName, order, infoSite, oldSection.getSuperiorInfoSection());

		//perform edition
		Object editionArgs[] = { oldSection, newSection };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "EditSection", editionArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		//read sections
		Object readArgs[] = { infoSite };
		ArrayList sections;
		try {
			sections = (ArrayList) manager.executar(userView, "ReadSections", readArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(sections);
		session.setAttribute(SessionConstants.SECTIONS, sections);
			
		return mapping.findForward("viewSite");
	}
	
	private List removeDaughters(UserView userView, InfoSite infoSite, InfoSection infoSection, List allSections)
		throws FenixActionException{

		List sections = new ArrayList();
		Object args[] = { infoSite, infoSection };
		GestorServicos manager = GestorServicos.manager();
		try {
			sections = (List) manager.executar(userView, "ReadSectionsBySiteAndSuperiorSection", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		allSections.removeAll(sections);
		
		Iterator iterator = sections.iterator();
		while (iterator.hasNext()){
			InfoSection infoSection2 = (InfoSection) iterator.next();
			allSections = removeDaughters(userView, infoSite, infoSection2, allSections);
		}
		
		return allSections;
	}

	public ActionForward changeParent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

			HttpSession session = request.getSession();

			DynaActionForm sectionForm = (DynaValidatorForm) form;
			String sectionName = (String) sectionForm.get("name");
			Integer index = (Integer) sectionForm.get("index");

			List allSections = (List) session.getAttribute("ALL_SECTIONS");

			System.out.println("index: " + index + " section.getName: " + ((InfoSection)allSections.get(index.intValue())).getName());

			return mapping.findForward("prepareEditSection");
		}
}