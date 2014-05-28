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
package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DocumentRequestCreateBean extends RegistrationAcademicServiceRequestCreateBean implements IDocumentRequestBean {

    public static class CycleTypeProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new EnumConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            DocumentRequestCreateBean bean = (DocumentRequestCreateBean) source;
            return bean.getRegistration().getDegreeType().getCycleTypes();
        }
    }

    private static final long serialVersionUID = 1L;

    private DocumentRequestType chosenDocumentRequestType;

    private DocumentPurposeType chosenDocumentPurposeType;

    private String otherPurpose;

    private CurriculumGroup branchCurriculumGroup;

    private String branch;

    private Boolean average = Boolean.FALSE;

    private Boolean detailed = Boolean.FALSE;

    private Boolean technicalEngineer = Boolean.FALSE;

    private Boolean internshipAbolished = Boolean.FALSE;

    private Boolean internshipApproved = Boolean.FALSE;

    private Boolean studyPlan = Boolean.FALSE;

    private YearMonthDay exceptionalConclusionDate;

    private Boolean toBeCreated;

    private String schema;

    private Collection<String> warningsToReport;

    private Integer year;

    private CycleType requestedCycle;

    private String givenNames;

    private String familyNames;

    private MobilityProgram mobilityProgram;

    private boolean ignoreExternalEntries = false;

    private boolean ignoreCurriculumInAdvance = false;

    private boolean toUseAll = false;

    private List<Enrolment> enrolments;

    private ExecutionSemester executionSemester;

    private List<Exam> exams;

    private Integer numberOfCourseLoads;

    private Unit institution;

    private Integer numberOfPrograms;

    private Money pastPaymentAmount;

    private LocalDate pastRequestDate;

    private LocalDate pastPaymentDate;

    private LocalDate pastEmissionDate;

    private LocalDate pastDispatchDate;

    private RegistrationAgreement registrationAgreement;

    public DocumentRequestCreateBean(Registration registration) {
        super(registration);
        this.enrolments = new ArrayList<Enrolment>();
        this.exams = new ArrayList<Exam>();
        pastRequestDate = new LocalDate();
        this.registrationAgreement = registration.getRegistrationAgreement();

        if (RegistrationAgreement.MOBILITY_AGREEMENTS.contains(registrationAgreement)) {
            setLanguage(MultiLanguageString.en);
        } else {
            setLanguage(MultiLanguageString.pt);
        }
    }

    public RegistrationAgreement getRegistrationAgreement() {
        return this.registrationAgreement;
    }

    public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        this.registrationAgreement = registrationAgreement;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
        return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
        this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    public DocumentPurposeType getChosenDocumentPurposeType() {
        return chosenDocumentPurposeType;
    }

    public void setChosenDocumentPurposeType(DocumentPurposeType chosenDocumentPurposeType) {
        this.chosenDocumentPurposeType = chosenDocumentPurposeType;
    }

    public String getOtherPurpose() {
        return otherPurpose;
    }

    public void setOtherPurpose(String otherPurpose) {
        this.otherPurpose = otherPurpose;
    }

    public Boolean getAverage() {
        return average;
    }

    public void setAverage(Boolean average) {
        this.average = average;
    }

    public Boolean getDetailed() {
        return detailed;
    }

    public void setDetailed(Boolean detailed) {
        this.detailed = detailed;
    }

    public Boolean getTechnicalEngineer() {
        return technicalEngineer;
    }

    public void setTechnicalEngineer(Boolean technicalEngineer) {
        this.technicalEngineer = technicalEngineer;
    }

    public Boolean getInternshipAbolished() {
        return internshipAbolished;
    }

    public void setInternshipAbolished(Boolean internshipAbolished) {
        this.internshipAbolished = internshipAbolished;
    }

    public Boolean getInternshipApproved() {
        return internshipApproved;
    }

    public void setInternshipApproved(Boolean internshipApproved) {
        this.internshipApproved = internshipApproved;
    }

    public Boolean getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(Boolean studyPlan) {
        this.studyPlan = studyPlan;
    }

    public YearMonthDay getExceptionalConclusionDate() {
        return exceptionalConclusionDate;
    }

    public void setExceptionalConclusionDate(YearMonthDay exceptionalConclusionDate) {
        this.exceptionalConclusionDate = exceptionalConclusionDate;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getToBeCreated() {
        return toBeCreated;
    }

    public void setToBeCreated(Boolean toBeCreated) {
        this.toBeCreated = toBeCreated;
    }

    /**
     * This method is only needed to report warnings to the user. While we don't
     * have enough info in our database to decide on what cases the document
     * request should abort (acording to the Administrative Office rules shown
     * in the use cases), warnings must be shown to the user.
     * 
     * @return
     */
    public Collection<String> getWarningsToReport() {
        if (warningsToReport == null) {
            warningsToReport = new HashSet<String>();

            if (chosenDocumentRequestType == DocumentRequestType.APPROVEMENT_CERTIFICATE) {
                if (chosenDocumentPurposeType == DocumentPurposeType.PROFESSIONAL) {
                    warningsToReport.add("aprovementType.professionalPurpose.thirdGrade");
                }

                warningsToReport.add("aprovementType.finished.degree");
            }

            if (chosenDocumentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
                warningsToReport.add("degreeFinalizationType.withoutDegreeCertificate");
            }
        }

        return warningsToReport;
    }

    public boolean hasWarningsToReport() {
        if (warningsToReport == null) {
            getWarningsToReport();
        }
        return !warningsToReport.isEmpty();
    }

    public void setPurpose(DocumentPurposeType chosenDocumentPurposeType, String otherPurpose) {

        otherPurpose = otherPurpose.trim();
        if (chosenDocumentPurposeType != null && chosenDocumentPurposeType.equals(DocumentPurposeType.OTHER)
                && (otherPurpose == null || otherPurpose.length() == 0)) {
            throw new DomainException("DocumentRequestCreateBean.error.other.purpose.required");
        }

        this.chosenDocumentPurposeType = chosenDocumentPurposeType;
        this.otherPurpose = otherPurpose;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public CycleType getRequestedCycle() {
        return requestedCycle;
    }

    public void setRequestedCycle(final CycleType cycleType) {
        this.requestedCycle = cycleType;
    }

    @Override
    protected void setRegistration(Registration registration) {
        super.setRegistration(registration);
        if (registration.getPerson().getGivenNames() == null) {
            String[] parts = registration.getStudent().getPerson().getName().split("\\s+");
            int split = parts.length > 3 ? 2 : 1;
            setGivenNames(StringUtils.join(Arrays.copyOfRange(parts, 0, split), " "));
            setFamilyNames(StringUtils.join(Arrays.copyOfRange(parts, split, parts.length), " "));
        } else {
            setGivenNames(registration.getPerson().getGivenNames());
            setFamilyNames(registration.getPerson().getFamilyNames());
        }
    }

    public void validateNames() {
        final String fullName = getRegistration().getStudent().getPerson().getName();
        final String familyName = getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? getGivenNames() : getGivenNames() + " " + familyName;
        if (!fullName.equals(composedName)) {
            throw new DomainException("error.serviceRequests.diplomaRequest.name.split.not.matching.fullname");
        }
    }

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(String familyNames) {
        this.familyNames = familyNames;
    }

    final public boolean getHasAdditionalInformation() {
        return getChosenDocumentRequestType() == null ? false : getChosenDocumentRequestType().getHasAdditionalInformation();
    }

    final public boolean getHasCycleTypeDependency() {
        return getChosenDocumentRequestType().getHasCycleTypeDependency(getRegistration().getDegreeType());
    }

    final public boolean getHasMobilityProgramDependency() {
        return chosenDocumentRequestType == DocumentRequestType.APPROVEMENT_CERTIFICATE
                || chosenDocumentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    final public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    final public void setMobilityProgram(final MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
    }

    public boolean isIgnoreExternalEntries() {
        return ignoreExternalEntries;
    }

    public void setIgnoreExternalEntries(final boolean ignoreExternalEntries) {
        this.ignoreExternalEntries = ignoreExternalEntries;
    }

    public boolean isIgnoreCurriculumInAdvance() {
        return ignoreCurriculumInAdvance;
    }

    public void setIgnoreCurriculumInAdvance(boolean ignoreCurriculumInAdvance) {
        this.ignoreCurriculumInAdvance = ignoreCurriculumInAdvance;
    }

    public boolean isToUseAll() {
        return toUseAll;
    }

    public void setToUseAll(boolean toUseAll) {
        this.toUseAll = toUseAll;
    }

    public List<Enrolment> getEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final Enrolment each : this.enrolments) {
            result.add(each);
        }

        return result;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
        final List<Enrolment> enrolmentsToSet = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : enrolments) {
            enrolmentsToSet.add(enrolment);
        }

        this.enrolments = enrolmentsToSet;
    }

    public List<Exam> getExams() {
        final List<Exam> result = new ArrayList<Exam>();
        for (final Exam each : this.exams) {
            result.add(each);
        }

        return result;
    }

    public void setExams(List<Exam> exams) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final Exam each : exams) {
            result.add(each);
        }

        this.exams = result;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public Integer getNumberOfCourseLoads() {
        return numberOfCourseLoads;
    }

    public void setNumberOfCourseLoads(Integer numberOfCourseLoads) {
        this.numberOfCourseLoads = numberOfCourseLoads;
    }

    public UnitName getInstitutionName() {
        return (this.institution != null) ? this.institution.getUnitName() : null;
    }

    public void setInstitutionName(final UnitName institutionName) {
        this.institution = (institutionName != null) ? institutionName.getUnit() : null;
    }

    public Unit getInstitution() {
        return this.institution;
    }

    public Integer getNumberOfPrograms() {
        return numberOfPrograms;
    }

    public void setNumberOfPrograms(Integer numberOfPrograms) {
        this.numberOfPrograms = numberOfPrograms;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public CurriculumGroup getBranchCurriculumGroup() {
        return branchCurriculumGroup;
    }

    public void setBranchCurriculumGroup(CurriculumGroup branchCurriculumGroup) {
        this.branchCurriculumGroup = (branchCurriculumGroup);
    }

    public String getBranchName() {
        if (getBranchCurriculumGroup() == null) {
            return branch;
        }
        return getBranchCurriculumGroup().getName().getContent();
    }

    public boolean getHasPurposeNeed() {
        return !(chosenDocumentRequestType.isDiploma() || chosenDocumentRequestType.isRegistryDiploma()
                || chosenDocumentRequestType.isPastDiploma() || chosenDocumentRequestType.isDiplomaSupplement());
    }

    public void setPastPaymentAmount(Money paymentAmount) {
        this.pastPaymentAmount = paymentAmount;
    }

    public Money getPastPaymentAmount() {
        return pastPaymentAmount;
    }

    public void setPastPaymentDate(LocalDate paymentDate) {
        this.pastPaymentDate = paymentDate;
    }

    public LocalDate getPastPaymentDate() {
        return pastPaymentDate;
    }

    public LocalDate getPastEmissionDate() {
        return pastEmissionDate;
    }

    public void setPastEmissionDate(LocalDate emissionDate) {
        this.pastEmissionDate = emissionDate;
    }

    public LocalDate getPastDispatchDate() {
        return pastDispatchDate;
    }

    public void setPastDispatchDate(LocalDate dispatchDate) {
        this.pastDispatchDate = dispatchDate;
    }

    public LocalDate getPastRequestDate() {
        return pastRequestDate;
    }

    public void setPastRequestDate(LocalDate pastRequestDate) {
        this.pastRequestDate = pastRequestDate;
    }

    @Override
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

}
