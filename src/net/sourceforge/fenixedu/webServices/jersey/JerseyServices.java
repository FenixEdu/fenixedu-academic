package net.sourceforge.fenixedu.webServices.jersey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("readAllProtocols")
    public static String readAllProtocols() {
        JSONArray array = new JSONArray();
        for (Protocol protocol : RootDomainObject.getInstance().getProtocols()) {
            array.add(protocol.getExternalId());
        }
        return array.toJSONString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("readProtocol")
    public static String readProtocol(@QueryParam("id") String externalId) {
        Protocol protocol = Protocol.fromExternalId(externalId);
        return protocol.readProtocol().toJSONString();
    }

}
