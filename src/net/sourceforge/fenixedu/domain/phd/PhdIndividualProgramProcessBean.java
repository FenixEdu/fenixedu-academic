package net.sourceforge.fenixedu.domain.phd;

import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class PhdIndividualProgramProcessBean implements Serializable {

    static private final long serialVersionUID = 909403079500457245L;

    private LocalDate candidacyDate;
    private String thesisTitle;
    private PhdIndividualProgramCollaborationType collaborationType;
    private String otherCollaborationType;

    public PhdIndividualProgramProcessBean() {
    }

    public PhdIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
	setCandidacyDate(process.getCandidacyDate());
	setThesisTitle(process.getThesisTitle());
	setCollaborationType(process.getCollaborationType());
	setOtherCollaborationType(process.getOtherCollaborationType());
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

    public boolean isOtherCollaborationTypeSelected() {
	return getCollaborationType().equals(PhdIndividualProgramCollaborationType.OTHER);
    }

    public boolean isCollaborationInformationCorrect() {
	return isOtherCollaborationTypeSelected() ? !isEmpty(otherCollaborationType) : true;
    }
}
