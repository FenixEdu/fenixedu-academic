package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.research.CreatePersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.research.CreateUnitFile;
import net.sourceforge.fenixedu.applicationTier.Servico.research.DeletePersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.research.DeleteUnitFile;
import net.sourceforge.fenixedu.applicationTier.Servico.research.EditPersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.research.EditUnitFile;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.PersistentGroupMembersBean;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileBean;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileUploadBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class UnitFunctionalities extends FenixDispatchAction {

    /**
     * Default page size for the unit's files list.
     */
    protected static final int PAGE_SIZE = 20;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("unit", getUnit(request));
        return super.execute(mapping, form, request, response);
    }

    public ActionForward configureGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Unit unit = getUnit(request);
        request.setAttribute("groups", unit.getPersistentGroups());

        return mapping.findForward("managePersistedGroups");
    }

    public ActionForward prepareCreatePersistedGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("bean", getNewPersistentGroupBean(request));
        return mapping.findForward("createPersistedGroup");
    }

    public ActionForward createPersistedGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("createGroup");
        if (viewState != null) {
            PersistentGroupMembersBean bean = (PersistentGroupMembersBean) viewState.getMetaObject().getObject();
            if (bean.getIstId() != null) {
                bean.getPeople().add(bean.getIstId());
            }
            CreatePersistentGroup.runCreatePersistentGroup(bean.getUnit(), bean.getName(), bean.getPeople(), bean.getType());
        }

        return configureGroups(mapping, form, request, response);
    }

    public ActionForward deletePersistedGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PersistentGroupMembers group = getGroup(request);
        if (group != null) {
            try {
                DeletePersistentGroup.runDeletePersistentGroup(group);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }
        return configureGroups(mapping, form, request, response);
    }

    public ActionForward prepareEditPersistedGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new PersistentGroupMembersBean(getGroup(request)));
        return mapping.findForward("editPersistedGroup");
    }

    public ActionForward editPersistedGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("editGroup");
        if (viewState != null) {
            try {
                PersistentGroupMembersBean bean = (PersistentGroupMembersBean) viewState.getMetaObject().getObject();
                if (bean.getIstId() != null) {
                    bean.getPeople().add(bean.getIstId());
                }
                EditPersistentGroup.runEditPersistentGroup(bean.getGroup(), bean.getName(), bean.getPeople(), bean.getUnit());
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }
        return configureGroups(mapping, form, request, response);
    }

    public ActionForward prepareFileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

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

        if (!bean.getUnit().isCurrentUserAllowedToUploadFiles()) {
            return manageFiles(mapping, form, request, response);
        }

        InputStream formFileInputStream = null;
        File file = null;
        try {
            formFileInputStream = bean.getUploadFile();
            file = FileUtils.copyToTemporaryFile(formFileInputStream);
            CreateUnitFile.run(file, bean.getFileName(), bean.getName(), bean.getDescription(), bean.getTags(),
                    bean.getPermittedGroup(), getUnit(request), getLoggedPerson(request));
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
        if (file != null && file.getUnit().isCurrentUserAllowedToUploadFiles()) {
            DeleteUnitFile.run(file);
        }
        return manageFiles(mapping, form, request, response);
    }

    private ActionForward putFilesOnRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, List<UnitFile> unsortedList) throws Exception {
        List<UnitFile> files = RenderUtils.sortCollectionWithCriteria(unsortedList, request.getParameter("sort"));
        int numberOfPages = files.size() / getPageSize();
        numberOfPages += (files.size() % getPageSize() != 0) ? 1 : 0;
        request.setAttribute("numberOfPages", numberOfPages);
        String page = request.getParameter("filePage");
        int pageNumber = 1;
        if (page != null) {
            pageNumber = Integer.valueOf(page);
        }
        request.setAttribute("filePage", pageNumber);
        int start = (pageNumber - 1) * getPageSize();
        request.setAttribute("files", files.subList(start, Math.min(start + getPageSize(), files.size())));
        return mapping.findForward("manageFiles");
    }

    public ActionForward manageFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return putFilesOnRequest(mapping, form, request, response, getUnit(request).getAccessibileFiles(getLoggedPerson(request)));

    }

    public ActionForward viewFilesByTag(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String tagName = request.getParameter("selectedTags");
        Set<UnitFileTag> unitFileTags = new HashSet<UnitFileTag>();

        if (tagName != null && tagName.length() > 0) {
            String[] tags = tagName.split("\\p{Space}+");
            for (String tag2 : tags) {
                UnitFileTag tag = getUnit(request).getUnitFileTag(tag2);
                if (tag != null) {
                    unitFileTags.add(tag);
                }
            }
            return putFilesOnRequest(mapping, form, request, response,
                    getUnit(request).getAccessibileFiles(getLoggedPerson(request), unitFileTags));
        }

        else {
            return manageFiles(mapping, form, request, response);
        }

    }

    public ActionForward prepareEditFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UnitFile file = getUnitFile(request);
        if (file != null && file.getUploader().equals(getLoggedPerson(request))) {
            request.setAttribute("file", new UnitFileBean(file));
        }
        return mapping.findForward("editFile");
    }

    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IViewState viewState = RenderUtils.getViewState("editFile");
        if (viewState != null) {
            UnitFileBean bean = (UnitFileBean) viewState.getMetaObject().getObject();
            EditUnitFile.run(bean.getFile(), bean.getName(), bean.getDescription(), bean.getTags(), bean.getGroup());
        }
        return manageFiles(mapping, form, request, response);
    }

    public ActionForward configureUploaders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("editUploaders");
    }

    protected Integer getPageSize() {
        return PAGE_SIZE;
    }

    protected UnitFile getUnitFile(HttpServletRequest request) {
        Integer id = getIdInternal(request, "fid");
        return (UnitFile) RootDomainObject.getInstance().readFileByOID(id);
    }

    protected Unit getUnit(HttpServletRequest request) {
        Integer id = getIdInternal(request, "unitId");
        return (Unit) RootDomainObject.getInstance().readPartyByOID(id);
    }

    protected PersistentGroupMembers getGroup(HttpServletRequest request) {
        Integer id = getIdInternal(request, "groupId");
        return RootDomainObject.getInstance().readPersistentGroupMembersByOID(id);
    }

    protected PersistentGroupMembersBean getNewPersistentGroupBean(HttpServletRequest request) {
        Unit unit = getUnit(request);
        return new PersistentGroupMembersBean(unit, PersistentGroupMembersType.UNIT_GROUP);
    }

}