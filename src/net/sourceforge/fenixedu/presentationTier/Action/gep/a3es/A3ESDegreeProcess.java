package net.sourceforge.fenixedu.presentationTier.Action.gep.a3es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.LecturedCurricularUnit;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.TeacherCurricularInformation.QualificationBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
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
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class A3ESDegreeProcess implements Serializable {
    private static final String BASE_URL = "http://www.a3es.pt/si/iportal.php";

    private static final String API_PROCESS = "api_process";

    private static final String API_FORM = "api_form";

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
		for (Object annexObj : invoke(webResource().path(API_ANNEX).queryParam("formId", formId)
			.queryParam("folderId", competencesId))) {
		    JSONObject annex = (JSONObject) annexObj;
		    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
			    .queryParam("folderId", competencesId));
		}
		for (Entry<JSONObject, String> json : buildCompetenceCoursesJson().entrySet()) {
		    ClientResponse response = post(
			    webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
			    json.getKey().toJSONString());
		    int status = response.getStatus();
		    if (status == 201) {
			output.add("201 Created: " + json.getKey().get("q-6.2.1.1") + ": " + json.getValue());
		    } else {
			output.add(status + ": " + json.getKey().get("q-6.2.1.1") + ": " + response.getEntity(String.class)
				+ " input: " + json.getKey().toJSONString());
		    }
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
		for (Object annexObj : invoke(webResource().path(API_ANNEX).queryParam("formId", formId)
			.queryParam("folderId", teacherCurriculumId))) {
		    JSONObject annex = (JSONObject) annexObj;
		    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
			    .queryParam("folderId", teacherCurriculumId));
		}
		for (Entry<JSONObject, String> json : buildTeacherCurriculumJson().entrySet()) {
		    ClientResponse response = post(
			    webResource().path(API_ANNEX).queryParam("formId", formId)
				    .queryParam("folderId", teacherCurriculumId), json.getKey().toJSONString());
		    int status = response.getStatus();
		    if (status == 201) {
			output.add("201 Created: " + json.getKey().get("q-cf-name") + ": " + json.getValue());
		    } else {
			output.add(status + ": " + json.getKey().get("q-cf-name") + ": " + response.getEntity(String.class)
				+ " input: " + json.getKey().toJSONString());
		    }
		}
		break;
	    }
	}
	return output;
    }

    protected WebResource webResource() {
	Client client = Client.create();
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

    protected ClientResponse delete(WebResource resource) {
	return resource.header("Authorization", "Basic " + base64Hash).type(MediaType.APPLICATION_JSON_TYPE)
		.delete(ClientResponse.class);
    }

    protected Map<JSONObject, String> buildCompetenceCoursesJson() {
	Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();
	RootCourseGroup root = degree.getLastActiveDegreeCurricularPlan().getRoot();
	for (CurricularCourse course : root.getAllCurricularCourses(executionSemester)) {
	    CompetenceCourse competence = course.getCompetenceCourse();
	    if (competence != null) {
		JSONObject json = new JSONObject();
		StringBuilder output = new StringBuilder();

		json.put("q-6.2.1.1", competence.getName(executionSemester));

		json.put("q-6.2.1.2", getTeachersAndTeachingHours(course, true));

		JSONObject q6213 = new JSONObject();
		String teachersAndTeachingHours = getTeachersAndTeachingHours(course, false);
		q6213.put("en", teachersAndTeachingHours);
		q6213.put("pt", teachersAndTeachingHours);
		json.put("q-6.2.1.3", q6213);

		JSONObject q6214 = new JSONObject();
		MultiLanguageString objectives = competence.getObjectivesI18N(executionSemester);
		q6214.put("en", cut("objectivos em ingles", objectives.getContent(Language.en), output, 1000));
		q6214.put("pt", cut("objectivos em portugues", objectives.getContent(Language.pt), output, 1000));
		json.put("q-6.2.1.4", q6214);

		JSONObject q6215 = new JSONObject();
		MultiLanguageString program = competence.getProgramI18N(executionSemester);
		q6215.put("en", cut("programa em ingles", program.getContent(Language.en), output, 1000));
		q6215.put("pt", cut("programa em portugues", program.getContent(Language.pt), output, 1000));
		json.put("q-6.2.1.5", q6215);

		JSONObject q6216 = new JSONObject();
		q6216.put("en",
			ResourceBundle.getBundle("resources.GEPResources", Locale.ENGLISH).getString("label.gep.a3es.q6-2-1-6"));
		q6216.put("pt",
			ResourceBundle.getBundle("resources.GEPResources", new Locale("pt")).getString("label.gep.a3es.q6-2-1-6"));
		json.put("q-6.2.1.6", q6216);

		JSONObject q6217 = new JSONObject();
		q6217.put("en", cut("avaliação em ingles", competence.getEvaluationMethodEn(executionSemester), output, 1000));
		q6217.put("pt", cut("avaliação em portugues", competence.getEvaluationMethod(executionSemester), output, 1000));
		json.put("q-6.2.1.7", q6217);

		JSONObject q6218 = new JSONObject();
		q6218.put("en",
			ResourceBundle.getBundle("resources.GEPResources", Locale.ENGLISH).getString("label.gep.a3es.q6-2-1-8"));
		q6218.put("pt",
			ResourceBundle.getBundle("resources.GEPResources", new Locale("pt")).getString("label.gep.a3es.q6-2-1-8"));
		json.put("q-6.2.1.8", q6218);

		List<String> references = new ArrayList<String>();
		for (BibliographicReference reference : competence.getBibliographicReferences(executionSemester)
			.getMainBibliographicReferences()) {
		    references.add(extractReference(reference));
		}
		json.put("q-6.2.1.9", StringUtils.join(references, "; "));
		jsons.put(json, output.toString());
	    }
	}
	return jsons;
    }

    private static String extractReference(BibliographicReference reference) {
	return StringUtils.join(
		new String[] { reference.getTitle(), reference.getAuthors(), reference.getYear(), reference.getReference() },
		", ");
    }

    private String cut(String field, String content, StringBuilder output, int size) {
	if (content == null) {
	    output.append(" " + field + " vazio.");
	} else {
	    int escapedLength = JSONObject.escape(content).getBytes().length;
	    if (escapedLength > size) {
		output.append(" " + field + " cortado a " + size + " caracteres.");
		return content.substring(0, size - 4 - (escapedLength - content.length())) + " ...";
	    }
	}
	return content;
    }

    private String getTeachersAndTeachingHours(CurricularCourse course, boolean responsibleTeacher) {
	Map<Teacher, Double> responsiblesMap = new HashMap<Teacher, Double>();
	List<ExecutionSemester> executionSemesters = getSelectedExecutionSemesters();
	for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
	    if (executionSemesters.contains(executionCourse.getExecutionPeriod())) {
		for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
		    if (professorhip.isResponsibleFor() == responsibleTeacher
			    && professorhip.getPerson().getTeacher()
				    .isActiveOrHasAuthorizationForSemester(executionCourse.getExecutionPeriod())) {
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
	int counter = 1000;
	List<String> responsibles = new ArrayList<String>();
	for (Teacher teacher : responsiblesMap.keySet()) {
	    String responsible = teacher.getPerson().getName() + " (" + responsiblesMap.get(teacher) + ")";
	    counter -= JSONObject.escape(responsible + ", ").getBytes().length;
	    responsibles.add(responsible);
	}

	if (!responsibleTeacher && course.isDissertation()) {
	    Set<Teacher> teachers = new HashSet<Teacher>();
	    for (ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
		if (executionCourse.getExecutionPeriod().getExecutionYear()
			.equals(executionSemester.getExecutionYear().getPreviousExecutionYear())) {
		    for (Attends attends : executionCourse.getAttends()) {
			if (attends.hasEnrolment() && attends.getEnrolment().getThesis() != null) {
			    for (ThesisEvaluationParticipant thesisEvaluationParticipant : attends.getEnrolment().getThesis()
				    .getOrientation()) {
				if (thesisEvaluationParticipant.getPerson().getTeacher() != null
					&& thesisEvaluationParticipant.getPerson().getTeacher()
						.isActiveOrHasAuthorizationForSemester(executionCourse.getExecutionPeriod())) {
				    teachers.add(thesisEvaluationParticipant.getPerson().getTeacher());
				}
			    }
			}
		    }
		}
	    }
	    for (Teacher teacher : teachers) {
		String responsible = teacher.getPerson().getName() + " (0.0)";
		if (counter - JSONObject.escape(responsible).getBytes().length < 0) {
		    break;
		}
		if (!responsiblesMap.containsKey(teacher)) {
		    counter -= JSONObject.escape(responsible + ", ").getBytes().length;
		    responsibles.add(responsible);
		}
	    }
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
		    for (CourseLoad courseLoad : degreeTeachingService.getShift().getCourseLoads()) {
			result = result + courseLoad.getTotalQuantity().doubleValue()
				* (degreeTeachingService.getPercentage().doubleValue() / 100);
		    }
		}
	    }
	}
	return result;
    }

    protected List<TeacherCurricularInformation> getTeacherCurricularInformation() {
	List<ExecutionSemester> executionSemesters = getSelectedExecutionSemesters();
	Set<Teacher> teachers = getTeachers(executionSemesters);
	List<TeacherCurricularInformation> teacherCurricularInformationList = new ArrayList<TeacherCurricularInformation>();
	for (Teacher teacher : teachers) {
	    TeacherCurricularInformation teacherCurricularInformation = new TeacherCurricularInformation(teacher, degree,
		    executionSemesters);
	    teacherCurricularInformationList.add(teacherCurricularInformation);
	}
	return teacherCurricularInformationList;
    }

    protected Set<Teacher> getTeachers(List<ExecutionSemester> executionSemesters) {
	Set<Teacher> teachers = new HashSet<Teacher>();
	ExecutionYear previousExecutionYear = executionSemester.getExecutionYear().getPreviousExecutionYear();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (executionSemesters.contains(executionCourse.getExecutionPeriod())) {
			for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
			    if (professorhip.getPerson().getTeacher()
				    .isActiveOrHasAuthorizationForSemester(executionCourse.getExecutionPeriod())) {
				teachers.add(professorhip.getPerson().getTeacher());
			    }
			}
		    }
		    if (previousExecutionYear.equals(executionCourse.getExecutionPeriod().getExecutionYear())) {
			if (executionCourse.isDissertation()) {
			    for (Attends attends : executionCourse.getAttends()) {
				if (attends.hasEnrolment() && attends.getEnrolment().getThesis() != null) {
				    for (ThesisEvaluationParticipant thesisEvaluationParticipant : attends.getEnrolment()
					    .getThesis().getOrientation()) {
					if (thesisEvaluationParticipant.getPerson().getTeacher() != null
						&& thesisEvaluationParticipant
							.getPerson()
							.getTeacher()
							.isActiveOrHasAuthorizationForSemester(
								executionCourse.getExecutionPeriod())) {
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
	return teachers;
    }

    protected Map<JSONObject, String> buildTeacherCurriculumJson() {
	Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();
	for (TeacherCurricularInformation info : getTeacherCurricularInformation()) {
	    JSONObject toplevel = new JSONObject();
	    StringBuilder output = new StringBuilder();

	    toplevel.put("q-cf-name", info.getTeacher().getPerson().getName());
	    // toplevel.put("q-cf-ies",
	    // RootDomainObject.getInstance().getInstitutionUnit().getName());
	    // toplevel.put("q-cf-uo", info.getUnitName());
	    toplevel.put("q-cf-cat", info.getProfessionalCategoryName());
	    toplevel.put("q-cf-time", info.getProfessionalRegimeTime());
	    JSONObject file = new JSONObject();
	    {
		file.put("name", cut("nome", info.getTeacher().getPerson().getName(), output, 200));
		// file.put("ies", cut("ies",
		// RootDomainObject.getInstance().getInstitutionUnit().getName(),
		// output, 200));
		// file.put("uo", cut("uo", info.getUnitName(), output, 200));
		file.put("cat", info.getProfessionalCategoryName());

		Iterator<QualificationBean> qualifications = info.getQualifications().iterator();
		if (qualifications.hasNext()) {
		    QualificationBean qualification = qualifications.next();
		    file.put("deg", qualification.getDegree());
		    if (qualification.getScientificArea() != null) {
			file.put("degarea", cut("area cientifica", qualification.getScientificArea(), output, 200));
		    } else {
			output.append(" Última Qualificação: degarea vazio.");
		    }
		    if (qualification.getYear() != null) {
			file.put("ano_grau", qualification.getYear());
		    } else {
			output.append(" Última Qualificação: ano_grau vazio.");
		    }
		    if (qualification.getInstitution() != null) {
			file.put("instituicao_conferente", cut("instituição", qualification.getInstitution(), output, 200));
		    } else {
			output.append(" Última Qualificação: instituicao_conferente vazio.");
		    }
		}
		file.put("regime", info.getProfessionalRegimeTime());

		if (qualifications.hasNext()) {
		    JSONArray academicArray = new JSONArray();
		    while (qualifications.hasNext()) {
			JSONObject academic = new JSONObject();
			QualificationBean qualification = qualifications.next();
			if (qualification.getYear() != null) {
			    academic.put("year", qualification.getYear());
			} else {
			    output.append(" Qualificações: year vazio.");
			}
			if (qualification.getDegree() != null) {
			    academic.put("degree", cut("grau", qualification.getDegree(), output, 30));
			} else {
			    output.append(" Qualificações: degree vazio.");
			}
			if (qualification.getScientificArea() != null) {
			    academic.put("area", cut("area", qualification.getScientificArea(), output, 100));
			} else {
			    output.append(" Qualificações: area vazio.");
			}
			if (qualification.getInstitution() != null) {
			    academic.put("ies", cut("ies", qualification.getInstitution(), output, 100));
			} else {
			    output.append(" Qualificações: ies vazio.");
			}
			if (qualification.getClassification() != null) {
			    academic.put("rank", cut("classificação", qualification.getClassification(), output, 30));
			} else {
			    output.append(" Qualificações: rank vazio.");
			}
			academicArray.add(academic);
		    }
		    file.put("form-academic", academicArray);
		}

		List<String> participations = info.getTop5ResultParticipation();
		if (!participations.isEmpty()) {
		    JSONArray researchArray = new JSONArray();
		    for (String publication : participations) {
			JSONObject research = new JSONObject();
			research.put("investigation", cut("investigação", publication, output, 300));
			researchArray.add(research);
		    }
		    file.put("form-investigation", researchArray);
		}

		List<String> career = info.getTop5ProfessionalCareer();
		if (!career.isEmpty()) {
		    JSONArray professionalArray = new JSONArray();
		    for (String profession : career) {
			JSONObject pro = new JSONObject();
			pro.put("profession", cut("carreira", profession, output, 100));
			professionalArray.add(pro);
		    }
		    file.put("form-professional", professionalArray);
		}

		JSONArray insideLectures = new JSONArray();
		List<LecturedCurricularUnit> lecturedUCs = info.getLecturedUCs();
		if (lecturedUCs.size() > 10) {
		    output.append(" UC leccionadas cortadas " + (lecturedUCs.size() - 10) + " entradas.");
		}
		for (LecturedCurricularUnit lecturedCurricularUnit : lecturedUCs.subList(0, Math.min(10, lecturedUCs.size()))) {
		    JSONObject lecture = new JSONObject();
		    lecture.put("curricularUnit", cut("unidade curricular", lecturedCurricularUnit.getName(), output, 100));
		    lecture.put("studyCycle", cut("ciclo de estudos", lecturedCurricularUnit.getDegree(), output, 200));
		    lecture.put("type", cut("tipo", lecturedCurricularUnit.getShiftType(), output, 30));
		    lecture.put("hoursPerWeek", lecturedCurricularUnit.getHours());
		    insideLectures.add(lecture);
		}
		file.put("form-unit", insideLectures);
	    }
	    toplevel.put("q-cf-cfile", file);

	    jsons.put(toplevel, output.toString());
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
		addCell("Instituição", null);
		addCell("Unidade Orgânica", null);
		addCell("Categoria", teacherCurricularInformation.getProfessionalCategoryName());
		Iterator<QualificationBean> qualifications = teacherCurricularInformation.getQualifications().iterator();

		QualificationBean qualification = qualifications.hasNext() ? qualifications.next() : null;
		addCell("Grau", qualification != null ? qualification.getDegree() : null);
		addCell("Área científica", qualification != null ? qualification.getScientificArea() : null);
		addCell("Ano", qualification != null ? qualification.getYear() : null);
		addCell("Instituição", qualification != null ? qualification.getInstitution() : null);

		addCell("Regime", teacherCurricularInformation.getProfessionalRegimeTime());

		List<String> otherQualificationStrings = new ArrayList<String>();
		while (qualifications.hasNext()) {
		    QualificationBean otherQualification = qualifications.next();
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
		for (LecturedCurricularUnit lecturedCurricularUnit : teacherCurricularInformation.getLecturedUCs()) {
		    StringBuilder lecturedString = new StringBuilder();
		    lecturedString.append(lecturedCurricularUnit.getName()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getDegree()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getShiftType()).append(",");
		    lecturedString.append(lecturedCurricularUnit.getHours());
		    lectured.add(lecturedString.toString());
		}
		addCell("UCs", StringUtils.join(lectured, "\n"));
	    }
	};
	return new SpreadsheetBuilder().addSheet(degree.getSigla(), sheet);
    }
}