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
package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import pt.ist.fenixframework.Atomic;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SibsPaymentFileProcessReport extends SibsPaymentFileProcessReport_Base {

    static final public Comparator<SibsPaymentFileProcessReport> COMPARATOR_BY_SIBS_PROCESS_DATE =
            Comparator.comparing(SibsPaymentFileProcessReport::getWhenProcessedBySibs);

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
        super.setSpecialSeasonEnrolmentEventAmount(sibsPaymentFileProcessReportDTO.getSpecialSeasonEnrolmentEventAmount());
    }

    private void checkRulesToCreate(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {
        if (readBy(sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs(),
                sibsPaymentFileProcessReportDTO.getFileVersion()) != null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.SibsPaymentFileProcessReport.file.already.processed");
        }

    }

    private void checkParameters(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {

        if (sibsPaymentFileProcessReportDTO.getFilename() == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.SibsPaymentFileProcessReport.filename.cannot.be.null");
        }

        if (sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs() == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.SibsPaymentFileProcessReport.whenProcessedBySibs.cannot.be.null");
        }

        if (sibsPaymentFileProcessReportDTO.getFileVersion() == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.SibsPaymentFileProcessReport.fileVersion.cannot.be.null");
        }
    }

    static public SibsPaymentFileProcessReport readBy(final YearMonthDay date, final Integer fileVersion) {
        return Bennu.getInstance().getSibsPaymentFileProcessReportsSet().stream()
                .filter(sibsPaymentFileProcessReport -> sibsPaymentFileProcessReport.getWhenProcessedBySibs().isEqual(date))
                .filter(sibsPaymentFileProcessReport -> sibsPaymentFileProcessReport.getFileVersion().equals(fileVersion))
                .findFirst().orElse(null);
    }

    static public boolean hasAny(final YearMonthDay date, final Integer fileVersion) {
        return readBy(date, fileVersion) != null;
    }

    static public List<SibsPaymentFileProcessReport> readAllBetween(final YearMonthDay startDate, final YearMonthDay endDate) {
        return readAllBetween(startDate.toLocalDate(), endDate.toLocalDate());
    }

    static public List<SibsPaymentFileProcessReport> readAllBetween(final LocalDate startDate, final LocalDate endDate) {
        return Bennu.getInstance().getSibsPaymentFileProcessReportsSet().stream()
                .filter(report -> report.getWhenProcessedBySibs().compareTo(startDate) >= 0)
                .filter(report -> report.getWhenProcessedBySibs().compareTo(endDate) <= 0)
                .collect(Collectors.toList());

    }

    @Atomic
    static public SibsPaymentFileProcessReport create(SibsPaymentFileProcessReportDTO reportDTO) {
        return new SibsPaymentFileProcessReport(reportDTO);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
