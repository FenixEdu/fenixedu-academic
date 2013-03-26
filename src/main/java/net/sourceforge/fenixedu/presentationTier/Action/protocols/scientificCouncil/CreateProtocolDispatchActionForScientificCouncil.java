package net.sourceforge.fenixedu.presentationTier.Action.protocols.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/createProtocol", attribute = "protocolsForm", formBean = "protocolsForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareCreate-protocol-units", path = "/protocols/prepareCreateProtocolUnits.jsp",
                tileProperties = @Tile(title = "private.scientificcouncil.protocols")),
        @Forward(name = "prepareCreate-protocol-data", path = "/protocols/prepareCreateProtocolData.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "show-protocols", path = "/protocols/showProtocols.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "prepareCreate-protocol-files", path = "/protocols/prepareCreateProtocolFiles.jsp",
                tileProperties = @Tile(title = "private.scientificcouncil.protocols")),
        @Forward(name = "prepareCreate-protocol-responsibles", path = "/protocols/prepareCreateProtocolResponsibles.jsp",
                tileProperties = @Tile(title = "private.scientificcouncil.protocols")) })
public class CreateProtocolDispatchActionForScientificCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.protocols.CreateProtocolDispatchAction {
}