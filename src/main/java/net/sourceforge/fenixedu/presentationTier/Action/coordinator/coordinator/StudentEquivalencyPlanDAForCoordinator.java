package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.StudentEquivalencyPlanDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/degreeCurricularPlan/studentEquivalencyPlan",
        functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "showPlan", path = "/coordinator/degreeCurricularPlan/showStudentEquivalencyPlan.jsp"))
public class StudentEquivalencyPlanDAForCoordinator extends StudentEquivalencyPlanDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

}