package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

public class ErasmusVacancyBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private UniversityUnit university;
	private Country country;
	private Degree degree;

	private Integer numberOfVacancies;

	private ErasmusVacancy vacancy;
	private MobilityQuota quota;

	private MobilityProgram mobilityProgram;

	public ErasmusVacancyBean() {

	}

	public ErasmusVacancyBean(MobilityQuota quota) {
		this.quota = quota;
	}

	public ErasmusVacancyBean(MobilityProgram program) {
		this.mobilityProgram = program;
	}

	public UniversityUnit getUniversity() {
		return university;
	}

	public void setUniversity(UniversityUnit university) {
		this.university = university;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Integer getNumberOfVacancies() {
		return numberOfVacancies;
	}

	public void setNumberOfVacancies(Integer numberOfVacancies) {
		this.numberOfVacancies = numberOfVacancies;
	}

	public MobilityQuota getQuota() {
		return this.quota;
	}

	public void setQuota(final MobilityQuota quota) {
		this.quota = quota;
	}

	public ErasmusVacancy getVacancy() {
		return vacancy;
	}

	public void setVacancy(ErasmusVacancy vacancy) {
		this.vacancy = vacancy;
	}

	public MobilityProgram getMobilityProgram() {
		return mobilityProgram;
	}

	public void setMobilityProgram(MobilityProgram mobilityProgram) {
		this.mobilityProgram = mobilityProgram;
	}

	public MobilityAgreement getMobilityAgreement() {
		if (mobilityProgram == null || university == null) {
			return null;
		}
		return mobilityProgram.getMobilityAgreementByUniversityUnit(university);
	}
}