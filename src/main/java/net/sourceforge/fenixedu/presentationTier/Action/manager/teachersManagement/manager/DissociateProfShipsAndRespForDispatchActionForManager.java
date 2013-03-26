package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/dissociateProfShipsAndRespFor",
        input = "/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC&page=0", attribute = "teacherManagementForm",
        formBean = "teacherManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareDissociateEC", path = "/manager/teachersManagement/prepareDissociateEC.jsp"),
        @Forward(name = "prepareDissociateECShowProfShipsAndRespFor",
                path = "/manager/teachersManagement/prepareDissociateECShowProfShipsAndRespFor.jsp") })
public class DissociateProfShipsAndRespForDispatchActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.DissociateProfShipsAndRespForDispatchAction {
}