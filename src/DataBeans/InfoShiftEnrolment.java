/*
 * InfoShiftEnrolment.java
 *
 * Created on December 20th, 2002, 05:26
 */

package DataBeans;

import java.util.List;

/**
 * 
 * @author tfc130
 */

public class InfoShiftEnrolment extends InfoObject {
    protected List _infoEnrolmentWithShift;

    protected List _infoEnrolmentWithOutShift;

    public InfoShiftEnrolment() {
    }

    public InfoShiftEnrolment(List infoEnrolmentWithShift, List infoEnrolmentWithOutShift) {
        setInfoEnrolmentWithShift(infoEnrolmentWithShift);
        setInfoEnrolmentWithOutShift(infoEnrolmentWithOutShift);
    }

    public List getInfoEnrolmentWithShift() {
        return _infoEnrolmentWithShift;
    }

    public void setInfoEnrolmentWithShift(List infoEnrolmentWithShift) {
        _infoEnrolmentWithShift = infoEnrolmentWithShift;
    }

    public List getInfoEnrolmentWithOutShift() {
        return _infoEnrolmentWithOutShift;
    }

    public void setInfoEnrolmentWithOutShift(List infoEnrolmentWithOutShift) {
        _infoEnrolmentWithOutShift = infoEnrolmentWithOutShift;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoShiftEnrolment) {
            InfoShiftEnrolment infoShiftEnrolment = (InfoShiftEnrolment) obj;
            result = getInfoEnrolmentWithShift().containsAll(
                    infoShiftEnrolment.getInfoEnrolmentWithShift())
                    && getInfoEnrolmentWithOutShift().containsAll(
                            infoShiftEnrolment.getInfoEnrolmentWithOutShift());
        }
        return result;
    }

    public String toString() {
        String result = "[INFOSHIFTENROLMENT";
        result += "]";
        return result;
    }

}