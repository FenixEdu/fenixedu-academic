package net.sourceforge.fenixedu.domain.phd;

public class ExternalPhdParticipant extends ExternalPhdParticipant_Base {

	private ExternalPhdParticipant() {
		super();
	}

	ExternalPhdParticipant(final PhdIndividualProgramProcess process, final PhdParticipantBean bean) {
		this();
		init(process);
		edit(bean.getName(), bean.getTitle(), bean.getQualification(), bean.getWorkLocation(), bean.getInstitution(),
				bean.getEmail());
		edit(bean.getCategory(), bean.getAddress(), bean.getPhone());
	}

	void edit(final String name, final String title, final String qualification, final String workLocation,
			final String institution, final String email) {

		check(name, "error.ExternalPhdParticipant.invalid.name");

		setName(name);
		setTitle(title);
		setQualification(qualification);
		setWorkLocation(workLocation);
		setInstitution(institution);
		setEmail(email);
	}

	void edit(final String category, final String address, final String phone) {
		setCategory(category);
		setAddress(address);
		setPhone(phone);
	}

	@Override
	public void edit(final PhdParticipantBean bean) {
		check(bean.getName(), "error.ExternalPhdParticipant.invalid.name");

		super.edit(bean);

		setName(bean.getName());
		setQualification(bean.getQualification());
		setAddress(bean.getAddress());
		setEmail(bean.getEmail());
		setPhone(bean.getPhone());
	}

	@Override
	public boolean isTeacher() {
		return false;
	}
}
