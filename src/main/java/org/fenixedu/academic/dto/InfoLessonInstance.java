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
package org.fenixedu.academic.dto;

import java.util.Calendar;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public class InfoLessonInstance extends InfoShowOccupation {

    private final LessonInstance lessonInstanceReference;

    public InfoLessonInstance(LessonInstance lessonInstance) {
        this.lessonInstanceReference = lessonInstance;
    }

    public LessonInstance getLessonInstance() {
        return lessonInstanceReference;
    }

    @Override
    public DiaSemana getDiaSemana() {
        return getLessonInstance().getDayOfweek();
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
        return getLessonInstance().getLessonInstanceSpaceOccupation() != null ? InfoRoomOccupation
                .newInfoFromDomain(getLessonInstance().getLessonInstanceSpaceOccupation()) : null;
    }

    @Override
    public String getExternalId() {
        return getLessonInstance().getExternalId();
    }

    @Override
    public Calendar getInicio() {
        return getLessonInstance().getBeginDateTime().toCalendar(I18N.getLocale());
    }

    @Override
    public Calendar getFim() {
        return getLessonInstance().getEndDateTime().toCalendar(I18N.getLocale());
    }

    @Override
    public InfoShift getInfoShift() {
        return getLessonInstance().getLesson().getShift() != null ? InfoShift.newInfoFromDomain(getLesson().getShift()) : null;
    }

    @Override
    public ShiftType getTipo() {
        return null;
    }

    public String getShiftTypeCodesPrettyPrint() {
//        if (getLessonInstance().getCourseLoad() != null) {
//            return getLessonInstance().getCourseLoad().getType().getSiglaTipoAula();
//        } else {
//            return getLessonInstance().getLesson().getShift().getShiftTypesCodePrettyPrint();
//        }
        return getLessonInstance().getLesson().getShift().getShiftTypesCodePrettyPrint();
    }

    public String getShiftTypesPrettyPrint() {
//        if (getLessonInstance().getCourseLoad() != null) {
//            return BundleUtil.getString(Bundle.ENUMERATION, getLessonInstance().getCourseLoad().getType().getName());
//        } else {
//            return getLessonInstance().getLesson().getShift().getShiftTypesPrettyPrint();
//        }
        return getLessonInstance().getLesson().getShift().getShiftTypesPrettyPrint();
    }

    private Lesson getLesson() {
        return getLessonInstance().getLesson();
    }
}
