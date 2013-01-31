package net.sourceforge.fenixedu.domain.alumni;

import pt.ist.fenixWebFramework.services.Service;

public class AlumniReportFileBean {

	@Service
	public static AlumniReportFile launchJob(boolean fullReport, boolean onlyRegisteredAlumni) {
		return AlumniReportFile.launchJob(fullReport, onlyRegisteredAlumni);
	}

}
