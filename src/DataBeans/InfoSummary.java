/*
 * Created on 21/Jul/2003
 */

package DataBeans;

import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.ISummary;
import Util.DiaSemana;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes 21/Jul/2003 fenix-head DataBeans
 */
public class InfoSummary extends InfoObject implements Comparable {

    private String title;

    private Calendar summaryDate;

    private Calendar summaryHour;

    private Date lastModifiedDate;

    private String summaryText;

    private InfoExecutionCourse infoExecutionCourse;

    private TipoAula summaryType;

    private InfoShift infoShift;

    private InfoProfessorship infoProfessorship;

    private InfoTeacher infoTeacher;

    private String teacherName;

    private Integer studentsNumber;

    private Boolean isExtraLesson;

    private Integer lessonIdSelected;

    private Integer keyRoom;

    private InfoRoom infoRoom;

    /**
     * @deprecated In spite of this method is deprecated it is used, because in
     *             the past Summary object are linked to the Execution Course
     *             object. Now Summary object are linked to the Shift object.
     *             Once were decided not make migrations, all Summary with Shift
     *             null or zero should be readed by Execution Course object.
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @deprecated In spite of this method is deprecated it is used, because in
     *             the past Summary object are linked to the Execution Course
     *             object. Now Summary object are linked to the Shift object.
     *             Once were decided not make migrations, all Summary with Shift
     *             null or zero should be readed by Execution Course object.
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @deprecated In spite of this method is deprecated it is used, because in
     *             the past Summary object are linked to the ExecutionCourse
     *             object and has Summary Type attribute. Now Summary object are
     *             linked to the Shift object, then Summary Type attribute is
     *             the same than Shift Type. Once were decided not make
     *             migrations, all Summary with Shift null or zero should be
     *             readed by ExecutionCourse object and if necessary by Summary
     *             Type.
     */
    public TipoAula getSummaryType() {
        return summaryType;
    }

    /**
     * @deprecated In spite of this method is deprecated it is used, because in
     *             the past Summary object are linked to the ExecutionCourse
     *             object and has Summary Type attribute. Now Summary object are
     *             linked to the Shift object, then Summary Type attribute is
     *             the same than Shift Type. Once were decided not make
     *             migrations, all Summary with Shift null or zero should be
     *             readed by ExecutionCourse object and if necessary by Summary
     *             Type.
     */
    public void setSummaryType(TipoAula summaryType) {
        this.summaryType = summaryType;
    }

    /**
     * @return
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return
     */
    public Calendar getSummaryDate() {
        return summaryDate;
    }

    /**
     * @param summaryDate
     */
    public void setSummaryDate(Calendar summaryDate) {
        this.summaryDate = summaryDate;
    }

    /**
     * @return
     */
    public Calendar getSummaryHour() {
        return summaryHour;
    }

    /**
     * @param summaryHour
     */
    public void setSummaryHour(Calendar summaryHour) {
        this.summaryHour = summaryHour;
    }

    /**
     * @return
     */
    public String getSummaryText() {
        return summaryText;
    }

    /**
     * @param summaryText
     */
    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the infoProfessorship.
     */
    public InfoProfessorship getInfoProfessorship() {
        return infoProfessorship;
    }

    /**
     * @param infoProfessorship
     *            The infoProfessorship to set.
     */
    public void setInfoProfessorship(InfoProfessorship infoProfessorship) {
        this.infoProfessorship = infoProfessorship;
    }

    /**
     * @return Returns the infoShift.
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * @param infoShift
     *            The infoShift to set.
     */
    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return Returns the studentNumber.
     */
    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    /**
     * @param studentNumber
     *            The studentNumber to set.
     */
    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    /**
     * @return Returns the teacherName.
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName
     *            The teacherName to set.
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * @return Returns the isExtraLesson.
     */
    public Boolean getIsExtraLesson() {
        return isExtraLesson;
    }

