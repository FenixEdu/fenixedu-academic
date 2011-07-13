package net.sourceforge.fenixedu.presentationTier.Action.protocols.employee;

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

@Mapping(module = "employee", path = "/createProtocol", attribute = "protocolsForm", formBean = "protocolsForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareCreate-protocol-units", path = "/protocols/prepareCreateProtocolUnits.jsp"),
		@Forward(name = "prepareCreate-protocol-data", path = "/protocols/prepareCreateProtocolData.jsp"),
		@Forward(name = "show-protocols", path = "/protocols/showProtocols.jsp"),
		@Forward(name = "view-protocol", path = "/protocols/viewProtocol.jsp"),
		@Forward(name = "prepareCreate-protocol-files", path = "/protocols/prepareCreateProtocolFiles.jsp"),
		@Forward(name = "prepareCreate-protocol-responsibles", path = "/protocols/prepareCreateProtocolResponsibles.jsp") })
public class CreateProtocolDispatchActionForEmployee extends net.sourceforge.fenixedu.presentationTier.Action.protocols.CreateProtocolDispatchAction {
}