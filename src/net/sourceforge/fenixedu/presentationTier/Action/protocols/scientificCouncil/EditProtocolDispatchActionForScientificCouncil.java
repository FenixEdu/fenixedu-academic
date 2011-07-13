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

@Mapping(module = "scientificCouncil", path = "/editProtocol", attribute = "protocolsForm", formBean = "protocolsForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "edit-protocol-units", path = "/protocols/editProtocolUnits.jsp"),
		@Forward(name = "edit-protocol-data", path = "/protocols/editProtocolData.jsp"),
		@Forward(name = "edit-protocol-files", path = "/protocols/editProtocolFiles.jsp"),
		@Forward(name = "edit-protocol-responsibles", path = "/protocols/editProtocolResponsibles.jsp"),
		@Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp") })
public class EditProtocolDispatchActionForScientificCouncil extends net.sourceforge.fenixedu.presentationTier.Action.protocols.EditProtocolDispatchAction {
}