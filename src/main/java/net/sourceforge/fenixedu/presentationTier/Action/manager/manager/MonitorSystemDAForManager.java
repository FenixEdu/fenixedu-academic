package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/monitorSystem", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Show", path = "/manager/monitorSystem_bd.jsp") })
public class MonitorSystemDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.MonitorSystemDA {
}