package net.sourceforge.fenixedu.presentationTier.Action.protocols.messaging;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "messaging", path = "/protocols", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "search-protocols", path = "/protocols/searchProtocols.jsp", tileProperties = @Tile(
				title = "private.messaging.search.protocols")),
		@Forward(name = "view-protocol-details", path = "/protocols/viewProtocolDetails.jsp") })
public class ProtocolsDispatchActionForMessaging extends
		net.sourceforge.fenixedu.presentationTier.Action.protocols.ProtocolsDispatchAction {
}