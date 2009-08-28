package net.sourceforge.fenixedu.domain.phd.seminar;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

abstract public class SeminarCommissionElement extends SeminarCommissionElement_Base {

    protected SeminarCommissionElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(new DateTime());
    }

    protected void init(final PublicPresentationSeminarProcess process) {
	check(process, "error.SeminarCommissionElement.invalid.process");
	setProcess(process);
    }

    public void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	removeProcess();
	removeRootDomainObject();
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getCategory();

    abstract public String getInstitution(); // Unit or String?

    abstract public String getAddress();

    abstract public String getPhone();

    abstract public String getEmail();

    static public SeminarCommissionElement create(final PublicPresentationSeminarProcess process,
	    final SeminarComissionElementBean bean) {
	if (bean.isInternal()) {
	    return new InternalSeminarCommissionElement(process, bean);
	} else {
	    return new ExternalSeminarCommissionElement(process, bean);
	}
    }
}
