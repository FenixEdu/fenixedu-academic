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
package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationForIndividualCandidacyFactory;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.User;

public class Over23IndividualCandidacyProcess extends Over23IndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new CandidacyPayment());
        activities.add(new EditCandidacyPersonalInformation());
        activities.add(new EditCandidacyInformation());
        activities.add(new IntroduceCandidacyResult());
        activities.add(new CancelCandidacy());
        activities.add(new CreateRegistration());
        activities.add(new EditPublicCandidacyPersonalInformation());
        activities.add(new EditPublicCandidacyDocumentFile());
        activities.add(new EditPublicCandidacyHabilitations());
        activities.add(new EditDocuments());
        activities.add(new BindPersonToCandidacy());
        activities.add(new ChangeProcessCheckedState());
        activities.add(new SendEmailForApplicationSubmission());
        activities.add(new ChangePaymentCheckedState());
        activities.add(new RevokeDocumentFile());
    }

    protected Over23IndividualCandidacyProcess() {
        super();
    }

    private Over23IndividualCandidacyProcess(final Over23IndividualCandidacyProcessBean bean) {
        this();

        /*
         * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
         * candidacy information are made in the init method
         */
        init(bean);

        /*
         * 20/04/2009 - New document files specific to Over23 candidacies
         */
        setSpecificIndividualCandidacyDocumentFiles(bean);
    }

    @Override
    protected void checkParameters(final CandidacyProcess process) {
        if (process == null || !process.hasCandidacyPeriod()) {
            throw new DomainException("error.Over23IndividualCandidacyProcess.invalid.candidacy.process");
        }
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
        new Over23IndividualCandidacy(this, (Over23IndividualCandidacyProcessBean) bean);
    }

    @Override
    public Over23IndividualCandidacy getCandidacy() {
        return (Over23IndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(this, userView);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    private Over23IndividualCandidacyProcess editCandidacyInformation(final Over23IndividualCandidacyProcessBean bean) {
        getCandidacy().editCandidacyInformation(bean.getCandidacyDate(), bean.getSelectedDegrees(), bean.getDisabilities(),
                bean.getEducation(), bean.getLanguagesRead(), bean.getLanguagesSpeak(), bean.getLanguagesWrite());

        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);

        return this;
    }

    public List<Degree> getSelectedDegreesSortedByOrder() {
        return getCandidacy().getSelectedDegreesSortedByOrder();
    }

    public String getDisabilities() {
        return getCandidacy().getDisabilities();
    }

    public String getEducation() {
        return getCandidacy().getEducation();
    }

    public String getLanguages() {
        return getCandidacy().getLanguages();
    }

    public String getLanguagesRead() {
        return getCandidacy().getLanguagesRead();
    }

    public String getLanguagesWrite() {
        return getCandidacy().getLanguagesWrite();
    }

    public String getLanguagesSpeak() {
        return getCandidacy().getLanguagesSpeak();
    }

    public Degree getAcceptedDegree() {
        return getCandidacy().getAcceptedDegree();
    }

    public boolean hasAcceptedDegree() {
        return getAcceptedDegree() != null;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    static private boolean isAllowedToManageProcess(Over23IndividualCandidacyProcess process, User userView) {
        Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

        if (process == null || process.getCandidacy() == null) {
            return false;
        }

        return !Collections.disjoint(programs, process.getCandidacy().getSelectedDegrees());
    }

    private void setSpecificIndividualCandidacyDocumentFiles(Over23IndividualCandidacyProcessBean bean) {
        bindIndividualCandidacyDocumentFile(bean.getCurriculumVitaeDocument());
        bindIndividualCandidacyDocumentFile(bean.getHandicapProofDocument());

        for (CandidacyProcessDocumentUploadBean documentBean : bean.getHabilitationCertificateList()) {
            bindIndividualCandidacyDocumentFile(documentBean);
        }

        for (CandidacyProcessDocumentUploadBean documentBean : bean.getReportOrWorkDocumentList()) {
            bindIndividualCandidacyDocumentFile(documentBean);
        }
    }

    private void saveChosenDegrees(final List<Degree> degrees) {
        this.getCandidacy().saveChoosedDegrees(degrees);
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            /*
             * 06/04/2009 The candidacy may be submited by someone who's not
             * authenticated in the system
             * 
             * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
             * PreConditionNotValidException();}
             */
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess dummy, User userView,
                Object object) {
            return new Over23IndividualCandidacyProcess((Over23IndividualCandidacyProcessBean) object);
        }
    }

    static private class CandidacyPayment extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            return process; // nothing to be done, for now payment is being
            // done by existing interface
        }
    }

    static private class EditCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            final IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) object;
            process.editPersonalCandidacyInformation(bean.getPersonBean());
            return process;
        }
    }

    static private class EditCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (!process.isInStandBy() || process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            process.editCandidacyHabilitations((IndividualCandidacyProcessBean) object);
            return process.editCandidacyInformation((Over23IndividualCandidacyProcessBean) object);
        }
    }

    static private class IntroduceCandidacyResult extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyDebtPayed()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isSentToJury()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            final Over23IndividualCandidacyResultBean bean = (Over23IndividualCandidacyResultBean) object;
            process.getCandidacy().editCandidacyResult(bean.getState(), bean.getAcceptedDegree());
            return process;
        }
    }

    static private class CancelCandidacy extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            process.cancelCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class CreateRegistration extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyAccepted()) {
                throw new PreConditionNotValidException();
            }

            if (process.hasRegistrationForCandidacy()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            createRegistration(process);
            return process;
        }

        private void createRegistration(final Over23IndividualCandidacyProcess candidacyProcess) {
            candidacyProcess.getCandidacy().createRegistration(getDegreeCurricularPlan(candidacyProcess), CycleType.FIRST_CYCLE,
                    Ingression.CM23);
        }

        private DegreeCurricularPlan getDegreeCurricularPlan(final Over23IndividualCandidacyProcess candidacyProcess) {
            return candidacyProcess.getAcceptedDegree().getLastActiveDegreeCurricularPlan();
        }
    }

    static private class BindPersonToCandidacy extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyInternal()) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) object;

            // First edit personal information
            process.editPersonalCandidacyInformation(bean.getPersonBean());
            // Then bind to person
            process.bindPerson(bean.getChoosePersonBean());

            return process;

        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            process.editPersonalCandidacyInformation(((IndividualCandidacyProcessBean) object).getPersonBean());
            process.saveLanguagesReadWriteSpeak((Over23IndividualCandidacyProcessBean) object);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            // TODO Auto-generated method stub
            return Boolean.FALSE;
        }
    }

    static private class EditPublicCandidacyDocumentFile extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyHabilitations extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) object;
            process.editCandidacyHabilitations(bean);
            process.saveChosenDegrees(((Over23IndividualCandidacyProcessBean) object).getSelectedDegrees());
            process.saveLanguagesReadWriteSpeak((Over23IndividualCandidacyProcessBean) object);
            process.getCandidacy().setDisabilities(((Over23IndividualCandidacyProcessBean) object).getDisabilities());

            process.editPrecedentDegreeInformation(bean);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }
    }

    static private class EditDocuments extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }
    }

    static private class ChangeProcessCheckedState extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            process.setProcessChecked(((IndividualCandidacyProcessBean) object).getProcessChecked());
            return process;
        }
    }

    private void saveLanguagesReadWriteSpeak(Over23IndividualCandidacyProcessBean bean) {
        this.getCandidacy().setLanguagesRead(bean.getLanguagesRead());
        this.getCandidacy().setLanguagesSpeak(bean.getLanguagesSpeak());
        this.getCandidacy().setLanguagesWrite(bean.getLanguagesWrite());
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
        return isCandidacyPersonalInformationComplete() && isCandidacyInformationComplete()
                && isCandidacyCommonInformationComplete();
    }

    private boolean isCandidacyCommonInformationComplete() {
        return true;
    }

    private boolean isCandidacyInformationComplete() {
        return !this.getCandidacy().getOver23IndividualCandidacyDegreeEntriesSet().isEmpty();
    }

    private boolean isCandidacyPersonalInformationComplete() {
        return true;
    }

    @Override
    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
        List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PHOTO);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
        }

        return missingDocumentFiles;
    }

    static private class SendEmailForApplicationSubmission extends Activity<Over23IndividualCandidacyProcess> {
        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
            hashCode.sendEmailForApplicationSuccessfullySubmited();
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class ChangePaymentCheckedState extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            process.setPaymentChecked(((IndividualCandidacyProcessBean) object).getPaymentChecked());
            return process;
        }
    }

    static protected class RevokeDocumentFile extends Activity<Over23IndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(Over23IndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, User userView,
                Object object) {
            ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    @Override
    protected void executeOperationsBeforeDocumentFileBinding(IndividualCandidacyDocumentFile documentFile) {
        IndividualCandidacyDocumentFileType type = documentFile.getCandidacyFileType();

        IndividualCandidacyDocumentFile file = getActiveFileForType(type);
        if (file == null) {
            return;
        }

        if (IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT.equals(type)) {
            return;
        }

        if (IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT.equals(type)) {
            throw new DomainException("error.over23.uploading.payment.document.more.than.once");
        }

        file.setCandidacyFileActive(false);
    }

}
