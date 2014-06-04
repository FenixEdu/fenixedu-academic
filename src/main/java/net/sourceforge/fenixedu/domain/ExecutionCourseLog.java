/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class ExecutionCourseLog extends ExecutionCourseLog_Base {

    public enum ExecutionCourseLogTypes {
        PROFESSORSHIP, CURRICULAR, CONTENT, GROUPS_AND_SHIFTS, EVALUATION;
        public String getQualifiedName() {
            return ExecutionCourseLogTypes.class.getSimpleName() + "." + name();
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
        return BundleUtil.getString(bundle, key, args);
    }

    public ExecutionCourseLogTypes getExecutionCourseLogType() {
        return null;
    }

    @Override
    public void delete() {
        setExecutionCourse(null);
        super.delete();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

}
