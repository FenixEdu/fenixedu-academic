package net.sourceforge.fenixedu.domain;

public class TutorshipLog extends TutorshipLog_Base {

    public TutorshipLog() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeTutorship();
        removeRootDomainObject();
        super.deleteDomainObject();
    }
}
