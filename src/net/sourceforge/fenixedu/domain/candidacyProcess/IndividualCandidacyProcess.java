package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
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
	setProcessChecked(Boolean.FALSE);
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
	setProcessCodeForThisIndividualCandidacy(bean.getCandidacyProcess());
    }

    private void setProcessCodeForThisIndividualCandidacy(CandidacyProcess process) {
	CandidacyPeriod period = process.getCandidacyPeriod();
	String beginExecutionYear = String.valueOf(
		period.getExecutionInterval().getBeginDateYearMonthDay().get(DateTimeFieldType.year())).substring(2, 4);
	String endExecutionYear = String.valueOf(
		period.getExecutionInterval().getEndDateYearMonthDay().get(DateTimeFieldType.year())).substring(2, 4);
	setProcessCode(beginExecutionYear + endExecutionYear + getIdInternal());
    }

    private void setCandidacyDocumentFiles(IndividualCandidacyProcessBean bean) {
	if (bean.getDocumentIdentificationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getDocumentIdentificationDocument());

	if (bean.getFirstCycleAccessHabilitationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getFirstCycleAccessHabilitationDocument());

	if (bean.getHabilitationCertificationDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getHabilitationCertificationDocument());

	if (bean.getPaymentDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getPaymentDocument());

	if (bean.getVatCatCopyDocument() != null)
	    bindIndividualCandidacyDocumentFile(bean.getVatCatCopyDocument());

	if (bean.getPhotoDocument() != null)
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
	IndividualCandidacyDocumentFile photo = getFileForType(IndividualCandidacyDocumentFileType.PHOTO);
	return photo;
    }

    public IndividualCandidacyDocumentFile getFileForType(IndividualCandidacyDocumentFileType type) {
	for (IndividualCandidacyDocumentFile document : this.getCandidacy().getDocuments()) {
	    if (document.getCandidacyFileType().equals(type))
		return document;
	}

	return null;
    }

    public void bindIndividualCandidacyDocumentFile(CandidacyProcessDocumentUploadBean uploadBean) {
	if (uploadBean.getDocumentFile() != null) {
	    uploadBean.getDocumentFile().setIndividualCandidacy(this.getCandidacy());
	}
    }

    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
	List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

	if (getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	}

	if (getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	}

	if (getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	}

	return missingDocumentFiles;
    }

    public Boolean getAllRequiredFilesUploaded() {
	return getMissingRequiredDocumentFiles().isEmpty();
    }

    /**
     * Check if the process is complete i.e. if it has all required candidate
     * personal information and candidacy information like the chosen degree.
     * 
     * @return
     */
    public abstract Boolean isCandidacyProcessComplete();

    public Boolean isCandidacyInternal() {
	return this.getCandidacy().isCandidacyInternal();
    }

    public Boolean getIsCandidacyInternal() {
	return this.isCandidacyInternal();
    }

    public void bindPerson(ChoosePersonBean choosePersonBean) {
	this.getCandidacy().bindPerson(choosePersonBean);
    }

}
