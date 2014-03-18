package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ChooseExecutionCourseAction;


@Mapping(path = "/escolherDisciplinaETipoForm", module = "resourceAllocationManager", input = "/escolherDisciplinaExecucao.jsp", formBean = "chooseExecutionCourseForm")
@Forwards(value = {
	@Forward(name = "showForm", path = "/escolherDisciplinaExecucao.jsp")})
public class ChooseExecutionCourseAction_AM3 extends ChooseExecutionCourseAction {

}
