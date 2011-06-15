package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EventReportQueueJob extends EventReportQueueJob_Base {

    private static final List<String> EVENTS = Arrays.asList(new String[] { "4213363900248", "4213363900246", "4213363900241",
	    "4213363900239", "4213363900238", "4213363900231", "25770770426", "25770767851", "25770751422", "4213363863828",
	    "25770743421", "25770742428", "25770742421", "25770731621", "4213363828821", "25770713810", "25770712807",
	    "25770707008", "25770703420", "25770703098", "25770703090", "25770702416", "4213363814206", "25770699213",
	    "25770694622", "25770694618", "25770692821", "25770692622", "25770691616", "25770689477", "25770688622",
	    "25770687435", "25770687430", "25770687424", "25770687418", "25770686963", "25770686961", "25770686861",
	    "25770686821", "25770686810", "25770685539", "25770685502", "25770685460", "25770685426", "25770685408",
	    "25770684807", "25770684751", "4213363797681", "4213363797677", "4213363797675", "4213363797671", "4213363797667",
	    "4213363797659", "4213363797652", "4213363797650", "4213363797645", "4213363797636", "4213363797630",
	    "4213363797625", "4213363797624", "4213363797610", "25770682393", "25770682295", "25770682288", "25770682244",
	    "25770682233", "25770681973", "25770680431", "25770680045", "25770679045", "25770679043", "25770679039",
	    "25770679017", "25770678231", "4213363791069", "4213363791068", "4213363791067", "4213363791066", "4213363791065",
	    "4213363791064", "4213363791063", "4213363791062", "25770677207", "25770676218", "25770674279", "25770674275",
	    "25770674274", "25770674254", "25770674226", "25770674086", "25770673149", "25770673105", "25770673102",
	    "25770673010", "25770672132", "25770672123", "25770672072", "25770671890", "25770671864", "25770671836",
	    "25770671828", "25770670824", "25770670823", "25770670810", "25770670730", "25770669680", "25770669626",
	    "25770669574", "25770669573", "25770669533", "25770669531", "25770669472", "25770669453", "25770669451",
	    "25770669443", "25770669394", "25770669377", "25770669347", "25770669329", "25770669284", "25770669261",
	    "25770669228", "25770669217", "25770668243", "25770668239", "25770668221", "25770668215", "25770666923",
	    "25770666890", "25770666886", "25770666883", "25770666870", "25770666861", "25770666852", "25770666839",
	    "25770665795", "25770665794", "25770665781", "25770665777", "25770665757", "25770665722", "25770665670",
	    "25770665661", "25770665650", "25770665621", "25770659570", "25770659537", "25770659530", "25770659522",
	    "25770659514", "25770659383", "25770659363", "25770659175", "25770659074", "25770659050", "25770659049",
	    "25770659016", "25770658936", "25770658850", "25770658843", "25770658784", "25770658781", "25770658775",
	    "25770658766", "25770658588", "25770658517", "25770658482", "25770658421", "25770658414", "25770658411",
	    "25770658405", "25770658350", "25770658187", "25770658044", "25770658020", "25770658018", "25770657971",
	    "25770657936", "25770657794", "25770657690", "25770657306", "25770657163", "25770657090", "25770657047",
	    "25770656962", "25770656911", "25770656906", "25770656742", "25770656734", "25770656702", "25770656668",
	    "25770656562", "25770656370", "25770656367", "25770656358", "25770656313", "25770656152", "25770656106",
	    "25770655859", "25770655846", "25770655805", "25770655727", "25770655719", "25770655710", "25770655524",
	    "25770655445", "25770655026", "25770654852", "25770654789", "25770654784", "25770654780", "25770654768",
	    "25770654749", "25770654596", "25770654550", "25770654547", "25770654529", "25770654449", "25770639612",
	    "4213363740824", "25770626011", "25770611809", "4213363723027", "25770607606", "25770595609", "25770592908",
	    "25770592863", "25770592861", "25770592842", "25770578632", "25770578631", "25770578612", "25770578606",
	    "25770577609", "25770574209", "25770573210", "25770573041", "25770572228", "25770572222", "25770572219",
	    "25770572206", "25770565763", "25770565762", "25770565738", "25770565706", "25770565617", "25770565611",
	    "25770560622", "2310693161883", "25770559813", "25770557003", "25770556977", "25770556950", "25770556882",
	    "25770556880", "25770556704", "25770556696", "25770556686", "25770556658", "25770556652", "25770556635",
	    "25770556618", "25770556606", "25770556600", "25770556569", "25770556512", "25770556389", "25770556379",
	    "25770556377", "25770556310", "25770556261", "25770556183", "25770556139", "25770556067", "25770556051",
	    "25770555853", "25770555852", "25770555797", "2310693146114", "2310693146111", "25770536364", "25770536035",
	    "25770535265", "25770535260", "25770535230", "25770535141", "25770535134", "25770535127", "25770535109",
	    "25770535108", "25770535048", "25770534633", "25770534570", "25770534514", "25770534505", "25770534504",
	    "25770534503", "25770534491", "25770534488", "25770534482", "25770534455", "25770534438", "25770534413",
	    "25770534408", "25770534403", "25770534347", "25770534346", "25770534339", "25770534088", "25770534066",
	    "25770534063", "25770534062", "25770534036", "25770533971", "25770533969", "25770533966", "25770533965",
	    "25770533883", "25770533729", "25770533728", "25770533699", "25770533695", "25770533694", "25770533599",
	    "25770533594", "25770533524", "25770533400", "25770533379", "25770533233", "25770533199", "25770533182",
	    "25770533157", "25770533101", "25770533094", "25770533068", "25770533062", "25770533012", "25770533007",
	    "25770532995", "25770532899", "25770532806", "25770532780", "25770532768", "25770532672", "25770532651",
	    "25770532278", "25770532270", "25770531826", "25770531421", "25770531416", "25770531392", "25770531368",
	    "25770531360", "25770531358", "25770531341", "25770531318", "25770531314", "25770531307", "25770531298",
	    "25770531291", "25770531239", "25770531227", "25770531218", "25770531213", "25770531205", "25770531202",
	    "25770531002", "25770531001", "25770530973", "25770530810", "25770530790", "25770530768", "25770530754",
	    "25770530728", "25770530697", "25770530641", "25770530574", "25770530572", "25770530557", "25770530547",
	    "25770530455", "25770530314", "25770530307", "25770530296", "25770530273", "25770530059", "25770529998",
	    "25770529990", "25770529984", "25770529958", "25770529957", "25770529870", "25770529861", "25770529853",
	    "25770529850", "25770529847", "25770529698", "25770529662", "25770529636", "4213363639813", "25770472608",
	    "4213363578809", "25770453660", "25770453074", "25770453060", "4213363564214", "4213363563210", "4213363561813",
	    "25770443622", "25770439836", "25770439835", "25770439834", "25770439833", "25770439815", "25770439804",
	    "25770439042", "25770439038", "25770439032", "25770439028", "25770439026", "25770439024", "25770439021",
	    "25770439019", "25770439018", "25770439009", "25770439006", "25770432210", "25770432180", "25770432118",
	    "25770424205", "25770418235", "25770410620", "25770410614", "25770410613", "25770408854", "25770408850",
	    "25770408820", "25770407645", "25770405694", "25770405683", "25770405642", "25770405621", "25770405612",
	    "25770389429", "25770389305", "25770389268", "25770389267", "25770389233", "25770389211", "25770389101",
	    "25770388557", "25770388437", "25770388230", "25770388195", "25770388124", "25770388121", "25770387968",
	    "25770387917", "25770387866", "25770387819", "25770387733", "25770387714", "25770387432", "25770386769",
	    "25770386452", "25770386145", "25770386097", "25770386083", "25770385971", "25770385790", "25770385705",
	    "25770385697", "25770385589", "25770385554", "25770385543", "25770385537", "25770385534", "25770385518",
	    "25770385502", "25770385389", "25770385302", "25770385299", "25770385189", "25770384960", "25770384815",
	    "25770384794", "25770384733", "25770384686", "25770384623", "25770384277", "25770384142", "25770383925",
	    "25770383922", "25770383867", "25770383800", "25770383266", "25770383151", "25770383145", "25770383142",
	    "25770383128", "25770383126", "25770383103", "25770383092", "25770383083", "25770383067", "25770383054",
	    "25770383049", "25770383047", "25770382793", "25770382745", "25770382629", "25770382621", "25770382518",
	    "25770382506", "25770382481", "25770382473", "25770382436", "25770382432", "25770382430", "25770382429",
	    "25770382428", "25770382427", "25770382420", "25770382417", "25770382414", "25770382410", "25770382384",
	    "25770382376", "25770382369", "25770382367", "25770382362", "25770382361", "25770382351", "25770382346",
	    "25770382338", "25770382331", "25770382330", "25770382224", "25770382127", "25770382114", "25770381962",
	    "25770381604", "25770381576", "25770381424", "25770381261", "25770381260", "25770381259", "25770381257",
	    "25770381255", "25770381220", "25770299204", "3388729677153", "3388729674586", "3388729663650", "3388729661935",
	    "3388729661819", "3388729661815", "3388729661786", "3388729658928", "3388729658502", "3388729656187",
	    "3388729647749", "3388729647721", "3388729647020", "3388729646599", "3388729646330", "3388729645992",
	    "3388729645644", "3388729645578", "3388729645473", "3388729645372", "3388729645150", "3388729642575",
	    "3388729638672", "3388729633237", "3388729630195", "3388729630007", "3388729629973", "3388729629317",
	    "3388729629311", "3388729629022", "3388729628954", "3388729628876", "3388729628802", "3388729627206",
	    "3388729624605", "3388729621482", "3388729619450", "3388729619054", "3388729618576", "3388729617943",
	    "3388729617823", "3388729617679", "3388729616513", "3388729616431", "3388729615715", "3388729614743",
	    "3388729613439", "3388729613114", "3388729612706", "3388729612546", "3388729611018", "3388729610846",
	    "3388729608988", "3388729604960", "3388729604400", "3388729603741", "3388729603617", "3388729602235",
	    "3388729599788", "3388729598966", "3388729598399", "3388729598184", "3388729597996", "3388729596844",
	    "3388729595622", "3388729594825", "3388729592723", "3388729592305", "3388729591631", "3388729591350",
	    "3388729591072", "3388729590850", "3388729590414", "3388729590395", "3388729589814", "3388729589755",
	    "3388729589732", "3388729589713", "3388729589562", "3388729589231", "3388729589213", "3388729589091",
	    "3388729588421", "3388729587618", "3388729587614", "3388729587231", "3388729587053", "3388729586420",
	    "3388729585910", "3388729585741", "3388729584640", "3388729584431", "3388729584296", "3388729584173",
	    "3388729583994", "3388729583986", "3388729580951", "3388729580467", "3388729580161", "3388729579951",
	    "3388729579545", "3388729579179", "3388729579157", "3388729578473", "3388729578444", "3388729578363",
	    "3388729578292", "3388729577740", "3388729577719", "3388729577581", "3388729576804", "3388729576493",
	    "3388729575940", "3388729575512", "3388729575328", "3388729573822", "3388729573501", "3388729571773",
	    "3388729571730", "3388729571520", "3388729570799", "3388729570669", "3388729570596", "3388729570306",
	    "3388729570229", "3388729570083", "3388729569925", "3388729569777", "3388729569599", "3388729569560",
	    "3388729569526", "3388729569426", "3388729569390", "3388729569262", "3388729569242", "3388729569216",
	    "3388729569206", "3388729569099", "3388729569069", "3388729569005", "3388729568979", "3388729568912",
	    "3388729568900", "3388729568878", "3388729568629", "3388729568519", "3388729568511", "3388729568505",
	    "3388729568096", "3388729568062", "3388729568034", "3388729567720", "3388729567522", "3388729567332",
	    "3388729567167", "3388729567095", "3388729567020", "3388729566985", "3388729566759", "3388729566516",
	    "3388729566371", "3388729566350", "3388729566236", "3388729566207", "3388729566053", "3388729566025",
	    "3388729566023", "3388729565837", "3388729565834", "3388729565776", "3388729565710", "3388729565706",
	    "3388729565683", "3388729565548", "3388729565467", "3388729565463", "3388729565442", "3388729565410",
	    "3388729565091", "3388729565041", "3388729565012", "3388729564839", "3388729564783", "3388729564552",
	    "3388729564523", "3388729564490", "3388729564488", "3388729564480", "3388729564476", "3388729564256",
	    "3388729564103", "3388729564084", "3388729564059", "3388729563722", "3388729563564", "3388729563539",
	    "3388729563514", "3388729563156", "3388729562989", "3388729562692", "3388729562681", "3388729562314",
	    "3388729562284", "3388729562254", "3388729562151", "3388729561925", "3388729561902", "3388729561531",
	    "3388729561479", "3388729561467", "3388729561433", "3388729561377", "3388729560881", "3388729560879",
	    "3388729560509", "3388729560454", "3388729560066", "3388729560014", "3388729559730", "3388729559649",
	    "3388729559602", "3388729559377", "3388729559350", "3388729559147", "3388729559098", "3388729558833",
	    "3388729558824", "3388729558683", "3388729558498", "3388729558337", "3388729558278", "3388729558135",
	    "3388729558094", "3388729558023", "3388729557892", "3388729557708", "3388729557642", "3388729557630",
	    "3388729557606", "3388729557516", "3388729557418", "3388729557416", "3388729556434", "3388729556010",
	    "3388729555258", "3388729554956", "3388729551905", "3388729549835"

    });

    private static final List<Class<? extends Event>> CANDIDACY_EVENT_TYPES = new ArrayList<Class<? extends Event>>();
    private static final List<Class<? extends AnnualEvent>> ADMIN_OFFICE_AND_INSURANCE_TYPES = new ArrayList<Class<? extends AnnualEvent>>();

    static {
	CANDIDACY_EVENT_TYPES.add(DFACandidacyEvent.class);
	CANDIDACY_EVENT_TYPES.add(PhdProgramCandidacyEvent.class);
	CANDIDACY_EVENT_TYPES.add(IndividualCandidacyEvent.class);

	ADMIN_OFFICE_AND_INSURANCE_TYPES.add(AdministrativeOfficeFeeAndInsuranceEvent.class);
	ADMIN_OFFICE_AND_INSURANCE_TYPES.add(InsuranceEvent.class);
    }

    private static boolean IS_CANDIDACY_EVENT(final Event event) {
	for (Class<? extends Event> type : CANDIDACY_EVENT_TYPES) {
	    if (type.isAssignableFrom(event.getClass())) {
		return true;
	    }
	}

	return false;
    }

    private static boolean ALL_OTHER(final Event event) {
	if (event.isGratuity()) {
	    return false;
	}

	if (event.isAcademicServiceRequestEvent()) {
	    return false;
	}

	return !IS_CANDIDACY_EVENT(event) && !IS_ADMIN_OFFICE_OR_INSURANCE_EVENT(event);
    }

    private static boolean IS_ADMIN_OFFICE_OR_INSURANCE_EVENT(final Event event) {
	for (Class<? extends Event> type : ADMIN_OFFICE_AND_INSURANCE_TYPES) {
	    if (type.isAssignableFrom(event.getClass())) {
		return true;
	    }
	}

	return false;
    }

    private List<Event> eventsToProcess = null;

    public EventReportQueueJob() {
	super();
    }

    @Override
    public String getFilename() {
	return "outros.xls";
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	List<Spreadsheet> buildReport = buildReport();

	Spreadsheet.exportToXLSSheets(byteArrayOS, buildReport);

	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }

    private List<Spreadsheet> buildReport() {
	final Spreadsheet eventsSheet = allEvents();
	final Spreadsheet exemptionsSheet = allExemptions();
	final Spreadsheet transactionsSheet = allTransactions();

	return Arrays.asList(new Spreadsheet[] { eventsSheet, exemptionsSheet, transactionsSheet });
    }

    /* ALL EVENTS */
    private Spreadsheet allEvents() {
	final Spreadsheet spreadsheet = new Spreadsheet("dividas");

	defineHeadersForEvents(spreadsheet);

	List<Event> accountingEvents = getAccountingEvents();
	System.out.println(String.format("%s events to process", accountingEvents.size()));
	int count = 0;
	for (Event event : accountingEvents) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    if (++count % 1000 == 0) {
		System.out.println(String.format("Processed %s events", count));
	    }

	    try {
		writeEvent(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
		// e.printStackTrace(System.out);
	    }
	}
	return spreadsheet;

    }

    private List<Event> getAccountingEvents() {
	if (this.eventsToProcess != null) {
	    return this.eventsToProcess;
	}

	this.eventsToProcess = new ArrayList<Event>();

	// for (String eventId : EVENTS) {
	// eventsToProcess.add((Event) Event.fromExternalId(eventId));
	// }

	int count = 0;
	ExecutionYear EXECUTION_YEAR_2009_2010 = ExecutionYear.readExecutionYearByName("2009/2010");
	for (Event event : RootDomainObject.getInstance().getAccountingEvents()) {

	    if (++count % 1000 == 0) {
		System.out.println(String.format("Read %s events", count));
	    }

	    if (event.isCancelled()) {
		continue;
	    }

	    Wrapper wrapper = buildWrapper(event);

	    if (wrapper == null) {
		continue;
	    }

	    if (!wrapper.isAfterOrEqualExecutionYear(EXECUTION_YEAR_2009_2010)) {
		continue;
	    }

	    if (!ALL_OTHER(event)) {
		continue;
	    }

	    eventsToProcess.add(event);
	}

	return eventsToProcess;

	// return Collections.singletonList((Event)
	// Event.fromExternalId("2310693429788"));
    }

    private void writeEvent(final Event event, final Spreadsheet spreadsheet) {
	Row row = spreadsheet.addRow();

	Wrapper wrapper = buildWrapper(event);

	row.setCell(event.getExternalId());
	row.setCell(wrapper.getStudentNumber());
	row.setCell(wrapper.getStudentName());
	row.setCell(wrapper.getRegistrationStartDate());
	row.setCell(wrapper.getExecutionYear());
	row.setCell(wrapper.getStudiesType());
	row.setCell(wrapper.getDegreeName());
	row.setCell(wrapper.getDegreeType());
	row.setCell(wrapper.getPhdProgramName());
	row.setCell(wrapper.getEnrolledECTS());
	row.setCell(wrapper.getRegime());
	row.setCell(wrapper.getEnrolmentModel());
	row.setCell(wrapper.getResidenceYear());
	row.setCell(wrapper.getResidenceMonth());

	row.setCell(event.getDescription().toString());
	row.setCell(event.getWhenOccured().toString("dd/MM/yyyy"));
	row.setCell(event.getTotalAmountToPay().toPlainString());
	row.setCell(event.getPayedAmount().toPlainString());
	row.setCell(event.getAmountToPay().toPlainString());
	row.setCell(event.getReimbursableAmount().toPlainString());
	row.setCell(event.getTotalDiscount().toPlainString());
    }

    private void defineHeadersForEvents(final Spreadsheet spreadsheet) {
	// Academical information
	spreadsheet.setHeader("Identificador");
	spreadsheet.setHeader("Aluno");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data inscrição");
	spreadsheet.setHeader("Ano lectivo");
	spreadsheet.setHeader("Tipo de matricula");
	spreadsheet.setHeader("Nome do Curso");
	spreadsheet.setHeader("Tipo de curso");
	spreadsheet.setHeader("Programa doutoral");
	spreadsheet.setHeader("ECTS inscritos");
	spreadsheet.setHeader("Regime");
	spreadsheet.setHeader("Modelo de inscrição");

	// Residence Information
	spreadsheet.setHeader("Residência - Ano");
	spreadsheet.setHeader("Residência - Mês");

	// Event information
	spreadsheet.setHeader("Tipo de divida");
	spreadsheet.setHeader("Data de criação");
	spreadsheet.setHeader("Valor Total");
	spreadsheet.setHeader("Valor Pago");
	spreadsheet.setHeader("Valor em divida");
	spreadsheet.setHeader("Valor Reembolsável");
	spreadsheet.setHeader("Desconto");

    }

    /* ALL EXEMPTIONS */
    private Spreadsheet allExemptions() {
	final Spreadsheet spreadsheet = new Spreadsheet("Isenções");

	defineHeadersForExemptions(spreadsheet);

	int count = 0;
	List<Event> accountingEvents = getAccountingEvents();
	System.out.println(String.format("Exemptions for %s events", accountingEvents.size()));
	for (Event event : accountingEvents) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    if (++count % 1000 == 0) {
		System.out.println(String.format("Processed %s events", count));
	    }

	    try {
		writeExemptionInformation(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
	    }
	}
	return spreadsheet;

    }

    // write Exemption Information
    private void writeExemptionInformation(Event event, Spreadsheet spreadsheet) {
	Set<Exemption> exemptionsSet = event.getExemptionsSet();

	for (Exemption exemption : exemptionsSet) {

	    ExemptionWrapper wrapper = new ExemptionWrapper(exemption);
	    Row row = spreadsheet.addRow();

	    row.setCell(event.getExternalId());
	    row.setCell(wrapper.getExemptionTypeDescription());
	    row.setCell(wrapper.getExemptionValue());
	    row.setCell(wrapper.getPercentage());
	    row.setCell(wrapper.getJustification());
	}
    }

    private void defineHeadersForExemptions(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Identificador");
	spreadsheet.setHeader("Tipo da Isenção");
	spreadsheet.setHeader("Valor da Isenção");
	spreadsheet.setHeader("Percentagem da Isenção");
	spreadsheet.setHeader("Motivo da Isenção");
    }

    /* ALL TRANSACTIONS */

    private Spreadsheet allTransactions() {
	final Spreadsheet spreadsheet = new Spreadsheet("Transacções");

	defineHeadersForTransactions(spreadsheet);

	int count = 0;
	List<Event> accountingEvents = getAccountingEvents();
	System.out.println(String.format("Transactions for %s events", accountingEvents.size()));
	for (Event event : accountingEvents) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    if (++count % 1000 == 0) {
		System.out.println(String.format("Processed %s events", count));
	    }

	    try {
		writeTransactionInformation(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
	    }
	}
	return spreadsheet;

    }

    private void writeTransactionInformation(Event event, Spreadsheet spreadsheet) {
	for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {

	    for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
		Row row = spreadsheet.addRow();

		row.setCell(event.getExternalId());
		row.setCell(transaction.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(transaction.getOriginalAmount().toPlainString());
		row.setCell(transaction.getAmountWithAdjustment().toPlainString());
		row.setCell(transaction.getPaymentMode().getLocalizedName());
		row.setCell(adjustment.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(adjustment.getAmountWithAdjustment().toPlainString());
		row.setCell(adjustment.getComments());
	    }

	    if (transaction.getAdjustmentTransactions().isEmpty()) {
		Row row = spreadsheet.addRow();

		row.setCell(event.getExternalId());
		row.setCell(transaction.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(transaction.getOriginalAmount().toPlainString());
		row.setCell(transaction.getAmountWithAdjustment().toPlainString());
		row.setCell(transaction.getPaymentMode().getLocalizedName());
		row.setCell("-");
		row.setCell("-");
		row.setCell("-");
	    }
	}
    }

    private void defineHeadersForTransactions(final Spreadsheet spreadsheet) {
	// Transaction Information
	spreadsheet.setHeader("Identificador");
	spreadsheet.setHeader("Data de entrada do pagamento");
	spreadsheet.setHeader("Montante inicial");
	spreadsheet.setHeader("Montante ajustado");
	spreadsheet.setHeader("Modo de pagamento");
	spreadsheet.setHeader("Data de entrada do ajuste");
	spreadsheet.setHeader("Montante do ajuste");
	spreadsheet.setHeader("Justificação");
    }

    // Residence

    private Wrapper buildWrapper(Event event) {
	if (event.isGratuity()) {
	    // return new GratuityEventWrapper((GratuityEvent) event);
	} else if (event.isAcademicServiceRequestEvent()) {
	    // return new
	    // AcademicServiceRequestEventWrapper((AcademicServiceRequestEvent)
	    // event);
	} else if (event.isIndividualCandidacyEvent()) {
	    // return new
	    // IndividualCandidacyEventWrapper((IndividualCandidacyEvent)
	    // event);
	} else if (event.isPhdEvent()) {
	    return new PhdEventWrapper((PhdEvent) event);
	} else {
	    return new EventWrapper(event);
	}

	return null;
    }

    private void copyRowCells(Row from, Row to) {
	List<Object> cells = from.getCells();

	for (Object cell : cells) {
	    to.setCell((String) cell);
	}
    }

    public static List<EventReportQueueJob> retrieveAllGeneratedReports() {
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

}
