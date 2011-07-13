package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.operator;

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

@Mapping(module = "operator", path = "/dissociateProfShipsAndRespFor", input = "/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC&page=0", attribute = "teacherManagementForm", formBean = "teacherManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareDissociateEC", path = "df.page.prepareDissociateEC"),
		@Forward(name = "prepareDissociateECShowProfShipsAndRespFor", path = "df.page.prepareDissociateECShowProfShipsAndRespFor") })
public class DissociateProfShipsAndRespForDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.DissociateProfShipsAndRespForDispatchAction {
}