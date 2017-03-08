/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import java.util.Date;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.util.PeriodState;

/**
 * @author Nuno &amp; Joana
 */
public class InfoExecutionYear extends InfoObject {

    private final ExecutionYear executionYearDomainReference;

    public InfoExecutionYear(final ExecutionYear executionYear) {
        executionYearDomainReference = executionYear;
    }

    public ExecutionYear getExecutionYear() {
        return executionYearDomainReference;
    }

    public String getYear() {
        return getExecutionYear().getYear();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionYear) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) obj;
            return getYear().equals(infoExecutionYear.getYear());
        }
        return false;
    }

    @Override
    public String toString() {
        return getExecutionYear().getYear();
    }

    public PeriodState getState() {
        return getExecutionYear().getState();
    }

    public int compareTo(Object arg0) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;
        return this.getYear().compareTo(infoExecutionYear.getYear());
    }

    public Date getBeginDate() {
        return getExecutionYear().getBeginDate();
    }

    public Date getEndDate() {
        return getExecutionYear().getEndDate();
    }

    public static InfoExecutionYear newInfoFromDomain(final ExecutionYear executionYear) {
        return executionYear == null ? null : new InfoExecutionYear(executionYear);
    }

    public String getNextExecutionYearYear() {
        return getExecutionYear().getNextYearsYearString();
    }

    public boolean after(InfoExecutionYear infoExecutionYear) {
        return getBeginDate().after(infoExecutionYear.getEndDate());
    }

    public InfoExecutionYear getNextInfoExecutionYear() {
        final ExecutionYear nextExecutionYear = getExecutionYear().getNextExecutionYear();
        return nextExecutionYear == null ? null : InfoExecutionYear.newInfoFromDomain(nextExecutionYear);
    }

    @Override
    public String getExternalId() {
        return getExecutionYear().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}