package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.parking.DeleteParkingRequestPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.parking.RenewParkingCards;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean.ParkingCardSearchPeriod;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean.ParkingCardUserState;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "parkingManager", path = "/manageParkingPeriods", input = "/exportParkingDB.do?method=prepareExportFile",
        attribute = "parkingRenewalForm", formBean = "parkingRenewalForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "manageRequestsPeriods", path = "/parkingManager/manageRequestsPeriods.jsp", tileProperties = @Tile(
                title = "private.parking.managingdemandperiods")),
        @Forward(name = "cardsRenewal", path = "/parkingManager/cardsRenewal.jsp"),
        @Forward(name = "showParkingDetails", path = "/parkingManager/showParkingDetails.jsp"),
        @Forward(name = "cardsSearch", path = "/parkingManager/cardsSearch.jsp", tileProperties = @Tile(
                title = "private.parking.parkingcards")) })
public class ManageParkingPeriodsDA extends FenixDispatchAction {

    public ActionForward prepareCardsSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ParkingCardSearchBean parkingCardSearchBean = null;
        if (getRenderedObject("parkingCardSearchBean") != null) {
            parkingCardSearchBean = getRenderedObject("parkingCardSearchBean");
        } else {
            parkingCardSearchBean = new ParkingCardSearchBean();
        }
        request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
        return mapping.findForward("cardsSearch");
    }

    public ActionForward searchCards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (request.getParameter("prepareRenewal") != null) {
            return prepareCardsRenewal(mapping, actionForm, request, response);
        }
        ParkingCardSearchBean parkingCardSearchBean = getRenderedObject("parkingCardSearchBean");
        RenderUtils.invalidateViewState();
        if (parkingCardSearchBean == null) {
            parkingCardSearchBean = getSearchParameters(request);
        }
        parkingCardSearchBean.doSearch();
        parkingCardSearchBean.orderSearchedParkingParties();
        request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
        return mapping.findForward("cardsSearch");
    }

    private ParkingCardSearchBean getSearchParameters(HttpServletRequest request) {
        ParkingCardSearchBean parkingCardSearchBean = new ParkingCardSearchBean();
        String parkingCardUserState = request.getParameter("parkingCardUserState");
        if (!StringUtils.isEmpty(parkingCardUserState)) {
            parkingCardSearchBean.setParkingCardUserState(ParkingCardUserState.valueOf(parkingCardUserState));
        }
        String parkingGroupID = request.getParameter("parkingGroupID");
        if (!StringUtils.isEmpty(parkingGroupID)) {
            parkingCardSearchBean.setParkingGroup(FenixFramework.<ParkingGroup> getDomainObject(parkingGroupID));
        }
        String actualEndDate = request.getParameter("actualEndDate");
        if (!StringUtils.isEmpty(actualEndDate)) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
            parkingCardSearchBean.setActualEndDate(dtf.parseDateTime(actualEndDate).toYearMonthDay());
        }
        String parkingCardSearchPeriod = request.getParameter("parkingCardSearchPeriod");
        if (!StringUtils.isEmpty(parkingCardSearchPeriod)) {
            parkingCardSearchBean.setParkingCardSearchPeriod(ParkingCardSearchPeriod.valueOf(parkingCardSearchPeriod));
        }
        return parkingCardSearchBean;
    }

    public ActionForward prepareCardsRenewal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String[] selectedParkingCards = ((DynaActionForm) actionForm).getStrings("selectedParkingCards");
        ParkingCardSearchBean parkingCardSearchBean = getRenderedObject("parkingCardSearchBean");
        RenderUtils.invalidateViewState();
        parkingCardSearchBean.getSelectedParkingParties().clear();
        for (String selectedParkingCard : selectedParkingCards) {
            parkingCardSearchBean.getSelectedParkingParties().add(
                    FenixFramework.<ParkingParty> getDomainObject(selectedParkingCard));
        }
        if (parkingCardSearchBean.getSelectedParkingParties().isEmpty()) {
            setMessage(request, "message.noParkingPartiesSelected");
            request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
            return mapping.findForward("cardsSearch");
        }
        parkingCardSearchBean.orderSelectedParkingParties();
        request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
        return mapping.findForward("cardsRenewal");
    }

    public ActionForward renewParkingCards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ParkingCardSearchBean parkingCardSearchBean = getRenderedObject("parkingCardSearchBean");
        if (request.getParameter("cancel") != null) {
            return searchCards(mapping, actionForm, request, response);
        }
        if (request.getParameter("remove") != null) {
            String[] parkingCardsToRemove = ((DynaActionForm) actionForm).getStrings("parkingCardsToRemove");
            for (String element : parkingCardsToRemove) {
                parkingCardSearchBean.removeSelectedParkingParty(element);
            }
            request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
            return mapping.findForward("cardsRenewal");
        }
        RenewParkingCards.run(parkingCardSearchBean.getSelectedParkingParties(), parkingCardSearchBean.getRenewalEndDate(),
                parkingCardSearchBean.getNewParkingGroup(), parkingCardSearchBean.getEmailText());
        parkingCardSearchBean.getSelectedParkingParties().clear();
        parkingCardSearchBean.setRenewalEndDate(null);
        parkingCardSearchBean.setNewParkingGroup(null);
        request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
        return searchCards(mapping, actionForm, request, response);
    }

    public ActionForward showParkingDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ParkingCardSearchBean parkingCardSearchBean = getSearchParameters(request);
        request.setAttribute("parkingParty", FenixFramework.getDomainObject(request.getParameter("parkingPartyID")));
        request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
        return mapping.findForward("showParkingDetails");
    }

    public ActionForward prepareManageRequestsPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<ParkingRequestPeriod> parkingRequestPeriods =
                new ArrayList<ParkingRequestPeriod>(rootDomainObject.getParkingRequestPeriodsSet());
        Collections.sort(parkingRequestPeriods, new BeanComparator("beginDate"));
        request.setAttribute("parkingRequestPeriods", parkingRequestPeriods);
        return mapping.findForward("manageRequestsPeriods");
    }

    public ActionForward editRequestPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("parkingRequestPeriodToEdit", FenixFramework.getDomainObject(request.getParameter("externalId")));
        return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    public ActionForward deleteRequestPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DeleteParkingRequestPeriod.run(request.getParameter("externalId"));
        return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    private void setMessage(HttpServletRequest request, String msg) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("message", new ActionMessage(msg));
        saveMessages(request, actionMessages);
    }
}