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
package org.fenixedu.academic.ui.struts.action.phd;

import java.io.InputStream;
import java.io.Serializable;

import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;
import org.fenixedu.academic.domain.phd.debts.PhdEvent;
import org.fenixedu.academic.domain.phd.debts.PhdEventExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;

public class PhdEventExemptionBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdEvent event;

    private PhdEventExemptionJustificationType justificationType;

    private Money value;

    private LocalDate dispatchDate;

    private String reason;

    private Party provider;

    private String fileName;

    private Long fileSize;

    transient private InputStream file;

    public PhdEventExemptionBean(final PhdEvent event) {
        setEvent(event);
    }

    public PhdEvent getEvent() {
        return event;
    }

    public void setEvent(PhdEvent event) {
        this.event = event;
    }

    public PhdEventExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(PhdEventExemptionJustificationType justificationType) {
        this.justificationType = justificationType;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Party getProvider() {
        return provider;
    }

    public void setProvider(Party provider) {
        this.provider = provider;
    }

    public PartySocialSecurityNumber getCreditorSocialSecurityNumber() {
        return (this.provider != null) ? this.provider.getPartySocialSecurityNumber() : null;
    }

    public void setCreditorSocialSecurityNumber(PartySocialSecurityNumber partySocialSecurityNumber) {
        this.provider = (partySocialSecurityNumber != null) ? partySocialSecurityNumber.getParty() : null;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

}
