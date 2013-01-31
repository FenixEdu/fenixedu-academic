package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public abstract class PersonIdentificationDocumentExtraInfo extends PersonIdentificationDocumentExtraInfo_Base {

	public PersonIdentificationDocumentExtraInfo() {
		super();
		setRegisteredInSystemTimestamp(new DateTime());
	}

}
