package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public abstract class SearchDSpaceGeneralAction extends FenixDispatchAction {

    protected ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws  FenixServiceException {

        SearchDSpaceBean bean = createNewBean();
        bean.addSearchElement();
        request.setAttribute("bean", bean);
        return mapping.findForward(forwardTo);
    }

    protected ActionForward addNewSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws  FenixServiceException {

        SearchDSpaceBean bean = getBean(request);
        String addIndex = request.getParameter("addIndex");
        if (addIndex == null) {
            bean.addSearchElement();
        } else {
            bean.addSearchElement(Integer.valueOf(addIndex));
        }
        request.setAttribute("bean", bean);
        request.setAttribute("pageNumber", bean.getPage());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        return mapping.findForward(forwardTo);
    }

    protected ActionForward removeSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws  FenixServiceException {

        SearchDSpaceBean bean = getBean(request);
        String removeIndex = request.getParameter("removeIndex");
        bean.removeSearchElement(Integer.valueOf(removeIndex));

        request.setAttribute("bean", bean);
        request.setAttribute("pageNumber", bean.getPage());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        return mapping.findForward(forwardTo);
    }

    protected SearchDSpaceBean getBean(HttpServletRequest request) {
        SearchDSpaceBean bean;
        IViewState viewState = RenderUtils.getViewState("search");
        if (viewState == null) {
            bean = reconstructBeanFromRequest(request);
        } else {
            bean = (SearchDSpaceBean) viewState.getMetaObject().getObject();
            RenderUtils.invalidateViewState();
        }
        return bean;
    }

    protected ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws  FenixServiceException {

        SearchDSpaceBean bean = reconstructBeanFromRequest(request);

        FileSearchResult searchResults = getSearchResults(request, bean, getSearchPath(request));
        putResearchResultsInBean(bean, searchResults.getSearchResults());

        request.setAttribute("bean", bean);
        return mapping.findForward(forwardTo);

    }

    protected ActionForward searchContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws  FenixServiceException {
        IViewState viewState = RenderUtils.getViewState("search");
        if (viewState == null) {
            return prepareSearch(mapping, form, request, response, forwardTo);
        }
        SearchDSpaceBean bean = (SearchDSpaceBean) viewState.getMetaObject().getObject();
        FileSearchResult searchResults = getSearchResults(request, bean, getSearchPath(request));
        putResearchResultsInBean(bean, searchResults.getSearchResults());

        request.setAttribute("bean", bean);
        return mapping.findForward(forwardTo);
    }

    protected FileSearchResult getSearchResults(HttpServletRequest request, SearchDSpaceBean bean, VirtualPath restrictTo) {
        String index = request.getParameter("pageNumber");
        Integer start = (index == null) ? 1 : Integer.valueOf(index);
        Integer searchOffset = (start - 1) * bean.getPageSize();
        FileSearchResult searchResults =
                FileManagerFactory.getFactoryInstance().getFileManager()
                        .searchFiles(bean.getSearchCriteria(searchOffset), restrictTo);
        request.setAttribute("pageNumber", start);
        bean.setPage(start);
        bean.setTotalItems(searchResults.getTotalElements());
        int numberOfPages = searchResults.getTotalElements() / searchResults.getPageSize();
        numberOfPages += (searchResults.getTotalElements() % searchResults.getPageSize() != 0) ? 1 : 0;
        request.setAttribute("numberOfPages", numberOfPages);
        bean.setNumberOfPages(numberOfPages);
        return searchResults;
    }

    protected FileSearchResult getSearchResults(HttpServletRequest request, SearchDSpaceBean bean) {
        return getSearchResults(request, bean, null);
    }

    protected VirtualPath getSearchPath(HttpServletRequest request) {
        return null;
    }

    protected SearchDSpaceBean createNewBean() {
        return new SearchDSpaceBean();
    }

    protected void putResearchResultsInBean(SearchDSpaceBean bean, List<FileDescriptor> searchResults) {
        Set<File> researchResults = new HashSet<File>();
        for (FileDescriptor descriptor : searchResults) {
            File documentFile = File.readByExternalStorageIdentification(descriptor.getUniqueId());
            researchResults.add(documentFile);
        }
        bean.setResults(new ArrayList<DomainObject>(researchResults));
    }

    protected SearchDSpaceBean reconstructBeanFromRequest(HttpServletRequest request) {
        SearchDSpaceBean bean = createNewBean();

        String values[] = request.getParameterValues("criteria");
        for (String value : values) {
            String fields[] = value.split(":");
            bean.addSearchElement(new SearchElement(SearchField.valueOf(fields[1]), ((fields.length == 2) ? "" : fields[2]),
                    ConjunctionType.valueOf(fields[0])));
        }

        return bean;
    }

}