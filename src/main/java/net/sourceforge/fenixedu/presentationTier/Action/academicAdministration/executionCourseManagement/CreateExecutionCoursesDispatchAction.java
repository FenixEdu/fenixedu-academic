package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

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
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.Pair;

@Mapping(module = "academicAdministration", path = "/createExecutionCourses",
        input = "/createExecutionCourses.do?method=chooseDegreeType", attribute = "createExecutionCoursesForm",
        formBean = "createExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseDegreeCurricularPlans",
                path = "/academicAdministration/executionCourseManagement/chooseDegreeCurricularPlans.jsp"),
        @Forward(name = "chooseDegreeType", path = "/academicAdministration/executionCourseManagement/chooseDegreeType.jsp"),
        @Forward(name = "createExecutionCoursesSuccess",
                path = "/academicAdministration/executionCourseManagement/createExecutionCoursesSuccess.jsp") })
public class CreateExecutionCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("chooseDegreeType");

    }

    public ActionForward chooseDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

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
                    ReadActiveDegreeCurricularPlansByDegreeType.runForAcademicAdmin(DegreeType.valueOf(degreeType));
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
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm actionForm = (DynaActionForm) form;
        String[] degreeCurricularPlansIDs = (String[]) actionForm.get("degreeCurricularPlansIDs");
        String executionPeriodID = (String) actionForm.get("executionPeriodID");
        try {
            HashMap<String, Pair<Integer, String>> result =
                    CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.run(degreeCurricularPlansIDs,
                            executionPeriodID);

            // avmc (ist150958): success messages: 1 line for each DCP
            final ExecutionSemester executionPeriod = FenixFramework.getDomainObject(executionPeriodID);
            addActionMessage("successHead", request,
                    "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successHead",
                    executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear());

            for (final String degreeCurricularPlanID : degreeCurricularPlansIDs) {
                final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
                addActionMessage("successDCP", request,
                        "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successDCP",
                        degreeCurricularPlan.getPresentationName(), result.get(degreeCurricularPlanID).getKey().toString(),
                        result.get(degreeCurricularPlanID).getValue());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return chooseDegreeCurricularPlans(mapping, actionForm, request, response);
        }
        return mapping.findForward("createExecutionCoursesSuccess");

    }
}
