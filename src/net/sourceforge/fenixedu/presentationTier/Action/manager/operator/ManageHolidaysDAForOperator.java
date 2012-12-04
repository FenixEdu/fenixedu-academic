package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "operator", path = "/manageHolidays", input = "/manageHolidays.do?method=prepare&page=0", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showHolidays", path = "/manager/showHolidays.jsp", tileProperties = @Tile(title = "private.operator.personnelmanagement.managementholidays")) })
public class ManageHolidaysDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageHolidaysDA {
}