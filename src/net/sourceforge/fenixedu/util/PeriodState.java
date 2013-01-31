/*
 * Created on 5/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author jpvl
 */
public class PeriodState extends FenixUtil {

	public static final PeriodState CLOSED = new PeriodState(PeriodState.CLOSED_CODE);
	public static final PeriodState OPEN = new PeriodState(PeriodState.OPEN_CODE);
	public static final PeriodState NOT_OPEN = new PeriodState(PeriodState.NOT_OPEN_CODE);
	public static final PeriodState CURRENT = new PeriodState(PeriodState.CURRENT_CODE);

	public static final String CLOSED_CODE = "CL";
	public static final String CURRENT_CODE = "C";
	public static final String OPEN_CODE = "O";
	public static final String NOT_OPEN_CODE = "NO";

	private final String stateCode;

	public PeriodState(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public PeriodState(PeriodState executionPeriodState) {
		this.stateCode = executionPeriodState.getStateCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeriodState) {
			PeriodState executionPeriodState = (PeriodState) obj;
			return executionPeriodState.getStateCode().equals(stateCode);
		}
		return false;
	}

	@Override
	public String toString() {
		String result = "";

		if (getStateCode().equals(CLOSED_CODE)) {
			result = "CLOSED";
		} else if (getStateCode().equals(CURRENT_CODE)) {
			result = "CURRENT";
		} else if (getStateCode().equals(OPEN_CODE)) {
			result = "OPEN";
		} else if (getStateCode().equals(NOT_OPEN_CODE)) {
			result = "NOT_OPEN";
		}

		return result;
	}

	public static PeriodState valueOf(String code) {
		if (code == null) {
			return null;
		} else if (code.equals(PeriodState.CURRENT_CODE)) {
			return PeriodState.CURRENT;
		} else if (code.equals(PeriodState.OPEN_CODE)) {
			return PeriodState.OPEN;
		} else if (code.equals(PeriodState.NOT_OPEN_CODE)) {
			return PeriodState.NOT_OPEN;
		} else if (code.equals(PeriodState.CLOSED_CODE)) {
			return PeriodState.CLOSED;
		}

		return null;
	}
}
