package Util;

import java.io.Serializable;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlanState implements Serializable{

	public static final int ACTIVE = 1;
	public static final int NOT_ACTIVE = 2;
	public static final int CONCLUDED = 3;

	public static final DegreeCurricularPlanState ACTIVE_OBJ = new DegreeCurricularPlanState(DegreeCurricularPlanState.ACTIVE); 
	public static final DegreeCurricularPlanState NOT_ACTIVE_OBJ = new DegreeCurricularPlanState(DegreeCurricularPlanState.NOT_ACTIVE);
	public static final DegreeCurricularPlanState CONCLUDED_OBJ = new DegreeCurricularPlanState(DegreeCurricularPlanState.CONCLUDED);
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
				break;
			case NOT_ACTIVE :
				valueS = "NOT_ACTIVE";
				break;
			case CONCLUDED :
				valueS = "CONCLUDED";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}

}