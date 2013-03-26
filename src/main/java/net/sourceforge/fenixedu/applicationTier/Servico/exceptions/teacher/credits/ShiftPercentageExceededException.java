/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.teacher.credits;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author jpvl
 */
public class ShiftPercentageExceededException extends FenixServiceException {

    private List shiftWithErrors;

    /**
     * @param shiftWithErrors
     *            List of InfoShifts...
     */
    public ShiftPercentageExceededException(List shiftWithErrors) {
        this.shiftWithErrors = shiftWithErrors;
    }

    /**
     * @return
     */
    public List getShiftWithErrors() {
        return shiftWithErrors;
    }

}