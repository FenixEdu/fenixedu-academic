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
package net.sourceforge.fenixedu.domain.phd.seminar;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.LocalDate;

public class PublicPresentationSeminarProcessBean implements Serializable {

    private static final long serialVersionUID = -7837778662130742070L;

    private String remarks;

    private PhdProgramDocumentUploadBean document;

    private LocalDate presentationDate;

    private Boolean generateAlert;

    private PublicPresentationSeminarProcess process;

    private PublicPresentationSeminarProcessStateType processState;

    private LocalDate stateDate;

    private LocalDate presentationRequestDate;

    private PhdIndividualProgramProcess phdIndividualProgramProcess;

    public PublicPresentationSeminarProcessBean() {
        this.document = new PhdProgramDocumentUploadBean();
    }

    public PublicPresentationSeminarProcessBean(final PhdIndividualProgramProcess process) {
        this();
        setProcess(process.getSeminarProcess());
        setGenerateAlert(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());

        setPresentationDate(process.getSeminarProcess() != null ? process.getSeminarProcess().getPresentationDate() : null);
        setPresentationRequestDate(process.getSeminarProcess() != null ? process.getSeminarProcess().getPresentationRequestDate() : null);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public PhdProgramDocumentUploadBean getDocument() {
        return document;
    }

    public void setDocument(PhdProgramDocumentUploadBean document) {
        this.document = document;
    }

    public LocalDate getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(LocalDate presentationDate) {
        this.presentationDate = presentationDate;
    }

    public Boolean getGenerateAlert() {
        return generateAlert;
    }

    public void setGenerateAlert(Boolean generateAlert) {
        this.generateAlert = generateAlert;
    }

    public PublicPresentationSeminarProcess getProcess() {
        return process;
    }

    public void setProcess(PublicPresentationSeminarProcess process) {
        this.process = process;
    }

    public PublicPresentationSeminarProcessStateType getProcessState() {
        return processState;
    }

    public void setProcessState(PublicPresentationSeminarProcessStateType processState) {
        this.processState = processState;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public LocalDate getPresentationRequestDate() {
        return presentationRequestDate;
    }

    public void setPresentationRequestDate(LocalDate presentationRequestDate) {
        this.presentationRequestDate = presentationRequestDate;
    }

    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return phdIndividualProgramProcess;
    }

    public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        this.phdIndividualProgramProcess = phdIndividualProgramProcess;
    }
}
