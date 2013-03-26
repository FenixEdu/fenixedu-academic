package net.sourceforge.fenixedu.presentationTier.Action.research.publico;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/prizes/showPrizes", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewPrize", path = "view.prize") })
public class PrizeManagementForPublico extends net.sourceforge.fenixedu.presentationTier.Action.research.PrizeManagement {
}