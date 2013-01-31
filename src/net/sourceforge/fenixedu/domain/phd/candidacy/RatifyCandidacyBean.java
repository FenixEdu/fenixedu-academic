package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.LocalDate;

public class RatifyCandidacyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_MAX_DAYS_TO_FORMALIZE_REGISTRATION = 20;

	private LocalDate whenRatified;

	private PhdProgramDocumentUploadBean ratificationFile;

	private PhdProgramCandidacyProcess process;

	private int maxDaysToFormalizeRegistration = DEFAULT_MAX_DAYS_TO_FORMALIZE_REGISTRATION;

	public RatifyCandidacyBean(PhdProgramCandidacyProcess process) {
		setProcess(process);
		this.ratificationFile = new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CANDIDACY_RATIFICATION);
	}

	public PhdProgramCandidacyProcess getProcess() {
		return this.process;
	}

	public void setProcess(PhdProgramCandidacyProcess process) {
		this.process = process;
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

	public int getMaxDaysToFormalizeRegistration() {
		return maxDaysToFormalizeRegistration;
	}

	public void setMaxDaysToFormalizeRegistration(int maxDaysToFormalizeRegistration) {
		this.maxDaysToFormalizeRegistration = maxDaysToFormalizeRegistration;
	}

}
