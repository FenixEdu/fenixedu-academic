package Dominio;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class UniversityCode extends DomainObject implements IUniversityCode {

	private String universityCode;

	public UniversityCode() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IUniversityCode) {
			IUniversityCode universityCode = (IUniversityCode) obj;

			resultado = this.getUniversityCode().equals(universityCode.getUniversityCode());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "universityCode = " + this.universityCode + "; ";
		return result;
	}

	public String getUniversityCode() {
		return universityCode;
	}

	public void setUniversityCode(String string) {
		universityCode = string;
	}

}
