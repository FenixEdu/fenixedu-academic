package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.documents;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.SimpleSearchPersonWithStudentBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/generatedDocuments", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {

@Forward(name = "searchPerson", path = "/academicAdminOffice/generatedDocuments/searchPerson.jsp"),

@Forward(name = "showAnnualIRSDocuments", path = "/academicAdminOffice/generatedDocuments/showAnnualIRSDocuments.jsp")

})
public class GeneratedDocumentsDA extends FenixDispatchAction {

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

	return mapping.findForward("searchPerson");
    }

    public ActionForward prepareSearchPersonInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchPersonBean", getObjectFromViewState("searchPersonBean"));

	return mapping.findForward("searchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final SimpleSearchPersonWithStudentBean searchPersonBean = (SimpleSearchPersonWithStudentBean) getObjectFromViewState("searchPersonBean");
	request.setAttribute("searchPersonBean", searchPersonBean);

	request.setAttribute("persons", searchPerson(request, searchPersonBean));

	return mapping.findForward("searchPerson");
    }

    @SuppressWarnings("unchecked")
    private Collection<Person> searchPerson(HttpServletRequest request, SimpleSearchPersonWithStudentBean searchPersonBean)
	    throws FenixFilterException, FenixServiceException {
	final SearchParameters searchParameters = new SearchPerson.SearchParameters(searchPersonBean.getName(), null,
		searchPersonBean.getUsername(), searchPersonBean.getDocumentIdNumber(),
		searchPersonBean.getIdDocumentType() != null ? searchPersonBean.getIdDocumentType().toString() : null, null,
		null, null, null, null, searchPersonBean.getStudentNumber(), Boolean.FALSE);

	final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	final CollectionPager<Person> result = (CollectionPager<Person>) executeService("SearchPerson", new Object[] {
		searchParameters, predicate });

	return result.getCollection();
    }

    public ActionForward showAnnualIRSDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	request.setAttribute("person", getPerson(request));

	request.setAttribute("annualIRSDocuments", getPerson(request).getAnnualIRSDocuments());

	return mapping.findForward("showAnnualIRSDocuments");
    }

    private Person getPerson(HttpServletRequest request) {
	return (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));
    }

}
