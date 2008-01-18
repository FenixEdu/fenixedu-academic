package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpacePublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.publication.SearchPublicationsAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;

public class SearchPublicationsDA extends SearchPublicationsAction {

    @Override
    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	SearchDSpaceBean bean = createNewBean();
	bean.addSearchElement();

	SearchElement searchElement = bean.getSearchElements().get(0);
	searchElement.setSearchField(SearchField.ANY);

	request.setAttribute("bean", bean);
	
	return mapping.findForward("SearchPublication");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	// TODO Auto-generated method stub
	setRequestDomainObject(request);
	
	return super.execute(mapping, actionForm, request, response);
    }

    protected void setRequestDomainObject(HttpServletRequest request) {
	
	FunctionalityContext context = AbstractFunctionalityContext.getCurrentContext(request);
	Site site = null;
	if (context != null) {
	    site = (Site) context.getLastContentInPath(Site.class);
	}
	else {
	    String siteID = request.getParameter("siteID");
	    site = (Site) RootDomainObject.getInstance().readContentByOID(Integer.valueOf(siteID));
	}
	request.setAttribute("site", site);
    }

    @Override
    protected SearchDSpaceBean createNewBean() {
	SearchDSpacePublicationBean searchDSpacePublicationBean = new SearchDSpacePublicationBean();
	searchDSpacePublicationBean.setSearchPatents(false);
	return searchDSpacePublicationBean;
    }

    
}
