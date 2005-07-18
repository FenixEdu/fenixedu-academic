/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class DistributedTest extends DistributedTest_Base {

    private Calendar beginDate;

    private Calendar endDate;

    private Calendar beginHour;

    private Calendar endHour;

    private TestType testType;

    private CorrectionAvailability correctionAvailability;

    public Calendar getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Calendar beginDate) {
        this.beginDate = beginDate;
    }

    public Calendar getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(Calendar beginHour) {
        this.beginHour = beginHour;
    }

    public CorrectionAvailability getCorrectionAvailability() {
        return correctionAvailability;
    }

    public void setCorrectionAvailability(CorrectionAvailability correctionAvailability) {
        this.correctionAvailability = correctionAvailability;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getEndHour() {
        return endHour;
    }

    public void setEndHour(Calendar endHour) {
        this.endHour = endHour;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

}
