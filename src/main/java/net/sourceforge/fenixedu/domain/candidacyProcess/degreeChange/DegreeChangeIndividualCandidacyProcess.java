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
package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
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

public class DegreeChangeIndividualCandidacyProcess extends DegreeChangeIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new CandidacyPayment());
        activities.add(new EditCandidacyPersonalInformation());
        activities.add(new EditCandidacyInformation());
        activities.add(new EditCandidacyCurricularCoursesInformation());
        activities.add(new IntroduceCandidacyResult());
        activities.add(new ChangeIndividualCandidacyState());
        activities.add(new CancelCandidacy());
        activities.add(new CreateRegistration());
        activities.add(new EditPublicCandidacyPersonalInformation());
        activities.add(new EditPublicCandidacyHabilitations());
        activities.add(new EditPublicCandidacyDocumentFile());
        activities.add(new SendEmailForApplicationSubmission());
        activities.add(new EditDocuments());
        activities.add(new ChangeProcessCheckedState());
        activities.add(new RevokeDocumentFile());
        activities.add(new ChangePaymentCheckedState());
        activities.add(new RejectCandidacy());
        activities.add(new RevertApplicationToStandBy());
    }

    private DegreeChangeIndividualCandidacyProcess() {
        super();
    }

    private DegreeChangeIndividualCandidacyProcess(final DegreeChangeIndividualCandidacyProcessBean bean) {
        this();

        /*
         * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
         * candidacy information are made in the init method
         */
        init(bean);
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
        new DegreeChangeIndividualCandidacy(this, (DegreeChangeIndividualCandidacyProcessBean) bean);
    }

    @Override
    protected void checkParameters(final CandidacyProcess candidacyProcess) {
        if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
            throw new DomainException("error.DegreeChangeIndividualCandidacyProcess.invalid.candidacy.process");
        }
    }

    @Override
    public DegreeChangeCandidacyProcess getCandidacyProcess() {
        return (DegreeChangeCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public DegreeChangeIndividualCandidacy getCandidacy() {
        return (DegreeChangeIndividualCandidacy) super.getCandidacy();
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

    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
        return getCandidacy().getRefactoredPrecedentDegreeInformation();
    }

    private void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
        getCandidacy().editCandidacyInformation(bean);
    }

    public BigDecimal getCandidacyAffinity() {
        return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
        return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyApprovedEctsRate() {
        return getCandidacy().getApprovedEctsRate();
    }

    public BigDecimal getCandidacyGradeRate() {
        return getCandidacy().getGradeRate();
    }

    public BigDecimal getCandidacySeriesCandidacyGrade() {
        return getCandidacy().getSeriesCandidacyGrade();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
        return getCandidacySelectedDegree() == degree;
    }

    // static information

    static private boolean isAllowedToManageProcess(DegreeChangeIndividualCandidacyProcess process, User userView) {
        Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

        if (process == null || process.getCandidacy() == null) {
            return false;
        }

        return programs.contains(process.getCandidacy().getSelectedDegree());
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            /*
             * 06/04/2009 The candidacy may be submited by someone who's not
             * authenticated in the system
             * 
             * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
             * PreConditionNotValidException();}
             */
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess dummy,
                User userView, Object object) {
            final DegreeChangeIndividualCandidacyProcessBean bean = (DegreeChangeIndividualCandidacyProcessBean) object;
            return new DegreeChangeIndividualCandidacyProcess(bean);
        }
    }

    static public class SendEmailForApplicationSubmission extends Activity<DegreeChangeIndividualCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
            hashCode.sendEmailForApplicationSuccessfullySubmited();
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class CandidacyPayment extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            return process; // nothing to be done, for now payment is being
            // done by existing interfaces
        }
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.editPersonalCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object).getPersonBean());
            return process;
        }
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {

            process.editPersonalCandidacyInformationPublic(((DegreeChangeIndividualCandidacyProcessBean) object).getPersonBean());

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyHabilitations extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            DegreeChangeIndividualCandidacyProcessBean bean = (DegreeChangeIndividualCandidacyProcessBean) object;
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

    static private class EditCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.editCandidacyHabilitations((DegreeChangeIndividualCandidacyProcessBean) object);
            process.getCandidacy().editObservations((DegreeChangeIndividualCandidacyProcessBean) object);
            process.editCandidacyInformation((DegreeChangeIndividualCandidacyProcessBean) object);
            process.getCandidacy().setUtlStudent(((DegreeChangeIndividualCandidacyProcessBean) object).getUtlStudent());
            return process;
        }
    }

    static private class EditCandidacyCurricularCoursesInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {

            return process;
        }
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
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
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            DegreeChangeIndividualCandidacyResultBean bean = (DegreeChangeIndividualCandidacyResultBean) object;
            DegreeChangeIndividualCandidacySeriesGrade degreeChangeIndividualCandidacySeriesGrade =
                    process.getCandidacy().getDegreeChangeIndividualCandidacySeriesGradeForDegree(bean.getDegree());
            degreeChangeIndividualCandidacySeriesGrade.setAffinity(bean.getAffinity());
            degreeChangeIndividualCandidacySeriesGrade.setDegreeNature(bean.getDegreeNature());
            degreeChangeIndividualCandidacySeriesGrade.setApprovedEctsRate(bean.getApprovedEctsRate());
            degreeChangeIndividualCandidacySeriesGrade.setGradeRate(bean.getGradeRate());
            degreeChangeIndividualCandidacySeriesGrade.setSeriesCandidacyGrade(bean.getSeriesCandidacyGrade());
            degreeChangeIndividualCandidacySeriesGrade.setState(bean.getSeriesGradeState());
            return process;
        }
    }

    static private class ChangeIndividualCandidacyState extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
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
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            DegreeChangeIndividualCandidacyResultBean bean = (DegreeChangeIndividualCandidacyResultBean) object;
            process.getCandidacy().setState(bean.getState());
            return process;
        }

    }

    static private class CancelCandidacy extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.cancelCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class CreateRegistration extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
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
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE,
                    getIngression(process));
            return process;
        }

        private Ingression getIngression(final DegreeChangeIndividualCandidacyProcess process) {
            return process.getPrecedentDegreeInformation().isCandidacyExternal() ? Ingression.MCE : Ingression.MCI;
        }

        private DegreeCurricularPlan getDegreeCurricularPlan(final DegreeChangeIndividualCandidacyProcess process) {
            return process.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
        }
    }

    static private class EditDocuments extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }
    }

    static private class ChangeProcessCheckedState extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.setProcessChecked(((IndividualCandidacyProcessBean) object).getProcessChecked());
            return process;
        }
    }

    static private class ChangePaymentCheckedState extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.setPaymentChecked(((IndividualCandidacyProcessBean) object).getPaymentChecked());
            return process;
        }
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
        // TODO Auto-generated method stub
        return null;
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

        if (!getCandidacy().hasStudent()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
        }

        if (getCandidacy().getRefactoredPrecedentDegreeInformation().isCandidacyExternal()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
        }

        if (getCandidacy().getRefactoredPrecedentDegreeInformation().isCandidacyExternal()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
        }

        if (getCandidacy().getRefactoredPrecedentDegreeInformation().isCandidacyExternal()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE);
        }

        if (getCandidacy().getRefactoredPrecedentDegreeInformation().isCandidacyInternal()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT);
        }

        return missingDocumentFiles;
    }

    static protected class RevokeDocumentFile extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class RejectCandidacy extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
            process.rejectCandidacy(userView.getPerson());
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

    static private class RevertApplicationToStandBy extends Activity<DegreeChangeIndividualCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isCandidacyCancelled() && !process.isCandidacyRejected()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
                User userView, Object object) {
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
