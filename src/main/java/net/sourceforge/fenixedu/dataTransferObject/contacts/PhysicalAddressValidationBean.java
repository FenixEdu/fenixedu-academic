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
