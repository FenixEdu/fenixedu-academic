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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

public class ApproveThesisDiscussion extends ThesisServiceWithMailNotification {
    private static final String SUBJECT_KEY = "thesis.evaluation.approve.subject";
    private static final String BODY_KEY = "thesis.evaluation.approve.body";
    private static final String COUNCIL_MEMBER_ROLE = "thesis.proposal.jury.approve.body.role.council";

    @Override
    public void process(Thesis thesis) {
        thesis.approveEvaluation();
    }

    public static byte[] readStream(final InputStream inputStream) {
        try {
            return ByteStreams.toByteArray(inputStream);
        } catch (final IOException e) {
            throw new Error(e);
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

        Calendar today = Calendar.getInstance(locale);
        String currentPersonName = currentPerson.getNickname();
        String role = thesis.isCoordinator() ? "" : getMessage(locale, COUNCIL_MEMBER_ROLE);

        return getMessage(locale, BODY_KEY, year, degreeName, studentName, studentNumber, institutionName,
                "" + today.get(Calendar.DAY_OF_MONTH), today.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                "" + today.get(Calendar.YEAR), currentPersonName, role);
    }

    @Override
    protected String getSubject(Thesis thesis) {
        return getMessage(I18N.getLocale(), SUBJECT_KEY, thesis.getTitle().getContent());
    }

    // Service Invokers migrated from Berserk

    private static final ApproveThesisDiscussion serviceInstance = new ApproveThesisDiscussion();

    @Atomic
    public static void runApproveThesisDiscussion(Thesis thesis) throws NotAuthorizedException {
        ScientificCouncilAuthorizationFilter.instance.execute();
        serviceInstance.run(thesis);
    }

}
