package net.sourceforge.fenixedu.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class ExecutionCourseLog extends ExecutionCourseLog_Base {

    public enum ExecutionCourseLogTypes {
        PROFESSORSHIP, CURRICULAR, CONTENT, GROUPS_AND_SHIFTS, EVALUATION;
        public String getQualifiedName() {
            return StringAppender.append(ExecutionCourseLogTypes.class.getSimpleName(), ".", name());
        }

        private static final Collection<ExecutionCourseLogTypes> typesAsList = Collections.unmodifiableList(Arrays
                .asList(values()));

        public static Collection<ExecutionCourseLogTypes> valuesAsList() {
            return typesAsList;
        }
    }

    public ExecutionCourseLog() {
        super();
    }

    public ExecutionCourseLog(ExecutionCourse executionCourse, String description) {
        super();
        if (getExecutionCourse() == null) {
            setExecutionCourse(executionCourse);
        }
        setDescription(description);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getStringFromResourceBundle(bundle, key, args);
    }

    public ExecutionCourseLogTypes getExecutionCourseLogType() {
        return null;
    }

}
