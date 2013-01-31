package net.sourceforge.fenixedu.presentationTier.Action.research.publico;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/showEvents", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showEvent", path = "show.event") })
public class ResearchEventManagementForPublico extends
		net.sourceforge.fenixedu.presentationTier.Action.research.ResearchEventManagement {
}