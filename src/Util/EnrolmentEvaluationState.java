package Util;

import java.io.Serializable;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentEvaluationState implements Serializable {

	public static final int FINAL = 1;
	public static final int TEMPORARY = 2;
//	public static final int RECTIFIED = 2;
//	public static final int RECTIFICATION = 3;
	public static final int ANNULED = 4;

	public static final EnrolmentEvaluationState FINAL_OBJ = new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL);
	public static final EnrolmentEvaluationState TEMPORARY_OBJ = new EnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY);
//	public static final EnrolmentEvaluationState RECTIFIED_OBJ = new EnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED);
//	public static final EnrolmentEvaluationState RECTIFICATION_OBJ = new EnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFICATION);
	public static final EnrolmentEvaluationState ANNULED_OBJ = new EnrolmentEvaluationState(EnrolmentEvaluationState.ANNULED);


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
			case TEMPORARY :
				valueS = "TEMPORARY";
				break;
			case FINAL :
				valueS = "FINAL";
				break;
//			case RECTIFIED :
//				valueS = "RECTIFIED";
//				break;
//			case RECTIFICATION :
//				valueS = "RECTIFICATION";
//				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]\n";
	}
}
