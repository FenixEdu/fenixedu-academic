/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanEditor;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.Conversores.Calendar2DateFieldConversion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.ojb.broker.accesslayer.conversions.JavaDate2SqlDateFieldConversion;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */

public class EditDegreeCurricularPlanDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

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
        dynaForm.set("state", oldInfoDegreeCP.getState().toString());

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
        
        if(oldInfoDegreeCP.getGradeScale() != null) {
        	dynaForm.set("gradeType", oldInfoDegreeCP.getGradeScale().toString());
        }


        if (oldInfoDegreeCP.getNumerusClausus() != null)
            dynaForm.set("numerusClausus", oldInfoDegreeCP.getNumerusClausus().toString());
        	dynaForm.set("anotation",oldInfoDegreeCP.getAnotation());

        return mapping.findForward("editDegreeCP");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldDegreeCPId = new Integer(request.getParameter("degreeCurricularPlanId"));
        Integer degreeId = new Integer(request.getParameter("degreeId"));
        final Degree degree = rootDomainObject.readDegreeByOID(degreeId);

        InfoDegreeCurricularPlanEditor newInfoDegreeCP = new InfoDegreeCurricularPlanEditor();
        newInfoDegreeCP.setIdInternal(oldDegreeCPId);
        InfoDegree infoDegree = new InfoDegree(degree);

        String name = (String) dynaForm.get("name");
        String stateString = (String) dynaForm.get("state");

        String initialDateString = (String) dynaForm.get("initialDate");
        String endDateString = (String) dynaForm.get("endDate");

        Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
        Integer minimalYearForOptionalCourses = new Integer((String) dynaForm
                .get("minimalYearForOptionalCourses"));

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

        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if(gradeTypeString != null && gradeTypeString.length() > 0) {
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
        newInfoDegreeCP.setIdInternal(oldDegreeCPId);
        newInfoDegreeCP.setAnotation(anotationtring);
        newInfoDegreeCP.setGradeScale(gradeScale);
        newInfoDegreeCP.setInfoDegree(infoDegree);

        Object args[] = { newInfoDegreeCP };

        try {
            ServiceUtils.executeService(userView, "EditDegreeCurricularPlanInOldDegreeStructure", args);

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