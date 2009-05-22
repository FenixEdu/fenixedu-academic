package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

public class PhdIndividualProgramProcessBean implements Serializable {

    static private final long serialVersionUID = 909403079500457245L;

    private String thesisTitle;
    private PhdIndividualProgramCollaborationType collaborationType;
    private String otherCollaborationType;

    public PhdIndividualProgramProcessBean() {
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

}
