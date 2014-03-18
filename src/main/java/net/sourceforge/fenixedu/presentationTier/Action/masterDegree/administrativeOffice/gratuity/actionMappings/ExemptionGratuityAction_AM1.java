package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.ExemptionGratuityAction;


@Mapping(path = "/readStudent", module = "masterDegreeAdministrativeOffice", input = "/readStudent.do?method=prepareReadStudent&page=0", formBean = "chooseStudentGratuityForm")
@Forwards(value = {
	@Forward(name = "chooseStudent", path = "chooseStudent"),
	@Forward(name = "readExemptionGratuity", path = "/manageExemptionGratuity.do?method=readExemptionGratuity&page=0"),
	@Forward(name = "chooseStudentCurricularPlan", path = "chooseStudentCurricularPlan")})
public class ExemptionGratuityAction_AM1 extends ExemptionGratuityAction {

}
