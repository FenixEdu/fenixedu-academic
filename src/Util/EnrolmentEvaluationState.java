package Util;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentEvaluationState {

	public static final int NORMAL = 1;
	public static final int RECTIFIED = 2;
	public static final int RECTIFICATION = 3;

	public static final EnrolmentEvaluationState NORMAL_OJB = new EnrolmentEvaluationState(EnrolmentEvaluationState.NORMAL);
	public static final EnrolmentEvaluationState RECTIFIED_OJB = new EnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED);
	public static final EnrolmentEvaluationState RECTIFICATION_OJB = new EnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFICATION);

	private Integer state;

	public EnrolmentEvaluationState() {
	}

	public EnrolmentEvaluationState(int state) {
		this.state = new Integer(state);
	}

	public EnrolmentEvaluationState(Integer state) {
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
		if (o instanceof EnrolmentEvaluationState) {
			EnrolmentEvaluationState aux = (EnrolmentEvaluationState) o;
			return this.state.equals(aux.getState());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.state.intValue();
		String valueS = null;

		switch (value) {
			case NORMAL :
				valueS = "NORMAL";
				break;
			case RECTIFIED :
				valueS = "RECTIFIED";
				break;
			case RECTIFICATION :
				valueS = "RECTIFICATION";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]\n";
	}
}
