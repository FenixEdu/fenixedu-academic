package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class IndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object current) {
	List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();
	
	CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
	IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();
	
	
	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_DOCUMENT) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_DOCUMENT);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.HANDICAP_PROOF_DOCUMENT) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.HANDICAP_PROOF_DOCUMENT);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
	}

	if(individualCandidacyProcess.getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
	    fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	}
	
	fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);
	fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	
	return fileTypesList;
    }

}
