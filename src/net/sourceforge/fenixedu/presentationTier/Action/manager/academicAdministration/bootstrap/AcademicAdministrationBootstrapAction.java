package net.sourceforge.fenixedu.presentationTier.Action.manager.academicAdministration.bootstrap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/academicAdministrationBootstrap", module = "manager")
@Forwards({ @Forward(name = "index", path = "/manager/academicAdministration/bootstrap/index.jsp") })
public class AcademicAdministrationBootstrapAction extends FenixDispatchAction {

    private static final String CREATE_REGISTRATION = new AcademicAuthorizationGroup(AcademicOperationType.CREATE_REGISTRATION)
	    .getExpression();

    private static final String SERVICE_REQUESTS = new AcademicAuthorizationGroup(AcademicOperationType.SERVICE_REQUESTS)
	    .getExpression();

    private static final String SERVICE_REQUESTS_RECTORAL_SENDING = new AcademicAuthorizationGroup(
	    AcademicOperationType.SERVICE_REQUESTS_RECTORAL_SENDING).getExpression();

    private static final String MANAGE_AUTHORIZATIONS = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_AUTHORIZATIONS).getExpression();

    private static final String STUDENT_LISTINGS = new AcademicAuthorizationGroup(AcademicOperationType.STUDENT_LISTINGS)
	    .getExpression();

    private static final String MANAGE_EXECUTION_COURSES = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_EXECUTION_COURSES).getExpression();

    private static final String MANAGE_DEGREE_CURRICULAR_PLANS = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS).getExpression();

    private static final String MANAGE_PRICES = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PRICES)
	    .getExpression();

    private static final String MANAGE_EXTRA_CURRICULAR_ACTIVITIES = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_EXTRA_CURRICULAR_ACTIVITIES).getExpression();

    private static final String MANAGE_EXTERNAL_UNITS = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_EXTERNAL_UNITS).getExpression();

    private static final String MANAGE_CANDIDACY_PROCESSES = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_CANDIDACY_PROCESSES).getExpression()
	    + " || "
	    + new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES).getExpression();

    private static final String MARKSHEET_MANAGEMENT = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_MARKSHEETS)
	    .getExpression();

    private static final String MANAGE_CONTRIBUTORS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_CONTRIBUTORS)
	    .getExpression();

    private static final String MANAGE_DOCUMENTS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_DOCUMENTS)
	    .getExpression();

    private static final String MANAGE_PHD_ENROLMENT_PERIODS = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_PHD_ENROLMENT_PERIODS).getExpression();

    private static final String MANAGE_PHD_PROCESSES = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES)
	    .getExpression();

    private static final String MANAGE_PHDS = MANAGE_PHD_ENROLMENT_PERIODS + " || " + MANAGE_PHD_PROCESSES;

    private static final String MANAGE_STUDENTS = "academic('OFFICE')";

    private static final String REPORT_STUDENTS_UTL_CANDIDATES = new AcademicAuthorizationGroup(
	    AcademicOperationType.REPORT_STUDENTS_UTL_CANDIDATES).getExpression();

    private static final String MANAGE_REGISTERED_DEGREE_CANDIDACIES = new AcademicAuthorizationGroup(
	    AcademicOperationType.MANAGE_REGISTERED_DEGREE_CANDIDACIES).getExpression();

    private static final String MANAGE_EVENT_REPORTS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_EVENT_REPORTS)
	    .getExpression();

    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("index");
    }

    public ActionForward createPortals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	makePortals();
	return null;
    }

    @Service
    private void makePortals() {
	Module root = rootDomainObject.getRootModule();
	Portal portal = rootDomainObject.getRootPortal();

	fillAdministration(root, portal);
    }

    private void fillAdministration(Module parent, Container parentSection) {
	Module administrationModule = makeModule(parent, "Administração Académica", "/academicAdministration");

	Section administrationSection = makeSection(parentSection, "Administração Académica", "academic('ADMINISTRATION')");

	Module officeModule = makeModule(parent, "Secretaria Académica", "/academicAdministration");

	Section officeSection = makeSection(parentSection, "Secretaria Académica", "academic('OFFICE')");

	makeFunctionality(administrationModule, administrationSection, "Administração Académica",
		"/academicAdministration.do?method=indexAdmin", "everyone");

	makeFunctionality(officeModule, officeSection, "Secretaria Académica", "/academicAdministration.do?method=indexOffice",
		"everyone");

	fillAuthorizations(administrationModule, administrationSection);

	fillStudentOperations(officeModule, officeSection);
	fillAcademicServiceRequests(officeModule, officeSection);
	fillListings(administrationModule, administrationSection);

	fillExecutionCourseManagement(administrationModule, administrationSection);

	fillDegreeCurricularPlanManagement(administrationModule, administrationSection);

	fillDepartmentDegrees(administrationModule, administrationSection);

	fillPriceManagement(administrationModule, administrationSection);

	fillExtraCurricularActivitiesManagement(administrationModule, administrationSection);

	fillExternalUnitsManagement(administrationModule, administrationSection);

	// Candidacies

	fillOver23Candidacies(officeModule, officeSection);
	fillSecondCycleCandidacies(officeModule, officeSection);
	fillGraduatedPersonCandidacies(officeModule, officeSection);
	fillDegreeChangeManagement(officeModule, officeSection);
	fillDegreeTransferManagement(officeModule, officeSection);
	fillStandaloneCandidacies(officeModule, officeSection);
	fillMobilityCandidacies(officeModule, officeSection);

	// marksheets

	fillMarksheets(administrationModule, administrationSection);

	fillOldMarksheets(administrationModule, administrationSection);

	fillViewMarksheets(administrationModule, administrationSection);

	// contributors

	fillCreateContributors(administrationModule, administrationSection);

	fillViewContributors(administrationModule, administrationSection);

	fillEditContributors(administrationModule, administrationSection);

	// institutions

	fillDocuments(officeModule, officeSection);

	fillStudentsUtlCandidates(administrationModule, administrationSection);

	fillManageRegisteredDegreeCandidacies(administrationModule, administrationSection);

	fillManageEventReports(administrationModule, administrationSection);

	// Phd

	fillPhdProcessManagement(officeModule, officeSection);
	fillPhdCandidacyPeriodsManagement(officeModule, officeSection);
	fillPhdEnrolmentPeriodsManagement(officeModule, officeSection);
    }

    private void fillStudentOperations(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Operações de Alunos", "/");

	Section section = makeSection(parentSection, "Operações de Alunos", CREATE_REGISTRATION + " || " + MANAGE_STUDENTS);

	makeFunctionality(module, section, "Matricular Aluno", "/createStudent.do?method=prepareCreateStudent",
		CREATE_REGISTRATION);

	makeFunctionality(module, section, "Visualizar Alunos", "/students.do?method=prepareSearch", MANAGE_STUDENTS);
    }

    private void fillMarksheets(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Pautas", "/");

	Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

	makeFunctionality(module, section, "Gestão Pautas", "/markSheetManagement.do?method=prepareSearchMarkSheet",
		MARKSHEET_MANAGEMENT);
    }

    private void fillOldMarksheets(Module parent, Section parentSection) {
	Module module = makeModule(parent, "Pautas", "/");

	Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

	makeFunctionality(module, section, "Gestão Pautas Antigas", "/oldMarkSheetManagement.do?method=prepareSearchMarkSheet",
		MARKSHEET_MANAGEMENT);
    }

    private void fillViewMarksheets(Module parent, Section parentSection) {
	Module module = makeModule(parent, "Pautas", "/");

	Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

	makeFunctionality(module, section, "Consulta", "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare",
		MARKSHEET_MANAGEMENT);
    }

    private void fillCreateContributors(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Contribuintes", "/");

	Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

	makeFunctionality(module, section, "Criar Contribuinte", "/createContributorDispatchAction.do?method=prepare",
		MANAGE_CONTRIBUTORS);
    }

    private void fillViewContributors(Module parent, Section parentSection) {
	Module module = makeModule(parent, "Contribuintes", "/");

	Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

	makeFunctionality(module, section, "Consulta de Contribuintes", "/visualizeContributors.do?method=prepare",
		MANAGE_CONTRIBUTORS);
    }

    private void fillEditContributors(Module parent, Section parentSection) {
	Module module = makeModule(parent, "Contribuintes", "/");

	Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

	makeFunctionality(module, section, "Alteração de Contribuintes", "/editContributors.do?method=prepare",
		MANAGE_CONTRIBUTORS);
    }

    private void fillAcademicServiceRequests(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Serviços Académicos", "/");

	Section section = makeSection(parentSection, "Serviços Académicos", SERVICE_REQUESTS + " || "
		+ SERVICE_REQUESTS_RECTORAL_SENDING);

	makeFunctionality(module, section, "Pedidos Novos",
		"/academicServiceRequestsManagement.do?method=search&academicSituationType=NEW", SERVICE_REQUESTS);

	makeFunctionality(module, section, "Pedidos em Processamento",
		"/academicServiceRequestsManagement.do?method=search&academicSituationType=PROCESSING", SERVICE_REQUESTS);

	makeFunctionality(module, section, "Pedidos Concluídos",
		"/academicServiceRequestsManagement.do?method=search&academicSituationType=CONCLUDED", SERVICE_REQUESTS);

	makeFunctionality(module, section, "Pedidos por Curso", "/requestListByDegree.do?method=prepareSearch", SERVICE_REQUESTS);

	makeFunctionality(module, section, "Envio à Reitoria", "/rectorateDocumentSubmission.do?method=index",
		SERVICE_REQUESTS_RECTORAL_SENDING);
    }

    private void fillListings(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Listagens", "/");

	Section section = makeSection(parentSection, "Listagens", STUDENT_LISTINGS);

	makeFunctionality(module, section, "Alunos por Curso", "/studentsListByDegree.do?method=prepareByDegree",
		STUDENT_LISTINGS);

	makeFunctionality(module, section, "Alunos por Disciplina",
		"/studentsListByCurricularCourse.do?method=prepareByCurricularCourse", STUDENT_LISTINGS);

    }

    private void fillAuthorizations(Module parent, Section parentSection) {
	makeFunctionality(parent, parentSection, "Autorizações", "/authorizations.do?method=authorizations",
		MANAGE_AUTHORIZATIONS);
    }

    private void fillExecutionCourseManagement(final Module parent, final Section parentSection) {
	Module module = makeModule(parent, "Disciplinas de execução", "/");

	Section section = makeSection(parentSection, "Gestão das disciplinas de execução", MANAGE_EXECUTION_COURSES);

	makeFunctionality(module, section, "Início", "/executionCourseManagement.do?method=index", MANAGE_EXECUTION_COURSES);
    }

    private void fillDegreeCurricularPlanManagement(final Module parent, final Section parentSection) {
	Module module = makeModule(parent, "Gestão da Estrutura de Ensino", "/");

	Section section = makeSection(parentSection, "Gestão da Estrutura de Ensino", MANAGE_DEGREE_CURRICULAR_PLANS);

	makeFunctionality(module, section, "Estrutura de Cursos", "/bolonha/curricularPlans/curricularPlansManagement.faces",
		MANAGE_DEGREE_CURRICULAR_PLANS);
    }

    private void fillDepartmentDegrees(Module parent, Section parentSection) {
	Module module = makeModule(parent, "Gestão da Estrutura de Ensino", "/");

	Section section = makeSection(parentSection, "Gestão da Estrutura de Ensino", MANAGE_DEGREE_CURRICULAR_PLANS);

	makeFunctionality(module, section, "Gerir Cursos de Departamentos", "/manageDepartmentDegrees.do?method=prepare",
		MANAGE_DEGREE_CURRICULAR_PLANS);
    }

    private void fillPriceManagement(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Pagamentos", "/");

	Section section = makeSection(parentSection, "Preçários", MANAGE_PRICES);

	makeFunctionality(module, section, "Preçário", "/pricesManagement.do?method=viewPrices", MANAGE_PRICES);
    }

    private void fillExtraCurricularActivitiesManagement(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Actividades Extra Curriculares", "/");

	Section section = makeSection(parentSection, "Actividades Extra Curriculares", MANAGE_EXTRA_CURRICULAR_ACTIVITIES);

	makeFunctionality(module, section, "Gerir Tipos de Actividades Extra Curriculares",
		"/manageExtraCurricularActivities.do?method=list", MANAGE_EXTRA_CURRICULAR_ACTIVITIES);
    }

    private void fillExternalUnitsManagement(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Instituições", "/");

	Section section = makeSection(parentSection, "Instituições", MANAGE_EXTERNAL_UNITS);

	makeFunctionality(module, section, "Instituições Externas", "/externalUnits.do?method=prepareSearch",
		MANAGE_EXTERNAL_UNITS);
    }

    private void fillOver23Candidacies(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Maiores 23", "/caseHandlingOver23CandidacyProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillSecondCycleCandidacies(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "2º Ciclo", "/caseHandlingSecondCycleCandidacyProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillGraduatedPersonCandidacies(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Cursos Médios e Superiores",
		"/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro", MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillDegreeChangeManagement(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Mudanças de Curso", "/caseHandlingDegreeChangeCandidacyProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillDegreeTransferManagement(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Transferências", "/caseHandlingDegreeTransferCandidacyProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillStandaloneCandidacies(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Curriculares Isoladas", "/caseHandlingStandaloneCandidacyProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillMobilityCandidacies(Module parent, Section parentSection) {

	Module module = makeModule(parent, "Candidaturas", "/");

	Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

	makeFunctionality(module, section, "Mobilidade", "/caseHandlingMobilityApplicationProcess.do?method=intro",
		MANAGE_CANDIDACY_PROCESSES);
    }

    private void fillDocuments(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Documentos", "/");

	Section section = makeSection(parentSection, "Documentos", MANAGE_DOCUMENTS);

	makeFunctionality(module, section, "IRS Anual", "/generatedDocuments.do?method=prepareSearchPerson", MANAGE_DOCUMENTS);
    }

    private void fillStudentsUtlCandidates(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Bolsas", "/");

	Section section = makeSection(parentSection, "Bolsas", REPORT_STUDENTS_UTL_CANDIDATES);

	makeFunctionality(module, section, "Bolsas da UTL", "/reportStudentsUTLCandidates.do?method=prepare",
		REPORT_STUDENTS_UTL_CANDIDATES);
    }

    private void fillManageRegisteredDegreeCandidacies(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Matriculas", "/");

	Section section = makeSection(parentSection, "Matriculas", MANAGE_REGISTERED_DEGREE_CANDIDACIES);

	makeFunctionality(module, section, "Alunos matriculados 1º ano 1º vez", "/registeredDegreeCandidacies.do?method=view",
		MANAGE_REGISTERED_DEGREE_CANDIDACIES);
    }

    private void fillManageEventReports(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Relatório de dividas", "/");

	Section section = makeSection(parentSection, "Relatório de dividas", MANAGE_EVENT_REPORTS);

	makeFunctionality(module, section, "Relatório de dividas", "/eventReports.do?method=listReports", MANAGE_EVENT_REPORTS);
    }

    private void fillPhdProcessManagement(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Doutoramentos", "/");

	Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

	makeFunctionality(module, section, "Processos de Doutoramento", "/phdIndividualProgramProcess.do?method=manageProcesses",
		MANAGE_PHD_PROCESSES);
    }

    private void fillPhdCandidacyPeriodsManagement(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Doutoramentos", "/");

	Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

	makeFunctionality(module, section, "Gestão dos períodos de candidatura", "/phdCandidacyPeriodManagement.do?method=list",
		MANAGE_PHD_PROCESSES);
    }

    private void fillPhdEnrolmentPeriodsManagement(Module parent, Container parentSection) {
	Module module = makeModule(parent, "Doutoramentos", "/");

	Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

	makeFunctionality(module, section, "Periodos de Inscrição",
		"/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods", MANAGE_PHD_ENROLMENT_PERIODS);
    }

    private static <T extends Content> T findChild(Container module, String name) {
	for (final Node node : module.getChildrenSet()) {
	    final Content child = node.getChild();
	    if (child.getName().getContent().equals(name)) {
		return (T) child;
	    }
	}
	return null;
    }

    private static FunctionalityCall findCall(Section section, Functionality functionality) {
	for (final Node node : section.getChildrenSet()) {
	    final Content child = node.getChild();
	    if (child instanceof FunctionalityCall) {
		FunctionalityCall call = (FunctionalityCall) child;
		if (call.getFunctionality().equals(functionality)) {
		    return call;
		}
	    }
	}
	return null;
    }

    private static Module makeModule(Module parent, String name, String prefix) {
	Module module = findChild(parent, name);
	if (module == null) {
	    module = new Module(new MultiLanguageString(name), prefix);
	    parent.addChild(module);
	}
	return module;
    }

    private static Section makeSection(Container parentSection, String name, String groupExpression) {
	Section section = findChild(parentSection, name);
	if (section == null) {
	    section = new Section(parentSection, new MultiLanguageString(name));
	}
	if (section.getAvailabilityPolicy() != null) {
	    section.getAvailabilityPolicy().delete();
	}
	new ExpressionGroupAvailability(section, groupExpression);
	return section;
    }

    private static void makeFunctionality(Module parent, Section parentSection, String name, String path, String groupExpression) {
	Functionality function = findChild(parent, name);
	if (function == null) {
	    function = new Functionality(new MultiLanguageString(name));
	    parent.addChild(function);
	}
	function.setExecutionPath(path);
	FunctionalityCall call = findCall(parentSection, function);
	if (call == null) {
	    call = new FunctionalityCall(function);
	    parentSection.addChild(call);
	}
	if (call.getAvailabilityPolicy() != null) {
	    call.getAvailabilityPolicy().delete();
	}
	new ExpressionGroupAvailability(call, groupExpression);
    }
}
