package Util.classProperties;

/**
 * @author David Santos in Apr 6, 2004
 */

public class ExecutionCoursePropertyName extends GeneralClassPropertyName {
    public static final String PHASE_NAME_STR = "PHASE";

    public static final ExecutionCoursePropertyName PHASE_NAME = new ExecutionCoursePropertyName(
            ExecutionCoursePropertyName.PHASE_NAME_STR);

    public ExecutionCoursePropertyName(String name) {
        super(name);
    }
}