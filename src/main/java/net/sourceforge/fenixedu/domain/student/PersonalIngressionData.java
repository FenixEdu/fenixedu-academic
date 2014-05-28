/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
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
        setRootDomainObject(Bennu.getInstance());
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
                && originInformationBean.getGrantOwnerProvider() == null) {
            throw new DomainException(
                    "error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
        }
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
                && originInformationBean.getGrantOwnerProvider() == null) {
            throw new DomainException(
                    "error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
        }
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
        setLastModifiedDate(new DateTime());
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
        setLastModifiedDate(new DateTime());
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        super.setExecutionYear(executionYear);

        if (executionYear != null && hasStudent() && studentHasRepeatedPID(getStudent(), executionYear)) {
            throw new DomainException("A Student cannot have two PersonalIngressionData objects for the same ExecutionYear.");
        }
    }

    @Override
    public void setStudent(Student student) {
        super.setStudent(student);

        if (student != null && hasExecutionYear() && studentHasRepeatedPID(student, getExecutionYear())) {
            throw new DomainException("A Student cannot have two PersonalIngressionData objects for the same ExecutionYear.");
        }
    }

    private static boolean studentHasRepeatedPID(Student student, ExecutionYear executionYear) {
        PersonalIngressionData existingPid = null;
        for (PersonalIngressionData pid : student.getPersonalIngressionsData()) {
            if (pid.getExecutionYear().equals(executionYear)) {
                if (existingPid == null) {
                    existingPid = pid;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void delete() {
        // TODO: Make this method safe.	
        setStudent(null);
        setExecutionYear(null);
        getPrecedentDegreesInformations().clear();
        setRootDomainObject(null);
        setCountryOfResidence(null);
        setGrantOwnerProvider(null);
        setDistrictSubdivisionOfResidence(null);
        setSchoolTimeDistrictSubDivisionOfResidence(null);
        deleteDomainObject();
    }

    @ConsistencyPredicate
    public boolean checkHasExecutionYear() {
        return hasExecutionYear();
    }

    @ConsistencyPredicate
    public boolean checkHasStudent() {
        return hasStudent();
    }

    @ConsistencyPredicate
    public boolean checkMultiplicityOfPrecedentDegreesInformations() {
        return getPrecedentDegreesInformationsSet().size() > 0;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreesInformations() {
        return getPrecedentDegreesInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreesInformations() {
        return !getPrecedentDegreesInformationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasFatherSchoolLevel() {
        return getFatherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasHighSchoolType() {
        return getHighSchoolType() != null;
    }

    @Deprecated
    public boolean hasSchoolTimeDistrictSubDivisionOfResidence() {
        return getSchoolTimeDistrictSubDivisionOfResidence() != null;
    }

    @Deprecated
    public boolean hasMotherSchoolLevel() {
        return getMotherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerType() {
        return getGrantOwnerType() != null;
    }

    @Deprecated
    public boolean hasCountryOfResidence() {
        return getCountryOfResidence() != null;
    }

    @Deprecated
    public boolean hasDislocatedFromPermanentResidence() {
        return getDislocatedFromPermanentResidence() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasDistrictSubdivisionOfResidence() {
        return getDistrictSubdivisionOfResidence() != null;
    }

    @Deprecated
    public boolean hasSpouseProfessionalCondition() {
        return getSpouseProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasFatherProfessionType() {
        return getFatherProfessionType() != null;
    }

    @Deprecated
    public boolean hasProfessionalCondition() {
        return getProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasMotherProfessionType() {
        return getMotherProfessionType() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerProvider() {
        return getGrantOwnerProvider() != null;
    }

    @Deprecated
    public boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    @Deprecated
    public boolean hasSpouseSchoolLevel() {
        return getSpouseSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasMotherProfessionalCondition() {
        return getMotherProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasFatherProfessionalCondition() {
        return getFatherProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasProfessionType() {
        return getProfessionType() != null;
    }

    @Deprecated
    public boolean hasSpouseProfessionType() {
        return getSpouseProfessionType() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
