package net.sourceforge.fenixedu.domain.internship;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DuplicateInternshipCandidacy extends Exception {
	private static final long serialVersionUID = 7766745377038102775L;

	private final String number;

	private final String university;

	public DuplicateInternshipCandidacy(String studentNumber, String university) {
		super();
		this.number = studentNumber;
		this.university = university;
	}

	public String getNumber() {
		return number;
	}

	public String getUniversity() {
		return university;
	}
}
