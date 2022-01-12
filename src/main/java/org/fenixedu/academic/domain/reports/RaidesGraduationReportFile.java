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
package org.fenixedu.academic.domain.reports;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaidesGraduationReportFile extends RaidesGraduationReportFile_Base {

    public interface RaidesGraduationReporProducer {

        public void produce(final Spreadsheet spreadsheet, final ExecutionYear executionYear, final DegreeType degreeType);

    }

    public static RaidesGraduationReporProducer PRODUCER = null;

    private static final Logger logger = LoggerFactory.getLogger(RaidesGraduationReportFile.class);

    public RaidesGraduationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem RAIDES - Graduação";
    }

    @Override
    protected String getPrefix() {
        return "graduationRAIDES";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
        final ExecutionYear executionYear = getExecutionYear();
        createHeaders(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().getName().getContent());

        if (PRODUCER != null) {
            PRODUCER.produce(spreadsheet, executionYear, getDegreeType());
        }

        logger.info("END report for " + getDegreeType().getName().getContent());
    }

    private void createHeaders(final Spreadsheet spreadsheet) {
        RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
    }

}
