/*
 * Created on Nov 18, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.enrollment;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;

/**
 * @author nmgo
 */
public class InfoImprovmentEnrolmentContext extends DataTranferObject {
    
    InfoExecutionPeriod infoExecutionPeriod;
    
    InfoStudent infoStudent;
    
    List improvmentsToEnroll;
    
    List alreadyEnrolled;
    
    /**
     * @return Returns the improvmentsToEnroll.
     */
    public List getImprovmentsToEnroll() {
        return improvmentsToEnroll;
    }
    /**
     * @param improvmentsToEnroll The improvmentsToEnroll to set.
     */
    public void setImprovmentsToEnroll(List improvmentsToEnroll) {
        this.improvmentsToEnroll = improvmentsToEnroll;
    }
    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }
    /**
     * @param infoExecutionPeriod The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }
    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }
    /**
     * @param infoStudent The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }
    /**
     * @return Returns the alreadyEnrolled.
     */
    public List getAlreadyEnrolled() {
        return alreadyEnrolled;
    }
    
    /**
     * @param alreadyEnrolled The alreadyEnrolled to set.
     */
    public void setAlreadyEnrolled(List alreadyEnrolled) {
        this.alreadyEnrolled = alreadyEnrolled;
    }
}
