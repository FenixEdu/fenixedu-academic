package Util;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class CurricularCourseType {

	public static final int NORMAL_COURSE = 1;
	public static final int OPTIONAL_COURSE = 2;
	public static final int PROJECT_COURSE = 3;
	public static final int TFC_COURSE = 4;

	private Integer type;

	public CurricularCourseType() {
	}

	public CurricularCourseType(int type) {
		this.type = new Integer(type);
	}

	public CurricularCourseType(Integer type) {
		this.type = type;
	}

	public Integer getCurricularCourseType() {
		return this.type;
	}

	public void setCurricularCourseType(Integer type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof CurricularCourseType) {
			CurricularCourseType ds = (CurricularCourseType) obj;
			resultado = this.getCurricularCourseType().equals(ds.getCurricularCourseType());
		}
		return resultado;
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case TFC_COURSE:
				valueS = "TFC_COURSE";
				break;
			case PROJECT_COURSE:
				valueS = "PROJECT_COURSE";
				break;
			case NORMAL_COURSE:
				valueS = "NORMAL_COURSE";
				break;
			case OPTIONAL_COURSE:
				valueS = "OPTIONAL_COURSE";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}

}