package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

public class ImportRegister extends ImportRegister_Base {

    public ImportRegister() {
        super();
        setRootDomainObject(Bennu.getInstance());
        new ImportRegisterLog(this);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ImportRegisterLog> getImportRegisterLogs() {
        return getImportRegisterLogsSet();
    }

    @Deprecated
    public boolean hasAnyImportRegisterLogs() {
        return !getImportRegisterLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRemoteExternalOid() {
        return getRemoteExternalOid() != null;
    }

}
