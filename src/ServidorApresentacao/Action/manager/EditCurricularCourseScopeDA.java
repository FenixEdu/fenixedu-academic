/*
 * Created on 21/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;

/**
 * @author lmac1
 */
public class EditCurricularCourseScopeDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
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

        dynaForm
                .set("branchId", oldInfoCurricularCourseScope.getInfoBranch().getIdInternal().toString());
        dynaForm.set("curricularSemesterId", oldInfoCurricularCourseScope.getInfoCurricularSemester()
                .getIdInternal().toString());

        Object[] args1 = { degreeCurricularPlanId };
        List result = null;
        try {
            result = (List) ServiceUtils.executeService(userView, "ReadBranchesByDegreeCurricularPlan",
                    args1);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping
                    .findForward("readDegree"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (result == null)
            throw new NonExistingActionException("message.insert.degreeCurricularCourseScope.error",
                    mapping.findForward("readCurricularCourse"));

        //	creation of bean of InfoBranches for use in jsp
        List branchesList = new ArrayList();
        InfoBranch infoBranch;
        Iterator iter = result.iterator();
        String label, value;
        while (iter.hasNext()) {
            infoBranch = (InfoBranch) iter.next();
            value = infoBranch.getIdInternal().toString();
            label = infoBranch.getCode() + " - " + infoBranch.getName();
            branchesList.add(new LabelValueBean(label, value));
        }

        // obtain execution periods to show in jsp
        List infoExecutionPeriods = null;
        try {
            infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods",
                    null);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionPeriods == null)
            throw new NonExistingActionException("message.insert.executionPeriods.error", mapping
                    .findForward("readCurricularCourse"));

        List executionPeriodsLabels = new ArrayList();
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        Iterator iterExecutionPeriods = infoExecutionPeriods.iterator();
        String labelExecutionPeriod, valueExecutionPeriod;
        while (iterExecutionPeriods.hasNext()) {
            infoExecutionPeriod = (InfoExecutionPeriod) iterExecutionPeriods.next();
            valueExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
            labelExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
            executionPeriodsLabels.add(new LabelValueBean(labelExecutionPeriod, valueExecutionPeriod));
        }

        request.setAttribute("executionPeriodsLabels", executionPeriodsLabels);
        request.setAttribute("branchesList", branchesList);

        return mapping.findForward("editCurricularCourseScope");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldCurricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));

        InfoCurricularCourseScope newInfoCurricularCourseScope = new InfoCurricularCourseScope();

        String curricularSemesterIdString = (String) dynaForm.get("curricularSemesterId");
        String branchIdString = (String) dynaForm.get("branchId");
        String beginDateString = (String) dynaForm.get("beginDate");

        Integer curricularSemesterId = new Integer(curricularSemesterIdString);

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
        infoCurricularSemester.setIdInternal(curricularSemesterId);
        newInfoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

        Integer branchId = new Integer(branchIdString);

        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setIdInternal(branchId);
        newInfoCurricularCourseScope.setInfoBranch(infoBranch);
        newInfoCurricularCourseScope.setIdInternal(oldCurricularCourseScopeId);

        if (beginDateString.compareTo("") != 0) {
            Calendar beginDateCalendar = Calendar.getInstance();
            beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
            newInfoCurricularCourseScope.setBeginDate(beginDateCalendar);
        }

        Object args[] = { newInfoCurricularCourseScope };

        try {
            ServiceUtils.executeService(userView, "EditCurricularCourseScope", args);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("readCurricularCourse"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.curricular.course.scope");
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("infoCurricularCourseScope", newInfoCurricularCourseScope);

        return mapping.findForward("readCurricularCourse");
    }
}