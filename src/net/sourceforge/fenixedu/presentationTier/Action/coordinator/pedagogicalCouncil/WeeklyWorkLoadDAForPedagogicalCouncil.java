package net.sourceforge.fenixedu.presentationTier.Action.coordinator.pedagogicalCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/weeklyWorkLoad", input = "/weeklyWorkLoad.do?method=prepare",
        attribute = "weeklyWorkLoadForm", formBean = "weeklyWorkLoadForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showWeeklyWorkLoad", path = "/pedagogicalCouncil/weeklyWorkLoad.jsp",
        tileProperties = @Tile(title = "private.pedagogiccouncil.weeklyeffort")) })
public class WeeklyWorkLoadDAForPedagogicalCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.coordinator.WeeklyWorkLoadDA {
}