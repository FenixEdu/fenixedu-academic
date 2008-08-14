package net.sourceforge.fenixedu.presentationTier.backBeans.example;

import java.util.Collection;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

public class ExecutionPeriods {

    public Collection getExecutionPeriods() {
	try {
	    final Object[] args = {};
	    final Collection infoExecutionPeriods = (Collection) ServiceUtils.executeService("ReadExecutionPeriods", args);
	    return infoExecutionPeriods;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void setExecutionPeriods(Collection executionPeriods) {
	throw new RuntimeException("Not implemented");
    }

}
