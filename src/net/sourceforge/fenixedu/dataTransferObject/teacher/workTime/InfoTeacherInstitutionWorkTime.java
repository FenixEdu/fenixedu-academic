/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.workTime;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public class InfoTeacherInstitutionWorkTime extends InfoObject {
    private Date endTime;

    private InfoExecutionPeriod infoExecutionPeriod;

    private InfoTeacher infoTeacher;

    private Integer keyTeacher;

    private Date startTime;

    private DiaSemana weekDay;

    /**
     *  
     */
    public InfoTeacherInstitutionWorkTime() {
        super();
    }

    /**
     * @param idInternal
     */
    public InfoTeacherInstitutionWorkTime(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the endTime.
     */
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return this.infoExecutionPeriod;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return this.infoTeacher;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return this.keyTeacher;
    }

    /**
     * @return Returns the startTime.
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * @return Returns the weekDay.
     */
    public DiaSemana getWeekDay() {
        return this.weekDay;
    }

    /**
     * @param endTime
     *            The endTime to set.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @param startTime
     *            The startTime to set.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @param weekDay
     *            The weekDay to set.
     */
    public void setWeekDay(DiaSemana weekDay) {
        this.weekDay = weekDay;
    }

    public static InfoTeacherInstitutionWorkTime newInfoFromDomain(TeacherInstitutionWorkTime item) {
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(item.getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(item.getExecutionPeriod());

        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = new InfoTeacherInstitutionWorkTime();
        infoTeacherInstitutionWorkTime.setEndTime(item.getEndTime());
        infoTeacherInstitutionWorkTime.setIdInternal(item.getIdInternal());
        infoTeacherInstitutionWorkTime.setStartTime(item.getStartTime());
        infoTeacherInstitutionWorkTime.setWeekDay(item.getWeekDay());

        infoTeacherInstitutionWorkTime.setInfoTeacher(infoTeacher);
        infoTeacherInstitutionWorkTime.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoTeacherInstitutionWorkTime;
    }

}