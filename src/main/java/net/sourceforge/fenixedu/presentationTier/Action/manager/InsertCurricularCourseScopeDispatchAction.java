/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertCurricularCourseScopeAtCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranchesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/insertCurricularCourseScope",
        input = "/insertCurricularCourseScope.do?method=prepareInsert&page=0", attribute = "curricularCourseScopeForm",
        formBean = "curricularCourseScopeForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
        @Forward(name = "readDegree", path = "/readDegree.do"),
        @Forward(name = "insertCurricularCourseScope", path = "/manager/insertCurricularCourseScope_bd.jsp",
                tileProperties = @Tile(navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertCurricularCourseScopeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanId");

        List result = null;
        try {
            result = ReadBranchesByDegreeCurricularPlan.run(degreeCurricularPlanId);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (result == null) {
            throw new NonExistingActionException("message.insert.degreeCurricularCourseScope.error",
                    mapping.findForward("readCurricularCourse"));
        }

        // creation of bean of InfoBranches for use in jsp
        List branchesList = new ArrayList();

        InfoBranch infoBranch;
        Iterator iter = result.iterator();
        String label, value;
        while (iter.hasNext()) {
            infoBranch = (InfoBranch) iter.next();
            value = infoBranch.getExternalId().toString();
            label = infoBranch.getCode() + " - " + infoBranch.getName();
            branchesList.add(new LabelValueBean(label, value));
        }

        List<InfoExecutionPeriod> infoExecutionPeriods = null;
        infoExecutionPeriods = ReadExecutionPeriods.run();

        if (infoExecutionPeriods == null) {
            throw new NonExistingActionException("message.insert.executionPeriods.error",
                    mapping.findForward("readCurricularCourse"));
        }

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

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoCurricularCourseScopeEditor infoCurricularCourseScope = new InfoCurricularCourseScopeEditor();

        InfoBranch infoBranch = new InfoBranch(AbstractDomainObject.<Branch> fromExternalId((String) dynaForm.get("branchId")));
        infoCurricularCourseScope.setInfoBranch(infoBranch);

        final CurricularCourse curricularCourse =
                (CurricularCourse) AbstractDomainObject.fromExternalId(request.getParameter("curricularCourseId"));
        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);

        InfoCurricularSemester infoCurricularSemester =
                new InfoCurricularSemester(AbstractDomainObject.<CurricularSemester> fromExternalId((String) dynaForm
                        .get("curricularSemesterId")));
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

        String beginDateString = (String) dynaForm.get("beginDate");
        if (beginDateString.compareTo("") != 0) {
            Calendar beginDateCalendar = Calendar.getInstance();
            beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
            infoCurricularCourseScope.setBeginDate(beginDateCalendar);
        }
        infoCurricularCourseScope.setAnotation((String) dynaForm.get("anotation"));

        try {
            InsertCurricularCourseScopeAtCurricularCourse.run(infoCurricularCourseScope);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage(), mapping.findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("readCurricularCourse");
    }
}