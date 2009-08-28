package net.sourceforge.fenixedu.domain.phd.seminar;

public class ExternalSeminarCommissionElement extends ExternalSeminarCommissionElement_Base {

    private ExternalSeminarCommissionElement() {
	super();
    }

    ExternalSeminarCommissionElement(final PublicPresentationSeminarProcess process, final SeminarComissionElementBean bean) {
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
