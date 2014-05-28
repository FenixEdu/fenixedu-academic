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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class ApprovedLearningAgreementDocumentFile extends ApprovedLearningAgreementDocumentFile_Base {

    public static final Comparator<ApprovedLearningAgreementDocumentFile> SUBMISSION_DATE_COMPARATOR =
            new Comparator<ApprovedLearningAgreementDocumentFile>() {

                @Override
                public int compare(ApprovedLearningAgreementDocumentFile o1, ApprovedLearningAgreementDocumentFile o2) {
                    return o1.getUploadTime().compareTo(o2.getUploadTime());
                }
            };

    private ApprovedLearningAgreementDocumentFile() {
        super();
        this.setCandidacyFileActive(Boolean.TRUE);
    }

    protected ApprovedLearningAgreementDocumentFile(byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        setCandidacyFileType(IndividualCandidacyDocumentFileType.APPROVED_LEARNING_AGREEMENT);
        init(filename, filename, contents, NobodyGroup.get());
    }

    @Atomic
    public static ApprovedLearningAgreementDocumentFile createCandidacyDocument(byte[] contents, String filename,
            String processName, String documentIdNumber) {
        return new ApprovedLearningAgreementDocumentFile(contents, filename);
    }

    @Atomic
    public void markLearningAgreementViewed() {
        new ApprovedLearningAgreementExecutedAction(this, ExecutedActionType.VIEWED_APPROVED_LEARNING_AGREEMENT);
    }

    @Atomic
    public void markLearningAgreementSent() {
        new ApprovedLearningAgreementExecutedAction(this, ExecutedActionType.SENT_APPROVED_LEARNING_AGREEMENT);
    }

    public boolean isApprovedLearningAgreementSent() {
        return !getSentLearningAgreementActions().isEmpty();
    }

    public boolean isApprovedLearningAgreementViewed() {
        return !getViewedLearningAgreementActions().isEmpty();
    }

    protected List<ApprovedLearningAgreementExecutedAction> getSentLearningAgreementActions() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList =
                new ArrayList<ApprovedLearningAgreementExecutedAction>();

        CollectionUtils.select(getExecutedActions(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((ApprovedLearningAgreementExecutedAction) arg0).isSentLearningAgreementAction();
            };

        }, executedActionList);

        Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

        return executedActionList;
    }

    public ApprovedLearningAgreementExecutedAction getMostRecentSentLearningAgreementAction() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList = getSentLearningAgreementActions();

        return executedActionList.isEmpty() ? null : executedActionList.iterator().next();
    }

    public DateTime getMostRecentSentLearningAgreementActionWhenOccured() {
        if (getMostRecentSentLearningAgreementAction() == null) {
            return null;
        }

        return getMostRecentSentLearningAgreementAction().getWhenOccured();
    }

    protected List<ApprovedLearningAgreementExecutedAction> getViewedLearningAgreementActions() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList =
                new ArrayList<ApprovedLearningAgreementExecutedAction>();

        CollectionUtils.select(getExecutedActions(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((ApprovedLearningAgreementExecutedAction) arg0).isViewedLearningAgreementAction();
            };

        }, executedActionList);

        Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

        return executedActionList;
    }

    public ApprovedLearningAgreementExecutedAction getMostRecentViewedLearningAgreementAction() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList = getViewedLearningAgreementActions();

        return executedActionList.isEmpty() ? null : executedActionList.iterator().next();
    }

    public DateTime getMostRecentViewedLearningAgreementActionWhenOccured() {
        if (getMostRecentViewedLearningAgreementAction() == null) {
            return null;
        }

        return getMostRecentViewedLearningAgreementAction().getWhenOccured();
    }

    protected List<ApprovedLearningAgreementExecutedAction> getSentEmailAcceptedStudentActions() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList =
                new ArrayList<ApprovedLearningAgreementExecutedAction>();

        CollectionUtils.select(getExecutedActions(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((ApprovedLearningAgreementExecutedAction) arg0).isSentEmailAcceptedStudent();
            };

        }, executedActionList);

        Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

        return executedActionList;
    }

    public ApprovedLearningAgreementExecutedAction getMostRecentSentEmailAcceptedStudentAction() {
        List<ApprovedLearningAgreementExecutedAction> executedActionList = getSentEmailAcceptedStudentActions();

        return executedActionList.isEmpty() ? null : executedActionList.iterator().next();
    }

    public DateTime getMostRecentSentEmailAcceptedStudentActionWhenOccured() {
        if (getMostRecentSentEmailAcceptedStudentAction() == null) {
            return null;
        }

        return getMostRecentSentEmailAcceptedStudentAction().getWhenOccured();
    }

    public boolean isMostRecent() {
        return getMobilityIndividualApplication().getMostRecentApprovedLearningAgreement() == this;
    }

    public MobilityIndividualApplicationProcess getProcess() {
        return getMobilityIndividualApplication().getCandidacyProcess();
    }

    public boolean isAbleToSendEmailToAcceptStudent() {
        return getProcess().isStudentAccepted() && isMostRecent() && getCandidacyFileActive();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ApprovedLearningAgreementExecutedAction> getExecutedActions() {
        return getExecutedActionsSet();
    }

    @Deprecated
    public boolean hasAnyExecutedActions() {
        return !getExecutedActionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasMobilityIndividualApplication() {
        return getMobilityIndividualApplication() != null;
    }

}
