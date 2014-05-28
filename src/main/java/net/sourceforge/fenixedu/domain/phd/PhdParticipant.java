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
package net.sourceforge.fenixedu.domain.phd;

import java.security.SecureRandom;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessTypeList;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

abstract public class PhdParticipant extends PhdParticipant_Base {

    protected PhdParticipant() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        setAccessTypes(PhdProcessAccessTypeList.EMPTY);
    }

    protected void init(PhdIndividualProgramProcess individualProcess) {
        String[] args = {};
        if (individualProcess == null) {
            throw new DomainException("error.PhdParticipant.individualProcess.cannot.be.null", args);
        }
        super.setIndividualProcess(individualProcess);
    }

    private void delete() {
        disconnect();
        deleteDomainObject();
    }

    protected void disconnect() {
        setIndividualProcess(null);
        setProcessForGuiding(null);
        setProcessForAssistantGuiding(null);
        setRootDomainObject(null);
        setAcceptanceLetter(null);
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getAddress();

    abstract public String getEmail();

    abstract public String getPhone();

    abstract public boolean isTeacher();

    public String getNameWithTitle() {
        return StringUtils.isEmpty(getTitle()) ? getName() : getTitle() + " " + getName();
    }

    public boolean isFor(Person person) {
        return false;
    }

    public boolean isInternal() {
        return false;
    }

    public boolean isFor(PhdIndividualProgramProcess process) {
        return getIndividualProcess().equals(process);
    }

    public void addAccessType(PhdProcessAccessType... types) {
        /*
         * TODO: this method is not being invoked on every email we sent to
         * external participants.
         */
        ensureExternalAccess();
        setAccessTypes(getAccessTypes().addAccessTypes(types));
    }

    public void ensureExternalAccess() {
        if (StringUtils.isEmpty(getAccessHashCode())) {
            super.setAccessHashCode(UUID.randomUUID().toString());
            super.setPassword(RandomStringUtils.random(15, 0, 0, true, true, null, new SecureRandom()));
        }
    }

    public void checkAccessCredentials(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || !hasAccessHashCode()) {
            throw new DomainException("error.PhdParticipant.credential.not.valid");
        }

        if (!getEmail().equals(email) || !getPassword().equals(password)) {
            throw new DomainException("error.PhdParticipant.credential.not.valid");
        }

    }

    private boolean hasAccessHashCode() {
        return !StringUtils.isEmpty(getAccessHashCode());
    }

    public void tryDelete() {
        if (canBeDeleted()) {
            delete();
        }
    }

    protected boolean canBeDeleted() {
        return !(hasAnyThesisJuryElements() || hasProcessForGuiding() || hasProcessForAssistantGuiding()
                || hasAnyCandidacyFeedbackRequestElements() || isParticipantCoordinator());
    }

    private boolean isParticipantCoordinator() {
        if (!isInternal()) {
            return false;
        }

        InternalPhdParticipant internalParticipant = (InternalPhdParticipant) this;
        return getIndividualProcess().getPhdProgram().isCoordinatorFor(internalParticipant.getPerson(),
                getIndividualProcess().getExecutionYear());
    }

    public boolean isGuidingOrAssistantGuiding() {
        return hasProcessForGuiding() || hasProcessForAssistantGuiding();
    }

    /*
     * Actually each participant belongs to one process, so it will have only
     * thesis jury element, but assuming that we can share participants we have
     * several thesis jury elements
     */
    public ThesisJuryElement getThesisJuryElement(final PhdThesisProcess process) {
        for (final ThesisJuryElement element : getThesisJuryElementsSet()) {
            if (element.isFor(process)) {
                return element;
            }
        }
        return null;
    }

    public PhdCandidacyFeedbackRequestElement getPhdCandidacyFeedbackRequestElement(
            final PhdCandidacyFeedbackRequestProcess process) {
        for (final PhdCandidacyFeedbackRequestElement element : getCandidacyFeedbackRequestElements()) {
            if (element.isFor(process)) {
                return element;
            }
        }
        return null;
    }

    private boolean hasAccessHashCode(final String hash) {
        return !StringUtils.isEmpty(getAccessHashCode()) && getAccessHashCode().equals(hash);
    }

    static public PhdParticipant getUpdatedOrCreate(final PhdIndividualProgramProcess process, final PhdParticipantBean bean) {

        if (bean.hasParticipant()) {
            bean.getParticipant().updateTitleIfNecessary(bean);
            return bean.getParticipant();
        }

        if (bean.isInternal()) {
            return new InternalPhdParticipant(process, bean);
        } else {
            return new ExternalPhdParticipant(process, bean);
        }
    }

    private void updateTitleIfNecessary(final PhdParticipantBean bean) {
        if (StringUtils.isEmpty(getTitle())) {
            setTitle(bean.getTitle());
        }
    }

    public void edit(final PhdParticipantBean bean) {
        setTitle(bean.getTitle());
        setCategory(bean.getCategory());
        setWorkLocation(bean.getWorkLocation());
        setInstitution(bean.getInstitution());
    }

    static public PhdParticipant readByAccessHashCode(final String hash) {
        for (final PhdParticipant participant : Bennu.getInstance().getPhdParticipantsSet()) {
            if (participant.hasAccessHashCode(hash)) {
                return participant;
            }
        }

        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement> getThesisJuryElements() {
        return getThesisJuryElementsSet();
    }

    @Deprecated
    public boolean hasAnyThesisJuryElements() {
        return !getThesisJuryElementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement> getCandidacyFeedbackRequestElements() {
        return getCandidacyFeedbackRequestElementsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyFeedbackRequestElements() {
        return !getCandidacyFeedbackRequestElementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAcceptanceLetter() {
        return getAcceptanceLetter() != null;
    }

    @Deprecated
    public boolean hasIndividualProcess() {
        return getIndividualProcess() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasAccessTypes() {
        return getAccessTypes() != null;
    }

    @Deprecated
    public boolean hasPassword() {
        return getPassword() != null;
    }

    @Deprecated
    public boolean hasProcessForGuiding() {
        return getProcessForGuiding() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasCategory() {
        return getCategory() != null;
    }

    @Deprecated
    public boolean hasWorkLocation() {
        return getWorkLocation() != null;
    }

    @Deprecated
    public boolean hasProcessForAssistantGuiding() {
        return getProcessForAssistantGuiding() != null;
    }

    @Deprecated
    public boolean hasPhdReporterReviewAlert() {
        return getPhdReporterReviewAlert() != null;
    }

}
