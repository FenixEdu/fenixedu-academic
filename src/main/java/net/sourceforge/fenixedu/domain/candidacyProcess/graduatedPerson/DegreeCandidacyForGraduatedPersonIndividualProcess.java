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
package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.fenixedu.bennu.core.domain.User;

public class DegreeCandidacyForGraduatedPersonIndividualProcess extends DegreeCandidacyForGraduatedPersonIndividualProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();

    static {
        activities.add(new CandidacyPayment());
        activities.add(new EditCandidacyPersonalInformation());
        activities.add(new EditCandidacyInformation());
        activities.add(new IntroduceCandidacyResult());
        activities.add(new ChangeIndividualCandidacyState());
        activities.add(new CancelCandidacy());
        activities.add(new CreateRegistration());
        activities.add(new EditPublicCandidacyPersonalInformation());
        activities.add(new EditPublicCandidacyDocumentFile());
        activities.add(new EditPublicCandidacyHabilitations());
        activities.add(new EditDocuments());
        activities.add(new ChangeProcessCheckedState());
        activities.add(new SendEmailForApplicationSubmission());
        activities.add(new RevokeDocumentFile());
        activities.add(new ChangePaymentCheckedState());
        activities.add(new RejectCandidacy());
        activities.add(new RevertApplicationToStandBy());

    }

    private DegreeCandidacyForGraduatedPersonIndividualProcess() {
        super();
    }

    private DegreeCandidacyForGraduatedPersonIndividualProcess(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
        this();

        /*
         * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
         * candidacy information are made in the init method
         */
        init(bean);

        /*
         * 27/04/2009 - New document files specific to SecondCycle candidacies
         */
        setSpecificIndividualCandidacyDocumentFiles(bean);

    }

    @Override
    protected void checkParameters(final CandidacyProcess candidacyProcess) {
        if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPersonIndividualProcess.invalid.candidacy.process");
        }
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
        new DegreeCandidacyForGraduatedPerson(this, (DegreeCandidacyForGraduatedPersonIndividualProcessBean) bean);
    }

    @Override
    public DegreeCandidacyForGraduatedPerson getCandidacy() {
        return (DegreeCandidacyForGraduatedPerson) super.getCandidacy();
    }

    @Override
    public DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess() {
        return (DegreeCandidacyForGraduatedPersonProcess) super.getCandidacyProcess();
    }

    @Override
    public boolean canExecuteActivity(final User userView) {
        return isAllowedToManageProcess(this, userView);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    public Degree getCandidacySelectedDegree() {
        return getCandidacy().getSelectedDegree();
    }

    public BigDecimal getCandidacyAffinity() {
        return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
        return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyGrade() {
        return getCandidacy().getCandidacyGrade();
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
        return getCandidacy().getRefactoredPrecedentDegreeInformation();
    }

    private void editCandidacyInformation(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
        getCandidacy().editCandidacyInformation(bean);
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
        return getCandidacySelectedDegree() == degree;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    // static information

    static private boolean isAllowedToManageProcess(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
        Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

        if (process == null || process.getCandidacy() == null) {
            return false;
        }

        return programs.contains(process.getCandidacy().getSelectedDegree());
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            /*
             * 06/04/2009 The candidacy may be submited by someone who's not
             * authenticated in the system
             * 
             * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
             * PreConditionNotValidException();}
             */
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess dummy, User userView, Object object) {
            return new DegreeCandidacyForGraduatedPersonIndividualProcess(
                    (DegreeCandidacyForGraduatedPersonIndividualProcessBean) object);
        }
    }

    static private class CandidacyPayment extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            return process; // nothing to be done, for now payment is being
            // done by existing interface
        }
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {

            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.editPersonalCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object)
                    .getPersonBean());
            return process;
        }
    }

    static private class EditCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {

            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            DegreeCandidacyForGraduatedPersonIndividualProcessBean bean =
                    (DegreeCandidacyForGraduatedPersonIndividualProcessBean) object;

            process.editCandidacyHabilitations(bean);
            process.getCandidacy().editObservations(bean);
            process.editCandidacyInformation(bean);
            process.getCandidacy().setUtlStudent(bean.getUtlStudent());

            return process;
        }
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled() || !process.isCandidacyDebtPayed()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isSentToCoordinator()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean =
                    (DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean) object;
            DegreeCandidacyForGraduatedPersonSeriesGade degreeCandidacyForGraduatedPersonSeriesGade =
                    process.getCandidacy().getDegreeCandidacyForGraduatedPersonSeriesGadeForDegree(bean.getDegree());
            degreeCandidacyForGraduatedPersonSeriesGade.setAffinity(bean.getAffinity());
            degreeCandidacyForGraduatedPersonSeriesGade.setDegreeNature(bean.getDegreeNature());
            degreeCandidacyForGraduatedPersonSeriesGade.setCandidacyGrade(bean.getGrade());
            degreeCandidacyForGraduatedPersonSeriesGade.setState(bean.getSeriesGradeState());

            return process;
        }
    }

    static private class ChangeIndividualCandidacyState extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyDebtPayed()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean =
                    (DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean) object;
            process.getCandidacy().setState(bean.getState());
            return process;
        }

    }

    static private class CancelCandidacy extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.cancelCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class CreateRegistration extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
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
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE, Ingression.CEA02);
            return process;
        }

        private DegreeCurricularPlan getDegreeCurricularPlan(
                final DegreeCandidacyForGraduatedPersonIndividualProcess candidacyProcess) {
            return candidacyProcess.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
        }
    }

    static private class EditPublicCandidacyPersonalInformation extends
            Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.editPersonalCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object)
                    .getPersonBean());
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {

            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyHabilitations extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {

            DegreeCandidacyForGraduatedPersonIndividualProcessBean bean =
                    (DegreeCandidacyForGraduatedPersonIndividualProcessBean) object;
            process.editCandidacyHabilitations(bean);
            process.getCandidacy().editSelectedDegree(bean.getSelectedDegree());
            process.getCandidacy().editObservations(bean);
            process.editPrecedentDegreeInformation(bean);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditDocuments extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }
    }

    static private class ChangeProcessCheckedState extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.setProcessChecked(((IndividualCandidacyProcessBean) object).getProcessChecked());
            return process;
        }
    }

    static private class ChangePaymentCheckedState extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.setPaymentChecked(((IndividualCandidacyProcessBean) object).getPaymentChecked());
            return process;
        }
    }

    static private class SendEmailForApplicationSubmission extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {
        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
            hashCode.sendEmailForApplicationSuccessfullySubmited();
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    @Override
    public Boolean isCandidacyProcessComplete() {
        // TODO Auto-generated method stub
        return null;
    }

    private void setSpecificIndividualCandidacyDocumentFiles(DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
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

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE);
        }

        return missingDocumentFiles;

    }

    static protected class RevokeDocumentFile extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class RejectCandidacy extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.rejectCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class RevertApplicationToStandBy extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyCancelled() && !process.isCandidacyRejected()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
                DegreeCandidacyForGraduatedPersonIndividualProcess process, User userView, Object object) {
            process.getCandidacy().setState(IndividualCandidacyState.STAND_BY);

            return process;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
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

        file.setCandidacyFileActive(false);
    }
}
