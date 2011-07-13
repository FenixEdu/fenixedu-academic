package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.CheckIfUserCanAccessFile;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author naat
 * 
 */

@Mapping(module = "external", path = "/userPermissionCheck", scope = "request", parameter = "method")
public class UserPermissionCheck extends ExternalInterfaceDispatchAction {

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    private static final String SUCCESS_CODE = "SUCCESS";

    public ActionForward canUserAccessFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!HostAccessControl.isAllowed(this, request)) {
	    writeResponse(response, NOT_AUTHORIZED_CODE, "");
	} else {
	    String dspaceBitstreamIdentification = request.getParameter("dspaceBitstreamIdentification");
	    String username = request.getParameter("username");

	    try {
		Boolean result = (Boolean) CheckIfUserCanAccessFile.run(username, dspaceBitstreamIdentification);

		writeResponse(response, SUCCESS_CODE, result.toString());

	    } catch (Exception e) {
		writeResponse(response, UNEXPECTED_ERROR_CODE, "");
	    }
	}

	return null;
    }

}