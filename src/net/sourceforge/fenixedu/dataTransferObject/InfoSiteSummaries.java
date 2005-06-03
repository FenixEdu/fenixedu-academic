/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author João Mota modified by Tânia Pousão 21/Jul/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteSummaries extends DataTranferObject implements ISiteComponent {

    private List infoSummaries;

    private InfoExecutionCourse executionCourse;

    private InfoSite infoSite;

    //usefull for show summaries use case in public site
    private ShiftType shiftType;

    private Integer shiftId;

    private Integer teacherId;

    //usefull for use cases about summaries
    private List lessonTypes;

    private List infoShifts;

    private List infoProfessorships;

    private List infoRooms;

    /**
     * @return
     */
    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @param infoExecutionCourse
     */
    public void setExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.executionCourse = infoExecutionCourse;
    }

    /**
     *  
     */
    public InfoSiteSummaries() {

    }

    /**
     * @return
     */
    public List getInfoSummaries() {
        return infoSummaries;
    }

    /**
     * @param infoSummaries
     */
    public void setInfoSummaries(List infoSummaries) {
        this.infoSummaries = infoSummaries;
    }

    /**
     * @return
     */
    public InfoSite getInfoSite() {
        return infoSite;
    }

    /**
     * @param infoSite
     */
    public void setInfoSite(InfoSite infoSite) {
        this.infoSite = infoSite;
    }

    /**
     * @return Returns the shiftId.
     */
    public Integer getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId
     *            The shiftId to set.
     */
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
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
     * @return Returns the infoShifts.
     */
    public List getInfoShifts() {
        return infoShifts;
    }

    /**
     * @param infoShifts
     *            The infoShifts to set.
     */
    public void setInfoShifts(List infoShifts) {
        this.infoShifts = infoShifts;
    }

    /**
     * @return Returns the infoProfessorship.
     */
    public List getInfoProfessorships() {
        return infoProfessorships;
    }

    /**
     * @param infoProfessorship
     *            The infoProfessorship to set.
     */
    public void setInfoProfessorships(List infoProfessorships) {
        this.infoProfessorships = infoProfessorships;
    }

    /**
     * @return Returns the lessonTypes.
     */
    public List getLessonTypes() {
        return lessonTypes;
    }

    /**
     * @param lessonTypes
     *            The lessonTypes to set.
     */
    public void setLessonTypes(List lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

    /**
     * @return Returns the infoRooms.
     */
    public List getInfoRooms() {
        return infoRooms;
    }

    /**
     * @param infoRooms
     *            The infoRooms to set.
     */
    public void setInfoRooms(List infoRooms) {
        this.infoRooms = infoRooms;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteSummaries) {
            InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) obj;
            result = getExecutionCourse().equals(infoSiteSummaries.getExecutionCourse())
                    && getInfoSummaries().containsAll(infoSiteSummaries.getInfoSummaries())
                    && infoSiteSummaries.getInfoSummaries().containsAll(getInfoSummaries());
        }

        return result;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }
    

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }
    
}