/*
 * Created on 5/Abr/2003 by jpvl
 *
 */
package Util;

/**
 * @author jpvl
 */
public class PeriodState {
	private String stateCode;
	public static final PeriodState CLOSED = new PeriodState(PeriodState.CLOSED_CODE);
	public static final PeriodState OPEN = new PeriodState(PeriodState.OPEN_CODE);
	public static final PeriodState NOT_OPEN = new PeriodState(PeriodState.NOT_OPEN_CODE);
	public static final PeriodState ACTUAL = new PeriodState(PeriodState.ACTUAL_CODE);	
	
	
	public static final String CLOSED_CODE = "C";
	public static final String ACTUAL_CODE = "A";
	public static final String  OPEN_CODE = "O";
	public static final String NOT_OPEN_CODE = "NO";
	
	private PeriodState(final String stateCode){
		this.stateCode = stateCode;
	}
	public String getCode(){
		return stateCode;
	}

	public PeriodState(PeriodState executionPeriodState){
		this.stateCode = executionPeriodState.getCode();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof PeriodState) {
			PeriodState executionPeriodState = (PeriodState) obj;
			return executionPeriodState.getCode() == stateCode;
		}
		return false;
	}
}
