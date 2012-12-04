package net.sourceforge.fenixedu.presentationTier.Action.protocols.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/protocols", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "search-protocols", path = "/protocols/searchProtocols.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.search")),
	@Forward(name = "renew-protocol", path = "/protocols/renewProtocol.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "show-protocols", path = "/protocols/showProtocols.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "show-protocol-alerts", path = "/protocols/showProtocolAlerts.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "create-protocol", path = "/protocols/createProtocol.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "view-protocol-details", path = "/protocols/viewProtocolDetails.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")),
	@Forward(name = "edit-protocol-history", path = "/protocols/editProtocolHistory.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.protocols.protocols")) })
public class ProtocolsDispatchActionForScientificCouncil extends
	net.sourceforge.fenixedu.presentationTier.Action.protocols.ProtocolsDispatchAction {
}