package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class EventReportQueueJob extends EventReportQueueJob_Base {

    private static final List<Class<? extends Event>> CANDIDACY_EVENT_TYPES = new ArrayList<Class<? extends Event>>();
    private static final List<Class<? extends AnnualEvent>> ADMIN_OFFICE_AND_INSURANCE_TYPES =
            new ArrayList<Class<? extends AnnualEvent>>();

    static {
        CANDIDACY_EVENT_TYPES.add(DFACandidacyEvent.class);
        CANDIDACY_EVENT_TYPES.add(PhdProgramCandidacyEvent.class);
        CANDIDACY_EVENT_TYPES.add(IndividualCandidacyEvent.class);

        ADMIN_OFFICE_AND_INSURANCE_TYPES.add(AdministrativeOfficeFeeAndInsuranceEvent.class);
        ADMIN_OFFICE_AND_INSURANCE_TYPES.add(InsuranceEvent.class);
    }

    private EventReportQueueJob() {
        super();
    }

    private EventReportQueueJob(final EventReportQueueJobBean bean) {
        this();
        checkPermissions(bean);

        setExportGratuityEvents(bean.getExportGratuityEvents());
        setExportAcademicServiceRequestEvents(bean.getExportAcademicServiceRequestEvents());
        setExportIndividualCandidacyEvents(bean.getExportIndividualCandidacyEvents());
        setExportPhdEvents(bean.getExportPhdEvents());
        setExportResidenceEvents(bean.getExportResidenceEvents());
        setExportOthers(bean.getExportOthers());
        setBeginDate(bean.getBeginDate());
        setEndDate(bean.getEndDate());

        setForExecutionYear(bean.getExecutionYear());
        setForAdministrativeOffice(bean.getAdministrativeOffice());
    }

    private void checkPermissions(EventReportQueueJobBean bean) {
        Person loggedPerson = AccessControl.getPerson();

        if (loggedPerson == null) {
            throw new DomainException("error.EventReportQueueJob.permission.denied");
        }

        if (loggedPerson.hasRole(RoleType.MANAGER)) {
            return;
        }

        // Only the manager can create Reports with no AdminOffice
        if (bean.getAdministrativeOffice() == null) {
            throw new DomainException("error.EventReportQueueJob.permission.denied");
        }

        final Set<AdministrativeOffice> offices =
                AcademicAuthorizationGroup.getOfficesForOperation(loggedPerson, AcademicOperationType.MANAGE_EVENT_REPORTS);

        if (!offices.contains(bean.getAdministrativeOffice())) {
            throw new DomainException("error.EventReportQueueJob.permission.denied");
        }

    }

    @Override
    public String getFilename() {
        return "dividas" + getRequestDate().toString("ddMMyyyyhms") + ".tsv";
    }

    @Override
    public QueueJobResult execute() throws Exception {
        ByteArrayOutputStream byteArrayOSForDebts = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOSForExemptions = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOSForTransactions = new ByteArrayOutputStream();

        SheetData[] reports = buildReport();

        SpreadsheetBuilder spreadsheetBuilderForDebts = new SpreadsheetBuilder();
        SpreadsheetBuilder spreadsheetBuilderForExemptions = new SpreadsheetBuilder();
        SpreadsheetBuilder spreadsheetBuilderForTransactions = new SpreadsheetBuilder();

        spreadsheetBuilderForDebts.addSheet("dividas", reports[0]);
        spreadsheetBuilderForExemptions.addSheet("isencoes", reports[1]);
        spreadsheetBuilderForTransactions.addSheet("transaccoes", reports[2]);

        spreadsheetBuilderForDebts.build(WorkbookExportFormat.TSV, byteArrayOSForDebts);
        spreadsheetBuilderForExemptions.build(WorkbookExportFormat.TSV, byteArrayOSForExemptions);
        spreadsheetBuilderForTransactions.build(WorkbookExportFormat.TSV, byteArrayOSForTransactions);

        byteArrayOSForDebts.close();
        byteArrayOSForExemptions.close();
        byteArrayOSForTransactions.close();

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("text/tsv");
        queueJobResult.setContent(byteArrayOSForDebts.toByteArray());

        EventReportQueueJobFile fileForDebts = new EventReportQueueJobFile(byteArrayOSForDebts.toByteArray(), "dividas.tsv");
        EventReportQueueJobFile fileForExemptions =
                new EventReportQueueJobFile(byteArrayOSForExemptions.toByteArray(), "isencoes.tsv");
        EventReportQueueJobFile fileForTransactions =
                new EventReportQueueJobFile(byteArrayOSForTransactions.toByteArray(), "transaccoes.tsv");

        this.setDebts(fileForDebts);
        this.setExemptions(fileForExemptions);
        this.setTransactions(fileForTransactions);

        System.out.println("Job " + getFilename() + " completed");

        return queueJobResult;
    }

    private SheetData[] buildReport() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        PrintStream stringStream = null;
        PrintStream defaultErrorStream = System.err;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            stringStream = new PrintStream(byteArrayOutputStream, true);

            System.setErr(stringStream);

            final SheetData<EventBean> eventsSheet = allEvents();
            final SheetData<ExemptionBean> exemptionsSheet = allExemptions();
            final SheetData<AccountingTransactionBean> transactionsSheet = allTransactions();

            return new SheetData[] { eventsSheet, exemptionsSheet, transactionsSheet };
        } finally {
            stringStream.close();
            this.setErrors(new String(byteArrayOutputStream.toByteArray()));
            System.setErr(defaultErrorStream);
        }
    }

    private List<String> getAllEventsExternalIds() {
        try {
            Connection connection =
                    Transaction.currentFenixTransaction().getOJBBroker().serviceConnectionManager().getConnection();

            PreparedStatement prepareStatement = connection.prepareStatement("SELECT OID FROM EVENT");
            ResultSet executeQuery = prepareStatement.executeQuery();

            List<String> result = new ArrayList<String>();
            while (executeQuery.next()) {
                result.add(executeQuery.getString(1));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Integer BLOCK = 5000;

    /* ALL EVENTS */
    private SheetData<EventBean> allEvents() {

        List<String> allEventsExternalIds = getAllEventsExternalIds();
        System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

        Integer blockRead = 0;

        final List<EventBean> result = Collections.synchronizedList(new ArrayList<EventBean>());

        while (blockRead < allEventsExternalIds.size()) {
            Integer inc = BLOCK;

            if (blockRead + inc >= allEventsExternalIds.size()) {
                inc = allEventsExternalIds.size() - blockRead;
            }

            final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
            blockRead += inc;

            Thread thread = new Thread() {

                @Override
                public void run() {
                    Transaction.withTransaction(true, new TransactionalCommand() {

                        @Override
                        public void doIt() {

                            for (String oid : block) {
                                Event event = null;
                                try {
                                    event = FenixFramework.getDomainObject(oid);

                                    if (!isAccountingEventForReport(event)) {
                                        continue;
                                    }

                                    result.add(writeEvent(event));
                                } catch (Throwable e) {
                                    e.printStackTrace(System.err);
                                    if (event != null) {
                                        System.err.println("Error on event -> " + event.getExternalId());
                                    }
                                }

                            }
                        }
                    });
                }
            };

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
            }

            System.out.println(String.format("Read %s events", blockRead));
        }

        System.out.println(String.format("Catch %s events ", result.size()));

        return new SheetData<EventBean>(result) {

            @Override
            protected void makeLine(EventBean bean) {
                addCell("Identificador", bean.externalId);
                addCell("Aluno", bean.studentNumber);
                addCell("Nome", bean.studentName);
                addCell("Email", bean.email);
                addCell("Data inscrição", bean.registrationStartDate);
                addCell("Ano lectivo", bean.executionYear);
                addCell("Tipo de matricula", bean.studiesType);
                addCell("Nome do Curso", bean.degreeName);
                addCell("Tipo de curso", bean.degreeType);
                addCell("Programa doutoral", bean.phdProgramName);
                addCell("ECTS inscritos", bean.enrolledECTS);
                addCell("Regime", bean.regime);
                addCell("Modelo de inscrição", bean.enrolmentModel);
                addCell("Residência - Ano", bean.residenceYear);
                addCell("Residência - Mês", bean.residenceMonth);
                addCell("Tipo de divida", bean.description);
                addCell("Data de criação", bean.whenOccured);
                addCell("Valor Total", bean.totalAmount);
                addCell("Valor Pago", bean.payedAmount);
                addCell("Valor em divida", bean.amountToPay);
                addCell("Valor Reembolsável", bean.reimbursableAmount);
                addCell("Desconto", bean.totalDiscount);
                addCell("Dívida Associada", bean.relatedEvent);
                addCell("Id. Fiscal Entidade", bean.debtorFiscalId);
                addCell("Nome Entidade", bean.debtorName);

                List<InstallmentWrapper> list = bean.installments;

                if (list != null) {
                    for (InstallmentWrapper installment : list) {
                        addCell(installment.getExpirationDateLabel(), installment.getExpirationDate());
                        addCell(installment.getAmountToPayLabel(), installment.getAmountToPay());
                        addCell(installment.getRemainingAmountLabel(), installment.getRemainingAmount());
                    }
                }
            }

        };
    }

    private boolean isAccountingEventForReport(final Event event) {

        if (event.isCancelled()) {
            return false;
        }

        Wrapper wrapper = buildWrapper(event);

        if (wrapper == null) {
            return false;
        }

        if (hasForExecutionYear() && getForExecutionYear() != wrapper.getForExecutionYear()) {
            return false;
        }

        if (!officesMatch(wrapper)) {
            return false;
        }

        if (event.getWhenOccured().isAfter(getBeginDate().toDateTimeAtStartOfDay())
                && event.getWhenOccured().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))) {
            return true;
        }

        for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
            if (transaction.getWhenRegistered() != null) {
                if (transaction.getWhenRegistered().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
                        && transaction.getWhenRegistered().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
                    return true;
                }
            }

            if (transaction.getWhenProcessed() != null) {
                if (transaction.getWhenProcessed().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
                        && transaction.getWhenProcessed().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
                    return true;
                }
            }

            for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
                if (adjustment.getWhenRegistered() != null) {
                    if (adjustment.getWhenRegistered()
                            .isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
                            && adjustment.getWhenRegistered().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
                        return true;
                    }
                }

                if (adjustment.getWhenProcessed() != null) {
                    if (adjustment.getWhenProcessed().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
                            && adjustment.getWhenProcessed().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean officesMatch(Wrapper wrapper) {
        if (wrapper.getRelatedAcademicOffice() == null) {
            return true;
        }

        return wrapper.getRelatedAcademicOffice().equals(getForAdministrativeOffice());
    }

    private EventBean writeEvent(final Event event) {
        Properties formatterProperties = new Properties();

        formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, "resources.EnumerationResources");
        formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");

        Wrapper wrapper = buildWrapper(event);

        EventBean bean = new EventBean();

        bean.externalId = event.getExternalId();
        bean.studentNumber = wrapper.getStudentNumber();
        bean.studentName = wrapper.getStudentName();
        bean.email = wrapper.getStudentEmail();
        bean.registrationStartDate = wrapper.getRegistrationStartDate();
        bean.executionYear = wrapper.getExecutionYear();
        bean.studiesType = wrapper.getStudiesType();
        bean.degreeName = wrapper.getDegreeName();
        bean.degreeType = wrapper.getDegreeType();
        bean.phdProgramName = wrapper.getPhdProgramName();
        bean.enrolledECTS = wrapper.getEnrolledECTS();
        bean.regime = wrapper.getRegime();
        bean.enrolmentModel = wrapper.getEnrolmentModel();
        bean.residenceYear = wrapper.getResidenceYear();
        bean.residenceMonth = wrapper.getResidenceMonth();

        bean.description = event.getDescription().toString(new DefaultResourceBundleProvider(formatterProperties));
        bean.whenOccured = event.getWhenOccured().toString("dd/MM/yyyy");
        bean.totalAmount = event.getTotalAmountToPay().toPlainString();
        bean.payedAmount = event.getPayedAmount().toPlainString();
        bean.amountToPay = event.getAmountToPay().toPlainString();
        bean.reimbursableAmount = event.getReimbursableAmount().toPlainString();
        bean.totalDiscount = event.getTotalDiscount().toPlainString();
        bean.relatedEvent = wrapper.getRelatedEventExternalId();
        bean.debtorFiscalId = wrapper.getDebtorFiscalId();
        bean.debtorName = wrapper.getDebtorName();

        if (wrapper instanceof GratuityEventWrapper) {
            bean.installments = ((GratuityEventWrapper) wrapper).getInstallments();
        }

        return bean;
    }

    private static class EventBean {
        public String externalId;
        public String studentNumber;
        public String studentName;
        public String email;
        public String registrationStartDate;
        public String executionYear;
        public String studiesType;
        public String degreeName;
        public String degreeType;
        public String phdProgramName;
        public String enrolledECTS;
        public String regime;
        public String enrolmentModel;
        public String residenceYear;
        public String residenceMonth;
        public String description;
        public String whenOccured;
        public String totalAmount;
        public String payedAmount;
        public String amountToPay;
        public String reimbursableAmount;
        public String totalDiscount;
        public String relatedEvent;
        public String debtorFiscalId;
        public String debtorName;

        public List<InstallmentWrapper> installments;
    }

    /* ALL EXEMPTIONS */
    private SheetData<ExemptionBean> allExemptions() {
        List<String> allEventsExternalIds = getAllEventsExternalIds();
        System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

        Integer blockRead = 0;

        final List<ExemptionBean> result = Collections.synchronizedList(new ArrayList<ExemptionBean>());

        while (blockRead < allEventsExternalIds.size()) {
            Integer inc = BLOCK;

            if (blockRead + inc >= allEventsExternalIds.size()) {
                inc = allEventsExternalIds.size() - blockRead;
            }

            final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
            blockRead += inc;

            Thread thread = new Thread() {

                @Override
                public void run() {
                    Transaction.withTransaction(true, new TransactionalCommand() {

                        @Override
                        public void doIt() {
                            for (String oid : block) {
                                Event event = FenixFramework.getDomainObject(oid);

                                try {
                                    if (!isAccountingEventForReport(event)) {
                                        continue;
                                    }

                                    result.addAll(writeExemptionInformation(event));
                                } catch (Throwable e) {
                                    e.printStackTrace(System.err);
                                    if (event != null) {
                                        System.err.println("Error on event -> " + event.getExternalId());
                                    }
                                }

                            }
                        }
                    });
                }
            };

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
            }

            System.out.println(String.format("Read %s events", blockRead));
        }

        return new SheetData<ExemptionBean>(result) {

            @Override
            protected void makeLine(ExemptionBean bean) {

                addCell("Identificador", bean.eventExternalId);
                addCell("Tipo da Isenção", bean.exemptionTypeDescription);
                addCell("Valor da Isenção", bean.exemptionValue);
                addCell("Percentagem da Isenção", bean.percentage);
                addCell("Motivo da Isenção", bean.justification);

            }

        };
    }

    // write Exemption Information
    private List<ExemptionBean> writeExemptionInformation(Event event) {
        Set<Exemption> exemptionsSet = event.getExemptionsSet();

        List<ExemptionBean> result = new ArrayList<ExemptionBean>();

        for (Exemption exemption : exemptionsSet) {

            ExemptionWrapper wrapper = new ExemptionWrapper(exemption);

            Properties formatterProperties = new Properties();

            ExemptionBean bean = new ExemptionBean();

            formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, "resources.EnumerationResources");
            formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");

            bean.eventExternalId = event.getExternalId();
            bean.exemptionTypeDescription = wrapper.getExemptionTypeDescription();
            bean.exemptionValue = wrapper.getExemptionValue();
            bean.percentage = wrapper.getPercentage();
            bean.justification = wrapper.getJustification();

            result.add(bean);
        }

        return result;
    }

    private class ExemptionBean {
        public String eventExternalId;
        public String exemptionTypeDescription;
        public String exemptionValue;
        public String percentage;
        public String justification;
    }

    /* ALL TRANSACTIONS */

    private SheetData<AccountingTransactionBean> allTransactions() {

        List<String> allEventsExternalIds = getAllEventsExternalIds();
        System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

        Integer blockRead = 0;

        final List<AccountingTransactionBean> result = Collections.synchronizedList(new ArrayList<AccountingTransactionBean>());

        while (blockRead < allEventsExternalIds.size()) {
            Integer inc = BLOCK;

            if (blockRead + inc >= allEventsExternalIds.size()) {
                inc = allEventsExternalIds.size() - blockRead;
            }

            final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
            blockRead += inc;

            Thread thread = new Thread() {

                @Override
                public void run() {
                    Transaction.withTransaction(true, new TransactionalCommand() {

                        @Override
                        public void doIt() {
                            for (String oid : block) {
                                Event event = FenixFramework.getDomainObject(oid);

                                try {
                                    if (!isAccountingEventForReport(event)) {
                                        continue;
                                    }

                                    result.addAll(writeTransactionInformation(event));
                                } catch (Throwable e) {
                                    e.printStackTrace(System.err);
                                    if (event != null) {
                                        System.err.println("Error on event -> " + event.getExternalId());
                                    }
                                }

                            }
                        }
                    });
                }
            };

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
            }

            System.out.println(String.format("Read %s events", blockRead));
        }

        return new SheetData<AccountingTransactionBean>(result) {

            @Override
            protected void makeLine(AccountingTransactionBean bean) {

                addCell("Identificador", bean.eventExternalId);
                addCell("Data do pagamento", bean.whenRegistered);
                addCell("Data de entrada do pagamento", bean.whenProcessed);
                addCell("Nome da entidade devedora", bean.debtPartyName);
                addCell("Contribuinte da entidade devedora", bean.debtSocialSecurityNumber);
                addCell("Nome da entidade credora", bean.credPartyName);
                addCell("Contribuinte da entidade credora", bean.credSocialSecurityNumber);
                addCell("Montante inicial", bean.originalAmount);
                addCell("Montante ajustado", bean.amountWithAdjustment);
                addCell("Modo de pagamento", bean.paymentMode);
                addCell("Data do ajuste", bean.whenAdjustmentRegistered);
                addCell("Data de entrada do ajuste", bean.whenAdjustmentProcessed);
                addCell("Montante do ajuste", bean.adjustmentAmount);
                addCell("Justificação", bean.comments);

            }

        };
    }

    private List<AccountingTransactionBean> writeTransactionInformation(Event event) {
        List<AccountingTransactionBean> result = new ArrayList<AccountingTransactionBean>();

        for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {

            for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
                Entry internalEntry = obtainInternalAccountEntry(adjustment);
                Entry externalEntry = obtainExternalAccountEntry(adjustment);

                AccountingTransactionBean bean = new AccountingTransactionBean();

                bean.eventExternalId = valueOrNull(event.getExternalId());
                bean.whenRegistered = valueOrNull(transaction.getWhenRegistered());
                bean.whenProcessed = valueOrNull(transaction.getWhenProcessed());
                bean.debtPartyName =
                        internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getPartyName().getContent()) : "-";
                bean.debtSocialSecurityNumber =
                        internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getSocialSecurityNumber()) : "-";
                bean.credPartyName =
                        externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getPartyName().getContent()) : "-";
                bean.credSocialSecurityNumber =
                        externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getSocialSecurityNumber()) : "-";
                bean.originalAmount = valueOrNull(transaction.getOriginalAmount().toPlainString());
                bean.amountWithAdjustment = valueOrNull(transaction.getAmountWithAdjustment().toPlainString());
                bean.paymentMode = valueOrNull(transaction.getPaymentMode().getLocalizedName());
                bean.whenAdjustmentRegistered = valueOrNull(adjustment.getWhenRegistered());
                bean.whenAdjustmentProcessed = valueOrNull(adjustment.getWhenProcessed());
                bean.amountWithAdjustment = valueOrNull(adjustment.getAmountWithAdjustment().toPlainString());
                bean.comments = valueOrNull(adjustment.getComments());

                result.add(bean);
            }

            if (transaction.getAdjustmentTransactions().isEmpty()) {
                Entry internalEntry = obtainInternalAccountEntry(transaction);
                Entry externalEntry = obtainExternalAccountEntry(transaction);

                AccountingTransactionBean bean = new AccountingTransactionBean();

                bean.eventExternalId = event.getExternalId();
                bean.whenRegistered = valueOrNull(transaction.getWhenRegistered());
                bean.whenProcessed = valueOrNull(transaction.getWhenProcessed());
                bean.debtPartyName =
                        internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getPartyName().getContent()) : "-";
                bean.debtSocialSecurityNumber =
                        internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getSocialSecurityNumber()) : "-";
                bean.credPartyName =
                        externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getPartyName().getContent()) : "-";
                bean.credSocialSecurityNumber =
                        externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getSocialSecurityNumber()) : "-";
                bean.originalAmount = valueOrNull(transaction.getOriginalAmount().toPlainString());
                bean.amountWithAdjustment = valueOrNull(transaction.getAmountWithAdjustment().toPlainString());
                bean.paymentMode = valueOrNull(transaction.getPaymentMode().getLocalizedName());
                bean.whenAdjustmentRegistered = "-";
                bean.amountWithAdjustment = "-";
                bean.whenAdjustmentProcessed = "-";
                bean.comments = "-";

                result.add(bean);
            }
        }

        return result;
    }

    private class AccountingTransactionBean {

        public String eventExternalId;
        public String whenRegistered;
        public String whenProcessed;
        public String debtPartyName;
        public String debtSocialSecurityNumber;
        public String credPartyName;
        public String credSocialSecurityNumber;
        public String originalAmount;
        public String amountWithAdjustment;
        public String paymentMode;
        public String whenAdjustmentRegistered;
        public String whenAdjustmentProcessed;
        public String adjustmentAmount;
        public String comments;
    }

    private Entry obtainInternalAccountEntry(final AccountingTransaction transaction) {
        return transaction.getFromAccountEntry();
    }

    private Entry obtainExternalAccountEntry(final AccountingTransaction transaction) {
        return transaction.getToAccountEntry();
    }

    // Residence

    private Wrapper buildWrapper(Event event) {

        if (event.isGratuity()) {
            return getExportGratuityEvents() ? new GratuityEventWrapper((GratuityEvent) event) : null;
        } else if (event.isAcademicServiceRequestEvent()) {
            return getExportAcademicServiceRequestEvents() ? new AcademicServiceRequestEventWrapper(
                    (AcademicServiceRequestEvent) event) : null;
        } else if (event.isIndividualCandidacyEvent()) {
            return getExportIndividualCandidacyEvents() ? new IndividualCandidacyEventWrapper((IndividualCandidacyEvent) event) : null;
        } else if (event.isPhdEvent()) {
            return getExportPhdEvents() ? new PhdEventWrapper((PhdEvent) event) : null;
        } else if (event.isResidenceEvent()) {
            return getExportResidenceEvents() ? new ResidenceEventWrapper((ResidenceEvent) event) : null;
        } else if (event.isFctScholarshipPhdGratuityContribuitionEvent()) {
            return getExportPhdEvents() ? new ExternalScholarshipPhdGratuityContribuitionEventWrapper(event) : null;
        } else if (getExportOthers()) {
            return new EventWrapper(event);
        }

        return null;
    }

    public static List<EventReportQueueJob> retrieveAllGeneratedReports() {
        List<EventReportQueueJob> reports = new ArrayList<EventReportQueueJob>();

        CollectionUtils.select(RootDomainObject.getInstance().getQueueJobSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof EventReportQueueJob;
            }

        }, reports);

        return reports;
    }

    public static List<EventReportQueueJob> retrieveDoneGeneratedReports() {
        List<EventReportQueueJob> reports = new ArrayList<EventReportQueueJob>();

        CollectionUtils.select(RootDomainObject.getInstance().getQueueJobSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                if (!(arg0 instanceof EventReportQueueJob)) {
                    return false;
                }

                EventReportQueueJob eventReportQueueJob = (EventReportQueueJob) arg0;

                return eventReportQueueJob.getDone();
            }

        }, reports);

        return reports;
    }

    public static List<EventReportQueueJob> retrievePendingOrCancelledGeneratedReports() {
        List<EventReportQueueJob> all = retrieveAllGeneratedReports();
        List<EventReportQueueJob> done = retrieveDoneGeneratedReports();

        all.removeAll(done);

        return all;
    }

    @Service
    public static EventReportQueueJob createRequest(EventReportQueueJobBean bean) {
        return new EventReportQueueJob(bean);
    }

    public static List<EventReportQueueJob> readPendingOrCancelledJobs(final Set<AdministrativeOffice> offices) {
        List<EventReportQueueJob> list = retrievePendingOrCancelledGeneratedReports();

        return filterByAdministrativeOfficeType(list, offices);
    }

    public static List<EventReportQueueJob> readDoneReports(final Set<AdministrativeOffice> offices) {
        List<EventReportQueueJob> list = retrieveDoneGeneratedReports();

        return filterByAdministrativeOfficeType(list, offices);
    }

    private static List<EventReportQueueJob> filterByAdministrativeOfficeType(final List<EventReportQueueJob> list,
            final Set<AdministrativeOffice> offices) {

        if (offices == null) {
            return list;
        }

        List<EventReportQueueJob> result = new ArrayList<EventReportQueueJob>();

        for (EventReportQueueJob eventReportQueueJob : list) {

            if (offices.contains(eventReportQueueJob.getForAdministrativeOffice())) {
                result.add(eventReportQueueJob);
            }
        }

        return result;
    }

    private static String valueOrNull(Object obj) {
        if (obj == null) {
            return "-";
        }

        if (obj instanceof DateTime) {
            return ((DateTime) obj).toString("dd-MM-yyyy HH:MM");
        }

        if (obj instanceof LocalDate) {
            return ((LocalDate) obj).toString("dd-MM-yyyy");
        }

        return obj.toString();
    }

    public boolean isBrokenInThree() {
        return hasDebts();
    }
}
