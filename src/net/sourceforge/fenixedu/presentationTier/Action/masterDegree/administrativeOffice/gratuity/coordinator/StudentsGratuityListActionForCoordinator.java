package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "coordinator", path = "/studentsGratuityList",
        input = "/studentsGratuityList.do?method=prepareChooseDegree&page=0", attribute = "studentsGratuityListForm",
        formBean = "studentsGratuityListForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "studentsGratuityList", path = "/coordinator/gratuity/studentsGratuityList.jsp", tileProperties = @Tile(
                title = "teste32")),
        @Forward(name = "choose", path = "/studentsGratuityList.do?method=chooseExecutionYear&page=0", tileProperties = @Tile(
                title = "teste33")),
        @Forward(name = "chooseStudentsGratuityList", path = "/coordinator/gratuity/chooseStudentsGratuityList.jsp",
                tileProperties = @Tile(title = "teste34")) })
public class StudentsGratuityListActionForCoordinator extends
        net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.StudentsGratuityListAction {
}