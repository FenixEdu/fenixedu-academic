package net.sourceforge.fenixedu.domain;

public class ImportRegister extends ImportRegister_Base {

    public ImportRegister() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        new ImportRegisterLog(this);
    }

}
