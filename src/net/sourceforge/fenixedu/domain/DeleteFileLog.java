package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class DeleteFileLog extends DeleteFileLog_Base {

    public DeleteFileLog(String requestor, String storageID) {
	super();
	this.setRequestorIstUsername(requestor);
	this.setExternalStorageIdentification(storageID);
	this.setRequestTime(new DateTime());
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

}
