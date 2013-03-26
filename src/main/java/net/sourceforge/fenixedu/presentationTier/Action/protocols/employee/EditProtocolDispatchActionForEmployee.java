package net.sourceforge.fenixedu.presentationTier.Action.protocols.employee;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "directiveCouncil", path = "/editProtocol", attribute = "protocolsForm", formBean = "protocolsForm",
        scope = "request", validate = false, parameter = "method")
@Forwards(value = {
        @Forward(name = "edit-protocol-units", path = "/protocols/editProtocolUnits.jsp", tileProperties = @Tile(
                title = "private.employee.protocols.protocols")),
        @Forward(name = "edit-protocol-data", path = "/protocols/editProtocolData.jsp", tileProperties = @Tile(
                title = "private.employee.protocols.protocols")),
        @Forward(name = "edit-protocol-files", path = "/protocols/editProtocolFiles.jsp", tileProperties = @Tile(
                title = "private.employee.protocols.protocols")),
        @Forward(name = "edit-protocol-responsibles", path = "/protocols/editProtocolResponsibles.jsp", tileProperties = @Tile(
                title = "private.employee.protocols.protocols")),
        @Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp", tileProperties = @Tile(
                title = "private.employee.protocols.protocols")) })
public class EditProtocolDispatchActionForEmployee extends
        net.sourceforge.fenixedu.presentationTier.Action.protocols.EditProtocolDispatchAction {
}