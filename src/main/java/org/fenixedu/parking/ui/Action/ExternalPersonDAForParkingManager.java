package org.fenixedu.parking.ui.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ExternalPersonDA;
import net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.parking.domain.ParkingParty;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "parkingManager", path = "/externalPerson", input = "/externalPerson.do?method=prepareCreate",
        functionality = ParkingPartyDA.class)
@Forwards({ @Forward(name = "showCreateForm", path = "/parkingManager/createExternalPerson.jsp"),
        @Forward(name = "showSearch", path = "/parkingManager/searchForExternalPersonBeforeCreate.jsp") })
@Exceptions(@ExceptionHandling(type = DomainException.class, handler = FenixDomainExceptionHandler.class, scope = "request"))
public class ExternalPersonDAForParkingManager extends ExternalPersonDA {

    public ActionForward createExternalPersonAndParkingParty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            create(mapping, form, request, response);
            return createParkingParty(mapping, form, request, response);
        } catch (DomainException ex) {
            final ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage(ex.getKey(), ex.getArgs());
            errors.add("error", error);
            saveErrors(request, errors);

            return mapping.getInputForward();
        }
    }

    public ActionForward createParkingParty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String personIDString = request.getParameter("personID");
        final Person person;
        if (personIDString != null) {
            person = (Person) FenixFramework.getDomainObject(personIDString);
        } else {
            person = (Person) request.getAttribute("person");
        }
        if (person != null) {

            createParkingParty(person);
            final ActionForward actionForward =
                    new ActionForward("/parking.do?plateNumber=&partyID=" + person.getExternalId()
                            + "&method=showParkingPartyRequests");
            return actionForward;
        } else {
            throw new Error("error.no.person.specified");
        }
    }

    @Atomic
    private ParkingParty createParkingParty(Party party) {
        return new ParkingParty(party);
    }
}