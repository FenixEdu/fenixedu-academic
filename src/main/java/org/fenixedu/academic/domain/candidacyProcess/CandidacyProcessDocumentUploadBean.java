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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CandidacyProcessDocumentUploadBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    IndividualCandidacyProcess individualCandidacyProcess;
    protected IndividualCandidacyDocumentFileType type;
    protected transient InputStream stream;
    protected long fileSize;
    protected String fileName;

    protected Long id;

    protected IndividualCandidacyDocumentFile documentFile;

    public CandidacyProcessDocumentUploadBean() {
        this.id = System.currentTimeMillis();
    }

    public CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType type) {
        this.id = System.currentTimeMillis();
        this.type = type;
    }

    public IndividualCandidacyDocumentFileType getType() {
        return type;
    }

    public void setType(IndividualCandidacyDocumentFileType type) {
        this.type = type;
    }

    public InputStream getStream() throws FileNotFoundException {
        return this.stream;
    }

    public void setStream(InputStream stream) throws IOException {
        this.stream = stream;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public IndividualCandidacyProcess getIndividualCandidacyProcess() {
        return this.individualCandidacyProcess;
    }

    public void setIndividualCandidacyProcess(IndividualCandidacyProcess individualCandidacyProcess) {
        this.individualCandidacyProcess = individualCandidacyProcess;
    }

    public Long getId() {
        return this.id;
    }

    public IndividualCandidacyDocumentFile getDocumentFile() {
        return this.documentFile;
    }

    public void setDocumentFile(IndividualCandidacyDocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    protected static final int MAX_FILE_SIZE = 3698688;

    public IndividualCandidacyDocumentFile createIndividualCandidacyDocumentFile(Class<? extends Process> processType,
            String documentIdNumber) throws IOException {
        String fileName = this.getFileName();
        long fileLength = this.getFileSize();
        IndividualCandidacyDocumentFileType type = this.getType();

        if (fileLength > MAX_FILE_SIZE) {
            throw new DomainException("error.file.to.big");
        }

        byte[] contents = readStreamContents();

        if (contents == null) {
            return null;
        }

        return IndividualCandidacyDocumentFile.createCandidacyDocument(contents, fileName, type, processType.getSimpleName(),
                documentIdNumber);
    }

    protected byte[] readStreamContents() throws IOException {
        InputStream stream = this.getStream();
        long fileLength = this.getFileSize();

        if (stream == null || fileLength == 0) {
            return null;
        }

        if (fileLength > MAX_FILE_SIZE) {
            throw new DomainException("error.file.to.big");
        }

        byte[] contents = new byte[(int) fileLength];
        stream.read(contents);

        return contents;
    }

}
