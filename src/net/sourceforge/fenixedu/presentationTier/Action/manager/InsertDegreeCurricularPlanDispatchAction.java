/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

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
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */

public class InsertDegreeCurricularPlanDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("insertDegreeCurricularPlan");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeId = new Integer(request.getParameter("degreeId"));
        final Degree degree = rootDomainObject.readDegreeByOID(degreeId);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

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
        String anotationString = (String) dynaForm.get("anotation");

        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if(gradeTypeString != null && gradeTypeString.length() > 0) {
        	gradeScale = GradeScale.valueOf(gradeTypeString);
        }


        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        DegreeCurricularPlanState state = DegreeCurricularPlanState.valueOf(stateString);

        Calendar initialDate = Calendar.getInstance();
        if (initialDateString.compareTo("") != 0) {
            String[] initialDateTokens = initialDateString.split("/");
            initialDate.set(Calendar.DAY_OF_MONTH, (new Integer(initialDateTokens[0])).intValue());
            initialDate.set(Calendar.MONTH, (new Integer(initialDateTokens[1])).intValue() - 1);
            initialDate.set(Calendar.YEAR, (new Integer(initialDateTokens[2])).intValue());
            infoDegreeCurricularPlan.setInitialDate(initialDate.getTime());
        }

        Calendar endDate = Calendar.getInstance();
        if (endDateString.compareTo("") != 0) {
            String[] endDateTokens = endDateString.split("/");
            endDate.set(Calendar.DAY_OF_MONTH, (new Integer(endDateTokens[0])).intValue());
            endDate.set(Calendar.MONTH, (new Integer(endDateTokens[1])).intValue() - 1);
            endDate.set(Calendar.YEAR, (new Integer(endDateTokens[2])).intValue());
            infoDegreeCurricularPlan.setEndDate(endDate.getTime());
        }

        if (endDate.before(initialDate))
            throw new InvalidArgumentsActionException("message.manager.date.restriction");

        if (neededCreditsString.compareTo("") != 0) {
            Double neededCredits = new Double(neededCreditsString);
            infoDegreeCurricularPlan.setNeededCredits(neededCredits);
        }

        MarkType markType = new MarkType(new Integer(markTypeString));
        infoDegreeCurricularPlan.setMarkType(markType);

        if (numerusClaususString.compareTo("") != 0) {
            Integer numerusClausus = new Integer(numerusClaususString);
            infoDegreeCurricularPlan.setNumerusClausus(numerusClausus);
        }

        infoDegreeCurricularPlan.setName(name);
        infoDegreeCurricularPlan.setState(state);
        infoDegreeCurricularPlan.setDegreeDuration(degreeDuration);
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        infoDegreeCurricularPlan.setAnotation(anotationString);
        infoDegreeCurricularPlan.setGradeScale(gradeScale);
        InfoDegree infoDegree = new InfoDegree(degree);
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

        Object args[] = { infoDegreeCurricularPlan };

        try {
            ServiceUtils.executeService(userView, "InsertDegreeCurricularPlan", args);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException("message.nonExistingDegree", mapping
                    .findForward("readDegree"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("readDegree");
    }
}