package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

public class PhdCandidacyRefereeBean implements Serializable {

	static private final long serialVersionUID = 1L;

	private String name;
	private String email;
	private String institution;

	public PhdCandidacyRefereeBean() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

}
