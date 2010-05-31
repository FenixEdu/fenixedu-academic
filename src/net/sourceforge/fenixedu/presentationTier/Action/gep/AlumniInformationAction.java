package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniMailSendToBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniInformationAction extends FenixDispatchAction {

    private static final String GABINETE_ESTUDOS_PLANEAMENTO = "Gabinete de Estudos e Planeamento";
    private final ResourceBundle eBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
    private final static String NOT_AVAILABLE = "n/a";
    private final static String DATE_FORMAT = "dd/MM/yyyy";

    public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	int totalAlumniCount = RootDomainObject.getInstance().getAlumnisCount();

	int newAlumniCount = 0;
	int registeredAlumniCount = 0;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
	    if (alumni.hasStartedPublicRegistry()) {
		newAlumniCount++;
	    }
	    if (alumni.hasFinishedPublicRegistry()) {
		registeredAlumniCount++;
	    }
	}

	int jobCount = RootDomainObject.getInstance().getJobsCount();

	int formationCount = 0;
	for (Qualification q : RootDomainObject.getInstance().getQualifications()) {
	    if (q.getClass().equals(Formation.class)) {
		formationCount++;
	    }
	}

	request.setAttribute("statistics1", Role.getRoleByRoleType(RoleType.ALUMNI).getAssociatedPersonsCount());
	request.setAttribute("statistics2", totalAlumniCount);
	request.setAttribute("statistics3", newAlumniCount);
	request.setAttribute("statistics4", registeredAlumniCount);
	request.setAttribute("statistics5", jobCount);
	request.setAttribute("statistics6", formationCount);
	return mapping.findForward("alumni.showAlumniStatistics");
    }

    public ActionForward prepareAddRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("createRecipient", new AlumniMailSendToBean());
	return mapping.findForward("addRecipients");
    }
    
    public ActionForward prepareRemoveRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	EmailBean emailBean = new EmailBean();
	final Set<Sender> availableSenders = Sender.getAvailableSenders();
	for (Sender sender : availableSenders) {
	    if(sender.getFromName().equals(GABINETE_ESTUDOS_PLANEAMENTO)) {
		emailBean.setSender(sender);
		break;
	    }
	}
	request.setAttribute("emailBean", emailBean);
	return mapping.findForward("removeRecipients");
    }
    
    public ActionForward manageRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Set<Sender> availableSenders = Sender.getAvailableSenders();
	Sender gepSender = getGEPSender(availableSenders);
	List<Recipient> recipients = new ArrayList<Recipient>();
	recipients.addAll(gepSender.getRecipients());
	Collections.sort(recipients, new BeanComparator("toName"));
	Collections.reverse(recipients);
	request.setAttribute("recipients",recipients);
	return mapping.findForward("manageRecipients");
    }

    private Sender getGEPSender(final Set<Sender> availableSenders) {
	Sender gepSender = null;
	for (Sender sender : availableSenders) {
	    if(sender.getFromName().equals(GABINETE_ESTUDOS_PLANEAMENTO)) {
		gepSender = sender;
		break;
	    }
	}
	return gepSender;
    }

    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("emailBean", getRenderedObject("emailBean"));
	request.setAttribute("createRecipient", getRenderedObject("createRecipient"));
	
	RenderUtils.invalidateViewState();
	return mapping.findForward("addRecipients");
    }
    
    public ActionForward addRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AlumniMailSendToBean alumniMailSendToBean = (AlumniMailSendToBean) getRenderedObject("createRecipient");
	Sender gepSender = getGEPSender(Sender.getAvailableSenders());
	alumniMailSendToBean.createRecipientGroup(gepSender);
	
	return manageRecipients(mapping, actionForm, request, response);
    }
    
    public ActionForward removeRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	EmailBean emailBean = (EmailBean) getRenderedObject("emailBean");
	emailBean.removeRecipients();
	
	return manageRecipients(mapping, actionForm, request, response);
    }
    
    public ActionForward generateAlumniPartialReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	return generateReport(false, response);
    }

    public ActionForward generateAlumniFullReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	return generateReport(true, response);
    }

    private ActionForward generateReport(boolean full, HttpServletResponse response) throws IOException {
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename(full));
	final ServletOutputStream writer = response.getOutputStream();
	Spreadsheet.exportToXLSSheets(writer, buildReport(full));
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private String getReportFilename(boolean fullReport) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/GEPResources", Language.getLocale());
	return MessageFormat.format(fullReport ? bundle.getString("alumni.full.reports.name") : bundle
		.getString("alumni.partial.reports.name"), new DateTime().toString("ddMMyyyyHHmmss"));
    }

    private List<Spreadsheet> buildReport(boolean fullReport) {

	final Spreadsheet curriculumData = new Spreadsheet("ALUMNI_CURRICULUM_DATA");
	curriculumData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "DESCRICAO",
		"EMPREGADO ACTUALMENTE" });

	final Spreadsheet personalData = new Spreadsheet("ALUMNI_PERSONAL_DATA");
	personalData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "MORADA", "COD_POSTAL", "LOCALIDADE", "PAIS", "EMAIL",
		"TELEFONE" });

	final Spreadsheet jobData = new Spreadsheet("ALUMNI_JOB_DATA");
	jobData.setHeaders(new String[] { "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE", "PAIS",
		"COD_AREA_NEGOCIO", "AREA_NEGOCIO", "POSICAO", "DATA_INICIO", "DATA_FIM", "TIPO_CONTRATO", "FORMA_COLOCACAO",
		"REMUN_MENSAL_BRUTA", "TIPO_SALARIO", "DATA_ALTERACAO", "DATA_REGISTO" });

	final Spreadsheet formationData = new Spreadsheet("ALUMNI_FORMATION_DATA");
	formationData.setHeaders(new String[] { "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "TIPO", "GRAU", "INSTITUICAO",
		"COD_AREA_EDUCATIVA", "AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS", "NUMERO_HORAS", "DATA_ALTERACAO", "DATA_REGISTO" });

	String alumniName;
	Integer studentNumber;

	for (final Alumni alumni : rootDomainObject.getAlumnis()) {
	    if (fullReport || alumni.getUrlRequestToken() != null) {
		alumniName = alumni.getName();
		studentNumber = alumni.getStudentNumber();
		addCurriculumDataRow(curriculumData, alumniName, studentNumber, alumni);
		addPersonalDataRow(personalData, alumniName, studentNumber, alumni);
		for (Job job : alumni.getJobs()) {
		    addJobDataRow(jobData, alumniName, studentNumber, job);
		}
		for (Formation formation : alumni.getFormations()) {
		    addFormationDataRow(formationData, alumniName, studentNumber, formation);
		}
	    }
	}

	List<Spreadsheet> sheets = new ArrayList<Spreadsheet>(4);
	sheets.add(curriculumData);
	sheets.add(personalData);
	sheets.add(jobData);
	sheets.add(formationData);
	return sheets;
    }

    private void addCurriculumDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Alumni alumni) {
	// "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "DESCRICAO",
	// "EMPREGADO ACTUALMENTE"
	for (Registration registration : alumni.getStudent().getRegistrations()) {
	    if (registration.isBolonha()) {
		if (registration.hasConcluded()) {
		    final SortedSet<CycleCurriculumGroup> concludeCycles = new TreeSet<CycleCurriculumGroup>(
			    CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
		    concludeCycles.addAll(registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());
		    Row row = sheet.addRow();
		    row.setCell(alumniName);
		    row.setCell(studentNumber);
		    row.setCell(registration.getDegreeName());
		    row.setCell(registration.getStartDate().toString(DATE_FORMAT));
		    final CycleCurriculumGroup lastConcludedCycle = concludeCycles.last();
		    try {
			row.setCell(lastConcludedCycle.isConclusionProcessed() ? lastConcludedCycle.getConclusionDate().toString(
				DATE_FORMAT) : lastConcludedCycle.calculateConclusionDate().toString(DATE_FORMAT));
		    } catch (Exception ex) {
			row.setCell(NOT_AVAILABLE);
		    }
		    row.setCell("Bolonha");
		    row.setCell(alumni.getIsEmployed() != null ? RenderUtils.getResourceString("APPLICATION_RESOURCES", "label."
			    + alumni.getIsEmployed()) : NOT_AVAILABLE);
		}
	    } else {
		if (registration.isRegistrationConclusionProcessed()) {
		    Row row = sheet.addRow();
		    row.setCell(alumniName);
		    row.setCell(studentNumber);
		    row.setCell(registration.getDegreeName());
		    row.setCell(registration.getStartDate().toString(DATE_FORMAT));
		    row.setCell(registration.getConclusionDate() != null ? registration.getConclusionDate().toString(DATE_FORMAT)
			    : NOT_AVAILABLE);
		    row.setCell("Pre-Bolonha");
		    row.setCell(alumni.getIsEmployed() != null ? RenderUtils.getResourceString("APPLICATION_RESOURCES", "label."
			    + alumni.getIsEmployed()) : NOT_AVAILABLE);
		}
	    }
	}
    }

    private void addPersonalDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Alumni alumni) {
	// "NOME", "NUMERO_ALUNO", "MORADA", "COD_POSTAL", "PAIS", "EMAIL",
	// "TELEFONE", "TELEMOVEL"
	final Row row = sheet.addRow();
	row.setCell(alumniName);
	row.setCell(studentNumber);
	row.setCell(alumni.getLastPersonalAddress().getAddress());
	row.setCell(alumni.getLastPersonalAddress().getAreaCode());
	row.setCell(alumni.getLastPersonalAddress().getArea());
	row.setCell(alumni.getLastPersonalAddress().getCountryOfResidenceName());
	row.setCell(alumni.hasPersonalEmail() ? alumni.getPersonalEmail().getValue() : NOT_AVAILABLE);
	row.setCell(alumni.getPersonalPhone() != null ? alumni.getPersonalPhone().getNumber() : NOT_AVAILABLE);
    }

    private void addJobDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Job job) {
	// "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE",
	// "PAIS",
	// "COD_AREA_NEGOCIO", "AREA_NEGOCIO", "POSICAO", "DATA_INICIO",
	// "DATA_FIM", "FORMA_COLOCACAO", "TIPO_CONTRATO", "SALARIO",
	// "TIPO_SALARIO", "DATA_ALTERACAO", "DATA_REGISTO"
	final Row row = sheet.addRow();
	row.setCell(String.valueOf(job.getOID()));
	row.setCell(alumniName);
	row.setCell(studentNumber);
	row.setCell(job.getEmployerName());
	row.setCell(job.getCity());
	row.setCell(job.getCountry() != null ? job.getCountry().getName() : NOT_AVAILABLE);
	row.setCell(job.getBusinessArea() != null ? job.getBusinessArea().getCode() : NOT_AVAILABLE);
	row.setCell(job.getBusinessArea() != null ? job.getBusinessArea().getDescription().replace(';', '|') : NOT_AVAILABLE);
	row.setCell(job.getPosition());
	row.setCell(job.getBeginDate() != null ? job.getBeginDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
	row.setCell(job.getEndDate() != null ? job.getEndDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
	row.setCell(job.getContractType() != null ? eBundle.getString(job.getContractType().getQualifiedName()) : NOT_AVAILABLE);
	row.setCell(job.getJobApplicationType() != null ? eBundle.getString(job.getJobApplicationType().getQualifiedName())
		: NOT_AVAILABLE);
	row.setCell(job.getSalary() != null ? job.getSalary().toString() : NOT_AVAILABLE);
	row.setCell(job.getSalaryType() != null ? eBundle.getString(job.getSalaryType().getQualifiedName()) : NOT_AVAILABLE);
	row.setCell(job.getLastModifiedDate() != null ? job.getLastModifiedDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
	AlumniIdentityCheckRequest lastIdentityRequest = job.getPerson().getStudent().getAlumni().getLastIdentityRequest();
	row.setCell(lastIdentityRequest != null ? lastIdentityRequest.getCreationDateTime().toString(DATE_FORMAT) : NOT_AVAILABLE);
    }

    private void addFormationDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Formation formation) {
	// "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "TIPO", "GRAU",
	// "INSTITUICAO",
	// "COD_AREA_EDUCATIVA",
	// "AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS",
	// "NUMERO_HORAS", "DATA_ALTERACAO", "DATA_REGISTO"
	final Row row = sheet.addRow();
	row.setCell(String.valueOf(formation.getOID()));
	row.setCell(alumniName);
	row.setCell(studentNumber);
	row.setCell(formation.getFormationType() != null ? eBundle.getString(formation.getFormationType().getQualifiedName())
		: NOT_AVAILABLE);
	row.setCell(formation.getType() != null ? eBundle.getString(formation.getType().getQualifiedName()) : NOT_AVAILABLE);
	row.setCell(formation.getInstitution() != null ? formation.getInstitution().getUnitName().getName() : NOT_AVAILABLE);
	row.setCell(formation.getEducationArea() != null ? formation.getEducationArea().getCode() : NOT_AVAILABLE);
	row.setCell(formation.getEducationArea() != null ? formation.getEducationArea().getDescription().replace(';', '|')
		: NOT_AVAILABLE);
	row.setCell(formation.getBeginYear());
	row.setCell(formation.getYear());
	row.setCell(formation.getEctsCredits() != null ? formation.getEctsCredits().toString() : NOT_AVAILABLE);
	row.setCell(formation.getFormationHours() != null ? formation.getFormationHours().toString() : NOT_AVAILABLE);
	row.setCell(formation.getLastModificationDateDateTime() != null ? formation.getLastModificationDateDateTime().toString(DATE_FORMAT) : NOT_AVAILABLE);
	AlumniIdentityCheckRequest lastIdentityRequest = formation.getPerson().getStudent().getAlumni().getLastIdentityRequest();
	row.setCell(lastIdentityRequest != null ? lastIdentityRequest.getCreationDateTime().toString(DATE_FORMAT) : NOT_AVAILABLE);
    }

    public ActionForward searchAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AlumniSearchBean searchBean;
	final IViewState viewState = RenderUtils.getViewState("searchAlumniBean");
	if (viewState != null) {
	    searchBean = (AlumniSearchBean) viewState.getMetaObject().getObject();
	} else {
	    searchBean = new AlumniSearchBean();
	}

	List<Registration> registrations = Alumni.readRegistrations(searchBean);
	searchBean.setAlumni(new ArrayList<Registration>(registrations));

	java.util.List<AlumniSearchResultItemBean> alumniSearchResultItems = new java.util.ArrayList<AlumniSearchResultItemBean>();

	for (Registration registration : registrations) {
	    alumniSearchResultItems.add(new AlumniSearchResultItemBean(registration));
	}

	RenderUtils.invalidateViewState();
	request.setAttribute("searchAlumniBean", searchBean);
	request.setAttribute("alumniResultItems", alumniSearchResultItems);
	return mapping.findForward("alumni.showAlumniDetails");
    }

    public ActionForward searchAlumniError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("searchAlumniBean", getFromRequest(request, "searchAlumniBean"));
	return mapping.findForward("alumni.showAlumniDetails");
    }

    public static class AlumniSearchResultItemBean implements java.io.Serializable {
	private Registration registration;

	public AlumniSearchResultItemBean(final Registration registration) {
	    this.registration = registration;
	}

	public String getName() {
	    return this.registration.getPerson().getName();
	}

	public String getDegree() {
	    return registration.getDegree().getPresentationName();
	}

	public String getStartYear() {
	    return registration.getStartExecutionYear().getYear();
	}

	public YearMonthDay getConclusionDateForBolonha() {
	    return this.registration.getConclusionDateForBolonha();
	}

	public Boolean getActiveAlumni() {
	    return this.registration.getStudent().getActiveAlumni();
	}

	public String getEmail() {
	    if (registration.getPerson().getDefaultEmailAddress() == null) {
		return NOT_AVAILABLE;
	    } else if (!registration.getPerson().getDefaultEmailAddress().getVisibleToEmployees()) {
		return NOT_AVAILABLE;
	    }

	    return registration.getPerson().getDefaultEmailAddress().getValue();
	}

	public String getMobilePhone() {
	    if (registration.getPerson().getDefaultMobilePhone() == null) {
		return NOT_AVAILABLE;
	    } else if (!registration.getPerson().getDefaultMobilePhone().getVisibleToEmployees()) {
		return NOT_AVAILABLE;
	    }
	    return registration.getPerson().getDefaultMobilePhone().getNumber();
	}
    }
}
