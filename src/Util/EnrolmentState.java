package Util;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentState {

	public static final int APROVED = 1;
	public static final int NOT_APROVED = 2;
	public static final int ENROLED = 3;
	public static final int TEMPORARILY_ENROLED = 4;
	public static final int ANNULED = 5;
	public static final int NOT_EVALUATED = 6;

	public static final EnrolmentState APROVED_OBJ = new EnrolmentState(EnrolmentState.APROVED);
	public static final EnrolmentState NOT_APROVED_OBJ = new EnrolmentState(EnrolmentState.NOT_APROVED);
	public static final EnrolmentState ENROLED_OBJ = new EnrolmentState(EnrolmentState.ENROLED);
	public static final EnrolmentState TEMPORARILY_ENROLED_OBJ = new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED);
	public static final EnrolmentState ANNULED_OBJ = new EnrolmentState(EnrolmentState.ANNULED);
	public static final EnrolmentState NOT_EVALUATED_OBJ = new EnrolmentState(EnrolmentState.NOT_EVALUATED);

	private Integer state;

	public EnrolmentState() {
	}

	public EnrolmentState(int state) {
		this.state = new Integer(state);
	}

	public EnrolmentState(Integer state) {
		this.state = state;
	}

	/** Getter for property state.
	 * @return Value of property state.
	 *
	 */
	public java.lang.Integer getState() {
		return state;
	}

	/** Setter for property state.
	 * @param state New value of property state.
	 *
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	public boolean equals(Object o) {
		if (o instanceof EnrolmentState) {
			EnrolmentState aux = (EnrolmentState) o;
			return this.state.equals(aux.getState());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.state.intValue();
		String valueS = null;

		switch (value) {
			case NOT_APROVED :
				valueS = "NOT_APROVED";
				break;
			case APROVED :
				valueS = "APROVED";
				break;
			case ENROLED :
				valueS = "ENROLED";
				break;
			case TEMPORARILY_ENROLED :
				valueS = "TEMPORARILY_ENROLED";
				break;
			case ANNULED :
				valueS = "ANNULED";
			case NOT_EVALUATED :
				valueS = "NOT_EVALUATED";
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]\n";
	}
}
