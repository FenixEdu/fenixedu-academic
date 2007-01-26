package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class SibsPaymentFileProcessReport extends SibsPaymentFileProcessReport_Base {

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
	super.setDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getDegreeGratuityTotalAmount());
	super.setBolonhaDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getBolonhaDegreeGratuityTotalAmount());
	super.setIntegratedMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getIntegratedMasterDegreeGratuityTotalAmount());
	super.setIntegratedBolonhaMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getIntegratedBolonhaMasterDegreeGratuityTotalAmount());
	super.setAdministrativeOfficeTaxTotalAmount(sibsPaymentFileProcessReportDTO
		.getAdministrativeOfficeTaxTotalAmount());
	super.setGraduationInsuranceTotalAmount(sibsPaymentFileProcessReportDTO
		.getGraduationInsuranceTotalAmount());
	super.setSpecializationGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getSpecializationGratuityTotalAmount());
	super.setBolonhaMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getBolonhaMasterDegreeGratuityTotalAmount());
	super.setMasterDegreeGratuityTotalAmount(sibsPaymentFileProcessReportDTO
		.getMasterDegreeGratuityTotalAmount());
	super.setDfaGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getDfaGratuityTotalAmount());
	super.setAfterGraduationInsuranceTotalAmount(sibsPaymentFileProcessReportDTO
		.getAfterGraduationInsuranceTotalAmount());
	super.setPhdGratuityTotalAmount(sibsPaymentFileProcessReportDTO.getPhdGratuityTotalAmout());
	super.setTransactionsTotalAmount(sibsPaymentFileProcessReportDTO.getTransactionsTotalAmount());
	super.setTotalCost(sibsPaymentFileProcessReportDTO.getTotalCost());

    }

    private void checkRulesToCreate(SibsPaymentFileProcessReportDTO sibsPaymentFileProcessReportDTO) {
	if (SibsPaymentFileProcessReport.readBy(
		sibsPaymentFileProcessReportDTO.getWhenProcessedBySibs(),
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

    public static SibsPaymentFileProcessReport readBy(YearMonthDay date, Integer fileVersion) {
	for (final SibsPaymentFileProcessReport sibsPaymentFileProcessReport : RootDomainObject
		.getInstance().getSibsPaymentFileProcessReportsSet()) {
	    if (sibsPaymentFileProcessReport.getWhenProcessedBySibs().isEqual(date)
		    && sibsPaymentFileProcessReport.getFileVersion().equals(fileVersion)) {
		return sibsPaymentFileProcessReport;
	    }
	}

	return null;
    }

    public static List<SibsPaymentFileProcessReport> readAllBetween(YearMonthDay startDate,
	    YearMonthDay endDate) {
	final List<SibsPaymentFileProcessReport> result = new ArrayList<SibsPaymentFileProcessReport>();
	for (final SibsPaymentFileProcessReport sibsPaymentFileProcessReport : RootDomainObject
		.getInstance().getSibsPaymentFileProcessReportsSet()) {
	    if (sibsPaymentFileProcessReport.getWhenProcessedBySibs().compareTo(startDate) >= 0
		    && sibsPaymentFileProcessReport.getWhenProcessedBySibs().compareTo(endDate) <= 0) {
		result.add(sibsPaymentFileProcessReport);
	    }
	}

	return result;

    }
}
