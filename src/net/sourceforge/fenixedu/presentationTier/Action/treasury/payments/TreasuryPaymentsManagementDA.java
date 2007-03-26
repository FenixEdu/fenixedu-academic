package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.SimpleSearchPersonWithStudentBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class TreasuryPaymentsManagementDA extends PaymentsManagementDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("administrativeOffice", getAdministrativeOffice(request));
	request.setAttribute("administrativeOfficeUnit", getAdministrativeOfficeUnit(request));
	return super.execute(mapping, actionForm, request, response);
    }

    private Unit getAdministrativeOfficeUnit(HttpServletRequest request) {
	return (Unit) rootDomainObject.readPartyByOID(getRequestParameterAsInteger(request,
		"administrativeOfficeUnitId"));
    }

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return rootDomainObject.readAdministrativeOfficeByOID(getRequestParameterAsInteger(request,
		"administrativeOfficeId"));
    }

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

	return mapping.findForward("searchPersons");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SimpleSearchPersonWithStudentBean searchPersonBean = (SimpleSearchPersonWithStudentBean) getRenderedObject();
	request.setAttribute("searchPersonBean", searchPersonBean);

	final Collection<Person> persons = searchPerson(request, searchPersonBean);
	if (persons.size() == 1) {
	    final Person person = persons.iterator().next();
	    request.setAttribute("personId", person.getIdInternal());

	    return prepareChooseAdministrativeOffice(mapping, form, request, response);
	}

	request.setAttribute("persons", persons);
	return mapping.findForward("searchPersons");
    }

    private Collection<Person> searchPerson(HttpServletRequest request,
	    final SimpleSearchPersonWithStudentBean searchPersonBean) throws FenixFilterException,
	    FenixServiceException {
	final SearchParameters searchParameters = new SearchPerson.SearchParameters(searchPersonBean
		.getName(), null, searchPersonBean.getUsername(),
		searchPersonBean.getDocumentIdNumber(),
		searchPersonBean.getIdDocumentType() != null ? searchPersonBean.getIdDocumentType()
			.toString() : null, null, null, null, null, null, searchPersonBean
			.getStudentNumber());

	final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	final CollectionPager<Person> result = (CollectionPager<Person>) executeService("SearchPerson",
		searchParameters, predicate);

	return result.getCollection();

    }

    public ActionForward prepareChooseAdministrativeOffice(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getPerson(request);
	request.setAttribute("person", person);
	final Set<AdministrativeOffice> administrativeOffices = getAdminstrativeOfficesFor(person);
	if (administrativeOffices.size() == 1) {
	    request.setAttribute("administrativeOffice", administrativeOffices.iterator().next());
	    return prepareChooseAdministrativeOfficeUnit(mapping, form, request, response);
	} else {
	    request.setAttribute("administrativeOffices", administrativeOffices);
	    return mapping.findForward("chooseAdministrativeOffice");
	}

    }

    public ActionForward prepareChooseAdministrativeOfficeUnit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final AdministrativeOffice administrativeOffice = (AdministrativeOffice) request
		.getAttribute("administrativeOffice");
	final Collection<Unit> unitsForAdministrativeOffice = administrativeOffice.getUnit()
		.getSubUnits();

	if (unitsForAdministrativeOffice.isEmpty()) {
	    request.setAttribute("administrativeOfficeUnit", administrativeOffice.getUnit());
	    return mapping.findForward("showOperations");
	}

	request.setAttribute("unitsForAdministrativeOffice", unitsForAdministrativeOffice);
	return mapping.findForward("chooseAdministrativeOfficeUnit");

    }

    private Set<AdministrativeOffice> getAdminstrativeOfficesFor(final Person person) {
	final Set<AdministrativeOffice> result = new HashSet<AdministrativeOffice>();
	for (final Event event : person.getEventsSet()) {
	    if (event.hasAdministrativeOffice()) {
		result.add(event.getAdministrativeOffice());
	    }
	}

	if (result.isEmpty()) {
	    result.add(AdministrativeOffice
		    .readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE));
	    result.add(AdministrativeOffice
		    .readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE));
	}

	return result;
    }

}
