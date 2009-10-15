package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.LocalDate;

public class RatifyCandidacyBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private LocalDate whenRatified;

    private PhdProgramDocumentUploadBean ratificationFile;

    private DomainReference<PhdProgramCandidacyProcess> process;

    public RatifyCandidacyBean(PhdProgramCandidacyProcess process) {

	setProcess(process);

	this.ratificationFile = new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CANDIDACY_RATIFICATION);

    }

    public PhdProgramCandidacyProcess getProcess() {
	return (this.process != null) ? this.process.getObject() : null;
    }

    public void setProcess(PhdProgramCandidacyProcess process) {
	this.process = (process != null) ? new DomainReference<PhdProgramCandidacyProcess>(process) : null;
    }

    public LocalDate getWhenRatified() {
	return whenRatified;
    }

    public void setWhenRatified(LocalDate whenRatified) {
	this.whenRatified = whenRatified;
    }

    public PhdProgramDocumentUploadBean getRatificationFile() {
	return ratificationFile;
    }

    public void setRatificationFile(PhdProgramDocumentUploadBean ratificationFile) {
	this.ratificationFile = ratificationFile;
    }

}
