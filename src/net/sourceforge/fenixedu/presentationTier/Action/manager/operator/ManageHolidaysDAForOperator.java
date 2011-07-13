package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

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

@Mapping(module = "operator", path = "/manageHolidays", input = "/manageHolidays.do?method=prepare&page=0", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showHolidays", path = "/manager/showHolidays.jsp") })
public class ManageHolidaysDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageHolidaysDA {
}