package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * <pre>
 * 
 * 
 * -------------------- ATTENTION --------------------
 * 
 * 02/01/2012
 * 
 * PrecedentDegreeInformation contains not only the completed qualification
 * but the origin degree and institution. The origin is relevant for
 * transfer, degree change and erasmus applications. In this cases the 
 * origin information says the degree the student was enrolled before 
 * this Institution.
 * 
 * The attribute and relation names are misleading on the role they
 * play. So this comment was created in hope to guide the developer.
 * 
 * 
 * The completed qualification information comes from:
 * 
 * - DGES first time students;
 * - Second Cycle applications;
 * - Degree applicattions for graduated students;
 * - Over 23 applications
 * 
 * The attributes for completed qualification information are
 * 
 * - conclusionGrade
 * - conclusionYear
 * - degreeDesignation
 * - schoolLevel
 * - otherSchoolLevel
 * - country
 * - institution
 * - sourceInstitution
 * - numberOfEnrolmentsInPreviousDegrees
 * - conclusionGrade
 * 
 * 
 * Origin institution information is meaningful for:
 * 
 * - Second cycle applications;
 * - Degree transfer applications;
 * - Degree change applications;
 * - Erasmus applications
 * 
 * The attributes are
 * 
 * - precedentDegreeDesignation
 * - precedentSchoolLevel
 * - numberOfEnroledCurricularCourses
 * - numberOfApprovedCurricularCourses
 * - precedentInstitution
 * - precedentCountry
 * - gradeSum
 * - approvedEcts
 * - enroledEcts
 * 
 * 
 * </pre>
 * 
 */

public class PrecedentDegreeInformation extends PrecedentDegreeInformation_Base {

    public static Comparator<PrecedentDegreeInformation> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<PrecedentDegreeInformation>() {
	@Override
	public int compare(PrecedentDegreeInformation info1, PrecedentDegreeInformation info2) {
	    return info1.getExecutionYear().getYear().compareTo(info2.getExecutionYear().getYear());
	}
    };

