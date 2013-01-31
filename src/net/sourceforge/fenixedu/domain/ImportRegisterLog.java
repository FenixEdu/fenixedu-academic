package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class ImportRegisterLog extends ImportRegisterLog_Base {

	public ImportRegisterLog(final ImportRegister importRegister) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setImportRegister(importRegister);
		setInstant(new DateTime());
	}

}
