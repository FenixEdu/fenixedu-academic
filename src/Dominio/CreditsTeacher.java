package Dominio;

/**
 * @author Tânia & Alexandra
 *
 */
public class CreditsTeacher extends DomainObject implements ICreditsTeacher {
	private IProfessorship professorShip = null;
	private Integer keyProfessorShip = null;

	private ITurno shift = null;
	private Integer keyShift = null;

	private Double percentage = null;

	/* construtor */
	public CreditsTeacher() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ICreditsTeacher) {
			ICreditsTeacher creditsTeacher = (ICreditsTeacher) obj;

			resultado = (getProfessorShip().equals(creditsTeacher.getProfessorShip())) 
			&& (getShift().equals(creditsTeacher.getShift()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[CREDITS_TEACHER";
		result += ", codInt=" + getIdInternal();
		result += ", teacher=" + getProfessorShip();
		result += ", shift=" + getShift();
		result += "]";
		return result;
	}

	public Integer getKeyProfessorShip() {
		return keyProfessorShip;
	}
	public void setKeyProfessorShip(Integer integer) {
		keyProfessorShip = integer;
	}

	public IProfessorship getProfessorShip() {
		return professorShip;
	}
	public void setProfessorShip(IProfessorship professorship) {
		professorShip = professorship;
	}

	public ITurno getShift() {
		return this.shift;
	}
	public void setShift(ITurno shift) {
		this.shift = shift;
	}

	public Integer getKeyShift() {
		return keyShift;
	}
	public void setKeyShift(Integer integer) {
		keyShift = integer;
	}

	public Double getPercentage() {
		return this.percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
}
