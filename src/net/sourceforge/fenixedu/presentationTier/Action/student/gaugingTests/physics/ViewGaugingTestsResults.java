/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.gaugingTests.physics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.gaugingTests.physics.readGaugingTestsResults;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

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
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 26/Nov/2003
 * 
 */
@Mapping(module = "student", path = "/studentGaugingTestResults", scope = "request")
@Forwards(value = { @Forward(name = "viewGaugingTestsResults", path = "definition.gaugingTestResults") })
public class ViewGaugingTestsResults extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = getUserView(request);

	try {
	    InfoGaugingTestResult result = (InfoGaugingTestResult) readGaugingTestsResults.run(userView);
	    request.setAttribute("gaugingTestResult", result);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("viewGaugingTestsResults");
    }

}