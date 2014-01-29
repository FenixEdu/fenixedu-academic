package pt.utl.ist.scripts.process.exportData.accounting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AcademicEvent;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.documents.AnnualIRSDeclarationDocument;
import net.sourceforge.fenixedu.presentationTier.docs.IRSCustomDeclaration;
import net.sourceforge.fenixedu.presentationTier.docs.IRSCustomDeclaration.IRSDeclarationDTO;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;

@Task(englishTitle = "ExportIRSDeclarations", readOnly = true)
public class ExportIRSDeclarations extends CronTask {

    final static int YEAR_TO_PROCESS = new LocalDate().getYear() - 1;
    int exportedDeclarationsCounter;
    StringBuilder output = new StringBuilder();
    static File academicSignature = null;

    @Override
    public void runTask() throws JRException, URISyntaxException, IOException {
        taskLog("Start ExportIRSDeclaration");

        academicSignature = getSignatureFile();
        exportedDeclarationsCounter = 0;

        final int PAGE_SIZE = 1000;

        Set<Event> paymentsForCivilYear = getEventIdsWithPaymentsForCivilYear();
        taskLog("Events to process : " + paymentsForCivilYear.size());

        final Map<Person, ExportIRSDeclaration> documents = getDocuments(paymentsForCivilYear);
        List<ExportIRSDeclaration> documentsToProcess = new ArrayList<ExportIRSDeclaration>(documents.values());
        int documentsSize = documents.size();
        final int numberOfPages = (int) Math.ceil(documentsSize / (double) 1000);

        taskLog("Number of documents to process: " + documentsSize);
        taskLog("Number of pages: " + numberOfPages);
        for (int i = 0; i < numberOfPages; i++) {
            final int fromIndex = i * PAGE_SIZE;
            final int toIndex = (fromIndex + PAGE_SIZE) > documentsSize ? documentsSize : (fromIndex + PAGE_SIZE);
            taskLog("From index: " + fromIndex);
            taskLog("To index: " + toIndex);

            writeDocuments(documentsToProcess.subList(fromIndex, toIndex));
            taskLog("Finished page : " + (i + 1));
        }
        taskLog("Exported document: " + exportedDeclarationsCounter);
        academicSignature.delete();
        output("pessoas_nao_processadas.tsv", output.toString().getBytes());
        taskLog("The end");
    }

