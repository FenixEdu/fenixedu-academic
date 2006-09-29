package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestSearch;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ParkingManagerDispatchAction extends FenixDispatchAction {
    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        ParkingRequestSearch parkingRequestSearch = (ParkingRequestSearch) getRendererObject();

        if (parkingRequestSearch == null) {
            parkingRequestSearch = new ParkingRequestSearch();
            String parkingRequestState = request.getParameter("parkingRequestState");
            if (!StringUtils.isEmpty(parkingRequestState)) {
                parkingRequestSearch.setParkingRequestState(ParkingRequestState
                        .valueOf(parkingRequestState));
            }
            String parkingPartyClassification = request.getParameter("parkingPartyClassification");
            if (!StringUtils.isEmpty(parkingPartyClassification)) {
                parkingRequestSearch.setParkingPartyClassification(ParkingPartyClassification
                        .valueOf(parkingPartyClassification));
            }
            String personName = request.getParameter("personName");
            if (!StringUtils.isEmpty(personName)) {
                parkingRequestSearch.setPersonName(personName);
            }

            String carPlateNumber = request.getParameter("carPlateNumber");
            if (!StringUtils.isEmpty(carPlateNumber)) {
                parkingRequestSearch.setCarPlateNumber(carPlateNumber);
            }
        }
        request.setAttribute("parkingRequestSearch", parkingRequestSearch);
        return mapping.findForward("showParkingRequests");
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        final String codeString = request.getParameter("idInternal");
        Integer code = null;
        if (codeString == null) {
            code = (Integer) request.getAttribute("idInternal");
        } else {
            code = new Integer(codeString);
        }

        final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
        if (parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
            request.setAttribute("groups", ParkingGroup.getAll());
        }
        request.setAttribute("parkingRequest", parkingRequest);

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String parkingPartyClassification = request.getParameter("parkingPartyClassification");
        if (parkingPartyClassification == null) {
            parkingPartyClassification = "";
        }
        String personName = request.getParameter("personName");
        if (personName == null) {
            personName = "";
        }

        String carPlateNumber = request.getParameter("carPlateNumber");
        if (carPlateNumber == null) {
            carPlateNumber = "";
        }

        request.setAttribute("parkingRequestState", parkingRequestState);
        request.setAttribute("parkingPartyClassification", parkingPartyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);
        return mapping.findForward("showParkingRequest");
    }

    public ActionForward editParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer parkingRequestID = new Integer(request.getParameter("code"));
        String note = request.getParameter("note");
        Object args[] = { parkingRequestID, null, null, null, note };

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String parkingPartyClassification = request.getParameter("parkingPartyClassification");
        if (parkingPartyClassification == null) {
            parkingPartyClassification = "";
        }
        String personName = request.getParameter("personName");
        if (personName == null) {
            personName = "";
        }
        String carPlateNumber = request.getParameter("carPlateNumber");
        if (carPlateNumber == null) {
            carPlateNumber = "";
        }
        request.setAttribute("parkingRequestState", parkingRequestState);
        request.setAttribute("parkingPartyClassification", parkingPartyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);

        if (request.getParameter("accept") != null) {
            Integer cardNumber = null;
            Integer group = null;
            try {
                cardNumber = new Integer(request.getParameter("cardNumber"));
            } catch (NullPointerException e) {
                saveErrorMessage(request, "cardNumber", "error.requiredCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "cardNumber", "error.invalidCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            try {
                group = new Integer(request.getParameter("group"));
            } catch (NullPointerException e) {
                saveErrorMessage(request, "group", "error.requiredGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            if (!isValidCardNumber(cardNumber)) {
                saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            if (!isValidGroup(group)) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            args[1] = ParkingRequestState.ACCEPTED;
            args[2] = cardNumber;
            args[3] = group;
        } else if (request.getParameter("reject") != null) {
            args[1] = ParkingRequestState.REJECTED;
        } else {
            args[1] = ParkingRequestState.PENDING;
        }
        ServiceUtils.executeService(SessionUtils.getUserView(request), "UpdateParkingParty", args);

        return showParkingRequests(mapping, actionForm, request, response);
    }

    private boolean isValidGroup(Integer groupId) {
        for (ParkingGroup group : rootDomainObject.getParkingGroups()) {
            if (group.getIdInternal().equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCardNumber(Integer cardNumber) {
        for (ParkingParty parkingParty : rootDomainObject.getParkingParties()) {
            if (parkingParty.getCardNumber() != null && parkingParty.getCardNumber().equals(cardNumber)) {
                return false;
            }
        }
        return true;
    }

    private void saveErrorMessage(HttpServletRequest request, String error, String errorMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, new ActionMessage(errorMessage));
        saveMessages(request, actionMessages);
    }

    public ActionForward prepareSearchParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchPartyBean", new SearchPartyBean());
        return mapping.findForward("searchParty");
    }

    public ActionForward showParkingPartyRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("searchPartyBean");
        SearchPartyBean searchPartyBean = null;
        if (viewState != null) {
            searchPartyBean = (SearchPartyBean) viewState.getMetaObject().getObject();
        }
        if (searchPartyBean == null) {
            final String codeString = request.getParameter("idInternal");
            if (!StringUtils.isEmpty(codeString)) {
                searchPartyBean = new SearchPartyBean();
                final ParkingRequest parkingRequest = rootDomainObject
                        .readParkingRequestByOID(new Integer(codeString));
                searchPartyBean.setParty(parkingRequest.getParkingParty().getParty());
                searchPartyBean.setPartyName(parkingRequest.getParkingParty().getParty().getName());
            }
        }

        if (searchPartyBean != null) {
            Party party = searchPartyBean.getParty();
            if (party != null
                    && searchPartyBean.getPartyName().equalsIgnoreCase(
                            searchPartyBean.getParty().getName())) {
                setupParkingRequests(request, party);
            } else {
                Map<String, String> map = new HashMap<String, String>();
                map.put("slot", "name");
                Object[] args = { Party.class, searchPartyBean.getPartyName(), 200, map };
                List<Party> partyList = (List<Party>) ServiceManagerServiceFactory.executeService(
                        SessionUtils.getUserView(request), "SearchObjects", args);
                request.setAttribute("partyList", partyList);
            }
        } else if (request.getParameter("idInternal") != null) {
            final Integer idInternal = new Integer(request.getParameter("idInternal"));
            Party party = rootDomainObject.readPartyByOID(idInternal);
            setupParkingRequests(request, party);
        }

        return mapping.findForward("showParkingPartyRequests");
    }

    private void setupParkingRequests(HttpServletRequest request, Party party)
            throws FenixFilterException, FenixServiceException {
        if (party.getParkingParty() != null) {
            List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>(party.getParkingParty()
                    .getParkingRequests());
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("creationDate"), true);
            comparatorChain.addComparator(new BeanComparator("parkingRequestState"));
            Collections.sort(parkingRequests, comparatorChain);
            request.setAttribute("parkingRequests", parkingRequests);
        } else {
            ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
                    "CreateParkingParty", new Object[] { party });
        }
        request.setAttribute("searchPartyBean", new SearchPartyBean(party));
    }
}