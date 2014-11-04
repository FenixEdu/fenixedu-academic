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
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordInitializationException;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.util.FenixConfigurationManager.ConfigurationProperties;

import org.fenixedu.bennu.core.domain.User;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;

public class InitializePassword {

    private static final Client HTTP_CLIENT = ClientBuilder.newClient();
    private static final String ALREADY_INITIALIZED = "internationalRegistration.error.alreadyInitialized";
    private static final String ERROR_REGISTERING = "internationalRegistration.error.registering";

    public static void run(User user, String password) throws PasswordInitializationException {
        Form form = new Form().param("istid", user.getUsername()).param("password", password);
        Response post =
                HTTP_CLIENT.target(FenixConfigurationManager.getConfiguration().getWebServicesInternationalRegistrationUrl())
                        .request(MediaType.APPLICATION_JSON).header("Authorization", getServiceAuth()).post(Entity.form(form));

        OutputBean output = null;
        if (post.getStatus() == 200) {
            String entity = post.readEntity(String.class);
            output = new Gson().fromJson((String) entity, OutputBean.class);
        }

        if (output == null || output.getErrno() != 0) {
            String errorMessage = output.getErrno() == 1 ? ERROR_REGISTERING : ALREADY_INITIALIZED;
            System.out.println(output.getErrno() + " : " + output.getError());
            throw new PasswordInitializationException(errorMessage);
        }

    }

    private static String getServiceAuth() {
        ConfigurationProperties config = FenixConfigurationManager.getConfiguration();
        String userpass =
                config.getWebServicesInternationalRegistrationUsername() + ":"
                        + config.getWebServicesInternationalRegistrationPassword();
        String encoding = new String(BaseEncoding.base64().encode(userpass.getBytes()));
        return "Basic " + encoding;
    }

    static class OutputBean {
        private Integer errno;
        private String error;

        public Integer getErrno() {
            return errno;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public void setErrno(Integer errno) {
            this.errno = errno;
        }
    }
}
