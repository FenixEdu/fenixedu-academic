package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

abstract public class IndividualCandidacyProcess extends IndividualCandidacyProcess_Base {

    static final public Comparator<IndividualCandidacyProcess> COMPARATOR_BY_CANDIDACY_PERSON = new Comparator<IndividualCandidacyProcess>() {
	public int compare(IndividualCandidacyProcess o1, IndividualCandidacyProcess o2) {
	    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(o1.getPersonalDetails(), o2
		    .getPersonalDetails());
	}
    };

    protected IndividualCandidacyProcess() {
	super();
	setAccessHash(RandomStringGenerator.getRandomStringGenerator(16));
    }

    /**
     * This method is a refactor of IndividualCandidacyProcess subclasses
     * initialization The initialization is composed by parameters checking,
     * IndividualCandidacy creation and candidacy information setting
     * 
     * @param bean
     */
    protected void init(IndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	createIndividualCandidacy(bean);

	/*
	 * 11/04/2009 - An external candidacy submission requires documents like
	 * identification and habilitation certificate documents
	 */
	setCandidacyDocumentFiles(bean);
	setCandidacyHashCode(bean.getPublicCandidacyHashCode());

	getCandidacy().editCandidacyInformation(bean.getCandidacyInformationBean());
    }

    private void setCandidacyDocumentFiles(IndividualCandidacyProcessBean bean) {
	if(bean.getDocumentIdentificationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getDocumentIdentificationDocument());
	
	if(bean.getFirstCycleAccessHabilitationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getFirstCycleAccessHabilitationDocument());
	
	if(bean.getHabilitationCertificationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getHabilitationCertificationDocument());
	
	if(bean.getPaymentDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getPaymentDocument());
	
	if(bean.getVatCatCopyDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getVatCatCopyDocument());
	
	if(bean.getPhotoDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getPhotoDocument());
    }

    protected abstract void checkParameters(CandidacyProcess process);

    /**
     * Each type of CandidacyProcess has its own IndividualCandidacy. Each
     * subclass must implement this method that will instantiate the specific
     * IndividualCandidacy
     * 
     * @param bean
     */
    protected abstract void createIndividualCandidacy(IndividualCandidacyProcessBean bean);

