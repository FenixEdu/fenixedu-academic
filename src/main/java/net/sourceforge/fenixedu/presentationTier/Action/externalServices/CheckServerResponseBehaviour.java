package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/checkServerResponseBehaviour", scope = "request", parameter = "method")
public class CheckServerResponseBehaviour extends ExternalInterfaceDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(CheckServerResponseBehaviour.class);

    public ActionForward check(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String output = request.getParameter("output");
        logger.info("BR: " + output);

        final String timeout = request.getParameter("timeout");
        if (timeout != null && timeout.length() > 0 && StringUtils.isNumeric(timeout)) {
            final long t = Long.parseLong(timeout);
            Thread.sleep(t);
        }

        final OutputStream outputStream = response.getOutputStream();
        outputStream.write(output.getBytes());
        outputStream.close();

        logger.info("AR: " + output);

        return null;
    }

}