    /**
     * @param isExtraLesson
     *            The isExtraLesson to set.
     */
    public void setIsExtraLesson(Boolean isExtraLesson) {
        this.isExtraLesson = isExtraLesson;
    }

    /**
     * @return Returns the lessonIdSeleted.
     */
    public Integer getLessonIdSelected() {
        return lessonIdSelected;
    }

    /**
     * @param lessonIdSeleted
     *            The lessonIdSeleted to set.
     */
    public void setLessonIdSelected(Integer lessonIdSelected) {
        this.lessonIdSelected = lessonIdSelected;
    }

    /**
     * @return Returns the infoRoom.
     */
    public InfoRoom getInfoRoom() {
        return infoRoom;
    }

    /**
     * @param infoRoom
     *            The infoRoom to set.
     */
    public void setInfoRoom(InfoRoom infoRoom) {
        this.infoRoom = infoRoom;
    }

    /**
     * @return Returns the keyRoom.
     */
    public Integer getKeyRoom() {
        return keyRoom;
    }

    /**
     * @param keyRoom
     *            The keyRoom to set.
     */
    public void setKeyRoom(Integer keyRoom) {
        this.keyRoom = keyRoom;
    }

    /***************************************************************************
     * Usefull methods for form in user's interface
     **************************************************************************/
    public String getSummaryDateInput() {
        String result = "";
        if (getSummaryDate() != null) {
            Calendar date = getSummaryDate();
            result += date.get(Calendar.DAY_OF_MONTH);
            result += "/";
            result += date.get(Calendar.MONTH) + 1;
            result += "/";
            result += date.get(Calendar.YEAR);
        }
        return result;
    }

    public String getSummaryHourInput() {
        String result = "";
        if (getSummaryHour() != null) {
            Calendar date = getSummaryHour();
            result += date.get(Calendar.HOUR_OF_DAY);
            result += ":";
            if (date.get(Calendar.MINUTE) < 10) {
                result += "0";
            }
            result += date.get(Calendar.MINUTE);
        }
        return result;
    }

    public String getLastModifiedDateFormatted() {
        String result = "";

        if (getLastModifiedDate() != null) {
            Date date = getLastModifiedDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            result += calendar.get(Calendar.DAY_OF_MONTH);
            result += "/";
            result += calendar.get(Calendar.MONTH) + 1;
            result += "/";
            result += calendar.get(Calendar.YEAR);
            result += " ";
            result += calendar.get(Calendar.HOUR_OF_DAY);
            result += ":";
            if (calendar.get(Calendar.MINUTE) < 10) {
                result += "0";
            }
            result += calendar.get(Calendar.MINUTE);
        }
        return result;
    }

    /**
     * @return String that describe the lesson
     */
    public String getSummaryLesson() {
        String result = "";

        if (getSummaryDate() != null && getSummaryHour() != null) {
            Calendar dateAndHourSummary = Calendar.getInstance();
            dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            dateAndHourSummary.set(Calendar.MONTH, summaryDate
                    .get(Calendar.MONTH));
            dateAndHourSummary.set(Calendar.YEAR, summaryDate
                    .get(Calendar.YEAR));
            dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour
                    .get(Calendar.HOUR_OF_DAY));
            dateAndHourSummary.set(Calendar.MINUTE, summaryHour
                    .get(Calendar.MINUTE));
            dateAndHourSummary.set(Calendar.SECOND, 00);

            Calendar beginLesson = Calendar.getInstance();
            beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            Calendar endLesson = Calendar.getInstance();
            endLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            if (infoShift.getInfoLessons() != null
                    && infoShift.getInfoLessons().size() > 0) {
                ListIterator listIterator = infoShift.getInfoLessons()
                        .listIterator();
                while (listIterator.hasNext()) {
                    InfoLesson lesson = (InfoLesson) listIterator.next();

                    beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio()
                            .get(Calendar.HOUR_OF_DAY));
                    beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(
                            Calendar.MINUTE));
                    beginLesson.set(Calendar.SECOND, 00);

                    endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(
                            Calendar.HOUR_OF_DAY));
                    endLesson.set(Calendar.MINUTE, lesson.getFim().get(
                            Calendar.MINUTE));
                    endLesson.set(Calendar.SECOND, 00);

