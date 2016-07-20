package org.fenixedu.academic.dto;

import java.io.InputStream;
import java.io.Serializable;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;

public class TransferDebtBean implements Serializable {

    private Party creditor;
    private String fileName;
    private Long fileSize;
    private String reason;
    transient private InputStream file;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Party getCreditor() {
        return creditor;
    }

    private Event event;

    public PartySocialSecurityNumber getCreditorSocialSecurityNumber() {
        return (this.creditor != null) ? this.creditor.getPartySocialSecurityNumber() : null;
    }

    public void setCreditorSocialSecurityNumber(PartySocialSecurityNumber partySocialSecurityNumber) {
        this.creditor = (partySocialSecurityNumber != null) ? partySocialSecurityNumber.getParty() : null;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}
