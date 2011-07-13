/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCoursesByCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadInterminatedCurricularCourseScopes;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
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
@Mapping(module = "manager", path = "/readCurricularCourse", input = "/readDegreeCurricularPlan.do", scope = "session")
@Forwards(value = {
		@Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do"),
		@Forward(name = "viewCurricularCourse", path = "/manager/readCurricularCourse_bd.jsp", tileProperties = @Tile(navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")) })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class, key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ReadCurricularCourseAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();
	Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

	request.setAttribute("degreeId", request.getParameter("degreeId"));
	request.setAttribute("degreeCurricularPlanId", request.getParameter("degreeCurricularPlanId"));
	request.setAttribute("curricularCourseId", curricularCourseId);

	InfoCurricularCourse infoCurricularCourse = null;

	try {
	    infoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService("ReadCurricularCourse",
		    new Object[] { curricularCourseId });

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	// in case the curricular course really exists
	List executionCourses = null;
	try {
	    executionCourses = ReadExecutionCoursesByCurricularCourse.run(curricularCourseId);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	if (executionCourses != null)
	    Collections.sort(executionCourses, new BeanComparator("nome"));

	List curricularCourseScopes = new ArrayList();
	try {
	    curricularCourseScopes = (List) ReadInterminatedCurricularCourseScopes.run(curricularCourseId);

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	if (curricularCourseScopes != null) {
	    ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
	    comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
	    Collections.sort(curricularCourseScopes, comparatorChain);
	}

	if (infoCurricularCourse.getBasic().booleanValue())
	    request.setAttribute("basic", "");

	request.setAttribute("executionCoursesList", executionCourses);

	request.setAttribute("infoCurricularCourse", infoCurricularCourse);
	request.setAttribute("curricularCourseScopesList", curricularCourseScopes);
	return mapping.findForward("viewCurricularCourse");
    }
}