package pt.ist.fenix.external;

import java.text.ParseException;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu.FenixIstConfiguration;
import net.sourceforge.fenixedu.applicationTier.Servico.candidacy.LogFirstTimeCandidacyTimestamp;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import pt.ist.fenixframework.FenixFramework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Path("/fenix-ist/ldapSync")
public class LdapSyncServices extends BennuRestResource {

    @Context
    private HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/person/{username}")
    public Response getPersonInformation(@PathParam("username") String username) {
        checkAccessControl();
        User user = User.findByUsername(username);
        if (user == null || user.getPerson() == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return Response.ok(gson.toJson(new PersonInformationDTO(user.getPerson()))).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/person")
    public Response updatePersonInformation(String json) {
        checkAccessControl();
        PersonInformationFromUniqueCardDTO personDTO = new Gson().fromJson(json, PersonInformationFromUniqueCardDTO.class);
        Collection<Person> persons = Person.readByDocumentIdNumber(personDTO.getDocumentIdNumber());
        if (persons.isEmpty() || persons.size() > 1) {
            return Response.serverError().build();
        }

        Person person = persons.iterator().next();
        if (person.getIdDocumentType() != IDDocumentType.IDENTITY_CARD) {
            return Response.serverError().build();
        }

        try {
            personDTO.edit(person);
        } catch (ParseException e) {
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alumni/{requestOID}/{requestUUID}")
    public Response alumniIdentityCheck(@PathParam("requestOID") String requestOID, @PathParam("requestUUID") String requestUUID) {
        checkAccessControl();
        AlumniIdentityCheckRequest identityCheckRequest = FenixFramework.getDomainObject(requestOID);
        if (identityCheckRequest.getRequestToken().equals(UUID.fromString(requestUUID))) {
            JsonObject obj = new JsonObject();
            obj.addProperty("username", identityCheckRequest.getAlumni().getLoginUsername());
            return Response.ok(toJson(obj)).build();
        } else {
            return Response.status(Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/candidacy-summary-file/{user}")
    public Response getCandidacySummaryFile(@PathParam("user") String username) {
        checkAccessControl();
        final User foundUser = User.findByUsername(username);
        final StudentCandidacy candidacy =
                foundUser.getPerson().getStudent().getRegistrationsSet().iterator().next().getStudentCandidacy();
        final CandidacySummaryFile file = candidacy.getSummaryFile();

        if (file == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        LogFirstTimeCandidacyTimestamp.logTimestamp(candidacy, FirstTimeCandidacyStage.RETRIEVED_SUMMARY_PDF);
        return Response.ok(file.getContent()).build();
    }

    private void checkAccessControl() {
        boolean authorized =
                FenixConfigurationManager.getHostAccessControl().isAllowed(LdapSyncServices.class.getName(), request);
        authorized &=
                Objects.equals(request.getHeader("__username__"), FenixIstConfiguration.getConfiguration()
                        .ldapSyncServicesUsername());
        authorized &=
                Objects.equals(request.getHeader("__password__"), FenixIstConfiguration.getConfiguration()
                        .ldapSyncServicesPassword());
        if (!authorized) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
    }
}
