/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller.teacher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.UriBuilder;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Attends.StudentAttendsStateType;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.util.email.ExecutionCourseSender;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.dto.student.StudentStatuteBean;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.security.CSRFTokenBean;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

@Controller
@RequestMapping("/teacher/{executionCourse}/attends/")
public class AttendsSearchController extends ExecutionCourseController {

    // hack
    @Autowired
    CSRFTokenBean csrfTokenBean;

    @ModelAttribute("csrf")
    public CSRFTokenBean getCSRF() {
        return csrfTokenBean;
    }

    @Override
    protected Class<?> getFunctionalityType() {
        return ManageExecutionCourseDA.class;
    }

    @Override
    Boolean getPermission(Professorship prof) {
        return prof.getPermissions().getStudents();
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public TeacherView searchAttends(Model model) {

        JsonArray studentAttendsStateTypes = new JsonArray();
        for (StudentAttendsStateType type : StudentAttendsStateType.values()) {
            JsonObject typeJson = new JsonObject();
            typeJson.addProperty("type", BundleUtil.getString(Bundle.ENUMERATION, type.getQualifiedName()));
            studentAttendsStateTypes.add(typeJson);
        }

        model.addAttribute("attendsStates", studentAttendsStateTypes);
        model.addAttribute("groupings", view(Collections.emptyList()));

        model.addAttribute("curricularPlans", view(executionCourse.getAttendsDegreeCurricularPlans()));
        model.addAttribute("shifts", view(executionCourse.getShiftsOrderedByLessons()));

        model.addAttribute("shiftTypes", view(executionCourse.getShiftTypes()));

        return new TeacherView("executionCourse/attendsSearch/viewStudentList", executionCourse);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> listAttends() {
        return new ResponseEntity<>(
                view(executionCourse.getAttendsSet().stream().filter(attendee -> attendee.getRegistration() != null
                        && !hasEnrolmentAnnuled(attendee.getRegistration(), executionCourse))).toString(),
                HttpStatus.OK);
    }

    static private boolean hasEnrolmentAnnuled(final Registration registration, final ExecutionCourse course) {
        final Attends attends = registration == null ? null : registration.getAssociatedAttendsSet().stream()
                .filter(a -> a.getExecutionCourse() == course).findAny().orElse(null);
        return attends != null && attends.getEnrolment().isAnnulled();
    }

    @RequestMapping(value = "/studentSpreadsheet", method = RequestMethod.POST)
    public void generateSpreadsheet(@RequestParam String filteredAttendsJson, HttpServletResponse response) throws IOException {
        Set<Attends> attends = new TreeSet<Attends>(Attends.COMPARATOR_BY_STUDENT_NUMBER);
        for (JsonElement elem : new JsonParser().parse(filteredAttendsJson).getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            attends.add((Attends) FenixFramework.getDomainObject(object.get("id").getAsString()));
        }

        final SpreadsheetBuilder builder = new SpreadsheetBuilder();
        builder.addSheet(executionCourse.getPrettyAcronym().concat(BundleUtil.getString(Bundle.APPLICATION, "label.students")),
                new SheetData<Attends>(attends) {
                    protected String getLabel(final String key) {
                        return BundleUtil.getString(Bundle.APPLICATION, key);
                    }

                    @Override
                    protected void makeLine(final Attends attends) {
                        addCell(getLabel("label.username"), attends.getRegistration().getPerson().getUsername());
                        addCell(getLabel("label.number"), attends.getRegistration().getNumber());
                        addCell(getLabel("label.name"), attends.getRegistration().getPerson().getName());
                        addCell(getLabel("label.institutional.email"), attends.getRegistration().getPerson().getInstitutionalEmailAddressValue());
                        addCell(getLabel("label.default.email"), attends.getRegistration().getPerson().getDefaultEmailAddressValue());
                        executionCourse.getShiftTypes()
                                .forEach(shiftType -> addCell(getLabel("label.shift") + " " + shiftType.getFullNameTipoAula(),
                                        Optional.ofNullable(
                                                attends.getRegistration().getShiftFor(attends.getExecutionCourse(), shiftType))
                                                .map(Shift::getNome).orElse("")));
                        if (attends.getEnrolment() != null) {
                            addCell(getLabel("label.numberOfEnrollments"), attends.getEnrolment()
                                    .getNumberOfTotalEnrolmentsInThisCourse(attends.getEnrolment().getExecutionPeriod()));
                        } else {
                            addCell(getLabel("label.numberOfEnrollments"), "--");
                        }

                        addCell(getLabel("label.attends.enrollmentState"),
                                BundleUtil.getString(Bundle.ENUMERATION, attends.getAttendsStateType().getQualifiedName()));

                        RegistrationState registrationState =
                                attends.getRegistration().getLastRegistrationState(attends.getExecutionYear());
                        addCell(getLabel("label.registration.state"),
                                registrationState == null ? "" : registrationState.getStateType().getDescription());

                        addCell(getLabel("label.Degree"),
                                attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan().getPresentationName());

                        Collection<StudentStatuteBean> studentStatutes =
                                attends.getRegistration().getStudent().getStatutes(executionCourse.getExecutionPeriod());
                        if (studentStatutes.size() > 0) {
                            addCell(getLabel("label.studentStatutes"),
                                    studentStatutes.stream()
                                            .map(st -> st.getStatuteType().getName().getContent()
                                                    + (Strings.isNullOrEmpty(st.getStudentStatute().getComment()) ? "" : " ("
                                                            + st.getStudentStatute().getComment() + ")"))
                                            .collect(Collectors.joining(" | ")));
                        }
                    }
                });

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition",
                String.format("attachment; filename=%s.xls",
                        Joiner.on(" - ")
                                .join(executionCourse.getSigla(),
                                        BundleUtil.getString("resources.ApplicationResources", "label.students"))
                                .replace(" ", "_")));
        try (OutputStream outputStream = response.getOutputStream()) {
            builder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
        }

    }

    @RequestMapping(value = "/studentEvaluationsSpreadsheet", method = RequestMethod.POST)
    public void generateSpreadsheet(HttpServletResponse response) throws IOException {

        Set<Attends> attends = executionCourse.getAttendsSet();

        final SpreadsheetBuilder builder = new SpreadsheetBuilder();
        builder.addSheet(executionCourse.getPrettyAcronym().concat(BundleUtil.getString(Bundle.APPLICATION, "label.grades")),
                new SheetData<Attends>(attends) {
                    protected String getLabel(final String key) {
                        return BundleUtil.getString(Bundle.APPLICATION, key);
                    }

                    @Override
                    protected void makeLine(final Attends attends) {
                        addCell(getLabel("label.username"), attends.getRegistration().getPerson().getUsername());
                        addCell(getLabel("label.number"), attends.getRegistration().getNumber());
                        addCell(getLabel("label.name"), attends.getRegistration().getPerson().getPresentationName());
                        addCell(getLabel("label.Degree"),
                                attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan().getPresentationName());
                        addCell(getLabel("label.attends.enrollmentState"),
                                BundleUtil.getString(Bundle.ENUMERATION, attends.getAttendsStateType().getQualifiedName()));
                        executionCourse.getAssociatedEvaluationsSet()
                                .forEach(ev -> addCell(ev.getPresentationName(),
                                        attends.getAssociatedMarksSet().stream().filter(mark -> mark.getEvaluation() == ev)
                                                .map(Mark::getMark).filter(mark -> mark != null).findAny().orElse("")));
                    }
                });

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", String.format("attachment; filename=%s.xls",
                Joiner.on(" - ")
                        .join(executionCourse.getSigla(), BundleUtil.getString("resources.ApplicationResources", "label.grades"))
                        .replace(" ", "_")));
        try (OutputStream outputStream = response.getOutputStream()) {
            builder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
        }
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public RedirectView sendEmail(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam("filteredAttendsJson") String filteredAttendsJson, @RequestParam("filtersJson") String filtersJson) {
        String attendTypeValues = "", degreeNameValues = "", shiftsValues = "", studentStatuteTypesValues = "";

        JsonObject filters = new JsonParser().parse(filtersJson).getAsJsonObject();
        for (JsonElement elem : filters.get("attendsStates").getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            if (object.get("value").getAsBoolean()) {
                if (!attendTypeValues.isEmpty()) {
                    attendTypeValues += ", ";
                }
                attendTypeValues += object.get("type");
            }
        }

        for (JsonElement elem : filters.get("curricularPlans").getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            if (object.get("value").getAsBoolean()) {
                if (!degreeNameValues.isEmpty()) {
                    degreeNameValues += ", ";
                }
                degreeNameValues += object.get("name");
            }
        }

        for (JsonElement elem : filters.get("shifts").getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            if (object.get("value").getAsBoolean()) {
                if (!shiftsValues.isEmpty()) {
                    shiftsValues += ", ";
                }
                shiftsValues += object.get("name");
            }
        }

        JsonObject noStatuteObject = filters.get("noStudentStatuteTypes").getAsJsonObject();
        if (noStatuteObject.get("value").getAsBoolean()) {
            studentStatuteTypesValues += noStatuteObject.get("shortName");
        }
        for (JsonElement elem : filters.get("studentStatuteTypes").getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            if (object.get("value").getAsBoolean()) {
                if (!studentStatuteTypesValues.isEmpty()) {
                    studentStatuteTypesValues += ", ";
                }
                studentStatuteTypesValues += object.get("name");
            }
        }

        String label = String.format("%s : %s \n%s : %s \n%s : %s \n%s : %s",
                BundleUtil.getString(Bundle.APPLICATION, "label.selectStudents"), attendTypeValues,
                BundleUtil.getString(Bundle.APPLICATION, "label.attends.courses"), degreeNameValues,
                BundleUtil.getString(Bundle.APPLICATION, "label.selectShift"), shiftsValues,
                BundleUtil.getString(Bundle.APPLICATION, "label.studentStatutes"), studentStatuteTypesValues);

        Builder<Attends> builder = Stream.builder();
        for (JsonElement elem : new JsonParser().parse(filteredAttendsJson).getAsJsonArray()) {
            JsonObject object = elem.getAsJsonObject();
            builder.accept(FenixFramework.getDomainObject(object.get("id").getAsString()));
        }

        Group users = Group.users(builder.build().map(a -> a.getRegistration().getPerson().getUser()).filter(Objects::nonNull)
                .sorted(User.COMPARATOR_BY_NAME));
        ArrayList<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(Recipient.newInstance(label, users));
        String sendEmailUrl = UriBuilder.fromUri("/messaging/emails.do").queryParam("method", "newEmail")
                .queryParam("sender", ExecutionCourseSender.newInstance(executionCourse).getExternalId())
                .queryParam("recipient", recipients.stream().filter(r -> r != null).map(r -> r.getExternalId()).toArray()).build()
                .toString();
        String sendEmailWithChecksumUrl =
                GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), sendEmailUrl, session);
        return new RedirectView(sendEmailWithChecksumUrl, true);

    }
}