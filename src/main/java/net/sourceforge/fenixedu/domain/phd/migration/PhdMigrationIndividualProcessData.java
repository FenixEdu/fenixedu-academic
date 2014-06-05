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
package net.sourceforge.fenixedu.domain.phd.migration;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean.QualificationExamsResult;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.CancelPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditQualificationExams;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicThesisPresentation;
import net.sourceforge.fenixedu.domain.phd.migration.activities.SkipThesisJuryActivities;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.FinalEstimatedStateNotReachedException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PhdMigrationException;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RatifyFinalThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SetFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SkipScheduleThesisDiscussion;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.SkipScheduleFirstThesisMeeting;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PhdMigrationIndividualProcessData extends PhdMigrationIndividualProcessData_Base {

    protected PhdMigrationIndividualProcessData(String data) {
        setData(data);
        setMigrationStatus(PhdMigrationProcessStateType.NOT_MIGRATED);
    }

    public PhdMigrationIndividualProcessDataBean getProcessBean() {
        return new PhdMigrationIndividualProcessDataBean(this);
    }

    public boolean hasMigrationParseLog() {
        return !StringUtils.isEmpty(getMigrationParseLog());
    }

    public String getMigrationException() {
        if (!hasMigrationParseLog()) {
            return null;
        }

        String exceptionLine = getMigrationParseLog();
        int messageStartIdx = exceptionLine.indexOf(" ");
        if (messageStartIdx == -1) {
            return exceptionLine;
        }
        return exceptionLine.substring(0, exceptionLine.indexOf(" ") - 1);
    }

    public String getMigrationExceptionMessage() {
        if (!hasMigrationParseLog()) {
            return null;
        }

        String exceptionLine = getMigrationParseLog();
        int messageStartIdx = exceptionLine.indexOf(" ");
        if (messageStartIdx == -1) {
            return null;
        }
        return exceptionLine.substring(exceptionLine.indexOf(" "));
    }

    public String getMigrationExceptionMessageFromBundle() {
        final String exceptionString = getMigrationException();

        if (exceptionString == null) {
            return null;
        }

        final String messageString = getMigrationExceptionMessage();
        String errorTranslated = null;

        /*
         * If the exception was a DomainException, we try to fetch the error
         * message from the bundle
         */
        if (exceptionString.contains(DomainException.class.getSimpleName())) {
            if (messageString == null) {
                errorTranslated = exceptionString + " " + messageString;
            } else {
                try {
                    errorTranslated = BundleUtil.getString(Bundle.APPLICATION, messageString);
                } catch (Exception e) {
                    errorTranslated = exceptionString + " " + messageString;
                }
            }
            return errorTranslated;
        }

        /*
         * Else, it must have been a Migration exception, which may have
         * additional information in the messageString
         */
        try {
            errorTranslated = BundleUtil.getString(Bundle.PHD, "label.phd.migration.exception." + exceptionString);
        } catch (Exception e) {
            return exceptionString + " " + messageString;
        }

        errorTranslated += " - " + getMigrationExceptionMessage();

        return errorTranslated;
    }

    public Person getGuidingPerson() {
        if (getProcessBean().getGuiderNumber().contains("E")) {
            throw new PersonNotFoundException();
        }

        return getPerson(getProcessBean().getGuiderNumber());
    }

    public Person getAssistantGuidingPerson() {
        if (getProcessBean().getAssistantGuiderNumber().contains("E")) {
            throw new PersonNotFoundException();
        }

        return getPerson(getProcessBean().getAssistantGuiderNumber());
    }

    public Person getPerson(String identification) {
        Teacher teacher = Employee.readByNumber(Integer.valueOf(identification)).getPerson().getTeacher();

        if (teacher == null) {
            throw new PersonNotFoundException();
        }

        return teacher.getPerson();
    }

    public boolean isMigratedToIndividualProgramProcess() {
        return getMigratedIndividualProgramProcess() != null;
    }

    public boolean isNotMigrated() {
        return getMigrationStatus().equals(PhdMigrationProcessStateType.NOT_MIGRATED)
                && getMigratedIndividualProgramProcess() == null;
    }

    public boolean isRegistered() {
        if (hasPhdMigrationIndividualPersonalData()) {
            try {
                if (getPhdMigrationIndividualPersonalData().isPersonRegisteredOnFenix()) {
                    return true;
                }
            } catch (PhdMigrationException e) {
            }
        }
        return false;
    }

    public boolean isNotRegisteredAndNoSimilarsExist() {
        if (hasPhdMigrationIndividualPersonalData()) {
            try {
                if (!getPhdMigrationIndividualPersonalData().isPersonRegisteredOnFenix()) {
                    return true;
                }
            } catch (PhdMigrationException e) {
            }
        }
        return false;
    }

    public boolean isThereAnySimilarRegistration() {
        if (hasPhdMigrationIndividualPersonalData()) {
            try {
                getPhdMigrationIndividualPersonalData().isPersonRegisteredOnFenix();
            } catch (PhdMigrationException e) {
                return true;
            }
        }
        return false;
    }

    public PhdIndividualProgramProcess getMigratedIndividualProgramProcess() {

        final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
        searchBean.setFilterPhdPrograms(false);
        searchBean.setFilterPhdProcesses(false);

        for (final PhdIndividualProgramProcess process : PhdIndividualProgramProcess.search(searchBean.getPredicates())) {
            if (process.getPhdStudentNumber() != null && process.getPhdStudentNumber().equals(getNumber())) {
                return process;
            }
        }

        return null;
    }

    public boolean hasExistingIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        final PhdMigrationIndividualPersonalData personalData = getPhdMigrationIndividualPersonalData();

        if (personalData == null) {
            return null;
        }

        if (!personalData.isPersonRegisteredOnFenix()) {
            return null;
        }

        final Person student = personalData.getPerson();

        if (student.hasAnyPhdIndividualProgramProcesses()) {
            return student.getPhdIndividualProgramProcesses().iterator().next();
        }

        return null;
    }

    public ExecutionYear getExecutionYear() {
        final LocalDate date = retrieveDateForExecutionYear();
        if (date != null) {
            return ExecutionYear.readByDateTime(date);
        } else {
            return null;
        }
    }

    private LocalDate retrieveDateForExecutionYear() {
        if (getProcessBean().getStartDevelopmentDate() != null) {
            return getProcessBean().getStartDevelopmentDate();
        }
        if (getProcessBean().getRatificationDate() != null) {
            return getProcessBean().getRatificationDate();
        }
        if (getProcessBean().getStartProcessDate() != null) {
            return getProcessBean().getStartProcessDate();
        }
        return null;
    }

    public PhdMigrationProcessStateType estimatedFinalMigrationStatus() {
        if (isProcessCanceled()) {
            return PhdMigrationProcessStateType.CANCELED;
        }

        if (getProcessBean().getEdictDate() != null || getProcessBean().getClassification() != null) {
            return PhdMigrationProcessStateType.CONCLUDED;
        }

        if (getProcessBean().getFirstDiscussionDate() != null || getProcessBean().getSecondDiscussionDate() != null) {
            return PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION;
        }

        if (getProcessBean().getRequirementDate() != null) {
            return PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION;
        }

        if (getProcessBean().getStartDevelopmentDate() != null || getProcessBean().getRatificationDate() != null) {
            return PhdMigrationProcessStateType.WORK_DEVELOPMENT;
        }

        if (getProcessBean().getRatificationDate() != null) {
            return PhdMigrationProcessStateType.CANDIDACY_RATIFIED;
        }

        if (getProcessBean().getStartProcessDate() != null) {
            return PhdMigrationProcessStateType.CANDIDACY_CREATED;
        }

        return PhdMigrationProcessStateType.NOT_MIGRATED;
    }

    public boolean possibleToCompleteNextState() {
        final PhdMigrationProcessStateType activeState = getMigrationStatus();

        if (activeState.equals(PhdMigrationProcessStateType.CANCELED)
                || activeState.equals(PhdMigrationProcessStateType.CONCLUDED)) {
            return false;
        }

        if (getProcessBean().getClassification() != null) {
            return true;
        }

        if (activeState.equals(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION)) {
            if (getProcessBean().getEdictDate() != null) {
                return true;
            }
        }

        if (activeState.equals(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION)) {
            if (getProcessBean().getFirstDiscussionDate() != null || getProcessBean().getSecondDiscussionDate() != null) {
                return true;
            }
        }

        if (activeState.equals(PhdMigrationProcessStateType.WORK_DEVELOPMENT)) {
            if (getProcessBean().getRequirementDate() != null) {
                return true;
            }
        }

        if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_RATIFIED)) {
            if (getProcessBean().getStartDevelopmentDate() != null || getProcessBean().getRatificationDate() != null) {
                return true;
            }
        }

        if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_CREATED)) {
            if (getProcessBean().getRatificationDate() != null) {
                return true;
            }
        }

        if (activeState.equals(PhdMigrationProcessStateType.NOT_MIGRATED)) {
            if (getProcessBean().getStartProcessDate() != null) {
                return true;
            }
        }

        return false;
    }

    private boolean isProcessCanceled() {
        return getProcessBean().getAnnulmentDate() != null;
    }

    @Atomic
    public Boolean proceedWithMigration(User userView) {

        PhdMigrationProcessStateType activeState;
        PhdIndividualProgramProcess individualProcess = null;

        boolean returnVal = false;

        while (possibleToCompleteNextState()) {
            activeState = getMigrationStatus();

            returnVal = true;

            if (activeState.equals(PhdMigrationProcessStateType.NOT_MIGRATED)) {
                individualProcess = createCandidacyProcess(userView);
                sendCandidacyToCoordinator(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.CANDIDACY_CREATED);
                continue;
            }

            if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_CREATED)) {
                ratifyCandidacyProcess(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.CANDIDACY_RATIFIED);
                continue;
            }

            if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_RATIFIED)) {
                formalizeRegistration(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.WORK_DEVELOPMENT);
                continue;
            }

            if (activeState.equals(PhdMigrationProcessStateType.WORK_DEVELOPMENT)) {
                requirePublicThesisPresentation(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION);
                continue;
            }

            if (activeState.equals(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION)) {
                skipJuryActivities(userView, individualProcess);
                manageMeetingsAndFinalThesis(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION);
                continue;
            }

            if (activeState.equals(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION)) {
                ratifyFinalThesis(userView, individualProcess);
                setMigrationStatus(PhdMigrationProcessStateType.CONCLUDED);
                continue;
            }

            break;
        }

        if (isProcessCanceled()) {
            cancelPhdProgram(userView, individualProcess, getProcessBean().getAnnulmentDate());
            setMigrationStatus(PhdMigrationProcessStateType.CANCELED);
        }

        activeState = getMigrationStatus();
        if (!activeState.equals(estimatedFinalMigrationStatus())) {
            throw new FinalEstimatedStateNotReachedException("Estimated: " + estimatedFinalMigrationStatus() + "\treached: "
                    + activeState);
        }

        setMigrationDate(new DateTime());

        return returnVal;
    }

    private PhdIndividualProgramProcess createCandidacyProcess(final User userView) {
        final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean();

        candidacyBean.setCandidacyDate(getProcessBean().getStartProcessDate());
        candidacyBean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION);
        candidacyBean.setPersonBean(getPhdMigrationIndividualPersonalData().getPersonBean());
        candidacyBean.setMigratedProcess(true);
        candidacyBean.setProgram(getProcessBean().getPhdProgram());
        candidacyBean.setThesisTitle(getProcessBean().getTitle());
        candidacyBean.setPhdStudentNumber(getPhdMigrationIndividualPersonalData().getNumber());
        candidacyBean.setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
        candidacyBean.setExecutionYear(getExecutionYear());
        candidacyBean.setFocusArea((getProcessBean().getPhdProgram().getPhdProgramFocusAreasSet().size() == 1) ? getProcessBean()
                .getPhdProgram().getPhdProgramFocusAreas().iterator().next() : null);

        final PhdIndividualProgramProcess individualProcess =
                (PhdIndividualProgramProcess) CreateNewProcess.run(PhdIndividualProgramProcess.class, candidacyBean);

        return individualProcess;
    }

    private void sendCandidacyToCoordinator(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();

        final PhdProgramCandidacyProcessStateBean reviewBean =
                new PhdProgramCandidacyProcessStateBean(candidacyProcess.getIndividualProgramProcess());
        reviewBean.setState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
        reviewBean.setGenerateAlert(false);
        ExecuteProcessActivity
                .run(candidacyProcess,
                        net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestCandidacyReview.class.getSimpleName(),
                        reviewBean);

        final PhdProgramCandidacyProcessStateBean requestRatifyBean = new PhdProgramCandidacyProcessStateBean(individualProcess);
        requestRatifyBean.setGenerateAlert(false);
        requestRatifyBean.setState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);
        ExecuteProcessActivity.run(candidacyProcess,
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestRatifyCandidacy.class.getSimpleName(),
                requestRatifyBean);
    }

    private void ratifyCandidacyProcess(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();
        final RatifyCandidacyBean ratifyBean = new RatifyCandidacyBean(candidacyProcess);
        ratifyBean.setWhenRatified(getProcessBean().getRatificationDate());
        ExecuteProcessActivity.run(candidacyProcess,
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.RatifyCandidacy.class.getSimpleName(), ratifyBean);
    }

    private void formalizeRegistration(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdIndividualProgramProcessBean individualProcessBean = new PhdIndividualProgramProcessBean(individualProcess);
        individualProcessBean.setQualificationExamsPerformed(QualificationExamsResult.NO);
        individualProcessBean.setQualificationExamsRequired(QualificationExamsResult.NO);
        ExecuteProcessActivity.run(individualProcess, EditQualificationExams.class.getSimpleName(), individualProcessBean);

        final PhdStudyPlanBean planBean = new PhdStudyPlanBean(individualProcess);
        planBean.setExempted(true);
        ExecuteProcessActivity.run(individualProcess, AddStudyPlan.class.getSimpleName(), planBean);

        final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();
        final RegistrationFormalizationBean formalizationBean = new RegistrationFormalizationBean(candidacyProcess);
        formalizationBean.setWhenStartedStudies(getMostAccurateStartDevelopmentDate());
        formalizationBean.setSelectRegistration(false);
        ExecuteProcessActivity.run(candidacyProcess,
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.RegistrationFormalization.class.getSimpleName(),
                formalizationBean);
    }

    private LocalDate getMostAccurateStartDevelopmentDate() {
        if (getProcessBean().getStartDevelopmentDate() != null) {
            return getProcessBean().getStartDevelopmentDate();
        } else {
            return getProcessBean().getRatificationDate();
        }
    }

    private void requirePublicThesisPresentation(final User userView, final PhdIndividualProgramProcess individualProcess) {
        ExecuteProcessActivity.run(individualProcess, ExemptPublicPresentationSeminarComission.class.getSimpleName(),
                new PublicPresentationSeminarProcessBean());

        if (!StringUtils.isEmpty(getProcessBean().getGuiderNumber())) {
            final PhdMigrationGuiding migrationGuiding = getGuiding(getProcessBean().getGuiderNumber());
            if (migrationGuiding != null) {
                final PhdParticipantBean guidingBean = migrationGuiding.getPhdParticipantBean(individualProcess);
                ExecuteProcessActivity.run(individualProcess, AddGuidingInformation.class.getSimpleName(), guidingBean);
            }
        }

        if (!StringUtils.isEmpty(getProcessBean().getAssistantGuiderNumber())) {
            final PhdMigrationGuiding migrationAssistantGuiding = getGuiding(getProcessBean().getAssistantGuiderNumber());
            if (migrationAssistantGuiding != null) {
                final PhdParticipantBean assistantGuidingBean =
                        migrationAssistantGuiding.getPhdParticipantBean(individualProcess);
                ExecuteProcessActivity.run(individualProcess, AddAssistantGuidingInformation.class.getSimpleName(),
                        assistantGuidingBean);
            }
        }

        final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean(individualProcess);
        thesisBean.setWhenThesisDiscussionRequired(getProcessBean().getRequirementDate());
        thesisBean.setGenerateAlert(false);
        thesisBean.setToNotify(false);
        ExecuteProcessActivity.run(individualProcess, RequestPublicThesisPresentation.class.getSimpleName(), thesisBean);
    }

    private PhdMigrationGuiding getGuiding(String guidingNumber) {
        String alternativeGuidingNumber = "0".concat(guidingNumber);
        for (PhdMigrationGuiding migrationGuiding : getPhdMigrationProcess().getPhdMigrationGuiding()) {
            if (guidingNumber.equals(migrationGuiding.getTeacherNumber())
                    || alternativeGuidingNumber.equals(migrationGuiding.getTeacherNumber())) {
                return migrationGuiding;
            }
        }

        return null;
        // throw new
        // PhdMigrationGuidingNotFoundException("Did not find guiding with code: "
        // + guidingNumber);
    }

    private void skipJuryActivities(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
        final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
        thesisBean.setThesisProcess(thesisProcess);
        thesisBean.setToNotify(false);
        thesisBean.setGenerateAlert(false);
        ExecuteProcessActivity.run(individualProcess.getThesisProcess(), SkipThesisJuryActivities.class.getSimpleName(),
                thesisBean);
    }

    private void manageMeetingsAndFinalThesis(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
        final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
        thesisBean.setThesisProcess(thesisProcess);
        thesisBean.setToNotify(false);
        thesisBean.setGenerateAlert(false);
        final PhdMeetingSchedulingProcess meetingProcess = thesisProcess.getMeetingProcess();
        ExecuteProcessActivity.run(meetingProcess, ScheduleFirstThesisMeetingRequest.class, thesisBean);

        thesisBean.setScheduledDate(getMeetingDate());
        thesisBean.setScheduledPlace("");
        ExecuteProcessActivity.run(meetingProcess, SkipScheduleFirstThesisMeeting.class, thesisBean);

        thesisBean.setScheduledDate(getMostAccurateDiscussionDateTime());
        thesisBean.setScheduledPlace("");
        ExecuteProcessActivity.run(thesisProcess, SkipScheduleThesisDiscussion.class, thesisBean);

        ExecuteProcessActivity.run(thesisProcess, SubmitThesis.class, thesisBean);
    }

    private DateTime getMostAccurateDiscussionDateTime() {
        if (getProcessBean().getSecondDiscussionDate() != null) {
            return getProcessBean().getSecondDiscussionDate().toDateTimeAtCurrentTime();
        } else if (getProcessBean().getFirstDiscussionDate() != null) {
            return getProcessBean().getFirstDiscussionDate().toDateTimeAtCurrentTime();
        }

        return null;
    }

    private DateTime getMeetingDate() {
        if (getProcessBean().getMeetingDate() != null) {
            return getProcessBean().getMeetingDate().toDateTimeAtCurrentTime();
        } else {
            return null;
        }
    }

    private void ratifyFinalThesis(final User userView, final PhdIndividualProgramProcess individualProcess) {
        final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
        final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
        thesisBean.setThesisProcess(thesisProcess);
        thesisBean.setToNotify(false);
        thesisBean.setGenerateAlert(false);
        thesisBean.setWhenFinalThesisRatified(getProcessBean().getEdictDate());
        ExecuteProcessActivity.run(thesisProcess, RatifyFinalThesis.class, thesisBean);

        thesisBean.setConclusionDate(getProcessBean().getEdictDate());
        thesisBean.setFinalGrade(getProcessBean().getClassification());
        ExecuteProcessActivity.run(thesisProcess, SetFinalGrade.class, thesisBean);
    }

    private void cancelPhdProgram(final User userView, final PhdIndividualProgramProcess individualProcess,
            LocalDate anullmentDate) {
        final PhdIndividualProgramProcessBean processBean = new PhdIndividualProgramProcessBean(individualProcess);
        processBean.setStateDate(anullmentDate);
        ExecuteProcessActivity.run(individualProcess, CancelPhdProgramProcess.class.getSimpleName(), processBean);
    }

    @Deprecated
    public boolean hasMigrationDate() {
        return getMigrationDate() != null;
    }

    @Deprecated
    public boolean hasData() {
        return getData() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMigrationStatus() {
        return getMigrationStatus() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasPhdMigrationIndividualPersonalData() {
        return getPhdMigrationIndividualPersonalData() != null;
    }

    @Deprecated
    public boolean hasPhdMigrationProcess() {
        return getPhdMigrationProcess() != null;
    }

    @Deprecated
    public boolean hasParseLog() {
        return getParseLog() != null;
    }

}
