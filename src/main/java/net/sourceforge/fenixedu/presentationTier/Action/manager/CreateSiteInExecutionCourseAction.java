/*
 * Created on 27/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateSiteInExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author lmac1
 */
@Mapping(module = "manager", path = "/createSite", input = "/readCurricularCourse.do", scope = "request")
@Forwards(value = { @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class CreateSiteInExecutionCourseAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));

        try {

            CreateSiteInExecutionCourse.run(executionCourseId);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        // TODO:ver qd nao exeiste curricularcourse
        return mapping.findForward("readCurricularCourse");
    }

}