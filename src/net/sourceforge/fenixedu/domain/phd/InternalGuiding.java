package net.sourceforge.fenixedu.domain.phd;

import static net.sourceforge.fenixedu.util.StringUtils.EMPTY;
import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class InternalGuiding extends InternalGuiding_Base {

    private InternalGuiding() {
	super();
    }

    InternalGuiding(final Person person, final String title) {
	this();
	check(person, "error.InternalGuiding.person.cannot.be.null");
	setPerson(person);
	setTitle(title);
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
    public String getWorkLocation() {
	if (getPerson().hasEmployee()) {
	    final Unit workingPlace = getPerson().getEmployee().getCurrentWorkingPlace();
	    if (workingPlace != null) {
		workingPlace.getName();
	    }
	}
	return null;
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
    public String getEmail() {
	return getPerson().getInstitutionalOrDefaultEmailAddressValue();
    }

    @Override
    public String getPhone() {
	final String phone = getPerson().getDefaultPhoneNumber();
	return !isEmpty(phone) ? phone : getPerson().getDefaultMobilePhoneNumber();
    }

    @Override
    protected void disconnect() {
	removePerson();
	super.disconnect();
    }

    @Override
    public boolean isFor(Person person) {
	return getPerson() == person;
    }

    @Override
    public boolean isInternal() {
	return true;
    }

}
