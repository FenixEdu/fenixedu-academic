package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.SearchDSpaceBean;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class SearchDSpaceGeneralAction extends FenixDispatchAction {

	protected ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String forwardTo) throws FenixFilterException,
			FenixServiceException {

		SearchDSpaceBean bean = new SearchDSpaceBean();
		request.setAttribute("bean", bean);
		String searchType = request.getParameter("searchType");
		request.setAttribute("searchType", searchType);
		return mapping.findForward(forwardTo);
	}

	protected ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String forwardTo) throws FenixFilterException, FenixServiceException {
		
		SearchDSpaceBean bean = reconstructBeanFromRequest(request);
		String searchType = request.getParameter("searchType");
		request.setAttribute("searchType", searchType);
		request.setAttribute("bean", bean);
		FileSearchResult searchResults = getSearchResults(request, bean);
		putResearchResultsInRequest(request, searchResults.getSearchResults());
		putBeanInRequest(request,bean);
		return mapping.findForward(forwardTo);
		
	}
    
	protected ActionForward searchContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String forwardTo) {
		SearchDSpaceBean bean = (SearchDSpaceBean) RenderUtils.getViewState("search").getMetaObject()
		.getObject();
		String searchType = request.getParameter("searchType");
		request.setAttribute("searchType", searchType);
		request.setAttribute("bean", bean);

		FileSearchResult searchResults = getSearchResults(request, bean,getSearchPath());
		putResearchResultsInRequest(request, searchResults.getSearchResults());
		putBeanInRequest(request, bean);

		return mapping.findForward(forwardTo);
	}
	
	protected FileSearchResult getSearchResults(HttpServletRequest request, SearchDSpaceBean bean, VirtualPath restrictTo) {
		String index = request.getParameter("page");
		Integer start = (index == null) ? 1 : Integer.valueOf(index);
		Integer searchOffset = (start-1)*bean.getPageSize();
		FileSearchResult searchResults =  FileManagerFactory.getFileManager().searchFiles(bean.getSearchCriteria(searchOffset),restrictTo);
		request.setAttribute("page", start);
		request.setAttribute("totalItems",searchResults.getTotalElements());
		int numberOfPages = searchResults.getTotalElements()/searchResults.getPageSize();
		numberOfPages += (searchResults.getTotalElements()%searchResults.getPageSize()!=0) ? 1 : 0;
		request.setAttribute("numberOfPages", numberOfPages);
		return searchResults;	
	}
		
	protected FileSearchResult getSearchResults(HttpServletRequest request, SearchDSpaceBean bean) {
		return getSearchResults(request, bean, null);
	}

	protected VirtualPath getSearchPath() {
		return null;
	}
	
	protected void putResearchResultsInRequest(HttpServletRequest request, List<FileDescriptor> searchResults) {
		Set<File> researchResults = new HashSet<File>();
		for (FileDescriptor descriptor : searchResults) {
			File documentFile =  File.readByExternalStorageIdentification(descriptor.getUniqueId());
			researchResults.add(documentFile);
		}
		
		request.setAttribute("searchResult", researchResults);	
	}
	
	private SearchDSpaceBean reconstructBeanFromRequest(HttpServletRequest request) {
		String field1 = request.getParameter("field1");
		String field2 = request.getParameter("field2");
		String field3 = request.getParameter("field3");
		String value1 = request.getParameter("value1");
		String value2 = request.getParameter("value2");
		String value3 = request.getParameter("value3");
		String connector1 = request.getParameter("c1");
		String connector2 = request.getParameter("c2"); 
	
		SearchDSpaceBean bean = new SearchDSpaceBean();
		bean.setField1(SearchField.valueOf(field1));
		bean.setField2(SearchField.valueOf(field2));
		bean.setField3(SearchField.valueOf(field3));
		bean.setValue1(value1);
		bean.setValue2(value2);
		bean.setValue3(value3);
		bean.setFirstConjunction(ConjunctionType.valueOf(connector1));
		bean.setSecondConjunction(ConjunctionType.valueOf(connector2));
		
		return bean;
	}
	
	private void putBeanInRequest(HttpServletRequest request, SearchDSpaceBean bean) {
		request.setAttribute("field1", bean.getField1());
		request.setAttribute("field2", bean.getField2());
		request.setAttribute("field3", bean.getField3());
		request.setAttribute("value1", bean.getValue1());
		request.setAttribute("value2", bean.getValue2());
		request.setAttribute("value3", bean.getValue3());
		request.setAttribute("c1", bean.getFirstConjunction());
		request.setAttribute("c2", bean.getSecondConjunction());
	}
}
