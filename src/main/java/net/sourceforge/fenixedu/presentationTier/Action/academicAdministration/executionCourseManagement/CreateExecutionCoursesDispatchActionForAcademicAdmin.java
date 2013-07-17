package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/createExecutionCourses",
        input = "/createExecutionCourses.do?method=chooseDegreeType", attribute = "createExecutionCoursesForm",
        formBean = "createExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseDegreeCurricularPlans",
                path = "/academicAdministration/executionCourseManagement/chooseDegreeCurricularPlans.jsp"),
        @Forward(name = "chooseDegreeType", path = "/academicAdministration/executionCourseManagement/chooseDegreeType.jsp"),
        @Forward(name = "createExecutionCoursesSuccess",
                path = "/academicAdministration/executionCourseManagement/createExecutionCoursesSuccess.jsp") })
public class CreateExecutionCoursesDispatchActionForAcademicAdmin extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.CreateExecutionCoursesDispatchAction {

    @Override
    public ActionForward chooseDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        try {

            DynaActionForm actionForm = (DynaActionForm) form;
            String degreeType = (String) actionForm.get("degreeType");

            if (StringUtils.isEmpty(degreeType)) {
                degreeType = request.getParameter("degreeType");
            }

            if (StringUtils.isEmpty(degreeType)) {
                throw new DomainException("error.selection.noDegreeType");
            }

            //TODO: Need to place access control better
            Collection<InfoDegreeCurricularPlan> degreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
            for (InfoDegreeCurricularPlan idcp : ReadActiveDegreeCurricularPlansByDegreeType.run(DegreeType.valueOf(degreeType))) {
                if (AcademicPredicates.MANAGE_EXECUTION_COURSES.evaluate(idcp.getDegreeCurricularPlan().getDegree())) {
                    degreeCurricularPlans.add(idcp);
                }
            }
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
}