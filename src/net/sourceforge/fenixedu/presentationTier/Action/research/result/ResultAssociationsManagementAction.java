package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.CreateResultUnitAssociation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.DeleteResultUnitAssociation;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.file.FileManagerException;

@Mapping(module = "researcher", path = "/result/resultAssociationsManagement", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(
				name = "editUnitAssociations",
				path = "/researcher/result/editResultUnitAssociations.jsp",
				tileProperties = @Tile(
						title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")),
		@Forward(name = "viewEditPublication", path = "/resultPublications/showPublication.do", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")),
		@Forward(name = "ListPublications", path = "/resultPublications/listPublications.do", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")),
		@Forward(name = "editPatent", path = "/resultPatents/showPatent.do", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
		@Forward(
				name = "editEventAssociations",
				path = "/researcher/result/editResultEventAssociations.jsp",
				tileProperties = @Tile(
						title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")),
		@Forward(name = "listPatents", path = "/resultPatents/management.do", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")) })
public class ResultAssociationsManagementAction extends ResultsManagementAction {

	/**
	 * Actions for Result Unit Associations
	 */
	public ActionForward prepareEditUnitAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		final ResearchResult result = getResultFromRequest(request);
		if (result == null) {
			return backToResultList(mapping, form, request, response);
		}
		setResUnitAssRequestAttributes(request, result);

		return mapping.findForward("editUnitAssociations");
	}

	public ActionForward addSugestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		ResultUnitAssociationCreationBean bean =
				(ResultUnitAssociationCreationBean) RenderUtils.getViewState("suggestion").getMetaObject().getObject();
		request.setAttribute("unitBean", bean);
		bean.setSuggestion(true);

		try {

			CreateResultUnitAssociation.run(bean);
		} catch (FileManagerException e) {
			e.printStackTrace();
			addActionMessage(request, "label.communicationError");
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		}
		request.setAttribute("result", bean.getResult());
		RenderUtils.invalidateViewState("suggestion");
		return mapping.findForward("editUnitAssociations");
	}

	public ActionForward changeTypeOfUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		ResultUnitAssociationCreationBean bean =
				(ResultUnitAssociationCreationBean) RenderUtils.getViewState("unitBean").getMetaObject().getObject();
		request.setAttribute("unitBean", bean);

		if (getFromRequest(request, "editExisting") != null) {
			request.setAttribute("editExisting", "editExisting");
		}
		final ResearchResult result = getResultFromRequest(request);
		request.setAttribute("result", result);

		RenderUtils.invalidateViewState("unitBean");
		return mapping.findForward("editUnitAssociations");
	}

	public ActionForward prepareEditUnitRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		request.setAttribute("editExisting", "editExisting");
		return prepareEditUnitAssociations(mapping, form, request, response);
	}

	public ActionForward createUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		final ResultUnitAssociationCreationBean bean = getRenderedObject("unitBean");
		bean.setSuggestion(false);
		try {

			CreateResultUnitAssociation.run(bean);
		} catch (FileManagerException e) {
			e.printStackTrace();
			addActionMessage(request, "label.communicationError");
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		}

		return prepareEditUnitAssociations(mapping, form, request, response);
	}

	public ActionForward removeUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		final Integer associationId = getRequestParameterAsInteger(request, "associationId");

		try {

			DeleteResultUnitAssociation.run(associationId);
		} catch (Exception e) {
			final ActionForward defaultForward = backToResultList(mapping, form, request, response);
			return processException(request, mapping, defaultForward, e);
		}

		return prepareEditUnitAssociations(mapping, form, request, response);
	}

	private void setResUnitAssRequestAttributes(HttpServletRequest request, ResearchResult result) throws FenixFilterException,
			FenixServiceException {
		if (getFromRequest(request, "editExisting") != null) {
			request.setAttribute("editExisting", "editExisting");
		}

		request.setAttribute("unitBean", new ResultUnitAssociationCreationBean(result));
		request.setAttribute("result", result);
		RenderUtils.invalidateViewState("unitBean");
	}
}