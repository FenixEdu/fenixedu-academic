/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdProgramContextPeriodBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PhdProgram phdProgram;
    private LocalDate beginDate;
    private LocalDate endDate;

    public PhdProgramContextPeriodBean(final PhdProgram phdProgram) {
        setPhdProgram(phdProgram);
    }

    public PhdProgram getPhdProgram() {
        return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DateTime getBeginDateAtMidnight() {
        return getBeginDate().toDateMidnight().toDateTime();
    }

    public DateTime getEndDateBeforeMidnight() {
        if (getEndDate() == null) {
            return null;
        }

        return getEndDate().plusDays(1).toDateMidnight().toDateTime().minusSeconds(1);
    }
}
