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
package org.fenixedu.academic.domain.student.scholarship.report;

import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.exceptions.DomainException;

import pt.ist.fenixframework.Atomic;

public class UTLScholarshipReport extends UTLScholarshipReport_Base {

    protected UTLScholarshipReport() {
        super();
    }

    protected UTLScholarshipReport(final UTLScholarshipSource source) {
        if (source == null) {
            throw new DomainException("error.UTLScholarshipReport.source.is.null");
        }

        setUtlScholarshipSource(source);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Atomic
    public static final UTLScholarshipReport launchQueueJob() {
        UTLScholarshipSource source = null;

        UTLScholarshipReport report = new UTLScholarshipReport(source);

        return report;
    }

}
