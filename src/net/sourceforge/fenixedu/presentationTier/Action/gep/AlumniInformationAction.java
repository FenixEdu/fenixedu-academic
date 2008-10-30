package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniInformationAction extends FenixDispatchAction {

    final private ResourceBundle eBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());

    public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	int totalAlumniCount = RootDomainObject.getInstance().getAlumnisCount();

	int newAlumniCount = 0;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
	    if (alumni.hasStartedPublicRegistry()) {
		newAlumniCount++;
	    }
	}

	int registeredAlumniCount = 0;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
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

	request.setAttribute("statistics1", totalAlumniCount);
	request.setAttribute("statistics2", newAlumniCount);
	request.setAttribute("statistics3", registeredAlumniCount);
	request.setAttribute("statistics4", jobCount);
	request.setAttribute("statistics5", formationCount);
	return mapping.findForward("alumni.showAlumniStatistics");
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
	curriculumData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "DESCRICAO" });

	final Spreadsheet personalData = new Spreadsheet("ALUMNI_PERSONAL_DATA");
	personalData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "MORADA", "COD_POSTAL", "LOCALIDADE", "PAIS", "EMAIL",
		"TELEFONE" });

	final Spreadsheet jobData = new Spreadsheet("ALUMNI_JOB_DATA");
	jobData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE", "PAIS", "COD_AREA_NEGOCIO",
		"AREA_NEGOCIO", "POSICAO", "DATA_INICIO", "DATA_FIM", "TIPO_CONTRATO" });

	final Spreadsheet formationData = new Spreadsheet("ALUMNI_FORMATION_DATA");
	formationData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "GRAU", "INSTITUICAO", "COD_AREA_EDUCATIVA",
		"AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS", "NUMERO_HORAS" });

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
	// "NOME", "NUMERO_ALUNO", "CURSO", "ANO_CONCLUSAO"
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
		    row.setCell(registration.getStartDate().toString("dd/MM/yyyy"));
		    final CycleCurriculumGroup lastConcludedCycle = concludeCycles.last();
		    row.setCell(lastConcludedCycle.isConclusionProcessed() ? lastConcludedCycle.getConclusionDate().toString(
				    "dd/MM/yyyy") : lastConcludedCycle.calculateConclusionDate().toString("dd/MM/yyyy"));
		    row.setCell("Bolonha");
		}

	    } else {

		if (registration.isRegistrationConclusionProcessed()) {
		    Row row = sheet.addRow();
		    row.setCell(alumniName);
		    row.setCell(studentNumber);
		    row.setCell(registration.getDegreeName());
		    row.setCell(registration.getStartDate().toString("dd/MM/yyyy"));
		    row.setCell(registration.getConclusionDate() != null ? registration.getConclusionDate().toString("dd/MM/yyyy") : "n/a");
		    row.setCell("Pre-Bolonha");
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
	row.setCell(alumni.hasPersonalEmail() ? alumni.getPersonalEmail().getValue() : "n/a");
	row.setCell(alumni.getPersonalPhone() != null ? alumni.getPersonalPhone().getNumber() : "n/a");
    }

    private void addJobDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Job job) {
	// "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE", "PAIS",
	// "COD_AREA_NEGOCIO", "AREA_NEGOCIO", "POSICAO", "DATA_INICIO",
	// "DATA_FIM", "TIPO_CONTRATO"
	final Row row = sheet.addRow();
	row.setCell(alumniName);
	row.setCell(studentNumber);
	row.setCell(job.getEmployerName());
	row.setCell(job.getCity());
	row.setCell(job.getCountry().getName());
	row.setCell(job.getBusinessArea().getCode());
	row.setCell(job.getBusinessArea().getDescription().replace(';', '|'));
	row.setCell(job.getPosition());
	row.setCell(job.getBeginDate().toString("dd/MM/YYYY"));
	row.setCell(job.getEndDate() != null ? job.getEndDate().toString("dd/MM/YYYY") : "n/a");
	row.setCell(job.getContractType() != null ? eBundle.getString(job.getContractType().getQualifiedName()) : "n/a");
    }

    private void addFormationDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Formation formation) {
	// "NOME", "NUMERO_ALUNO", "GRAU", "INSTITUICAO", "COD_AREA_EDUCATIVA",
	// "AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS",
	// "NUMERO_HORAS"
	final Row row = sheet.addRow();
	row.setCell(alumniName);
	row.setCell(studentNumber);
	row.setCell(eBundle.getString(formation.getFormationType().getQualifiedName()));
	row.setCell(formation.getInstitution().getUnitName().getName());
	row.setCell(formation.getEducationArea().getCode());
	row.setCell(formation.getEducationArea().getDescription().replace(';', '|'));
	row.setCell(formation.getBeginYear());
	row.setCell(formation.getYear());
	row.setCell(formation.getEctsCredits() != null ? formation.getEctsCredits().toString() : "n/a");
	row.setCell(formation.getFormationHours() != null ? formation.getFormationHours().toString() : "n/a");
    }

}
