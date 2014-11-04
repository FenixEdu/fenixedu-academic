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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ReportStudentsUTLCandidatesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear = null;
    private InputStream xlsFile = null;
    private String fileName;
    private Integer fileSize;
    private Boolean forFirstYear;
    private Integer studentNumber;

    public ReportStudentsUTLCandidatesBean() {
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public InputStream getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(InputStream xlsFile) {
        this.xlsFile = xlsFile;
    }

    public byte[] readXLSContents() throws IOException {
        byte[] contents = new byte[fileSize];
        xlsFile.read(contents);

        return contents;
    }

    public Boolean getForFirstYear() {
        return forFirstYear;
    }

    public void setForFirstYear(Boolean forFirstYear) {
        this.forFirstYear = forFirstYear;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

}
