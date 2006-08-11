/*
 * Created on 28/Jan/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;

/**
 * 
 * TODO Remove InfoTeacher extends... doesn't makeSense
 * 
 * @author jpvl
 */
public class TeacherCreditsDetailsDTO extends DataTranferObject {
    private InfoCredits infoCredits;

    private String teacherName;

    private Integer teacherId;

    private InfoCategory category;

    private Integer teacherNumber;

    private InfoExecutionPeriod infoExecution;

    /**
     * @param infoTeacher
     */
    public TeacherCreditsDetailsDTO() {
    }

    /**
     * @return Returns the infoCredits.
     */
    public InfoCredits getInfoCredits() {
        return infoCredits;
    }

    /**
     * @param infoCredits
     *            The infoCredits to set.
     */
    public void setInfoCredits(InfoCredits infoCredits) {
        this.infoCredits = infoCredits;
    }

    /**
     * @return Returns the infoExecution.
     */
    public InfoExecutionPeriod getInfoExecution() {
        return infoExecution;
    }

    /**
     * @param infoExecution
     *            The infoExecution to set.
     */
    public void setInfoExecution(InfoExecutionPeriod infoExecution) {
        this.infoExecution = infoExecution;
    }

    /**
     * @return Returns the teacherCategory.
     */
    public InfoCategory getCategory() {
        return category;
    }

    /**
     * @param teacherCategory
     *            The teacherCategory to set.
     */
    public void setCategory(InfoCategory teacherCategory) {
        this.category = teacherCategory;
    }

    /**
     * @return Returns the teacherId.
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId
     *            The teacherId to set.
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
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
     * @return Returns the teacherNumber.
     */
    public Integer getTeacherNumber() {
        return teacherNumber;
    }

    /**
     * @param teacherNumber
     *            The teacherNumber to set.
     */
    public void setTeacherNumber(Integer teacherNumber) {
        this.teacherNumber = teacherNumber;
    }
}