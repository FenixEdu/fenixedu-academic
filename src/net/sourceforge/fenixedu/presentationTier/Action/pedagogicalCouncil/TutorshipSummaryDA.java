package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.excel.SpreadsheetXLSExporter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/tutorshipSummary", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "searchTeacher", path = "/pedagogicalCouncil/tutorship/tutorTutorships.jsp"),
	@Forward(name = "createSummary", path = "/pedagogicalCouncil/tutorship/createSummary.jsp"),
	@Forward(name = "editSummary", path = "/pedagogicalCouncil/tutorship/editSummary.jsp"),
	@Forward(name = "processCreateSummary", path = "/pedagogicalCouncil/tutorship/processCreateSummary.jsp"),
	@Forward(name = "confirmCreateSummary", path = "/pedagogicalCouncil/tutorship/confirmCreateSummary.jsp"),
	@Forward(name = "viewSummary", path = "/pedagogicalCouncil/tutorship/viewSummary.jsp") })
public class TutorshipSummaryDA extends ViewStudentsByTutorDispatchAction {

    public ActionForward searchTeacher(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorSummaryBean bean = (TutorSummaryBean) getRenderedObject("tutorateBean");

	if (bean == null) {
	    bean = new TutorSummaryBean();
	} else {
	    if (bean.getTeacher() != null) {
		getTutorships(request, bean.getTeacher());

		request.setAttribute("tutor", bean.getTeacher());
	    }
	}
	request.setAttribute("tutorateBean", bean);

	return mapping.findForward("searchTeacher");
    }

    public ActionForward postback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorSummaryBean bean = (TutorSummaryBean) getRenderedObject("tutorateBean");

	RenderUtils.invalidateViewState();

	request.setAttribute("tutorateBean", bean);

