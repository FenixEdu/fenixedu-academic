/*
 * Created on 5/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EndCurricularCourseScope;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Fernanda Quitério 27/10/2003
 * 
 */
@Mapping(module = "manager", path = "/endCurricularCourseScope", input = "/endCurricularCourseScope.do?method=prepareEnd&page=0",
        attribute = "curricularCourseScopeForm", formBean = "curricularCourseScopeForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
        @Forward(name = "endCurricularCourseScope", path = "/manager/endCurricularCourseScope_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException.class,
                key = "resources.Action.exceptions.InvalidSituationActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException.class,
                key = "resources.Action.exceptions.InvalidArgumentsActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EndCurricularCourseScopeDA extends FenixDispatchAction {

    public ActionForward prepareEnd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoCurricularCourseScope oldInfoCurricularCourseScope = null;

        try {
            oldInfoCurricularCourseScope = ReadCurricularCourseScope.run(request.getParameter("curricularCourseScopeId"));
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourseScope",
                    mapping.findForward("readCurricularCourse"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (oldInfoCurricularCourseScope.getBeginDate() != null) {
            dynaForm.set("beginDate", Data.format2DayMonthYear(oldInfoCurricularCourseScope.getBeginDate().getTime(), "/"));
        }

        request.setAttribute("infoCurricularCourseScope", oldInfoCurricularCourseScope);
        return mapping.findForward("endCurricularCourseScope");
    }

    public ActionForward end(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        String oldCurricularCourseScopeId = request.getParameter("curricularCourseScopeId");

        InfoCurricularCourseScopeEditor newInfoCurricularCourseScope = new InfoCurricularCourseScopeEditor();
        newInfoCurricularCourseScope.setExternalId(oldCurricularCourseScopeId);

        String beginDateString = (String) dynaForm.get("beginDate");
        String endDateString = (String) dynaForm.get("endDate");

        if (beginDateString.compareTo("") != 0) {
            Calendar beginDateCalendar = Calendar.getInstance();
            beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
            newInfoCurricularCourseScope.setBeginDate(beginDateCalendar);
        }

        if (endDateString.compareTo("") != 0) {
            Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(Data.convertStringDate(endDateString, "/"));
            newInfoCurricularCourseScope.setEndDate(endDateCalendar);
        }

        try {
            EndCurricularCourseScope.run(newInfoCurricularCourseScope);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readCurricularCourse"), ex);
        } catch (InvalidArgumentsServiceException ex) {
            throw new InvalidArgumentsActionException("error.manager.wrongDates", ex);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage(), fenixServiceException);
        }

        return mapping.findForward("readCurricularCourse");
    }
}