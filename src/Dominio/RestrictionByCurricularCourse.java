package Dominio;


/**
 * @author David Santos
 */

public abstract class RestrictionByCurricularCourse extends Restriction implements IRestrictionByCurricularCourse {

	protected Integer keyPrecedentCurricularCourse;
	protected ICurricularCourse precedentCurricularCourse;

	public ICurricularCourse getPrecedentCurricularCourse() {
		return precedentCurricularCourse;
	}

	public void setPrecedentCurricularCourse(ICurricularCourse curricularCourse) {
		precedentCurricularCourse = curricularCourse;
	}

	public Integer getKeyPrecedentCurricularCourse() {
		return keyPrecedentCurricularCourse;
	}

	public void setKeyPrecedentCurricularCourse(Integer keyPrecedentCurricularCourse) {
		this.keyPrecedentCurricularCourse = keyPrecedentCurricularCourse;
	}

	public boolean equals(Object obj) {
		boolean result = super.equals(obj);
		if ((result) && (obj instanceof IRestrictionByCurricularCourse)) {
			IRestrictionByCurricularCourse restrictionByCurricularCourse = (IRestrictionByCurricularCourse) obj;
			result =	this.getPrecedentCurricularCourse().equals(restrictionByCurricularCourse.getPrecedentCurricularCourse()) &&
						this.getClass().getName().equals(restrictionByCurricularCourse.getClass().getName());
		}
		return result;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
		stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
		return stringBuffer.toString();
	}
}