package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpacePublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
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

	String siteID = request.getParameter("siteID");
	UnitSite site = (UnitSite) rootDomainObject.readSiteByOID(new Integer(siteID));

	SearchElement searchElement = bean.getSearchElements().get(0);
	searchElement.setSearchField(SearchField.UNIT);
	searchElement.setQueryValue(site.getUnit().getName());

	request.setAttribute("bean", bean);
	return mapping.findForward("SearchPublication");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	// TODO Auto-generated method stub
	String siteID = request.getParameter("siteID");
	Site site = rootDomainObject.readSiteByOID(new Integer(siteID));

	request.setAttribute("site", site);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected SearchDSpaceBean createNewBean() {
	SearchDSpacePublicationBean searchDSpacePublicationBean = new SearchDSpacePublicationBean();
	searchDSpacePublicationBean.setSearchPatents(false);
	return searchDSpacePublicationBean;
    }

}
