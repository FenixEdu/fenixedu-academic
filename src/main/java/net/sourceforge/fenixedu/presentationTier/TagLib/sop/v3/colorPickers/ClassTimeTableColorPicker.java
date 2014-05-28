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
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluation;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

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
