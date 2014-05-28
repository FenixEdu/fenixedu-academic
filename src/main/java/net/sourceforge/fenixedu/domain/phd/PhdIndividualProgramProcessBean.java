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
package net.sourceforge.fenixedu.domain.phd;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdThesisSubjectOrderBean;

import org.joda.time.LocalDate;

public class PhdIndividualProgramProcessBean implements Serializable {

    public static enum QualificationExamsResult {
        NULL(null),

        YES(Boolean.TRUE),

        NO(Boolean.FALSE);

        private Boolean value;

        private QualificationExamsResult(Boolean value) {
            this.value = value;
        }

        public Boolean getValue() {
            return value;
        }

        static public QualificationExamsResult fromValue(Boolean value) {
            if (value == null) {
                return QualificationExamsResult.NULL;
            } else if (value == Boolean.TRUE) {
                return QualificationExamsResult.YES;
            } else {
                return QualificationExamsResult.NO;
            }

        }

    }

    static private final long serialVersionUID = 909403079500457245L;

    private LocalDate candidacyDate;
    private String thesisTitle;
    private String thesisTitleEn;
    private PhdIndividualProgramCollaborationType collaborationType;
    private String otherCollaborationType;

    private final List<PhdThesisSubjectOrderBean> thesisSubjectBeans;

    private PhdIndividualProgramProcess individualProgramProcess;
    private PhdProgram phdProgram;
    private PhdProgramFocusArea focusArea;
    private ExternalPhdProgram externalPhdProgram;

    private QualificationExamsResult qualificationExamsRequired;
    private QualificationExamsResult qualificationExamsPerformed;

    private PhdIndividualProgramProcessState processState;

    private LocalDate whenRatified;

    private LocalDate whenFormalizedRegistration;

    private LocalDate whenStartedStudies;

    private Integer phdStudentNumber;

    private PhdIndividualProgramProcess destiny;

    private String remarks;

    private LocalDate stateDate;

    public PhdIndividualProgramProcessBean() {
        thesisSubjectBeans = new ArrayList<PhdThesisSubjectOrderBean>();
        setQualificationExamsRequired(QualificationExamsResult.NULL);
        setQualificationExamsPerformed(QualificationExamsResult.NULL);
    }

    public PhdIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
        thesisSubjectBeans = new ArrayList<PhdThesisSubjectOrderBean>();
        setCandidacyDate(process.getCandidacyDate());

        setPhdProgram(process.getPhdProgram());
        setFocusArea(process.getPhdProgramFocusArea());
        setExternalPhdProgram(process.getExternalPhdProgram());

        for (ThesisSubjectOrder subjectOrder : process.getThesisSubjectOrders()) {
            addThesisSubjectBean(new PhdThesisSubjectOrderBean(subjectOrder.getSubjectOrder(), subjectOrder.getThesisSubject()));
        }
        sortThesisSubjectBeans();

        setThesisTitle(process.getThesisTitle());
        setThesisTitleEn(process.getThesisTitleEn());
        setCollaborationType(process.getCollaborationType());
        setOtherCollaborationType(process.getOtherCollaborationType());

        setIndividualProgramProcess(process);

        setQualificationExamsRequired(QualificationExamsResult.fromValue(process.getQualificationExamsRequired()));
        setQualificationExamsPerformed(QualificationExamsResult.fromValue(process.getQualificationExamsPerformed()));

        setWhenRatified(process.getCandidacyProcess().getWhenRatified());
        setWhenFormalizedRegistration(process.getWhenFormalizedRegistration());
        setWhenStartedStudies(process.getWhenStartedStudies());

