/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanEditor;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/editDegreeCurricularPlan",
        input = "/editDegreeCurricularPlan.do?method=prepareEdit&page=0", attribute = "degreeCurricularPlanForm",
        formBean = "degreeCurricularPlanForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readDegreeCP", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "editDegreeCP", path = "/manager/editDegreeCurricularPlan_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")),
        @Forward(name = "readDegree", path = "/readDegree.do") })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException.class,
                key = "resources.Action.exceptions.InvalidArgumentsActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditDegreeCurricularPlanDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoDegreeCurricularPlan oldInfoDegreeCP = null;

        try {
            oldInfoDegreeCP =
                    ReadDegreeCurricularPlan.runReadDegreeCurricularPlan(request.getParameter("degreeCurricularPlanId"));

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        dynaForm.set("name", oldInfoDegreeCP.getName());
        dynaForm.set("state", oldInfoDegreeCP.getState().toString());

        DegreeCurricularPlan oldDegreeCP = oldInfoDegreeCP.getDegreeCurricularPlan();

        if (oldDegreeCP.getInitialDateYearMonthDay() != null) {
            YearMonthDay ymd = oldDegreeCP.getInitialDateYearMonthDay();
            String initialDateString = ymd.getDayOfMonth() + "/" + ymd.getMonthOfYear() + "/" + ymd.getYear();

            dynaForm.set("initialDate", initialDateString);
        }

        if (oldDegreeCP.getEndDateYearMonthDay() != null) {
            YearMonthDay ymd = oldDegreeCP.getEndDateYearMonthDay();
            String endDateString = ymd.getDayOfMonth() + "/" + ymd.getMonthOfYear() + "/" + ymd.getYear();

            dynaForm.set("endDate", endDateString);
        }

        if (oldInfoDegreeCP.getDegreeDuration() != null) {
            dynaForm.set("degreeDuration", oldInfoDegreeCP.getDegreeDuration().toString());
        }
        if (oldInfoDegreeCP.getMinimalYearForOptionalCourses() != null) {
            dynaForm.set("minimalYearForOptionalCourses", oldInfoDegreeCP.getMinimalYearForOptionalCourses().toString());
        }

        if (oldInfoDegreeCP.getNeededCredits() != null) {
            dynaForm.set("neededCredits", oldInfoDegreeCP.getNeededCredits().toString());
        }

        dynaForm.set("markType", oldInfoDegreeCP.getMarkType().getType().toString());

        if (oldInfoDegreeCP.getGradeScale() != null) {
            dynaForm.set("gradeType", oldInfoDegreeCP.getGradeScale().toString());
        }

        if (oldInfoDegreeCP.getNumerusClausus() != null) {
            dynaForm.set("numerusClausus", oldInfoDegreeCP.getNumerusClausus().toString());
        }
        dynaForm.set("anotation", oldInfoDegreeCP.getAnotation());

        return mapping.findForward("editDegreeCP");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        String oldDegreeCPId = request.getParameter("degreeCurricularPlanId");
        final Degree degree = FenixFramework.getDomainObject(request.getParameter("degreeId"));

        InfoDegreeCurricularPlanEditor newInfoDegreeCP = new InfoDegreeCurricularPlanEditor();
        newInfoDegreeCP.setExternalId(oldDegreeCPId);
        InfoDegree infoDegree = new InfoDegree(degree);

        String name = (String) dynaForm.get("name");
        String stateString = (String) dynaForm.get("state");

        String initialDateString = (String) dynaForm.get("initialDate");
        String endDateString = (String) dynaForm.get("endDate");

        Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
        Integer minimalYearForOptionalCourses = new Integer((String) dynaForm.get("minimalYearForOptionalCourses"));

        String neededCreditsString = (String) dynaForm.get("neededCredits");

        String markTypeString = (String) dynaForm.get("markType");
        String numerusClaususString = (String) dynaForm.get("numerusClausus");
        String anotationtring = (String) dynaForm.get("anotation");

        DegreeCurricularPlanState state = DegreeCurricularPlanState.valueOf(stateString);

        Calendar initialDate = Calendar.getInstance();
        if (initialDateString.compareTo("") != 0) {
            String[] initialDateTokens = initialDateString.split("/");
            initialDate.set(Calendar.DAY_OF_MONTH, (new Integer(initialDateTokens[0])).intValue());
            initialDate.set(Calendar.MONTH, (new Integer(initialDateTokens[1])).intValue() - 1);
            initialDate.set(Calendar.YEAR, (new Integer(initialDateTokens[2])).intValue());
            newInfoDegreeCP.setInitialDate(initialDate.getTime());
        }

        Calendar endDate = Calendar.getInstance();
        if (endDateString.compareTo("") != 0) {
            String[] endDateTokens = endDateString.split("/");
            endDate.set(Calendar.DAY_OF_MONTH, (new Integer(endDateTokens[0])).intValue());
            endDate.set(Calendar.MONTH, (new Integer(endDateTokens[1])).intValue() - 1);
            endDate.set(Calendar.YEAR, (new Integer(endDateTokens[2])).intValue());
            newInfoDegreeCP.setEndDate(endDate.getTime());
        }

        if (endDate.before(initialDate)) {
            throw new InvalidArgumentsActionException("message.manager.date.restriction");
        }

        if (neededCreditsString.compareTo("") != 0) {
            Double neededCredits = new Double(neededCreditsString);
            newInfoDegreeCP.setNeededCredits(neededCredits);
        }

        if (markTypeString.compareTo("") != 0) {

            Integer markTypeInt = new Integer(markTypeString);
            MarkType markType = new MarkType(markTypeInt);
            newInfoDegreeCP.setMarkType(markType);
        }

        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if (gradeTypeString != null && gradeTypeString.length() > 0) {
            gradeScale = GradeScale.valueOf(gradeTypeString);
        }

        if (numerusClaususString.compareTo("") != 0) {
            Integer numerusClausus = new Integer(numerusClaususString);
            newInfoDegreeCP.setNumerusClausus(numerusClausus);
        }

        newInfoDegreeCP.setName(name);
        newInfoDegreeCP.setState(state);
        newInfoDegreeCP.setDegreeDuration(degreeDuration);
        newInfoDegreeCP.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        newInfoDegreeCP.setExternalId(oldDegreeCPId);
        newInfoDegreeCP.setAnotation(anotationtring);
        newInfoDegreeCP.setGradeScale(gradeScale);
        newInfoDegreeCP.setInfoDegree(infoDegree);

        try {
            EditDegreeCurricularPlan.run(newInfoDegreeCP);

        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.degree.curricular.plan", e);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readDegree"));
        } catch (FenixServiceException fenixServiceException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(fenixServiceException.getMessage(), new ActionMessage(fenixServiceException.getMessage()));
            saveMessages(request, actionMessages);
            return mapping.findForward("editDegreeCP");
        }
        return mapping.findForward("readDegreeCP");
    }
}