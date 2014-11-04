/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.candidacy.feedbackRequest;

import java.util.Collections;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class PhdCandidacyFeedbackRequestElement extends PhdCandidacyFeedbackRequestElement_Base {

    protected PhdCandidacyFeedbackRequestElement() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdCandidacyFeedbackRequestElement init(final PhdCandidacyFeedbackRequestProcess process,
            PhdParticipant participant, PhdCandidacyFeedbackRequestElementBean bean) {

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.invalid.process", args);
        }
        checkParticipant(process, participant);

        setProcess(process);
        setParticipant(participant);

        setMailSubject(bean.getMailSubject());
        setMailBody(bean.getMailBody());

        return this;
    }

    private void checkParticipant(final PhdCandidacyFeedbackRequestProcess process, final PhdParticipant participant) {
        String[] args = {};
        if (participant == null) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.participant.cannot.be.null", args);
        }

        /*
         * Can not have more than one element for the same process
         */
        for (final PhdCandidacyFeedbackRequestElement element : participant.getCandidacyFeedbackRequestElementsSet()) {
            if (element.getProcess() != null && element.getProcess().equals(process)) {
                throw new DomainException(
                        "error.PhdCandidacyFeedbackRequestElement.participant.already.has.jury.element.in.process");
            }
        }
    }

    public PhdProgramCandidacyProcess getCandidacyProcess() {
        return getProcess().getCandidacyProcess();
    }

    @Atomic
    public void delete() {
        checkIfCanBeDeleted();
        disconnect();
        deleteDomainObject();
    }

    private void checkIfCanBeDeleted() {
        if (!getFeedbackDocumentsSet().isEmpty()) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.has.feedback.documents");
        }
    }

    private void disconnect() {

        final PhdParticipant participant = getParticipant();
        setParticipant(null);
        participant.tryDelete();

        setProcess(null);
        setRootDomainObject(null);
    }

    public PhdCandidacyFeedbackRequestDocument getLastFeedbackDocument() {
        return !getFeedbackDocumentsSet().isEmpty() ? Collections.max(getFeedbackDocumentsSet(),
                PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public String getNameWithTitle() {
        return getParticipant().getNameWithTitle();
    }

    public String getName() {
        return getParticipant().getName();
    }

    public String getEmail() {
        return getParticipant().getEmail();
    }

    public boolean isFeedbackSubmitted() {
        return !getFeedbackDocumentsSet().isEmpty();
    }

    public boolean isFor(PhdCandidacyFeedbackRequestProcess process) {
        return getProcess().equals(process);
    }

    public boolean isFor(Person person) {
        return getParticipant().isFor(person);
    }

    static public PhdCandidacyFeedbackRequestElement create(final PhdCandidacyFeedbackRequestProcess process,
            final PhdCandidacyFeedbackRequestElementBean bean) {

        return new PhdCandidacyFeedbackRequestElement().init(process,
                PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(), bean), bean);
    }

    static public PhdCandidacyFeedbackRequestElement create(final PhdCandidacyFeedbackRequestProcess process,
            final PhdParticipant participant, final PhdCandidacyFeedbackRequestElementBean bean) {
        return new PhdCandidacyFeedbackRequestElement().init(process, participant, bean);
    }

}
