package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpacePublicationBean;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.presentationTier.Action.SearchDSpaceGeneralAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

@Mapping(module = "researcher", path = "/publications/search", scope = "session", parameter = "method")
@Forwards(
        value = { @Forward(name = "SearchPublication", path = "/researcher/result/publications/searchPublication.jsp",
                tileProperties = @Tile(
                        title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")) })
public class SearchPublicationsAction extends SearchDSpaceGeneralAction {

    public ActionForward addNewSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        return super.addNewSearchCriteria(mapping, form, request, response, "SearchPublication");
    }

    public ActionForward removeSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        return super.removeSearchCriteria(mapping, form, request, response, "SearchPublication");
    }

    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.prepareSearch(mapping, form, request, response, "SearchPublication");
    }

    public ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.moveIndex(mapping, form, request, response, "SearchPublication");

    }

    public ActionForward searchPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.searchContent(mapping, form, request, response, "SearchPublication");
    }

    @Override
    protected VirtualPath getSearchPath(HttpServletRequest request) {

        final VirtualPath searchPath = new VirtualPath();

        searchPath.addNode(new VirtualPathNode("Research", "Research"));
        searchPath.addNode(new VirtualPathNode("Results", "Results"));

        SearchDSpacePublicationBean bean = (SearchDSpacePublicationBean) getBean(request);

        return (bean.getSearchNode() == null) ? searchPath : searchPath.addNode(bean.getSearchNode());
    }

    @Override
    protected SearchDSpaceBean createNewBean() {
        return new SearchDSpacePublicationBean();
    }

    @Override
    protected void putResearchResultsInBean(SearchDSpaceBean bean, List<FileDescriptor> searchResults) {
        Set<ResearchResult> researchResults = new HashSet<ResearchResult>();
        for (FileDescriptor descriptor : searchResults) {
            File file = File.readByExternalStorageIdentification(descriptor.getUniqueId());
            if (file != null) {
                ResearchResultDocumentFile documentFile = (ResearchResultDocumentFile) file;
                researchResults.add(documentFile.getResult());
            }

        }
        bean.setResults(new ArrayList<DomainObject>(researchResults));
    }

    @Override
    protected SearchDSpaceBean reconstructBeanFromRequest(HttpServletRequest request) {
        SearchDSpacePublicationBean bean = (SearchDSpacePublicationBean) super.reconstructBeanFromRequest(request);
        String searchPublications = request.getParameter("searchPublications");
        String searchPatents = request.getParameter("searchPatents");

        bean.setSearchPublications(Boolean.valueOf(searchPublications));
        bean.setSearchPatents(Boolean.valueOf(searchPatents));
        return bean;
    }

}
