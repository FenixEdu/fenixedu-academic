package org.fenixedu.academic.task;

import java.util.Locale;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

public class LoadMissingAcademicEntities extends CustomTask {

	@Override
	public void runTask() throws Exception {
		RegistrationProtocol protocol = new RegistrationProtocol("001", new LocalizedString(Locale.getDefault(), "Normal"), true, true, false, false, false, false, false, false, false, false, false);
		for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
			registration.setRegistrationProtocol(protocol);
		}
	}

}
