package net.sourceforge.fenixedu.presentationTier.Action.protocols.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/editProtocol", attribute = "protocolsForm", formBean = "protocolsForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "edit-protocol-units", path = "/protocols/editProtocolUnits.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "edit-protocol-data", path = "/protocols/editProtocolData.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "edit-protocol-files", path = "/protocols/editProtocolFiles.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "edit-protocol-responsibles", path = "/protocols/editProtocolResponsibles.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")),
        @Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.protocols")) })
public class EditProtocolDispatchActionForScientificCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.protocols.EditProtocolDispatchAction {
}