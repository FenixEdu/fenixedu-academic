package Dominio;

/**
 * @author Tânia & Alexandra
 *
 */
public class CreditsTeacher extends DomainObject implements ICreditsTeacher {
	private ITeacher teacher = null;
	private Integer keyTeacher = null;

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

			resultado = (getTeacher().equals(creditsTeacher.getTeacher())) 
			&& (getShift().equals(creditsTeacher.getShift()))
			&& (getPercentage().equals(creditsTeacher.getPercentage()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[CREDITS_TEACHER";
		result += ", codInt=" + getIdInternal();
		result += ", teacher=" + getTeacher();
		result += ", shift=" + getShift();
		result += "]";
		return result;
	}

	public ITeacher getTeacher() {
		return this.teacher;
	}
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}

	public Integer getKeyTeacher() {
		return keyTeacher;
	}
	public void setKeyTeacher(Integer integer) {
		keyTeacher = integer;
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
