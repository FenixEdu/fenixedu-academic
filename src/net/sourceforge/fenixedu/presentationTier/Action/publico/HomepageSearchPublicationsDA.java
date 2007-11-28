package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;

public class HomepageSearchPublicationsDA  extends SearchPublicationsDA {

    @Override
    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	SearchDSpaceBean bean = createNewBean();
	bean.addSearchElement();

	SearchElement searchElement = bean.getSearchElements().get(0);
	searchElement.setSearchField(SearchField.AUTHOR);

	
	Integer homepageId = getIntegerFromRequest(request, "homepageID");

	DomainObject possibleHomepage = readDomainObject(request, Homepage.class, homepageId);
	if (possibleHomepage instanceof Homepage) {
	    Homepage homepage = (Homepage) possibleHomepage;
	    if (homepage.getActivated() != null && homepage.getActivated()) {
		    request.setAttribute("homepage", homepage);
		    searchElement.setQueryValue(homepage.getPerson().getName());
		}
	    }

	request.setAttribute("bean", bean);
	
	return mapping.findForward("SearchPublication");
    }

    @Override
    protected void setRequestDomainObject(HttpServletRequest request) {
	
	Integer homepageId = getIntegerFromRequest(request, "homepageID");

	DomainObject possibleHomepage = readDomainObject(request, Homepage.class, homepageId);
	if (possibleHomepage instanceof Homepage) {
	    Homepage homepage = (Homepage) possibleHomepage;
	    if (homepage.getActivated() != null && homepage.getActivated()) {
		    request.setAttribute("homepage", homepage);
		}
	    }
    }
}
