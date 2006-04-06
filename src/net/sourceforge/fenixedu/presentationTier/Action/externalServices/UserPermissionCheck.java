package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author naat
 * 
 */

public class UserPermissionCheck extends FenixDispatchAction {

    public ActionForward canUserAccessFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        // TODO: logic to check permissions goes here

        response.setContentType("text/html");

        OutputStream outputStream = response.getOutputStream();

        outputStream.write("true".getBytes());

        return null;
    }
}