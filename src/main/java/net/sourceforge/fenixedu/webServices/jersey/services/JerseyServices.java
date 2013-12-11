package net.sourceforge.fenixedu.webServices.jersey.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublication.PreferredComparator;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.webServices.ExportPublications;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Path("/jersey/services")
public class JerseyServices {

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
        for (final User user : Bennu.getInstance().getUsersSet()) {
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
        for (final User user : Bennu.getInstance().getUsersSet()) {
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
    @Path("preferred")
    @Produces(MediaType.APPLICATION_JSON)
    public String readPreferred() {
        JsonArray array = new JsonArray();
        for (Party party : Bennu.getInstance().getPartysSet()) {
            if (party instanceof Person) {
                Person person = (Person) party;
                if (person.getUsername() != null && !person.getPreferredPublicationSet().isEmpty()) {
                    SortedSet<ResearchResultPublication> results = new TreeSet<>(new PreferredComparator(person));
                    for (PreferredPublication preferred : person.getPreferredPublicationSet()) {
                        results.add(preferred.getPreferredPublication());
                    }
                    JsonObject researcher = new JsonObject();
                    researcher.addProperty("istID", person.getUsername());
                    JsonArray preferences = new JsonArray();
                    int count = 5;
                    for (ResearchResultPublication publication : results) {
                        if (count-- == 0) {
                            break;
                        }
                        preferences.add(new JsonPrimitive(publication.getExternalId()));
                    }
                    researcher.add("preference", preferences);
                    array.add(researcher);
                }
            }
        }
        return array.toString();
    }

    @GET
    @Path("researchers")
    @Produces(MediaType.APPLICATION_JSON)
    public String readResearchers() {
        JSONArray researchers = new JSONArray();

        final Map<User, Set<Unit>> researchUnitMap = new HashMap<User, Set<Unit>>();
        for (final User user : Bennu.getInstance().getUsersSet()) {
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
        if (!researchUnitMap.containsKey(user)) {
            researchUnitMap.put(user, new HashSet<Unit>());
        }
        researchUnitMap.get(user).add(unit);
    }

    @GET
    @Path("publications")
    @Produces(MediaType.APPLICATION_XML)
    public byte[] readPublications() {
        return new ExportPublications().harverst();
    }

    @GET
    @Path("publication")
    public Response publicationFile(@QueryParam("storageId") String storageId) {
        File file = File.readByExternalStorageIdentification(storageId);
        if (file != null) {
            return Response.ok().entity(file.getStream()).build();
        }
        throw new WebApplicationException(Status.NO_CONTENT);
    }

    @GET
    @Path("publication/info")
    @Produces(MediaType.APPLICATION_JSON)
    public String publicationInfo(@QueryParam("storageId") String storageId) {
        File file = File.readByExternalStorageIdentification(storageId);
        if (file != null) {
            JsonObject info = new JsonObject();
            info.addProperty("filename", file.getFilename());
            info.addProperty("mimeType", file.getMimeType());
            if (file instanceof ResearchResultDocumentFile) {
                info.addProperty("group", ((ResearchResultDocumentFile) file).getFileResultPermittedGroupType().name());
            } else if (file instanceof ThesisFile) {
                info.addProperty("group", ((ThesisFile) file).getDissertationThesis().getVisibility().name());
            } else if (file instanceof PhdProgramProcessDocument) {
                info.addProperty("group", "RESEARCHER");
            }
            return info.toString();
        }
        throw new WebApplicationException(Status.NO_CONTENT);
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
                String title = t.getFinalFullTitle().getContent(Language.en);
                if (title == null) {
                    title = t.getFinalFullTitle().getContent(Language.pt);
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
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("photograph/{photoUsername}/{clientUsername}")
    public byte[] getPhotograph(@PathParam("photoUsername") String photoUsername,
            @PathParam("clientUsername") String clientUsername, @QueryParam("xRatio") final String xRatioParameter,
            @QueryParam("yRatio") final String yRatioParameter, @QueryParam("width") final String widthParameter,
            @QueryParam("height") final String heightParameter, @QueryParam("mode") final String modeParameter) {

        Person photoPerson;
        Person clientPerson = null;
        //set users
        try {
            photoPerson = User.findByUsername(photoUsername).getPerson();
            if (!clientUsername.equals("NoUser")) {
                clientPerson = User.findByUsername(clientUsername).getPerson();
            }
        } catch (NullPointerException e) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        if (photoPerson.isPhotoAvailableToPerson(clientPerson)) {
            Photograph photo = photoPerson.getPersonalPhoto();
            if (photo != null) {
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

                try {
                    return photo.getCustomAvatar(xRatio, yRatio, width, height, pictureMode);
                } catch (Exception e) {
                    throw new WebApplicationException(Status.BAD_REQUEST);
                }
            }
        }
        throw new WebApplicationException(Status.UNAUTHORIZED);
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

}
