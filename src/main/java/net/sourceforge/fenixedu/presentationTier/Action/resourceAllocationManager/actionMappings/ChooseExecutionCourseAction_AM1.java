package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ChooseExecutionCourseAction;


@Mapping(path = "/escolherDisciplinaExecucaoForm", module = "resourceAllocationManager", input = "/escolherDisciplinaExecucao.jsp", formBean = "chooseExecutionCourseForm")
@Forwards(value = {
	@Forward(name = "forwardChoose", path = "/escolherAulas.do"),
	@Forward(name = "showForm", path = "/escolherDisciplinaExecucao.jsp")})
public class ChooseExecutionCourseAction_AM1 extends ChooseExecutionCourseAction {

}
