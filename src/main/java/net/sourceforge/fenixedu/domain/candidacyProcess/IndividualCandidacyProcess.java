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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.RandomStringGenerator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

abstract public class IndividualCandidacyProcess extends IndividualCandidacyProcess_Base {

    static final public Comparator<IndividualCandidacyProcess> COMPARATOR_BY_CANDIDACY_PERSON =
            new Comparator<IndividualCandidacyProcess>() {
                @Override
                public int compare(IndividualCandidacyProcess o1, IndividualCandidacyProcess o2) {
                    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(o1.getPersonalDetails(),
                            o2.getPersonalDetails());
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

        if (bean.getPublicCandidacyHashCode() == null) {
            throw new DomainException("error.IndividualCandidacy.hash.code.is.null");
        }

        if (existsIndividualCandidacyProcessForDocumentId(bean.getCandidacyProcess(), bean.getPersonBean().getIdDocumentType(),
                bean.getPersonBean().getDocumentIdNumber())) {
            throw new DomainException("error.IndividualCandidacy.exists.for.same.document.id");
        }

        setCandidacyProcess(bean.getCandidacyProcess());
        createIndividualCandidacy(bean);

        /*
         * 11/04/2009 - An external candidacy submission requires documents like
         * identification and habilitation certificate documents
         */
        setCandidacyHashCode(bean.getPublicCandidacyHashCode());

        setCandidacyDocumentFiles(bean);

        setProcessCodeForThisIndividualCandidacy(bean.getCandidacyProcess());

    }

    protected boolean existsIndividualCandidacyProcessForDocumentId(final CandidacyProcess process, IDDocumentType documentType,
            String identification) {
        return process.getOpenChildProcessByDocumentId(documentType, identification) != null;
    }

    protected void setProcessCodeForThisIndividualCandidacy(CandidacyProcess process) {
        CandidacyPeriod period = process.getCandidacyPeriod();
        String beginExecutionYear =
                String.valueOf(period.getExecutionInterval().getBeginDateYearMonthDay().get(DateTimeFieldType.year())).substring(
                        2, 4);
        String endExecutionYear =
                String.valueOf(period.getExecutionInterval().getEndDateYearMonthDay().get(DateTimeFieldType.year())).substring(2,
                        4);
        setProcessCode(beginExecutionYear + endExecutionYear + getExternalId());
    }

    protected void setCandidacyDocumentFiles(IndividualCandidacyProcessBean bean) {
        /*
         * 06/07/2009 - Lots of candidates camplaint about the upload of
         * documents in application submission. The upload of documents will be
         * done in application edit right after submission
         */
        if (bean.getPhotoDocument() != null) {
            bindIndividualCandidacyDocumentFile(bean.getPhotoDocument());
        }
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

    protected void rejectCandidacy(final Person person) {
        getCandidacy().reject(person);
    }

    protected void revertToStandBy(final Person person) {
        getCandidacy().revertToStandBy(person);
    }

    public IndividualCandidacyState getCandidacyState() {
        return getCandidacy().getState();
    }

    public boolean isCandidacyValid() {
        return !isCandidacyCancelled() && (isEventCanceledOrNoEvent() || isCandidacyDebtPayed());
    }

    private boolean isEventCanceledOrNoEvent() {
        return !getCandidacy().hasEvent() || getCandidacy().getEvent().isCancelled();
    }

    public boolean isCandidacyInStandBy() {
        return getCandidacy().isInStandBy();
    }

    public boolean isCandidacyAccepted() {
        return getCandidacy().isAccepted();
    }

    public boolean isCandidacyNotAccepted() {
        return getCandidacy().isNotAccepted();
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
        return BundleUtil.getString(Bundle.CASE_HANDLEING, getClass().getSimpleName());
    }

    protected void editPersonalCandidacyInformation(final PersonBean personBean) {
        getCandidacy().editPersonalCandidacyInformationPublic(personBean);
    }

    protected void editPersonalCandidacyInformationPublic(final PersonBean personBean) {
        getCandidacy().editPersonalCandidacyInformationPublic(personBean);
    }

    /**
     * Find an individual candidacy process given a kind of candidacy process,
     * an email and an access hash
     */
    public static IndividualCandidacyProcess findIndividualCandidacyProcess(
            final Class<? extends IndividualCandidacyProcess> individualCandidacyProcessClass, String email, String accessHash) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(accessHash)) {
            return null;
        }

        Set<IndividualCandidacyProcess> candidacies = getAllInstancesOf(individualCandidacyProcessClass);

        for (IndividualCandidacyProcess individualCandidacyProcess : candidacies) {
            if (email.equals(individualCandidacyProcess.getPersonalDetails().getEmail())
                    && accessHash.equals(individualCandidacyProcess.getAccessHash())) {
                return individualCandidacyProcess;
            }
        }

        return null;
    }

