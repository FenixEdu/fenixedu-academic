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

import java.util.Collection;
import java.util.Map;

public interface ReportPrinter {

    ReportResult printReports(ReportDescription... reports) throws Exception;

    default ReportResult printReport(String key, Map<String, Object> parameters, Collection<?> dataSource)
            throws Exception {
        return printReports(new ReportDescription() {

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Map<String, Object> getParameters() {
                return parameters;
            }

            @Override
            public Collection<?> getDataSource() {
                return dataSource;
            }

        });
    }

    interface ReportDescription {

        String getKey();

        Map<String, Object> getParameters();

        Collection<?> getDataSource();
    }

    class ReportResult {

        private final byte[] data;
        private final String contentType;
        private final String fileExtension;

        public ReportResult(byte[] data, String contentType, String fileExtension) {
            this.data = data;
            this.contentType = contentType;
            this.fileExtension = fileExtension;
        }

        public byte[] getData() {
            return data;
        }

        public String getContentType() {
            return contentType;
        }

        public String getFileExtension() {
            return fileExtension;
        }

    }

}
