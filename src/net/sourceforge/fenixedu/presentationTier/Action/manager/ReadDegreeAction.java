/*
 * Created on 15/Mai/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlansByDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
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
 * @author lmac1
 */

@Mapping(module = "manager", path = "/readDegree", input = "/readDegrees.do", scope = "session")
@Forwards(value = {
		@Forward(name = "viewDegree", path = "/manager/readDegree_bd.jsp"),
		@Forward(name = "readDegrees", path = "/readDegrees.do") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class, key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ReadDegreeAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer degreeId = null;
	if (request.getParameter("degreeId") != null) {
	    degreeId = new Integer(request.getParameter("degreeId"));
	}
	InfoDegree degree = null;

	try {
	    degree = (InfoDegree) ReadDegree.run(degreeId);

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("message.nonExistingDegree", "", e);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	// in case the degree really exists
	List degreeCurricularPlans = null;

	try {
	    degreeCurricularPlans = ReadDegreeCurricularPlansByDegree.run(degreeId);

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	Collections.sort(degreeCurricularPlans);
	request.setAttribute("infoDegree", degree);
	request.setAttribute("curricularPlansList", degreeCurricularPlans);
	return mapping.findForward("viewDegree");
    }
}