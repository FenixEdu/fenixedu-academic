package DataBeans;

import java.io.Serializable;

/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public class InfoCurricularCourseEnrolmentInfo implements Serializable {

	private Integer maxIncrementNac;
	private Integer minIncrementNac;
	private Integer weigth;

	public InfoCurricularCourseEnrolmentInfo() {
	}


	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCurricularCourseEnrolmentInfo) {
			InfoCurricularCourseEnrolmentInfo infoCurricularCourseEnrolmentInfo =
				(InfoCurricularCourseEnrolmentInfo) obj;
			result = 	this.getMaxIncrementNac().equals(infoCurricularCourseEnrolmentInfo.getMaxIncrementNac()) &&
						this.getMinIncrementNac().equals(infoCurricularCourseEnrolmentInfo.getMinIncrementNac());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "maxIncrementNac = " + this.maxIncrementNac + "; ";
		result += "minIncrementNac = "+ this.minIncrementNac + "]\n";
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
