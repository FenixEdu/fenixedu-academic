/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateFileContent;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteFileContent;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteItem;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteSection;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditFilePermissions;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
import net.sourceforge.fenixedu.domain.cms.TemplatedSectionInstance;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.person.ModifiedContentBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

/**
 * Generic action to control the management of a website.
 * 
 * Used forwards:
 * <ul>
 * <li>createItem</li>
 * <li>editItem</li>
 * <li>section: view a section</li>
 * <li>uploadFile: page that allows you to upload a simple file</li>
 * <li>sectionsManagement: initial page that shows all top level sections</li>
 * <li>createSection</li>
 * <li>editSection</li>
 * <li>editFile</li>
 * <li>organizeItems</li>
 * <li>organizeFiles</li>
 * <li>editSectionPermissions</li>
 * <li>editItemPermissions</li>
 * </ul>
 * 
 * @author cfgi
 */
public abstract class SiteManagementDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(SiteManagementDA.class);

    private static final int MAX_FILE_SIZE = 66060288;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Site site = getSite(request);

        if (site != null) {
            request.setAttribute("site", site);
        }

        ActionForward forward = super.execute(mapping, actionForm, request, response);

        if (site != null && site.hasQuota()) {
            request.setAttribute("siteQuota", site.getQuota());
            request.setAttribute("siteUsedQuota", site.getUsedQuota());
        }

        return forward;
    }

    public ActionForward createItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section section = getSection(request);
        if (section == null) {
            return sections(mapping, form, request, response);
        }

        request.setAttribute("creator", new ItemCreator(section));

        selectSection(request);
        return mapping.findForward("createItem");
    }

    public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        selectItem(request);
        return mapping.findForward("editItem");
    }

    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Item item = getItem(request);
        if (item != null) {
            final Section section = item.getSection();

            try {
                DeleteItem.runDeleteItem(section.getOwnerSite(), item);
            } catch (DomainException e) {
                addErrorMessage(request, "items", e.getKey(), (Object[]) e.getArgs());
            }

            selectSection(request, section);
        }
        return mapping.findForward("section");
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CmsContent content = getSelectedContent(request);
        Site site = getSite(request);

        FileContentCreationBean bean = new FileContentCreationBean(content, site);

        if (!site.isFileClassificationSupported()) {
            bean.setEducationalLearningResourceType(EducationalResourceType.SITE_CONTENT);
            request.setAttribute("skipFileClassification", true);
        }

        bean.setAuthorsName(getAuthorNameForFile(request));
        request.setAttribute("fileItemCreator", bean);

        return mapping.findForward("uploadFile");
    }

    private CmsContent getSelectedContent(HttpServletRequest request) {
        Item item = selectItem(request);

        return item == null ? selectSection(request) : item;
    }

    private Item selectItem(HttpServletRequest request) {
        final Item item = getItem(request);

        if (item != null) {
            selectSection(request, item.getSection());
        }

        request.setAttribute("item", item);
        return item;
    }

    // SECTIONS

    public ActionForward sections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("sectionsManagement");
    }

    public ActionForward createSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);

        Section section = getSection(request);
        if (section == null) {
            Site site = getSite(request);

            request.setAttribute("creator", new SectionCreator(site));
        } else {
            request.setAttribute("creator", new SectionCreator(section));
        }

        return mapping.findForward("createSection");
    }

    public ActionForward editSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);
        return mapping.findForward("editSection");
    }

    public ActionForward deleteSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Section section = selectSection(request);

        if (section.isDeletable()) {
            return mapping.findForward("confirmSectionDelete");
        } else {
            addErrorMessage(request, "section", "site.section.delete.notAllowed");
            return section == null ? mapping.findForward("sectionsManagement") : mapping.findForward("section");
        }
    }

    public ActionForward confirmSectionDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section section = selectSection(request);

        if (section == null) {
            return sections(mapping, form, request, response);
        }

        if (request.getParameter("confirm") != null) {
            try {
                Section superiorSection = section.getSuperiorSection();

                Site site = section.getOwnerSite();
                DeleteSection.runDeleteSection(site, section);

                section = superiorSection;
            } catch (DomainException e) {
                addErrorMessage(request, "section", e.getKey(), (Object[]) e.getArgs());
            }

        }

        selectSection(request, section);
        return section == null ? mapping.findForward("sectionsManagement") : mapping.findForward("section");
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        selectSection(request);

        return mapping.findForward("section");
    }

    protected Section selectSection(final HttpServletRequest request) {
        final Section section = getSection(request);
        selectSection(request, section);
        return section;
    }

    private void selectSection(final HttpServletRequest request, final Section section) {
        List<Section> breadCrumbs = new ArrayList<Section>();

        if (section != null) {
            for (Section superior = section.getSuperiorSection(); superior != null; superior = superior.getSuperiorSection()) {
                breadCrumbs.add(0, superior);
            }
        }

        request.setAttribute("sectionBreadCrumbs", breadCrumbs);
        request.setAttribute("section", section);
    }

    protected Item getItem(final HttpServletRequest request) {
        String parameter = request.getParameter("itemID");
        if (parameter == null) {
            return null;
        }
        return (Item) FenixFramework.getDomainObject(parameter);
    }

    protected Section getSection(final HttpServletRequest request) {
        final String parameter = request.getParameter("sectionID");
        if (parameter == null) {
            return null;
        }
        return (Section) FenixFramework.getDomainObject(parameter);
    }

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("creator");
        if (viewState == null) {
            return sections(mapping, form, request, response);
        }
        FileContentCreationBean bean = (FileContentCreationBean) viewState.getMetaObject().getObject();
        List<String> errors = validationErrors(bean);
        if (errors.isEmpty()) {
            try {
                return internalFileUpload(mapping, form, request, response);
            } catch (DomainException e) {
                addErrorMessage(request, "section", e.getKey(), (Object[]) e.getArgs());
                return reportUploadError(mapping, request, bean, errors);
            } catch (NotAuthorizedException e) {
                addErrorMessage(request, "section", "error.not.authorized", new String[0]);
                return reportUploadError(mapping, request, bean, errors);
            }
        } else {
            return reportUploadError(mapping, request, bean, errors);
        }

    }

    private ActionForward reportUploadError(ActionMapping mapping, HttpServletRequest request, FileContentCreationBean bean,
            List<String> errors) {
        bean.setFile(null);
        RenderUtils.invalidateViewState("file");

        for (String error : errors) {
            addActionMessage(request, error, (String[]) null);
        }
        selectItem(request);
        request.setAttribute("fileItemCreator", bean);

        return mapping.findForward("uploadFile");
    }

    private List<String> validationErrors(FileContentCreationBean bean) {

        List<String> errors = new ArrayList<String>();

        String filename = bean.getFileName();
        if (filename == null || filename.length() == 0 || bean.getFileSize() == 0) {
            errors.add("errors.fileRequired");
        }

        String displayName = bean.getDisplayName();
        if (displayName == null || displayName.length() == 0 || displayName.trim().length() == 0) {
            bean.setDisplayName(filename);
        }

        String name = bean.getAuthorsName();
        if (name == null || name.length() == 0 || name.trim().length() == 0) {
            errors.add("errors.authorRequired");
        }

        if (bean.getEducationalLearningResourceType() == null) {
            errors.add("errors.educationalTypeRequired");
        }

        return errors;
    }

    private ActionForward internalFileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("creator");
        if (viewState == null) {
            return section(mapping, form, request, response);
        }

        FileContentCreationBean bean = (FileContentCreationBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        CmsContent container = bean.getContent();
        if (container instanceof Item) {
            selectItem(request);
        } else {
            selectSection(request);
        }

        try (InputStream formFileInputStream = bean.getFile()) {
            if (formFileInputStream == null) {
                addErrorMessage(request, "unableToStoreFile", "errors.unableToStoreFile", bean.getFileName());
                return uploadFile(mapping, form, request, response);
            }

            if (bean.getFileSize() > MAX_FILE_SIZE) {
                addErrorMessage(request, "fileMaxSizeExceeded", "errors.file.max.size.exceeded", MAX_FILE_SIZE);
                return uploadFile(mapping, form, request, response);
            }

            byte[] bytes = ByteStreams.toByteArray(formFileInputStream);

            CreateFileContent.runCreateFileContent(bean.getSite(), container, bytes, bean.getFileName(), bean.getDisplayName(),
                    bean.getPermittedGroup(), getLoggedPerson(request), bean.getEducationalLearningResourceType());

        } catch (DomainException e) {
            addErrorMessage(request, "unableToStoreFile", "errors.unableToStoreFile", bean.getFileName());

            return uploadFile(mapping, form, request, response);

        }

        return mapping.findForward("section");
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        Item item = selectItem(request);
        if (item == null) {
            selectSection(request);
        }
        FileContent fileContent = selectFileContent(request);
        Site site = fileContent != null ? fileContent.getSite() : null;

        if (site == null) {
            return mapping.findForward("section");
        }

        try {
            DeleteFileContent.runDeleteFileContent(fileContent);
        } catch (DomainException e1) {
            addErrorMessage(request, "items", "errors.unableToDeleteFile");
        }

        return mapping.findForward("section");
    }

    public ActionForward editDisplayName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        selectItem(request);
        selectSection(request);
        FileContent fileItem = selectFileContent(request);

        if (fileItem == null) {
            return mapping.findForward("section");
        }

        request.setAttribute("fileItem", fileItem);

        return mapping.findForward("edit-fileItem-name");
    }

    private FileContent selectFileContent(HttpServletRequest request) {
        String fileItemIdString = request.getParameter("fileItemId");
        if (fileItemIdString == null) {
            return null;
        }

        FileContent fileContent = FileContent.readByOID(fileItemIdString);
        request.setAttribute("fileItem", fileContent);

        return fileContent;
    }

    public ActionForward prepareEditItemFilePermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Item item = selectItem(request);
        if (item == null) {
            selectSection(request);
        }
        FileContent fileItem = selectFileContent(request);

        if (fileItem == null) {
            return section(mapping, form, request, response);
        } else {
            FileItemPermissionBean bean = new FileItemPermissionBean(fileItem);
            request.setAttribute("fileItemBean", bean);

            return mapping.findForward("editFile");
        }
    }

    public ActionForward editItemFilePermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        selectSection(request);

        final FileContent fileItem = selectFileContent(request);

        IViewState viewState = RenderUtils.getViewState();
        if (viewState == null) {
            return mapping.findForward("section");
        }

        FileItemPermissionBean bean = (FileItemPermissionBean) viewState.getMetaObject().getObject();
        try {
            Site site = fileItem.getSite();
            EditFilePermissions.runEditFilePermissions(site, fileItem, bean.getPermittedGroup());
            return mapping.findForward("section");
        } catch (DomainException ex) {
            addErrorMessage(request, "error.teacher.siteAdministration.editItemFilePermissions.unableToChangeFilePermissions");

            request.setAttribute("fileItemBean", bean);
            return mapping.findForward("editFile");
        }
    }

    public ActionForward saveSectionsOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section parentSection = getSection(request);
        if (parentSection == null) {
            return sections(mapping, form, request, response);
        } else {
            return section(mapping, form, request, response);
        }
    }

    public List<ModifiedContentBean> getModifiedContent(Site site, String structure) {

        List<DomainObject> flatContent = flatten(site.getAssociatedSectionSet());
        // Put null in the first position, to signal the site root
        flatContent.add(0, null);

        List<ModifiedContentBean> modifiedContent = new ArrayList<ModifiedContentBean>();

        if (structure == null) {
            return new ArrayList<ModifiedContentBean>();
        }

        String[] nodes = structure.split(",");
        Map<Integer, Integer> nodeOrder = new HashMap<Integer, Integer>();

        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer childIndex = getId(parts[0]);
            Integer newParentIndex = getId(parts[2]);

            int orderForParent = 0;
            if (nodeOrder.containsKey(newParentIndex)) {
                orderForParent = nodeOrder.get(newParentIndex) + 1;
                nodeOrder.remove(newParentIndex);
                nodeOrder.put(newParentIndex, orderForParent);
            } else {
                nodeOrder.put(newParentIndex, orderForParent);
            }

            Section parentContainer = (Section) flatContent.get(newParentIndex);
            CmsContent content = (CmsContent) flatContent.get(childIndex);
            modifiedContent.add(new ModifiedContentBean(parentContainer, content, orderForParent));
        }
        return modifiedContent;
    }

    private List<DomainObject> flatten(Collection<? extends CmsContent> contents) {
        List<DomainObject> flatContent = new ArrayList<>();
        for (CmsContent content : contents) {
            if (content instanceof Section) {
                flatContent.add(content);
                Section section = (Section) content;
                flatContent.addAll(section.getOrderedChildren());
            } else {
                flatContent.add(content);
            }
            // This is terrible, but it must offset the indexes...
            flatContent.addAll(content.getFileContentSet());
        }
        return flatContent;
    }

    private Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public ActionForward organizeSectionItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section section = getSection(request);
        if (section == null) {
            return sections(mapping, form, request, response);
        }

        selectSection(request, section);
        return mapping.findForward("organizeItems");
    }

    public ActionForward organizeItemFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("organizeFiles");
    }

    private void addErrorMessage(HttpServletRequest request, String key) {
        addErrorMessage(request, ActionMessages.GLOBAL_MESSAGE, key);
    }

    public ActionForward editSectionPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);
        return mapping.findForward("editSectionPermissions");
    }

    public ActionForward editItemPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("editItemPermissions");
    }

    public ActionForward prepareAddFromPool(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section container = selectSection(request);
        Site site = getSite(request);

        request.setAttribute("parent", container == null ? site : container);
        request.setAttribute("template", site.getTemplate());

        return mapping.findForward("addInstitutionSection");
    }

    public ActionForward addFromPool(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String containerId = request.getParameter("containerId");
        String contentId = request.getParameter("contentId");

        if (containerId == null || contentId == null) {
            return prepareAddFromPool(mapping, actionForm, request, response);
        }

        DomainObject container = FenixFramework.getDomainObject(containerId);
        TemplatedSection template = FenixFramework.getDomainObject(contentId);

        addFromPool(container, template);

        return container instanceof Section ? section(mapping, actionForm, request, response) : sections(mapping, actionForm,
                request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    protected void addFromPool(DomainObject parent, TemplatedSection template) {
        if (parent instanceof Site) {
            new TemplatedSectionInstance(template, (Site) parent);
        } else {
            new TemplatedSectionInstance(template, (Section) parent);
        }
    }

    protected Site getSite(HttpServletRequest request) {
        String siteId = request.getParameter("siteID");

        if (siteId == null) {
            return null;
        }

        return (Site) FenixFramework.getDomainObject(siteId);
    }

    protected abstract String getAuthorNameForFile(HttpServletRequest request);

    protected abstract String getItemLocationForFile(HttpServletRequest request, Item item, Section section);

}