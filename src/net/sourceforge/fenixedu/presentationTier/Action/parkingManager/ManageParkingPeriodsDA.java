package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean.ParkingCardSearchPeriod;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean.ParkingCardUserState;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

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

public class ManageParkingPeriodsDA extends FenixDispatchAction {

    public ActionForward prepareCardsSearch(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ParkingCardSearchBean parkingCardSearchBean = null;
	if (getRenderedObject("parkingCardSearchBean") != null) {
	    parkingCardSearchBean = (ParkingCardSearchBean) getRenderedObject("parkingCardSearchBean");
	} else {
	    parkingCardSearchBean = new ParkingCardSearchBean();
	}
	request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
	return mapping.findForward("cardsSearch");
    }

    public ActionForward searchCards(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	if (request.getParameter("prepareRenewal") != null) {
	    return prepareCardsRenewal(mapping, actionForm, request, response);
	}
	ParkingCardSearchBean parkingCardSearchBean = (ParkingCardSearchBean) getRenderedObject("parkingCardSearchBean");
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
	    parkingCardSearchBean.setParkingCardUserState(ParkingCardUserState
		    .valueOf(parkingCardUserState));
	}
	String parkingGroupID = request.getParameter("parkingGroupID");
	if (!StringUtils.isEmpty(parkingGroupID)) {
	    parkingCardSearchBean.setParkingGroup(rootDomainObject.readParkingGroupByOID(Integer
		    .valueOf(parkingGroupID)));
	}
	String actualEndDate = request.getParameter("actualEndDate");
	if (!StringUtils.isEmpty(actualEndDate)) {
	    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
	    parkingCardSearchBean.setActualEndDate(dtf.parseDateTime(actualEndDate).toYearMonthDay());
	}
	String parkingCardSearchPeriod = request.getParameter("parkingCardSearchPeriod");
	if (!StringUtils.isEmpty(parkingCardSearchPeriod)) {
	    parkingCardSearchBean.setParkingCardSearchPeriod(ParkingCardSearchPeriod
		    .valueOf(parkingCardSearchPeriod));
	}
	return parkingCardSearchBean;
    }

    public ActionForward prepareCardsRenewal(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	String[] selectedParkingCards = ((DynaActionForm) actionForm).getStrings("selectedParkingCards");
	ParkingCardSearchBean parkingCardSearchBean = (ParkingCardSearchBean) getRenderedObject("parkingCardSearchBean");
	RenderUtils.invalidateViewState();
	parkingCardSearchBean.getSelectedParkingParties().clear();
	for (int iter = 0; iter < selectedParkingCards.length; iter++) {
	    parkingCardSearchBean.getSelectedParkingParties().add(
		    rootDomainObject.readParkingPartyByOID(Integer.valueOf(selectedParkingCards[iter])));
	}
	parkingCardSearchBean.orderSelectedParkingParties();
	request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
	return mapping.findForward("cardsRenewal");
    }

    public ActionForward renewParkingCards(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ParkingCardSearchBean parkingCardSearchBean = (ParkingCardSearchBean) getRenderedObject("parkingCardSearchBean");
	if (request.getParameter("cancel") != null) {
	    return searchCards(mapping, actionForm, request, response);
	}
	ServiceUtils.executeService(SessionUtils.getUserView(request), "RenewParkingCards",
		new Object[] { parkingCardSearchBean.getSelectedParkingParties(),
			parkingCardSearchBean.getRenewalEndDate() });
	parkingCardSearchBean.getSelectedParkingParties().clear();
	parkingCardSearchBean.setRenewalEndDate(null);
	request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
	return searchCards(mapping, actionForm, request, response);
    }

    public ActionForward showParkingDetails(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	ParkingCardSearchBean parkingCardSearchBean = getSearchParameters(request);
	Integer parkingPartyID = Integer.valueOf(request.getParameter("parkingPartyID"));
	request.setAttribute("parkingParty", rootDomainObject.readParkingPartyByOID(parkingPartyID));
	request.setAttribute("parkingCardSearchBean", parkingCardSearchBean);
	return mapping.findForward("showParkingDetails");
    }

    public ActionForward prepareManageRequestsPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	List<ParkingRequestPeriod> parkingRequestPeriods = new ArrayList<ParkingRequestPeriod>(
		rootDomainObject.getParkingRequestPeriods());
	Collections.sort(parkingRequestPeriods, new BeanComparator("beginDate"));
	request.setAttribute("parkingRequestPeriods", parkingRequestPeriods);
	return mapping.findForward("manageRequestsPeriods");
    }

    public ActionForward editRequestPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer parkingRequestPeriodToEditCode = new Integer(request.getParameter("idInternal"));
	request.setAttribute("parkingRequestPeriodToEdit", rootDomainObject
		.readParkingRequestPeriodByOID(parkingRequestPeriodToEditCode));
	return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    public ActionForward deleteRequestPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer parkingRequestPeriodToDeleteCode = new Integer(request.getParameter("idInternal"));
	ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteParkingRequestPeriod",
		new Object[] { parkingRequestPeriodToDeleteCode });
	return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    private void setError(HttpServletRequest request, String errorMsg) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("error", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }
}