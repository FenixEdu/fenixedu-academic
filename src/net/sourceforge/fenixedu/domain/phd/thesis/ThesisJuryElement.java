package net.sourceforge.fenixedu.domain.phd.thesis;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

abstract public class ThesisJuryElement extends ThesisJuryElement_Base {

    protected ThesisJuryElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(new DateTime());
    }

    protected void init(final PhdThesisProcess process) {
	check(process, "error.ThesisJuryElement.invalid.process");
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

    static public ThesisJuryElement create(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	if (bean.isInternal()) {
	    return new InternalThesisJuryElement(process, bean);
	} else {
	    return new ExternalThesisJuryElement(process, bean);
	}
    }

}
