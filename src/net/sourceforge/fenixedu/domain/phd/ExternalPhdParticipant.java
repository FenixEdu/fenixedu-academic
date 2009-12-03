package net.sourceforge.fenixedu.domain.phd;

public class ExternalPhdParticipant extends ExternalPhdParticipant_Base {

    private ExternalPhdParticipant() {
	super();
    }

    ExternalPhdParticipant(PhdIndividualProgramProcess process, final String name, final String title,
	    final String qualification, final String workLocation, final String email) {
	this();
	init(process);
	edit(name, title, qualification, workLocation, email);
    }

    void edit(final String name, final String title, final String qualification, final String workLocation, final String email) {
	check(name, "error.ExternalGuiding.invalid.name");
	// check(qualification, "error.ExternalGuiding.invalid.qualification");
	check(workLocation, "error.ExternalGuiding.invalid.workLocation");
	// check(email, "error.ExternalGuiding.invalid.email");

	setName(name);
	setTitle(title);
	setQualification(qualification);
	setWorkLocation(workLocation);
	setEmail(email);
    }

    void edit(final String category, final String address, final String phone) {
	setCategory(category);
	setAddress(address);
	setPhone(phone);
    }

}
