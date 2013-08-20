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
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences.InsertSimplePrecedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Tânia Pousão
 * 
 */
@Mapping(module = "manager", path = "/makeSimplePrecedence", input = "/managePrecedences.do",
        attribute = "insertRestrictionForm", formBean = "insertRestrictionForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "sucess", path = "/managePrecedences.do"),
        @Forward(name = "insertRestrictionByPeriodToApply", path = "/manager/precedences/insertRestrictionByPeriodToApply.jsp",
                tileProperties = @Tile(navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")),
        @Forward(name = "insertRestrictionByCurricularCourse",
                path = "/manager/precedences/insertRestrictionByCurricularCourse.jsp", tileProperties = @Tile(
                        navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")),
        @Forward(name = "insertRestrictionByNumber", path = "/manager/precedences/insertRestrictionByNumber.jsp",
                tileProperties = @Tile(navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")),
        @Forward(name = "showAllRestrictions", path = "/manager/precedences/manageRestriction.jsp", tileProperties = @Tile(
                navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")) })
public class MakeSimplePrecedenceAction extends FenixDispatchAction {

    public ActionForward showAllRestrictions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String degreeID = request.getParameter("degreeId");
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanId");

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showAllRestrictions");
    }

    public ActionForward chooseRestriction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = UserView.getUser();

        ActionErrors errors = new ActionErrors();

        String degreeID = request.getParameter("degreeId");
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanId");

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        // read all curricular courses belong to this degree curricular year

        List curricularCoursesList = null;
        try {
            curricularCoursesList = ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanID);
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

        if (className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName())) {
            return mapping.findForward("insertRestrictionByNumber");
        } else if (className.equals(RestrictionPeriodToApply.class.getName())) {
            request.setAttribute("periodToApplyList", PeriodToApplyRestriction.getEnumList());
            return mapping.findForward("insertRestrictionByPeriodToApply");
        } else if (className.equals(RestrictionDoneCurricularCourse.class.getName())
                || className.equals(RestrictionNotDoneCurricularCourse.class.getName())
                || className.equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName())
                || className.equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName())
                || className.equals(RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName())
                || className.equals(RestrictionNotEnrolledInCurricularCourse.class.getName())) {
            return mapping.findForward("insertRestrictionByCurricularCourse");
        }

        return mapping.getInputForward();
    }

    public ActionForward insertRestriction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = UserView.getUser();

        ActionErrors errors = new ActionErrors();

        String degreeID = request.getParameter("degreeId");
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanId");

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        DynaActionForm insertRestrictionForm = (DynaActionForm) actionForm;

        String classeNameRestriction = (String) insertRestrictionForm.get("className");
        Integer number = (Integer) insertRestrictionForm.get("number");
        String curricularCourseToAddPrecedenceID = (String) insertRestrictionForm.get("curricularCourseToAddPrecedenceID");
        String precedentCurricularCourseID = (String) insertRestrictionForm.get("precedentCurricularCourseID");

        request.setAttribute("className", classeNameRestriction);
        request.setAttribute("number", number);
        request.setAttribute("curricularCourseToAddPrecedenceID", curricularCourseToAddPrecedenceID);
        request.setAttribute("precedentCurricularCourseID", precedentCurricularCourseID);

        try {
            InsertSimplePrecedence.run(classeNameRestriction, curricularCourseToAddPrecedenceID, precedentCurricularCourseID,
                    number);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleInsertPrecedence", new ActionError("error.manager.impossible.insertPrecedence"));
        }

        return mapping.findForward("sucess");
    }
}