package Dominio;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class CurricularCourseEnrolmentInfo extends DomainObject implements ICurricularCourseEnrolmentInfo{

	private Integer maxIncrementNac;
	private Integer minIncrementNac;
	private Integer weigth;

	public CurricularCourseEnrolmentInfo() {
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICurricularCourseEnrolmentInfo) {
			ICurricularCourseEnrolmentInfo curricularCourseEnrolmentInfo = (ICurricularCourseEnrolmentInfo) obj;
			result = 	this.getMaxIncrementNac().equals(curricularCourseEnrolmentInfo.getMaxIncrementNac()) &&
						this.getMinIncrementNac().equals(curricularCourseEnrolmentInfo.getMinIncrementNac());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "maxIncrementNac = " + this.maxIncrementNac + "; ";
		result += "minIncrementNac = " + this.minIncrementNac + "]\n";
		return result;
	}
	/**
	 * @return
	 */
	public Integer getMaxIncrementNac() {
		return maxIncrementNac;
	}

	/**
	 * @return
	 */
	public Integer getMinIncrementNac() {
		return minIncrementNac;
	}

	/**
	 * @param integer
	 */
	public void setMaxIncrementNac(Integer integer) {
		maxIncrementNac = integer;
	}

	/**
	 * @param integer
	 */
	public void setMinIncrementNac(Integer integer) {
		minIncrementNac = integer;
	}
	/**
	 * @return
	 */
	public Integer getWeigth() {
		return weigth;
	}

	/**
	 * @param integer
	 */
	public void setWeigth(Integer integer) {
		weigth = integer;
	}

}