    public PrecedentDegreeInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setLastModifiedDate(new DateTime());
    }

    public void edit(PersonalIngressionData personalIngressionData, Registration registration,
	    PrecedentDegreeInformationBean precedentDegreeInformationBean, StudentCandidacy studentCandidacy) {
	setPersonalIngressionData(personalIngressionData);
	setRegistration(registration);
	setStudentCandidacy(studentCandidacy);
	Unit institution = precedentDegreeInformationBean.getInstitution();
	if (institution == null && !StringUtils.isEmpty(precedentDegreeInformationBean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean.getInstitutionName());
	    }
	}
	setInstitution(institution);
	setDegreeDesignation(precedentDegreeInformationBean.getDegreeDesignation());
	setConclusionGrade(precedentDegreeInformationBean.getConclusionGrade());
	setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
	setCountry(precedentDegreeInformationBean.getCountry());
	setSchoolLevel(precedentDegreeInformationBean.getSchoolLevel());
	setOtherSchoolLevel(precedentDegreeInformationBean.getOtherSchoolLevel());

	checkAndUpdatePrecedentInformation(precedentDegreeInformationBean);
    }

    public void edit(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

	Unit institution = precedentDegreeInformationBean.getInstitution();
	if (institution == null && !StringUtils.isEmpty(precedentDegreeInformationBean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean.getInstitutionName());
	    }
	}

	this.setInstitution(institution);

	this.setDegreeDesignation(precedentDegreeInformationBean.getDegreeDesignation());
	this.setConclusionGrade(precedentDegreeInformationBean.getConclusionGrade());
	this.setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
	this.setCountry(precedentDegreeInformationBean.getCountry());
	this.setSchoolLevel(precedentDegreeInformationBean.getSchoolLevel());
	this.setOtherSchoolLevel(precedentDegreeInformationBean.getOtherSchoolLevel());
	setLastModifiedDate(new DateTime());
    }

    public String getInstitutionName() {
	return hasInstitution() ? getInstitution().getName() : null;
    }

    public ExecutionYear getExecutionYear() {
	return getPersonalIngressionData().getExecutionYear();
    }

    public void edit(final PersonalInformationBean bean, boolean isStudentEditing) {
	setConclusionGrade(bean.getConclusionGrade());
	setConclusionYear(bean.getConclusionYear());
	setCountry(bean.getCountryWhereFinishedPrecedentDegree());
	Unit institution = bean.getInstitution();
	if (institution == null && !StringUtils.isEmpty(bean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(bean.getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
	    }
	}
	setInstitution(institution);
	setDegreeDesignation(bean.getDegreeDesignation());
	setSchoolLevel(bean.getSchoolLevel());
	setOtherSchoolLevel(bean.getOtherSchoolLevel());

	if (!isStudentEditing) {
	    checkAndUpdatePrecedentInformation(bean);
	}
    }

    //TODO remove this methods below
    public void edit(final CandidacyInformationBean bean) {
	setConclusionGrade(bean.getConclusionGrade());
	setConclusionYear(bean.getConclusionYear());
	setCountry(bean.getCountryWhereFinishedPrecedentDegree());
	setInstitution(bean.getInstitution());
	setDegreeDesignation(bean.getDegreeDesignation());
	setSchoolLevel(bean.getSchoolLevel());
	setOtherSchoolLevel(bean.getOtherSchoolLevel());
	setLastModifiedDate(new DateTime());
    }

    public void editMissingInformation(final CandidacyInformationBean bean) {
	setConclusionGrade(hasConclusionGrade() ? getConclusionGrade() : bean.getConclusionGrade());
	setConclusionYear(hasConclusionYear() ? getConclusionYear() : bean.getConclusionYear());
	setCountry(hasCountry() ? getCountry() : bean.getCountryWhereFinishedPrecedentDegree());
	setInstitution(hasInstitution() ? getInstitution() : getOrCreateInstitution(bean));
	setDegreeDesignation(hasDegreeDesignation() ? getDegreeDesignation() : bean.getDegreeDesignation());
	setSchoolLevel(hasSchoolLevel() ? getSchoolLevel() : bean.getSchoolLevel());
	setOtherSchoolLevel(hasOtherSchoolLevel() ? getOtherSchoolLevel() : bean.getOtherSchoolLevel());
	setLastModifiedDate(new DateTime());
    }

    private void checkAndUpdatePrecedentInformation(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
	if (precedentDegreeInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) {
	    Unit precedentInstitution = precedentDegreeInformationBean.getPrecedentInstitution();
	    if (precedentInstitution == null
		    && !StringUtils.isEmpty(precedentDegreeInformationBean.getPrecedentInstitutionName())) {
		precedentInstitution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean
			.getPrecedentInstitutionName());
		if (precedentInstitution == null) {
		    precedentInstitution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean
			    .getPrecedentInstitutionName());
		}
	    }
	    setPrecedentInstitution(precedentInstitution);
	    setPrecedentDegreeDesignation(precedentDegreeInformationBean.getPrecedentDegreeDesignation());
	    setPrecedentSchoolLevel(precedentDegreeInformationBean.getPrecedentSchoolLevel());
	    setNumberOfEnrolmentsInPreviousDegrees(precedentDegreeInformationBean.getNumberOfPreviousEnrolmentsInDegrees());
	    setMobilityProgramDuration(precedentDegreeInformationBean.getMobilityProgramDuration());
	}
    }

    private void checkAndUpdatePrecedentInformation(PersonalInformationBean personalInformationBean) {
	if (personalInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) {
	    Unit precedentInstitution = personalInformationBean.getPrecedentInstitution();
	    if (precedentInstitution == null && !StringUtils.isEmpty(personalInformationBean.getPrecedentInstitutionName())) {
		precedentInstitution = UnitUtils.readExternalInstitutionUnitByName(personalInformationBean
			.getPrecedentInstitutionName());
		if (precedentInstitution == null) {
		    precedentInstitution = Unit.createNewNoOfficialExternalInstitution(personalInformationBean
			    .getPrecedentInstitutionName());
		}
	    }
	    setPrecedentInstitution(precedentInstitution);
	    setPrecedentDegreeDesignation(personalInformationBean.getPrecedentDegreeDesignation());
	    setPrecedentSchoolLevel(personalInformationBean.getPrecedentSchoolLevel());
	    if (personalInformationBean.getPrecedentSchoolLevel().equals(SchoolLevelType.OTHER)) {
		setOtherPrecedentSchoolLevel(personalInformationBean.getOtherPrecedentSchoolLevel());
	    } else {
		setOtherPrecedentSchoolLevel(null);
	    }
	    setNumberOfEnrolmentsInPreviousDegrees(personalInformationBean.getNumberOfPreviousEnrolmentsInDegrees());
	    setMobilityProgramDuration(personalInformationBean.getMobilityProgramDuration());
	}
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
}
