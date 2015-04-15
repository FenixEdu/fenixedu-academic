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
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.util.Comparator;

import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.LocalDate;

public class ErasmusAlert extends ErasmusAlert_Base {

    public static final Comparator<ErasmusAlert> WHEN_CREATED_COMPARATOR = new Comparator<ErasmusAlert>() {

        @Override
        public int compare(ErasmusAlert o1, ErasmusAlert o2) {
            return o1.getWhenCreated().compareTo(o2.getWhenCreated());
        }

    };

    public ErasmusAlert() {
        super();
    }

    public ErasmusAlert(MobilityIndividualApplicationProcess process, Boolean sendEmail, LocalDate whenToFire,
            final MultiLanguageString subject, final MultiLanguageString body, ErasmusAlertEntityType entity) {
        this();
        init(process, sendEmail, whenToFire, subject, body, entity);
    }

    protected void init(MobilityIndividualApplicationProcess process, Boolean sendEmail, LocalDate whenToFire,
            final MultiLanguageString subject, final MultiLanguageString body, ErasmusAlertEntityType entity) {
        super.init(subject, body);
        String[] args = {};

        if (whenToFire == null) {
            throw new DomainException(
                    "org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess.fireDate.cannot.be.null",
                    args);
        }

        super.setSendEmail(sendEmail);
        super.setWhenToFire(whenToFire);
        super.setEntity(entity);
        super.setProcess(process);

        super.setWhoCreated(AccessControl.getPerson());
    }

    @Override
    protected void generateMessage() {
        // new Message(getRootDomainObject().getSystemSender(), null,
        // Collections.EMPTY_LIST, buildMailSubject(), buildMailBody(),
        // getProcess().getPersonalDetails().getEmail());
    }

    protected String buildMailBody() {
        final StringBuilder result = new StringBuilder();

        for (final String eachContent : getFormattedBody().getAllContents()) {
            result.append(eachContent).append("\n").append(" ------------------------- ");
        }

        result.delete(result.lastIndexOf("\n") + 1, result.length());

        return result.toString();

    }

    protected String buildMailSubject() {
        final StringBuilder result = new StringBuilder();

        for (final String eachContent : getFormattedSubject().getAllContents()) {
            result.append(eachContent).append(" / ");
        }

        if (result.toString().endsWith(" / ")) {
            result.delete(result.length() - 3, result.length());
        }

        return result.toString();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected boolean isToDiscard() {
        return !isToSendMail();
    }

    @Override
    public boolean isToFire() {
        return getFireDate() == null;
    }

    @Override
    public boolean isToSendMail() {
        return getSendEmail() && getProcess().getCandidacyProcess().hasOpenCandidacyPeriod();
    }

}
