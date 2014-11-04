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

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoLessonInstanceAggregation;
import org.fenixedu.academic.dto.InfoOccupation;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.dto.InfoWrittenEvaluation;
import org.fenixedu.academic.servlet.taglib.sop.v3.ColorPicker;

public class ExecutionCourseTimeTableColorPicker extends ColorPicker {

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
        }
        if (infoShowOccupation instanceof InfoOccupation) {
            return "GenericEvent";
        }
        return "Other";
    }

    private String key(final Lesson lesson) {
        return lesson.getShift().getExternalId();
    }

    private String key(final InfoLesson infoLesson) {
        return key(infoLesson.getLesson());
    }

    private String key(final InfoLessonInstanceAggregation aggregation) {
        return aggregation.getShift().getExternalId();
    }

    private String key(final InfoLessonInstance infoLessonInstance) {
        return key(infoLessonInstance.getLessonInstance().getLesson());
    }

    private String key(final InfoWrittenEvaluation infoWrittenEvaluation) {
        return infoWrittenEvaluation.getWrittenEvaluation().getExternalId();
    }

}
