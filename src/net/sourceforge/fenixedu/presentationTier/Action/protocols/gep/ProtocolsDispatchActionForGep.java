package net.sourceforge.fenixedu.presentationTier.Action.protocols.gep;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "gep", path = "/protocols", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search-protocols", path = "/protocols/searchProtocols.jsp", tileProperties = @Tile(  title = "private.gep.protocols.search")) })
public class ProtocolsDispatchActionForGep extends
	net.sourceforge.fenixedu.presentationTier.Action.protocols.ProtocolsDispatchAction {
}