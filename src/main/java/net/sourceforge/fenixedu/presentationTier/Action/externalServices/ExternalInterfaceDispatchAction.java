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
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.json.simple.JSONObject;

public abstract class ExternalInterfaceDispatchAction extends FenixDispatchAction {

    protected static final String SUCCESS_CODE = "SUCCESS";

    protected static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    protected static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    protected static final String SERVICE_NOT_EXECUTED = "SERVICE_NOT_EXECUTED";

    protected void writeResponse(HttpServletResponse response, String responseCode, String responseMessage) throws IOException {
        response.setContentType("text/html");

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(responseCode.getBytes());
        outputStream.write("\n".getBytes());
        outputStream.write(responseMessage.getBytes());
    }

    protected void writeJSONObject(HttpServletResponse response, final JSONObject jsonObject) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        final ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonObject.toJSONString().getBytes());
        outputStream.close();
    }
}
