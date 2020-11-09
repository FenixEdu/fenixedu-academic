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
/*
 * InfoShift.java
 * 
 * Created on 31 de Outubro de 2002, 12:35
 */

package org.fenixedu.academic.dto;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftEnrolment;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.NumberUtils;

public class InfoShift extends InfoObject {

    public static final Comparator<InfoShift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = new Comparator<InfoShift>() {

        @Override
        public int compare(InfoShift o1, InfoShift o2) {
            final int c1 = o1.getShift().getExecutionCourse().getNome().compareTo(o2.getShift().getExecutionCourse().getNome());
            if (c1 != 0) {
                return c1;
            }
            final int c2 = o1.getShiftTypesIntegerComparator().compareTo(o2.getShiftTypesIntegerComparator());
            if (c2 != 0) {
                return c2;
            }
            final int c3 = o1.getLessonsStringComparator().compareTo(o2.getLessonsStringComparator());
            return c3 == 0 ? o1.getShift().getExternalId().compareTo(o2.getShift().getExternalId()) : c3;
        }

    };

    private final Shift shift;

    private Integer capacity;

    public InfoShift(final Shift shift) {
        this.shift = shift;
        this.capacity = 0;
    }

    public Integer getSize() {
        return Integer.valueOf(getShift().getAssociatedClassesSet().size());
    }

    public String getNome() {
        return getShift().getNome();
    }

    public InfoExecutionCourse getInfoDisciplinaExecucao() {
        return InfoExecutionCourse.newInfoFromDomain(getShift().getExecutionCourse());
    }

    public Set<ShiftType> getSortedTypes() {
        return getShift().getSortedTypes();
    }

    public Integer getShiftTypesIntegerComparator() {
        return getShift().getShiftTypesIntegerComparator();
    }

    public String getLessonsStringComparator() {
        return getShift().getLessonsStringComparator();
    }

    public String getShiftTypesPrettyPrint() {
        return getShift().getShiftTypesPrettyPrint();
    }

    public String getShiftTypesCodePrettyPrint() {
        return getShift().getShiftTypesCodePrettyPrint();
    }

    public Integer getLotacao() {
        return getShift().getLotacao();
    }

    public Integer getOcupation() {
        return ShiftEnrolment.getTotalEnrolments(getShift());
    }

    public Integer getGroupCapacity() {
        return this.capacity;
    }

    public void setGroupCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPercentage() {
        return NumberUtils.formatNumber(Double.valueOf(getOcupation().floatValue() * 100 / getLotacao().floatValue()), 1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getShift() == ((InfoShift) obj).getShift();
    }

    @Override
    public String toString() {
        return getShift().toString();
    }

    public String getLessons() {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<InfoLesson> infoLessonsList = getInfoLessons();
        if (infoLessonsList != null) {
            int index = 0;
            for (final InfoLesson infoLesson : infoLessonsList) {
                index = index + 1;
                stringBuilder.append(infoLesson.getDiaSemana().toString());
                stringBuilder.append(" (");
                stringBuilder.append(DateFormatUtil.format("HH:mm", infoLesson.getInicio().getTime()));
                stringBuilder.append("-");
                stringBuilder.append(DateFormatUtil.format("HH:mm", infoLesson.getFim().getTime()));
                stringBuilder.append(") ");
                if (infoLesson.getInfoSala() != null) {
                    stringBuilder.append(infoLesson.getInfoSala().getNome().toString());
                }

                int last = (infoLessonsList.size());
                if (index != last || (index != 1 && index != last)) {
                    stringBuilder.append(" , ");
                } else {
                    stringBuilder.append(" ");
                }
            }
        }

        return stringBuilder.toString();
    }

    public List<InfoLesson> getInfoLessons() {
        final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
        for (final Lesson lesson : getShift().getLessonsOrderedByWeekDayAndStartTime()) {
            infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoLessons;
    }

    public List<InfoClass> getInfoClasses() {
        final List<InfoClass> infoClasses = new ArrayList<InfoClass>();
        for (final SchoolClass schoolClass : getShift().getAssociatedClassesSet()) {
            infoClasses.add(InfoClass.newInfoFromDomain(schoolClass));
        }
        return infoClasses;
    }

    @Override
    public String getExternalId() {
        return getShift().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public static InfoShift newInfoFromDomain(final Shift shift) {
        return shift == null ? null : new InfoShift(shift);
    }

    public Shift getShift() {
        return shift;
    }

    public boolean containsType(ShiftType shiftType) {
        return getShift().containsType(shiftType);
    }

    public String getComment() {
        return getShift().getComment();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (this.getExternalId() != null) {
            return this.getExternalId().hashCode();
        }

        return 0;
    }

}
