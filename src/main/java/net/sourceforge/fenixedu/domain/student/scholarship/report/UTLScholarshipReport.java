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
package net.sourceforge.fenixedu.domain.student.scholarship.report;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Student> getStudent() {
        return getStudentSet();
    }

    @Deprecated
    public boolean hasAnyStudent() {
        return !getStudentSet().isEmpty();
    }

    @Deprecated
    public boolean hasUtlScholarshipSource() {
        return getUtlScholarshipSource() != null;
    }

}
