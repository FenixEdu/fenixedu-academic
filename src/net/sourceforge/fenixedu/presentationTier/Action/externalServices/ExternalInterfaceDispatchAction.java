package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class ExternalInterfaceDispatchAction extends FenixDispatchAction {

    protected void writeResponse(HttpServletResponse response, String responseCode,
            String responseMessage) throws IOException {
        response.setContentType("text/html");

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(responseCode.getBytes());
        outputStream.write("\n".getBytes());
        outputStream.write(responseMessage.getBytes());
    }

}
