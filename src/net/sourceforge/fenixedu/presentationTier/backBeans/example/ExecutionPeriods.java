package net.sourceforge.fenixedu.presentationTier.backBeans.example;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;

public class ExecutionPeriods {

	public Collection getExecutionPeriods() {
		try {

			final Collection infoExecutionPeriods = ReadExecutionPeriods.run();
			return infoExecutionPeriods;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setExecutionPeriods(Collection executionPeriods) {
		throw new RuntimeException("Not implemented");
	}

}