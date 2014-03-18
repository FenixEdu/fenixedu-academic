package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ExecutionPeriodDA;


@Mapping(path = "/mainExams", module = "resourceAllocationManager", input = "/mainExams.do?method=prepare", formBean = "pagedIndexForm")
@Forwards(value = {
	@Forward(name = "showForm", path = "/mainExams.jsp"),
	@Forward(name = "choose", path = "/mainExams.do?method=prepare")})
public class ExecutionPeriodDA_AM2 extends ExecutionPeriodDA {

}
