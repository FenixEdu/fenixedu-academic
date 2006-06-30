/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class PaymentsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("searchPerson");
    }

    public ActionForward searchPersonByCandidacyNumber(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer candidacyNumber = getCandidacyNumber(form);
        if (candidacyNumber == null) {
            addActionMessage(request,
                    "error.masterDegreeAdministrativeOffice.payments.invalid.candidacyNumber");
            form.set("candidacyNumber", candidacyNumber);
            return prepareSearchPerson(mapping, actionForm, request, response);
        }
        final Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(
                    request,
                    "error.masterDegreeAdministrativeOffice.payments.invalid.candidacyNumber.withNumber",
                    candidacyNumber.toString());
            form.set("candidacyNumber", candidacyNumber);
            return prepareSearchPerson(mapping, actionForm, request, response);
        }

        request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(candidacy
                .getPerson()));
        return mapping.findForward("showEvents");
    }

    public ActionForward searchPersonByUsername(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String username = (String) form.get("username");
        final Person person = getPersonByUsername(username);

        if (person == null) {
            addActionMessage(request, "error.masterDegreeAdministrativeOffice.payments.person.not.found");
            return prepareSearchPerson(mapping, actionForm, request, response);
        }

        request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(person));
        return mapping.findForward("showEvents");
    }

    public ActionForward searchPersonByDocumentIDandDocumentType(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final IDDocumentType documentType = IDDocumentType.valueOf(form.getString("documentType"));
        final String documentNumber = (String) form.get("documentNumber");

        final Person person = getPersonByDocumentNumberIdAndDocumentType(documentNumber, documentType);

        if (person == null) {
            addActionMessage(request, "error.masterDegreeAdministrativeOffice.payments.person.not.found");
            return prepareSearchPerson(mapping, actionForm, request, response);
        }

        request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(person));
        return mapping.findForward("showEvents");
    }

    protected Integer getCandidacyNumber(final DynaActionForm form) {
        final String candidacyNumberString = (String) form.get("candidacyNumber");
        return (candidacyNumberString != null) ? Integer.valueOf(candidacyNumberString) : null;
    }

    protected Person getPersonByUsername(String username) {
        return Person.readPersonByUsername(username);
    }

    protected Person getPersonByDocumentNumberIdAndDocumentType(String documentNumber,
            IDDocumentType documentType) {
        return Person.readByDocumentIdNumberAndIdDocumentType(documentNumber, documentType);
    }

    protected Person getPersonByCandidacyNumber(Integer candidacyNumber) {
        final Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        return (candidacy != null) ? candidacy.getPerson() : null;
    }

    protected PaymentsManagementDTO searchNotPayedEventsForPerson(Person person) {
        final PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
        for (final Event event : person.getNotPayedEvents()) {
            paymentsManagementDTO.addEntryDTOs(event.calculateEntries());
        }

        return paymentsManagementDTO;
    }

}
