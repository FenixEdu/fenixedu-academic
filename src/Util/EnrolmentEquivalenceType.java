package Util;

import java.io.Serializable;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class EnrolmentEquivalenceType implements Serializable {

	public static final int EQUIVALENT_COURSE = 1;
	
	public static final EnrolmentEquivalenceType EQUIVALENT_COURSE_OBJ = new EnrolmentEquivalenceType(EnrolmentEquivalenceType.EQUIVALENT_COURSE);

	private Integer type;

	public EnrolmentEquivalenceType() {
	}

	public EnrolmentEquivalenceType(int type) {
		this.type = new Integer(type);
	}

	public EnrolmentEquivalenceType(Integer type) {
		this.type = type;
	}

	public Integer getEquivalenceType() {
		return this.type;
	}

	public void setEquivalenceType(Integer type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof EnrolmentEquivalenceType) {
			EnrolmentEquivalenceType ds = (EnrolmentEquivalenceType) obj;
			resultado = this.getEquivalenceType().equals(ds.getEquivalenceType());
		}
		return resultado;
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case EQUIVALENT_COURSE :
				valueS = "EQUIVALENT_COURSE";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}

}