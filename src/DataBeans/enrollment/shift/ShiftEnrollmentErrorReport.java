/*
 * Created on 13/Fev/2004
 */
package DataBeans.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import DataBeans.DataTranferObject;

/**
 * @author jmota
 *  
 */
public class ShiftEnrollmentErrorReport extends DataTranferObject {

    private List unExistingShifts;

    private List unAvailableShifts;

    /**
     *  
     */
    public ShiftEnrollmentErrorReport() {
        setUnAvailableShifts(new ArrayList());
        setUnExistingShifts(new ArrayList());
    }

    /**
     * @return Returns the unAvailableShifts.
     */
    public List getUnAvailableShifts() {
        return unAvailableShifts;
    }

    /**
     * @param unAvailableShifts
     *            The unAvailableShifts to set.
     */
    public void setUnAvailableShifts(List unAvailableShifts) {
        this.unAvailableShifts = unAvailableShifts;
    }

    /**
     * @return Returns the unExistingShifts.
     */
    public List getUnExistingShifts() {
        return unExistingShifts;
    }

    /**
     * @param unExistingShifts
     *            The unExistingShifts to set.
     */
    public void setUnExistingShifts(List unExistingShifts) {
        this.unExistingShifts = unExistingShifts;
    }
}