        setPhdStudentNumber(process.getPhdIndividualProcessNumber().getPhdStudentNumber());
    }

    public List<PhdThesisSubjectOrderBean> getThesisSubjectBeans() {
        return thesisSubjectBeans;
    }

    public void addThesisSubjectBean(PhdThesisSubjectOrderBean thesisSubjectBean) {
        thesisSubjectBeans.add(thesisSubjectBean);
        sortThesisSubjectBeans();
    }

    public PhdThesisSubjectOrderBean getThesisSubjectBean(int order) {
        for (PhdThesisSubjectOrderBean bean : getThesisSubjectBeans()) {
            if (bean.getOrder() == order) {
                return bean;
            }
        }

        return null;
    }

    public void sortThesisSubjectBeans() {
        Collections.sort(thesisSubjectBeans, PhdThesisSubjectOrderBean.COMPARATOR_BY_ORDER);
    }

    public void updateThesisSubjectBeans() {
        int order = 1;
        getThesisSubjectBeans().clear();
        if (hasFocusArea()) {
            for (ThesisSubject thesisSubject : getFocusArea().getThesisSubjects()) {
                addThesisSubjectBean(new PhdThesisSubjectOrderBean(order++, thesisSubject));
            }
        }
    }

    public LocalDate getCandidacyDate() {
        return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
        this.candidacyDate = candidacyDate;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public String getThesisTitleEn() {
        return thesisTitleEn;
    }

    public void setThesisTitleEn(String thesisTitleEn) {
        this.thesisTitleEn = thesisTitleEn;
    }

    public PhdIndividualProgramCollaborationType getCollaborationType() {
        return collaborationType;
    }

    public void setCollaborationType(PhdIndividualProgramCollaborationType collaborationType) {
        this.collaborationType = collaborationType;
    }

    public String getOtherCollaborationType() {
        return otherCollaborationType;
    }

    public void setOtherCollaborationType(String otherCollaborationType) {
        this.otherCollaborationType = otherCollaborationType;
    }

    public boolean isCollaborationInformationCorrect() {
        return getCollaborationType().needExtraInformation() ? !isEmpty(otherCollaborationType) : true;
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
        return this.individualProgramProcess;
    }

    public void setIndividualProgramProcess(final PhdIndividualProgramProcess individualProgramProcess) {
        this.individualProgramProcess = individualProgramProcess;
    }

    public PhdProgram getPhdProgram() {
        return this.phdProgram;
    }

    public void setPhdProgram(final PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

    public PhdProgramFocusArea getFocusArea() {
        return this.focusArea;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
        this.focusArea = focusArea;
    }

    public boolean hasFocusArea() {
        return getFocusArea() != null;
    }

    public QualificationExamsResult getQualificationExamsRequired() {
        return qualificationExamsRequired;
    }

    public Boolean getQualificationExamsRequiredBooleanValue() {
        return qualificationExamsRequired.getValue();
    }

    public void setQualificationExamsRequired(QualificationExamsResult qualificationExamsRequired) {
        this.qualificationExamsRequired = qualificationExamsRequired;
    }

    public QualificationExamsResult getQualificationExamsPerformed() {
        return qualificationExamsPerformed;
    }

    public Boolean getQualificationExamsPerformedBooleanValue() {
        return qualificationExamsPerformed.getValue();
    }

    public void setQualificationExamsPerformed(QualificationExamsResult qualificationExamsPerformed) {
        this.qualificationExamsPerformed = qualificationExamsPerformed;
    }

    public PhdIndividualProgramProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(PhdIndividualProgramProcessState processState) {
        this.processState = processState;
    }

    public ExternalPhdProgram getExternalPhdProgram() {
        return this.externalPhdProgram;
    }

    public void setExternalPhdProgram(final ExternalPhdProgram externalPhdProgram) {
        this.externalPhdProgram = externalPhdProgram;
    }

    public LocalDate getWhenRatified() {
        return whenRatified;
    }

    public void setWhenRatified(LocalDate whenRatified) {
        this.whenRatified = whenRatified;
    }

    public LocalDate getWhenFormalizedRegistration() {
        return whenFormalizedRegistration;
    }

    public void setWhenFormalizedRegistration(LocalDate whenFormalizedRegistration) {
        this.whenFormalizedRegistration = whenFormalizedRegistration;
    }

    public LocalDate getWhenStartedStudies() {
        return whenStartedStudies;
    }

    public void setWhenStartedStudies(LocalDate whenStartedStudies) {
        this.whenStartedStudies = whenStartedStudies;
    }

    public Integer getPhdStudentNumber() {
        return phdStudentNumber;
    }

    public void setPhdStudentNumber(Integer phdStudentNumber) {
        this.phdStudentNumber = phdStudentNumber;
    }

    public PhdIndividualProgramProcess getDestiny() {
        return destiny;
    }

    public void setDestiny(PhdIndividualProgramProcess destiny) {
        this.destiny = destiny;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }
}
