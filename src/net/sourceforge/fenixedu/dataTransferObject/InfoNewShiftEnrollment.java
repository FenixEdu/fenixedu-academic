/**
* Aug 4, 2005
*/
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author Ricardo Rodrigues
 *
 */

public class InfoNewShiftEnrollment extends DataTranferObject {
    
    private InfoExecutionCourse infoExecutionCourse;
    private ShiftType theoricType;
    private ShiftType praticType;
    private ShiftType laboratoryType;
    private ShiftType theoricoPraticType;
    
    private InfoShift theoricShift;
    private InfoShift praticShift;
    private InfoShift laboratoryShift;
    private InfoShift theoricoPraticShift;
    
    private boolean enrolled;
    
    public Boolean getEnrolled() {
        return enrolled;
    }
    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public InfoShift getLaboratoryShift() {
        return laboratoryShift;
    }
    public void setLaboratoryShift(InfoShift laboratoryShift) {
        this.laboratoryShift = laboratoryShift;
    }

    public InfoShift getPraticShift() {
        return praticShift;
    }
    public void setPraticShift(InfoShift praticShift) {
        this.praticShift = praticShift;
    }

    public InfoShift getTheoricoPraticShift() {
        return theoricoPraticShift;
    }
    public void setTheoricoPraticShift(InfoShift theoricoPraticShift) {
        this.theoricoPraticShift = theoricoPraticShift;
    }
    public InfoShift getTheoricShift() {
        return theoricShift;
    }
    public void setTheoricShift(InfoShift theoricShift) {
        this.theoricShift = theoricShift;
    }
    public ShiftType getLaboratoryType() {
        return laboratoryType;
    }
    public void setLaboratoryType(ShiftType laboratoryType) {
        this.laboratoryType = laboratoryType;
    }
    public ShiftType getPraticType() {
        return praticType;
    }
    public void setPraticType(ShiftType praticType) {
        this.praticType = praticType;
    }
    public ShiftType getTheoricoPraticType() {
        return theoricoPraticType;
    }
    public void setTheoricoPraticType(ShiftType theoricoPraticType) {
        this.theoricoPraticType = theoricoPraticType;
    }
    public ShiftType getTheoricType() {
        return theoricType;
    }
    public void setTheoricType(ShiftType theoricType) {
        this.theoricType = theoricType;
    }
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }
    
}


