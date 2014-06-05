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
package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class PublicPresentationSeminarProcess extends PublicPresentationSeminarProcess_Base {

    static abstract private class PhdActivity extends Activity<PublicPresentationSeminarProcess> {

        @Override
        public void checkPreConditions(PublicPresentationSeminarProcess process, User userView) {
            processPreConditions(process, userView);
            activityPreConditions(process, userView);
        }

        protected void processPreConditions(final PublicPresentationSeminarProcess process, final User userView) {
            if (process != null && process.isExempted()) {
                throw new PreConditionNotValidException();
            }
        }

        abstract protected void activityPreConditions(final PublicPresentationSeminarProcess process, final User userView);
    }

    @StartActivity
    static public class RequestComission extends PhdActivity {

        @Override
        protected void processPreConditions(PublicPresentationSeminarProcess process, User userView) {
            // overrided to prevent exempted test
        }

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            // Activity on main process ensures access control
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess noProcess, User userView,
                Object object) {
            PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;

            final PublicPresentationSeminarProcess result =
                    new PublicPresentationSeminarProcess(bean.getPhdIndividualProgramProcess(), bean);

            result.createState(PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION,
                    userView.getPerson(), ((PublicPresentationSeminarProcessBean) object).getRemarks());

            return result;
        }
    }

    static public class SubmitComission extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)
                    && !process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);

            process.addDocument(bean.getDocument(), userView.getPerson());
            process.createState(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION,
                    userView.getPerson(), bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                        AcademicOperationType.VIEW_PHD_PUBLIC_PRESENTATION_ALERTS,
                        "message.phd.alert.public.presentation.seminar.comission.validation.subject",
                        "message.phd.alert.public.presentation.seminar.comission.validation.body");
            }

            return process;
        }

    }

    static public class ValidateComission extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {

            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);

            if (bean.getDocument().hasAnyInformation()) {
                process.addDocument(bean.getDocument(), userView.getPerson());
            }

            process.createState(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED, userView.getPerson(),
                    bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertGuiders(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.comission.validated.subject",
                        "message.phd.alert.public.presentation.seminar.comission.validated.body");

                AlertService.alertStudent(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.comission.validated.subject",
                        "message.phd.alert.public.presentation.seminar.comission.validated.body");
            }

            return process;
        }

    }

    static public class RejectComission extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {

            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;

            PublicPresentationSeminarState mostRecentState = process.getMostRecentState();

            PublicPresentationSeminarState.createWithGivenStateDate(process,
                    PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION, userView.getPerson(),
                    bean.getRemarks(), mostRecentState.getStateDate().plus(5));

            if (bean.getGenerateAlert()) {
                AlertService.alertCoordinators(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.comission.rejected.subject",
                        "message.phd.alert.public.presentation.seminar.comission.rejected.body");
            }

            return process;
        }
    }

    static public class SchedulePresentationDate extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {

            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)
                    && !process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            process.setPresentationDate(bean.getPresentationDate());
            process.createState(PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED,
                    userView.getPerson(), bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                        AcademicOperationType.VIEW_PHD_PUBLIC_PRESENTATION_ALERTS,
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");

                AlertService.alertGuiders(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");

                AlertService.alertStudent(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
                        "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");
            }

            return process;
        }
    }

    static public class UploadReport extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)
                    && !process.getIndividualProgramProcess().isGuider(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);
            process.addDocument(bean.getDocument(), userView.getPerson());

            process.createState(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION, userView.getPerson(),
                    bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                        AcademicOperationType.VIEW_PHD_PUBLIC_PRESENTATION_ALERTS,
                        "message.phd.alert.public.presentation.seminar.report.uploaded.subject",
                        "message.phd.alert.public.presentation.seminar.report.uploaded.body");
            }

            return process;
        }
    }

    static public class ValidateReport extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);

            if (bean.getDocument().hasAnyInformation()) {
                process.addDocument(bean.getDocument(), userView.getPerson());
            }

            process.createState(PublicPresentationSeminarProcessStateType.REPORT_VALIDATED, userView.getPerson(),
                    bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertCoordinators(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.report.validated.subject",
                        "message.phd.alert.public.presentation.seminar.report.validated.body");

                AlertService.alertGuiders(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.report.validated.subject",
                        "message.phd.alert.public.presentation.seminar.report.validated.body");

                AlertService.alertStudent(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.report.validated.subject",
                        "message.phd.alert.public.presentation.seminar.report.validated.body");
            }

            return process;
        }
    }

    static public class RejectReport extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (process.getActiveState() != PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {

            final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;

            process.createState(PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED,
                    userView.getPerson(), bean.getRemarks());

            if (bean.getGenerateAlert()) {
                AlertService.alertGuiders(process.getIndividualProgramProcess(),
                        "message.phd.alert.public.presentation.seminar.report.rejected.subject",
                        "message.phd.alert.public.presentation.seminar.report.rejected.body");
            }

            return process;
        }
    }

    static public class DownloadReportDocument extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {

            if (!process.hasReportDocument()) {
                throw new PreConditionNotValidException();
            }

            if (process.isAllowedToManageProcess(userView)
                    || process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())
                    || process.getIndividualProgramProcess().isGuiderOrAssistentGuider(userView.getPerson())) {
                return;
            }

            if (process.getActiveState() == PublicPresentationSeminarProcessStateType.REPORT_VALIDATED
                    && process.getIndividualProgramProcess().getPerson() == userView.getPerson()) {
                return;
            }

            throw new PreConditionNotValidException();

        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            // Nothing to be done
            return null;
        }

    }

    static public class DownloadComissionDocument extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.hasComissionDocument()) {
                throw new PreConditionNotValidException();
            }

            if (process.isAllowedToManageProcess(userView)
                    || process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())
                    || process.getIndividualProgramProcess().isGuiderOrAssistentGuider(userView.getPerson())) {
                return;
            }

            if (process.hasState(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED)
                    && process.getIndividualProgramProcess().getPerson() == userView.getPerson()) {
                return;
            }

            throw new PreConditionNotValidException();

        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            // Nothing to be done

            return null;
        }

    }

    static public class RevertToWaitingForComissionConstitution extends PhdActivity {

        @Override
        public void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.getActiveState().equals(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            if (!process.getActiveState().equals(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION)) {
                throw new DomainException("error.PublicPresentationSeminarProcess.is.not.in.comission.waiting.for.validation");
            }

            PublicPresentationSeminarState mostRecentState = process.getMostRecentState();

            PublicPresentationSeminarState.createWithGivenStateDate(process,
                    PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION, userView.getPerson(), "",
                    mostRecentState.getStateDate().plusMinutes(1));

            return process;
        }
    }

    static public class RevertToWaitingComissionForValidation extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.getActiveState().equals(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            if (!process.getActiveState().equals(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED)) {
                throw new DomainException("error.PublicPresentationSeminarProcess.is.not.in.comission.validated.state");
            }

            PublicPresentationSeminarState mostRecentState = process.getMostRecentState();
            PublicPresentationSeminarState.createWithGivenStateDate(process,
                    PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION, userView.getPerson(), "",
                    mostRecentState.getStateDate().plusMinutes(1));

            return process;
        }
    }

    static public class AddState extends PhdActivity {

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
            process.createState(bean.getProcessState(), AccessControl.getPerson(), bean.getRemarks());

            return process;
        }
    }

    static public class RemoveLastState extends PhdActivity {

        @Override
        protected void processPreConditions(PublicPresentationSeminarProcess process, User userView) {
            // no pre-conditions
        }

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            process.removeLastState();

            return process;
        }
    }

    static public class EditProcessAttributes extends PhdActivity {

        @Override
        protected void processPreConditions(PublicPresentationSeminarProcess process, User userView) {
        }

        @Override
        protected void activityPreConditions(PublicPresentationSeminarProcess process, User userView) {
            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, User userView,
                Object object) {
            PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;

            process.setPresentationDate(bean.getPresentationDate());
            process.setPresentationRequestDate(bean.getPresentationRequestDate());

            return process;
        }
    }

    static private List<Activity> activities = new ArrayList<Activity>();

    static {
        activities.add(new SubmitComission());
        activities.add(new ValidateComission());
        activities.add(new RejectComission());
        activities.add(new SchedulePresentationDate());
        activities.add(new UploadReport());
        activities.add(new ValidateReport());
        activities.add(new RejectReport());
        activities.add(new DownloadReportDocument());
        activities.add(new DownloadComissionDocument());
        activities.add(new RevertToWaitingForComissionConstitution());
        activities.add(new RevertToWaitingComissionForValidation());
        activities.add(new AddState());
        activities.add(new RemoveLastState());
        activities.add(new EditProcessAttributes());
    }

    @Override
    public boolean isAllowedToManageProcess(User userView) {
        return this.getIndividualProgramProcess().isAllowedToManageProcess(userView);
    }

    private PublicPresentationSeminarProcess(final PhdIndividualProgramProcess individualProcess,
            final PublicPresentationSeminarProcessBean bean) {
        super();
        String[] args = {};

        if (individualProcess == null) {
            throw new DomainException("error.phd.PublicPresentationSeminarProcess.individualProgramProcess.cannot.be.null", args);
        }
        this.setIndividualProgramProcess(individualProcess);

        if (!individualProcess.isMigratedProcess()) {
            if (bean.getPresentationRequestDate() == null) {
                throw new DomainException("error.seminar.PublicPresentationSeminarProcess.presentation.request.date.required");
            }
        }

        setPresentationRequestDate(bean.getPresentationRequestDate());
    }

    public boolean hasReportDocument() {
        return getReportDocument() != null;
    }

    public PhdProgramProcessDocument getReportDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);
    }

    public boolean hasComissionDocument() {
        return getComissionDocument() != null;
    }

    public PhdProgramProcessDocument getComissionDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.PHD, getClass().getSimpleName());
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return false;
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public PublicPresentationSeminarState getMostRecentState() {
        return (PublicPresentationSeminarState) super.getMostRecentState();
    }

    @Override
    public PublicPresentationSeminarProcessStateType getActiveState() {
        return (PublicPresentationSeminarProcessStateType) super.getActiveState();
    }

    public void createState(final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {
        PublicPresentationSeminarState.createWithInferredStateDate(this, type, person, remarks);
    }

    @Override
    protected Person getPerson() {
        return getIndividualProgramProcess().getPerson();
    }

    public boolean isExempted() {
        return getActiveState() == PublicPresentationSeminarProcessStateType.EXEMPTED;
    }

    public boolean isConcluded() {
        return getActiveState() == PublicPresentationSeminarProcessStateType.REPORT_VALIDATED;
    }

    public List<PublicPresentationSeminarProcessStateType> getPossibleNextStates() {
        PublicPresentationSeminarProcessStateType activeState = getActiveState();

        if (activeState == null) {
            return Collections.singletonList(PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION);
        }

        switch (activeState) {
        case WAITING_FOR_COMMISSION_CONSTITUTION:
            final List<PublicPresentationSeminarProcessStateType> result =
                    new ArrayList<PublicPresentationSeminarProcessStateType>();
            result.add(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION);
            result.add(PublicPresentationSeminarProcessStateType.EXEMPTED);
            return result;
        case COMMISSION_WAITING_FOR_VALIDATION:
            return Collections.singletonList(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED);
        case COMMISSION_VALIDATED:
            return Collections.singletonList(PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED);
        case PUBLIC_PRESENTATION_DATE_SCHEDULED:
            return Collections.singletonList(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION);
        case REPORT_WAITING_FOR_VALIDATION:
            return Collections.singletonList(PublicPresentationSeminarProcessStateType.REPORT_VALIDATED);
        case EXEMPTED:
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    public void removeLastState() {
        if (getStates().size() == 1) {
            throw new DomainException("phd.seminar.PublicPresentationSeminarProcess.process.has.only.one.state");
        }

        getMostRecentState().delete();
    }

    @Override
    public LocalDate getPresentationRequestDate() {
        if (super.getPresentationRequestDate() != null) {
            return super.getPresentationRequestDate();
        }

        if (!getIndividualProgramProcess().getStudyPlan().isExempted()) {
            if (getIndividualProgramProcess().getRegistration().isConcluded()) {
                return getIndividualProgramProcess().getRegistration().getConclusionDate().toLocalDate();
            }
        }

        if (getPresentationDate() != null) {
            return getPresentationDate().minusMonths(1);
        }

        return getIndividualProgramProcess().getWhenStartedStudies();
    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarState> getStates() {
        return getStatesSet();
    }

    @Override
    @Deprecated
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

    @Deprecated
    public boolean hasPresentationDate() {
        return getPresentationDate() != null;
    }

    @Deprecated
    public boolean hasPresentationRequestDate() {
        return getPresentationRequestDate() != null;
    }

    @Deprecated
    public boolean hasIndividualProgramProcess() {
        return getIndividualProgramProcess() != null;
    }

}
