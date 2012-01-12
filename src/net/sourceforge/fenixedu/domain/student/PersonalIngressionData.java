package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class PersonalIngressionData extends PersonalIngressionData_Base {

    public static Comparator<PersonalIngressionData> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<PersonalIngressionData>() {
	@Override
	public int compare(PersonalIngressionData data1, PersonalIngressionData data2) {
	    return data1.getExecutionYear().getYear().compareTo(data2.getExecutionYear().getYear());
	}
    };

    public PersonalIngressionData() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setLastModifiedDate(new DateTime());
    }

    public PersonalIngressionData(ExecutionYear executionYear, PrecedentDegreeInformation precedentDegreeInformation) {
	this();
	setExecutionYear(executionYear);
	addPrecedentDegreesInformations(precedentDegreeInformation);
    }

    public PersonalIngressionData(Student student, ExecutionYear executionYear,
	    PrecedentDegreeInformation precedentDegreeInformation) {
	this(executionYear, precedentDegreeInformation);
	setStudent(student);
    }

    public PersonalIngressionData(OriginInformationBean originInformationBean, PersonBean personBean, Student student,
	    ExecutionYear executionYear) {
	this();
	setStudent(student);
	setExecutionYear(executionYear);
	setDistrictSubdivisionOfResidence(personBean.getDistrictSubdivisionOfResidenceObject());
	setCountryOfResidence(personBean.getCountryOfResidence());
	setSchoolTimeDistrictSubDivisionOfResidence(originInformationBean.getSchoolTimeDistrictSubdivisionOfResidence());
	setDislocatedFromPermanentResidence(originInformationBean.getDislocatedFromPermanentResidence());
	setGrantOwnerType(originInformationBean.getGrantOwnerType());
	if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
		&& originInformationBean.getGrantOwnerProvider() == null)
	    throw new DomainException(
		    "error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
	setGrantOwnerProvider(originInformationBean.getGrantOwnerProvider());
	setHighSchoolType(originInformationBean.getHighSchoolType());
	setMaritalStatus(personBean.getMaritalStatus());
	setProfessionType(personBean.getProfessionType());
	setProfessionalCondition(personBean.getProfessionalCondition());

	setMotherSchoolLevel(originInformationBean.getMotherSchoolLevel());
	setMotherProfessionType(originInformationBean.getMotherProfessionType());
	setMotherProfessionalCondition(originInformationBean.getMotherProfessionalCondition());
	setFatherSchoolLevel(originInformationBean.getFatherSchoolLevel());
	setFatherProfessionType(originInformationBean.getFatherProfessionType());
	setFatherProfessionalCondition(originInformationBean.getFatherProfessionalCondition());
    }

    public void edit(OriginInformationBean originInformationBean, PersonBean personBean) {
	setDistrictSubdivisionOfResidence(personBean.getDistrictSubdivisionOfResidenceObject());
	setCountryOfResidence(personBean.getCountryOfResidence());
	setSchoolTimeDistrictSubDivisionOfResidence(originInformationBean.getSchoolTimeDistrictSubdivisionOfResidence());
	setDislocatedFromPermanentResidence(originInformationBean.getDislocatedFromPermanentResidence());
	setGrantOwnerType(originInformationBean.getGrantOwnerType());
	if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
		&& originInformationBean.getGrantOwnerProvider() == null)
	    throw new DomainException(
		    "error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
	setGrantOwnerProvider(originInformationBean.getGrantOwnerProvider());
	setHighSchoolType(originInformationBean.getHighSchoolType());
	setMaritalStatus(personBean.getMaritalStatus());
	setProfessionType(personBean.getProfessionType());
	setProfessionalCondition(personBean.getProfessionalCondition());

	setMotherSchoolLevel(originInformationBean.getMotherSchoolLevel());
	setMotherProfessionType(originInformationBean.getMotherProfessionType());
	setMotherProfessionalCondition(originInformationBean.getMotherProfessionalCondition());
	setFatherSchoolLevel(originInformationBean.getFatherSchoolLevel());
	setFatherProfessionType(originInformationBean.getFatherProfessionType());
	setFatherProfessionalCondition(originInformationBean.getFatherProfessionalCondition());
    }

    public void edit(final PersonalInformationBean bean) {
	setCountryOfResidence(bean.getCountryOfResidence());
	setDistrictSubdivisionOfResidence(bean.getDistrictSubdivisionOfResidence());
	setDislocatedFromPermanentResidence(bean.getDislocatedFromPermanentResidence());
	setSchoolTimeDistrictSubDivisionOfResidence(bean.getSchoolTimeDistrictSubdivisionOfResidence());
	setGrantOwnerType(bean.getGrantOwnerType());
	setGrantOwnerProvider(bean.getGrantOwnerProvider());
	setHighSchoolType(bean.getHighSchoolType());
	setMaritalStatus(bean.getMaritalStatus());
	setProfessionType(bean.getProfessionType());
	setProfessionalCondition(bean.getProfessionalCondition());
	setMotherSchoolLevel(bean.getMotherSchoolLevel());
	setMotherProfessionType(bean.getMotherProfessionType());
	setMotherProfessionalCondition(bean.getMotherProfessionalCondition());
	setFatherSchoolLevel(bean.getFatherSchoolLevel());
	setFatherProfessionType(bean.getFatherProfessionType());
	setFatherProfessionalCondition(bean.getFatherProfessionalCondition());
    }

    public void delete() {
	// TODO: Make this method safe.
	setLastModifiedDate(null);
	removeStudent();
	removeExecutionYear();
	getPrecedentDegreesInformations().clear();
	removeRootDomainObject();
	removeCountryOfResidence();
	removeGrantOwnerProvider();
	removeDistrictSubdivisionOfResidence();
	removeSchoolTimeDistrictSubDivisionOfResidence();
	deleteDomainObject();
    }
}
