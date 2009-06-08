package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringUtils;

public class PhdCandidacyRefereeLetter extends PhdCandidacyRefereeLetter_Base {

    private PhdCandidacyRefereeLetter() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public PhdCandidacyRefereeLetter(final PhdCandidacyReferee referee, final PhdProgramCandidacyProcess process,
	    final PhdCandidacyRefereeLetterBean bean) {
	this();

	check(referee, "error.PhdCandidacyRefereeLetter.invalid.referee");
	check(process, "error.PhdCandidacyRefereeLetter.invalid.process");

	check(bean.getOverallPromise(), "error.PhdCandidacyRefereeLetter.invalid.overallPromise");
	check(bean.getComparisonGroup(), "error.PhdCandidacyRefereeLetter.invalid.comparisonGroup");
	checkOr(bean.getRankValue(), bean.getRank(), "error.PhdCandidacyRefereeLetter.invalid.rank");

	setCandidacyReferee(referee);
	setPhdProgramCandidacyProcess(process);

	setOverallPromise(bean.getOverallPromise());
	setComparisonGroup(bean.getComparisonGroup());
	setRankValue(bean.getRankValue());
	setRank(bean.getRank());

	setComments(bean.getComments());
	if (bean.hasFileContent()) {
	    setFile(new PhdCandidacyRefereeLetterFile(process, bean.getFilename(), bean.getFileContent()));
	}

	setRefereeName(bean.getRefereeName());
	setRefereePosition(bean.getRefereePosition());
	setRefereeInstituition(bean.getRefereeInstituition());
	setRefereeAddress(bean.getRefereeAddress());
	setRefereeCity(bean.getRefereeCity());
	setRefereeZipCode(bean.getRefereeZipCode());
	setRefereeCountry(bean.getRefereeCountry());
	setRefereePhone(bean.getRefereePhone());
	setDate(bean.getDate());
    }

    private void checkOr(final String rankValue, final ApplicantRank rank, final String message) {
	if (StringUtils.isEmpty(rankValue) && rank == null) {
	    throw new DomainException(message);
	}
    }

    public String getRefereeEmail() {
	return getCandidacyReferee().getEmail();
    }

    public void delete() {
	getFile().delete();
	removeRefereeCountry();
	removeCandidacyReferee();
	removePhdProgramCandidacyProcess();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
