package net.sourceforge.fenixedu.domain.phd;

import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;

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
    private PhdIndividualProgramCollaborationType collaborationType;
    private String otherCollaborationType;

    private DomainReference<PhdIndividualProgramProcess> individualProgramProcess;
    private DomainReference<PhdProgram> phdProgram;
    private DomainReference<PhdProgramFocusArea> focusArea;

    private QualificationExamsResult qualificationExamsRequired;
    private QualificationExamsResult qualificationExamsPerformed;

    public PhdIndividualProgramProcessBean() {
	setQualificationExamsRequired(QualificationExamsResult.NULL);
	setQualificationExamsPerformed(QualificationExamsResult.NULL);
    }

    public PhdIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
	setCandidacyDate(process.getCandidacyDate());

	setPhdProgram(process.getPhdProgram());
	setFocusArea(process.getPhdProgramFocusArea());

	setThesisTitle(process.getThesisTitle());
	setCollaborationType(process.getCollaborationType());
	setOtherCollaborationType(process.getOtherCollaborationType());

	setIndividualProgramProcess(process);

	setQualificationExamsRequired(QualificationExamsResult.fromValue(process.getQualificationExamsRequired()));
	setQualificationExamsPerformed(QualificationExamsResult.fromValue(process.getQualificationExamsPerformed()));
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
	return (this.individualProgramProcess != null) ? this.individualProgramProcess.getObject() : null;
    }

    public void setIndividualProgramProcess(final PhdIndividualProgramProcess individualProgramProcess) {
	this.individualProgramProcess = (individualProgramProcess != null) ? new DomainReference<PhdIndividualProgramProcess>(
		individualProgramProcess) : null;
    }

    public PhdProgram getPhdProgram() {
	return (this.phdProgram != null) ? this.phdProgram.getObject() : null;
    }

    public void setPhdProgram(final PhdProgram phdProgram) {
	this.phdProgram = (phdProgram != null) ? new DomainReference<PhdProgram>(phdProgram) : null;
    }

    public boolean hasPhdProgram() {
	return getPhdProgram() != null;
    }

    public PhdProgramFocusArea getFocusArea() {
	return (this.focusArea != null) ? this.focusArea.getObject() : null;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
	this.focusArea = (focusArea != null) ? new DomainReference<PhdProgramFocusArea>(focusArea) : null;
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

}
