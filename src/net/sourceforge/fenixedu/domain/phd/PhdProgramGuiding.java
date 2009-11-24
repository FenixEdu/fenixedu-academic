package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.lang.StringUtils;

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
	removeAssistantPhdIndividualProgramProcess();
	removeRootDomainObject();
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getCategory();

    abstract public String getWorkLocation();

    abstract public String getAddress();

    abstract public String getEmail();

    abstract public String getPhone();

    public String getNameWithTitle() {
	return StringUtils.isEmpty(getTitle()) ? getName() : getTitle() + " " + getName();
    }

    static public PhdProgramGuiding create(final Person person, final String title) {
	return new InternalGuiding(person, title);
    }

    static public PhdProgramGuiding create(final String name, final String title, final String qualification,
	    final String workLocation, final String email) {
	return new ExternalGuiding(name, title, qualification, workLocation, email);
    }

    static public PhdProgramGuiding create(final PhdProgramGuidingBean bean) {
	if (bean.isInternal()) {
	    return new InternalGuiding(bean.getPerson(), bean.getTitle());
	} else {

	    final ExternalGuiding guiding = new ExternalGuiding(bean.getName(), bean.getTitle(), bean.getQualification(), bean
		    .getWorkLocation(), bean.getEmail());

	    guiding.edit(bean.getCategory(), bean.getAddress(), bean.getPhone());

	    return guiding;
	}
    }

    public boolean isFor(Person person) {
	return false;
    }

    public boolean isInternal() {
	return false;
    }

}
