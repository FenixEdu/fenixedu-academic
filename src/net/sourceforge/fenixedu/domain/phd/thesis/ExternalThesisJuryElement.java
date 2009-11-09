package net.sourceforge.fenixedu.domain.phd.thesis;


public class ExternalThesisJuryElement extends ExternalThesisJuryElement_Base {

    private ExternalThesisJuryElement() {
	super();
    }

    ExternalThesisJuryElement(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	this();

	super.init(process);

	setName(bean.getName());
	setQualification(bean.getQualification());
	setCategory(bean.getCategory());
	setInstitution(bean.getInstitution());
	setAddress(bean.getAddress());
	setPhone(bean.getPhone());
	setEmail(bean.getEmail());

    }
}
