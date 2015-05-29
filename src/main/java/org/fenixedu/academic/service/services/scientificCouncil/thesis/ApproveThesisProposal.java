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
package org.fenixedu.academic.service.services.scientificCouncil.thesis;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.domain.thesis.ThesisState;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.Atomic;

public class ApproveThesisProposal extends ThesisServiceWithMailNotification {

    private static final int FIELD_ON = 1;
    private static final int FIELD_OFF = 0;

    private static final String SUBJECT_KEY = "thesis.proposal.jury.approve.subject";
    private static final String BODY_KEY = "thesis.proposal.jury.approve.body";
    private static final String NO_DATE_KEY = "thesis.proposal.jury.approve.body.noDate";
    private static final String WITH_DATE_KEY = "thesis.proposal.jury.approve.body.withDate";
    private static final String COORDINATOR_SENDER = "thesis.proposal.jury.approve.body.sender.coordinator";
    private static final String COUNCIL_MEMBER_SENDER = "thesis.proposal.jury.approve.body.sender.council";
    private static final String COUNCIL_MEMBER_ROLE = "thesis.proposal.jury.approve.body.role.council";

    @Override
    void process(Thesis thesis) {
        if (thesis.getState() != ThesisState.APPROVED) {
            thesis.approveProposal();
        }
    }

    @Override
    protected Collection<String> getReceiversEmails(Thesis thesis) {
        Set<String> persons =
                thesis.getAllParticipants(ThesisParticipationType.ORIENTATOR, ThesisParticipationType.COORIENTATOR,
                        ThesisParticipationType.PRESIDENT, ThesisParticipationType.VOWEL).stream().map(p -> p.getEmail())
                        .collect(Collectors.toSet());
        persons.add(thesis.getStudent().getPerson().getProfile().getEmail());

        // also send proposal approval to the contact team
        ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
        for (ScientificCommission member : thesis.getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson().getProfile().getEmail());
            }
        }

        return persons;
    }

    @Override
    protected String getSubject(Thesis thesis) {
        return getMessage(I18N.getLocale(), SUBJECT_KEY, thesis.getTitle().getContent());
    }

    @Override
    protected String getMessage(Thesis thesis) {
        Locale locale = I18N.getLocale();
        Person currentPerson = AccessControl.getPerson();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        String institutionName = Bennu.getInstance().getInstitutionUnit().getPartyName().getContent(locale);

        String title = thesis.getTitle().getContent();
        String year = executionYear.getYear();
        String degreeName = thesis.getDegree().getNameFor(executionYear).getContent();
        String studentName = thesis.getStudent().getPerson().getName();
        String studentNumber = thesis.getStudent().getNumber().toString();
        String presidentName = name(thesis.getPresident());
        String presidentAffiliation = affiliation(thesis.getPresident());
        String vowel1Name = name(thesis.getVowels(), 0);
        String vowel1Affiliation = affiliation(thesis.getVowels(), 0);
        String vowel2Name = name(thesis.getVowels(), 1);
        String vowel2Affiliation = affiliation(thesis.getVowels(), 1);
        String vowel3Name = name(thesis.getVowels(), 2);
        String vowel3Affiliation = affiliation(thesis.getVowels(), 2);
        String vowel4Name = name(thesis.getVowels(), 3);
        String vowel4Affiliation = affiliation(thesis.getVowels(), 3);
        String orientationName =
                thesis.getOrientation().stream().map(p -> p.getName() + ", " + p.getAffiliation())
                        .collect(Collectors.joining("\n"));

        String currentPersonName = currentPerson.getNickname();

        String dateMessage;
        String discussedDate = "";

        if (thesis.getDiscussed() == null) {
            //No date was defined to the thesis
            dateMessage = getMessage(locale, NO_DATE_KEY);
        } else {
            dateMessage = getMessage(locale, WITH_DATE_KEY);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
            discussedDate = thesis.getDiscussed().toString(fmt);
        }

        String sender =
                thesis.isCoordinator() ? getMessage(locale, COORDINATOR_SENDER) : getMessage(locale, COUNCIL_MEMBER_SENDER);
        String role = thesis.isCoordinator() ? "" : getMessage(locale, COUNCIL_MEMBER_ROLE);

        Calendar today = Calendar.getInstance(locale);
        return getMessage(locale, BODY_KEY, year, degreeName, studentName, studentNumber, presidentName, presidentAffiliation,
                includeFlag(vowel1Name), vowel1Name, vowel1Affiliation, includeFlag(vowel2Name), vowel2Name, vowel2Affiliation,
                includeFlag(vowel3Name), vowel3Name, vowel3Affiliation, includeFlag(vowel4Name), vowel4Name, vowel4Affiliation,
                includeFlag(orientationName), orientationName, dateMessage, discussedDate, institutionName,
                "" + today.get(Calendar.DAY_OF_MONTH), today.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                "" + today.get(Calendar.YEAR), sender, currentPersonName, role);
    }

    private int includeFlag(String value) {
        return value == null ? FIELD_OFF : FIELD_ON;
    }

    private String name(ThesisEvaluationParticipant participant) {
        return participant == null ? null : participant.getName();
    }

    private String name(List<ThesisEvaluationParticipant> participants, int index) {
        if (participants.size() > index) {
            return name(participants.get(index));
        } else {
            return null;
        }
    }

    private String affiliation(ThesisEvaluationParticipant participant) {
        return participant == null ? null : participant.getAffiliation();
    }

    private String affiliation(List<ThesisEvaluationParticipant> participants, int index) {
        if (participants.size() > index) {
            return affiliation(participants.get(index));
        } else {
            return null;
        }
    }

    // Service Invokers migrated from Berserk

    private static final ApproveThesisProposal serviceInstance = new ApproveThesisProposal();

    @Atomic
    public static void runApproveThesisProposal(Thesis thesis) {
        serviceInstance.run(thesis);
    }

}
