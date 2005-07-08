/*
 * Created on 28/Jul/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.precedences;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 *  
 */
public class MakeSimplePrecedenceAction extends FenixDispatchAction {

    public ActionForward showAllRestrictions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showAllRestrictions");
    }

    public ActionForward chooseRestriction(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        ActionErrors errors = new ActionErrors();

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        //read all curricular courses belong to this degree curricular year
        Object[] args = { degreeCurricularPlanID };
        List curricularCoursesList = null;
        try {
            curricularCoursesList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCurricularCoursesByDegreeCurricularPlan", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleCCOfDCP", new ActionError("error.manager.impossible.readCCofDCP"));
        }
        if (curricularCoursesList == null || curricularCoursesList.size() <= 0) {
            errors.add("impossibleCCOfDCP", new ActionError("error.manager.impossible.readCCofDCP"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        Collections.sort(curricularCoursesList, new BeanComparator("name"));

        request.setAttribute("curricularCoursesList", curricularCoursesList);

        String className = request.getParameter("className");
        if (className == null || className.length() <= 0) {
            return mapping.getInputForward();
        }
        request.setAttribute("className", className);

        if (className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName().substring(
                RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf(".") + 1))) {
            return mapping.findForward("insertRestrictionByNumber");
        } else if (className.equals(RestrictionPeriodToApply.class.getName().substring(
                RestrictionPeriodToApply.class.getName().lastIndexOf(".") + 1))) {
            request.setAttribute("periodToApplyList", PeriodToApplyRestriction.getEnumList());
            return mapping.findForward("insertRestrictionByPeriodToApply");
        } else if ((className.equals(RestrictionDoneCurricularCourse.class.getName().substring(
                RestrictionDoneCurricularCourse.class.getName().lastIndexOf(".") + 1)))
                || (className.equals(RestrictionNotDoneCurricularCourse.class.getName().substring(
                        RestrictionNotDoneCurricularCourse.class.getName().lastIndexOf(".") + 1)))
                || (className.equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                        .getName().substring(
                                RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                                        .getName().lastIndexOf(".") + 1)))
                || (className
                        .equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                                .getName()
                                .substring(
                                        RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                                                .getName().lastIndexOf(".") + 1)))
                || (className.equals(RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class
                        .getName().substring(
                                RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName()
                                        .lastIndexOf(".") + 1)))
                || (className.equals(RestrictionNotEnrolledInCurricularCourse.class.getName().substring(
                        RestrictionNotEnrolledInCurricularCourse.class.getName().lastIndexOf(".") + 1)))) {
            return mapping.findForward("insertRestrictionByCurricularCourse");
        }

        return mapping.getInputForward();
    }

    public ActionForward insertRestriction(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        ActionErrors errors = new ActionErrors();

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        DynaActionForm insertRestrictionForm = (DynaActionForm) actionForm;

        String classeNameRestriction = (String) insertRestrictionForm.get("className");
        Integer number = (Integer) insertRestrictionForm.get("number");
        Integer curricularCourseToAddPrecedenceID = (Integer) insertRestrictionForm
                .get("curricularCourseToAddPrecedenceID");
        Integer precedentCurricularCourseID = (Integer) insertRestrictionForm
                .get("precedentCurricularCourseID");

        request.setAttribute("className", classeNameRestriction);
        request.setAttribute("number", number);
        request.setAttribute("curricularCourseToAddPrecedenceID", curricularCourseToAddPrecedenceID);
        request.setAttribute("precedentCurricularCourseID", precedentCurricularCourseID);

        Object[] args = { classeNameRestriction, curricularCourseToAddPrecedenceID,
                precedentCurricularCourseID, number };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertSimplePrecedence", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleInsertPrecedence", new ActionError(
                    "error.manager.impossible.insertPrecedence"));
        }

        return mapping.findForward("sucess");
    }
}

