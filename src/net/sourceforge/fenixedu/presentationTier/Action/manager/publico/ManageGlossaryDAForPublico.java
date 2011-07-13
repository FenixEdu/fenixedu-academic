package net.sourceforge.fenixedu.presentationTier.Action.manager.publico;

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

@Mapping(module = "publico", path = "/supportGlossary", input = "/supportGlossary.do?method=prepare&page=0", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Manage", path = "df.page.support-glossary") })
public class ManageGlossaryDAForPublico extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageGlossaryDA {
}