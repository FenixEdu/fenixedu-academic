package net.sourceforge.fenixedu.presentationTier.Action.manager.parkingManager;

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

@Mapping(module = "parkingManager", path = "/externalPerson", input = "/externalPerson.do?method=prepareCreate", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showCreateForm", path = "/parkingManager/createExternalPerson.jsp"),
		@Forward(name = "showSearch", path = "/parkingManager/searchForExternalPersonBeforeCreate.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ExternalPersonDAForParkingManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ExternalPersonDA {
}