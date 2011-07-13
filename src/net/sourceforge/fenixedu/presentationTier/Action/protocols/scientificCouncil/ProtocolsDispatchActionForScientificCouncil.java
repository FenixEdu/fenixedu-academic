package net.sourceforge.fenixedu.presentationTier.Action.protocols.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/protocols", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "search-protocols", path = "/protocols/searchProtocols.jsp"),
		@Forward(name = "renew-protocol", path = "/protocols/renewProtocol.jsp"),
		@Forward(name = "show-protocols", path = "/protocols/showProtocols.jsp"),
		@Forward(name = "show-protocol-alerts", path = "/protocols/showProtocolAlerts.jsp"),
		@Forward(name = "create-protocol", path = "/protocols/createProtocol.jsp"),
		@Forward(name = "view-protocol-details", path = "/protocols/viewProtocolDetails.jsp"),
		@Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp"),
		@Forward(name = "edit-protocol-history", path = "/protocols/editProtocolHistory.jsp") })
public class ProtocolsDispatchActionForScientificCouncil extends net.sourceforge.fenixedu.presentationTier.Action.protocols.ProtocolsDispatchAction {
}