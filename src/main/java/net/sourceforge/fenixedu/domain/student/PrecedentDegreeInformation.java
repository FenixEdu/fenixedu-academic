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
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
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

    public static Comparator<PrecedentDegreeInformation> COMPARATOR_BY_EXECUTION_YEAR =
            new Comparator<PrecedentDegreeInformation>() {
                @Override
                public int compare(PrecedentDegreeInformation info1, PrecedentDegreeInformation info2) {
                    return info1.getExecutionYear().getYear().compareTo(info2.getExecutionYear().getYear());
                }
            };

    public PrecedentDegreeInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        setCountryHighSchool(precedentDegreeInformationBean.getCountryWhereFinishedHighSchoolLevel());
        setSchoolLevel(precedentDegreeInformationBean.getSchoolLevel());
        setOtherSchoolLevel(precedentDegreeInformationBean.getOtherSchoolLevel());

        checkAndUpdatePrecedentInformation(precedentDegreeInformationBean);
        setLastModifiedDate(new DateTime());
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
        this.setCountryHighSchool(precedentDegreeInformationBean.getCountryWhereFinishedHighSchoolLevel());
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
        setCountry(bean.getCountryWhereFinishedPreviousCompleteDegree());
        setCountryHighSchool(bean.getCountryWhereFinishedHighSchoolLevel());
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
        setLastModifiedDate(new DateTime());
    }

    private void checkAndUpdatePrecedentInformation(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
        if (precedentDegreeInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) {
            Unit precedentInstitution = precedentDegreeInformationBean.getPrecedentInstitution();
            if (precedentInstitution == null
                    && !StringUtils.isEmpty(precedentDegreeInformationBean.getPrecedentInstitutionName())) {
                precedentInstitution =
                        UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getPrecedentInstitutionName());
                if (precedentInstitution == null) {
                    precedentInstitution =
                            Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean
                                    .getPrecedentInstitutionName());
                }
            }
            setPrecedentInstitution(precedentInstitution);
            setPrecedentDegreeDesignation(precedentDegreeInformationBean.getPrecedentDegreeDesignation());
            setPrecedentSchoolLevel(precedentDegreeInformationBean.getPrecedentSchoolLevel());
            setNumberOfEnrolmentsInPreviousDegrees(precedentDegreeInformationBean
                    .getNumberOfPreviousYearEnrolmentsInPrecedentDegree());
            setMobilityProgramDuration(precedentDegreeInformationBean.getMobilityProgramDuration());
        }
    }

    private void checkAndUpdatePrecedentInformation(PersonalInformationBean personalInformationBean) {
        if (personalInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) {
            Unit precedentInstitution = personalInformationBean.getPrecedentInstitution();
            if (precedentInstitution == null && !StringUtils.isEmpty(personalInformationBean.getPrecedentInstitutionName())) {
                precedentInstitution =
                        UnitUtils.readExternalInstitutionUnitByName(personalInformationBean.getPrecedentInstitutionName());
                if (precedentInstitution == null) {
                    precedentInstitution =
                            Unit.createNewNoOfficialExternalInstitution(personalInformationBean.getPrecedentInstitutionName());
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
            setNumberOfEnrolmentsInPreviousDegrees(personalInformationBean.getNumberOfPreviousYearEnrolmentsInPrecedentDegree());
            setMobilityProgramDuration(personalInformationBean.getMobilityProgramDuration());
        }
    }

    public String getDegreeAndInstitutionName() {
        String institutionName = null;
        if (hasInstitution()) {
            institutionName = getInstitution().getName();
        } else {
            institutionName = getPrecedentInstitution().getName();
        }
        return getDegreeDesignation() + " / " + institutionName;
    }

    public void delete() {
        setCountry(null);
        setCountryHighSchool(null);
        setInstitution(null);
        setSourceInstitution(null);

        setStudent(null);
        setRegistration(null);
        setPhdIndividualProgramProcess(null);

        setStudentCandidacy(null);
        setIndividualCandidacy(null);

        setPrecedentCountry(null);
        setPrecedentInstitution(null);

        setPersonalIngressionData(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public void setPersonalIngressionData(PersonalIngressionData personalIngressionData) {
        super.setPersonalIngressionData(personalIngressionData);

        if (personalIngressionData != null && hasRegistration()
                && registrationHasRepeatedPDI(getRegistration(), personalIngressionData.getExecutionYear())) {
            throw new DomainException("A Registration cannot have two PrecedentDegreeInformations for the same ExecutionYear.");
        }

        if (hasPhdIndividualProgramProcess()
                && phdProcessHasRepeatedPDI(getPhdIndividualProgramProcess(), personalIngressionData.getExecutionYear())) {
            throw new DomainException("A Phd Process cannot have two PrecedentDegreeInformations for the same ExecutionYear.");
        }
    }

    @Override
    public void setRegistration(Registration registration) {
        super.setRegistration(registration);

        if (registration != null && hasPersonalIngressionData()
                && registrationHasRepeatedPDI(registration, getPersonalIngressionData().getExecutionYear())) {
            throw new DomainException("A Registration cannot have two PrecedentDegreeInformations for the same ExecutionYear.");
        }
    }

    @Override
    public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        super.setPhdIndividualProgramProcess(phdIndividualProgramProcess);

        if (phdIndividualProgramProcess != null && hasPersonalIngressionData()
                && phdProcessHasRepeatedPDI(phdIndividualProgramProcess, getPersonalIngressionData().getExecutionYear())) {
            throw new DomainException("A Registration cannot have two PrecedentDegreeInformations for the same ExecutionYear.");
        }
    }

    private static boolean registrationHasRepeatedPDI(Registration registration, ExecutionYear executionYear) {
        PrecedentDegreeInformation existingPdi = null;
        for (PrecedentDegreeInformation pdi : registration.getPrecedentDegreesInformations()) {
            if (pdi.getExecutionYear().equals(executionYear)) {
                if (existingPdi == null) {
                    existingPdi = pdi;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean phdProcessHasRepeatedPDI(PhdIndividualProgramProcess phdProcess, ExecutionYear executionYear) {
        PrecedentDegreeInformation existingPdi = null;
        for (PrecedentDegreeInformation pdi : phdProcess.getPrecedentDegreeInformations()) {
            if (pdi.getExecutionYear().equals(executionYear)) {
                if (existingPdi == null) {
                    existingPdi = pdi;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @ConsistencyPredicate
    public boolean checkHasAllRegistrationOrPhdInformation() {
        return (hasAllRegistrationInformation() && !hasAllPhdInformation())
                || (!hasAllRegistrationInformation() && hasAllPhdInformation())
                || (hasNoPersonalInformation() && hasAtLeastOneCandidacy());
    }

    private boolean hasAllRegistrationInformation() {
        return hasPersonalIngressionData() && hasRegistration();
    }

    private boolean hasAllPhdInformation() {
        return hasPersonalIngressionData() && hasPhdIndividualProgramProcess();
    }

    private boolean hasNoPersonalInformation() {
        return !hasPersonalIngressionData() && !hasRegistration() && !hasPhdIndividualProgramProcess();
    }

    private boolean hasAtLeastOneCandidacy() {
        return hasStudentCandidacy() || hasIndividualCandidacy();
    }

    public boolean isCandidacyInternal() {
        return getCandidacyInternal() != null && getCandidacyInternal();
    }

    public boolean isCandidacyExternal() {
        return !isCandidacyInternal();
    }

    @Override
    public net.sourceforge.fenixedu.domain.organizationalStructure.Unit getPrecedentInstitution() {
        if (isCandidacyInternal()) {
            return Bennu.getInstance().getInstitutionUnit();
        }

        return super.getPrecedentInstitution();

    }

    @Deprecated
    public boolean hasCandidacyInternal() {
        return getCandidacyInternal() != null;
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumberOfEnrolmentsInPreviousDegrees() {
        return getNumberOfEnrolmentsInPreviousDegrees() != null;
    }

    @Deprecated
    public boolean hasConclusionDate() {
        return getConclusionDate() != null;
    }

    @Deprecated
    public boolean hasPrecedentSchoolLevel() {
        return getPrecedentSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasApprovedEcts() {
        return getApprovedEcts() != null;
    }

    @Deprecated
    public boolean hasOtherPrecedentSchoolLevel() {
        return getOtherPrecedentSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

    @Deprecated
    public boolean hasConclusionGrade() {
        return getConclusionGrade() != null;
    }

    @Deprecated
    public boolean hasConclusionYear() {
        return getConclusionYear() != null;
    }

    @Deprecated
    public boolean hasSchoolLevel() {
        return getSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasOtherSchoolLevel() {
        return getOtherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasPersonalIngressionData() {
        return getPersonalIngressionData() != null;
    }

    @Deprecated
    public boolean hasNumberOfApprovedCurricularCourses() {
        return getNumberOfApprovedCurricularCourses() != null;
    }

    @Deprecated
    public boolean hasNumberOfEnroledCurricularCourses() {
        return getNumberOfEnroledCurricularCourses() != null;
    }

    @Deprecated
    public boolean hasGradeSum() {
        return getGradeSum() != null;
    }

    @Deprecated
    public boolean hasEnroledEcts() {
        return getEnroledEcts() != null;
    }

    @Deprecated
    public boolean hasIndividualCandidacy() {
        return getIndividualCandidacy() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasDegreeDesignation() {
        return getDegreeDesignation() != null;
    }

    @Deprecated
    public boolean hasMobilityProgramDuration() {
        return getMobilityProgramDuration() != null;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasPrecedentCountry() {
        return getPrecedentCountry() != null;
    }

    @Deprecated
    public boolean hasSourceInstitution() {
        return getSourceInstitution() != null;
    }

    @Deprecated
    public boolean hasPrecedentInstitution() {
        return getPrecedentInstitution() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasPrecedentDegreeDesignation() {
        return getPrecedentDegreeDesignation() != null;
    }

    @Deprecated
    public boolean hasCycleType() {
        return getCycleType() != null;
    }

}
