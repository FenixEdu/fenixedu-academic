package net.sourceforge.fenixedu.domain.phd;

public class ExternalPhdParticipant extends ExternalPhdParticipant_Base {

    private ExternalPhdParticipant() {
	super();
    }

    ExternalPhdParticipant(PhdIndividualProgramProcess process, PhdParticipantBean bean) {
	this();
	init(process);
	edit(bean.getName(), bean.getTitle(), bean.getQualification(), bean.getWorkLocation(), bean.getInstitution(), bean
		.getEmail());
	edit(bean.getCategory(), bean.getAddress(), bean.getPhone());
    }

    void edit(final String name, final String title, final String qualification, final String workLocation,
	    final String institution, final String email) {

	check(name, "error.ExternalPhdParticipant.invalid.name");
	// check(qualification,
	// "error.ExternalPhdParticipant.invalid.qualification");
	// check(workLocation,
	// "error.ExternalPhdParticipant.invalid.workLocation");
	// check(institution,
	// "error.ExternalPhdParticipant.invalid.institution");
	// check(email, "error.ExternalPhdParticipant.invalid.email");

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
