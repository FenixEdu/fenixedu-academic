/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteSummary extends DataTranferObject implements ISiteComponent {

    private InfoSummary infoSummary;

    private InfoExecutionCourse executionCourse;

    private List infoShifts;

    private List infoProfessorships;

    private List infoRooms;

    /**
     * @return
     */
    public InfoSummary getInfoSummary() {
        return infoSummary;
    }

    /**
     * @param infoSummary
     */
    public void setInfoSummary(InfoSummary infoSummary) {
        this.infoSummary = infoSummary;
    }

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
     * @return Returns the infoProfessorships.
     */
    public List getInfoProfessorships() {
        return infoProfessorships;
    }

    /**
     * @param infoProfessorships
     *            The infoProfessorships to set.
     */
    public void setInfoProfessorships(List infoProfessorships) {
        this.infoProfessorships = infoProfessorships;
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

    /**
     *  
     */
    public InfoSiteSummary() {

    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteSummary) {
            InfoSiteSummary infoSiteSummary = (InfoSiteSummary) obj;
            result = getExecutionCourse().equals(infoSiteSummary.getExecutionCourse())
                    && getInfoSummary().equals(infoSiteSummary.getInfoSummary());
        }
        return result;
    }
}