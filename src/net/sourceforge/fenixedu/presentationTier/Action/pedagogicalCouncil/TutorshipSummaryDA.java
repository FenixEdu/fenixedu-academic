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
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

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
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("tutorateBean", bean);
	return mapping.findForward("searchTeacher");
    }

    public ActionForward exportSummaries(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final ResourceBundle bundleEnum = ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale());

	TutorSummaryBean bean = (TutorSummaryBean) getRenderedObject("tutorateBean");

	if (bean == null) {
	    return searchTeacher(mapping, actionForm, request, response);
	}

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=test.xls");

	final ServletOutputStream writer = response.getOutputStream();

	SheetData<TutorshipSummary> generalSheet = new SheetData<TutorshipSummary>(bean.getPastSummaries()) {

	    @Override
	    protected void makeLine(TutorshipSummary summary) {
		addCell("Docente", summary.getTeacher().getPerson().getName());
		addCell("Semestre", summary.getSemester().getSemester() + " - "
			+ summary.getSemester().getExecutionYear().getYear());
		addCell("Curso", summary.getDegree().getSigla());
		addCell(bundle.getString("label.tutorshipSummary.form.relationsSize"),
			summary.getTutorshipSummaryRelationsCount());
		addCell(bundle.getString("label.tutorshipSummary.form.howManyReunionsGroup"), summary.getHowManyReunionsGroup());
		addCell(bundle.getString("label.tutorshipSummary.form.howManyReunionsIndividual"),
			summary.getHowManyReunionsIndividual());
		addCell(bundle.getString("label.tutorshipSummary.form.howManyContactsPhone"), summary.getHowManyContactsPhone());
		addCell(bundle.getString("label.tutorshipSummary.form.howManyContactsEmail"), summary.getHowManyContactsEmail());

		addCell("Problemas:", "");
		addCell("Horários/Inscrições", convertBoolean(summary.getProblemsR1()));
		addCell("Métodos de Estudo", convertBoolean(summary.getProblemsR2()));
		addCell("Gestão de Tempo/Volume de Trabalho", convertBoolean(summary.getProblemsR3()));
		addCell("Acesso a Informação (ex.:aspectos administrativos; ERASMUS; etc.)",
			convertBoolean(summary.getProblemsR4()));
		addCell("Transição Ensino Secundário/Ensino Superior", convertBoolean(summary.getProblemsR5()));
		addCell("Problemas Vocacionais", convertBoolean(summary.getProblemsR6()));
		addCell("Relação Professor - Aluno", convertBoolean(summary.getProblemsR7()));
		addCell("Desempenho Académico (ex.: taxas de aprovação)", convertBoolean(summary.getProblemsR8()));
		addCell("Avaliação (ex.: metodologia, datas de exames; etc.)", convertBoolean(summary.getProblemsR9()));
		addCell("Adaptação ao IST", convertBoolean(summary.getProblemsR10()));
		addCell("Outro", summary.getProblemsOther());

		addCell("Ganhos:", "");
		addCell("Maior responsabilização/autonomização do Aluno", convertBoolean(summary.getGainsR1()));
		addCell("Alteração dos métodos de estudo", convertBoolean(summary.getGainsR2()));
		addCell("Planeamento do semestre/Avaliação", convertBoolean(summary.getGainsR3()));
		addCell("Acompanhamento mais individualizado", convertBoolean(summary.getGainsR4()));
		addCell("Maior motivação para o curso", convertBoolean(summary.getGainsR5()));
		addCell("Melhor desempenho académico", convertBoolean(summary.getGainsR6()));
		addCell("Maior proximidade Professor-Aluno", convertBoolean(summary.getGainsR7()));
		addCell("Transição do Ensino Secundário para o Ensino Superior mais fácil", convertBoolean(summary.getGainsR8()));
		addCell("Melhor adaptação ao IST", convertBoolean(summary.getGainsR9()));
		addCell("Apoio na tomada de decisões/Resolução de problemas", convertBoolean(summary.getGainsR10()));
		addCell("Outro", summary.getGainsOther());

		if (summary.getTutorshipSummaryProgramAssessment() != null) {
		    addCell("Apreciação Global", bundleEnum.getString(summary.getTutorshipSummaryProgramAssessment().getName()));
		} else {
		    addCell("Apreciação Global", "");
		}
		addCell("Dificuldades", summary.getDifficulties());
		addCell("Ganhos", summary.getGains());
		addCell("Sugestões", summary.getSuggestions());
	    }
	};

	List<Pair<TutorshipSummary, TutorshipSummaryRelation>> relations = new ArrayList<Pair<TutorshipSummary, TutorshipSummaryRelation>>();
	for (TutorshipSummary summary : bean.getPastSummaries()) {
	    for (TutorshipSummaryRelation relation : summary.getTutorshipSummaryRelationsSet()) {
		relations.add(new Pair<TutorshipSummary, TutorshipSummaryRelation>(summary, relation));
	    }
	}
	SheetData<Pair<TutorshipSummary, TutorshipSummaryRelation>> detailedSheet = new SheetData<Pair<TutorshipSummary, TutorshipSummaryRelation>>(
		relations) {
	    @Override
	    protected void makeLine(Pair<TutorshipSummary, TutorshipSummaryRelation> line) {
		addCell("Docente", line.getKey().getTeacher().getPerson().getName());
		addCell("Semestre", line.getKey().getSemester().getSemester() + " - "
			+ line.getKey().getSemester().getExecutionYear().getYear());
		addCell("Curso", line.getKey().getDegree().getSigla());
		addCell("Aluno", line.getValue().getTutorship().getStudent().getName() + "("
			+ line.getValue().getTutorship().getStudent().getNumber() + ")");
		addCell(bundle.getString("label.tutorshipSummary.form.withoutEnrolments"), convertBoolean(line.getValue()
			.getWithoutEnrolments()));
		if (line.getValue().getParticipationType() == null) {
		    addCell(bundle.getString("label.tutorshipSummary.form.participationType"), "");
		} else {
		    addCell(bundle.getString("label.tutorshipSummary.form.participationType"),
			    bundleEnum.getString(line.getValue().getParticipationType().getName()));
		}
		addCell(bundle.getString("label.tutorshipSummary.form.participationRegularly"), convertBoolean(line.getValue()
			.getParticipationRegularly()));
		addCell(bundle.getString("label.tutorshipSummary.form.participationNone"), convertBoolean(line.getValue()
			.getParticipationNone()));
		addCell(bundle.getString("label.tutorshipSummary.form.outOfTouch"), convertBoolean(line.getValue()
			.getOutOfTouch()));
		addCell(bundle.getString("label.tutorshipSummary.form.highPerformance"), convertBoolean(line.getValue()
			.getHighPerformance()));
		addCell(bundle.getString("label.tutorshipSummary.form.lowPerformance"), convertBoolean(line.getValue()
			.getLowPerformance()));
	    }
	};

	new SpreadsheetBuilder().addSheet("Fichas do Tutor (geral)", generalSheet)
		.addSheet("Fichas do Tutor (tutorandos)", detailedSheet).build(WorkbookExportFormat.EXCEL, writer);

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
