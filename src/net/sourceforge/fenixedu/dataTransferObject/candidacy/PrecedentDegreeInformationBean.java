package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.SchoolPeriodDuration;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PrecedentDegreeInformationBean implements Serializable {

	private static final long serialVersionUID = 574983352972623607L;

	private PrecedentDegreeInformation precedentDegreeInformation;
	private Unit institution;
	private String institutionName;
	private String degreeDesignation;
	private DegreeDesignation raidesDegreeDesignation;
	private String conclusionGrade;
	private Integer conclusionYear;
	private SchoolLevelType schoolLevel;
	private String otherSchoolLevel;
	private Country country;

	private boolean degreeChangeOrTransferOrErasmusStudent = false;
	private SchoolLevelType precedentSchoolLevel;
	private String otherPrecedentSchoolLevel;
	private Unit precedentInstitution;
	private String precedentInstitutionName;
	private String precedentDegreeDesignation;
	private DegreeDesignation precedentDegreeDesignationObject;
	private Integer numberOfPreviousYearEnrolmentsInPrecedentDegree;
	private SchoolPeriodDuration mobilityProgramDuration;

	private Integer numberOfEnroledCurricularCourses;
	private Integer numberOfApprovedCurricularCourses;
	private BigDecimal gradeSum;
	private BigDecimal approvedEcts;
	private BigDecimal enroledEcts;

	private LocalDate conclusionDate;

	public PrecedentDegreeInformationBean() {
		super();
	}

	public PrecedentDegreeInformationBean(PrecedentDegreeInformation information) {
		setPrecedentDegreeInformation(information);
		setDegreeDesignation(information.getDegreeDesignation());
		setInstitution(information.getInstitution());
		setConclusionGrade(information.getConclusionGrade());
		setConclusionYear(information.getConclusionYear());
		setCountry(information.getCountry());
		setSchoolLevel(information.getSchoolLevel());
		setOtherSchoolLevel(information.getOtherSchoolLevel());
	}

	public PrecedentDegreeInformation getPrecedentDegreeInformation() {
		return precedentDegreeInformation;
	}

	public void setPrecedentDegreeInformation(PrecedentDegreeInformation precedentDegreeInformation) {
		this.precedentDegreeInformation = precedentDegreeInformation;
	}

	public Unit getInstitution() {
		return institution;
	}

	public void setInstitution(Unit institution) {
		this.institution = institution;
	}

	public String getConclusionGrade() {
		return conclusionGrade;
	}

	public void setConclusionGrade(String conclusionGrade) {
		this.conclusionGrade = conclusionGrade;
	}

	public Integer getConclusionYear() {
		return conclusionYear;
	}

	public void setConclusionYear(Integer conclusionYear) {
		this.conclusionYear = conclusionYear;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getDegreeDesignation() {
		if (isUnitFromRaidesListMandatory()) {
			return getRaidesDegreeDesignation() != null ? getRaidesDegreeDesignation().getDescription() : null;
		} else {
			return degreeDesignation;
		}
	}

	public boolean isUnitFromRaidesListMandatory() {
		return getSchoolLevel() != null && getSchoolLevel().isHigherEducation() && getCountry() != null
				&& getCountry().isDefaultCountry();
	}

	public void setDegreeDesignation(String degreeDesignation) {
		this.degreeDesignation = degreeDesignation;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public UnitName getInstitutionUnitName() {
		return (institution == null) ? null : institution.getUnitName();
	}

	public void setInstitutionUnitName(UnitName institutionUnitName) {
		this.institution = (institutionUnitName == null) ? null : institutionUnitName.getUnit();
	}

	public SchoolLevelType getSchoolLevel() {
		return schoolLevel;
	}

	public void setSchoolLevel(SchoolLevelType schoolLevel) {
		this.schoolLevel = schoolLevel;
	}

	public String getOtherSchoolLevel() {
		return otherSchoolLevel;
	}

	public void setOtherSchoolLevel(String otherSchoolLevel) {
		this.otherSchoolLevel = otherSchoolLevel;
	}

	public void resetDegree() {
		setDegreeDesignation(null);
		setRaidesDegreeDesignation(null);
	}

	public void validate() {
		if (this.schoolLevel == SchoolLevelType.OTHER && StringUtils.isEmpty(this.otherSchoolLevel)) {
			throw new DomainException("error.registration.PrecedentDegreeInformationBean.otherSchoolLevel.must.be.filled");
		}
		if (isDegreeChangeOrTransferOrErasmusStudent() && StringUtils.isEmpty(getPrecedentDegreeDesignation())) {
			throw new DomainException(
					"error.registration.PrecedentDegreeInformationBean.precedentDegreeDesignation.must.be.filled");
		}
	}

	public void setRaidesDegreeDesignation(DegreeDesignation raidesDegreeDesignation) {
		this.raidesDegreeDesignation = raidesDegreeDesignation;
	}

	public DegreeDesignation getRaidesDegreeDesignation() {
		return raidesDegreeDesignation;
	}

	public void setDegreeChangeOrTransferOrErasmusStudent(boolean degreeChangeOrTransferOrErasmusStudent) {
		this.degreeChangeOrTransferOrErasmusStudent = degreeChangeOrTransferOrErasmusStudent;
	}

	public boolean isDegreeChangeOrTransferOrErasmusStudent() {
		return degreeChangeOrTransferOrErasmusStudent;
	}

	public SchoolLevelType getPrecedentSchoolLevel() {
		return precedentSchoolLevel;
	}

	public void setPrecedentSchoolLevel(SchoolLevelType precedentSchoolLevel) {
		this.precedentSchoolLevel = precedentSchoolLevel;
	}

	public void setOtherPrecedentSchoolLevel(String otherPrecedentSchoolLevel) {
		this.otherPrecedentSchoolLevel = otherPrecedentSchoolLevel;
	}

	public String getOtherPrecedentSchoolLevel() {
		return otherPrecedentSchoolLevel;
	}

	public Unit getPrecedentInstitution() {
		return precedentInstitution;
	}

	public void setPrecedentInstitution(Unit precedentInstitution) {
		this.precedentInstitution = precedentInstitution;
	}

	public String getPrecedentInstitutionName() {
		return precedentInstitutionName;
	}

	public void setPrecedentInstitutionName(String precedentInstitutionName) {
		this.precedentInstitutionName = precedentInstitutionName;
	}

	public UnitName getPrecedentInstitutionUnitName() {
		return (getPrecedentInstitution() == null) ? null : getPrecedentInstitution().getUnitName();
	}

	public void setPrecedentInstitutionUnitName(UnitName institutionUnitName) {
		setPrecedentInstitution(institutionUnitName == null ? null : institutionUnitName.getUnit());
	}

	public String getPrecedentDegreeDesignation() {
		return getPrecedentDegreeDesignationObject() != null ? getPrecedentDegreeDesignationObject().getDescription() : precedentDegreeDesignation;
	}

	public void setPrecedentDegreeDesignation(String precedentDegreeDesignation) {
		this.precedentDegreeDesignation = precedentDegreeDesignation;
	}

	public void setPrecedentDegreeDesignationObject(DegreeDesignation precedentDegreeDesignationObject) {
		this.precedentDegreeDesignationObject = precedentDegreeDesignationObject;
	}

	public DegreeDesignation getPrecedentDegreeDesignationObject() {
		return precedentDegreeDesignationObject;
	}

	public void setNumberOfPreviousYearEnrolmentsInPrecedentDegree(Integer numberOfPreviousYearEnrolmentsInPrecedentDegree) {
		this.numberOfPreviousYearEnrolmentsInPrecedentDegree = numberOfPreviousYearEnrolmentsInPrecedentDegree;
	}

	public Integer getNumberOfPreviousYearEnrolmentsInPrecedentDegree() {
		return numberOfPreviousYearEnrolmentsInPrecedentDegree;
	}

	public SchoolPeriodDuration getMobilityProgramDuration() {
		return mobilityProgramDuration;
	}

	public void setMobilityProgramDuration(SchoolPeriodDuration mobilityProgramDuration) {
		this.mobilityProgramDuration = mobilityProgramDuration;
	}

	/* From CandidacyPrecedentDegreeInformationBean */

	public Integer getNumberOfEnroledCurricularCourses() {
		return numberOfEnroledCurricularCourses;
	}

	public void setNumberOfEnroledCurricularCourses(Integer numberOfEnroledCurricularCourses) {
		this.numberOfEnroledCurricularCourses = numberOfEnroledCurricularCourses;
	}

	public Integer getNumberOfApprovedCurricularCourses() {
		return numberOfApprovedCurricularCourses;
	}

	public void setNumberOfApprovedCurricularCourses(Integer numberOfApprovedCurricularCourses) {
		this.numberOfApprovedCurricularCourses = numberOfApprovedCurricularCourses;
	}

	public BigDecimal getGradeSum() {
		return gradeSum;
	}

	public void setGradeSum(BigDecimal gradeSum) {
		this.gradeSum = gradeSum;
	}

	public BigDecimal getApprovedEcts() {
		return approvedEcts;
	}

	public void setApprovedEcts(BigDecimal approvedEcts) {
		this.approvedEcts = approvedEcts;
	}

	public BigDecimal getEnroledEcts() {
		return enroledEcts;
	}

	public void setEnroledEcts(BigDecimal enroledEcts) {
		this.enroledEcts = enroledEcts;
	}

	public LocalDate getConclusionDate() {
		return conclusionDate;
	}

	public void setConclusionDate(LocalDate conclusionDate) {
		this.conclusionDate = conclusionDate;
	}
}
