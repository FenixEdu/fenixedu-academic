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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "external", path = "/connect", scope = "request", parameter = "method")
public class ISTConnectDA extends ExternalInterfaceDispatchAction {

    private boolean doLogin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final String username = (String) getFromRequest(request, "username");
        final String password = (String) getFromRequest(request, "password");
        final String usernameProp = FenixConfigurationManager.getConfiguration().getExternalServicesISTConnectUsername();
        final String passwordProp = FenixConfigurationManager.getConfiguration().getExternalServicesISTConnectPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(usernameProp)
                || StringUtils.isEmpty(passwordProp)) {
            return false;
        }
        return username.equals(usernameProp) && password.equals(passwordProp);
    }

    public ActionForward getPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (doLogin(mapping, actionForm, request, response)) {
            final String istID = (String) getFromRequest(request, "istID");
            final Person person = Person.readPersonByUsername(istID);
            final JSONObject jsonObject = DomainObjectJSONSerializer.getDomainObject(person);
            writeJSONObject(response, jsonObject);
        } else {
            response.sendError(404, "Not authorized");
        }
        return null;
    }

    public ActionForward getBasicUserData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (doLogin(mapping, actionForm, request, response)) {
            final String istID = (String) getFromRequest(request, "istID");
            final Person person = Person.readPersonByUsername(istID);

            final JSONObject jsonObject = new JSONObject();

            if (person != null) {
                jsonObject.put("externalId", person.getExternalId());
                jsonObject.put("className", person.getClass().getName());
                jsonObject.put("email", person.getEmailForSendingEmails());

                jsonObject.put("partyName", person.getPartyName().toString());
                jsonObject.put("nickname", person.getNickname());

                JSONArray jsonList = new JSONArray();
                for (final net.sourceforge.fenixedu.domain.Role role : person.getPersonRolesSet()) {
                    jsonList.add(role.getRoleType().getName());
                }
                jsonObject.put("roles", jsonList);
            }

            writeJSONObject(response, jsonObject);
        } else {
            response.sendError(404, "Not authorized");
        }
        return null;
    }

    private void writeJSONObject(HttpServletResponse response, final JSONArray jsonObject) throws IOException {
        final ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonObject.toJSONString().getBytes());
        outputStream.close();
    }

    public ActionForward getExternalIds(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String externalIds = (String) getFromRequest(request, "externalIds");
        if (doLogin(mapping, actionForm, request, response)) {
            JSONParser parser = new JSONParser();
            final JSONArray extIdsJSONArray = (JSONArray) parser.parse(externalIds);
            final JSONArray jsonArrayResult = new JSONArray();
            for (Object externalId : extIdsJSONArray) {
                jsonArrayResult.add(DomainObjectJSONSerializer.getDomainObject(FenixFramework
                        .getDomainObject((String) externalId)));
            }
            writeJSONObject(response, jsonArrayResult);
        } else {
            response.sendError(404, "Not authorized");
        }
        return null;
    }
}
