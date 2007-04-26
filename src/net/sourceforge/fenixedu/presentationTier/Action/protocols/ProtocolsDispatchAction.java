package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolSearch;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolHistoryFactory.ProtocolHistoryEditorFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolHistoryFactory.ProtocolHistoryRenewerFactory;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.Months;
import org.joda.time.YearMonthDay;

public class ProtocolsDispatchAction extends FenixDispatchAction {

    public ActionForward showProtocols(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Protocol> protocolList = new ArrayList<Protocol>();
        ProtocolSearch protocolSearch = (ProtocolSearch) getRenderedObject();
        if (protocolSearch == null) {
            protocolSearch = new ProtocolSearch();
            protocolSearch.setActives(true);
        }
        if (protocolSearch.isActives() || protocolSearch.isInactives()) {
            int iter = 1;
            for (Protocol protocol : rootDomainObject.getProtocols()) {
                if (protocol.isActive() && protocolSearch.isActives()) {
                    protocolList.add(protocol);
                } else if (protocolSearch.isInactives() && !protocol.isActive()) {
                    protocolList.add(protocol);
                }
            }
        }
        request.setAttribute("protocolSearch", protocolSearch);
        request.setAttribute("protocols", protocolList);
        return mapping.findForward("show-protocols");
    }

    public ActionForward showProtocolAlerts(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        YearMonthDay now = new YearMonthDay();
        List<ProtocolHistory> protocolHistories = new ArrayList<ProtocolHistory>();
        List<ProtocolHistory> protocolHistoriesNullEndDate = new ArrayList<ProtocolHistory>();
        for (Protocol protocol : rootDomainObject.getProtocols()) {
            ProtocolHistory protocolHistory = protocol.getLastProtocolHistory();
            if (protocolHistory.getEndDate() == null) {
                if (protocol.isActive()) {
                    protocolHistoriesNullEndDate.add(protocolHistory);
                }
            } else if (!protocolHistory.getEndDate().isBefore(now)
                    && Months.monthsBetween(now, protocolHistory.getEndDate()).getMonths() <= 1) {
                protocolHistories.add(protocolHistory);
            }
        }
        Collections.sort(protocolHistories, new BeanComparator("endDate"));
        request.setAttribute("protocolHistories", protocolHistories);
        request.setAttribute("protocolHistoriesNullEndDate", protocolHistoriesNullEndDate);
        return mapping.findForward("show-protocol-alerts");
    }

    public ActionForward prepareRenewProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer protocolId = new Integer(request.getParameter("idInternal"));
        final Protocol protocol = rootDomainObject.readProtocolByOID(protocolId);
        ProtocolHistoryRenewerFactory protocolHistoryFactory = new ProtocolHistoryRenewerFactory(
                protocol);
        request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
        return mapping.findForward("renew-protocol");
    }

    public ActionForward renewProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return showProtocolAlerts(mapping, actionForm, request, response);
        }
        ProtocolHistoryRenewerFactory protocolHistoryFactory = (ProtocolHistoryRenewerFactory) getRenderedObject("protocolHistoryFactory");
        ActionMessage actionMessage = (ActionMessage) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolHistoryFactory });
        if (actionMessage != null) {
            setError(request, "message", actionMessage);
            request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
            return mapping.findForward("renew-protocol");
        }
        request.setAttribute("protocols", rootDomainObject.getProtocols());
        return showProtocolAlerts(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditProtocolHistory(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer protocolId = new Integer(request.getParameter("idInternal"));
        final Protocol protocol = rootDomainObject.readProtocolByOID(protocolId);
        ProtocolHistoryEditorFactory protocolHistoryFactory = new ProtocolHistoryEditorFactory(protocol);
        request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
        return mapping.findForward("edit-protocol-history");
    }

    public ActionForward editProtocolHistory(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return showProtocolAlerts(mapping, actionForm, request, response);
        }
        ProtocolHistoryEditorFactory protocolHistoryFactory = (ProtocolHistoryEditorFactory) getRenderedObject("protocolHistoryFactory");
        ActionMessage actionMessage = (ActionMessage) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolHistoryFactory });
        if (actionMessage != null) {
            setError(request, "message", actionMessage);
            request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
            return mapping.findForward("edit-protocol-history");
        }
        request.setAttribute("protocols", rootDomainObject.getProtocols());
        return showProtocolAlerts(mapping, actionForm, request, response);
    }

    public ActionForward searchProtocols(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolSearch protocolSearch = (ProtocolSearch) getRenderedObject("protocolSearch");
        if (protocolSearch == null) {
            protocolSearch = new ProtocolSearch();
        }
        request.setAttribute("protocolSearch", protocolSearch);
        return mapping.findForward("search-protocols");
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}