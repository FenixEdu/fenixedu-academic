package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class PhdCandidacyDocumentUploadBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PhdIndividualProgramDocumentType type;

    private transient FileInputStream file;

    private String filename;

    public byte[] getFileContent() {
	final ByteArrayOutputStream result = new ByteArrayOutputStream();
	try {
	    FileUtils.copy(this.file, result);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

	return result.toByteArray();

    }

    public PhdIndividualProgramDocumentType getType() {
	return type;
    }

    public void setType(PhdIndividualProgramDocumentType type) {
	this.type = type;
    }

    public FileInputStream getFile() {
	return file;
    }

    public void setFile(FileInputStream file) {
	this.file = file;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public boolean isValid() {
	return this.type != null && !StringUtils.isEmpty(this.filename) && this.file != null;
    }
}
