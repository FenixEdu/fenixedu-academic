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
	public static final PeriodState CURRENT = new PeriodState(PeriodState.CURRENT_CODE);	
	
	
	public static final String CLOSED_CODE = "CL";
	public static final String CURRENT_CODE = "C";
	public static final String OPEN_CODE = "O";
	public static final String NOT_OPEN_CODE = "NO";
	
	private PeriodState(final String stateCode){
		this.stateCode = stateCode;
	}
	public String getStateCode(){
		return stateCode;
	}

	public PeriodState(PeriodState executionPeriodState){
		this.stateCode = executionPeriodState.getStateCode();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof PeriodState) {
			PeriodState executionPeriodState = (PeriodState) obj;
			return executionPeriodState.getStateCode() == stateCode;
		}
		return false;
	}
}
