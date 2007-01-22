package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.utl.fenix.utils.Pair;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateScormFileItemForItem;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * Generic action to control the management of a website.
 * 
 * Used forwards:
 * <ul>
 *  <li>createItem</li>
 *  <li>editItem</li>
 *  <li>section: view a section</li>
 *  <li>uploadFile: page that allows you to upload a simple file</li>
 *  <li>sectionsManagement: inicial page that shows all top level sections</li>
 *  <li>createSection</li>
 *  <li>editSection</li>
 *  <li>editFile</li>
 *  <li>organizeItems</li>
 *  <li>organizeFiles</li>
 *  <li>editSectionPermissions</li>
 *  <li>editItemPermissions</li>
 * </ul>
 * 
 * @author cfgi
 */
public abstract class SiteManagementDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = getSite(request);
        if (site != null) {
            request.setAttribute("site", site);
        }
        
        return super.execute(mapping, actionForm, request, response);
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

    public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("editItem");
    }

    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Item item = getItem(request);
        final Section section = item.getSection();

        try {
            final Object[] args = { section.getSite(), item };
            executeService(request, "DeleteItem", args);
        } catch (DomainException e) {
            addErrorMessage(request, "items", e.getKey(), (Object[]) e.getArgs());
        }

        selectSection(request, section);
        return mapping.findForward("section");
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Item item = selectItem(request);
        
        FileItemCreationBean bean = new FileItemCreationBean(item);
        bean.setAuthorsName(getAuthorNameForFile(request, item));
        request.setAttribute("fileItemCreator", bean);

        return mapping.findForward("uploadFile");
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

    public ActionForward sections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
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
            return section == null ? mapping.findForward("sectionsManagement") : mapping
                    .findForward("section");
        }
    }

    public ActionForward confirmSectionDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = selectSection(request);

        if (section == null) {
            return sections(mapping, form, request, response);
        }

        if (request.getParameter("confirm") != null) {
            try {
                Section superiorSection = section.getSuperiorSection();
                
                Site site = section.getSite();
                executeService(request, "DeleteSection", new Object[] { site, section });

                section = superiorSection;
            } catch (DomainException e) {
                addErrorMessage(request, "section", e.getKey(), (Object[]) e.getArgs());
            }
        }

        selectSection(request, section);
        return section == null ? mapping.findForward("sectionsManagement") : mapping.findForward("section");
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
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
            for (Section superior = section.getSuperiorSection(); superior != null; superior = superior
                    .getSuperiorSection()) {
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
        final Integer itemID = Integer.valueOf(parameter);
        return rootDomainObject.readItemByOID(itemID);
    }

    protected Section getSection(final HttpServletRequest request) {
        final String parameter = request.getParameter("sectionID");
        if (parameter == null) {
            return null;
        }
        final Integer sectionID = Integer.valueOf(parameter);
        return rootDomainObject.readSectionByOID(sectionID);
    }

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        FileItemCreationBean bean = (FileItemCreationBean) RenderUtils
                .getViewState("creator").getMetaObject().getObject();
        List<String> errors = validationErrors(bean);
        if (errors.isEmpty()) {
            return fileUpload(mapping, form, request, response,
                    "CreateFileItemForItem");
        } else {
            bean.setFile(null);
            RenderUtils.invalidateViewState("file");

            for (String error : errors) {
                addActionMessage(request, error, (String[]) null);
            }
            selectItem(request);
            request.setAttribute("fileItemCreator", bean);

            return mapping.findForward("uploadFile");

        }

    }
    
    public ActionForward scormFileUpload(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FileItemCreationBean bean = (FileItemCreationBean) RenderUtils
                .getViewState("creator").getMetaObject().getObject();
        List<String> errors = validationErrors(bean);
        if (errors.isEmpty()) {
            return fileUpload(mapping, form, request, response,
                    "CreateScormPackageForItem");
        } else {
            bean.setFile(null);
            RenderUtils.invalidateViewState("file");
            for (String error : errors) {
                addActionMessage(request, error, (String[]) null);
            }
            return prepareUploadScormFile(mapping, form, request, response);
        }
    }
    
    private List<String> validationErrors(FileItemCreationBean bean) {

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
    
    private ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String service) throws Exception {
        final Item item = selectItem(request);

        IViewState viewState = RenderUtils.getViewState("creator");
        if (viewState == null) {
            return section(mapping, form, request, response);
        }

        FileItemCreationBean bean = (FileItemCreationBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        InputStream formFileInputStream = null;
        try {
            formFileInputStream = bean.getFile();

            Site site = item.getSection().getSite();
            executeService(request, service, new Object[] { site, item, formFileInputStream, bean.getFileName(),
                    bean.getDisplayName(), bean.getPermittedGroup(), getLoggedPerson(request),
                    bean.getEducationalLearningResourceType() });
        } catch (FileManagerException e) {
            addErrorMessage(request, "unableToStoreFile", "errors.unableToStoreFile", bean.getFileName());

            return (service.equalsIgnoreCase("CreateScormPackageForItem") ? prepareCreateScormFile(mapping,
                    form, request, response) : uploadFile(mapping, form, request, response));

        } finally {
            if (formFileInputStream != null) {
                formFileInputStream.close();
            }
        }

        return mapping.findForward("section");
    }

    public ActionForward prepareUploadScormFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        putScormCreationBeanInRequest(request);
        return mapping.findForward("uploadScorm");
    }
    
    public ActionForward prepareCreateScormFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        putScormCreationBeanInRequest(request);
        return mapping.findForward("createScorm");
    }

    private void putScormCreationBeanInRequest(HttpServletRequest request) {
        Item item = selectItem(request);
        ScormCreationBean bean = new ScormCreationBean(item);
        bean.setAuthorsName(item.getSection().getSite().getAuthorName());
        ScormCreationBean possibleBean = (ScormCreationBean) getRenderedObject();
        if (possibleBean != null) {
            bean.copyValuesFrom(possibleBean);
        }
        request.setAttribute("bean", bean);
    }
    
    public ActionForward validateScormForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ScormCreationBean bean = (ScormCreationBean) getRenderedObject("scormPackage");

        if (bean.isValid() && validationErrors(bean).isEmpty()) {
            return createScormFile(mapping, form, request, response);
        }

        addActionMessage(request, "label.missingRequiredFields", (String[]) null);

        selectItem(request);
        request.setAttribute("bean", bean);
        
        return mapping.findForward("createScorm");
    }
    
    public ActionForward createScormFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ScormCreationBean bean = (ScormCreationBean) getRenderedObject("scormPackage");

        Item item = selectItem(request);
        
        String displayName = bean.getDisplayName();
        if (displayName == null || displayName.length() == 0 || displayName.trim().length() == 0) {
            displayName = getFilenameOnly(bean.getFileName());
        }

        InputStream vcardFile = bean.getVirtualCardFile();
        InputStream formFileInputStream = null;

        Section section = item.getSection();
        String resourceLocation = getItemLocationForFile(request, item, section);
        bean.setTechnicalLocation(resourceLocation);
        
        try {
            if(bean.getVirtualCardFilename()!=null && bean.getVirtualCardFilename().length()>0) {
                String vcardContent = readContentOfVCard(vcardFile);
                bean.setVcardContent(vcardContent);
            }
        
            formFileInputStream = bean.getFile();
            final Object[] args = { new CreateScormFileItemForItem.CreateScormFileItemForItemArgs(item,
                    formFileInputStream, bean.getFileName(), displayName, bean.getPermittedGroup(), bean
                            .getMetaInformation(), getLoggedPerson(request),bean.getEducationalLearningResourceType()) };

            executeService(request, "CreateScormFileItemForItem", args);

        } catch (FenixServiceException e) {
            addActionMessage(request, "error.scormfilupload", (String[]) null);
            RenderUtils.invalidateViewState("scormPackage");
            return prepareCreateScormFile(mapping, form, request, response);
        } catch (IOException e) {
            addActionMessage(request, "error.unableToReadVCard", (String[]) null);
            RenderUtils.invalidateViewState("scormPackage");
            return prepareCreateScormFile(mapping, form, request, response);
        } finally {
            if (formFileInputStream != null) {
                formFileInputStream.close();
            }
            if (vcardFile != null) {
                vcardFile.close();
            }
        }

        return mapping.findForward("section");
    }

    private String readContentOfVCard(InputStream cardFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(cardFile));
        String line;
        String content = "";
        while ((line = reader.readLine()) != null) {
            content += " " + line;
        }
        return content;
    }

    private String getFilenameOnly(final String fullPathToFile) {
        // Strip all but the last filename. It would be nice
        // to know which OS the file came from.
        String filenameOnly = fullPathToFile;

        while (filenameOnly.indexOf('/') > -1) {
            filenameOnly = filenameOnly.substring(filenameOnly.indexOf('/') + 1);
        }

        while (filenameOnly.indexOf('\\') > -1) {
            filenameOnly = filenameOnly.substring(filenameOnly.indexOf('\\') + 1);
        }

        return filenameOnly;
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {

        selectItem(request);
        FileItem fileItem = selectFileItem(request);

        if (fileItem == null) {
            return mapping.findForward("section");
        }

        try {
            Site site = fileItem.getItem().getSection().getSite();
            ServiceUtils.executeService(getUserView(request), "DeleteFileItemFromItem", site, fileItem);
        } catch (FileManagerException e1) {
            addErrorMessage(request, "items", "errors.unableToDeleteFile");
        }

        return mapping.findForward("section");
    }

    private FileItem selectFileItem(HttpServletRequest request) {
        String fileItemIdString = request.getParameter("fileItemId");
        if (fileItemIdString == null) {
            return null;
        }

        FileItem fileItem = FileItem.readByOID(Integer.valueOf(fileItemIdString));
        request.setAttribute("fileItem", fileItem);

        return fileItem;
    }

    public ActionForward prepareEditItemFilePermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        selectItem(request);
        FileItem fileItem = selectFileItem(request);

        if (fileItem == null) {
            return section(mapping, form, request, response);
        } else {
            FileItemPermissionBean bean = new FileItemPermissionBean(fileItem);
            request.setAttribute("fileItemBean", bean);

            return mapping.findForward("editFile");
        }
    }

    public ActionForward editItemFilePermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        selectItem(request);
        final FileItem fileItem = selectFileItem(request);

        IViewState viewState = RenderUtils.getViewState();
        if (viewState == null) {
            return mapping.findForward("section");
        }

        FileItemPermissionBean bean = (FileItemPermissionBean) viewState.getMetaObject().getObject();
        try {
            Site site = fileItem.getItem().getSection().getSite();
            ServiceUtils.executeService(getUserView(request), "EditItemFilePermissions", site, fileItem, bean
                    .getPermittedGroup());
            return mapping.findForward("section");
        } catch (FileManagerException ex) {
            addErrorMessage(request,
                    "error.teacher.siteAdministration.editItemFilePermissions.unableToChangeFilePermissions");
            
            request.setAttribute("fileItemBean", bean);
            return mapping.findForward("editFile");
        }
    }

    public ActionForward saveSectionsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("sectionsOrder");
        Site site = getSite(request);

        Section parentSection = getSection(request);
        List<Section> initialOrder = flattenSection(site, parentSection);

        List<Pair<Section, Section>> hierarchy = new ArrayList<Pair<Section, Section>>();

        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");

            Integer childIndex = getId(parts[0]);
            Integer parentIndex = getId(parts[1]);

            Section parent = initialOrder.get(parentIndex);
            Section child = initialOrder.get(childIndex);

            hierarchy.add(new Pair<Section, Section>(parent, child));
        }

        ServiceUtils.executeService(getUserView(request), "RearrangeSiteSections", site, hierarchy);

        if (parentSection == null) {
            return sections(mapping, form, request, response);
        } else {
            return section(mapping, form, request, response);
        }
    }

    private Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Section> flattenSection(Site site, Section parent) {
        List<Section> result = new ArrayList<Section>();

        result.add(parent);

        SortedSet<Section> sections;
        if (parent == null) {
            sections = site.getOrderedTopLevelSections();
        } else {
            sections = parent.getOrderedSubSections();
        }

        for (Section section : sections) {
            result.addAll(flattenSection(site, section));
        }

        return result;
    }

    public ActionForward organizeSectionItems(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = getSection(request);
        if (section == null) {
            return sections(mapping, form, request, response);
        }

        selectSection(request, section);
        return mapping.findForward("organizeItems");
    }

    public ActionForward saveItemsOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("itemsOrder");
        Section section = getSection(request);

        if (section == null) {
            return sections(mapping, form, request, response);
        }

        List<Item> initialItems = new ArrayList<Item>(section.getOrderedItems());
        List<Item> orderedItems = new ArrayList<Item>();

        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");

            Integer itemIndex = getId(parts[0]);
            orderedItems.add(initialItems.get(itemIndex - 1));
        }

        Site site = section.getSite();
        ServiceUtils.executeService(getUserView(request), "RearrangeSectionItems", site, section, orderedItems);
        return section(mapping, form, request, response);
    }

    public ActionForward organizeItemFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("organizeFiles");
    }

    public ActionForward saveFilesOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("filesOrder");
        Item item = getItem(request);

        if (item == null) {
            return sections(mapping, form, request, response);
        }

        List<FileItem> initialFiles = new ArrayList<FileItem>(item.getSortedFileItems());
        List<FileItem> orderedFiles = new ArrayList<FileItem>();

        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");

            Integer itemIndex = getId(parts[0]);
            orderedFiles.add(initialFiles.get(itemIndex - 1));
        }

        Site site = item.getSection().getSite();
        ServiceUtils.executeService(getUserView(request), "RearrangeItemFiles", site, item, orderedFiles);
        return section(mapping, form, request, response);
    }

    private void addErrorMessage(HttpServletRequest request, String key) {
        addErrorMessage(request, ActionMessages.GLOBAL_MESSAGE, key);
    }

    private void addErrorMessage(HttpServletRequest request, String property, String key, Object... args) {
        ActionMessages messages = getErrors(request);
        messages.add(property, new ActionMessage(key, args));

        saveErrors(request, messages);
    }

    public ActionForward editSectionPermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectSection(request);
        return mapping.findForward("editSectionPermissions");
    }

    public ActionForward editItemPermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("editItemPermissions");
    }

    protected Site getSite(HttpServletRequest request) {
        Integer siteId = getId(request.getParameter("siteID"));
        return (Site) RootDomainObject.readDomainObjectByOID(Site.class, siteId);
    }

    protected abstract String getAuthorNameForFile(HttpServletRequest request, Item item);
    protected abstract String getItemLocationForFile(HttpServletRequest request, Item item, Section section);
}
