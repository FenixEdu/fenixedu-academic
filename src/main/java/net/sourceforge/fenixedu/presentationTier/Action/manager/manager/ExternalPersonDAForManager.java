package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/externalPerson", input = "/externalPerson.do?method=prepareCreate", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "showCreateForm", path = "/manager/createExternalPerson.jsp"),
        @Forward(name = "showSearch", path = "/manager/searchForExternalPersonBeforeCreate.jsp"),
        @Forward(name = "showCreatedPerson", path = "/manager/showCreateExternalPerson.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ExternalPersonDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ExternalPersonDA {
}