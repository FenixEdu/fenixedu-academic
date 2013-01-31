package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class PhdCandidacyRefereeLetter extends PhdCandidacyRefereeLetter_Base {

	private PhdCandidacyRefereeLetter() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public PhdCandidacyRefereeLetter(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
		this();
		edit(referee, bean);
	}

	private void edit(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {

		check(referee, bean);

		setCandidacyReferee(referee);
		setPhdProgramCandidacyProcess(referee.getPhdProgramCandidacyProcess());

		setHowLongKnownApplicant(bean.getHowLongKnownApplicant());
		setCapacity(bean.getCapacity());
		setComparisonGroup(bean.getComparisonGroup());
		setRankInClass(bean.getRankInClass());

		setAcademicPerformance(bean.getAcademicPerformance());
		setSocialAndCommunicationSkills(bean.getSocialAndCommunicationSkills());
		setPotencialToExcelPhd(bean.getPotencialToExcelPhd());

		setComments(bean.getComments());
		if (bean.hasFileContent()) {
			if (hasFile()) {
				getFile().delete();
			}
			setFile(new PhdCandidacyRefereeLetterFile(getPhdProgramCandidacyProcess(), bean.getFilename(), bean.getFileContent()));
		}

		setRefereeName(bean.getRefereeName());
		setRefereePosition(bean.getRefereePosition());
		setRefereeInstitution(bean.getRefereeInstitution());
		setRefereeAddress(bean.getRefereeAddress());
		setRefereeCity(bean.getRefereeCity());
		setRefereeZipCode(bean.getRefereeZipCode());
		setRefereeCountry(bean.getRefereeCountry());
	}

	private void check(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
		check(referee, "error.PhdCandidacyRefereeLetter.invalid.referee");
		if (referee.hasLetter()) {
			throw new DomainException("error.PhdCandidacyRefereeLetter.referee.already.has.letter");
		}

		check(referee.getPhdProgramCandidacyProcess(), "error.PhdCandidacyRefereeLetter.invalid.process");

		check(bean.getHowLongKnownApplicant(), "error.PhdCandidacyRefereeLetter.invalid.howLongKnownApplicant");
		check(bean.getCapacity(), "error.PhdCandidacyRefereeLetter.invalid.capacity");
		check(bean.getComparisonGroup(), "error.PhdCandidacyRefereeLetter.invalid.comparisonGroup");

		check(bean.getAcademicPerformance(), "error.PhdCandidacyRefereeLetter.invalid.academicPerformance");
		check(bean.getSocialAndCommunicationSkills(), "error.PhdCandidacyRefereeLetter.invalid.socialAndCommunicationSkills");
		check(bean.getPotencialToExcelPhd(), "error.PhdCandidacyRefereeLetter.invalid.potencialToExcelPhd");

		check(bean.getRefereeName(), "error.PhdCandidacyRefereeLetter.invalid.refereeName");
		check(bean.getRefereeInstitution(), "error.PhdCandidacyRefereeLetter.invalid.refereeInstitution");
		check(bean.getRefereeAddress(), "error.PhdCandidacyRefereeLetter.invalid.refereeAddress");
		check(bean.getRefereeCity(), "error.PhdCandidacyRefereeLetter.invalid.refereeCity");
		check(bean.getRefereeZipCode(), "error.PhdCandidacyRefereeLetter.invalid.refereeZipCode");
		check(bean.getRefereeCountry(), "error.PhdCandidacyRefereeLetter.invalid.refereeCountry");
	}

	public String getRefereeEmail() {
		return getCandidacyReferee().getEmail();
	}

	public void delete() {
		if (hasFile()) {
			getFile().delete();
		}
		removeRefereeCountry();
		removeCandidacyReferee();
		removePhdProgramCandidacyProcess();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	@Service
	static public PhdCandidacyRefereeLetter create(PhdCandidacyRefereeLetterBean bean) {
		return new PhdCandidacyRefereeLetter(bean.getCandidacyReferee(), bean);
	}

}
