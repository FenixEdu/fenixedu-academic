package Util;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlanState {

	public static final int ACTIVE = 1;
	public static final int NOT_ACTIVE = 2;
	public static final int CONCLUDED = 3;

	private Integer state;

	public DegreeCurricularPlanState() {
	}

	public DegreeCurricularPlanState(int state) {
		this.state = new Integer(state);
	}

	public DegreeCurricularPlanState(Integer state) {
		this.state = state;
	}

	public Integer getDegreeState() {
		return this.state;
	}

	public void setDegreeState(Integer state) {
		this.state = state;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof DegreeCurricularPlanState) {
			DegreeCurricularPlanState ds = (DegreeCurricularPlanState) obj;
			resultado = this.getDegreeState().equals(ds.getDegreeState());
		}
		return resultado;
	}

	public String toString() {

		int value = this.state.intValue();
		String valueS = null;

		switch (value) {
			case ACTIVE :
				valueS = "ACTIVE";
			case NOT_ACTIVE :
				valueS = "NOT_ACTIVE";
			case CONCLUDED :
				valueS = "CONCLUDED";
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}

}