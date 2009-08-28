package net.sourceforge.fenixedu.domain.phd.seminar;

import static net.sourceforge.fenixedu.util.StringUtils.EMPTY;
import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class InternalSeminarCommissionElement extends InternalSeminarCommissionElement_Base {

    private InternalSeminarCommissionElement() {
	super();
    }

    InternalSeminarCommissionElement(final PublicPresentationSeminarProcess process, final SeminarComissionElementBean bean) {
	this();

	super.init(process);

	check(bean.getPerson(), "error.InternalSeminarCommissionElement.invalid.person");
	setPerson(bean.getPerson());
    }

    @Override
    protected void disconnect() {
	removePerson();
	super.disconnect();
    }

    @Override
    public String getName() {
	return getPerson().getName();
    }

    @Override
    public String getQualification() {
	final Qualification qualification = getPerson().getLastQualification();
	return qualification != null ? qualification.getType().getLocalizedName() : EMPTY;
    }
    
    @Override
    public String getCategory() {
	final String category = getPerson().getEmployee().getCurrentEmployeeProfessionalCategoryName();
	return !isEmpty(category) ? category : (hasTeacher() ? getTeacher().getCategory().getName().getContent() : EMPTY);
    }

    private Teacher getTeacher() {
	return getPerson().getTeacher();
    }

    private boolean hasTeacher() {
	return getPerson().hasTeacher();
    }

    @Override
    public String getInstitution() {
	return getPerson().getEmployee().getCurrentWorkingPlace().getName();
    }

    @Override
    public String getAddress() {
	final PhysicalAddress address = getPerson().getDefaultPhysicalAddress();
	return address != null ? writeAddress(address) : EMPTY;
    }

    private String writeAddress(final PhysicalAddress address) {
	final StringBuilder buffer = new StringBuilder();

	buffer.append(isEmpty(address.getAddress()) ? EMPTY : address.getAddress());

	if (!isEmpty(address.getAreaCode())) {
	    buffer.append(", ").append(address.getAreaCode());
	}

	if (!isEmpty(address.getAreaOfAreaCode())) {
	    buffer.append(", ").append(address.getAreaOfAreaCode());
	}

	return buffer.toString();
    }

    @Override
    public String getPhone() {
	final String phone = getPerson().getDefaultMobilePhoneNumber();
	return !isEmpty(phone) ? phone : getPerson().getDefaultPhoneNumber();
    }

    @Override
    public String getEmail() {
	return getPerson().getInstitutionalOrDefaultEmailAddressValue();
    }

}
