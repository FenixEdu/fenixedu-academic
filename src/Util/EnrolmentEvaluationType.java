package Util;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentEvaluationType {

	public static final int NORMAL = 1; // This one is used to represent "epoca" (time) 0 and 2.
	public static final int IMPROVEMENT = 2;
	public static final int SPECIAL = 3;
	public static final int EXTERNAL = 4;
	public static final int FIRST_TIME = 5; // This one is used to represent "epoca" (time) 1.

	public static final EnrolmentEvaluationType NORMAL_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	public static final EnrolmentEvaluationType IMPROVEMENT_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
	public static final EnrolmentEvaluationType SPECIAL_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL);
	public static final EnrolmentEvaluationType EXTERNAL_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.EXTERNAL);
	public static final EnrolmentEvaluationType FIRST_TIME_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.FIRST_TIME);

	private Integer type;

	public EnrolmentEvaluationType() {
	}

	public EnrolmentEvaluationType(int state) {
		this.type = new Integer(state);
	}

	public EnrolmentEvaluationType(Integer state) {
		this.type = state;
	}

	/** Getter for property type.
	 * @return Value of property type.
	 *
	 */
	public java.lang.Integer getType() {
		return type;
	}

	/** Setter for property type.
	 * @param type New value of property type.
	 *
	 */
	public void setType(Integer state) {
		this.type = state;
	}

	public boolean equals(Object o) {
		if (o instanceof EnrolmentEvaluationType) {
			EnrolmentEvaluationType aux = (EnrolmentEvaluationType) o;
			return this.type.equals(aux.getType());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case NORMAL :
				valueS = "NORMAL";
				break;
			case IMPROVEMENT :
				valueS = "IMPROVEMENT";
				break;
			case SPECIAL :
				valueS = "SPECIAL";
				break;
			case EXTERNAL :
				valueS = "EXTERNAL";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]\n";
	}
}
