package net.sourceforge.fenixedu.dataTransferObject.contacts;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation;

public class PhysicalAddressValidationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private Long fileSize;
    private String mimeType;
    private InputStream stream;
    private String description;
    private PhysicalAddressValidation validation;
    private PartyContactValidationState validationState;

    public PhysicalAddressValidationBean(PhysicalAddressValidation partyContactValidation) {
	this.validation = partyContactValidation;
	this.validationState = partyContactValidation.getState();
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Long getFileSize() {
	return fileSize;
    }

    public void setFileSize(Long fileSize) {
	this.fileSize = fileSize;
    }

    public String getMimeType() {
	return mimeType;
    }

    public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }

    public InputStream getStream() {
	return stream;
    }

    public void setStream(InputStream stream) {
	this.stream = stream;
    }

    public PartyContactValidationState getValidationState() {
	return validationState;
    }

    public void setValidationState(PartyContactValidationState validationState) {
	this.validationState = validationState;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public PhysicalAddressValidation getValidation() {
	return validation;
    }

    public void setValidation(PhysicalAddressValidation validation) {
	this.validation = validation;
    }

    public byte[] readStream() throws IOException {
	byte[] data = new byte[getFileSize().intValue()];
	stream.read(data);
	stream.close();
	return data;
    }
}
