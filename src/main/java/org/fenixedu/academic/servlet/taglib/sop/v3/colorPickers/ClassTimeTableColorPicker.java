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
package org.fenixedu.academic.servlet.taglib.sop.v3.colorPickers;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoLessonInstanceAggregation;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.dto.InfoWrittenEvaluation;
import org.fenixedu.academic.servlet.taglib.sop.v3.ColorPicker;

public class ClassTimeTableColorPicker extends ColorPicker {

    @Override
    protected String getColorKeyFromInfoLesson(final InfoShowOccupation infoShowOccupation) {
        if (infoShowOccupation instanceof InfoLesson) {
            return key((InfoLesson) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoLessonInstance) {
            return key((InfoLessonInstance) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoLessonInstanceAggregation) {
            return key((InfoLessonInstanceAggregation) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoWrittenEvaluation) {
            return key((InfoWrittenEvaluation) infoShowOccupation);
        } else {
            return "GenericEvent";
        }
    }

    private String key(final Lesson lesson) {
        return lesson.getExecutionCourse().getExternalId();
    }

    private String key(final InfoLesson infoLesson) {
        return key(infoLesson.getLesson());
    }

    private String key(final InfoLessonInstance infoLessonInstance) {
        return key(infoLessonInstance.getLessonInstance().getLesson());
    }

    private String key(final InfoLessonInstanceAggregation infoLessonInstanceAggregation) {
        return infoLessonInstanceAggregation.getShift().getExecutionCourse().getExternalId();
    }

    private String key(final InfoWrittenEvaluation infoWrittenEvaluation) {
        final StringBuilder stringBuilder = new StringBuilder();
        final WrittenEvaluation writtenEvaluation = infoWrittenEvaluation.getWrittenEvaluation();
        for (final ExecutionCourse executionCourse : sort(writtenEvaluation.getAssociatedExecutionCoursesSet())) {
            stringBuilder.append(executionCourse.getExternalId());
        }
        return stringBuilder.toString();
    }

    private SortedSet<ExecutionCourse> sort(final Set<ExecutionCourse> associatedExecutionCoursesSet) {
        final SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(DomainObjectUtil.COMPARATOR_BY_ID);
        result.addAll(associatedExecutionCoursesSet);
        return result;
    }

}
