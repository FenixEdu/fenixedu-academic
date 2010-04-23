package net.sourceforge.fenixedu.domain;

public class ImportRegister extends ImportRegister_Base {
    
    public ImportRegister() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
        new ImportRegisterLog(this);
    }
    
}
