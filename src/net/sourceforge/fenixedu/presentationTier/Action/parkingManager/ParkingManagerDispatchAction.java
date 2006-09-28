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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ParkingManagerDispatchAction extends FenixDispatchAction {
    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        ParkingRequestSearch parkingRequestSearch = (ParkingRequestSearch) getRendererObject();

        if (parkingRequestSearch == null) {
            parkingRequestSearch = new ParkingRequestSearch();
        }

        request.setAttribute("parkingRequestSearch", parkingRequestSearch);
        return mapping.findForward("showParkingRequests");
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        final Integer code = new Integer(request.getParameter("idInternal"));
        final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
        if (parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
            request.setAttribute("groups", ParkingGroup.getAll());
        }
        request.setAttribute("parkingRequest", parkingRequest);
        return mapping.findForward("showParkingRequest");
    }

    public ActionForward editParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer parkingRequestID = new Integer(request.getParameter("code"));
        String note = request.getParameter("note");
        Object args[] = { parkingRequestID, null, null, null, note };
        if (request.getParameter("accept") != null) {
            Integer cardNumber = new Integer(request.getParameter("cardNumber"));
            Integer group = new Integer(request.getParameter("group"));
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