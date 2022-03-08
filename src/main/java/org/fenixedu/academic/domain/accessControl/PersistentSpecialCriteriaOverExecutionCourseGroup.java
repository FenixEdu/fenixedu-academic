/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accessControl;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.ExecutionCourse;

import pt.ist.fenixframework.dml.runtime.Relation;

public abstract class PersistentSpecialCriteriaOverExecutionCourseGroup extends
        PersistentSpecialCriteriaOverExecutionCourseGroup_Base {
    protected PersistentSpecialCriteriaOverExecutionCourseGroup() {
        super();
    }

    protected void init(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    public void delete() {
        super.setBennu(null);
        super.setExecutionCourse(null);

        super.deleteDomainObject();
    }

    @Override
    protected Collection<Relation<?, ?>> getContextRelations() {
        return Collections.singleton(getRelationPersistentSpecialCriteriaOverExecutionCourseGroupExecutionCourse());
    }

    protected static <T extends PersistentSpecialCriteriaOverExecutionCourseGroup> T singleton(Class<T> type,
            ExecutionCourse executionCourse, Supplier<T> creator) {
        return singleton(
                () -> (Optional<T>) executionCourse.getSpecialCriteriaOverExecutionCourseGroupSet().stream()
                        .filter(group -> group.getClass() == type).findAny(), creator);
    }
}
