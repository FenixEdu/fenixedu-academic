package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

abstract public class PhdProgramGuiding extends PhdProgramGuiding_Base {

    protected PhdProgramGuiding() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	removePhdIndividualProgramProcess();
	removePhdIndividualProgramProcess();
	removeRootDomainObject();
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getCategory();

    abstract public String getWorkLocation();

    abstract public String getAddress();

    abstract public String getEmail();

    abstract public String getPhone();

    abstract void edit(final String name, final String qualification, final String workLocation, final String email);

    abstract void edit(final String category, final String address, final String phone);

    static public PhdProgramGuiding create(final Person person) {
	return new InternalGuiding(person);
    }

    static public PhdProgramGuiding create(final String name, final String qualification, final String workLocation,
	    final String email) {
	return new ExternalGuiding(name, qualification, workLocation, email);
    }

}
