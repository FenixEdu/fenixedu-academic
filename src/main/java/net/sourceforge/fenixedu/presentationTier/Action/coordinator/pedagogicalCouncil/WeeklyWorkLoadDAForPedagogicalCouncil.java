package net.sourceforge.fenixedu.presentationTier.Action.coordinator.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.WeeklyWorkLoadDA;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "weekly-work-load", titleKey = "link.weekly.work.load",
        bundle = "ApplicationResources")
@Mapping(module = "pedagogicalCouncil", path = "/weeklyWorkLoad", input = "/weeklyWorkLoad.do?method=prepare",
        formBean = "weeklyWorkLoadForm")
@Forwards(value = { @Forward(name = "showWeeklyWorkLoad", path = "/pedagogicalCouncil/weeklyWorkLoad.jsp") })
public class WeeklyWorkLoadDAForPedagogicalCouncil extends WeeklyWorkLoadDA {
}