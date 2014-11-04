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
package org.fenixedu.academic.domain.phd.thesis;

import java.util.Collections;
import java.util.Comparator;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.PhdThesisReportFeedbackDocument;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class ThesisJuryElement extends ThesisJuryElement_Base {

    static public final Comparator<ThesisJuryElement> COMPARATOR_BY_ELEMENT_ORDER = new Comparator<ThesisJuryElement>() {
        @Override
        public int compare(ThesisJuryElement o1, ThesisJuryElement o2) {
            return o1.getElementOrder().compareTo(o2.getElementOrder());
        }
    };

    static public ThesisJuryElement createPresident(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {

        if (process.getPresidentJuryElement() != null) {
            throw new DomainException("error.ThesisJuryElement.president.already.exists");
        }

        final PhdParticipant participant = PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(), bean);
        final ThesisJuryElement element = new ThesisJuryElement();

        element.checkParticipant(process, participant, true);
        element.setElementOrder(0);
        element.setProcessForPresidentJuryElement(process);
        element.setParticipant(participant);
        element.setReporter(false);
        element.setExpert(false);

        return element;
    }

    protected ThesisJuryElement() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDate(new DateTime());
    }

    protected ThesisJuryElement init(final PhdThesisProcess process, PhdParticipant participant, PhdThesisJuryElementBean bean) {

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.ThesisJuryElement.invalid.process", args);
        }
        checkParticipant(process, participant, false);

        setElementOrder(generateNextElementOrder(process));
        setProcess(process);
        setParticipant(participant);
        setReporter(bean.isReporter());
        setExpert(bean.isExpert());

        return this;
    }

    private void checkParticipant(final PhdThesisProcess process, final PhdParticipant participant, boolean isPresident) {
        String[] args = {};
        if (participant == null) {
            throw new DomainException("error.ThesisJuryElement.participant.cannot.be.null", args);
        }

        /*
         * Can not have more than one jury element for the same process, but
         * allow to be President and Jury Member at the same time
         */
        for (final ThesisJuryElement element : participant.getThesisJuryElementsSet()) {
            if (element.getProcess() != null && element.getProcess().equals(process) && !isPresident && !element.isPresident()) {
                throw new DomainException("error.ThesisJuryElement.participant.already.has.jury.element.in.process");
            }
        }
    }

    private Integer generateNextElementOrder(final PhdThesisProcess process) {
        if (process.getThesisJuryElementsSet().isEmpty()) {
            return Integer.valueOf(1);
        }
        return Integer.valueOf(Collections.max(process.getThesisJuryElementsSet(), ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER)
                .getElementOrder().intValue() + 1);
    }

    @Atomic
    public void delete() {
        checkIfCanBeDeleted();
        disconnect();
        deleteDomainObject();
    }

    private void checkIfCanBeDeleted() {
        if (!getFeedbackDocumentsSet().isEmpty()) {
            throw new DomainException("error.ThesisJuryElement.has.feedback.documents");
        }
    }

    protected void disconnect() {

        final PhdParticipant participant = getParticipant();
        setParticipant(null);
        participant.tryDelete();

        setProcess(null);
        setProcessForPresidentJuryElement(null);
        setRootDomainObject(null);
    }

    public boolean isInternal() {
        return getParticipant().isInternal();
    }

    public String getName() {
        return getParticipant().getName();
    }

    public String getNameWithTitle() {
        return getParticipant().getNameWithTitle();
    }

    public String getNameWithTitleAndRoleOnProcess() {
        StringBuilder stringBuilder = new StringBuilder(getNameWithTitle());
        if (getProcess().getIndividualProgramProcess().isGuider(getParticipant())) {
            stringBuilder.append(" (").append(BundleUtil.getString(Bundle.PHD, "label.phd.guiding")).append(")");
        }

        if (getProcess().getIndividualProgramProcess().isAssistantGuider(getParticipant())) {
            stringBuilder.append(" (").append(BundleUtil.getString(Bundle.PHD, "label.phd.assistant.guiding")).append(")");
        }

        return stringBuilder.toString();
    }

    public String getQualification() {
        return getParticipant().getQualification();
    }

    public String getCategory() {
        return getParticipant().getCategory();
    }

    public String getWorkLocation() {
        return getParticipant().getWorkLocation();
    }

    public String getInstitution() {
        return getParticipant().getInstitution();
    }

    public String getAddress() {
        return getParticipant().getAddress();
    }

    public String getPhone() {
        return getParticipant().getPhone();
    }

    public String getEmail() {
        return getParticipant().getEmail();
    }

    public String getTitle() {
        return getParticipant().getTitle();
    }

    public boolean isTopElement() {
        return getElementOrder().intValue() == 1;
    }

    public boolean isBottomElement() {
        return getElementOrder().intValue() == getProcess().getThesisJuryElementsSet().size();
    }

    static public ThesisJuryElement create(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
        return new ThesisJuryElement().init(process,
                PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(), bean), bean);
    }

    public boolean isGuidingOrAssistantGuiding() {
        return getParticipant().isGuidingOrAssistantGuiding();
    }

    public boolean isMainGuiding() {
        return getParticipant().getProcessForGuiding() != null;
    }

    public boolean isAssistantGuiding() {
        return getParticipant().getProcessForAssistantGuiding() != null;
    }

    public boolean isFor(final PhdThesisProcess process) {
        return getProcess().equals(process);
    }

    public PhdThesisReportFeedbackDocument getLastFeedbackDocument() {
        return !getFeedbackDocumentsSet().isEmpty() ? Collections.max(getFeedbackDocumentsSet(),
                PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public boolean isFor(final Person person) {
        return getParticipant().isFor(person);
    }

    public boolean isDocumentValidated() {
        final PhdThesisReportFeedbackDocument document = getLastFeedbackDocument();
        return document != null && document.isAssignedToProcess();
    }

    public void edit(final PhdThesisJuryElementBean bean) {
        getParticipant().edit(bean);
        setReporter(bean.isReporter());
        setExpert(bean.isExpert());
    }

    public boolean isJuryValidated() {
        return getProcess().isJuryValidated();
    }

    public boolean isPresident() {
        return getProcessForPresidentJuryElement() != null;
    }

}
