package Util.classProperties;


/**
 * @author David Santos in Apr 6, 2004
 */

public class ExecutionCoursePropertyValue extends GeneralClassPropertyValue
{
	public static final String SECOND_PHASE_VALUE_STR = "SECOND";

	public static final ExecutionCoursePropertyValue SECOND_PHASE_VALUE = new ExecutionCoursePropertyValue(
		ExecutionCoursePropertyValue.SECOND_PHASE_VALUE_STR);

	public ExecutionCoursePropertyValue(String value)
	{
		super(value);
	}
}