/*
 * Created on 1/Ago/2003
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
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
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

@Mapping(module = "manager", path = "/readDegreeCurricularPlan", input = "/readDegree.do", scope = "session")
@Forwards(value = {
		@Forward(name = "viewDegreeCurricularPlan", path = "/manager/readDegreeCurricularPlan_bd.jsp", tileProperties = @Tile(navLocal = "/manager/degreeNavLocalManager.jsp")),
		@Forward(name = "readDegree", path = "/readDegree.do") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class, key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ReadDegreeCurricularPlanAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer degreeCurricularPlanId = null;
	if (request.getParameter("degreeCurricularPlanId") != null) {
	    degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
	}

	InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

	try {
	    infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService("ReadDegreeCurricularPlan",
		    new Object[] { degreeCurricularPlanId });

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	// in case the degreeCurricularPlan really exists
	List curricularCourses = null;
	try {
	    curricularCourses = ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanId);

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	Collections.sort(curricularCourses, new BeanComparator("name"));
	Object[] args = { degreeCurricularPlanId };
	List executionDegrees = null;
	try {
	    // executionDegrees =
	    // ReadExecutionDegreesByDegreeCurricularPlan.run(degreeCurricularPlanId);
	    executionDegrees = (List) ServiceUtils.executeService("ReadExecutionDegreesByDegreeCurricularPlanID", args);

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	Collections.sort(executionDegrees, new BeanComparator("infoExecutionYear.year"));

	request.setAttribute("curricularCoursesList", curricularCourses);
	request.setAttribute("executionDegreesList", executionDegrees);
	request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
	return mapping.findForward("viewDegreeCurricularPlan");
    }
}