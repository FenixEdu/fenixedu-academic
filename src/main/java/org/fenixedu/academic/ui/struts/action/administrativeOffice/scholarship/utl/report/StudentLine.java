/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.scholarship.utl.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentLine implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StudentLine.class);

    protected static final List<Integer> STUDENTS_WITH_CET = Arrays.asList(new Integer[] { 70855, 70696, 70757, 70786, 55647,
            59218, 70749, 70856, 70678, 70681, 70712, 70737, 70837, 70793, 10425, 38565, 70783, 70664, 70859, 70766, 70844,
            48936, 50315, 70788, 70794, 70795, 70804, 70809, 70716, 70719, 70763, 70776, 70841, 70923 });

    public ExecutionYear forExecutionYear;

    public String institutionCode;
    public String institutionName;
    public String candidacyNumber;
    public Integer studentNumber;
    public String studentName;
    public String documentTypeName;
    public String documentNumber;
    public String degreeCode;
    public String degreeName;
    public String degreeTypeName;
    public String upperObservations = "";

    public boolean enrolledInAnualCoursesLastYear;
    private Student student = null;
    private Registration registration = null;
    private Person person = null;
    private boolean multiplePersonsFound = false;
    private String observations;

    public boolean fillWithFileLineData(String dataLine) {
        String[] compounds = dataLine.split(";");

        this.institutionCode = compounds[0].trim();
        this.institutionName = compounds[1].trim();
        this.candidacyNumber = compounds[2].trim();

        try {
            this.studentNumber = Integer.valueOf(compounds[3].trim());

        } catch (NumberFormatException e) {
            this.studentNumber = null;
        }
        this.studentName = compounds[4].trim();
        this.documentTypeName = compounds[5].trim();
        this.documentNumber = compounds[6].trim();
        this.degreeCode = compounds[7].trim();
        this.degreeName = compounds[8].trim();
        this.degreeTypeName = compounds[9].trim();

        if (compounds.length == 36) {
            this.upperObservations = compounds[35].trim();
        }

        setBestMatchingStudent();

        if (this.multiplePersonsFound) {
            return false;
        }

        if (this.person == null) {
            return false;
        }

        enrolledInAnualCoursesLastYear = false;

        return true;
    }

    public boolean fillWithSpreadsheetRow(final ExecutionYear forExecutionYear, final HSSFRow row) {
        this.forExecutionYear = forExecutionYear;

        try {
            this.institutionCode = getValueFromColumnMayBeNull(row, 0);
            this.institutionName = getValueFromColumnMayBeNull(row, 1);
            this.candidacyNumber = getValueFromColumnMayBeNull(row, 2);

            try {
                this.studentNumber = Integer.valueOf(getValueFromColumnMayBeNull(row, 3));

            } catch (NumberFormatException e) {
                this.studentNumber = null;
            }
            this.studentName = getValueFromColumnMayBeNull(row, 4);
            this.documentTypeName = getValueFromColumnMayBeNull(row, 5);
            this.documentNumber = getValueFromColumnMayBeNull(row, 6);
            this.degreeCode = getValueFromColumnMayBeNull(row, 7);
            this.degreeName = getValueFromColumnMayBeNull(row, 8);
            this.degreeTypeName = getValueFromColumnMayBeNull(row, 9);

            if (row.getLastCellNum() >= 35) {
                this.upperObservations = getValueFromColumnMayBeNull(row, 35);
            }

            setBestMatchingStudent();

            if (this.multiplePersonsFound) {
                return false;
            }

            if (this.person == null) {
                return false;
            }

            enrolledInAnualCoursesLastYear = false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getValueFromColumnMayBeNull(HSSFRow row, int i) {
        HSSFCell cell = row.getCell((short) i);
        if (cell == null) {
            return StringUtils.EMPTY;
        }
        return getValueFromColumn(row, i).trim();
    }

    private String getValueFromColumn(HSSFRow row, int i) {
        try {
            return new Integer(new Double(row.getCell((short) i).getNumericCellValue()).intValue()).toString();
        } catch (NumberFormatException e) {
            return row.getCell((short) i).getStringCellValue();
        } catch (IllegalStateException e) {
            return row.getCell((short) i).getStringCellValue();
        }
    }

    public String getUniqueKey() {
        return this.documentNumber;
    }

    private static final String INTEGRATED_MASTER_DESIGNATION = "Mestrado Integrado";
    private static final String BOLONHA_DEGREE_DESIGNATION = "Licenciatura 1º Ciclo";

    public DegreeType readDegreeType() {
        if (this.degreeTypeName.equals(INTEGRATED_MASTER_DESIGNATION)) {
            return DegreeType.matching(DegreeType::isIntegratedMasterDegree).get();
        } else if (this.degreeTypeName.equals(BOLONHA_DEGREE_DESIGNATION)) {
            return DegreeType.matching(DegreeType::isBolonhaDegree).get();
        }

        throw new RuntimeException("Unknown degree type " + this.degreeTypeName);
    }

    public Degree readDegree() {
        for (Degree degree : Degree.readAllByDegreeCode(this.degreeCode)) {
            if (degree.getDegreeType().equals(readDegreeType())) {
                return degree;
            }
        }

        return null;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isStudentFound() {
        return getStudent() != null;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isPersonFound() {
        return getPerson() != null;
    }

    public Registration getRegistration() {
        if (getStudent() == null) {
            return null;
        }

        if (registration == null) {
            registration = getStudent().getLastActiveRegistration();
        }

        return this.registration;
    }

    public boolean isStudentActive() {
        if (getRegistration() == null) {
            return false;
        }

        if (!getRegistration().isActive()) {
            return false;
        }

        return getRegistration().getLastStudentCurricularPlan().hasAnyEnrolmentForExecutionYear(getForExecutionYear())
                || getRegistration().isInMobilityState();
    }

    public boolean isLastRegistrationEqualToSpecifiedDegree() {
        if (getRegistration() == null) {
            return false;
        }

        return getRegistration().getDegree() == readDegree();
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        final Registration registration = getRegistration();
        return registration == null ? null : registration.getLastStudentCurricularPlan();
    }

    public boolean isDocumentIdentificationEqual() {
        return getPerson().getDocumentIdNumber().equals(this.documentNumber);
    }

    public boolean isNameEqual() {
        return getPerson().getName().toUpperCase().equals(this.studentName.toUpperCase());
    }

    public boolean isMasterQualificationOwner() {
        for (Qualification qualification : getPerson().getAssociatedQualificationsSet()) {
            if (QualificationTypes.isMasterQualificationType(qualification.getType())) {
                return true;
            }
        }

        return false;
    }

    public boolean isPhdQualificationOwner() {
        for (Qualification qualification : getPerson().getAssociatedQualificationsSet()) {
            if (QualificationTypes.isPhdQualificationType(qualification.getType())) {
                return true;
            }
        }

        return false;
    }

    public boolean isDegreeQualificationOwner() {
        for (Qualification qualification : getPerson().getAssociatedQualificationsSet()) {
            if (QualificationTypes.isDegreeQualificationType(qualification.getType())) {
                return true;
            }
        }

        return false;
    }

    public LocalDate getFirstEnrolmentOnCurrentExecutionYear() {
        if (getRegistration() == null) {
            return null;
        }

        if (getRegistration().isInMobilityState()) {
            return getForExecutionYear().getBeginDateYearMonthDay().toLocalDate();
        }

        TreeSet<Enrolment> orderedEnrolmentSet =
                new TreeSet<Enrolment>(Collections.reverseOrder(CurriculumModule.COMPARATOR_BY_CREATION_DATE));
        orderedEnrolmentSet.addAll(getStudentCurricularPlan().getEnrolmentsByExecutionYear(getForExecutionYear()));

        return orderedEnrolmentSet.isEmpty() ? null : orderedEnrolmentSet.iterator().next().getCreationDateDateTime()
                .toLocalDate();
    }

    public LocalDate getEnrolmentDateTime() {
        if (getRegistration().isFirstTime()) {
            return getRegistration().getStartDate().toLocalDate();
        }

        return getFirstEnrolmentOnCurrentExecutionYear();
    }

    public Money getGratuityAmount() {
        if (getRegistration() == null) {
            return Money.ZERO;
        }

        if (!getRegistration().hasToPayGratuityOrInsurance()) {
            return Money.ZERO;
        }

        if (!registration.getDegree().isEmpty()) {
            final Stream<Event> eventsFor = registration.getGratuityEventsFor(getForExecutionYear());
            final Event event = eventsFor.sorted(Comparator.comparing(Event::getWhenOccured).reversed())
                    .findFirst().orElse(null);
            if (event != null) {
                return event.getOriginalAmountToPay();
            }
        }
        return Money.ZERO;
    }

    public String getRegime() {
        if (getRegistration() == null) {
            return "";
        }

        RegistrationRegimeType type = getRegistration().getRegimeType(getForExecutionYear());

        if (RegistrationRegimeType.FULL_TIME.equals(type)) {
            return "Tempo integral";
        } else if (RegistrationRegimeType.PARTIAL_TIME.equals(type)) {
            return "Tempo parcial";
        }

        return "";
    }

    public Integer getNumberOfDegreeCurricularYears() {
        if (getRegistration() == null) {
            return null;
        }

        return getStudentCurricularPlan().getDegreeCurricularPlan().getDurationInYears();
    }

    public Integer getNumberOfEnrolmentYearsOnIST() {
        return getEnrolmentsExecutionYears(getStudent()).size();
    }

    public String getDegreeCode() {
        if (getRegistration() == null) {
            return "";
        }

        String ministryCode = getRegistration().getDegree().getMinistryCode();
        return "9999".equals(ministryCode) ? "0" : ministryCode;
    }

    public String getDegreeName() {
        if (getRegistration() != null) {
            return getRegistration().getDegree().getNameI18N().getContent(org.fenixedu.academic.util.LocaleUtils.PT);
        }

        return degreeName;
    }

    public boolean isOwnerOfCollegeQualification() {
        return isDegreeQualificationOwner() || isMasterQualificationOwner() || isPhdQualificationOwner();
    }

    public double getNumberOfDoneECTS() {
        return getRegistration().getEctsCredits();
    }

    public Double getNumberOfEnrolledECTS() {
        if (getStudentCurricularPlan() == null) {
            return null;
        }

        return getEnrolmentsEctsCredits(getForExecutionYear());
    }

    private double getEnrolmentsEctsCredits(final ExecutionYear executionYear) {
        double result = 0.0;
        Set<Enrolment> annualEnrolments = new HashSet<Enrolment>();

        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (final Enrolment enrolment : getStudentCurricularPlan().getEnrolmentsSet()) {
                if (enrolment.isValid(executionSemester) && !annualEnrolments.contains(enrolment)) {
                    result += enrolment.getEctsCredits();

                    if (enrolment.isAnual()) {
                        annualEnrolments.add(enrolment);
                    }
                }
            }
        }

        return result;
    }

    public boolean isMultiplePersonsFound() {
        return this.multiplePersonsFound;
    }

    public String getStudentNumberForPrint() {
        if (this.studentNumber != null) {
            return this.studentNumber.toString();
        }

        if (!isStudentFound()) {
            return "n/a";
        }

        return getStudent().getNumber().toString();
    }

    public String getDegreeTypeName() {
        if (getRegistration() == null) {
            return "";
        }

        if (getRegistration().getDegree().getDegreeType().isBolonhaDegree()) {
            return BOLONHA_DEGREE_DESIGNATION;
        }

        if (getRegistration().getDegree().getDegreeType().isIntegratedMasterDegree()) {
            return INTEGRATED_MASTER_DESIGNATION;
        }

        return getRegistration().getDegree().getDegreeType().getName().getContent();
    }

    public boolean isStudentNumberDiffer() {
        if (this.studentNumber != null && this.isStudentFound()) {
            return this.studentNumber.equals(this.getStudent().getNumber());
        }

        return false;
    }

    private Collection<Person> readPersonsByCompleteDocumentId() {
        return Person.readByDocumentIdNumber(this.documentNumber);
    }

    public String getPartialDocumentId() {
        return this.documentNumber.substring(0, this.documentNumber.length() - 1);
    }

    private Collection<Person> readPersonsByPartialDocumentId() {
        return Person.readByDocumentIdNumber(getPartialDocumentId());
    }

    public boolean isPersonReadByPartialDocumentId() {
        return readPersonsByCompleteDocumentId().isEmpty() && readPersonsByPartialDocumentId().size() == 1;
    }

    public Integer getCountNumberOfDegreeChanges() {
        int numberOfDegreeChanges = 0;

        if (student == null) {
            return 0;
        }

        List<Registration> registrations = new ArrayList<Registration>(student.getRegistrationsSet());
        Collections.sort(registrations, Registration.COMPARATOR_BY_START_DATE);
        for (final Registration iter : registrations) {
            final SortedSet<RegistrationState> states = new TreeSet<RegistrationState>(RegistrationState.DATE_COMPARATOR);
            states.addAll(iter.getRegistrationStates(RegistrationStateType.INTERNAL_ABANDON));
            if (!states.isEmpty()) {
                numberOfDegreeChanges++;
            }
        }

        return numberOfDegreeChanges;
    }

    public boolean hasMadeDegreeChange() {
        if (getRegistration() == null) {
            return false;
        }

        return getRegistration().getStartExecutionYear() == getForExecutionYear()
                && getRegistration().getIngressionType().isInternalDegreeChange();
    }

    public Boolean getHasMadeDegreeChange() {
        return hasMadeDegreeChange();
    }

    public String getFirstRegistrationExecutionYear() {
        if (getRegistration() == null) {
            return "";
        }

        return ((Integer) getRegistration().getStudent().getFirstRegistrationExecutionYear().getBeginCivilYear()).toString();
    }

    public Integer getCountNumberOfEnrolmentsYearsSinceRegistrationStart() {
        if (getRegistration() == null) {
            return null;
        }

        return getRegistration().getEnrolmentsExecutionYears().size();
    }

    public Integer getCountNumberOfEnrolmentsYearsInIntegralRegime() {
        if (getRegistration() == null) {
            return null;
        }

        Collection<ExecutionYear> registrationExecutionYears = getRegistration().getEnrolmentsExecutionYears();
        int count = 0;
        for (ExecutionYear executionYear : registrationExecutionYears) {
            RegistrationRegimeType regimeType = getRegistration().getRegimeType(executionYear);

            count += (RegistrationRegimeType.FULL_TIME.equals(regimeType)) ? 1 : 0;
        }

        return count;
    }

    public Integer getCurricularYearOneYearAgo() {
        ExecutionYear oneYearAgo = getForExecutionYear().getPreviousExecutionYear();

        if (student == null) {
            return 0;
        }

        Registration lastRegistration = student.getLastActiveRegistration();

        if (lastRegistration == null) {
            return null;
        }

        if (lastRegistration.getDegreeType().isBolonhaDegree()) {
            return lastRegistration.getCurriculum(new DateTime(), oneYearAgo, CycleType.FIRST_CYCLE).getCurricularYear();
        } else if (lastRegistration.getDegreeType().isIntegratedMasterDegree()) {
            if (lastRegistration.hasConcludedFirstCycle()) {
                return lastRegistration.getCurricularYear(oneYearAgo);
            } else {
                if (lastRegistration.getLastStudentCurricularPlan().getCycle(CycleType.FIRST_CYCLE) != null) {
                    return lastRegistration.getCurriculum(new DateTime(), oneYearAgo, CycleType.FIRST_CYCLE).getCurricularYear();
                } else {
                    return lastRegistration.getCurriculum(new DateTime(), oneYearAgo, CycleType.SECOND_CYCLE).getCurricularYear();
                }
            }
        } else if (lastRegistration.getDegreeType().isBolonhaMasterDegree()) {
            return lastRegistration.getCurricularYear(oneYearAgo);
        }

        return lastRegistration.getCurricularYear(oneYearAgo);
    }

    public Integer getCurricularYearInCurrentYear() {
        ExecutionYear currentYear = getForExecutionYear();

        if (student == null) {
            return 0;
        }

        Registration lastRegistration = student.getLastActiveRegistration();

        if (lastRegistration == null) {
            return null;
        }

        if (lastRegistration.getDegreeType().isBolonhaDegree()) {
            return lastRegistration.getCurriculum(new DateTime(), currentYear, CycleType.FIRST_CYCLE).getCurricularYear();
        } else if (lastRegistration.getDegreeType().isIntegratedMasterDegree()) {
            if (lastRegistration.hasConcludedFirstCycle()) {
                return lastRegistration.getCurricularYear(currentYear);
            } else {
                if (lastRegistration.getLastStudentCurricularPlan().getCycle(CycleType.FIRST_CYCLE) != null) {
                    return lastRegistration.getCurriculum(new DateTime(), currentYear, CycleType.FIRST_CYCLE).getCurricularYear();
                } else {
                    return lastRegistration.getCurriculum(new DateTime(), currentYear, CycleType.SECOND_CYCLE)
                            .getCurricularYear();
                }
            }
        } else if (lastRegistration.getDegreeType().isBolonhaMasterDegree()) {
            return lastRegistration.getCurricularYear(currentYear);
        }

        return lastRegistration.getCurricularYear(currentYear);
    }

    public BigDecimal getNumberOfEnrolledEctsCurrentYear() {
        ExecutionYear currentYear = getForExecutionYear();

        return new BigDecimal(getRegistration().getEnrolmentsEcts(currentYear));
    }

    public BigDecimal getNumberOfEnrolledEctsOneYearAgo() {
        ExecutionYear oneYearAgo = getForExecutionYear().getPreviousExecutionYear();
        BigDecimal result = BigDecimal.ZERO;

        if (student == null) {
            return BigDecimal.ZERO;
        }

        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isBolonha() && registration.hasAnyEnrolmentsIn(oneYearAgo)) {
                result = result.add(new BigDecimal(getEnrolmentsEctsCredits(registration, oneYearAgo)));
            }
        }

        return result;
    }

    public BigDecimal getNumberOfApprovedEctsOneYearAgo() {
        ExecutionYear oneYearAgo = getForExecutionYear().getPreviousExecutionYear();
        BigDecimal result = BigDecimal.ZERO;

        if (student == null) {
            return BigDecimal.ZERO;
        }

        for (final Registration registration : student.getRegistrationsSet()) {

            if (registration.isBolonha() && registration.hasAnyEnrolmentsIn(oneYearAgo)) {
                result =
                        result.add(
                                calculateApprovedECTS(registration.getLastStudentCurricularPlan()
                                        .getAprovedEnrolmentsInExecutionPeriod(oneYearAgo.getFirstExecutionPeriod()))).add(
                                calculateApprovedECTS(registration.getLastStudentCurricularPlan()
                                        .getAprovedEnrolmentsInExecutionPeriod(oneYearAgo.getLastExecutionPeriod())));
            }
        }

        return result;
    }

    private double getEnrolmentsEctsCredits(final Registration registration, final ExecutionYear executionYear) {
        double result = 0.0;
        double annualCredits = 0.0;

        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (final Enrolment enrolment : registration.getLastStudentCurricularPlan().getEnrolmentsSet()) {
                if (enrolment.isValid(executionSemester)) {
                    if (enrolment.isAnual()) {
                        this.enrolledInAnualCoursesLastYear = true;

                        if (executionSemester.getSemester() == 1) {
                            annualCredits += enrolment.getEctsCredits();
                        }
                        continue;
                    }

                    result += enrolment.getEctsCredits();
                }
            }
        }

        return result + annualCredits;
    }

    public ExecutionYear getForExecutionYear() {
        return forExecutionYear;
    }

    public void setForExecutionYear(ExecutionYear forExecutionYear) {
        this.forExecutionYear = forExecutionYear;
    }

    private void setBestMatchingStudent() {
        Person personByFullDocumentId = null;
        Person personByPartialDocumentId = null;

        Collection<Person> fullDocumentIdPersonsCollection = readPersonsByCompleteDocumentId();
        Collection<Person> partialDocumentIdPersonsCollection = readPersonsByPartialDocumentId();
        if (fullDocumentIdPersonsCollection.size() > 1) {
            this.multiplePersonsFound = true;
            return;
        }

        if (fullDocumentIdPersonsCollection.size() == 1) {
            personByFullDocumentId = fullDocumentIdPersonsCollection.iterator().next();
        }

        if (partialDocumentIdPersonsCollection.size() == 1) {
            personByPartialDocumentId = partialDocumentIdPersonsCollection.iterator().next();
        }
        /*
         * We couldnt found the person with full and partial document id. Search
         * with with and check if the names are equals
         */
        Student studentReadByNumber = this.studentNumber != null ? Student.readStudentByNumber(this.studentNumber) : null;
        Person personFoundByStudent = null;
        if (studentReadByNumber != null) {
            personFoundByStudent = studentReadByNumber.getPerson();
        }

        if (personByFullDocumentId != null && personFoundByStudent != null && personByFullDocumentId != personFoundByStudent) {
            this.multiplePersonsFound = true;
            return;
        } else if (personByPartialDocumentId != null && personFoundByStudent != null
                && personByPartialDocumentId != personFoundByStudent) {
            this.multiplePersonsFound = true;
            return;
        }

        if (personByFullDocumentId != null) {
            this.person = personByFullDocumentId;
            this.student = this.person.getStudent();
        } else if (personByPartialDocumentId != null) {
            this.person = personByPartialDocumentId;
            this.student = this.person.getStudent();
        } else if (personFoundByStudent != null) {
            this.person = personFoundByStudent;
            this.student = this.person.getStudent();
        }

    }

    private static BigDecimal calculateApprovedECTS(final Collection<Enrolment> list) {
        BigDecimal result = BigDecimal.ZERO;

        for (final CurriculumLine curriculumLine : list) {
            result = result.add(BigDecimal.valueOf(curriculumLine.getAprovedEctsCredits()));
        }

        return result;
    }

    public static Collection<ExecutionYear> getEnrolmentsExecutionYears(final Student student) {
        Set<ExecutionYear> executionYears = new HashSet<ExecutionYear>();
        for (final Registration registration : student.getRegistrationsSet()) {
            if (RegistrationStateType.CANCELED.equals(registration.getLastActiveState())) {
                continue;
            }

            if (RegistrationStateType.TRANSITION.equals(registration.getLastActiveState())) {
                continue;
            }

            executionYears.addAll(registration.getEnrolmentsExecutionYears());
        }

        return executionYears;
    }

    public String getObservations() {
        if (observations == null) {
            buildSystemObservations();
        }

        if (StringUtils.isEmpty(observations)) {
            return "";
        }

        return upperObservations + " | " + observations + ";";
    }

    private void buildSystemObservations() {
        StringBuilder observationsBuilder = new StringBuilder();

        if (!isPersonFound()) {
            appendPersonNotFound(observationsBuilder);
        }

        if (isMultiplePersonsFound()) {
            appendMultiplePersonsFound(observationsBuilder);
        }

        if (isPersonReadByPartialDocumentId()) {
            appendPersonReadByPartialDocumentId(observationsBuilder);
        }

        if (!isStudentFound()) {
            appendStudentNotFound(observationsBuilder);
            observations = observationsBuilder.toString();
            return;
        }

        if (!isStudentNumberDiffer()) {
            appendStudentNumberIsDifferent(observationsBuilder);
        }

        if (!isDocumentIdentificationEqual()) {
            appendIdentificationNotEqual(observationsBuilder);
        }

        if (!isNameEqual()) {
            appendNameIsNotEqual(observationsBuilder);
        }

        if (!isStudentActive()) {
            appendStudentIsNotActive(observationsBuilder);
            observations = observationsBuilder.toString();
            return;
        }

        // if (!isLastRegistrationEqualToSpecifiedDegree()) {
        // appendDegreeIsNotEqual(observationsBuilder);
        // }

        observations = observationsBuilder.toString();
    }

    protected void appendPersonReadByPartialDocumentId(StringBuilder observationsBuilder) {
        observationsBuilder
                .append(String.format("O aluno foi lido com o numero de identificacao '%s'. ", getPartialDocumentId()));
    }

    protected void appendStudentNumberIsDifferent(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("Os numeros de aluno diferem '%s' / '%s'. ", studentNumber, getStudent()
                .getNumber()));

    }

    protected void appendMultiplePersonsFound(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("Foram encontradas muitas pessoas com a identificacao %s. ", documentNumber));
    }

    protected void appendDegreeIsNotEqual(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("Os cursos diferem '%s' / '%s'. ", degreeName, getRegistration().getDegree()
                .getNameI18N().getContent()));
    }

    protected void appendNameIsNotEqual(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("Os nomes diferem '%s' / '%s'. ", studentName, getPerson().getName()));
    }

    protected void appendIdentificationNotEqual(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("Os documentos de identificacao diferem '%s' / '%s'. ", documentNumber,
                getPerson().getDocumentIdNumber()));
    }

    protected void appendStudentIsNotActive(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("O aluno nao tem nenhuma matricula activa. "));
    }

    protected void appendStudentNotFound(StringBuilder observationsBuilder) {
        observationsBuilder.append(String.format("O aluno com o numero %s nao foi encontrado. ", studentNumber));
    }

    protected void appendPersonNotFound(StringBuilder observationsBuilder) {
        observationsBuilder.append(String
                .format("O aluno não foi encontrado com o numero de aluno nem com o nº de identificação. "));
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public String getInstitutionName() {
        return "Universidade Técnica de Lisboa - " + Unit.getInstitutionName().getContent();
    }

    public String getCandidacyNumber() {
        return candidacyNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Boolean getStudentHadPerformanceLastYear() {
        BigDecimal numberOfApprovedEctsOneYearAgo = getNumberOfApprovedEctsOneYearAgo();
        BigDecimal numberOfEnrolledEctsOneYearAgo = getNumberOfEnrolledEctsOneYearAgo();

        if (numberOfEnrolledEctsOneYearAgo.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        BigDecimal average = numberOfApprovedEctsOneYearAgo.divide(numberOfEnrolledEctsOneYearAgo, RoundingMode.HALF_EVEN);
        average.setScale(2);

        return average.compareTo(new BigDecimal(0.5f)) >= 0;
    }

    public String getDegreeConclusionValue() {
        return "";
    }

    public String getFinalResultValue() {
        return "";
    }

    public Integer getNumberOfMonthsExecutionYear() {
        return 10;
    }

    public String getFirstMonthOfPayment() {
        return "Setembro";
    }

    public Boolean getOwnerOfCETQualification() {
        return isStudentFound() && STUDENTS_WITH_CET.contains(getStudent().getNumber());
    }

    public String getRegimeCode() {
        return "Não especificado";
    }

    public String getLastEnrolledExecutionYear() {
        if (getStudent() == null) {
            return "";
        }

        List<ExecutionYear> enrolmentsExecutionYears = new ArrayList<ExecutionYear>(getEnrolmentsExecutionYears(getStudent()));
        Collections.sort(enrolmentsExecutionYears, ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);

        ExecutionYear lastEnrolledExecutionYear = null;

        if (enrolmentsExecutionYears.isEmpty()) {
            return "";
        }

        if (enrolmentsExecutionYears.size() == 1
                && ExecutionYear.readCurrentExecutionYear().equals(enrolmentsExecutionYears.iterator().next())) {
            return "";
        } else if (ExecutionYear.readCurrentExecutionYear().equals(enrolmentsExecutionYears.iterator().next())) {
            lastEnrolledExecutionYear = enrolmentsExecutionYears.get(1);
        } else {
            lastEnrolledExecutionYear = enrolmentsExecutionYears.iterator().next();
        }

        return ((Integer) lastEnrolledExecutionYear.getBeginCivilYear()).toString();
    }

    public String getNif() {
        if (getPerson() == null) {
            return "";
        }

        return getPerson().getSocialSecurityNumber();
    }

    public boolean isAbleToReadAllValues() {
        try {
            getInstitutionCode();
            getInstitutionName();
            getCandidacyNumber();
            getStudentNumberForPrint();
            getStudentName();
            getDocumentTypeName();
            getDocumentNumber();
            getDegreeCode();
            getDegreeName();
            getDegreeTypeName();
            getCountNumberOfDegreeChanges();
            getHasMadeDegreeChange();
            getFirstEnrolmentOnCurrentExecutionYear();
            getRegime();
            getFirstRegistrationExecutionYear();
            getCountNumberOfEnrolmentsYearsSinceRegistrationStart();
            getNumberOfDegreeCurricularYears();
            getNumberOfDoneECTS();
            getCurricularYearOneYearAgo();
            getNumberOfEnrolledEctsOneYearAgo();
            getNumberOfApprovedEctsOneYearAgo();
            getCurricularYearInCurrentYear();
            getNumberOfEnrolledECTS();
            getGratuityAmount();
            getNumberOfMonthsExecutionYear();
            getFirstMonthOfPayment();
            getOwnerOfCETQualification();
            isDegreeQualificationOwner();
            isMasterQualificationOwner();
            isPhdQualificationOwner();
            isOwnerOfCollegeQualification();
            getObservations();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    public boolean fillWithStudent(final ExecutionYear forExecutionYear, Student student) {
        this.forExecutionYear = forExecutionYear;

        try {
            this.institutionCode = getDefaultInstitutionCode();
            this.institutionName = getDefaultInstitutionName();
            this.candidacyNumber = "";

            this.studentNumber = student.getNumber();

            this.studentName = student.getPerson().getName();
            this.documentTypeName = student.getPerson().getIdDocumentType().getLocalizedName();
            this.documentNumber = student.getPerson().getDocumentIdNumber();
            Registration activeRegistration = getActiveRegistration(student);

            this.degreeCode = activeRegistration.getDegree().getMinistryCode();

            if ("9999".equals(this.degreeCode)) {
                this.degreeCode = "";
            }

            this.degreeName = activeRegistration.getDegree().getNameI18N().getContent();
            this.degreeTypeName = activeRegistration.getDegree().getDegreeTypeName();

            this.upperObservations = "";

            this.person = student.getPerson();
            this.student = student;
            enrolledInAnualCoursesLastYear = false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    private String getDefaultInstitutionName() {
        return MessageFormat
                .format(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.org.fenixedu.academic.ui.struts.action.administrativeOffice.scholarship.utl.report.ReportStudentsUTLCandidates.defaultInstitutionName"),
                        Unit.getInstitutionName().getContent());
    }

    private String getDefaultInstitutionCode() {
        return BundleUtil
                .getString(
                        Bundle.ACADEMIC,
                        "label.org.fenixedu.academic.ui.struts.action.administrativeOffice.scholarship.utl.report.ReportStudentsUTLCandidates.defaultInstitutionCode");
    }

    private Registration getActiveRegistration(Student student) {
        return student.getActiveRegistrationStream()
            .filter(r -> r.getDegree().getDegreeType().isEmpty())
            .findAny().orElse(student.getLastRegistration());
    }

}
