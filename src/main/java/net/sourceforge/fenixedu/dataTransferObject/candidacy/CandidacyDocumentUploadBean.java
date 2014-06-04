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
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocumentFile;
import net.sourceforge.fenixedu.util.Bundle;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CandidacyDocumentUploadBean implements Serializable {

    private transient InputStream inputStream;

    private File temporaryFile;

    private String filename;

    private String documentDescription;

    private String actualFile;

    private CandidacyDocument candidacyDocument;

    public CandidacyDocumentUploadBean(CandidacyDocument candidacyDocument) {
        super();
        CandidacyDocumentFile file = candidacyDocument.getFile();
        this.documentDescription = candidacyDocument.getDocumentDescription();
        this.actualFile = (file != null) ? file.getFilename() + " - " + file.getUploadTime().toString("dd/MM/yyyy hh:mm") : null;
        this.candidacyDocument = candidacyDocument;
    }

    public String getActualFile() {
        return (actualFile == null) ? new LabelFormatter().appendLabel("label.file.not.uploaded.yet",
                Bundle.CANDIDATE).toString() : actualFile;
    }

    public CandidacyDocument getCandidacyDocument() {
        return candidacyDocument;
    }

    public InputStream getFileInputStream() {
        return inputStream;
    }

    public void setFileInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDocumentDescription() {
        return new LabelFormatter().appendLabel("label." + documentDescription, Bundle.CANDIDATE).toString();
    }

    public boolean getIsFileUploaded() {
        return actualFile != null;
    }

    public void createTemporaryFile() {
        try {
            temporaryFile = inputStream != null ? FileUtils.copyToTemporaryFile(inputStream) : null;
        } catch (IOException exception) {
            temporaryFile = null;
        }

    }

    public File getTemporaryFile() {
        return temporaryFile;
    }

    public void deleteTemporaryFile() {
        temporaryFile.delete();
    }
}
