package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.PersistentGroupMembersBean;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileBean;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileUploadBean;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class UnitFunctionalities extends FenixDispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setAttribute("unit", getUnit(request));
		return super.execute(mapping, form, request, response);
	}

	public ActionForward configureGroups(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Unit unit = getUnit(request);
		request.setAttribute("groups", unit.getPersistentGroups());

		return mapping.findForward("managePersistedGroups");
	}

	public ActionForward prepareCreatePersistedGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("bean", getNewPersistentGroupBean(request));
		return mapping.findForward("createPersistedGroup");
	}

	public ActionForward createPersistedGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		IViewState viewState = RenderUtils.getViewState("createGroup");
		if (viewState != null) {
			PersistentGroupMembersBean bean = (PersistentGroupMembersBean) viewState.getMetaObject()
					.getObject();
			executeService("CreatePersistentGroup", new Object[] { bean.getUnit(), bean.getName(),
					bean.getPeople(), bean.getType() });
		}

		return configureGroups(mapping, form, request, response);
	}

	public ActionForward deletePersistedGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PersistentGroupMembers group = getGroup(request);
		if (group != null) {
			try {
				executeService("DeletePersistentGroup", new Object[] { group });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}
		return configureGroups(mapping, form, request, response);
	}

	public ActionForward prepareEditPersistedGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("bean", new PersistentGroupMembersBean(getGroup(request)));
		return mapping.findForward("editPersistedGroup");
	}

	public ActionForward editPersistedGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		IViewState viewState = RenderUtils.getViewState("editGroup");
		if (viewState != null) {
			try {
				PersistentGroupMembersBean bean = (PersistentGroupMembersBean) viewState.getMetaObject()
						.getObject();
				executeService("EditPersistentGroup", new Object[] { bean.getGroup(), bean.getName(),
						bean.getPeople(), bean.getUnit() });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}
		return configureGroups(mapping, form, request, response);
	}

	public ActionForward prepareFileUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UnitFileUploadBean bean = new UnitFileUploadBean(getUnit(request));
		request.setAttribute("fileBean", bean);

		return mapping.findForward("uploadFile");

	}

	public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		IViewState viewState = RenderUtils.getViewState("upload");
		if (viewState == null) {
			return prepareFileUpload(mapping, form, request, response);
		}

		UnitFileUploadBean bean = (UnitFileUploadBean) viewState.getMetaObject().getObject();
		RenderUtils.invalidateViewState();

		InputStream formFileInputStream = null;
		File file = null;
		try {
			formFileInputStream = bean.getFile();
			file = FileUtils.copyToTemporaryFile(formFileInputStream);
			executeService("CreateUnitFile", new Object[] { file, bean.getFileName(),
					bean.getDisplayName(), bean.getDescription(), bean.getTags(),
					bean.getPermittedGroup(), getUnit(request), getLoggedPerson(request) });
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
		} finally {
			file.delete();
		}
		return manageFiles(mapping, form, request, response);
	}

	public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UnitFile file = getUnitFile(request);
		if (file != null) {
			executeService("DeleteUnitFile", new Object[] { file });
		}
		return manageFiles(mapping, form, request, response);
	}

	private ActionForward putFilesOnRequest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, List<UnitFile> files)
			throws Exception {
		int numberOfPages = files.size() / getPageSize();
		numberOfPages += (files.size() % getPageSize() != 0) ? 1 : 0;
		request.setAttribute("numberOfPages", numberOfPages);
		String page = request.getParameter("page");
		int pageNumber = 1;
		if (page != null) {
			pageNumber = Integer.valueOf(page);
		}
		request.setAttribute("page", pageNumber);
		int start = (pageNumber - 1) * getPageSize();
		request.setAttribute("files", files
				.subList(start, Math.min(start + getPageSize(), files.size())));
		return mapping.findForward("manageFiles");
	}

	public ActionForward manageFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return putFilesOnRequest(mapping, form, request, response, getUnit(request).getAccessibileFiles(
				getLoggedPerson(request)));

	}

	public ActionForward viewFilesByTag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tagName = request.getParameter("tagName");
		if (tagName != null) {
			request.setAttribute("tagName", tagName);
			return putFilesOnRequest(mapping, form, request, response, getUnit(request)
					.getAccessibileFiles(getLoggedPerson(request), tagName));
		} else {
			return manageFiles(mapping, form, request, response);
		}

	}

	public ActionForward prepareEditFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UnitFile file = getUnitFile(request);
		if (file != null && file.getUploader().equals(getLoggedPerson(request))) {
			request.setAttribute("file", new UnitFileBean(file));
		}
		return mapping.findForward("editFile");
	}

	public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		IViewState viewState = RenderUtils.getViewState("editFile");
		if (viewState != null) {
			UnitFileBean bean = (UnitFileBean) viewState.getMetaObject().getObject();
			executeService("EditUnitFile", bean.getFile(), bean.getName(), bean.getDescription(), bean
					.getTags(), bean.getGroup());
		}
		return manageFiles(mapping, form, request, response);
	}

	protected abstract Integer getPageSize();

	protected abstract UnitFile getUnitFile(HttpServletRequest request);

	protected abstract Unit getUnit(HttpServletRequest request);

	protected abstract PersistentGroupMembers getGroup(HttpServletRequest request);

	protected abstract PersistentGroupMembersBean getNewPersistentGroupBean(HttpServletRequest request);
}