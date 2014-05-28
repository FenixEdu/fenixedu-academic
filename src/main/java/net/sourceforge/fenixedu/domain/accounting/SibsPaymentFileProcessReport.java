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
package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class SibsPaymentFileProcessReport extends SibsPaymentFileProcessReport_Base {

    static final public Comparator<SibsPaymentFileProcessReport> COMPARATOR_BY_SIBS_PROCESS_DATE =
            new Comparator<SibsPaymentFileProcessReport>() {
                @Override
                public int compare(SibsPaymentFileProcessReport o1, SibsPaymentFileProcessReport o2) {
                    return o1.getWhenProcessedBySibs().compareTo(o2.getWhenProcessedBySibs());
                }
            };

    public SibsPaymentFileProcessReport(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {
        super();
        init(sibsPaymentFileProcessReportDTO);

    }

    private void init(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {

        checkParameters(sibsPaymentFileProcessReportDTO);
        checkRulesToCreate(sibsPaymentFileProcessReportDTO);

        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenProcessed(new DateTime());
        super.setFilename(sibsPaymentFileProcessReportDTO.getFilename());
        super.setWhenProcessedBySibs(sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs());
        super.setFileVersion(sibsPaymentFileProcessReportDTO.getFileVersion());
        super.setDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getDegreeGratuityTotalAmount());
        super.setBolonhaDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getBolonhaDegreeGratuityTotalAmount());
        super.setIntegratedMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
                .getIntegratedMasterDegreeGratuityTotalAmount());
        super.setIntegratedBolonhaMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
                .getIntegratedBolonhaMasterDegreeGratuityTotalAmount());
        super.setAdministrativeOfficeTaxTotalAmount(sibsPaymentFileProcessReportDTO.getAdministrativeOfficeTaxTotalAmount());
        super.setGraduationInsuranceTotalAmount(sibsPaymentFileProcessReportDTO.getGraduationInsuranceTotalAmount());
        super.setSpecializationGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getSpecializationGratuityTotalAmount());
        super.setBolonhaMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
                .getBolonhaMasterDegreeGratuityTotalAmount());
        super.setMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getMasterDegreeGratuityTotalAmount());
        super.setDfaGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getDfaGratuityTotalAmount());
        super.setAfterGraduationInsuranceTotalAmount(sibsPaymentFileProcessReportDTO.getAfterGraduationInsuranceTotalAmount());
        super.setPhdGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getPhdGratuityTotalAmout());
        super.setTransactionsTotalAmount(sibsPaymentFileProcessReportDTO.getTransactionsTotalAmount());
        super.setTotalCost(sibsPaymentFileProcessReportDTO.getTotalCost());
        super.setResidencePayment(sibsPaymentFileProcessReportDTO.getResidenceAmount());
        super.setDegreeCandidacyForGraduatedPersonAmount(sibsPaymentFileProcessReportDTO
                .getDegreeCandidacyForGraduatedPersonAmount());
        super.setDegreeChangeIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO.getDegreeChangeIndividualCandidacyAmount());
        super.setDegreeTransferIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO
                .getDegreeTransferIndividualCandidacyAmount());
        super.setSecondCycleIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO.getSecondCycleIndividualCandidacyAmount());
        super.setStandaloneEnrolmentGratuityEventAmount(sibsPaymentFileProcessReportDTO
                .getStandaloneEnrolmentGratuityEventAmount());
        super.setOver23IndividualCandidacyEventAmount(sibsPaymentFileProcessReportDTO.getOver23IndividualCandidacyEventAmount());
        super.setInstitutionAffiliationEventAmount(sibsPaymentFileProcessReportDTO.getInstitutionAffiliationEventAmount());
        super.setPhdProgramCandidacyEventAmount(sibsPaymentFileProcessReportDTO.getPhdProgramCandidacyEventAmount());
        super.setRectorateAmount(sibsPaymentFileProcessReportDTO.getRectorateAmount());
    }

    private void checkRulesToCreate(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {
        if (SibsPaymentFileProcessReport.readBy(sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs(),
                sibsPaymentFileProcessReportDTO.getFileVersion()) != null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport.file.already.processed");
        }

    }

    private void checkParameters(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {

        if (sibsPaymentFileProcessReportDTO.getFilename() == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport.filename.cannot.be.null");
        }

        if (sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs() == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport.whenProcessedBySibs.cannot.be.null");
        }

        if (sibsPaymentFileProcessReportDTO.getFileVersion() == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport.fileVersion.cannot.be.null");
        }
    }

    static public SibsPaymentFileProcessReport readBy(final YearMonthDay date, final Integer fileVersion) {
        for (final SibsPaymentFileProcessReport sibsPaymentFileProcessReport : Bennu.getInstance()
                .getSibsPaymentFileProcessReportsSet()) {
            if (sibsPaymentFileProcessReport.getWhenProcessedBySibs().isEqual(date)
                    && sibsPaymentFileProcessReport.getFileVersion().equals(fileVersion)) {
                return sibsPaymentFileProcessReport;
            }
        }

        return null;
    }

    static public boolean hasAny(final YearMonthDay date, final Integer fileVersion) {
        return readBy(date, fileVersion) != null;
    }

    static public List<SibsPaymentFileProcessReport> readAllBetween(final YearMonthDay startDate, final YearMonthDay endDate) {
        return readAllBetween(startDate.toLocalDate(), endDate.toLocalDate());
    }

    static public List<SibsPaymentFileProcessReport> readAllBetween(final LocalDate startDate, final LocalDate endDate) {
        final List<SibsPaymentFileProcessReport> result = new ArrayList<SibsPaymentFileProcessReport>();
        for (final SibsPaymentFileProcessReport report : Bennu.getInstance().getSibsPaymentFileProcessReportsSet()) {
            if (report.getWhenProcessedBySibs().compareTo(startDate) >= 0
                    && report.getWhenProcessedBySibs().compareTo(endDate) <= 0) {
                result.add(report);
            }
        }
        return result;

    }

    @Atomic
    static public SibsPaymentFileProcessReport create(SibsPaymentFileProcessReportDTO reportDTO) {
        return new SibsPaymentFileProcessReport(reportDTO);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDegreeGratuityTotalAmount() {
        return getDegreeGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasTotalCost() {
        return getTotalCost() != null;
    }

    @Deprecated
    public boolean hasIntegratedBolonhaMasterDegreeGratuityTotalAmount() {
        return getIntegratedBolonhaMasterDegreeGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasFileVersion() {
        return getFileVersion() != null;
    }

    @Deprecated
    public boolean hasFilename() {
        return getFilename() != null;
    }

    @Deprecated
    public boolean hasRectorateAmount() {
        return getRectorateAmount() != null;
    }

    @Deprecated
    public boolean hasAfterGraduationInsuranceTotalAmount() {
        return getAfterGraduationInsuranceTotalAmount() != null;
    }

    @Deprecated
    public boolean hasDfaGratuityTotalAmount() {
        return getDfaGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasSecondCycleIndividualCandidacyAmount() {
        return getSecondCycleIndividualCandidacyAmount() != null;
    }

    @Deprecated
    public boolean hasGraduationInsuranceTotalAmount() {
        return getGraduationInsuranceTotalAmount() != null;
    }

    @Deprecated
    public boolean hasPhdProgramCandidacyEventAmount() {
        return getPhdProgramCandidacyEventAmount() != null;
    }

    @Deprecated
    public boolean hasOver23IndividualCandidacyEventAmount() {
        return getOver23IndividualCandidacyEventAmount() != null;
    }

    @Deprecated
    public boolean hasResidencePayment() {
        return getResidencePayment() != null;
    }

    @Deprecated
    public boolean hasTransactionsTotalAmount() {
        return getTransactionsTotalAmount() != null;
    }

    @Deprecated
    public boolean hasDegreeCandidacyForGraduatedPersonAmount() {
        return getDegreeCandidacyForGraduatedPersonAmount() != null;
    }

    @Deprecated
    public boolean hasWhenProcessedBySibs() {
        return getWhenProcessedBySibs() != null;
    }

    @Deprecated
    public boolean hasDegreeTransferIndividualCandidacyAmount() {
        return getDegreeTransferIndividualCandidacyAmount() != null;
    }

    @Deprecated
    public boolean hasBolonhaDegreeGratuityTotalAmount() {
        return getBolonhaDegreeGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasWhenProcessed() {
        return getWhenProcessed() != null;
    }

    @Deprecated
    public boolean hasInstitutionAffiliationEventAmount() {
        return getInstitutionAffiliationEventAmount() != null;
    }

    @Deprecated
    public boolean hasSpecializationGratuityTotalAmount() {
        return getSpecializationGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasAdministrativeOfficeTaxTotalAmount() {
        return getAdministrativeOfficeTaxTotalAmount() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeGratuityTotalAmount() {
        return getMasterDegreeGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasPhdGratuityTotalAmount() {
        return getPhdGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasStandaloneEnrolmentGratuityEventAmount() {
        return getStandaloneEnrolmentGratuityEventAmount() != null;
    }

    @Deprecated
    public boolean hasDegreeChangeIndividualCandidacyAmount() {
        return getDegreeChangeIndividualCandidacyAmount() != null;
    }

    @Deprecated
    public boolean hasIntegratedMasterDegreeGratuityTotalAmount() {
        return getIntegratedMasterDegreeGratuityTotalAmount() != null;
    }

    @Deprecated
    public boolean hasBolonhaMasterDegreeGratuityTotalAmount() {
        return getBolonhaMasterDegreeGratuityTotalAmount() != null;
    }

}
