package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.masterDegreeAdministrativeOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "masterDegreeAdministrativeOffice",
		path = "/studentsGratuityList",
		input = "/studentsGratuityList.do?method=prepareChooseDegree&page=0",
		attribute = "studentsGratuityListForm",
		formBean = "studentsGratuityListForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "studentsGratuityList", path = "studentsGratuityList", tileProperties = @Tile(title = "teste38")),
		@Forward(name = "choose", path = "/studentsGratuityList.do?method=chooseExecutionYear&page=0", tileProperties = @Tile(
				title = "teste39")),
		@Forward(name = "chooseStudentsGratuityList", path = "chooseStudentsGratuityList", tileProperties = @Tile(
				title = "teste40")) })
public class StudentsGratuityListActionForMasterDegreeAdministrativeOffice extends
		net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.StudentsGratuityListAction {
}