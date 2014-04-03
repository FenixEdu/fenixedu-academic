package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.io.IOUtils;

import pt.ist.fenixframework.Atomic;

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
            return IOUtils.toByteArray(inputStream);
        } catch (final IOException e) {
            throw new Error(e);
        }
    }

    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
        Person student = thesis.getStudent().getPerson();
        Person president = getPerson(thesis.getPresident());

        Set<Person> persons = personSet(student, president);

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
        Person currentPerson = AccessControl.getPerson();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        String title = thesis.getTitle().getContent();
        String year = executionYear.getYear();
        String degreeName = thesis.getDegree().getNameFor(executionYear).getContent();
        String studentName = thesis.getStudent().getPerson().getName();
        String studentNumber = thesis.getStudent().getNumber().toString();

        String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
        String currentPersonName = currentPerson.getNickname();
        String institutionAcronym = Unit.getInstitutionAcronym();

        return getMessage(BODY_KEY, year, degreeName, studentName, studentNumber, date, currentPersonName, institutionAcronym);
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