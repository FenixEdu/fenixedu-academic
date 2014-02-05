package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class InitializePassword {

    private static final String RESOURCE_URL = "http://localhost:8080/RESTfulExample/rest/json/metallica/post";

    @Atomic
    public static void run(User user, String password) throws InvalidPasswordServiceException, Exception {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(RESOURCE_URL);

            UserPassword userPassword = new UserPassword(user.getUsername(), password);
            String input = new Gson().toJson(userPassword);

            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

            if (response.getStatus() != 201) {
                throw new InvalidPasswordServiceException("Failed : HTTP error code : " + response.getStatus());
            }

        } catch (Exception e) {
            throw new InvalidPasswordServiceException(e.getCause());
        }
    }

    static class UserPassword {
        final String username;
        final String password;

        public UserPassword(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
