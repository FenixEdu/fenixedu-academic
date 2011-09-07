package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class SibsPaymentFileProcessReport extends SibsPaymentFileProcessReport_Base {

    static final public Comparator<SibsPaymentFileProcessReport> COMPARATOR_BY_SIBS_PROCESS_DATE = new Comparator<SibsPaymentFileProcessReport>() {
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

	super.setRootDomainObject(RootDomainObject.getInstance());
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
	super
	.setDegreeChangeIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO
		.getDegreeChangeIndividualCandidacyAmount());
	super.setDegreeTransferIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO
		.getDegreeTransferIndividualCandidacyAmount());
	super.setSecondCycleIndividualCandidacyAmount(sibsPaymentFileProcessReportDTO.getSecondCycleIndividualCandidacyAmount());
	super.setStandaloneEnrolmentGratuityEventAmount(sibsPaymentFileProcessReportDTO
		.getStandaloneEnrolmentGratuityEventAmount());
	super.setOver23IndividualCandidacyEventAmount(sibsPaymentFileProcessReportDTO.getOver23IndividualCandidacyEventAmount());
	super.setInstitutionAffiliationEventAmount(sibsPaymentFileProcessReportDTO.getInstitutionAffiliationEventAmount());
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
	for (final SibsPaymentFileProcessReport sibsPaymentFileProcessReport : RootDomainObject.getInstance()
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
	for (final SibsPaymentFileProcessReport report : RootDomainObject.getInstance().getSibsPaymentFileProcessReportsSet()) {
	    if (report.getWhenProcessedBySibs().compareTo(startDate) >= 0
		    && report.getWhenProcessedBySibs().compareTo(endDate) <= 0) {
		result.add(report);
	    }
	}
	return result;

    }

    @Service
    static public SibsPaymentFileProcessReport create(SibsPaymentFileProcessReportDTO reportDTO) {
	return new SibsPaymentFileProcessReport(reportDTO);
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
