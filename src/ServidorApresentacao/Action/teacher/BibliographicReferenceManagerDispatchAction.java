/*
 * Created on 17/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoBibliographicReference;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixLookupDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BibliographicReferenceManagerDispatchAction
	extends FenixLookupDispatchAction {

	public ActionForward createBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		DynaActionForm insertBibliographicReferenceForm = (DynaActionForm) form;
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
		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) session.getAttribute("InfoExecutionCourse");
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] =
			{ infoExecutionCourse, title, authors, reference, year, optional };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "InsertBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw e;
		} //verificar o tipo de excepção a lançar
		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward editBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		DynaActionForm editBibliographicReferenceForm = (DynaActionForm) form;

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

		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) session.getAttribute("InfoExecutionCourse");

		UserView userView = (UserView) session.getAttribute("UserView");

		InfoBibliographicReference infoBibliographicReference = null;

		infoBibliographicReference =
			(InfoBibliographicReference) session.getAttribute(
				"BibliographicReference");

		Object args[] =
			{
				infoExecutionCourse,
				infoBibliographicReference,
				infoBibliographicReferenceNew };

		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw e;
		} //verificar excepção a lançar
		return mapping.findForward("bibliographyManagement");
	}

	public ActionForward deleteBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {					

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);
		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) session.getAttribute("InfoExecutionCourse");
			

		ArrayList bibliographicReferences =
			(ArrayList) session.getAttribute("BibliographicReferences");
		
		String infoBiblioRefIndex =
			(String) request.getParameter("infoBibliographicReferenceIndex");
				
		Integer index = new Integer(infoBiblioRefIndex);		
		
		System.out.println("index"+index);
				
		InfoBibliographicReference infoBibliographicReference =
			(InfoBibliographicReference) bibliographicReferences.get(
				index.intValue());

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { infoExecutionCourse, infoBibliographicReference };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw e;
		}
		
		Object args1[] = { infoExecutionCourse, null };		
		List references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (FenixServiceException e) {
			throw e;
		}
		session.setAttribute("BibliographicReferences",references);
		//confirmar excepção a lançar*/
		return mapping.getInputForward();
	}

	public ActionForward viewBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);
		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) session.getAttribute("InfoExecutionCourse");
		/*InfoBibliographicReference infoBibliographicReference =
			(InfoBibliographicReference) session.getAttribute(
				"BibliographicReference");
				*/
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
			throw e;
		} //confirmar excepção a lançar
		session.setAttribute("BibliographicReferences", references);
		return mapping.findForward("bibliographyManagement");
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.Action.FenixLookupDispatchAction#getKeyMethodMap()
	 */
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.insert", "createBibliographicReference");
		map.put("button.delete", "deleteBibliographicReference");
		map.put("button.edit", "editBibliographicReference");
		map.put("button.view", "viewBibliographicReference");
		return map;
	}
}
