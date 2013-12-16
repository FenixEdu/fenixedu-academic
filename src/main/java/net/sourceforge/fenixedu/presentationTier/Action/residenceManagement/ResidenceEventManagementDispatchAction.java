package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CancelResidenceEvent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.CreateResidencePaymentCodes;
import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.PayResidenceEvent;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/residenceEventManagement", module = "residenceManagement")
@Forwards({
        @Forward(name = "manageResidenceEvents", path = "/residenceManagement/eventsManagement.jsp", tileProperties = @Tile(
                title = "private.housingmanagement.debtmanagement")),
        @Forward(name = "viewPersonResidenceEvents", path = "/residenceManagement/viewPersonResidenceEvents.jsp",
                tileProperties = @Tile(title = "private.housingmanagement.debtmanagement")),
        @Forward(name = "insertPayingDate", path = "/residenceManagement/insertPayingDate.jsp") })
public class ResidenceEventManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ImportResidenceEventBean importResidenceEventBean = getRenderedObject("searchEventMonth");
        if (importResidenceEventBean == null) {
            ResidenceMonth month = getResidenceMonth(request);
            importResidenceEventBean = month != null ? new ImportResidenceEventBean(month) : new ImportResidenceEventBean();
        }

        RenderUtils.invalidateViewState();

        request.setAttribute("currentResidence", getManagementUnit(request));
        request.setAttribute("searchBean", importResidenceEventBean);
        return mapping.findForward("manageResidenceEvents");
    }

    private ResidenceManagementUnit getManagementUnit(HttpServletRequest request) {
        return Bennu.getInstance().getResidenceManagementUnit();
    }

    public ActionForward generatePaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResidenceMonth month = getResidenceMonth(request);
        CreateResidencePaymentCodes.run(month.getEvents());

        return manageResidenceEvents(mapping, actionForm, request, response);
    }

    private ResidenceMonth getResidenceMonth(HttpServletRequest request) {
        String oid = request.getParameter("monthOID");
        return oid == null ? null : (ResidenceMonth) FenixFramework.getDomainObject(oid);
    }

    public ActionForward viewPersonResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ResidenceMonth month = getResidenceMonth(request);
        String personOID = request.getParameter("person");

        Person person = FenixFramework.getDomainObject(personOID);
        Set<Event> events = person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, false);
        events.addAll(person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, true));
        events.addAll(person.getPayedEvents(ResidenceEvent.class));

        request.setAttribute("month", month);
        request.setAttribute("person", person);
        request.setAttribute("residenceEvents", events);
        return mapping.findForward("viewPersonResidenceEvents");
    }

    public ActionForward cancelResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(request.getParameter("event"));

        try {
            CancelResidenceEvent.run(residenceEvent, AccessControl.getPerson());
        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        }

        return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

    public ActionForward preparePayResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(request.getParameter("event"));
        VariantBean bean = new VariantBean();
        bean.setYearMonthDay(new YearMonthDay());
        ResidenceMonth month = getResidenceMonth(request);

        request.setAttribute("month", month);
        request.setAttribute("residenceEvent", residenceEvent);
        request.setAttribute("bean", bean);
        return mapping.findForward("insertPayingDate");

    }

    public ActionForward payResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResidenceEvent residenceEvent = (ResidenceEvent) FenixFramework.getDomainObject(request.getParameter("event"));
        YearMonthDay date = getRenderedObject("date");

        try {
            PayResidenceEvent.run(getLoggedPerson(request).getUser(), residenceEvent, date);
        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        }

        return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

}