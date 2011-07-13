package net.sourceforge.fenixedu.presentationTier.Action.research.activity.publico;

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

@Mapping(module = "publico", path = "/activities/editResearchActivity", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "EditResearchActivity", path = "show-participationDetails") })
public class EditResearchActivityDispatchActionForPublico extends net.sourceforge.fenixedu.presentationTier.Action.research.activity.EditResearchActivityDispatchAction {
}