    public ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return getCandidacyExecutionInterval() == executionInterval;
    }

    public DateTime getCandidacyStart() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyStart() : null;
    }

    public DateTime getCandidacyEnd() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyEnd() : null;
    }

    public LocalDate getCandidacyDate() {
	return getCandidacy().getCandidacyDate();
    }

    public boolean hasOpenCandidacyPeriod() {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod(date);
    }

    public CandidacyProcessState getState() {
	return hasCandidacyProcess() ? getCandidacyProcess().getState() : null;
    }

    public boolean isInStandBy() {
	return hasCandidacyProcess() && getCandidacyProcess().isInStandBy();
    }

    public boolean isSentToJury() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToJury();
    }

    public boolean isSentToCoordinator() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToCoordinator();
    }

    public boolean isSentToScientificCouncil() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToScientificCouncil();
    }

    public boolean isPublished() {
	return hasCandidacyProcess() && getCandidacyProcess().isPublished();
    }

    protected boolean hasAnyPaymentForCandidacy() {
	return getCandidacy().hasAnyPayment();
    }

    protected void cancelCandidacy(final Person person) {
	getCandidacy().cancel(person);
    }

    public IndividualCandidacyState getCandidacyState() {
	return getCandidacy().getState();
    }

    public boolean isCandidacyValid() {
	return !isCandidacyCancelled() && isCandidacyDebtPayed();
    }

    public boolean isCandidacyInStandBy() {
	return getCandidacy().isInStandBy();
    }

    public boolean isCandidacyAccepted() {
	return getCandidacy().isAccepted();
    }

    public boolean isCandidacyRejected() {
	return getCandidacy().isRejected();
    }

    public boolean isCandidacyCancelled() {
	return getCandidacy().isCancelled();
    }

    public boolean isCandidacyDebtPayed() {
	return getCandidacy().isDebtPayed();
    }

    public IndividualCandidacyPersonalDetails getPersonalDetails() {
	return getCandidacy().getPersonalDetails();
    }

    public boolean hasCandidacyPerson() {
	return getPersonalDetails() != null;
    }

    public Student getCandidacyStudent() {
	return getPersonalDetails().getStudent();
    }

    public boolean hasCandidacyStudent() {
	return getCandidacyStudent() != null;
    }

    public Registration getCandidacyRegistration() {
	return getCandidacy().getRegistration();
    }

    public boolean hasRegistrationForCandidacy() {
	return getCandidacy().hasRegistration();
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources").getString(getClass().getSimpleName());
    }

    protected void editPersonalCandidacyInformation(final PersonBean personBean) {
	getCandidacy().editPersonalCandidacyInformation(personBean);
    }
    
    public CandidacyInformationBean getCandidacyInformationBean() {
	return hasCandidacy() ? getCandidacy().getCandidacyInformationBean() : null;
    }

    protected void editCommonCandidacyInformation(final CandidacyInformationBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

    /**
     * Find an individual candidacy process given a kind of candidacy process,
     * an email and an access hash
     */
    public static IndividualCandidacyProcess findIndividualCandidacyProcess(
	    Class<? extends IndividualCandidacyProcess> individualCandidacyProcessClass, String email, String accessHash) {
	if (StringUtils.isEmpty(email) || StringUtils.isEmpty(accessHash)) {
	    return null;
	}

	Set<IndividualCandidacyProcess> candidacies = RootDomainObject.readAllDomainObjects(individualCandidacyProcessClass);

	for (IndividualCandidacyProcess individualCandidacyProcess : candidacies) {
	    if (email.equals(individualCandidacyProcess.getPersonalDetails().getEmail())
		    && accessHash.equals(individualCandidacyProcess.getAccessHash())) {
		return individualCandidacyProcess;
	    }
	}

	return null;
    }
    
    public IndividualCandidacyDocumentFile getPhoto() {
	IndividualCandidacyDocumentFile photo = getFileForType(DocumentFileType.PHOTO);
	return photo;
    }
    
    private IndividualCandidacyDocumentFile getFileForType(DocumentFileType type) {
	for (IndividualCandidacyDocumentFile document : this.getCandidacy().getDocuments()) {
	    if (document.getCandidacyFileType().equals(type))
		return document;
	}

	return null;
    }
    
    
    private static final int MAX_FILE_SIZE = 30000000;
    public void bindIndividualCandidacyDocumentFile(CandidacyProcessDocumentUploadBean uploadBean) {
	DocumentFileType type = uploadBean.getType();
	String fileName = uploadBean.getFileName();
	long fileSize = uploadBean.getFileSize();
	InputStream stream = uploadBean.getStream();
	
	byte[] contents = null;
	if(uploadBean.getContents() != null) {
	    contents = uploadBean.getContents();
	} else if (stream != null && fileSize > 0) {
	    contents = new byte[(int) fileSize];
	    try {
		stream.read(contents);
	    } catch (IOException exception) {
		throw new DomainException("error.read.input.stream.candidacy");
	    }
	    
	}
	
	if(fileSize > MAX_FILE_SIZE) {
	    throw new DomainException("File too big");
	}
	
	if(contents != null) {
	    new IndividualCandidacyDocumentFile(type, getCandidacy(), contents, fileName);
	}
    }
    
    public List<DocumentFileType> getMissingRequiredDocumentFiles() {
	List<DocumentFileType> missingDocumentFiles = new ArrayList<DocumentFileType>();
	
	if(getFileForType(DocumentFileType.CV_DOCUMENT) == null) {
	    missingDocumentFiles.add(DocumentFileType.CV_DOCUMENT);
	}
	
	if(getFileForType(DocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
	    missingDocumentFiles.add(DocumentFileType.DOCUMENT_IDENTIFICATION);
	}
	
	if(getFileForType(DocumentFileType.PAYMENT_DOCUMENT) == null) {
	    missingDocumentFiles.add(DocumentFileType.PAYMENT_DOCUMENT);
	}
	
	if(getFileForType(DocumentFileType.VAT_CARD_DOCUMENT) == null) {
	    missingDocumentFiles.add(DocumentFileType.VAT_CARD_DOCUMENT);
	}
	
	return missingDocumentFiles;
    }
    
    public Boolean getAllRequiredFilesUploaded() {
	return getMissingRequiredDocumentFiles().isEmpty();
    }
}
