package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/checkServerResponseBehaviour", scope = "request", parameter = "method")
public class CheckServerResponseBehaviour extends ExternalInterfaceDispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String output = request.getParameter("output");
        System.out.println("BR: " + output);

        final String timeout = request.getParameter("timeout");
        if (timeout != null && timeout.length() > 0 && StringUtils.isNumeric(timeout)) {
            final long t = Long.parseLong(timeout);
            Thread.sleep(t);
        }

        final OutputStream outputStream = response.getOutputStream();
        outputStream.write(output.getBytes());
        outputStream.close();

        System.out.println("AR: " + output);

        return null;
    }

}