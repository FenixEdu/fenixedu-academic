package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ImportRegisterLog extends ImportRegisterLog_Base {

    public ImportRegisterLog(final ImportRegister importRegister) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setImportRegister(importRegister);
        setInstant(new DateTime());
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInstant() {
        return getInstant() != null;
    }

    @Deprecated
    public boolean hasImportRegister() {
        return getImportRegister() != null;
    }

}
