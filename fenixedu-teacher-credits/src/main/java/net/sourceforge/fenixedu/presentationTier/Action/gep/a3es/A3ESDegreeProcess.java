/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.gep.a3es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fenixedu.academic.dto.externalServices.TeacherCurricularInformation;
import org.fenixedu.academic.dto.externalServices.TeacherCurricularInformation.LecturedCurricularUnit;
import org.fenixedu.academic.dto.externalServices.TeacherCurricularInformation.QualificationBean;
import org.fenixedu.academic.dto.externalServices.TeacherPublicationsInformation;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.DegreeTeachingServiceCorrection;
import org.fenixedu.academic.domain.teacher.OtherService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class A3ESDegreeProcess implements Serializable {
    private static final String BASE_URL = "http://www.a3es.pt/si/iportal.php";
//    private static final String BASE_URL = "http://formacao.a3es.pt/iportal.php";

    private static final String API_PROCESS = "api_process";

    private static final String API_FORM = "api_form";

    private static final String API_FOLDER = "api_folder";

    private static final String API_ANNEX = "api_annex";

    private static Map<String, String> acefIndex = new HashMap<String, String>();

    static {
        // This being hard-coded prevents future executions without our
        // interference, this should be persisted and managed by UI.

        acefIndex.put("ACEF/1314/06702", "LEGI");
        acefIndex.put("ACEF/1314/06722", "LEAN");
        acefIndex.put("ACEF/1314/06727", "LEMat");
        acefIndex.put("ACEF/1314/06737", "LMAC");
        acefIndex.put("ACEF/1314/06742", "MEAN");
        acefIndex.put("ACEF/1314/06747", "MBiotec");
        acefIndex.put("ACEF/1314/06757", "MEGI");
        acefIndex.put("ACEF/1314/06777", "MMA");
        acefIndex.put("ACEF/1314/06787", "MEMat");
        acefIndex.put("ACEF/1314/06792", "MBioNano");
        acefIndex.put("ACEF/1314/06807", "MQ");
        acefIndex.put("ACEF/1314/13727", "MEFarm");
        acefIndex.put("ACEF/1314/06812", "MEAer");
        acefIndex.put("ACEF/1314/06817", "MEAmbi");
        acefIndex.put("ACEF/1314/06822", "MEBiol");
        acefIndex.put("ACEF/1314/06842", "MEQ");
        acefIndex.put("ACEF/1314/06847", "MEBiom");
        acefIndex.put("ACEF/1314/06857", "MEMec");
        acefIndex.put("ACEF/1314/06867", "DBiotec");
        acefIndex.put("ACEF/1314/06877", "DEGest");
        acefIndex.put("ACEF/1314/06892", "DEN");
        acefIndex.put("ACEF/1314/06902", "DEAEPP");
        acefIndex.put("ACEF/1314/06952", "DEMec");
        acefIndex.put("ACEF/1314/06957", "DEPE");
        acefIndex.put("ACEF/1314/06982", "DEAer");
        acefIndex.put("ACEF/1314/06947", "DEAmb");
        acefIndex.put("ACEF/1314/06887", "DEMat");
        acefIndex.put("ACEF/1314/06897", "DEQuim");
        acefIndex.put("ACEF/1314/06912", "DMat");
        acefIndex.put("ACEF/1314/06922", "DQuim");
        acefIndex.put("ACEF/1314/06932", "DEBiom");

//        acefIndex.put("ACEF/1314/06812", "MEAER");
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
        base64Hash = new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
        JSONArray processes = invoke(webResource().path(API_PROCESS));
        JSONObject json = (JSONObject) processes.iterator().next();
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
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
                                    json.getKey().toJSONString());
                    int status = response.getStatus();
                    if (status == 201) {
                        output.add("201 Created: " + json.getKey().get("q-6.2.1.1") + ": " + json.getValue());
                    } else {
                        output.add(status + ": " + json.getKey().get("q-6.2.1.1") + ": " + response.getEntity() + " input: "
                                + json.getKey().toJSONString());
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
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId)
                                    .queryParam("folderId", teacherCurriculumId), json.getKey().toJSONString());
                    int status = response.getStatus();
                    if (status == 201) {
                        output.add("201 Created: " + json.getKey().get("q-cf-name") + ": " + json.getValue());
                    } else {
                        output.add(status + ": " + json.getKey().get("q-cf-name") + ": " + response.getEntity() + " input: "
                                + json.getKey().toJSONString());
                    }
                }
                break;
            }
        }
        return output;
    }

    protected WebTarget webResource() {
        Client client = ClientBuilder.newClient();
        return client.target(BASE_URL);
    }

    protected JSONArray invoke(WebTarget resource) {
        return (JSONArray) ((JSONObject) JSONValue.parse(resource.request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + base64Hash).get(String.class))).get("list");
    }

    protected Response post(WebTarget resource, String arg) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash)
                .buildPost(Entity.text(arg)).invoke();

    }

    protected Response delete(WebTarget resource) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash).buildDelete().invoke();
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
                json.put("q-6.2.1.2", cut("Docente responsavel", getTeachersAndTeachingHours(course, true), output, 100));

                JSONObject q6213 = new JSONObject();
                String teachersAndTeachingHours = getTeachersAndTeachingHours(course, false);
                json.put("q-6.2.1.3", teachersAndTeachingHours);

                JSONObject q6214 = new JSONObject();
                MultiLanguageString objectives = competence.getObjectivesI18N(executionSemester);
                q6214.put("en", cut("objectivos em ingles", objectives.getContent(MultiLanguageString.en), output, 1000));
                q6214.put("pt", cut("objectivos em portugues", objectives.getContent(MultiLanguageString.pt), output, 1000));
                json.put("q-6.2.1.4", q6214);

                JSONObject q6215 = new JSONObject();
                MultiLanguageString program = competence.getProgramI18N(executionSemester);
                q6215.put("en", cut("programa em ingles", program.getContent(MultiLanguageString.en), output, 1000));
                q6215.put("pt", cut("programa em portugues", program.getContent(MultiLanguageString.pt), output, 1000));
                json.put("q-6.2.1.5", q6215);

                JSONObject q6216 = new JSONObject();
                q6216.put("en", BundleUtil.getString(Bundle.GEP, Locale.ENGLISH, "label.gep.a3es.q6-2-1-6"));
                q6216.put("pt", BundleUtil.getString(Bundle.GEP, new Locale("pt"), "label.gep.a3es.q6-2-1-6"));
                json.put("q-6.2.1.6", q6216);

                JSONObject q6217 = new JSONObject();
                q6217.put("en", cut("avaliação em ingles", competence.getEvaluationMethodEn(executionSemester), output, 1000));
                q6217.put("pt", cut("avaliação em portugues", competence.getEvaluationMethod(executionSemester), output, 1000));
                json.put("q-6.2.1.7", q6217);

                JSONObject q6218 = new JSONObject();
                q6218.put("en", BundleUtil.getString(Bundle.GEP, Locale.ENGLISH, "label.gep.a3es.q6-2-1-8"));
                q6218.put("pt", BundleUtil.getString(Bundle.GEP, new Locale("pt"), "label.gep.a3es.q6-2-1-8"));
                json.put("q-6.2.1.8", q6218);

                List<String> references = new ArrayList<String>();
                final BibliographicReferences bibliographicReferences = competence.getBibliographicReferences(executionSemester);
                if (bibliographicReferences != null) {
                    for (BibliographicReference reference : bibliographicReferences.getMainBibliographicReferences()) {
                        references.add(extractReference(reference));
                    }
                }
                json.put("q-6.2.1.9", cut("Referências Bibliográficas", StringUtils.join(references, "; "), output, 1000));
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
        for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
            if (executionSemesters.contains(executionCourse.getExecutionPeriod())) {
                for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
                    if (professorhip.isResponsibleFor() == responsibleTeacher
                            && PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(professorhip.getPerson().getTeacher(), executionCourse.getExecutionPeriod())) {
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
            String responsible =
                    (responsibleTeacher ? teacher.getPerson().getFirstAndLastName() : teacher.getPerson().getName()) + " ("
                            + responsiblesMap.get(teacher) + ")";
            counter -= JSONObject.escape(responsible + ", ").getBytes().length;
            responsibles.add(responsible);
        }

        if (!responsibleTeacher && course.isDissertation()) {
            Set<Teacher> teachers = new HashSet<Teacher>();
            for (ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                if (executionCourse.getExecutionPeriod().getExecutionYear()
                        .equals(executionSemester.getExecutionYear().getPreviousExecutionYear())) {
                    for (Attends attends : executionCourse.getAttendsSet()) {
                        if (attends.getEnrolment() != null && attends.getEnrolment().getThesis() != null) {
                            for (ThesisEvaluationParticipant thesisEvaluationParticipant : attends.getEnrolment().getThesis()
                                    .getOrientation()) {
                                if (thesisEvaluationParticipant.getPerson().getTeacher() != null
                                        && PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(thesisEvaluationParticipant.getPerson().getTeacher(), executionCourse.getExecutionPeriod())) {
                                    teachers.add(thesisEvaluationParticipant.getPerson().getTeacher());
                                }
                            }
                        }
                    }
                }
            }
            for (Teacher teacher : teachers) {
                String responsible =
                        (responsibleTeacher ? teacher.getPerson().getFirstAndLastName() : teacher.getPerson().getName())
                                + " (0.0)";
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
        TeacherService teacherService =
                TeacherService.getTeacherServiceByExecutionPeriod(teacher, professorhip.getExecutionCourse().getExecutionPeriod());
        Double result = 0.0;
        if (teacherService != null) {
            for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {
                if (degreeTeachingService.getProfessorship().getExecutionCourse().equals(professorhip.getExecutionCourse())) {
                    for (CourseLoad courseLoad : degreeTeachingService.getShift().getCourseLoadsSet()) {
                        result =
                                result + courseLoad.getTotalQuantity().doubleValue()
                                        * (degreeTeachingService.getPercentage().doubleValue() / 100);
                    }
                }
            }
            for (OtherService otherService : teacherService.getOtherServices()) {
                if (otherService instanceof DegreeTeachingServiceCorrection) {
                    DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                            (DegreeTeachingServiceCorrection) otherService;
                    if (degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                            .equals(professorhip.getExecutionCourse())
                            && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().isDissertation())
                            && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                    .getProjectTutorialCourse())) {
                        result = result + degreeTeachingServiceCorrection.getCorrection().doubleValue();
                    }
                }
            }
        }
        return result;
    }

    protected List<TeacherCurricularInformation> getTeacherCurricularInformation() {
        List<ExecutionSemester> executionSemesters = getSelectedExecutionSemesters();
        Set<Teacher> teachers = getTeachers(executionSemesters);

        Map<Teacher, List<String>> teacherPublicationsInformationsMap =
                TeacherPublicationsInformation.getTeacherPublicationsInformations(teachers);

        List<TeacherCurricularInformation> teacherCurricularInformationList = new ArrayList<TeacherCurricularInformation>();
        for (Teacher teacher : teachers) {
            TeacherCurricularInformation teacherCurricularInformation =
                    new TeacherCurricularInformation(teacher, degree, executionSemesters,
                            teacherPublicationsInformationsMap.get(teacher));
            teacherCurricularInformationList.add(teacherCurricularInformation);
        }
        return teacherCurricularInformationList;
    }

    protected Set<Teacher> getTeachers(List<ExecutionSemester> executionSemesters) {
        Set<Teacher> teachers = new HashSet<Teacher>();
        ExecutionYear previousExecutionYear = executionSemester.getExecutionYear().getPreviousExecutionYear();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                    if (executionSemesters.contains(executionCourse.getExecutionPeriod())) {
                        for (Professorship professorhip : executionCourse.getProfessorshipsSet()) {
                            if (PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(professorhip.getPerson().getTeacher(), executionCourse.getExecutionPeriod())) {
                                teachers.add(professorhip.getPerson().getTeacher());
                            }
                        }
                    }
                    if (previousExecutionYear.equals(executionCourse.getExecutionPeriod().getExecutionYear())) {
                        if (executionCourse.isDissertation()) {
                            for (Attends attends : executionCourse.getAttendsSet()) {
                                if (attends.getEnrolment() != null && attends.getEnrolment().getThesis() != null) {
                                    for (ThesisEvaluationParticipant thesisEvaluationParticipant : attends.getEnrolment()
                                            .getThesis().getOrientation()) {
                                        if (thesisEvaluationParticipant.getPerson().getTeacher() != null
                                                && PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(thesisEvaluationParticipant
                                                .getPerson()
                                                .getTeacher(), executionCourse.getExecutionPeriod())) {
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
            for (PhdIndividualProgramProcess phdIndividualProgramProcess : phdProgram.getIndividualProgramProcessesSet()) {
                for (ExecutionSemester executionSemester : executionSemesters) {
                    if (phdIndividualProgramProcess.isActive(executionSemester.getAcademicInterval().toInterval())) {
                        for (PhdParticipant phdParticipant : phdIndividualProgramProcess.getParticipantsSet()) {
                            if (phdParticipant instanceof InternalPhdParticipant) {
                                InternalPhdParticipant internalPhdParticipant = (InternalPhdParticipant) phdParticipant;
                                if (internalPhdParticipant.isGuidingOrAssistantGuiding()
                                        && internalPhdParticipant.getPerson().getTeacher() != null
                                        && PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(internalPhdParticipant.getPerson().getTeacher(), executionSemester)) {
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
            // Bennu.getInstance().getInstitutionUnit().getName());
            // toplevel.put("q-cf-uo", info.getUnitName());
            toplevel.put("q-cf-cat", info.getProfessionalCategoryName());
            toplevel.put("q-cf-time", info.getProfessionalRegimeTime());
            JSONObject file = new JSONObject();
            {
                file.put("name", cut("nome", info.getTeacher().getPerson().getName(), output, 200));
                // file.put("ies", cut("ies",
                // Bennu.getInstance().getInstitutionUnit().getName(),
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
                    int i = 0;
                    while (qualifications.hasNext()) {
                        i++;
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
                    for (int j = i; j < 3; j++) {
                        JSONObject academic = new JSONObject();
                        academic.put("year", StringUtils.EMPTY);
                        academic.put("degree", StringUtils.EMPTY);
                        academic.put("area", StringUtils.EMPTY);
                        academic.put("ies", StringUtils.EMPTY);
                        academic.put("rank", StringUtils.EMPTY);
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

                List<String> developmentActivities = info.getTop5ProfessionalDevelomentActivities();
                if (!developmentActivities.isEmpty()) {
                    JSONArray researchArray = new JSONArray();
                    for (String developmentActivity : developmentActivities) {
                        JSONObject activity = new JSONObject();
                        activity.put("highlevelactivities", cut("actividade", developmentActivity, output, 100));
                        researchArray.add(activity);
                    }
                    file.put("form-highlevelactivities", researchArray);
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
                for (int lecturedCurricularUnitIndex = Math.min(10, lecturedUCs.size()); lecturedCurricularUnitIndex < 10; lecturedCurricularUnitIndex++) {
                    JSONObject lecture = new JSONObject();
                    lecture.put("curricularUnit", StringUtils.EMPTY);
                    lecture.put("studyCycle", StringUtils.EMPTY);
                    lecture.put("type", StringUtils.EMPTY);
                    lecture.put("hoursPerWeek", StringUtils.EMPTY);
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
        SheetData<TeacherCurricularInformation> sheet =
                new SheetData<TeacherCurricularInformation>(getTeacherCurricularInformation()) {
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