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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

public class ApproveThesisDiscussion extends ThesisServiceWithMailNotification {
    private static final String SUBJECT_KEY = "thesis.evaluation.approve.subject";
    private static final String BODY_KEY = "thesis.evaluation.approve.body";

    @Override
    public void process(Thesis thesis) {
        thesis.approveEvaluation();

        if (thesis.isFinalAndApprovedThesis()) {
            // Evaluated thesis have a public page in
            // ../dissertacoes/<id_internal>
            new ThesisSite(thesis);
        }
    }

    public static byte[] readStream(final InputStream inputStream) {
        try {
            return ByteStreams.toByteArray(inputStream);
        } catch (final IOException e) {
            throw new Error(e);
        }
    }

    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
        Person student = thesis.getStudent().getPerson();
        Person orientator = thesis.getOrientator().getPerson();
        Person president = getPerson(thesis.getPresident());

        Set<Person> persons = personSet(student, president, orientator);

        Person coorientator = thesis.getCoorientator() != null ? thesis.getCoorientator().getPerson() : null;
        if (coorientator != null) {
            persons.add(coorientator);
        }

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

        return getMessage(BODY_KEY, year, degreeName, studentName, studentNumber, institutionName,
                "" + today.get(Calendar.DAY_OF_MONTH), today.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                "" + today.get(Calendar.YEAR), currentPersonName);
    }

    @Override
    protected String getSubject(Thesis thesis) {
        return getMessage(SUBJECT_KEY, thesis.getTitle().getContent());
    }

    // Service Invokers migrated from Berserk

    private static final ApproveThesisDiscussion serviceInstance = new ApproveThesisDiscussion();

    @Atomic
    public static void runApproveThesisDiscussion(Thesis thesis) throws NotAuthorizedException {
        ScientificCouncilAuthorizationFilter.instance.execute();
        serviceInstance.run(thesis);
    }

}