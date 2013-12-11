package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class DeleteFileLog extends DeleteFileLog_Base {

    public DeleteFileLog(String requestor, String storageID) {
        super();
        this.setRequestorIstUsername(requestor);
        this.setExternalStorageIdentification(storageID);
        this.setRequestTime(new DateTime());
        this.setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRequestorIstUsername() {
        return getRequestorIstUsername() != null;
    }

    @Deprecated
    public boolean hasExternalStorageIdentification() {
        return getExternalStorageIdentification() != null;
    }

    @Deprecated
    public boolean hasRequestTime() {
        return getRequestTime() != null;
    }

}
