package net.sourceforge.fenixedu.domain.phd;

public class ExternalGuiding extends ExternalGuiding_Base {

    private ExternalGuiding() {
	super();
    }

    ExternalGuiding(final String name, final String qualification, final String workLocation, final String email) {
	this();
	edit(name, qualification, workLocation, email);
    }

    @Override
    void edit(final String name, final String qualification, final String workLocation, final String email) {
	check(name, "error.ExternalGuiding.invalid.name");
	// check(qualification, "error.ExternalGuiding.invalid.qualification");
	check(workLocation, "error.ExternalGuiding.invalid.workLocation");
	check(email, "error.ExternalGuiding.invalid.email");

	setName(name);
	setQualification(qualification);
	setWorkLocation(workLocation);
	setEmail(email);
    }

    @Override
    void edit(final String category, final String address, final String phone) {
	setCategory(category);
	setAddress(address);
	setPhone(phone);
    }

}
