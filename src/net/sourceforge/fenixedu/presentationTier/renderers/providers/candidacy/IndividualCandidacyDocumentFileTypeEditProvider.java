package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class IndividualCandidacyDocumentFileTypeEditProvider {

    public static class DegreeChangeOrTransferIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return null;
	}

	@Override
	public Object provide(Object source, Object current) {
	    List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

	    CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
	    IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
	    }

	    return fileTypesList;
	}
    }

    public static class DegreeCandidacyForGraduatedPersonIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Object provide(Object source, Object current) {
	    List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

	    CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
	    IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
	    }

	    fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

	    return fileTypesList;
	}

    }

    public static class SecondCycleIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Object provide(Object source, Object current) {
	    List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

	    CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
	    IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	    }

	    if (individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
		fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
	    }

	    fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

	    return fileTypesList;
	}

    }
}