                    if (infoShift.getTipo().equals(lesson.getTipo())
                            && dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson
                                    .getDiaSemana().getDiaSemana().intValue()
                            && !beginLesson.after(dateAndHourSummary)
                            && !endLesson.before(dateAndHourSummary)) {
                        result += lesson.getDiaSemana().toString();
                        result += " ";
                        result += beginLesson.get(Calendar.HOUR_OF_DAY);
                        result += ":";
                        if (beginLesson.get(Calendar.MINUTE) < 10) {
                            result += "0";
                        }
                        result += beginLesson.get(Calendar.MINUTE);
                        result += "-";
                        result += endLesson.get(Calendar.HOUR_OF_DAY);
                        result += ":";
                        if (endLesson.get(Calendar.MINUTE) < 10) {
                            result += "0";
                        }
                        result += endLesson.get(Calendar.MINUTE);
                        result += " ";
                        result += lesson.getInfoSala().getNome();

                    }
                }
            }
        }
        return result;
    }

    /**
     * @return String that describe the lesson
     */
    public String getLesson() {
        if (getSummaryDate() != null && getSummaryHour() != null) {
            Calendar dateAndHourSummary = Calendar.getInstance();
            dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            dateAndHourSummary.set(Calendar.MONTH, summaryDate
                    .get(Calendar.MONTH));
            dateAndHourSummary.set(Calendar.YEAR, summaryDate
                    .get(Calendar.YEAR));
            dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour
                    .get(Calendar.HOUR_OF_DAY));
            dateAndHourSummary.set(Calendar.MINUTE, summaryHour
                    .get(Calendar.MINUTE));
            dateAndHourSummary.set(Calendar.SECOND, 00);

            Calendar beginLesson = Calendar.getInstance();
            beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            Calendar endLesson = Calendar.getInstance();
            endLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            if (infoShift.getInfoLessons() != null
                    && infoShift.getInfoLessons().size() > 0) {
                ListIterator listIterator = infoShift.getInfoLessons()
                        .listIterator();
                while (listIterator.hasNext()) {
                    InfoLesson lesson = (InfoLesson) listIterator.next();

                    beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio()
                            .get(Calendar.HOUR_OF_DAY));
                    beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(
                            Calendar.MINUTE));
                    beginLesson.set(Calendar.SECOND, 00);

                    endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(
                            Calendar.HOUR_OF_DAY));
                    endLesson.set(Calendar.MINUTE, lesson.getFim().get(
                            Calendar.MINUTE));
                    endLesson.set(Calendar.SECOND, 00);

                    if (infoShift.getTipo().equals(lesson.getTipo())
                            && dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson
                                    .getDiaSemana().getDiaSemana().intValue()
                            && !beginLesson.after(dateAndHourSummary)
                            && !endLesson.before(dateAndHourSummary)) { return lesson
                            .getIdInternal().toString(); }
                }
            }
        }
        return "0";//extra lesson
    }

    /**
     * @return String that describe the lesson
     */
    public String getSummaryExtraLesson() {
        String result = "";

        if (getSummaryDate() != null && getSummaryHour() != null) {
            Calendar dateAndHourSummary = Calendar.getInstance();
            dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate
                    .get(Calendar.DAY_OF_MONTH));
            dateAndHourSummary.set(Calendar.MONTH, summaryDate
                    .get(Calendar.MONTH));
            dateAndHourSummary.set(Calendar.YEAR, summaryDate
                    .get(Calendar.YEAR));
            dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour
                    .get(Calendar.HOUR_OF_DAY));
            dateAndHourSummary.set(Calendar.MINUTE, summaryHour
                    .get(Calendar.MINUTE));
            dateAndHourSummary.set(Calendar.SECOND, 00);

            result += new DiaSemana(dateAndHourSummary
                    .get(Calendar.DAY_OF_WEEK)).toString();
            result += " ";
            result += dateAndHourSummary.get(Calendar.HOUR_OF_DAY);
            result += ":";
            if (dateAndHourSummary.get(Calendar.MINUTE) < 10) {
                result += "0";
            }
            result += dateAndHourSummary.get(Calendar.MINUTE);
        }
        return result;
    }

    /**
     * @return The shift selected that the summary belong
     */
    public String getShift() {
        if (infoShift != null && infoShift.getIdInternal() != null) { return infoShift
                .getIdInternal().toString(); }

        return null;
    }

    /**
     * @return The romm selected that the summary belong
     */
    public String getRoom() {
        if (infoRoom != null && infoRoom.getIdInternal() != null) { return infoRoom
                .getIdInternal().toString(); }

        return null;
    }

    /**
     * @return The professor ship id that represented the teacher who lectured a
     *         lesson
     */
    public String getTeacher() {
        if (infoProfessorship != null
                && infoProfessorship.getIdInternal() != null) { return infoProfessorship
                .getIdInternal().toString(); }

        if (infoTeacher != null) { return "0"; }

        if (teacherName != null) { return "-1"; }

        return null;
    }

    /**
     * @return The teacher's number who lectured a lesson
     */
    public String getTeacherNumber() {
        if (infoTeacher != null && infoTeacher.getTeacherNumber() != null) { return infoTeacher
                .getTeacherNumber().toString(); }

        return null;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSummary) {
            InfoSummary infoSummary = (InfoSummary) obj;
            result = getIdInternal().equals(infoSummary.getIdInternal());
            result = result
                    || (getInfoExecutionCourse().equals(
                            infoSummary.getInfoExecutionCourse())
                            && getSummaryDate().get(Calendar.DAY_OF_MONTH) == infoSummary
                                    .getSummaryDate()
                                    .get(Calendar.DAY_OF_MONTH)
                            && getSummaryDate().get(Calendar.MONTH) == infoSummary
                                    .getSummaryDate().get(Calendar.MONTH)
                            && getSummaryDate().get(Calendar.YEAR) == infoSummary
                                    .getSummaryDate().get(Calendar.YEAR)
                            && getSummaryHour().get(Calendar.HOUR_OF_DAY) == infoSummary
                                    .getSummaryHour().get(Calendar.HOUR_OF_DAY) && getSummaryHour()
                            .get(Calendar.MINUTE) == infoSummary
                            .getSummaryHour().get(Calendar.MINUTE));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        InfoSummary infoSummary = (InfoSummary) arg0;
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(getSummaryDate(), infoSummary
                .getSummaryDate()) == 0) {
            return hourComparator.compare(getSummaryHour(), infoSummary
                    .getSummaryHour());
        } else {
            return dateComparator.compare(getSummaryDate(), infoSummary
                    .getSummaryDate());
        }

    }

    public void copyFromDomain(ISummary summary) {
        super.copyFromDomain(summary);
        if (summary != null) {
            setIsExtraLesson(summary.getIsExtraLesson());
            setLastModifiedDate(summary.getLastModifiedDate());
            setStudentsNumber(summary.getStudentsNumber());
            setSummaryDate(summary.getSummaryDate());
            setSummaryHour(summary.getSummaryDate());
            setSummaryText(summary.getSummaryText());
            setTeacherName(summary.getTeacherName());
            setTitle(summary.getTitle());
            setSummaryType(summary.getSummaryType());
        }
    }

    /**
     * @param summary
     * @return
     */
    public static InfoSummary newInfoFromDomain(ISummary summary) {
        InfoSummary infoSummary = null;
        if (summary != null) {
            infoSummary = new InfoSummary();
            infoSummary.copyFromDomain(summary);
        }
        return infoSummary;
    }
}