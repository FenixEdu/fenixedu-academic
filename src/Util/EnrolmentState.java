package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentState extends FenixValuedEnum {

	public static final int APROVED_TYPE = 1;
	public static final int NOT_APROVED_TYPE = 2;
	public static final int ENROLLED_TYPE = 3;
	public static final int TEMPORARILY_ENROLLED_TYPE = 4;
	public static final int ANNULED_TYPE = 5;
	public static final int NOT_EVALUATED_TYPE = 6;

	public static final EnrolmentState APROVED =
		new EnrolmentState("msg.approved", EnrolmentState.APROVED_TYPE);
	public static final EnrolmentState NOT_APROVED =
		new EnrolmentState("msg.notApproved", EnrolmentState.NOT_APROVED_TYPE);
	public static final EnrolmentState ENROLLED =
		new EnrolmentState("msg.enrolled", EnrolmentState.ENROLLED_TYPE);
	public static final EnrolmentState TEMPORARILY_ENROLED =
		new EnrolmentState(
			"msg.temporarilyEnrolled",
			EnrolmentState.TEMPORARILY_ENROLLED_TYPE);
	public static final EnrolmentState ANNULED =
		new EnrolmentState("msg.annuled", EnrolmentState.ANNULED_TYPE);
	public static final EnrolmentState NOT_EVALUATED =
		new EnrolmentState(
			"msg.notEvaluated",
			EnrolmentState.NOT_EVALUATED_TYPE);

	private EnrolmentState(String name, int value) {
		super(name, value);
	}

	public static EnrolmentState getEnum(String state) {
		return (EnrolmentState) getEnum(EnrolmentState.class, state);
	}

	public static EnrolmentState getEnum(int state) {
		return (EnrolmentState) getEnum(EnrolmentState.class, state);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnrolmentState.class);
	}

	public static List getEnumList() {
		return getEnumList(EnrolmentState.class);
	}

	public static Iterator iterator() {
		return iterator(EnrolmentState.class);
	}

	public String toString() {
		return this.getName();
	}

}
