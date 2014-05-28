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
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ApproveThesisProposal extends ThesisServiceWithMailNotification {

    private static final int FIELD_ON = 1;
    private static final int FIELD_OFF = 0;

    private static final String SUBJECT_KEY = "thesis.proposal.jury.approve.subject";
    private static final String BODY_KEY = "thesis.proposal.jury.approve.body";
    private static final String COORDINATOR_BODY_KEY = "thesis.proposal.jury.approve.body.coordinator";
    private static final String NO_DATE_KEY = "thesis.proposal.jury.approve.body.noDate";
    private static final String WITH_DATE_KEY = "thesis.proposal.jury.approve.body.withDate";

    private static final String DEGREE_ANNOUNCEMENTS_BOARD_NAME = "Anúncios";

    @Override
    void process(Thesis thesis) {
        if (thesis.getState() != ThesisState.APPROVED) {
            createAnnouncement(thesis);
            thesis.approveProposal();
        }
    }

    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
        Person student = thesis.getStudent().getPerson();
        Person orientator = thesis.getOrientator().getPerson();
        Person coorientator = getPerson(thesis.getCoorientator());
        Person president = getPerson(thesis.getPresident());

        Set<Person> persons = personSet(student, orientator, coorientator, president);
        for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
            persons.add(participant.getPerson());
        }

        // also send proposal approval to the contact team
        ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
        for (ScientificCommission member : thesis.getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson());
            }
        }

        return persons;
    }

    @Override
    protected String getSubject(Thesis thesis) {
        return getMessage(SUBJECT_KEY, thesis.getTitle().getContent());
    }

    @Override
    protected String getMessage(Thesis thesis) {
        Person currentPerson = AccessControl.getPerson();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        String title = thesis.getTitle().getContent();
        String year = executionYear.getYear();
        String degreeName = thesis.getDegree().getNameFor(executionYear).getContent();
        String studentName = thesis.getStudent().getPerson().getName();
        String studentNumber = thesis.getStudent().getNumber().toString();
        String presidentName = name(thesis.getPresident());
        String presidentAffiliation = affiliation(thesis.getPresident());
        String orientatorName = name(thesis.getOrientator());
        String orientatorAffiliation = affiliation(thesis.getOrientator());
        String coorientatorName = name(thesis.getCoorientator());
        String coorientatorAffiliation = affiliation(thesis.getCoorientator());
        String vowel1Name = name(thesis.getVowels(), 0);
        String vowel1Affiliation = affiliation(thesis.getVowels(), 0);
        String vowel2Name = name(thesis.getVowels(), 1);
        String vowel2Affiliation = affiliation(thesis.getVowels(), 1);
        String vowel3Name = name(thesis.getVowels(), 2);
        String vowel3Affiliation = affiliation(thesis.getVowels(), 2);

        String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
        String currentPersonName = currentPerson.getNickname();

        String dateMessage;
        String discussedDate = "";

        if (thesis.getDiscussed() == null) {
            //No date was defined to the thesis
            dateMessage = getMessage(NO_DATE_KEY);
        } else {
            dateMessage = getMessage(WITH_DATE_KEY);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
            discussedDate = thesis.getDiscussed().toString(fmt);
        }

        if (thesis.isCoordinator()) {
            return getMessage(COORDINATOR_BODY_KEY, year, degreeName, studentName, studentNumber, presidentName,
                    presidentAffiliation, orientatorName, orientatorAffiliation, includeFlag(coorientatorName), coorientatorName,
                    coorientatorAffiliation, includeFlag(vowel1Name), vowel1Name, vowel1Affiliation, includeFlag(vowel2Name),
                    vowel2Name, vowel2Affiliation, includeFlag(vowel3Name), vowel3Name, vowel3Affiliation, dateMessage,
                    discussedDate, date, currentPersonName);
        } else {
            return getMessage(BODY_KEY, year, degreeName, studentName, studentNumber, presidentName, presidentAffiliation,
                    orientatorName, orientatorAffiliation, includeFlag(coorientatorName), coorientatorName,
                    coorientatorAffiliation, includeFlag(vowel1Name), vowel1Name, vowel1Affiliation, includeFlag(vowel2Name),
                    vowel2Name, vowel2Affiliation, includeFlag(vowel3Name), vowel3Name, vowel3Affiliation, dateMessage,
                    discussedDate, date, currentPersonName);

        }
    }

    private int includeFlag(String value) {
        return value == null ? FIELD_OFF : FIELD_ON;
    }

    private String name(ThesisEvaluationParticipant participant) {
        return participant == null ? null : participant.getPersonName();
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

    private void createAnnouncement(Thesis thesis) {
        if (thesis.getProposedDiscussed() == null) {
            return;
        }

        AnnouncementBoard board = getBoard(DEGREE_ANNOUNCEMENTS_BOARD_NAME, thesis.getDegree().getUnit());

        if (board == null) {
            return;
        }

        DateTime now = new DateTime();

        Announcement announcement = new Announcement();
        announcement.setAnnouncementBoard(board);
        announcement.setCreator(AccessControl.getPerson());
        announcement.setCreationDate(now);
        announcement.setPlace(thesis.getProposedPlace());
        announcement.setVisible(true);

        announcement.setAuthor(getMessage("system.public.name"));
        announcement.setAuthorEmail(getMessage("system.public.email"));
        announcement.setPublicationBegin(now);
        announcement.setReferedSubjectBegin(thesis.getProposedDiscussed());

        MultiLanguageString subject =
                new MultiLanguageString().with(MultiLanguageString.pt,
                        getAnnouncementSubject(thesis, "thesis.announcement.subject", MultiLanguageString.pt)).with(
                        MultiLanguageString.en,
                        getAnnouncementSubject(thesis, "thesis.announcement.subject", MultiLanguageString.en));

        MultiLanguageString body =
                new MultiLanguageString().with(MultiLanguageString.pt,
                        getAnnouncementBody(thesis, "thesis.announcement.body", MultiLanguageString.pt)).with(
                        MultiLanguageString.en, getAnnouncementBody(thesis, "thesis.announcement.body", MultiLanguageString.en));

        announcement.setSubject(subject);
        announcement.setBody(body);
    }

    private String getAnnouncementSubject(Thesis thesis, String key, Locale language) {
        return getMessage(key, new Locale(language.toString()), thesis.getStudent().getPerson().getName());
    }

    private String getAnnouncementBody(Thesis thesis, String key, Locale language) {
        return getMessage(key, new Locale(language.toString()), thesis.getStudent().getPerson().getName(),
                getDate(thesis.getProposedDiscussed()), hasPlace(thesis), thesis.getProposedPlace(),
                hasTime(thesis.getProposedDiscussed()), getTime(thesis.getProposedDiscussed()),
                getString(thesis.getTitle(), language));
    }

    private Integer hasPlace(Thesis thesis) {
        String place = thesis.getProposedPlace();
        return place == null || place.trim().length() == 0 ? 0 : 1;
    }

    private String getTime(DateTime dateTime) {
        return String.format(new Locale("pt"), "%tR", dateTime.toDate());
    }

    private Integer hasTime(DateTime proposedDiscussed) {
        if (proposedDiscussed.getHourOfDay() == 0 && proposedDiscussed.getMinuteOfHour() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private String getDate(DateTime dateTime) {
        return String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", dateTime.toDate());
    }

    private String getString(MultiLanguageString mls, Locale language) {
        String value = mls.getContent(language);

        if (value == null) {
            value = mls.getContent();
        }

        return value;
    }

    private AnnouncementBoard getBoard(String name, Unit unit) {
        for (AnnouncementBoard board : unit.getBoards()) {
            if (board.getName().equalInAnyLanguage(name)) {
                return board;
            }
        }

        return null;
    }

    // Service Invokers migrated from Berserk

    private static final ApproveThesisProposal serviceInstance = new ApproveThesisProposal();

    @Atomic
    public static void runApproveThesisProposal(Thesis thesis) {
        serviceInstance.run(thesis);
    }

}