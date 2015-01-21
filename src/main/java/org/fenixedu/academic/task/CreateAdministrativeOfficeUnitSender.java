package org.fenixedu.academic.task;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.ist.fenixframework.FenixFramework;

public class CreateAdministrativeOfficeUnitSender extends CustomTask {

	@Override
	public void runTask() throws Exception {
		Unit administrativeOfficeUnit = FenixFramework.getDomainObject("285241663029249");
		if (administrativeOfficeUnit.getUnitBasedSenderSet().isEmpty()) {
            UnitBasedSender.newInstance(administrativeOfficeUnit);
        }
	}

}
