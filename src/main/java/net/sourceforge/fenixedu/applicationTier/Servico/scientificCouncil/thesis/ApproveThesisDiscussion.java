package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.io.IOUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ApproveThesisDiscussion extends ThesisServiceWithMailNotification {

    private static class TransactionalThread extends Thread {

        private final String thesisOid;

        public TransactionalThread(final String thesisOid) {
            this.thesisOid = thesisOid;
        }

        @Override
        @Atomic(mode = TxMode.READ)
        public void run() {
            callService();
        }

        @Atomic
        private void callService() {
            for (final Thesis thesis : Bennu.getInstance().getThesesPendingPublicationSet()) {
                if (thesis.getExternalId().equals(thesisOid)) {
                    createResult(thesis);
                    thesis.setRootDomainObjectFromPendingPublication(null);
                    break;
                }
            }
        }

    }

    private static final String SUBJECT_KEY = "thesis.evaluation.approve.subject";
    private static final String BODY_KEY = "thesis.evaluation.approve.body";

    @Override
    public void process(Thesis thesis) {
        thesis.approveEvaluation();

        if (thesis.isFinalAndApprovedThesis()) {
            // Evaluated thesis have a public page in
            // ../dissertacoes/<id_internal>
            new ThesisSite(thesis);
            createResultEventually(thesis);
        }
    }

    private void createResultEventually(final Thesis thesis) {
        thesis.setRootDomainObjectFromPendingPublication(thesis.getRootDomainObject());
        final TransactionalThread thread = new TransactionalThread(thesis.getExternalId());
        thread.start();
    }

    public static byte[] readStream(final InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (final IOException e) {
            throw new Error(e);
        }
    }

    public static void createResult(final Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        Person author = thesis.getStudent().getPerson();

        final MultiLanguageString title = thesis.getFinalFullTitle();
        String titleForFile = title.getContent(thesis.getLanguage());
        if (titleForFile == null) {
            titleForFile = title.getContent();
        }
        net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication =
                new net.sourceforge.fenixedu.domain.research.result.publication.Thesis(author,
                        ThesisType.Graduation_Thesis,
                        titleForFile,
                        thesis.getKeywords(),
                        Bennu.getInstance().getInstitutionUnit().getName(),
                        thesis.getDiscussed().getYear(), // publication year
                        getAddress(Bennu.getInstance().getInstitutionUnit()), // address
                        thesis.getThesisAbstract(),
                        null, // number of pages
                        BundleUtil.getStringFromResourceBundle("resources.EnumerationResources",
                                thesis.getLanguage() == null ? Language.getDefaultLanguage().name() : thesis.getLanguage().name()), // language
                        getMonth(thesis), // publication month
                        null, // year begin
                        null, // month begin
                        null); // url

        FileResultPermittedGroupType groupType;
        switch (thesis.getVisibility()) {
        case PUBLIC:
            groupType = FileResultPermittedGroupType.PUBLIC;
            break;
        case INTRANET:
            groupType = FileResultPermittedGroupType.INSTITUTION;
            break;
        default:
            groupType = FileResultPermittedGroupType.INSTITUTION;
            break;
        }

        publication.addDocumentFile(dissertation.getContents(), dissertation.getFilename(), dissertation.getDisplayName(),
                groupType);

        publication.setThesis(thesis);
        author.addPersonRoleByRoleType(RoleType.RESEARCHER);
    }

    private static String getAddress(Unit institutionUnit) {
        List<PhysicalAddress> addresses = institutionUnit.getPhysicalAddresses();

        if (addresses == null || addresses.isEmpty()) {
            return null;
        }

        for (PhysicalAddress address : addresses) {
            String addr = address.getAddress();

            if (addr != null) {
                return addr;
            }
        }

        return null;
    }

    private static Month getMonth(Thesis thesis) {
        return Month.fromDateTime(thesis.getDiscussed());
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