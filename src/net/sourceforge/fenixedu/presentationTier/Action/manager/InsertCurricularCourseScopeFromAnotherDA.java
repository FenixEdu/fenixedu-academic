/*
 * Created on 5/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeFromAnotherDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

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
        dynaForm.set("anotation",oldInfoCurricularCourseScope.getAnotation());

        // obtain branches to show in jsp
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
        List<InfoExecutionPeriod> infoExecutionPeriods = null;
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
        String labelExecutionPeriod, valueExecutionPeriod;
        for (final InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
            valueExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
            labelExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
            executionPeriodsLabels.add(new LabelValueBean(labelExecutionPeriod, valueExecutionPeriod));
        }

        request.setAttribute("executionPeriodsLabels", executionPeriodsLabels);
        request.setAttribute("branchesList", branchesList);

        return mapping.findForward("insertCurricularCourseScope");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoCurricularCourseScopeEditor newInfoCurricularCourseScope = new InfoCurricularCourseScopeEditor();

        String curricularSemesterIdString = (String) dynaForm.get("curricularSemesterId");
        String branchIdString = (String) dynaForm.get("branchId");
        String beginDateString = (String) dynaForm.get("beginDate");

        Integer curricularSemesterId = new Integer(curricularSemesterIdString);
        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester(rootDomainObject.readCurricularSemesterByOID(curricularSemesterId));
        newInfoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

        Integer branchId = new Integer(branchIdString);
        InfoBranch infoBranch = new InfoBranch(rootDomainObject.readBranchByOID(branchId));
        newInfoCurricularCourseScope.setInfoBranch(infoBranch);
        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.
				readDegreeModuleByOID(Integer.valueOf(request.getParameter("curricularCourseId")));
        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
        newInfoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);

        if (beginDateString.compareTo("") != 0) {
            Calendar beginDateCalendar = Calendar.getInstance();
            beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
            newInfoCurricularCourseScope.setBeginDate(beginDateCalendar);
        }

        Object args[] = { newInfoCurricularCourseScope };
        try {
            ServiceUtils.executeService(userView, "InsertCurricularCourseScopeAtCurricularCourse", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        request.setAttribute("infoCurricularCourseScope", newInfoCurricularCourseScope);

        return mapping.findForward("readCurricularCourse");
    }
}