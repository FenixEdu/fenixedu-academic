/*
 * InfoExam.java
 * 
 * Created on 2003/03/19
 */
package DataBeans;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IExam;
import Util.EvaluationType;
import Util.Season;
import Util.DiaSemana;

public class InfoExam extends InfoWrittenEvaluation implements ISiteComponent {

    protected Season season;

    protected String publishmentMessage;

    protected Integer enrolledStudents;

    private List infoExecutionCourses;

    /**
     * The following variable serves the purpose of indicating the execution
     * course associated with this exam through which the exam was obtained. It
     * should serve only for view purposes!!! It was created to be used and set
     * by the ExamsMap Utilities. It has no meaning in the business logic.
     */
    private InfoExecutionCourse infoExecutionCourse;

    public InfoExam() {
    }

    public InfoExam(Calendar day, Calendar beginning, Calendar end,
            Season season) {
        this.setDay(day);
        this.setBeginning(beginning);
        this.setEnd(end);
        this.setSeason(season);

    }

    public String toString() {
        return "[INFOEXAM:" + " day= '" + this.getDay() + "'" + " beginning= '"
                + this.getBeginning() + "'" + " end= '" + this.getEnd() + "'"
                + " season= '" + this.getSeason() + "'" + "";
    }

    /**
     * @return
     */
    public Season getSeason() {
        return season;
    }

    /**
     * @param season
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @param course
     */
    public void setInfoExecutionCourse(InfoExecutionCourse course) {
        infoExecutionCourse = course;
    }

    /**
     * 
     * @return
     */
    public List getAssociatedRooms() {
        return (List) CollectionUtils.collect(super
                .getAssociatedRoomOccupation(), new Transformer() {

            public Object transform(Object arg0) {
                InfoRoomOccupation roomOccupation = (InfoRoomOccupation) arg0;
                return roomOccupation.getInfoRoom();
            }
        });
    }

    /**
     * @return
     */
    public String getPublishmentMessage() {
        return publishmentMessage;
    }

    /**
     * @param publishmentMessage
     */
    public void setPublishmentMessage(String publishmentMessage) {
        this.publishmentMessage = publishmentMessage;
    }

    public String getDate() {
        if (getDay() == null) {
            return "0/0/0";
        }
        String result = String.valueOf(getDay().get(Calendar.DAY_OF_MONTH));
        result += "/";
        result += String.valueOf(getDay().get(Calendar.MONTH) + 1);
        result += "/";
        result += String.valueOf(getDay().get(Calendar.YEAR));
        return result;
    }

    public String getBeginningHour() {
        if (getBeginning() == null) {
            return "00:00";
        }
        String result = format(String.valueOf(getBeginning().get(
                Calendar.HOUR_OF_DAY)));
        result += ":";
        result += format(String.valueOf(getBeginning().get(Calendar.MINUTE)));
        return result;
    }

    public String getEndHour() {
        if (getEnd() == null) {
            return "00:00";
        }
        String result = format(String.valueOf(getEnd()
                .get(Calendar.HOUR_OF_DAY)));
        result += ":";
        result += format(String.valueOf(getEnd().get(Calendar.MINUTE)));
        return result;
    }

    /**
     * @param string
     * @return
     */
    private String format(String string) {
        if (string.length() == 1) {
            string = "0" + string;
        }
        return string;
    }

    /**
     * @return Returns the enrolledStudents.
     */
    public Integer getEnrolledStudents() {
        return enrolledStudents;
    }

    /**
     * @param enrolledStudents
     *            The enrolledStudents to set.
     */
    public void setEnrolledStudents(Integer enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String dateFormatter(Calendar calendar) {
        String result = "";
        if (calendar != null) {
            result += calendar.get(Calendar.DAY_OF_MONTH);
            result += "/";
            result += calendar.get(Calendar.MONTH) + 1;
            result += "/";
            result += calendar.get(Calendar.YEAR);
        }
        return result;
    }

    public String timeFormatter(Calendar calendar) {
        String result = "";
        if (calendar != null) {
            result += calendar.get(Calendar.HOUR_OF_DAY);
            result += ":";
            if (calendar.get(Calendar.MINUTE) < 10) {
                result += "0";
                result += calendar.get(Calendar.MINUTE);
            } else {
                result += calendar.get(Calendar.MINUTE);
            }
        }
        return result;
    }

    public String getEnrollmentBeginDayFormatted() {
        return dateFormatter(getEnrollmentBeginDay());
    }

    public String getEnrollmentEndDayFormatted() {
        return dateFormatter(getEnrollmentEndDay());
    }

    public String getEnrollmentBeginTimeFormatted() {
        return timeFormatter(getEnrollmentBeginTime());
    }

    public String getEnrollmentEndTimeFormatted() {
        return timeFormatter(getEnrollmentEndTime());
    }

    public boolean getEnrollmentAuthorization() {
        if (getEnrollmentEndDay() == null) {
            return false;
        }
        Calendar enrollmentEnd = Calendar.getInstance();
        enrollmentEnd.set(Calendar.DAY_OF_MONTH, getEnrollmentEndDay().get(
                Calendar.DAY_OF_MONTH));
        enrollmentEnd.set(Calendar.MONTH, getEnrollmentEndDay().get(
                Calendar.MONTH));
        enrollmentEnd.set(Calendar.YEAR, getEnrollmentEndDay().get(
                Calendar.YEAR));
        enrollmentEnd.set(Calendar.HOUR_OF_DAY, getEnrollmentEndTime().get(
                Calendar.HOUR_OF_DAY));
        enrollmentEnd.set(Calendar.MINUTE, getEnrollmentEndTime().get(
                Calendar.MINUTE));
        Calendar now = Calendar.getInstance();
        if (enrollmentEnd.getTimeInMillis() > now.getTimeInMillis()) {
            return true;
        }
        return false;

    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoExam) {
            InfoExam infoExam = (InfoExam) obj;
            result = getIdInternal().equals(infoExam.getIdInternal())
                    && getDate().equals(infoExam.getDate())
                    && getEnrollmentBeginDayFormatted().equals(
                            infoExam.getEnrollmentBeginDayFormatted())
                    && getEnrollmentBeginTimeFormatted().equals(
                            infoExam.getEnrollmentBeginTimeFormatted())
                    && getEnrollmentEndDayFormatted().equals(
                            infoExam.getEnrollmentEndDayFormatted())
                    && getEnrollmentEndTimeFormatted().equals(
                            infoExam.getEnrollmentEndTimeFormatted());
        }
        return result;
    }

    public DiaSemana getDiaSemana() 
    {
        Calendar day = this.getDay();
        return new DiaSemana(day.get(Calendar.DAY_OF_WEEK));
    }
    
    /**
     * @return Returns the infoExecutionCourses.
     */
    public List getInfoExecutionCourses() {
        return infoExecutionCourses;
    }

    /**
     * @param infoExecutionCourses
     *            The infoExecutionCourses to set.
     */
    public void setInfoExecutionCourses(List infoExecutionCourses) {
        this.infoExecutionCourses = infoExecutionCourses;
    }

    public void copyFromDomain(IExam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setSeason(exam.getSeason());
            setEvaluationType(EvaluationType.EXAM_TYPE);
        }
    }

    /**
     * @param evaluation
     * @return
     */
    public static InfoExam newInfoFromDomain(IExam exam) {
        InfoExam infoExam = null;
        if (exam != null) {
            infoExam = new InfoExam();
            infoExam.copyFromDomain(exam);
        }
        return infoExam;
    }
}