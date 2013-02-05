/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("chooseDegreeType");

    }

    public ActionForward chooseDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = UserView.getUser();

        DynaActionForm actionForm = (DynaActionForm) form;
        String degreeType = (String) actionForm.get("degreeType");

        Collection<InfoDegreeCurricularPlan> degreeCurricularPlans =
                ReadActiveDegreeCurricularPlansByDegreeType.run(DegreeType.valueOf(degreeType));

        List executionPeriods = ReadNotClosedExecutionPeriods.run();

        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
        request.setAttribute("executionPeriods", executionPeriods);

        return mapping.findForward("chooseDegreeCurricularPlans");

    }

    public ActionForward createExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = UserView.getUser();

        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] degreeCurricularPlansIDs = (Integer[]) actionForm.get("degreeCurricularPlansIDs");
        Integer executionPeriodID = (Integer) actionForm.get("executionPeriodID");

        CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.run(degreeCurricularPlansIDs, executionPeriodID);

        return mapping.findForward("createExecutionCoursesSuccess");

    }

}