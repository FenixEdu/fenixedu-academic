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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public abstract class PersistentTeachersWithIncompleteEvaluationWorkGroup extends
        PersistentTeachersWithIncompleteEvaluationWorkGroup_Base {
    protected PersistentTeachersWithIncompleteEvaluationWorkGroup() {
        super();
    }

    protected void init(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        setPeriod(period);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    @Override
    protected void gc() {
        setPeriod(null);
        setDegreeCurricularPlan(null);
        super.gc();
    }

    protected static <T extends PersistentTeachersWithIncompleteEvaluationWorkGroup> T singleton(Class<T> type,
            ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan, Supplier<T> creator) {
        return singleton(
                () -> ((Optional<T>) period.getTeachersWithIncompleteEvaluationWorkGroupSet().stream()
                        .filter(group -> Objects.equals(group.getDegreeCurricularPlan(), degreeCurricularPlan)).findAny()),
                creator);
    }
}
