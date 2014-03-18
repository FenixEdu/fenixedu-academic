package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.SelectRoomsFormAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/pesquisarSala", module = "resourceAllocationManager", input = "/pesquisarSalas.jsp", attribute = "roomForm",
        formBean = "roomForm", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "/manipularSalas.jsp", useTile = false) })
public class SelectRoomsFormAction_AM1 extends SelectRoomsFormAction {

}
