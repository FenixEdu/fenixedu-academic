/*
 * Created on 17/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoBibliographicReference;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BibliographicReferenceManagerDispatchAction
	extends FenixDispatchAction {

	public ActionForward createBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		DynaActionForm insertBibliographicReferenceForm = (DynaActionForm) form;
		
		System.out.println("page depois de insert: "+insertBibliographicReferenceForm.get("page"));
		
		String title = (String) insertBibliographicReferenceForm.get("title");
		String authors =
			(String) insertBibliographicReferenceForm.get("authors");
		String reference =
			(String) insertBibliographicReferenceForm.get("reference");
		String year = (String) insertBibliographicReferenceForm.get("year");
		String optionalString =
			(String) insertBibliographicReferenceForm.get("optional");
		Boolean optional;
		if (optionalString != null) {
			optional = new Boolean(true);
		} else {
			optional = new Boolean(false);
		}

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoExecutionCourse infoExecutionCourse =
			infoSite.getInfoExecutionCourse();

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] =
			{ infoExecutionCourse, title, authors, reference, year, optional };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "InsertBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Object args1[] = { infoExecutionCourse, null };
		ArrayList references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(
			SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST,
			references);
		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward editBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		DynaActionForm editBibliographicReferenceForm = (DynaActionForm) form;
		
		System.out.println("page depois de edit: "+editBibliographicReferenceForm.get("page"));

		String title = (String) editBibliographicReferenceForm.get("title");
		String authors = (String) editBibliographicReferenceForm.get("authors");
		String reference =
			(String) editBibliographicReferenceForm.get("reference");
		String year = (String) editBibliographicReferenceForm.get("year");
		String optionalStr =
			(String) editBibliographicReferenceForm.get("optional");
		Boolean optional;

		if (optionalStr != null) {
			optional = new Boolean(true);
		} else {
			optional = new Boolean(false);
		}

		InfoBibliographicReference infoBibliographicReferenceNew =
			new InfoBibliographicReference(
				title,
				authors,
				reference,
				year,
				optional);

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoExecutionCourse infoExecutionCourse =
			infoSite.getInfoExecutionCourse();

		InfoBibliographicReference infoBibliographicReference =
			(InfoBibliographicReference) session.getAttribute(
				SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE);

		Object args[] =
			{
				infoExecutionCourse,
				infoBibliographicReference,
				infoBibliographicReferenceNew };

		UserView userView = (UserView) session.getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Object args1[] = { infoExecutionCourse, null };
		ArrayList references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(
			SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST,
			references);

		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward deleteBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoExecutionCourse infoExecutionCourse =
			infoSite.getInfoExecutionCourse();

		ArrayList bibliographicReferences =
			(ArrayList) session.getAttribute(
				SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);

		String infoBiblioRefIndex = (String) request.getParameter("index");

		Integer index = new Integer(infoBiblioRefIndex);

		InfoBibliographicReference infoBibliographicReference =
			(InfoBibliographicReference) bibliographicReferences.get(
				index.intValue());

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { infoExecutionCourse, infoBibliographicReference };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Object args1[] = { infoExecutionCourse, null };
		ArrayList references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(
			SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST,
			references);
		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward viewBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		
		DynaActionForm bibRefForm= (DynaActionForm) form;
		System.out.println("page: "+bibRefForm.get("page"));

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoExecutionCourse infoExecutionCourse =
			infoSite.getInfoExecutionCourse();

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { infoExecutionCourse, null };
		GestorServicos gestor = GestorServicos.manager();
		List references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(
			SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST,
			references);
		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward prepareEditBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaValidatorForm referenceForm = (DynaValidatorForm) form;
				
		
		String method = request.getParameter("method");
		String index = request.getParameter("index");

		System.out.println("method: " + method + " index: " + index);

		HttpSession session = request.getSession();

		ArrayList referenceList = null;
		if (index != null) {
			session.setAttribute("edit", "Editar");
			Integer indexInt = new Integer(index);
			referenceList =
				(ArrayList) session.getAttribute(
					SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);

			InfoBibliographicReference infoBibRef =
				(InfoBibliographicReference) referenceList.get(
					indexInt.intValue());

			session.setAttribute(
				SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE,
				infoBibRef);
							
			referenceForm.set("title", infoBibRef.getTitle());
			referenceForm.set("authors", infoBibRef.getAuthors());
			referenceForm.set("reference", infoBibRef.getReference());
			referenceForm.set("year", infoBibRef.getYear());						
			
			if (infoBibRef.getOptional().equals(new Boolean(true)))
				referenceForm.set("optional", "1");
			else
				referenceForm.set("optional", "0");
		} else {
			session.removeAttribute("edit");			
			session.removeAttribute("bibliographicReferenceForm");		
		}
				
		return mapping.findForward("editBibliographicReference");
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.Action.FenixLookupDispatchAction#getKeyMethodMap()
	 */
/*	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.insert", "prepareEditBibliographicReference");
		map.put("button.confirmInsert", "createBibliographicReference");
		map.put("button.delete", "deleteBibliographicReference");
		map.put("button.edit", "prepareEditBibliographicReference");
		map.put("button.confirmEdit", "editBibliographicReference");
		map.put("button.view", "viewBibliographicReference");
		return map;
	}*/
}
