package net.sourceforge.fenixedu.presentationTier.Action.gep.a3es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.core.MediaType;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.LecturedCurricularUnit;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.QualificationBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class A3ESDegreeProcess implements Serializable {
    private static final String BASE_URL = "http://formacao.a3es.pt/iportal.php";

    private static final String API_PROCESS = "api_process";

    private static final String API_FORM = "api_form";

    private static final String API_BLOCK = "api_block";

    private static final String API_FILE = "api_file";

    private static final String API_FOLDER = "api_folder";

    private static final String API_ANNEX = "api_annex";

    private static Map<String, String> acefIndex = new HashMap<String, String>();

    static {
	// This being hard-coded prevents future executions without our
	// interference, this should be persisted and managed by UI.

	acefIndex.put("ACEF/1213/06697", "LEE");
	acefIndex.put("ACEF/1213/06707", "LEIC-A");
	acefIndex.put("ACEF/1213/06712", "LEIC-T");
	acefIndex.put("ACEF/1213/12622", "LERC");
	acefIndex.put("ACEF/1213/06752", "MEE");
	acefIndex.put("ACEF/1213/06762", "MEIC-A");
	acefIndex.put("ACEF/1213/06767", "MEIC-T");
	acefIndex.put("ACEF/1213/06772", "MERC");
	acefIndex.put("ACEF/1213/06797", "MUOT");
	acefIndex.put("ACEF/1213/06832", "MEEC");
	acefIndex.put("ACEF/1213/06852", "MA");
	acefIndex.put("ACEF/1213/06862", "DArq");
	acefIndex.put("ACEF/1213/06872", "DEEC");
	acefIndex.put("ACEF/1213/06882", "DEIC");
	acefIndex.put("ACEF/1213/06917", "DMte");
	acefIndex.put("ACEF/1213/06927", "DEASegInf");
	acefIndex.put("ACEF/1213/06967", "DEAEngCmp");
	acefIndex.put("ACEF/1213/06987", "DET");
    }

    protected String user;

    protected String password;

    protected String base64Hash;

    protected String id;

    protected Degree degree;

    protected ExecutionSemester executionSemester;

    protected String formId;

    public A3ESDegreeProcess() {
	super();
	this.user = "pep33461";
	this.password = "ydata7us";
	this.executionSemester = ExecutionSemester.readActualExecutionSemester();
    }

    public void initialize() {
	base64Hash = new String(Base64.encodeBase64((user + ":" + password).getBytes()));
	JSONArray processes = invoke(webResource().path(API_PROCESS));
	JSONObject json = (JSONObject) processes.get(0);
	id = (String) json.get("id");
	String name = (String) json.get("name");
	if (acefIndex.containsKey(name)) {
	    degree = Degree.readBySigla(acefIndex.get(name));
	    JSONArray forms = invoke(webResource().path(API_FORM).queryParam("processId", id));
	    for (Object object : forms) {
		JSONObject form = (JSONObject) object;
		if ("Guião para a auto-avaliação".equals(form.get("name"))) {
		    formId = (String) form.get("id");
		}
	    }
	    if (formId == null) {
		throw new DomainException("Process " + name + " has no auto-evaluation form.");
	    }
	} else {
	    throw new DomainException("Not recognized ACEF code: " + name);
	}
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Degree getDegree() {
	return degree;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public List<ExecutionSemester> getAvailableExecutionSemesters() {
	List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	ExecutionSemester readActualExecutionSemester = ExecutionSemester.readActualExecutionSemester();
	result.add(readActualExecutionSemester);
	result.add(readActualExecutionSemester.getPreviousExecutionPeriod());
	return result;
    }

    public List<ExecutionSemester> getSelectedExecutionSemesters() {
	List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	if (getExecutionSemester() != null) {
	    result.add(getExecutionSemester());
	    result.add(getExecutionSemester().getPreviousExecutionPeriod());
	}
	return result;
    }

    private Set<ExecutionYear> getSelectedExecutionYears() {
	Set<ExecutionYear> result = new HashSet<ExecutionYear>();
	if (getExecutionSemester() != null) {
	    result.add(getExecutionSemester().getExecutionYear());
	    result.add(getExecutionSemester().getPreviousExecutionPeriod().getExecutionYear());
	}
	return result;
    }

    public List<String> uploadCompetenceCourses() {
	List<String> output = new ArrayList<String>();
	for (Object object : invoke(webResource().path(API_FOLDER).queryParam("formId", formId))) {
	    JSONObject folder = (JSONObject) object;
	    if ("6.2.1. Ficha das unidades curriculares".equals(folder.get("name"))) {
		String competencesId = (String) folder.get("id");
		for (JSONObject json : buildCompetenceCoursesJson()) {
		    ClientResponse response = post(
			    webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
			    json.toJSONString());
		    int status = response.getStatus();
		    if (status == 201) {
			output.add("201 Created: " + json.get("q-6.2.1.1"));
		    } else {
			output.add(status + " : " + json.get("q-6.2.1.1") + " : " + response.getEntity(String.class));
		    }
		    break;
		}
		break;
	    }
	}
	return output;
    }

    public List<String> uploadTeacherCurriculum() {
	List<String> output = new ArrayList<String>();
	for (Object object : invoke(webResource().path(API_FOLDER).queryParam("formId", formId))) {
	    JSONObject folder = (JSONObject) object;
	    if ("4.1.1. Fichas curriculares".equals(folder.get("name"))) {
		String teacherCurriculumId = (String) folder.get("id");
		JSONArray annexes = invoke(webResource().path(API_ANNEX).queryParam("formId", formId)
			.queryParam("folderId", teacherCurriculumId));
		// TODO: delete
		for (String json : buildTeacherCurriculumJson()) {
		    post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", teacherCurriculumId),
			    json);
		}
		break;
	    }
	}
	return output;
    }

    protected WebResource webResource() {
	Client client = Client.create();
	// client.addFilter(new LoggingFilter(System.out));
	return client.resource(BASE_URL);
    }

    protected JSONArray invoke(WebResource resource) {
	return (JSONArray) ((JSONObject) JSONValue.parse(resource.header("Authorization", "Basic " + base64Hash)
		.get(String.class))).get("list");
    }

    protected ClientResponse post(WebResource resource, String arg) {
	return resource.header("Authorization", "Basic " + base64Hash).type(MediaType.APPLICATION_JSON_TYPE)
		.post(ClientResponse.class, arg);
    }

    protected List<JSONObject> buildCompetenceCoursesJson() {
	List<JSONObject> jsons = new ArrayList<JSONObject>();
	RootCourseGroup root = degree.getLastActiveDegreeCurricularPlan().getRoot();
	for (CurricularCourse course : root.getAllCurricularCourses(executionSemester)) {
	    JSONObject json = new JSONObject();
	    CompetenceCourse competence = course.getCompetenceCourse();
	    json.put("q-6.2.1.1", competence.getName(executionSemester));

	    json.put("q-6.2.1.2", getTeachersAndTeachingHours(course, executionSemester, true));

	    JSONObject q6213 = new JSONObject();
	    String teachersAndTeachingHours = getTeachersAndTeachingHours(course, executionSemester, false);
	    q6213.put("en", teachersAndTeachingHours);
	    q6213.put("pt", teachersAndTeachingHours);
	    json.put("q-6.2.1.3", q6213);

	    JSONObject q6214 = new JSONObject();
	    q6214.put("en", competence.getObjectivesI18N(executionSemester).getContent(Language.en));
	    q6214.put("pt", competence.getObjectivesI18N(executionSemester).getContent(Language.pt));
	    json.put("q-6.2.1.4", q6214);

	    JSONObject q6215 = new JSONObject();
	    q6215.put("en", competence.getProgramI18N(executionSemester).getContent(Language.en));
	    q6215.put("pt", competence.getProgramI18N(executionSemester).getContent(Language.pt));
	    json.put("q-6.2.1.5", q6215);

	    JSONObject q6216 = new JSONObject();
	    q6216.put("en", "coherence");
	    q6216.put("pt", "coerencia");
	    json.put("q-6.2.1.6", q6216);

	    JSONObject q6217 = new JSONObject();
	    q6217.put("en", competence.getEvaluationMethodEn(executionSemester));
	    q6217.put("pt", competence.getEvaluationMethod(executionSemester));
	    json.put("q-6.2.1.7", q6217);

	    JSONObject q6218 = new JSONObject();
	    q6218.put("en", "coherence");
	    q6218.put("pt", "coerencia");
	    json.put("q-6.2.1.8", q6218);

	    List<String> references = new ArrayList<String>();
	    for (BibliographicReference reference : competence.getBibliographicReferences(executionSemester)
		    .getMainBibliographicReferences()) {
		references.add(reference.getReference());
	    }
	    json.put("q-6.2.1.9", StringUtils.join(references, "; "));
	    jsons.add(json);
	}
	return jsons;
    }

    private String getTeachersAndTeachingHours(CurricularCourse course, ExecutionSemester executionSemester,
	    boolean responsibleTeacher) {
	Map<Teacher, Double> responsiblesMap = new HashMap<Teacher, Double>();
	for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
	    if (executionSemester == executionCourse.getExecutionPeriod()) {
		for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
		    if (professorhip.isResponsibleFor() == responsibleTeacher
			    && professorhip.getPerson().getTeacher().isActiveOrHasAuthorizationForSemester(executionSemester)) {
			Double hours = responsiblesMap.get(professorhip.getTeacher());
			if (hours == null) {
			    hours = 0.0;
			}
			hours = hours + getHours(professorhip);
			responsiblesMap.put(professorhip.getTeacher(), hours);
		    }
		}
	    }
	}
	List<String> responsibles = new ArrayList<String>();
	for (Teacher teacher : responsiblesMap.keySet()) {
	    responsibles.add(teacher.getPerson().getName() + " (" + responsiblesMap.get(teacher) + ")");
	}
	return StringUtils.join(responsibles, ", ");
    }

    private Double getHours(Professorship professorhip) {
	Teacher teacher = professorhip.getPerson().getTeacher();
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(professorhip.getExecutionCourse()
		.getExecutionPeriod());
	Double result = 0.0;
	if (teacherService != null) {
	    for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {
		if (degreeTeachingService.getProfessorship().getExecutionCourse().equals(professorhip.getExecutionCourse())) {
		    double hours = degreeTeachingService.getShift().getUnitHours().doubleValue();
		    result = result + (hours * (degreeTeachingService.getPercentage().doubleValue() / 100));
		}
	    }
	}
	return result;
    }

    protected List<TeacherCurricularInformation> getTeacherCurricularInformation() {
	Set<Teacher> teachers = new HashSet<Teacher>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (isInScope(course, executionCourse)) {
			for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
			    if (professorhip.getPerson().getTeacher().isActiveOrHasAuthorizationForSemester(executionSemester)) {
				teachers.add(professorhip.getPerson().getTeacher());
			    }
			}
			if (executionCourse.isDissertation()) {
			    for (Attends attends : executionCourse.getAttends()) {
				if (attends.hasEnrolment() || attends.getEnrolment().getThesis() != null) {
				    for (ThesisEvaluationParticipant thesisEvaluationParticipant : attends.getEnrolment()
					    .getThesis().getOrientation()) {
					if (thesisEvaluationParticipant.getPerson().getTeacher() != null
						&& thesisEvaluationParticipant.getPerson().getTeacher()
							.isActiveOrHasAuthorizationForSemester(executionSemester)) {
					    teachers.add(thesisEvaluationParticipant.getPerson().getTeacher());
					}
				    }
				}
			    }
			}
		    }
		}
	    }
	}

	List<ExecutionSemester> executionSemesters = getSelectedExecutionSemesters();
	PhdProgram phdProgram = degree.getPhdProgram();
	if (phdProgram != null) {
	    for (PhdIndividualProgramProcess phdIndividualProgramProcess : phdProgram.getIndividualProgramProcesses()) {
		for (ExecutionSemester executionSemester : executionSemesters) {
		    if (phdIndividualProgramProcess.isActive(executionSemester.getAcademicInterval().toInterval())) {
			for (PhdParticipant phdParticipant : phdIndividualProgramProcess.getParticipants()) {
			    if (phdParticipant instanceof InternalPhdParticipant) {
				InternalPhdParticipant internalPhdParticipant = (InternalPhdParticipant) phdParticipant;
				if (internalPhdParticipant.isGuidingOrAssistantGuiding()
					&& internalPhdParticipant.getPerson().getTeacher() != null
					&& internalPhdParticipant.getPerson().getTeacher()
						.isActiveOrHasAuthorizationForSemester(executionSemester)) {
				    teachers.add(internalPhdParticipant.getPerson().getTeacher());
				}
			    }
			}
		    }
		}
	    }
	}
	List<TeacherCurricularInformation> teacherCurricularInformationList = new ArrayList<TeacherCurricularInformation>();
	for (Teacher teacher : teachers) {
	    TeacherCurricularInformation teacherCurricularInformation = new TeacherCurricularInformation(teacher, degree,
		    executionSemesters);
	    teacherCurricularInformationList.add(teacherCurricularInformation);
	}
	return teacherCurricularInformationList;
    }

    private boolean isInScope(CurricularCourse course, ExecutionCourse executionCourse) {
	List<ExecutionSemester> selectedExecutionSemesters = getSelectedExecutionSemesters();
	Set<ExecutionYear> selectedExecutionYears = getSelectedExecutionYears();
	return selectedExecutionSemesters.contains(executionCourse.getExecutionPeriod())
		|| (course.isAnual() && selectedExecutionYears.contains(executionCourse.getExecutionPeriod().getExecutionYear()));
    }

    protected List<String> buildTeacherCurriculumJson() {
	List<String> jsons = new ArrayList<String>();
	for (TeacherCurricularInformation info : getTeacherCurricularInformation()) {
	    JSONObject toplevel = new JSONObject();

	    toplevel.put("q-cf-name", info.getTeacher().getPerson().getName());
	    toplevel.put("q-cf-ies", RootDomainObject.getInstance().getInstitutionUnit().getName());
	    toplevel.put("q-cf-time", Long.toString(System.currentTimeMillis()));
	    toplevel.put("q-cf-uo", info.getProfessionalCategoryName());
	    JSONObject file = new JSONObject();
	    {
		file.put("name", info.getTeacher().getPerson().getName());
		file.put("ies", RootDomainObject.getInstance().getInstitutionUnit().getName());
		file.put("uo", info.getUnitName());
		file.put("cat", info.getProfessionalCategoryName());
		QualificationBean qualification = info.getCurrentQualification();
		file.put("deg", StringUtils.defaultIfEmpty(qualification != null ? qualification.getDegree() : null, "0"));
		file.put("degarea",
			StringUtils.defaultIfEmpty(qualification != null ? qualification.getScientificArea() : null, "0"));
		file.put("ano_grau", StringUtils.defaultIfEmpty(qualification != null ? qualification.getYear() : null, "0"));
		file.put("instituicao_conferente",
			StringUtils.defaultIfEmpty(qualification != null ? qualification.getInstitution() : null, "0"));
		file.put("regime", info.getProfessionalRegimeName());

		if (!info.getOtherQualifications().isEmpty()) {
		    JSONArray academicArray = new JSONArray();
		    SortedSet<QualificationBean> otherQualifications = info.getOtherQualifications();
		    for (QualificationBean otherQualification : otherQualifications) {
			JSONObject academic = new JSONObject();
			academic.put("year", otherQualification.getYear());
			academic.put("degree", otherQualification.getDegree());
			academic.put("area", otherQualification.getScientificArea());
			academic.put("ies", otherQualification.getInstitution());
			academic.put("rank", StringUtils.defaultIfEmpty(otherQualification.getClassification(), "-"));
			academicArray.add(academic);
		    }
		    file.put("form-academic", academicArray);
		}

		if (!info.getTop5ResultParticipation().isEmpty()) {
		    JSONArray researchArray = new JSONArray();
		    for (String publication : info.getTop5ResultParticipation()) {
			JSONObject research = new JSONObject();
			research.put("investigation", "pois é");// publication);
			researchArray.add(research);
		    }
		    file.put("form-investigation", researchArray);
		}

		if (!info.getTop5ProfessionalCareer().isEmpty()) {
		    JSONArray professionalArray = new JSONArray();
		    for (String profession : info.getTop5ProfessionalCareer()) {
			JSONObject pro = new JSONObject();
			pro.put("profession", profession);
			professionalArray.add(pro);
		    }
		    file.put("form-professional", professionalArray);
		}

		JSONArray insideLectures = new JSONArray();
		for (LecturedCurricularUnit lecturedCurricularUnit : info.getLecturedUCsOnCycle()) {
		    JSONObject lecture = new JSONObject();
		    lecture.put("curricularUnit", lecturedCurricularUnit.getName());
		    lecture.put("studyCyle", lecturedCurricularUnit.getName());
		    lecture.put("type", lecturedCurricularUnit.getShiftType());
		    lecture.put("hoursPerWeek", lecturedCurricularUnit.getHours());
		    insideLectures.add(lecture);
		}
		file.put("form-unit", insideLectures);
		//
		// JSONArray outsideLectures = new JSONArray();
		// for (LecturedCurricularUnit lecturedCurricularUnit :
		// info.getLecturedUCsOnOtherCycles()) {
		// JSONObject lecture = new JSONObject();
		// lecture.put("curricularUnit",
		// StringUtils.defaultIfEmpty(lecturedCurricularUnit.getName(),
		// "0"));
		// lecture.put("type",
		// StringUtils.defaultIfEmpty(lecturedCurricularUnit.getShiftType(),
		// "0"));
		// lecture.put("hoursPerWeek",
		// StringUtils.defaultIfEmpty(lecturedCurricularUnit.getHours(),
		// "0"));
		// outsideLectures.add(lecture);
		// }
		// file.put("form-unit", outsideLectures);
	    }
	    toplevel.put("q-cf-cfile", file);

	    jsons.add(toplevel.toJSONString());
	}
	return jsons;
    }

    public SpreadsheetBuilder exportTeacherCurriculum() {
	SheetData<TeacherCurricularInformation> sheet = new SheetData<TeacherCurricularInformation>(
		getTeacherCurricularInformation()) {
	    @Override
	    protected void makeLine(TeacherCurricularInformation teacherCurricularInformation) {
		addCell("IstId", teacherCurricularInformation.getTeacher().getPerson().getUsername());
		addCell("Nome", teacherCurricularInformation.getTeacher().getPerson().getName());
		addCell("Instituição", "Instituto Superior Técnico");
		addCell("Unidade Orgânica", teacherCurricularInformation.getUnitName());
		addCell("Categoria", teacherCurricularInformation.getProfessionalCategoryName());
		QualificationBean qualification = teacherCurricularInformation.getCurrentQualification();
		addCell("Grau", qualification != null ? qualification.getDegree() : null);
		addCell("Área científica", qualification != null ? qualification.getScientificArea() : null);
		addCell("Ano", qualification != null ? qualification.getYear() : null);
		addCell("Instituição", qualification != null ? qualification.getInstitution() : null);
		addCell("Regime", teacherCurricularInformation.getProfessionalRegimeName());
		SortedSet<QualificationBean> otherQualifications = teacherCurricularInformation.getOtherQualifications();
		List<String> otherQualificationStrings = new ArrayList<String>();
		for (QualificationBean otherQualification : otherQualifications) {
		    StringBuilder qualificationString = new StringBuilder();
		    qualificationString.append(otherQualification.getYear()).append(",");
		    qualificationString.append(otherQualification.getDegree()).append(",");
		    qualificationString.append(otherQualification.getScientificArea()).append(",");
		    qualificationString.append(otherQualification.getInstitution()).append(",");
		    qualificationString.append(otherQualification.getClassification());
		    otherQualificationStrings.add(qualificationString.toString());
		}
		addCell("Outras Qualificações", StringUtils.join(otherQualificationStrings, "\n"));
		addCell("Publicações", StringUtils.join(teacherCurricularInformation.getTop5ResultParticipation(), "\n"));
		addCell("Experiência Profissional",
			StringUtils.join(teacherCurricularInformation.getTop5ProfessionalCareer(), "\n"));

		List<String> lectured = new ArrayList<String>();
		for (LecturedCurricularUnit lecturedCurricularUnit : teacherCurricularInformation.getLecturedUCsOnCycle()) {
		    StringBuilder lecturedString = new StringBuilder();
		    lecturedString.append(lecturedCurricularUnit.getName()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getShiftType()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getHours());
		    lectured.add(lecturedString.toString());
		}
		addCell("UCs no ciclo proposto", StringUtils.join(lectured, "\n"));

		lectured = new ArrayList<String>();
		for (LecturedCurricularUnit lecturedCurricularUnit : teacherCurricularInformation.getLecturedUCsOnOtherCycles()) {
		    StringBuilder lecturedString = new StringBuilder();
		    lecturedString.append(lecturedCurricularUnit.getName()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getShiftType()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getHours());
		    lectured.add(lecturedString.toString());
		}
		addCell("UCs outros ciclos", StringUtils.join(lectured, "\n"));
	    }
	};
	return new SpreadsheetBuilder().addSheet(degree.getSigla(), sheet);
    }
}