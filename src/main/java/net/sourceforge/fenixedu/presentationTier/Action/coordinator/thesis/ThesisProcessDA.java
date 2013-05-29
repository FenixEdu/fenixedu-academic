package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/thesisProcess", module = "coordinator")
@Forwards({ @Forward(name = "showInformation", path = "/coordinator/thesis/showInformation.jsp") })
public class ThesisProcessDA extends FenixDispatchAction {

    public ActionForward showInformation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final ManageThesisContext manageThesisContext = processContext(request);
        return mapping.findForward("showInformation");
    }

    private ManageThesisContext processContext(final HttpServletRequest request) {
        ManageThesisContext manageThesisContext = getRenderedObject();
        if (manageThesisContext == null) {
            final ExecutionDegree executionDegree = guessExecutionDegree(request);
            if (executionDegree != null) {
                manageThesisContext = new ManageThesisContext(executionDegree);
            }
        }
        if (manageThesisContext != null) {
            request.setAttribute("manageThesisContext", manageThesisContext);
        }
        return manageThesisContext;
    }

    private ExecutionDegree guessExecutionDegree(final HttpServletRequest request) {
        final DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "degreeCurricularPlanID");
        ExecutionDegree last = null;
        if (degreeCurricularPlan != null) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                final ExecutionYear executionYear = executionDegree.getExecutionYear();
                if (executionYear.isCurrent()) {
                    return executionDegree;
                }
                if (last == null || last.getExecutionYear().isBefore(executionYear)) {
                    last = executionDegree;
                }
            }
        }
        return last;
    }

}