    public static <T extends IndividualCandidacyProcess> T findIndividualCandidacyProcessByCode(
            Class<T> individualCandidacyProcessClass, final String processCode) {
        Set<IndividualCandidacyProcess> candidacies = getAllInstancesOf(individualCandidacyProcessClass);

        for (IndividualCandidacyProcess process : candidacies) {
            if (processCode.equals(process.getProcessCode())) {
                return (T) process;
            }
        }

        return null;
    }

    public IndividualCandidacyDocumentFile getPhoto() {
        IndividualCandidacyDocumentFile photo = getActiveFileForType(IndividualCandidacyDocumentFileType.PHOTO);
        return photo;
    }

    public IndividualCandidacyDocumentFile getActiveFileForType(IndividualCandidacyDocumentFileType type) {
        for (IndividualCandidacyDocumentFile document : this.getCandidacy().getDocuments()) {
            if (document.getCandidacyFileType().equals(type) && document.getCandidacyFileActive()) {
                return document;
            }
        }

        return null;
    }

    public List<IndividualCandidacyDocumentFile> getAllFilesForType(IndividualCandidacyDocumentFileType type) {
        List<IndividualCandidacyDocumentFile> files = new ArrayList<IndividualCandidacyDocumentFile>();
        for (IndividualCandidacyDocumentFile document : this.getCandidacy().getDocuments()) {
            if (document.getCandidacyFileType().equals(type)) {
                files.add(document);
            }
        }

        return files;
    }

    public void bindIndividualCandidacyDocumentFile(CandidacyProcessDocumentUploadBean uploadBean) {
        if (uploadBean.getDocumentFile() != null) {
            executeOperationsBeforeDocumentFileBinding(uploadBean.getDocumentFile());

            uploadBean.getDocumentFile().addIndividualCandidacy(this.getCandidacy());
        }
    }

    protected abstract void executeOperationsBeforeDocumentFileBinding(IndividualCandidacyDocumentFile documentFile);

    public abstract List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles();

    public boolean isProcessMissingRequiredDocumentFiles() {
        return !getMissingRequiredDocumentFiles().isEmpty();
    }

    public List<IndividualCandidacyDocumentFile> getActiveDocumentFiles() {
        List<IndividualCandidacyDocumentFile> documentList = new ArrayList<IndividualCandidacyDocumentFile>();

        CollectionUtils.select(getCandidacy().getDocuments(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                IndividualCandidacyDocumentFile file = (IndividualCandidacyDocumentFile) arg0;

                return file.getCandidacyFileActive();
            }

        }, documentList);

        return documentList;
    }

    public Boolean getAllRequiredFilesUploaded() {
        return getMissingRequiredDocumentFiles().isEmpty();
    }

    public Boolean getCandidacyHasVatDocument() {
        return getActiveFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) != null;
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

    public IndividualCandidacyPaymentCode getAssociatedPaymentCode() {
        if (getCandidacy().getEvent() != null) {
            return (IndividualCandidacyPaymentCode) (getCandidacy().getEvent().getAllPaymentCodes().isEmpty() ? null : getCandidacy()
                    .getEvent().getAllPaymentCodes().iterator().next());
        }

        return null;
    }

    public Boolean getIsCandidateEmployee() {
        return this.getCandidacy().getPersonalDetails().isEmployee();
    }

    public Boolean getIsCandidateWithRoles() {
        return this.getCandidacy().getPersonalDetails().hasAnyRole();
    }

    protected void editCandidacyHabilitations(IndividualCandidacyProcessBean bean) {
        this.getCandidacy().editFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());
    }

    protected void editPrecedentDegreeInformation(IndividualCandidacyProcessBean bean) {
        getCandidacy().editPrecedentDegreeInformation(bean);

    }

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasOriginalIndividualCandidacyProcess() {
        return getOriginalIndividualCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasAccessHash() {
        return getAccessHash() != null;
    }

    @Deprecated
    public boolean hasPaymentChecked() {
        return getPaymentChecked() != null;
    }

    @Deprecated
    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasCopyIndividualCandidacyProcess() {
        return getCopyIndividualCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasCandidacyHashCode() {
        return getCandidacyHashCode() != null;
    }

    @Deprecated
    public boolean hasProcessCode() {
        return getProcessCode() != null;
    }

    @Deprecated
    public boolean hasProcessChecked() {
        return getProcessChecked() != null;
    }

    private static Set<IndividualCandidacyProcess> getAllInstancesOf(final Class<? extends IndividualCandidacyProcess> type) {
        return Sets.<IndividualCandidacyProcess> newHashSet(Iterables.filter(Bennu.getInstance().getProcessesSet(), type));
    }
}
