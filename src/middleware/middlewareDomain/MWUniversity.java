package middleware.middlewareDomain;

public class MWUniversity extends MWDomainObject
{
	private String universityCode;
	private String universityName;

	public String getUniversityCode() {
		return this.universityCode;
	}
	public void setUniversityCode(String universityCode) {
		this.universityCode = universityCode;
	}

	public String toString() {
		return " [universityName] " + universityName + " [universityCode] " + universityCode;

	}

	public String getUniversityName() {
		return this.universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}
}