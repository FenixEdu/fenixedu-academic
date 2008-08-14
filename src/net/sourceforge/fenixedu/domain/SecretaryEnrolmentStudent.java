package net.sourceforge.fenixedu.domain;

public class SecretaryEnrolmentStudent extends SecretaryEnrolmentStudent_Base {

    public SecretaryEnrolmentStudent() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeStudent();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
