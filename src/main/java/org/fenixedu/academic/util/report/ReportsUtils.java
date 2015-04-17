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
package org.fenixedu.academic.util.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.fenixedu.academic.util.report.ReportPrinter.ReportDescription;
import org.fenixedu.academic.util.report.ReportPrinter.ReportResult;

public class ReportsUtils {

    private static ReportPrinter printer = (reports) -> {
        throw new UnsupportedOperationException("Cannot print reports: " + Arrays.toString(reports));
    };

    public static void setPrinter(ReportPrinter printer) {
        ReportsUtils.printer = Objects.requireNonNull(printer);
    }

    public static ReportResult generateReport(final String key, final Map<String, Object> parameters,
            final Collection<?> dataSource) {
        try {
            return printer.printReport(key, parameters, dataSource);
        } catch (Exception e) {
            throw new RuntimeException("Exception while generating report '" + key + "'", e);
        }
    }

    public static ReportResult generateReport(final ReportDescription... reports) {
        try {
            return printer.printReports(reports);
        } catch (Exception e) {
            throw new RuntimeException("Exception while generating reports '" + Arrays.toString(reports) + "'", e);
        }
    }
}
