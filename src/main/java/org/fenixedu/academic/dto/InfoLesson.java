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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.DiaSemana;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

public class InfoLesson extends InfoShowOccupation implements Comparable<InfoLesson> {

    private final static Comparator<InfoLesson> INFO_LESSON_COMPARATOR_CHAIN = new Comparator<InfoLesson>() {

        @Override
        public int compare(InfoLesson o1, InfoLesson o2) {
            final int c1 = o1.getDiaSemana().getDiaSemana().compareTo(o2.getDiaSemana().getDiaSemana());
            if (c1 != 0) {
                return c1;
            }
            final int c2 = o1.getInicio().compareTo(o2.getInicio());
            if (c2 != 0) {
                return c2;
            }
            final int c3 = o1.getFim().compareTo(o2.getFim());
            return c3 != 0 ? o1.getInfoSala().getNome().compareTo(o2.getInfoShift().getNome()) : c3;
        }

    };

    private InfoRoom infoSala;
    private InfoShift infoShift;
    private InfoRoomOccupation infoRoomOccupation;

    public InfoLesson(Lesson lesson) {
        super.copyFromDomain(lesson);
    }

    @Override
    public ShiftType getTipo() {
        return null;
    }

    @Override
    public DiaSemana getDiaSemana() {
        return getLesson().getDiaSemana();
    }

    @Override
    public Calendar getFim() {
        return getLesson().getFim();
    }

    @Override
    public Calendar getInicio() {
        return getLesson().getInicio();
    }

    public FrequencyType getFrequency() {
        return getLesson().getFrequency();
    }

    public YearMonthDay getLessonBegin() {
        return getLesson() != null && getLesson().getPeriod() != null ? getLesson().getPeriod().getStartYearMonthDay() : null;
    }

    public YearMonthDay getLessonEnd() {
        return getLesson() != null && getLesson().getPeriod() != null ? getLesson().getPeriod()
                .getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay() : null;
    }

    public String getWeekDay() {
        final String result = getDiaSemana().getDiaSemana().toString();
        if (result != null && result.equals("7")) {
            return "S";
        }
        if (result != null && result.equals("1")) {
            return "D";
        }
        return result;
    }

    public String getLessonDuration() {
        return getLesson().getUnitHours().toString();
    }

    public InfoRoom getInfoSala() {
        return (infoSala == null) ? infoSala = InfoRoom.newInfoFromDomain(getLesson().getSala()) : infoSala;
    }

    @Override
    public InfoShift getInfoShift() {
        return (infoShift == null) ? infoShift = InfoShift.newInfoFromDomain(getLesson().getShift()) : infoShift;
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
        if (infoRoomOccupation == null) {
            infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(getLesson().getLessonSpaceOccupation());
        }
        return infoRoomOccupation;
    }

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
        return (lesson != null) ? new InfoLesson(lesson) : null;
    }

    /*
     * This is a convenience method, until RenderTimeTableTag support proper domain classes...
     */
    @Deprecated
    public static List<InfoLesson> newInfosForSchoolClass(SchoolClass schoolClass) {
        List<InfoLesson> lessons = new ArrayList<>();
        for (Shift shift : schoolClass.getAssociatedShiftsSet()) {
            for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                lessons.add(new InfoLesson(lesson));
            }
        }
        return lessons;
    }

    @Override
    public int compareTo(InfoLesson arg0) {
        return INFO_LESSON_COMPARATOR_CHAIN.compare(this, arg0);
    }

    public String getNextPossibleLessonInstanceDate() {
        YearMonthDay day = getLesson().getNextPossibleLessonInstanceDate();
        return day != null ? day.toString("dd/MM/yyyy") : "-";
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoLesson) {
            InfoLesson infoAula = (InfoLesson) obj;
            resultado =
                    (getDiaSemana().equals(infoAula.getDiaSemana()))
                            && (getInicio().get(Calendar.HOUR_OF_DAY) == infoAula.getInicio().get(Calendar.HOUR_OF_DAY))
                            && (getInicio().get(Calendar.MINUTE) == infoAula.getInicio().get(Calendar.MINUTE))
                            && (getFim().get(Calendar.HOUR_OF_DAY) == infoAula.getFim().get(Calendar.HOUR_OF_DAY))
                            && (getFim().get(Calendar.MINUTE) == infoAula.getFim().get(Calendar.MINUTE))
                            && ((getInfoSala() == null && infoAula.getInfoSala() == null) || (getInfoSala() != null && getInfoSala()
                                    .equals(infoAula.getInfoSala())))
                            && ((getInfoRoomOccupation() == null && infoAula.getInfoRoomOccupation() == null) || (getInfoRoomOccupation() != null && getInfoRoomOccupation()
                                    .equals(infoAula.getInfoRoomOccupation())));
        }
        return resultado;
    }

    public Lesson getLesson() {
        if (!Strings.isNullOrEmpty(getExternalId())) {
            Lesson lesson = FenixFramework.getDomainObject(getExternalId());
            if (FenixFramework.isDomainObjectValid(lesson)) {
                return lesson;
            }
        }
        return null;
    }

    public String getOccurrenceWeeksAsString() {
        return getLesson().getOccurrenceWeeksAsString();
    }

}
