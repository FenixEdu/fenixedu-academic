/*
 * Created on 5/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ojb.broker.accesslayer.conversions.JavaDate2SqlDateFieldConversion;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.Conversores.Calendar2DateFieldConversion;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author lmac1
 */

public class EditDegreeCurricularPlanDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        InfoDegreeCurricularPlan oldInfoDegreeCP = null;

        Object args[] = { degreeCurricularPlanId };

        try {
            oldInfoDegreeCP = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping
                    .findForward("readDegree"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        dynaForm.set("name", oldInfoDegreeCP.getName());
        dynaForm.set("state", oldInfoDegreeCP.getState().getDegreeState().toString());

        if (oldInfoDegreeCP.getInitialDate() != null) {

            JavaDate2SqlDateFieldConversion iJavaDate = new JavaDate2SqlDateFieldConversion();
            Date iSqlDate = (Date) iJavaDate.javaToSql(oldInfoDegreeCP.getInitialDate());
            Calendar2DateFieldConversion initialCal = new Calendar2DateFieldConversion();
            Calendar initialCalendar = (Calendar) initialCal.sqlToJava(iSqlDate);

            String day = (new Integer(initialCalendar.get(Calendar.DAY_OF_MONTH))).toString();
            String month = (new Integer(initialCalendar.get(Calendar.MONTH) + 1)).toString();
            String year = (new Integer(initialCalendar.get(Calendar.YEAR))).toString();
            String initialDateString = day + "/" + month + "/" + year;

            dynaForm.set("initialDate", initialDateString);
        }

        if (oldInfoDegreeCP.getEndDate() != null) {

            JavaDate2SqlDateFieldConversion javaDate = new JavaDate2SqlDateFieldConversion();
            Date sqlDate = (Date) javaDate.javaToSql(oldInfoDegreeCP.getEndDate());
            Calendar2DateFieldConversion endCal = new Calendar2DateFieldConversion();
            Calendar endCalendar = (Calendar) endCal.sqlToJava(sqlDate);

            String day = (new Integer(endCalendar.get(Calendar.DAY_OF_MONTH))).toString();
            String month = (new Integer(endCalendar.get(Calendar.MONTH) + 1)).toString();
            String year = (new Integer(endCalendar.get(Calendar.YEAR))).toString();
            String endDateString = day + "/" + month + "/" + year;

            dynaForm.set("endDate", endDateString);

        }

        if (oldInfoDegreeCP.getDegreeDuration() != null) {
            dynaForm.set("degreeDuration", oldInfoDegreeCP.getDegreeDuration().toString());
        }
        if (oldInfoDegreeCP.getMinimalYearForOptionalCourses() != null) {
            dynaForm.set("minimalYearForOptionalCourses", oldInfoDegreeCP
                    .getMinimalYearForOptionalCourses().toString());
        }

        if (oldInfoDegreeCP.getNeededCredits() != null)
            dynaForm.set("neededCredits", oldInfoDegreeCP.getNeededCredits().toString());

        dynaForm.set("markType", oldInfoDegreeCP.getMarkType().getType().toString());

        if (oldInfoDegreeCP.getNumerusClausus() != null)
            dynaForm.set("numerusClausus", oldInfoDegreeCP.getNumerusClausus().toString());

        return mapping.findForward("editDegreeCP");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldDegreeCPId = new Integer(request.getParameter("degreeCurricularPlanId"));

        InfoDegreeCurricularPlan newInfoDegreeCP = new InfoDegreeCurricularPlan();

        String name = (String) dynaForm.get("name");
        Integer stateInt = new Integer((String) dynaForm.get("state"));

        String initialDateString = (String) dynaForm.get("initialDate");
        String endDateString = (String) dynaForm.get("endDate");

        Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
        Integer minimalYearForOptionalCourses = new Integer((String) dynaForm
                .get("minimalYearForOptionalCourses"));

        String neededCreditsString = (String) dynaForm.get("neededCredits");

        String markTypeString = (String) dynaForm.get("markType");
        String numerusClaususString = (String) dynaForm.get("numerusClausus");

        DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);

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

        if (endDate.before(initialDate))
            throw new InvalidArgumentsActionException("message.manager.date.restriction");

        if (neededCreditsString.compareTo("") != 0) {
            Double neededCredits = new Double(neededCreditsString);
            newInfoDegreeCP.setNeededCredits(neededCredits);
        }

        if (markTypeString.compareTo("") != 0) {

            Integer markTypeInt = new Integer(markTypeString);
            MarkType markType = new MarkType(markTypeInt);
            newInfoDegreeCP.setMarkType(markType);
        }

        if (numerusClaususString.compareTo("") != 0) {
            Integer numerusClausus = new Integer(numerusClaususString);
            newInfoDegreeCP.setNumerusClausus(numerusClausus);
        }

        newInfoDegreeCP.setName(name);
        newInfoDegreeCP.setState(state);
        newInfoDegreeCP.setDegreeDuration(degreeDuration);
        newInfoDegreeCP.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        newInfoDegreeCP.setIdInternal(oldDegreeCPId);

        Object args[] = { newInfoDegreeCP };

        try {
            ServiceUtils.executeService(userView, "EditDegreeCurricularPlan", args);

        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.degree.curricular.plan");
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readDegree"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readDegreeCP");
    }
}