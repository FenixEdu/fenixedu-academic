package Util;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */
public class HasAlternativeSemester {

	public static final int FALSE = 0;
	public static final int TRUE = 1;
	
	private Integer state;

	/** Creates a new instance of HasAlternativeSemester */
	public HasAlternativeSemester() {
	}

	public HasAlternativeSemester(int state) {
		this.state = new Integer(state);
	}

	public HasAlternativeSemester(Integer state) {
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
		if (o instanceof HasAlternativeSemester) {
			HasAlternativeSemester aux = (HasAlternativeSemester) o;
			return this.state.equals(aux.getState());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.state.intValue();
		String valueS = null;

		switch (value) {
			case FALSE :
				valueS = "FALSE";
				break;
			case TRUE :
				valueS = "TRUE";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}
}
