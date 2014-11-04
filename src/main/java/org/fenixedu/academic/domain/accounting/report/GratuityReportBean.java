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
package org.fenixedu.academic.domain.accounting.report;

import org.fenixedu.academic.domain.ExecutionYear;
import org.joda.time.DateTime;

public class GratuityReportBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear;

    private GratuityReportQueueJobType type;

    private DateTime beginDate;
    private DateTime endDate;

    public GratuityReportBean(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public GratuityReportQueueJobType getType() {
        return type;
    }

    public void setType(GratuityReportQueueJobType type) {
        this.type = type;
    }
}