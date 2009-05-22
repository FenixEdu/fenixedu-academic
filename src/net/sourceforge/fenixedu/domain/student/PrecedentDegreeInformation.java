package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;

public class PrecedentDegreeInformation extends PrecedentDegreeInformation_Base {

    public PrecedentDegreeInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public PrecedentDegreeInformation(final StudentCandidacy studentCandidacy) {
	this();
	super.setStudentCandidacy(studentCandidacy);
    }

    public String getInstitutionName() {
	return hasInstitution() ? getInstitution().getName() : null;
    }

    public void edit(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

	Unit institution = precedentDegreeInformationBean.getInstitution();
	if (institution == null && !StringUtils.isEmpty(precedentDegreeInformationBean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean.getInstitutionName());
	    }
	}

	this.setSourceInstitution(institution);
	this.setDegreeDesignation(precedentDegreeInformationBean.getDegreeDesignation());
	this.setConclusionGrade(precedentDegreeInformationBean.getConclusionGrade());
	this.setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
	this.setCountry(precedentDegreeInformationBean.getCountry());
	this.setSchoolLevel(precedentDegreeInformationBean.getSchoolLevel());
	this.setOtherSchoolLevel(precedentDegreeInformationBean.getOtherSchoolLevel());
    }

    public void delete() {
	removeCountry();
	removeInstitution();
	if (hasSourceInstitution()) {
	    removeSourceInstitution();
	}
	removeStudent();
	removeStudentCandidacy();

	removeRootDomainObject();
	deleteDomainObject();
    }

    public void edit(final CandidacyInformationBean bean) {
	setConclusionGrade(bean.getConclusionGrade());
	setConclusionYear(bean.getConclusionYear());
	setCountry(bean.getCountryWhereFinishedPrecedentDegree());
	setInstitution(bean.getInstitution());
	setDegreeDesignation(bean.getDegreeDesignation());
	setSchoolLevel(bean.getSchoolLevel());
	setOtherSchoolLevel(bean.getOtherSchoolLevel());
    }

    public void editMissingInformation(final CandidacyInformationBean bean) {
	setConclusionGrade(hasConclusionGrade() ? getConclusionGrade() : bean.getConclusionGrade());
	setConclusionYear(hasConclusionYear() ? getConclusionYear() : bean.getConclusionYear());
	setCountry(hasCountry() ? getCountry() : bean.getCountryWhereFinishedPrecedentDegree());
	setInstitution(hasInstitution() ? getInstitution() : getOrCreateInstitution(bean));
	setDegreeDesignation(hasDegreeDesignation() ? getDegreeDesignation() : bean.getDegreeDesignation());
	setSchoolLevel(hasSchoolLevel() ? getSchoolLevel() : bean.getSchoolLevel());
	setOtherSchoolLevel(hasOtherSchoolLevel() ? getOtherSchoolLevel() : bean.getOtherSchoolLevel());
    }

    private Unit getOrCreateInstitution(final CandidacyInformationBean bean) {
	if (bean.getInstitution() != null) {
	    return bean.getInstitution();
	}

	if (bean.getInstitutionName() == null || bean.getInstitutionName().isEmpty()) {
	    throw new DomainException("error.PrecedentDegreeInformation.invalid.institution.name");
	}

	final Unit unit = Unit.findFirstExternalUnitByName(bean.getInstitutionName());
	return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
    }

    private boolean hasConclusionGrade() {
	return getConclusionGrade() != null && !getConclusionGrade().isEmpty();
    }

    private boolean hasConclusionYear() {
	return getConclusionYear() != null;
    }

    private boolean hasDegreeDesignation() {
	return getDegreeDesignation() != null && !getDegreeDesignation().isEmpty();
    }

    private boolean hasSchoolLevel() {
	return getSchoolLevel() != null;
    }

    private boolean hasOtherSchoolLevel() {
	return getOtherSchoolLevel() != null && !getOtherSchoolLevel().isEmpty();
    }

}
