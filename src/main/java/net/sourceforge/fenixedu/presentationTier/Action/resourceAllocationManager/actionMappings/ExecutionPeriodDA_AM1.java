package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ExecutionPeriodDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/chooseExecutionPeriod", module = "resourceAllocationManager",
        input = "/chooseExecutionPeriod.do?method=prepare", formBean = "pagedIndexForm")
@Forwards(
        value = { @Forward(name = "showForm", path = "/chooseExecutionPeriod.jsp", useTile = false),
                @Forward(name = "toggleFirstYearShiftsCapacity", path = "/toggleFirstYearShiftsCapacity.jsp", useTile = false),
                @Forward(name = "viewRoom", path = "/sopViewRoom.jsp"),
                @Forward(name = "showTimeTable", path = "df.page.showTimeTable") })
public class ExecutionPeriodDA_AM1 extends ExecutionPeriodDA {

}
