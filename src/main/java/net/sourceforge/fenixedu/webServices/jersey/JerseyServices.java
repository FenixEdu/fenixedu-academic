package net.sourceforge.fenixedu.webServices.jersey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.webServices.ExportPublications;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Strings;

@Path("/services")
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
        for (final User user : RootDomainObject.getInstance().getUsersSet()) {
            if (!StringUtils.isEmpty(user.getUserUId())) {
                final Person person = user.getPerson();
                if (roles.length == 0 || person.hasAnyRole(roles)) {
                    builder.append(user.getUserUId());
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
        for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
            if (party.isPerson()) {
                final Person person = (Person) party;
                final String email = person.getEmailForSendingEmails();
                if (email != null) {
                    final User user = person.getUser();
                    if (user != null) {
                        final String username = user.getUserUId();
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
        for (final User user : RootDomainObject.getInstance().getUsersSet()) {
            if (!StringUtils.isEmpty(user.getUserUId()) && user.hasPerson()) {
                JSONObject json = new JSONObject();
                json.put("istId", user.getUserUId());
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
        for (final User user : RootDomainObject.getInstance().getUsersSet()) {
            Person person = user.getPerson();
            if (!StringUtils.isEmpty(user.getUserUId()) && person != null
                    && (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.RESEARCHER))) {
                JSONObject json = new JSONObject();
                json.put("istId", user.getUserUId());
                Set<Unit> units = new HashSet<>();
                if (person.getEmployee() != null) {
                    Unit unit = person.getEmployee().getCurrentWorkingPlace();
                    if (unit != null) {
                        units.add(unit);
                    }
                    if (person.getResearcher() != null && person.getResearcher().isActiveContractedResearcher()) {
                        Collection<? extends Accountability> accountabilities =
                                person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
                        for (final Accountability accountability : accountabilities) {
                            if (accountability.isActive()) {
                                unit = (Unit) accountability.getParentParty();
                                if (unit != null) {
                                    units.add(unit);
                                }
                            }
                        }
                    }
                }
                JSONArray array = new JSONArray();
                for (Unit unit : units) {
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

    @GET
    @Path("publications")
    @Produces(MediaType.APPLICATION_XML)
    public byte[] readPublications() {
        return new ExportPublications().harverst();
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

        for (PhdIndividualProgramProcessNumber phdProcessNumber : RootDomainObject.getInstance().getPhdIndividualProcessNumbers()) {
            PhdIndividualProgramProcess phdProcess = phdProcessNumber.getProcess();
            if (phdProcess.isConcluded()) {
                JSONObject phdInfo = new JSONObject();
                phdInfo.put("id", phdProcess.getExternalId());
                phdInfo.put("author", phdProcess.getPerson().getUsername());
                phdInfo.put("title", phdProcess.getThesisTitle());

                JSONArray schools = new JSONArray();
                switch (phdProcess.getCollaborationType()) {
                case NONE:
                case WITH_SUPERVISION:
                case ERASMUS_MUNDUS:
                case OTHER:
                    schools.add("Instituto Superior Técnico");
                    break;
                default:
                    schools.add("Instituto Superior Técnico");
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

        for (Thesis t : RootDomainObject.getInstance().getTheses()) {
            if (t.isEvaluated()) {
                JSONObject mscInfo = new JSONObject();
                mscInfo.put("author", t.getStudent().getPerson().getUsername());
                String title = t.getFinalFullTitle().getContent(Language.en);
                if (title == null) {
                    title = t.getFinalFullTitle().getContent(Language.pt);
                }
                mscInfo.put("title", title);
                mscInfo.put("year", t.getDiscussed().year().getAsShortText());
                mscInfo.put("month", t.getDiscussed().monthOfYear().getAsShortText());

                JSONArray schools = new JSONArray();
                schools.add("Instituto Superior Técnico");
                mscInfo.put("schools", schools);

                mscInfo.put("url", t.getDissertation().getDownloadUrl());
                mscInfo.put("type", "mastersthesis");
                infos.add(mscInfo);
            }
        }
        return infos.toJSONString();

    }
}
