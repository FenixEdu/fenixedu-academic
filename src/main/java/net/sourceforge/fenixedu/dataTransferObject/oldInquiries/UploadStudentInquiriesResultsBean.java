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
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.InputStream;

import org.joda.time.LocalDate;

abstract public class UploadStudentInquiriesResultsBean {

    protected transient InputStream file;

    protected String keyExecutionCourseHeader;

    protected String keyExecutionDegreeHeader;

    protected LocalDate resultsDate;

    public UploadStudentInquiriesResultsBean() {
        super();
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getKeyExecutionCourseHeader() {
        return keyExecutionCourseHeader;
    }

    public void setKeyExecutionCourseHeader(String keyExecutionCourseHeader) {
        this.keyExecutionCourseHeader = keyExecutionCourseHeader;
    }

    public String getKeyExecutionDegreeHeader() {
        return keyExecutionDegreeHeader;
    }

    public void setKeyExecutionDegreeHeader(String keyExecutionDegreeHeader) {
        this.keyExecutionDegreeHeader = keyExecutionDegreeHeader;
    }

    public LocalDate getResultsDate() {
        return resultsDate;
    }

    public void setResultsDate(LocalDate resultsDate) {
        this.resultsDate = resultsDate;
    }
}