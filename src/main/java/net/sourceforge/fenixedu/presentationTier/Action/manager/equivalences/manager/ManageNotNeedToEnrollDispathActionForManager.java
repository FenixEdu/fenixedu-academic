package net.sourceforge.fenixedu.presentationTier.Action.manager.equivalences.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/showNotNeedToEnroll", attribute = "equivalencesForm", formBean = "equivalencesForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showNotNeedToEnroll", path = "/manager/equivalences/notNeedToEnroll.jsp"),
        @Forward(name = "insertNotNeedToEnroll", path = "/showNotNeedToEnroll.do?method=prepareNotNeedToEnroll") })
public class ManageNotNeedToEnrollDispathActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.equivalences.ManageNotNeedToEnrollDispathAction {
}