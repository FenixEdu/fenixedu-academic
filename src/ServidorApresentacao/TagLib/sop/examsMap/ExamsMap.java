/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoRoomExamsMap;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ExamsMap {

    private List days;

    private List curricularYears;

    private List executionCourses;

    private InfoExecutionDegree infoExecutionDegree;

    private Calendar firstDayOfSeason;

    private Calendar lastDayOfSeason;

    /**
     * @param infoRoomExamsMap
     */
    public ExamsMap(InfoRoomExamsMap infoRoomExamsMap) {
        Calendar firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
        Calendar lastDayOfSeason = infoRoomExamsMap.getEndSeason2();
        this.firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
        this.lastDayOfSeason = infoRoomExamsMap.getEndSeason2();

        days = new ArrayList();
        if (firstDayOfSeason.get(Calendar.YEAR) != lastDayOfSeason.get(Calendar.YEAR)) {
            for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < makeLastDayOfYear(
                    firstDayOfSeason).get(Calendar.DAY_OF_YEAR); day++) {
                Calendar tempDayToAdd = makeDay(firstDayOfSeason, day
                        - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
                if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    days.add(new ExamsMapSlot(tempDayToAdd, findExamsFromListOfExams(tempDayToAdd,
                            infoRoomExamsMap.getExams())));
                }
            }
            firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason);
        }
        for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < lastDayOfSeason
                .get(Calendar.DAY_OF_YEAR) + 1; day++) {
            Calendar tempDayToAdd = makeDay(firstDayOfSeason, day
                    - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
            if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days.add(new ExamsMapSlot(tempDayToAdd, findExamsFromListOfExams(tempDayToAdd,
                        infoRoomExamsMap.getExams())));
            }
        }
    }

    public ExamsMap(InfoExamsMap infoExamsMap) {
        this.firstDayOfSeason = infoExamsMap.getStartSeason1();
        this.lastDayOfSeason = infoExamsMap.getEndSeason2();

        setInfoExecutionDegree(infoExamsMap.getInfoExecutionDegree());

        Calendar firstDayOfSeason = infoExamsMap.getStartSeason1();
        Calendar lastDayOfSeason = infoExamsMap.getEndSeason2();

        curricularYears = infoExamsMap.getCurricularYears();
        executionCourses = infoExamsMap.getExecutionCourses();

        days = new ArrayList();
        if (firstDayOfSeason.get(Calendar.YEAR) != lastDayOfSeason.get(Calendar.YEAR)) {
            for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < makeLastDayOfYear(
                    firstDayOfSeason).get(Calendar.DAY_OF_YEAR); day++) {
                Calendar tempDayToAdd = makeDay(firstDayOfSeason, day
                        - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
                if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                    days.add(new ExamsMapSlot(tempDayToAdd, findExams(tempDayToAdd, infoExamsMap
                            .getExecutionCourses())));
            }

            firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason);
        }

        for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR); day < lastDayOfSeason
                .get(Calendar.DAY_OF_YEAR) + 1; day++) {
            Calendar tempDayToAdd = makeDay(firstDayOfSeason, day
                    - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
            if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                days.add(new ExamsMapSlot(tempDayToAdd, findExams(tempDayToAdd, infoExamsMap
                        .getExecutionCourses())));
        }
    }

    private List findExams(Calendar day, List executionCourses) {
        List result = new ArrayList();

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

    private List findExamsFromListOfExams(Calendar day, List infoExams) {
        List result = new ArrayList();

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

    // ------------------------------------------------------------------------------------------
    // --- Utils Para Manupulação de Datas
    // ------------------------------------------------------

    private Calendar makeFirstDayOfYear(Calendar someDayOfSameYear) {
        Calendar result = Calendar.getInstance();

        result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
        result.set(Calendar.MONTH, Calendar.JANUARY);
        result.set(Calendar.DAY_OF_MONTH, 1);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    private Calendar makeLastDayOfYear(Calendar someDayOfSameYear) {
        Calendar result = Calendar.getInstance();

        result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
        result.set(Calendar.MONTH, Calendar.DECEMBER);
        result.set(Calendar.DAY_OF_MONTH, 31);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    private Calendar makeDay(Calendar dayToCopy, int offset) {
        Calendar result = Calendar.getInstance();

        result.set(Calendar.YEAR, dayToCopy.get(Calendar.YEAR));
        result.set(Calendar.DAY_OF_YEAR, dayToCopy.get(Calendar.DAY_OF_YEAR) + offset);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);

        return result;
    }

    /**
     * @return
     */
    public List getDays() {
        return days;
    }

    /**
     * @return
     */
    public List getCurricularYears() {
        return curricularYears;
    }

    /**
     * @return
     */
    public List getExecutionCourses() {
        return executionCourses;
    }

    /**
     * @return Returns the infoExecutionDegree.
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @param infoExecutionDegree
     *            The infoExecutionDegree to set.
     */
    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    /**
     * @return Returns the firstDayOfSeason.
     */
    public Calendar getFirstDayOfSeason() {
        return firstDayOfSeason;
    }

    /**
     * @param firstDayOfSeason
     *            The firstDayOfSeason to set.
     */
    public void setFirstDayOfSeason(Calendar firstDayOfSeason) {
        this.firstDayOfSeason = firstDayOfSeason;
    }

    /**
     * @return Returns the lastDayOfSeason.
     */
    public Calendar getLastDayOfSeason() {
        return lastDayOfSeason;
    }

    /**
     * @param lastDayOfSeason
     *            The lastDayOfSeason to set.
     */
    public void setLastDayOfSeason(Calendar lastDayOfSeason) {
        this.lastDayOfSeason = lastDayOfSeason;
    }

}