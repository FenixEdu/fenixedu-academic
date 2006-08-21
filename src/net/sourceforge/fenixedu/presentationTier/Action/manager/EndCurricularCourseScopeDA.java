/*
 * Created on 5/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 27/10/2003
 *  
 */
public class EndCurricularCourseScopeDA extends FenixDispatchAction {

    public ActionForward prepareEnd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer curricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));
        InfoCurricularCourseScope oldInfoCurricularCourseScope = null;

        Object args[] = { curricularCourseScopeId };

        try {
            oldInfoCurricularCourseScope = (InfoCurricularCourseScope) ServiceUtils.executeService(
                    userView, "ReadCurricularCourseScope", args);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourseScope", mapping
                    .findForward("readCurricularCourse"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (oldInfoCurricularCourseScope.getBeginDate() != null)
            dynaForm.set("beginDate", Data.format2DayMonthYear(oldInfoCurricularCourseScope
                    .getBeginDate().getTime(), "/"));

        request.setAttribute("infoCurricularCourseScope", oldInfoCurricularCourseScope);
        return mapping.findForward("endCurricularCourseScope");
    }

    public ActionForward end(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldCurricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));

        InfoCurricularCourseScopeEditor newInfoCurricularCourseScope = new InfoCurricularCourseScopeEditor();
        newInfoCurricularCourseScope.setIdInternal(oldCurricularCourseScopeId);

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
        Object args[] = { newInfoCurricularCourseScope };

        try {
            ServiceUtils.executeService(userView, "EndCurricularCourseScope", args);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("readCurricularCourse"), ex);
        } catch (InvalidArgumentsServiceException ex) {
            throw new InvalidArgumentsActionException("error.manager.wrongDates", ex);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage(), fenixServiceException);
        }

        return mapping.findForward("readCurricularCourse");
    }
}