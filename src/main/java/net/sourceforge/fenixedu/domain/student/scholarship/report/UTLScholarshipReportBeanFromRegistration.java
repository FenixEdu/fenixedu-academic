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
package net.sourceforge.fenixedu.domain.student.scholarship.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationRegimeType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class UTLScholarshipReportBeanFromRegistration implements Serializable, IUTLScholarshipReportResult {

    public Registration registration;

    public UTLScholarshipReportBeanFromRegistration(final Registration registration) {
        this.registration = registration;
    }

    public static UTLScholarshipReportBeanFromRegistration create(final Registration registration) {
        return new UTLScholarshipReportBeanFromRegistration(registration);
    }

    @Override
    public String getInstitutionCode() {
        return IUTLScholarshipReportResult.INSTITUTION_CODE;
    }

    @Override
    public String getInstitutionName() {
        return MessageFormat.format(BundleUtil.getString(Bundle.ACADEMIC, "label.utl.scholarship.report.institution.name"), Unit
                .getInstitutionName().getContent());
    }

    @Override
    public String getApplicationNumber() {
        return "";
    }

    @Override
    public Integer getStudentNumber() {
        return registration.getNumber();
    }

    @Override
    public String getStudentName() {
        return registration.getPerson().getName();
    }

    @Override
    public String getIdDocumentType() {
        return registration.getPerson().getIdDocumentType().getLocalizedName();
    }

    @Override
    public String getIdDocumentNumber() {
        return registration.getPerson().getDocumentIdNumber();
    }

    @Override
    public String getDegreeCode() {
        String ministryCode = registration.getDegree().getMinistryCode();
        return ministryCode != null ? ministryCode : "";
    }

    @Override
    public String getDegreeName() {
        return registration.getDegree().getName();
    }

    @Override
    public String getDegreeTypeName() {
        switch (registration.getDegree().getDegreeType()) {
        case BOLONHA_DEGREE:
            return BOLONHA_DEGREE_DESIGNATION;
        case BOLONHA_INTEGRATED_MASTER_DEGREE:
            return INTEGRATED_MASTER_DESIGNATION;
        default:
            return registration.getDegree().getDegreeType().getLocalizedName();
        }
    }

    @Override
    public Integer getNumberOfDegreeChanges() {
        int numberOfDegreeChanges = 0;

        List<Registration> registrations = new ArrayList<Registration>(readStudent().getRegistrations());
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

    @Override
    public String getHasMadeDegreeChangeInThisExecutionYear() {
        boolean hasMade =
                registration.getStartExecutionYear() == readCurrentExecutionYear()
                        && Ingression.MCI.equals(registration.getIngression());

        return hasMade ? BundleUtil.getString(Bundle.ACADEMIC, "label.yes") : BundleUtil.getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public String getCurrentExecutionYearBeginDate() {
        if (registration.isInMobilityState()) {
            return readCurrentExecutionYear().getBeginDateYearMonthDay().toLocalDate().toString("dd-MM-yyyy");
        }

        StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();
        TreeSet<Enrolment> orderedEnrolmentSet =
                new TreeSet<Enrolment>(Collections.reverseOrder(CurriculumModule.COMPARATOR_BY_CREATION_DATE));
        orderedEnrolmentSet.addAll(lastStudentCurricularPlan.getEnrolmentsByExecutionYear(readCurrentExecutionYear()));

        return orderedEnrolmentSet.isEmpty() ? "" : orderedEnrolmentSet.iterator().next().getCreationDateDateTime().toLocalDate()
                .toString("dd-MM-yyyy");
    }

    @Override
    public String getRegimen() {
        RegistrationRegimeType type = registration.getRegimeType(readCurrentExecutionYear());

        if (RegistrationRegimeType.FULL_TIME.equals(type)) {
            return BundleUtil.getString(Bundle.ACADEMIC, "label.utl.scholarship.report.regimen.full");
        } else if (RegistrationRegimeType.PARTIAL_TIME.equals(type)) {
            return BundleUtil.getString(Bundle.ACADEMIC, "label.utl.scholarship.report.regimen.partial");
        }

        return "";

    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getFirstExecutionYearInIST() {
        return registration.getStudent().getFirstRegistrationExecutionYear().getName();
    }

    @Override
    public Integer getNumberOfStudyExecutionYearsInCurrentRegistration() {
        return registration.getEnrolmentsExecutionYears().size();
    }

    @Override
    public Integer getNumberOfCurricularYearsOnCurrentDegreeCurricularPlan() {
        return registration.getDegree().getDegreeType().getYears();
    }

    @Override
    public Integer getLastYearCurricularYear() {
        ExecutionYear oneYearAgo = readCurrentExecutionYear().getPreviousExecutionYear();
        Registration lastRegistration = readStudent().getLastActiveRegistration();

        if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
            return lastRegistration.getCurriculum(new DateTime(), oneYearAgo, CycleType.FIRST_CYCLE).getCurricularYear();
        } else if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
            if (lastRegistration.hasConcludedFirstCycle()) {
                return lastRegistration.getCurricularYear(oneYearAgo);
            } else {
                return lastRegistration.getCurriculum(new DateTime(), oneYearAgo, CycleType.FIRST_CYCLE).getCurricularYear();
            }
        } else if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)) {
            return lastRegistration.getCurricularYear(oneYearAgo);
        }

        return lastRegistration.getCurricularYear(oneYearAgo);
    }

    @Override
    public BigDecimal getLastYearEnrolledECTS() {
        ExecutionYear oneYearAgo = readCurrentExecutionYear().getPreviousExecutionYear();
        BigDecimal result = BigDecimal.ZERO;

        for (final Registration registration : readStudent().getRegistrationsSet()) {
            if (registration.isBolonha() && registration.hasAnyCurriculumLines(oneYearAgo)) {
                result = result.add(getEnrolmentsEctsCredits(registration, oneYearAgo));
            }
        }

        return result;
    }

    @Override
    public BigDecimal getLastYearApprovedECTS() {
        ExecutionYear oneYearAgo = readCurrentExecutionYear().getPreviousExecutionYear();
        BigDecimal result = BigDecimal.ZERO;

        for (final Registration registration : readStudent().getRegistrationsSet()) {

            if (registration.isBolonha() && registration.hasAnyCurriculumLines(oneYearAgo)) {
                result =
                        result.add(
                                calculateApprovedECTS(registration.getLastStudentCurricularPlan().getApprovedCurriculumLines(
                                        oneYearAgo.getFirstExecutionPeriod()))).add(
                                calculateApprovedECTS(registration.getLastStudentCurricularPlan().getApprovedCurriculumLines(
                                        oneYearAgo.getLastExecutionPeriod())));
            }
        }

        return result;
    }

    @Override
    public String getWasApprovedOnMostECTS() {
        BigDecimal lastYearEnrolledECTS = getLastYearEnrolledECTS().setScale(10, RoundingMode.HALF_EVEN);
        BigDecimal lastYearApprovedECTS = getLastYearApprovedECTS().setScale(10, RoundingMode.HALF_EVEN);

        if (lastYearEnrolledECTS.compareTo(new BigDecimal(0)) == 0) {
            return BundleUtil.getString(Bundle.ACADEMIC, "label.no");
        }

        BigDecimal ratio = lastYearApprovedECTS.divide(lastYearEnrolledECTS, RoundingMode.HALF_EVEN);
        return ratio.compareTo(new BigDecimal(0.5f)) == 1 ? BundleUtil.getString(Bundle.ACADEMIC, "label.yes") : BundleUtil
                .getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public Integer getCurrentYearCurricularYear() {
        ExecutionYear currentYear = readCurrentExecutionYear();
        Registration lastRegistration = readStudent().getLastActiveRegistration();

        if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
            return lastRegistration.getCurriculum(new DateTime(), currentYear, CycleType.FIRST_CYCLE).getCurricularYear();
        } else if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
            if (lastRegistration.hasConcludedFirstCycle()) {
                return lastRegistration.getCurricularYear(currentYear);
            } else {
                return lastRegistration.getCurriculum(new DateTime(), currentYear, CycleType.FIRST_CYCLE).getCurricularYear();
            }
        } else if (lastRegistration.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)) {
            return lastRegistration.getCurricularYear(currentYear);
        }

        return lastRegistration.getCurricularYear(currentYear);
    }

    @Override
    public BigDecimal getCurrentYearEnrolledECTS() {
        StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();
        return new BigDecimal(lastStudentCurricularPlan.getEnrolmentsEctsCredits(readCurrentExecutionYear()));
    }

    @Override
    public String getDegreeConcluded() {
        return "";
    }

    @Override
    public String getFinalResult() {
        return "";
    }

    @Override
    public Money getGratuityAmount() {
        if (!registration.hasToPayGratuityOrInsurance()) {
            return Money.ZERO;
        }

        StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();

        GratuityEventWithPaymentPlan event =
                lastStudentCurricularPlan.getGratuityEvent(readCurrentExecutionYear(), GratuityEventWithPaymentPlan.class);

        return event != null ? event.getOriginalAmountToPay() : Money.ZERO;
    }

    @Override
    public Integer getNumberOfMonthsInExecutionYear() {
        return 10;
    }

    @Override
    public String getFirstMonthToPay() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.utl.scholarship.report.first.month.to.pay");
    }

    @Override
    public String getIsCETQualificationOwner() {
        return STUDENTS_WITH_CET.contains(registration.getNumber()) ? BundleUtil.getString(Bundle.ACADEMIC, "label.yes") : BundleUtil
                .getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public String getIsDegreeQualificationOwner() {
        for (Qualification qualification : readStudent().getPerson().getAssociatedQualifications()) {
            if (isDegreeQualificationType(qualification.getType())) {
                return BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }
        }

        return BundleUtil.getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public String getIsMasterDegreeQualificationOwner() {
        for (Qualification qualification : registration.getPerson().getAssociatedQualifications()) {
            if (isMasterQualificationType(qualification.getType())) {
                return BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }
        }

        return BundleUtil.getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public String getIsPhdQualificationOwner() {
        for (Qualification qualification : registration.getPerson().getAssociatedQualifications()) {
            if (isPhdQualificationType(qualification.getType())) {
                BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }
        }

        return BundleUtil.getString(Bundle.ACADEMIC, "label.no");
    }

    @Override
    public String getIsOwnerOfQualification() {
        for (Qualification qualification : registration.getPerson().getAssociatedQualifications()) {
            if (isDegreeQualificationType(qualification.getType())) {
                return BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }

            if (isMasterQualificationType(qualification.getType())) {
                return BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }

            if (isPhdQualificationType(qualification.getType())) {
                BundleUtil.getString(Bundle.ACADEMIC, "label.yes");
            }
        }

        return BundleUtil.getString(Bundle.ACADEMIC, "label.no");

    }

    @Override
    public Integer getNumberOfEnrolmentsYearsSinceRegistrationStart() {
        return registration.getEnrolmentsExecutionYears().size();
    }

    @Override
    public String getObservations() {
        return "";
    }

    private ExecutionYear readCurrentExecutionYear() {
        return ExecutionYear.readCurrentExecutionYear();
    }

    private Student readStudent() {
        return registration.getStudent();
    }

    private static boolean isDegreeQualificationType(QualificationType type) {
        return DEGREE_QUALIFICATION_TYPES.contains(type);
    }

    private static boolean isMasterQualificationType(QualificationType type) {
        return MASTER_QUALIFICATION_TYPES.contains(type);
    }

    private static boolean isPhdQualificationType(QualificationType type) {
        return PHD_QUALIFICATION_TYPES.contains(type);
    }

    private BigDecimal getEnrolmentsEctsCredits(final Registration registration, final ExecutionYear executionYear) {
        BigDecimal result = new BigDecimal(0.0);
        BigDecimal annualCredits = new BigDecimal(0.0);

        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (final CurriculumLine enrolment : registration.getLastStudentCurricularPlan().getAllCurriculumLines()) {
                if (enrolment.isDismissal()) {
                    Dismissal dismissal = (Dismissal) enrolment;
                    if (!dismissal.getCredits().isAllEnrolmentsAreExternal()) {
                        continue;
                    }
                }

                if (enrolment.isValid(executionSemester)) {
                    if (enrolment.isEnrolment() && ((Enrolment) enrolment).isAnual()) {

                        if (executionSemester.getSemester() == 1) {
                            annualCredits = annualCredits.add(new BigDecimal(enrolment.getEctsCredits()));
                        }
                        continue;
                    }

                    result = result.add(new BigDecimal(enrolment.getEctsCredits()));
                }
            }
        }

        return result.add(annualCredits);
    }

    private static BigDecimal calculateApprovedECTS(final Collection<CurriculumLine> list) {
        BigDecimal result = BigDecimal.ZERO;

        for (final CurriculumLine curriculumLine : list) {
            if (curriculumLine.isDismissal()) {
                Dismissal dismissal = (Dismissal) curriculumLine;
                if (!dismissal.getCredits().isAllEnrolmentsAreExternal()) {
                    continue;
                }
            }

            result = result.add(BigDecimal.valueOf(curriculumLine.getAprovedEctsCredits()));
        }

        return result;
    }

    public Spreadsheet buildSpreadsheet() {
        Spreadsheet spreadsheet = new Spreadsheet("resultado da bolsa de accao social");

        setHeaders(spreadsheet);

        Row row = spreadsheet.addRow();

        row.setCell(getInstitutionCode());
        row.setCell(getInstitutionName());
        row.setCell(getApplicationNumber());
        row.setCell(getStudentNumber());
        row.setCell(getStudentName());
        row.setCell(getIdDocumentType());
        row.setCell(getIdDocumentNumber());
        row.setCell(getDegreeCode());
        row.setCell(getDegreeName());
        row.setCell(getDegreeTypeName());
        row.setCell(getNumberOfDegreeChanges());
        row.setCell(getHasMadeDegreeChangeInThisExecutionYear());
        row.setCell(getCurrentExecutionYearBeginDate());
        row.setCell(getRegimen());
        row.setCell(getCode());
        row.setCell(getFirstExecutionYearInIST());
        row.setCell(getNumberOfStudyExecutionYearsInCurrentRegistration());
        row.setCell(getNumberOfCurricularYearsOnCurrentDegreeCurricularPlan());
        row.setCell(getLastYearCurricularYear());
        row.setCell(getLastYearEnrolledECTS());
        row.setCell(getLastYearApprovedECTS());
        row.setCell(getWasApprovedOnMostECTS());
        row.setCell(getCurrentYearCurricularYear());
        row.setCell(getCurrentYearEnrolledECTS());
        row.setCell(getDegreeConcluded());
        row.setCell(getFinalResult());
        row.setCell(getGratuityAmount().toString());
        row.setCell(getNumberOfMonthsInExecutionYear());
        row.setCell(getFirstMonthToPay());
        row.setCell(getIsCETQualificationOwner());
        row.setCell(getIsDegreeQualificationOwner());
        row.setCell(getIsMasterDegreeQualificationOwner());
        row.setCell(getIsPhdQualificationOwner());
        row.setCell(getIsOwnerOfQualification());
        row.setCell(getObservations());

        return spreadsheet;
    }

    protected void setHeaders(Spreadsheet spreadsheet) {
        spreadsheet
                .setHeader(
                        0,
                        BundleUtil
                                .getString(Bundle.ACADEMIC,
                                        "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.institutionCode"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.institutionName"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.applicationNumber"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.studentNumber"));

        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.studentName"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.idDocumentType"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.idDocumentNumber"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.degreeCode"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.degreeName"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.degreeTypeName"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.numberOfDegreeChanges"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.hasMadeDegreeChangeInThisExecutionYear"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.currentExecutionYearBeginDate"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.regimen"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.code"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.firstExecutionYearInIST"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.numberOfStudyExecutionYearsInCurrentRegistration"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.numberOfCurricularYearsOnCurrentDegreeCurricularPlan"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.lastYearCurricularYear"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.lastYearEnrolledECTS"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.lastYearApprovedECTS"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.wasApprovedOnMostECTS"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.currentYearCurricularYear"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.currentYearEnrolledECTS"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.degreeConcluded"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.finalResult"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.gratuityAmount"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.numberOfMonthsInExecutionYear"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.firstMonthToPay"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.isCETQualificationOwner"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.isDegreeQualificationOwner"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.isMasterDegreeQualificationOwner"));
        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.isPhdQualificationOwner"));

        spreadsheet
                .setHeader(BundleUtil
                        .getString(
                                Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.isOwnerOfQualification"));

        spreadsheet
                .setHeader(BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration.observations"));
    }

}
