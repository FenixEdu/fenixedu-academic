/*
 * Created on 21/Jul/2003
 *
 * 
 */
package Dominio;

import java.util.Calendar;
import java.util.Date;

import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 *  
 */
public class Summary extends DomainObject implements ISummary {
    private String title;

    private Calendar summaryDate;

    private Calendar summaryHour;

    private Date lastModifiedDate;

    private String summaryText;

    private IExecutionCourse executionCourse;

    private Integer keyExecutionCourse;

    private TipoAula summaryType;

    private Integer keyShift;

    private ITurno shift;

    private Integer keyProfessorship;

    private IProfessorship professorship;

    private Integer keyTeacher;

    private ITeacher teacher;

    private String teacherName;

    private Integer studentsNumber;

    private Boolean isExtraLesson;

    private Integer keyRoom;

    private ISala room;

    /**
     *  
     */
    public Summary() {
    }

    /**
     *  
     */
    public Summary(Integer summaryId) {
        setIdInternal(summaryId);

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
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @param executionCourse
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @param keyExecutionCourse
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }

    /**
     * @return Returns the summaryType.
     */
    public TipoAula getSummaryType() {
        return summaryType;
    }

    /**
     * @param summaryType
     *            The summaryType to set.
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
     * @return Returns the keyProfessorship.
     */
    public Integer getKeyProfessorship() {
        return keyProfessorship;
    }

    /**
     * @param keyProfessorship
     *            The keyProfessorship to set.
     */
    public void setKeyProfessorship(Integer keyProfessorship) {
        this.keyProfessorship = keyProfessorship;
    }

    /**
     * @return Returns the keyShift.
     */
    public Integer getKeyShift() {
        return keyShift;
    }

    /**
     * @param keyShift
     *            The keyShift to set.
     */
    public void setKeyShift(Integer keyShift) {
        this.keyShift = keyShift;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return Returns the professorship.
     */
    public IProfessorship getProfessorship() {
        return professorship;
    }

    /**
     * @param professorship
     *            The professorship to set.
     */
    public void setProfessorship(IProfessorship professorship) {
        this.professorship = professorship;
    }

    /**
     * @return Returns the shift.
     */
    public ITurno getShift() {
        return shift;
    }

    /**
     * @param shift
     *            The shift to set.
     */
    public void setShift(ITurno shift) {
        this.shift = shift;
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
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
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

    /**
     * @return Returns the room.
     */
    public ISala getRoom() {
        return room;
    }

    /**
     * @param room
     *            The room to set.
     */
    public void setRoom(ISala room) {
        this.room = room;
    }

    /**
     * @param obj
     */
    public boolean compareTo(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISummary) {
            ISummary summary = (ISummary) obj;

            resultado = (summary != null) && this.getShift().equals(summary.getShift())
                    && this.getSummaryDate().equals(summary.getSummaryDate())
                    && this.getSummaryHour().equals(summary.getSummaryHour())
                    && this.getSummaryText().equals(summary.getSummaryText())
                    && this.getTitle().equals(summary.getTitle());
        }
        return resultado;
    }

}