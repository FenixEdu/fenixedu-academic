/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
            HttpServletResponse response) throws  FenixServiceException {

        try {

            DynaActionForm actionForm = (DynaActionForm) form;
            String degreeType = (String) actionForm.get("degreeType");

            if (StringUtils.isEmpty(degreeType)) {
                degreeType = request.getParameter("degreeType");
            }

            if (StringUtils.isEmpty(degreeType)) {
                throw new DomainException("error.selection.noDegreeType");
            }

            Collection<InfoDegreeCurricularPlan> degreeCurricularPlans =
                    ReadActiveDegreeCurricularPlansByDegreeType.run(DegreeType.valueOf(degreeType));

            List<InfoExecutionPeriod> executionPeriods = ReadNotClosedExecutionPeriods.run();

            request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
            request.setAttribute("executionPeriods", executionPeriods);
            request.setAttribute("degreeType", degreeType);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return chooseDegreeType(mapping, form, request, response);
        }

        return mapping.findForward("chooseDegreeCurricularPlans");

    }

    public ActionForward createExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] degreeCurricularPlansIDs = (Integer[]) actionForm.get("degreeCurricularPlansIDs");
        Integer executionPeriodID = (Integer) actionForm.get("executionPeriodID");
        try {
            HashMap<Integer, Integer> result =
                    CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.run(degreeCurricularPlansIDs,
                            executionPeriodID);

            // avmc (ist150958): success messages: 1 line for each DCP
            final ExecutionSemester executionPeriod =
                    RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);
            addActionMessage("successHead", request,
                    "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successHead",
                    executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear());

            for (final Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {
                final DegreeCurricularPlan degreeCurricularPlan =
                        RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);
                addActionMessage("successDCP", request,
                        "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successDCP",
                        degreeCurricularPlan.getPresentationName(), result.get(degreeCurricularPlanID).toString());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return chooseDegreeCurricularPlans(mapping, actionForm, request, response);
        }
        return mapping.findForward("createExecutionCoursesSuccess");

    }

}