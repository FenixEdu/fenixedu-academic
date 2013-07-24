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
