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
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}
}
