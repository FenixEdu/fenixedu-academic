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
package net.sourceforge.fenixedu.webServices.jersey.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Path("/fenix/jersey/services")
public class JerseyServices {
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    @Context
    ServletContext context;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hellofenix")
    public String hellofenix() {
        return "Hello!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("remotePerson")
    public String remotePerson(@QueryParam("username") final String username, @QueryParam("method") final String method)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        final Person person = Person.readPersonByUsername(username);
        if (person != null) {
            final Method personMethod = Person.class.getMethod(method);
            Object result = personMethod.invoke(person);
            return result == null ? StringUtils.EMPTY : result.toString();
        }
        return StringUtils.EMPTY;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("readAllUserData")
    public static String readAllUserData(@QueryParam("types") final String types) {
        RoleType[] roles;
        if (types != null && StringUtils.isNotBlank(types)) {
            roles = new RoleType[types.split("-").length];
            int i = 0;
            for (String typeString : types.split("-")) {
                roles[i] = RoleType.valueOf(typeString);
                i++;
            }
        } else {
            roles = new RoleType[0];
        }
        final StringBuilder builder = new StringBuilder();
        for (final User user : Bennu.getInstance().getUserSet()) {
            if (!StringUtils.isEmpty(user.getUsername())) {
                final Person person = user.getPerson();
                if (roles.length == 0 || person.hasAnyRole(roles)) {
                    builder.append(user.getUsername());
                    builder.append("\t");
                    builder.append(person.getName());
                    builder.append("\t");
                    builder.append(person.getExternalId());
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("readAllEmails")
    public static String readAllEmails() {
        final StringBuilder builder = new StringBuilder();
        for (final Party party : Bennu.getInstance().getPartysSet()) {
            if (party.isPerson()) {
                final Person person = (Person) party;
                final String email = person.getEmailForSendingEmails();
                if (email != null) {
                    final User user = person.getUser();
                    if (user != null) {
                        final String username = user.getUsername();
                        builder.append(username);
                        builder.append("\t");
                        builder.append(email);
                        builder.append("\n");
                    }
                }
            }
        }
        return builder.toString();
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public String readUsers() {
        JsonArray users = new JsonArray();
        for (final User user : Bennu.getInstance().getUserSet()) {
            if (!StringUtils.isEmpty(user.getUsername()) && user.getPerson() != null) {
                JsonObject json = new JsonObject();
                json.addProperty("istId", user.getUsername());
                json.addProperty("name", user.getPerson().getName());
                if (user.getPerson().getEmailForSendingEmails() != null) {
                    json.addProperty("email", user.getPerson().getEmailForSendingEmails());
                }
                users.add(json);
            }
        }
        return users.toString();
    }

    @GET
    @Path("researchers")
    @Produces(MediaType.APPLICATION_JSON)
    public String readResearchers() {
        JsonArray researchers = new JsonArray();

        final Map<User, Set<Unit>> researchUnitMap = new HashMap<User, Set<Unit>>();
        for (final User user : Bennu.getInstance().getUserSet()) {
            Person person = user.getPerson();
            if (!StringUtils.isEmpty(user.getUsername()) && person != null
                    && (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.RESEARCHER) || person.isPhdStudent())) {
                researchUnitMap.put(user, new HashSet<Unit>());
            }
        }
        for (final Party party : Bennu.getInstance().getPartysSet()) {
            if (party instanceof ResearchUnit) {
                final ResearchUnit unit = (ResearchUnit) party;
                for (final Teacher teacher : unit.getAllTeachers()) {
                    add(researchUnitMap, teacher.getPerson().getUser(), unit);
                }
                for (final Person person : unit.getResearchers()) {
                    add(researchUnitMap, person.getUser(), unit);
                }
                for (final Employee employee : unit.getAllWorkingEmployees()) {
                    add(researchUnitMap, employee.getPerson().getUser(), unit);
                }
            }
        }

        for (final Entry<User, Set<Unit>> entry : researchUnitMap.entrySet()) {
            final User user = entry.getKey();
            JsonObject json = new JsonObject();
            json.addProperty("istId", user.getUsername());

            JsonArray array = new JsonArray();
            for (Unit unit : entry.getValue()) {
                JsonObject element = new JsonObject();
                if (!Strings.isNullOrEmpty(unit.getAcronym())) {
                    element.addProperty("acronym", unit.getAcronym());
                }
                if (!Strings.isNullOrEmpty(unit.getName())) {
                    element.addProperty("name", unit.getName());
                }
                array.add(element);
            }

            json.add("department", array);
            researchers.add(json);
        }
        return researchers.toString();
    }

    private void add(final Map<User, Set<Unit>> researchUnitMap, final User user, final ResearchUnit unit) {
        if (user != null) {
            if (!researchUnitMap.containsKey(user)) {
                researchUnitMap.put(user, new HashSet<Unit>());
            }
            researchUnitMap.get(user).add(unit);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readActiveStudentInfoForJobBank")
    public static String readActiveStudentInfoForJobBank(@QueryParam("username") final String username) {
        final Student student = Person.readPersonByUsername(username).getStudent();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        Set<Registration> registrations = new HashSet<Registration>();
        LocalDate today = new LocalDate();
        for (Registration registration : student.getRegistrationsSet()) {
            if (registration.isBolonha() && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                if (registration.hasAnyActiveState(currentExecutionYear)) {
                    registrations.add(registration);
                } else {
                    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
                    if (registrationConclusionBean.isConcluded()) {
                        YearMonthDay conclusionDate = registrationConclusionBean.getConclusionDate();
                        if (conclusionDate != null && !conclusionDate.plusYears(1).isBefore(today)) {
                            registrations.add(registration);
                        }
                    }
                }
            }
        }
        String info = getRegistrationsAsJSON(registrations);
        return student != null ? info : StringUtils.EMPTY;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readStudentInfoForJobBank")
    public static String readStudentInfoForJobBank(@QueryParam("username") final String username) {
        final Student student = Person.readPersonByUsername(username).getStudent();
        Set<Registration> registrations = new HashSet<Registration>();
        for (Registration registration : student.getRegistrationsSet()) {
            if (registration.isBolonha() && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
                if (registration.isActive() || registrationConclusionBean.isConcluded()) {
                    registrations.add(registration);
                }
            }
        }
        String info = getRegistrationsAsJSON(registrations);
        return student != null ? info : StringUtils.EMPTY;
    }

    protected static String getRegistrationsAsJSON(Set<Registration> registrations) {
        JSONArray infos = new JSONArray();
        int i = 0;
        for (Registration registration : registrations) {
            JSONObject studentInfoForJobBank = new JSONObject();
            studentInfoForJobBank.put("username", registration.getPerson().getUsername());
            studentInfoForJobBank.put("hasPersonalDataAuthorization", registration.getStudent()
                    .hasPersonalDataAuthorizationForProfessionalPurposesAt().toString());
            Person person = registration.getStudent().getPerson();
            studentInfoForJobBank.put("dateOfBirth", person.getDateOfBirthYearMonthDay() == null ? null : person
                    .getDateOfBirthYearMonthDay().toString());
            studentInfoForJobBank.put("nationality", person.getCountry() == null ? null : person.getCountry().getName());
            PhysicalAddress defaultPhysicalAddress = person.getDefaultPhysicalAddress();
            studentInfoForJobBank.put("address", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getAddress());
            studentInfoForJobBank.put("area", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getArea());
            studentInfoForJobBank.put("areaCode", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getAreaCode());
            studentInfoForJobBank.put("districtSubdivisionOfResidence",
                    defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getDistrictSubdivisionOfResidence());
            studentInfoForJobBank.put("mobilePhone", person.getDefaultMobilePhoneNumber());
            studentInfoForJobBank.put("phone", person.getDefaultPhoneNumber());
            studentInfoForJobBank.put("email", person.getEmailForSendingEmails());
            studentInfoForJobBank.put("remoteRegistrationOID", registration.getExternalId());
            studentInfoForJobBank.put("number", registration.getNumber().toString());
            studentInfoForJobBank.put("degreeOID", registration.getDegree().getExternalId());
            studentInfoForJobBank.put("isConcluded", String.valueOf(registration.isRegistrationConclusionProcessed()));
            studentInfoForJobBank.put("curricularYear", String.valueOf(registration.getCurricularYear()));
            for (CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
                    .getCycleCurriculumGroups()) {
                studentInfoForJobBank.put(cycleCurriculumGroup.getCycleType().name(), cycleCurriculumGroup.getAverage()
                        .toString());

            }
            infos.add(studentInfoForJobBank);
        }
        return infos.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readAllStudentsInfoForJobBank")
    public static String readAllStudentsInfoForJobBank() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        Set<Registration> registrations = new HashSet<Registration>();
        LocalDate today = new LocalDate();
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.hasAnyActiveState(currentExecutionYear) && registration.isBolonha()
                    && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                registrations.add(registration);
            }
        }
        for (ConclusionProcess conclusionProcess : Bennu.getInstance().getConclusionProcessesSet()) {
            if (conclusionProcess.getConclusionDate() != null
                    && !conclusionProcess.getConclusionDate().plusYears(1).isBefore(today)) {
                registrations.add(conclusionProcess.getRegistration());
            }
        }
        JSONArray infos = new JSONArray();
        for (Registration registration : registrations) {
            infos.add(getStudentInfoForJobBank(registration));
        }
        return infos.toJSONString();
    }

    @SuppressWarnings("unchecked")
    protected static JSONObject getStudentInfoForJobBank(Registration registration) {
        try {
            JSONObject studentInfoForJobBank = new JSONObject();
            studentInfoForJobBank.put("username", registration.getPerson().getUsername());
            studentInfoForJobBank.put("hasPersonalDataAuthorization", registration.getStudent()
                    .hasPersonalDataAuthorizationForProfessionalPurposesAt().toString());
            Person person = registration.getStudent().getPerson();
            studentInfoForJobBank.put("dateOfBirth", person.getDateOfBirthYearMonthDay() == null ? null : person
                    .getDateOfBirthYearMonthDay().toString());
            studentInfoForJobBank.put("nationality", person.getCountry() == null ? null : person.getCountry().getName());
            studentInfoForJobBank.put("address", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getAddress());
            studentInfoForJobBank.put("area", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getArea());
            studentInfoForJobBank.put("areaCode", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getAreaCode());
            studentInfoForJobBank.put("districtSubdivisionOfResidence",
                    person.getDefaultPhysicalAddress() == null ? null : person.getDefaultPhysicalAddress()
                            .getDistrictSubdivisionOfResidence());
            studentInfoForJobBank.put("mobilePhone", person.getDefaultMobilePhoneNumber());
            studentInfoForJobBank.put("phone", person.getDefaultPhoneNumber());
            studentInfoForJobBank.put("email", person.getEmailForSendingEmails());
            studentInfoForJobBank.put("remoteRegistrationOID", registration.getExternalId());
            studentInfoForJobBank.put("number", registration.getNumber().toString());
            studentInfoForJobBank.put("degreeOID", registration.getDegree().getExternalId());
            studentInfoForJobBank.put("isConcluded", String.valueOf(registration.isRegistrationConclusionProcessed()));
            studentInfoForJobBank.put("curricularYear", String.valueOf(registration.getCurricularYear()));
            for (CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
                CycleCurriculumGroup cycle = registration.getLastStudentCurricularPlan().getCycle(cycleType);
                if (cycle != null) {
                    studentInfoForJobBank.put(cycle.getCycleType().name(), cycle.getAverage().toString());
                }
            }
            return studentInfoForJobBank;
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readBolonhaDegrees")
    public static String readBolonhaDegrees() {
        JsonArray infos = new JsonArray();
        for (Degree degree : Degree.readBolonhaDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {
                JsonObject degreeInfo = new JsonObject();
                degreeInfo.addProperty("degreeOid", degree.getExternalId());
                degreeInfo.addProperty("name", degree.getPresentationName());
                degreeInfo.addProperty("degreeType", degree.getDegreeTypeName());
                infos.add(degreeInfo);
            }
        }
        return infos.toString();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readThesis")
    public static String readPhdThesis() {
        JsonArray infos = new JsonArray();

        for (PhdIndividualProgramProcessNumber phdProcessNumber : Bennu.getInstance().getPhdIndividualProcessNumbersSet()) {
            PhdIndividualProgramProcess phdProcess = phdProcessNumber.getProcess();
            if (phdProcess.isConcluded()) {
                JsonObject phdInfo = new JsonObject();
                phdInfo.addProperty("id", phdProcess.getExternalId());
                phdInfo.addProperty("author", phdProcess.getPerson().getUsername());
                phdInfo.addProperty("title", phdProcess.getThesisTitle());

                JsonArray schools = new JsonArray();
                switch (phdProcess.getCollaborationType()) {
                case NONE:
                case WITH_SUPERVISION:
                case ERASMUS_MUNDUS:
                case OTHER:
                    schools.add(new JsonPrimitive(Unit.getInstitutionName().getContent()));
                    break;
                default:
                    schools.add(new JsonPrimitive(Unit.getInstitutionName().getContent()));
                    schools.add(new JsonPrimitive(phdProcess.getCollaborationType().getLocalizedName()));
                }
                phdInfo.add("schools", schools);

                phdInfo.addProperty("year", phdProcess.getConclusionDate().year().getAsShortText());

                phdInfo.addProperty("month", phdProcess.getConclusionDate().monthOfYear().getAsShortText());

                try {
                    phdInfo.addProperty("url", phdProcess.getThesisProcess().getProvisionalThesisDocument().getDownloadUrl());
                } catch (NullPointerException e) {
                }
                phdInfo.addProperty("type", "phdthesis");
                infos.add(phdInfo);
            }

        }

        for (Thesis t : Bennu.getInstance().getThesesSet()) {
            if (t.isEvaluated()) {
                JsonObject mscInfo = new JsonObject();
                mscInfo.addProperty("id", t.getExternalId());
                mscInfo.addProperty("author", t.getStudent().getPerson().getUsername());
                String title = t.getFinalFullTitle().getContent(MultiLanguageString.en);
                if (title == null) {
                    title = t.getFinalFullTitle().getContent(MultiLanguageString.pt);
                }
                mscInfo.addProperty("title", title);
                mscInfo.addProperty("year", t.getDiscussed().year().getAsShortText());
                mscInfo.addProperty("month", t.getDiscussed().monthOfYear().getAsShortText());

                JsonArray schools = new JsonArray();
                schools.add(new JsonPrimitive(Unit.getInstitutionName().getContent()));
                mscInfo.add("schools", schools);

                mscInfo.addProperty("url", t.getDissertation().getDownloadUrl());
                mscInfo.addProperty("type", "mastersthesis");
                infos.add(mscInfo);
            }
        }
        return infos.toString();
    }

    @GET
    @Path("photograph/{photoUsername}/{clientUsername}")
    public Response getPhotograph(@PathParam("photoUsername") String photoUsername,
            @PathParam("clientUsername") String clientUsername, @QueryParam("xRatio") final String xRatioParameter,
            @QueryParam("yRatio") final String yRatioParameter, @QueryParam("width") final String widthParameter,
            @QueryParam("height") final String heightParameter, @QueryParam("mode") final String modeParameter,
            @QueryParam("default") final String unavailableDefault) {

        //set users
        User user = User.findByUsername(photoUsername);
        if (user == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        Person client = null;
        if (!clientUsername.equals("NoUser")) {
            User clientUser = User.findByUsername(clientUsername);
            if (clientUser == null) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
            client = clientUser.getPerson();
        }
        int xRatio = 1, yRatio = 1, width = 100, height = 100;
        PictureMode pictureMode = PictureMode.FIT;
        //prepare arguments
        if (xRatioParameter != null) {
            xRatio = Integer.parseInt(xRatioParameter);
        }
        if (yRatioParameter != null) {
            yRatio = Integer.parseInt(yRatioParameter);
        }
        if (widthParameter != null) {
            width = Integer.parseInt(widthParameter);
        }
        if (heightParameter != null) {
            height = Integer.parseInt(heightParameter);
        }
        if (modeParameter != null) {
            pictureMode = PictureMode.valueOf(modeParameter);
        }

        Photograph photo = user.getPerson().getPersonalPhoto();
        if (photo == null) {
            if (unavailableDefault != null) {
                return unavailableDefaultProcess(xRatio, yRatio, width, height, pictureMode, unavailableDefault);
            }
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        if (user.getPerson().isPhotoAvailableToPerson(client)) {
            try {
                return Response.ok(photo.getCustomAvatar(xRatio, yRatio, width, height, pictureMode),
                        ContentType.PNG.getMimeType()).build();
            } catch (Exception e) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
        }
        if (unavailableDefault != null) {
            return unavailableDefaultProcess(xRatio, yRatio, width, height, pictureMode, unavailableDefault);
        }
        throw new WebApplicationException(Status.UNAUTHORIZED);
    }

    private Response unavailableDefaultProcess(int xRatio, int yRatio, int width, int height, PictureMode pictureMode,
            String unavailableDefault) {
        if (unavailableDefault.equals("mm")) {
            return Response.ok(Photograph.mysteryManPhoto(xRatio, yRatio, width, height, pictureMode),
                    ContentType.PNG.getMimeType()).build();
        }
        try {
            response.sendRedirect(URLDecoder.decode(unavailableDefault, Charsets.UTF_8.name()));
            return null;
        } catch (IOException e) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("role/developer/{istid}")
    public static Response addDeveloperRole(@PathParam("istid") String istid) {
        User user = User.findByUsername(istid);
        if (user != null && user.getPerson() != null) {
            if (user.getPerson().getPersonRole(RoleType.DEVELOPER) == null) {
                addDeveloper(user);
            }
        }
        return Response.status(Status.OK).build();
    }

    @Atomic(mode = TxMode.WRITE)
    public static void addDeveloper(User user) {
        user.getPerson().addPersonRoleByRoleType(RoleType.DEVELOPER);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("userAliasses")
    public String userAliasses(@QueryParam("username") final String username) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Person person = Person.readPersonByUsername(username);
        if (person != null) {
            final StringBuilder builder = new StringBuilder(username);
            if (person.getEmployee() != null) {
                addAliass(builder, person.getEmployee().getEmployeeNumber());
            }
            if (person.getStudent() != null) {
                addAliass(builder, person.getStudent().getNumber());
            }
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private void addAliass(final StringBuilder builder, final Integer aliass) {
        if (aliass != null) {
            builder.append(", ");
            builder.append(aliass);
        }
    }

}
