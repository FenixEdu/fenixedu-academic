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

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

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
        JSONArray users = new JSONArray();
        for (final User user : Bennu.getInstance().getUserSet()) {
            if (!StringUtils.isEmpty(user.getUsername()) && user.getPerson() != null) {
                JSONObject json = new JSONObject();
                json.put("istId", user.getUsername());
                json.put("name", user.getPerson().getName());
                if (user.getPerson().getEmailForSendingEmails() != null) {
                    json.put("email", user.getPerson().getEmailForSendingEmails());
                }
                users.add(json);
            }
        }
        return users.toJSONString();
    }

    @GET
    @Path("researchers")
    @Produces(MediaType.APPLICATION_JSON)
    public String readResearchers() {
        JSONArray researchers = new JSONArray();

        final Map<User, Set<Unit>> researchUnitMap = new HashMap<User, Set<Unit>>();
        for (final User user : Bennu.getInstance().getUserSet()) {
            Person person = user.getPerson();
            if (!StringUtils.isEmpty(user.getUsername()) && person != null
                    && (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.RESEARCHER))) {
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
            final Person person = user.getPerson();
            if (!StringUtils.isEmpty(user.getUsername()) && person != null
                    && (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.RESEARCHER))) {
                JSONObject json = new JSONObject();
                json.put("istId", user.getUsername());

                JSONArray array = new JSONArray();
                for (Unit unit : entry.getValue()) {
                    JSONObject element = new JSONObject();
                    if (!Strings.isNullOrEmpty(unit.getAcronym())) {
                        element.put("acronym", unit.getAcronym());
                    }
                    if (!Strings.isNullOrEmpty(unit.getName())) {
                        element.put("name", unit.getName());
                    }
                    array.add(element);
                }

                json.put("department", array);
                researchers.add(json);
            }
        }
        return researchers.toJSONString();
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
        final Person person = Person.readPersonByUsername(username);
        final Student student = person.getStudent();
        return student != null ? student.readActiveStudentInfoForJobBank() : StringUtils.EMPTY;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readStudentInfoForJobBank")
    public static String readStudentInfoForJobBank(@QueryParam("username") final String username) {
        final Person person = Person.readPersonByUsername(username);
        final Student student = person.getStudent();
        return student != null ? student.readStudentInfoForJobBank() : StringUtils.EMPTY;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readAllStudentsInfoForJobBank")
    public static String readAllStudentsInfoForJobBank() {
        return Registration.readAllStudentsInfoForJobBank();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readBolonhaDegrees")
    public static String readBolonhaDegrees() {
        JSONArray infos = new JSONArray();
        for (Degree degree : Degree.readBolonhaDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {
                JSONObject degreeInfo = new JSONObject();
                degreeInfo.put("degreeOid", degree.getExternalId());
                degreeInfo.put("name", degree.getPresentationName());
                degreeInfo.put("degreeType", degree.getDegreeTypeName());
                infos.add(degreeInfo);
            }
        }
        return infos.toJSONString();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("readThesis")
    public static String readPhdThesis() {
        JSONArray infos = new JSONArray();

        for (PhdIndividualProgramProcessNumber phdProcessNumber : Bennu.getInstance().getPhdIndividualProcessNumbersSet()) {
            PhdIndividualProgramProcess phdProcess = phdProcessNumber.getProcess();
            if (phdProcess.isConcluded()) {
                JSONObject phdInfo = new JSONObject();
                phdInfo.put("id", phdProcess.getExternalId());
                phdInfo.put("author", phdProcess.getPerson().getIstUsername());
                phdInfo.put("title", phdProcess.getThesisTitle());

                JSONArray schools = new JSONArray();
                switch (phdProcess.getCollaborationType()) {
                case NONE:
                case WITH_SUPERVISION:
                case ERASMUS_MUNDUS:
                case OTHER:
                    schools.add(Unit.getInstitutionName());
                    break;
                default:
                    schools.add(Unit.getInstitutionName());
                    schools.add(phdProcess.getCollaborationType().getLocalizedName());
                }
                phdInfo.put("schools", schools);

                phdInfo.put("year", phdProcess.getConclusionDate().year().getAsShortText());

                phdInfo.put("month", phdProcess.getConclusionDate().monthOfYear().getAsShortText());

                try {
                    phdInfo.put("url", phdProcess.getThesisProcess().getProvisionalThesisDocument().getDownloadUrl());
                } catch (NullPointerException e) {
                }
                phdInfo.put("type", "phdthesis");
                infos.add(phdInfo);
            }

        }

        for (Thesis t : Bennu.getInstance().getThesesSet()) {
            if (t.isEvaluated()) {
                JSONObject mscInfo = new JSONObject();
                mscInfo.put("id", t.getExternalId());
                mscInfo.put("author", t.getStudent().getPerson().getIstUsername());
                String title = t.getFinalFullTitle().getContent(MultiLanguageString.en);
                if (title == null) {
                    title = t.getFinalFullTitle().getContent(MultiLanguageString.pt);
                }
                mscInfo.put("title", title);
                mscInfo.put("year", t.getDiscussed().year().getAsShortText());
                mscInfo.put("month", t.getDiscussed().monthOfYear().getAsShortText());

                JSONArray schools = new JSONArray();
                schools.add(Unit.getInstitutionName());
                mscInfo.put("schools", schools);

                mscInfo.put("url", t.getDissertation().getDownloadUrl());
                mscInfo.put("type", "mastersthesis");
                infos.add(mscInfo);
            }
        }
        return infos.toJSONString();

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
