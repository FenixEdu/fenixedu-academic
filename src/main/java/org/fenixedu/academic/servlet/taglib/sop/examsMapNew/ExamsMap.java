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
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;

public class ExamsMap {

    private List<ExamsMapSlot> days;

    private List curricularYears;

    private List<InfoExecutionCourse> executionCourses;

    private InfoExecutionDegree infoExecutionDegree;

    private Calendar firstDayOfSeason;

    private Calendar lastDayOfSeason;

    private InfoExecutionPeriod infoExecutionPeriod;

    public ExamsMap(InfoRoomExamsMap infoRoomExamsMap, Locale locale) {
        Calendar firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
        Calendar lastDayOfSeason = infoRoomExamsMap.getEndSeason2();
        this.firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
        this.lastDayOfSeason = infoRoomExamsMap.getEndSeason2();

        days = new ArrayList<ExamsMapSlot>();
        if (firstDayOfSeason.get(Calendar.YEAR) != lastDayOfSeason.get(Calendar.YEAR)) {
            for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < makeLastDayOfYear(firstDayOfSeason, locale).get(
                    Calendar.DAY_OF_YEAR); day++) {
                Calendar tempDayToAdd = makeDay(firstDayOfSeason, day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR), locale);
                if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    days.add(new ExamsMapSlot(tempDayToAdd, findExamsFromListOfExams(tempDayToAdd, infoRoomExamsMap.getExams())));
                }
            }
            firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason, locale);
        }
        for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < lastDayOfSeason.get(Calendar.DAY_OF_YEAR) + 1; day++) {
            Calendar tempDayToAdd = makeDay(firstDayOfSeason, day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR), locale);
            if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days.add(new ExamsMapSlot(tempDayToAdd, findExamsFromListOfExams(tempDayToAdd, infoRoomExamsMap.getExams())));
            }
        }
    }

    public ExamsMap(InfoExamsMap infoExamsMap, Locale locale) {
        this.firstDayOfSeason = infoExamsMap.getStartSeason1();
        this.lastDayOfSeason = infoExamsMap.getEndSeason2();

        setInfoExecutionPeriod(infoExamsMap.getInfoExecutionPeriod());
        setInfoExecutionDegree(infoExamsMap.getInfoExecutionDegree());

        Calendar firstDayOfSeason = infoExamsMap.getStartSeason1();
        Calendar lastDayOfSeason = infoExamsMap.getEndSeason2();

        curricularYears = infoExamsMap.getCurricularYears();
        executionCourses = infoExamsMap.getExecutionCourses();

        days = new ArrayList<ExamsMapSlot>();
        if (firstDayOfSeason.get(Calendar.YEAR) != lastDayOfSeason.get(Calendar.YEAR)) {
            for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day <= makeLastDayOfYear(firstDayOfSeason, locale).get(
                    Calendar.DAY_OF_YEAR); day++) {
                Calendar tempDayToAdd = makeDay(firstDayOfSeason, day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR), locale);
                if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    days.add(new ExamsMapSlot(tempDayToAdd, findExams(tempDayToAdd, infoExamsMap.getExecutionCourses())));
                }
            }

            firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason, locale);
        }

        for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < lastDayOfSeason.get(Calendar.DAY_OF_YEAR) + 1; day++) {

            Calendar tempDayToAdd = makeDay(firstDayOfSeason, day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR), locale);
            if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days.add(new ExamsMapSlot(tempDayToAdd, findExams(tempDayToAdd, infoExamsMap.getExecutionCourses())));
            }
        }
    }

    private List<InfoExam> findExams(Calendar day, List executionCourses) {
        List<InfoExam> result = new ArrayList<InfoExam>();

        for (int i = 0; i < executionCourses.size(); i++) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) executionCourses.get(i);
            List infoExams = infoExecutionCourse.getAssociatedInfoExams();

            for (int j = 0; j < infoExams.size(); j++) {
                InfoExam infoExam = (InfoExam) infoExams.get(j);

                if (sameDayAsExam(day, infoExam)) {
                    infoExam.setInfoExecutionCourse(infoExecutionCourse);
                    result.add(infoExam);
                }
            }
        }

        return result;
    }

    private List<InfoExam> findExamsFromListOfExams(Calendar day, List infoExams) {
        List<InfoExam> result = new ArrayList<InfoExam>();

        for (int j = 0; j < infoExams.size(); j++) {
            InfoExam infoExam = (InfoExam) infoExams.get(j);

            if (sameDayAsExam(day, infoExam)) {
                result.add(infoExam);
            }
        }

        return result;
    }

    private boolean sameDayAsExam(Calendar day, InfoExam infoExam) {
        return day.get(Calendar.YEAR) == infoExam.getDay().get(Calendar.YEAR)
                && day.get(Calendar.MONTH) == infoExam.getDay().get(Calendar.MONTH)
                && day.get(Calendar.DAY_OF_MONTH) == infoExam.getDay().get(Calendar.DAY_OF_MONTH);
    }

    private Calendar makeFirstDayOfYear(Calendar someDayOfSameYear, Locale locale) {
        Calendar result = Calendar.getInstance(locale);

        result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
        result.set(Calendar.MONTH, Calendar.JANUARY);
        result.set(Calendar.DAY_OF_MONTH, 1);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    private Calendar makeLastDayOfYear(Calendar someDayOfSameYear, Locale locale) {
        Calendar result = Calendar.getInstance();

        result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
        result.set(Calendar.MONTH, Calendar.DECEMBER);
        result.set(Calendar.DAY_OF_MONTH, 31);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    private Calendar makeDay(Calendar dayToCopy, int offset, Locale locale) {
        Calendar result = Calendar.getInstance(locale);
        result.set(Calendar.YEAR, dayToCopy.get(Calendar.YEAR));
        result.set(Calendar.DAY_OF_YEAR, dayToCopy.get(Calendar.DAY_OF_YEAR) + offset);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    public List getDays() {
        return days;
    }

    public List getCurricularYears() {
        return curricularYears;
    }

    public List getExecutionCourses() {
        return executionCourses;
    }

    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    public Calendar getFirstDayOfSeason() {
        return firstDayOfSeason;
    }

    public void setFirstDayOfSeason(Calendar firstDayOfSeason) {
        this.firstDayOfSeason = firstDayOfSeason;
    }

    public Calendar getLastDayOfSeason() {
        return lastDayOfSeason;
    }

    public void setLastDayOfSeason(Calendar lastDayOfSeason) {
        this.lastDayOfSeason = lastDayOfSeason;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    public boolean hasExecutionCoursesForGivenCurricularYear(Integer curricularYear) {
        for (InfoExecutionCourse infoExecutionCourse : executionCourses) {
            if (infoExecutionCourse.getCurricularYear() == curricularYear) {
                return true;
            }
        }
        return false;
    }

}