	return mapping.findForward("searchTeacher");
    }

    public ActionForward exportSummaries(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorSummaryBean bean = (TutorSummaryBean) getRenderedObject("tutorateBean");

	if (bean == null) {
	    return searchTeacher(mapping, actionForm, request, response);
	}

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=test.xls");

	final ServletOutputStream writer = response.getOutputStream();

	final List<Spreadsheet> spreadsheets = new ArrayList<Spreadsheet>();

	spreadsheets.add(generateGeneralSheet(bean.getPastSummaries()));
	spreadsheets.add(generateDetailedSheet(bean.getPastSummaries()));

	new SpreadsheetXLSExporter().exportToXLSSheets(writer, spreadsheets);

	writer.flush();
	response.flushBuffer();

	return null;
    }

    private String convertBoolean(Boolean bool) {
	if (bool == null) {
	    return "";
	}
	return bool ? "X" : "";
    }

    private List<Object> getSheetHeader() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final List<Object> result = new ArrayList<Object>();

	result.add("Docente");
	result.add("Semestre");
	result.add("Curso");
	result.add(bundle.getString("label.tutorshipSummary.form.relationsSize"));
	result.add(bundle.getString("label.tutorshipSummary.form.howManyReunionsGroup"));
	result.add(bundle.getString("label.tutorshipSummary.form.howManyReunionsIndividual"));
	result.add(bundle.getString("label.tutorshipSummary.form.howManyContactsPhone"));
	result.add(bundle.getString("label.tutorshipSummary.form.howManyContactsEmail"));

	result.add("Problemas:");
	result.add("Horários/Inscrições");
	result.add("Métodos de Estudo");
	result.add("Gestão de Tempo/Volume de Trabalho");
	result.add("Acesso a Informação (ex.:aspectos administrativos; ERASMUS; etc.)");
	result.add("Transição Ensino Secundário/Ensino Superior");
	result.add("Problemas Vocacionais");
	result.add("Relação Professor - Aluno");
	result.add("Desempenho Académico (ex.: taxas de aprovação)");
	result.add("Avaliação (ex.: metodologia, datas de exames; etc.)");
	result.add("Adaptação ao IST");
	result.add("Outro");

	result.add("Ganhos:");
	result.add("Maior responsabilização/autonomização do Aluno");
	result.add("Alteração dos métodos de estudo");
	result.add("Planeamento do semestre/Avaliação");
	result.add("Acompanhamento mais individualizado");
	result.add("Maior motivação para o curso");
	result.add("Melhor desempenho académico");
	result.add("Maior proximidade Professor-Aluno");
	result.add("Transição do Ensino Secundário para o Ensino Superior mais fácil");
	result.add("Melhor adaptação ao IST");
	result.add("Apoio na tomada de decisões/Resolução de problemas");
	result.add("Outro");

	result.add("Apreciação Global");
	result.add("Dificuldades");
	result.add("Ganhos");
	result.add("Sugestões");

	return result;
    }

    private Spreadsheet generateGeneralSheet(List<TutorshipSummary> summaries) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final ResourceBundle bundleEnum = ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale());
	final Spreadsheet spreadsheet = new Spreadsheet("Fichas do Tutor (geral)", getSheetHeader());

	for (TutorshipSummary summary : summaries) {
	    final Row row = spreadsheet.addRow();

	    row.setCell(summary.getTeacher().getPerson().getName());
	    row.setCell(summary.getSemester().getSemester() + " - " + summary.getSemester().getExecutionYear().getYear());
	    row.setCell(summary.getDegree().getSigla());
	    row.setCell(summary.getTutorshipSummaryRelationsCount());
	    row.setCell(summary.getHowManyReunionsGroup());
	    row.setCell(summary.getHowManyReunionsIndividual());
	    row.setCell(summary.getHowManyContactsPhone());
	    row.setCell(summary.getHowManyContactsEmail());

	    row.setCell("");
	    row.setCell(convertBoolean(summary.getProblemsR1()));
	    row.setCell(convertBoolean(summary.getProblemsR2()));
	    row.setCell(convertBoolean(summary.getProblemsR3()));
	    row.setCell(convertBoolean(summary.getProblemsR4()));
	    row.setCell(convertBoolean(summary.getProblemsR5()));
	    row.setCell(convertBoolean(summary.getProblemsR6()));
	    row.setCell(convertBoolean(summary.getProblemsR7()));
	    row.setCell(convertBoolean(summary.getProblemsR8()));
	    row.setCell(convertBoolean(summary.getProblemsR9()));
	    row.setCell(convertBoolean(summary.getProblemsR10()));
	    row.setCell(summary.getProblemsOther());

	    row.setCell("");
	    row.setCell(convertBoolean(summary.getGainsR1()));
	    row.setCell(convertBoolean(summary.getGainsR2()));
	    row.setCell(convertBoolean(summary.getGainsR3()));
	    row.setCell(convertBoolean(summary.getGainsR4()));
	    row.setCell(convertBoolean(summary.getGainsR5()));
	    row.setCell(convertBoolean(summary.getGainsR6()));
	    row.setCell(convertBoolean(summary.getGainsR7()));
	    row.setCell(convertBoolean(summary.getGainsR8()));
	    row.setCell(convertBoolean(summary.getGainsR9()));
	    row.setCell(convertBoolean(summary.getGainsR10()));
	    row.setCell(summary.getGainsOther());

	    if (summary.getTutorshipSummaryProgramAssessment() != null) {
		row.setCell(bundleEnum.getString(summary.getTutorshipSummaryProgramAssessment().getName()));
	    } else {
		row.setCell("");
	    }

	    row.setCell(summary.getDifficulties());
	    row.setCell(summary.getGains());
	    row.setCell(summary.getSuggestions());
	}

	return spreadsheet;
    }

    private List<Object> getSummaryHeader() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final List<Object> result = new ArrayList<Object>();

	result.add("Docente");
	result.add("Semestre");
	result.add("Curso");
	result.add("Aluno");
	result.add(bundle.getString("label.tutorshipSummary.form.withoutEnrolments"));
	result.add(bundle.getString("label.tutorshipSummary.form.participationType"));
	result.add(bundle.getString("label.tutorshipSummary.form.participationRegularly"));
	result.add(bundle.getString("label.tutorshipSummary.form.participationNone"));
	result.add(bundle.getString("label.tutorshipSummary.form.outOfTouch"));
	result.add(bundle.getString("label.tutorshipSummary.form.highPerformance"));
	result.add(bundle.getString("label.tutorshipSummary.form.lowPerformance"));

	return result;
    }

    private Spreadsheet generateDetailedSheet(List<TutorshipSummary> summaries) {

	final ResourceBundle bundleEnum = ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale());

	final Spreadsheet spreadsheet = new Spreadsheet("Fichas do Tutor (tutorandos)", getSummaryHeader());

	for (TutorshipSummary summary : summaries) {
	    for (TutorshipSummaryRelation relation : summary.getTutorshipSummaryRelations()) {
		final Row relationRow = spreadsheet.addRow();

		relationRow.setCell(summary.getTeacher().getPerson().getName());
		relationRow.setCell(summary.getSemester().getSemester() + " - "
			+ summary.getSemester().getExecutionYear().getYear());
		relationRow.setCell(summary.getDegree().getSigla());
		relationRow.setCell(relation.getTutorship().getStudent().getName() + "("
			+ relation.getTutorship().getStudent().getNumber() + ")");

		relationRow.setCell(convertBoolean(relation.getWithoutEnrolments()));
		if (relation.getParticipationType() == null) {
		    relationRow.setCell("");
		} else {
		    relationRow.setCell(bundleEnum.getString(relation.getParticipationType().getName()));
		}
		relationRow.setCell(convertBoolean(relation.getParticipationRegularly()));
		relationRow.setCell(convertBoolean(relation.getParticipationNone()));
		relationRow.setCell(convertBoolean(relation.getOutOfTouch()));
		relationRow.setCell(convertBoolean(relation.getHighPerformance()));
		relationRow.setCell(convertBoolean(relation.getLowPerformance()));
	    }
	}

	return spreadsheet;
    }

    public ActionForward createSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	CreateSummaryBean bean = (CreateSummaryBean) getRenderedObject("createSummaryBean");

	if (bean == null) {

	    bean = getCreateSummaryBean(request);

	    if (bean == null) {
		return searchTeacher(mapping, actionForm, request, response);
	    }

	}

	request.setAttribute("createSummaryBean", bean);

	return mapping.findForward("createSummary");
    }

    protected CreateSummaryBean getCreateSummaryBean(HttpServletRequest request) {
	CreateSummaryBean bean = null;

	String summaryId = (String) getFromRequest(request, "summaryId");

	if (summaryId != null) {
	    TutorshipSummary tutorshipSummary = AbstractDomainObject.fromExternalId(summaryId);

	    if (tutorshipSummary != null) {
		bean = new EditSummaryBean(tutorshipSummary);
	    }
	} else {
	    String teacherId = (String) getFromRequest(request, "teacherId");
	    String degreeId = (String) getFromRequest(request, "degreeId");
	    String semesterId = (String) getFromRequest(request, "semesterId");

	    Teacher teacher = AbstractDomainObject.fromExternalId(teacherId);
	    Degree degree = AbstractDomainObject.fromExternalId(degreeId);
	    ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(semesterId);

	    if (teacher != null && degree != null && executionSemester != null) {
		bean = new CreateSummaryBean(teacher, executionSemester, degree);
	    }
	}

	return bean;
    }

    public ActionForward processCreateSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	CreateSummaryBean bean = (CreateSummaryBean) getRenderedObject("createSummaryBean");

	if (bean == null) {
	    return createSummary(mapping, actionForm, request, response);
	}

	request.setAttribute("createSummaryBean", bean);

	if (getFromRequest(request, "confirm") != null) {
	    bean.save();

	    return mapping.findForward("confirmCreateSummary");
	}

	return mapping.findForward("processCreateSummary");
    }

    public ActionForward viewSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String summaryId = (String) getFromRequest(request, "summaryId");
	TutorshipSummary tutorshipSummary = AbstractDomainObject.fromExternalId(summaryId);

	request.setAttribute("tutorshipSummary", tutorshipSummary);

	return mapping.findForward("viewSummary");
    }
}