    @Atomic(mode = TxMode.WRITE)
    private void writeDocuments(List<ExportIRSDeclaration> documents) throws JRException {
        for (ExportIRSDeclaration exportIRSDeclaration : documents) {
            if (exportIRSDeclaration.getPerson().hasAnnualIRSDocumentFor(YEAR_TO_PROCESS)) {
                exportIRSDeclaration.getPerson().getAnnualIRSDocumentFor(YEAR_TO_PROCESS).delete();
            }
            final IRSCustomDeclaration customDeclaration = new IRSCustomDeclaration(exportIRSDeclaration.getDeclarationDTO());
            addUnitCoordinatorSignature(customDeclaration);

            final byte[] result =
                    ReportsUtils.exportToPdfFileAsByteArray(customDeclaration.getReportTemplateKey(),
                            customDeclaration.getParameters(),
                            ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale()),
                            customDeclaration.getDataSource());

            new AnnualIRSDeclarationDocument(exportIRSDeclaration.getPerson(), null, customDeclaration.getReportFileName()
                    + ".pdf", result, YEAR_TO_PROCESS);
        }
    }

    private Map<Person, ExportIRSDeclaration> getDocuments(Set<Event> paymentsForCivilYear) throws JRException {
        final Map<Person, ExportIRSDeclaration> result = new HashMap<Person, ExportIRSDeclaration>();
        for (final Event event : paymentsForCivilYear) {
            if (isToProcess(event)) {
                if (event.getPerson().getStudent() == null) {
                    output.append("Person without student\t").append(event.getPerson().getName()).append("\t")
                            .append(event.getPerson().getIdDocumentType().name()).append("\t")
                            .append(event.getPerson().getDocumentIdNumber()).append("\n");
                }
                createDeclarationData(event, result);
            }
        }
        return result;
    }

    private boolean isToProcess(Event event) {
        if (event instanceof AcademicEvent) {
            final AcademicEvent academicEvent = (AcademicEvent) event;
            if (academicEvent.getAdministrativeOffice() != null
                    && academicEvent.getAdministrativeOffice().getAdministrativeOfficeType()
                            .equals(AdministrativeOfficeType.DEGREE)
                    && event.getMaxDeductableAmountForLegalTaxes(YEAR_TO_PROCESS).isPositive()) {
                return true;
            }
        }
        return false;
    }

    private Set<Event> getEventIdsWithPaymentsForCivilYear() {
        Set<Event> result = new HashSet<Event>();
        for (AccountingTransactionDetail atd : Bennu.getInstance().getAccountingTransactionDetailsSet()) {
            if (atd.getWhenRegistered().getYear() == YEAR_TO_PROCESS && atd.getTransaction() != null) {
                if (!atd.getEvent().isCancelled() && atd.getEvent() instanceof AcademicEvent) {
                    result.add(atd.getEvent());
                }
            }
        }
        return result;
    }

    private void createDeclarationData(Event event, Map<Person, ExportIRSDeclaration> result) {

        ExportIRSDeclaration exportIRSDeclaration = result.get(event.getPerson());
        if (exportIRSDeclaration != null) {
            exportIRSDeclaration.getDeclarationDTO().addAmount(event, YEAR_TO_PROCESS);
            return;
        }

        final IRSDeclarationDTO declarationDTO = new IRSDeclarationDTO(YEAR_TO_PROCESS, event.getPerson());
        if (event.getPerson().getStudent() != null) {
            declarationDTO.setStudentNumber(event.getPerson().getStudent().getNumber());
        }

        declarationDTO.addAmount(event, YEAR_TO_PROCESS);
        if (declarationDTO.getTotalAmount().isZero()) {
            output.append("Trying to send zero value declaration\t").append(event.getPerson().getUsername()).append("\n");
            return;
        }
        if (isToIgnore(declarationDTO)) {
            output.append(String.format("'%s'\tDoc.Id.Number\t'%s'\tignored due to invalid document id\n",
                    declarationDTO.getPersonName(), declarationDTO.getDocumentIdNumber()));
            return;
        }

        exportIRSDeclaration = new ExportIRSDeclaration(declarationDTO, event.getPerson());
        result.put(event.getPerson(), exportIRSDeclaration);
        exportedDeclarationsCounter++;
    }

    private boolean isToIgnore(final IRSDeclarationDTO declaration) {
        return Strings.isNullOrEmpty(declaration.getDocumentIdNumber().trim());
    }

    //Creates temp file due to Jasper Reports limitations 
    private File getSignatureFile() throws IOException {
        File createTempFile = File.createTempFile("tmp", null);
        InputStream resourceAsStream = getClass().getResourceAsStream("/signatures/academic_responsible.jpg");

        if (resourceAsStream == null) {
            throw new Error("Can't load signature file");
        }

        FileOutputStream to = new FileOutputStream(createTempFile);
        ByteStreams.copy(resourceAsStream, to);
        to.flush();
        to.close();

        taskLog("Created temp signature file %s\n", createTempFile.getAbsolutePath());
        return createTempFile;

    }

    private void addUnitCoordinatorSignature(final IRSCustomDeclaration customDeclaration) {
        customDeclaration.addParameter("signature", academicSignature);
    }

    private static class ExportIRSDeclaration implements Serializable {

        private static final long serialVersionUID = 1L;

        private IRSDeclarationDTO declarationDTO;
        private Person person;

        public ExportIRSDeclaration(IRSDeclarationDTO declarationDTO, Person person) {
            setDeclarationDTO(declarationDTO);
            setPerson(person);
        }

        public IRSDeclarationDTO getDeclarationDTO() {
            return declarationDTO;
        }

        public void setDeclarationDTO(IRSDeclarationDTO declarationDTO) {
            this.declarationDTO = declarationDTO;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }
    }
}
