package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

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

@Mapping(module = "manager", path = "/manageGlossary", input = "/manageGlossary.do?method=prepare", attribute = "glossaryForm", formBean = "glossaryForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "Manage", path = "/manager/manageGlossary_bd.jsp") })
public class ManageGlossaryDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageGlossaryDA {
}