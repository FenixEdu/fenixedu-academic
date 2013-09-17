package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AddScientificCommission;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.DeleteScientificCommission;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/scientificCommissionTeamDA", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewScientificCommission",
        path = "/coordinator/scientificCommission/manageScientificCommission.jsp", tileProperties = @Tile(
                title = "private.coordinator.management.courses.management.scientificcommittee")) })
public class ScientificCommissionTeamDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

        ExecutionDegree executionDegree = getExecutionDegree(request);
        if (executionDegree != null) {
            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionDegreeId", executionDegree.getExternalId());
        } else {
            request.setAttribute("executionDegreeId", "");
        }

        return super.execute(mapping, actionForm, request, response);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String id = request.getParameter("degreeCurricularPlanID");
        if (id == null) {
            return null;
        } else {
            return FenixFramework.getDomainObject(id);
        }
    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        ExecutionDegreeBean bean = getRenderedObject("executionDegreeChoice");
        if (bean != null) {
            return bean.getExecutionDegree();
        } else {
            String id = request.getParameter("executionDegreeID");
            if (id == null) {
                return getDefaultExecutionDegree(request);
            } else {
                return FenixFramework.getDomainObject(id);
            }
        }
    }

    private ExecutionDegree getDefaultExecutionDegree(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        TreeSet<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        executionDegrees.addAll(degreeCurricularPlan.getExecutionDegrees());

        return executionDegrees.isEmpty() ? null : executionDegrees.last();
    }

    public ActionForward manage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        ExecutionDegree executionDegree = getExecutionDegree(request);

        ExecutionDegreeBean bean = getRenderedObject("executionDegreeChoice");
        if (bean == null) {
            bean = new ExecutionDegreeBean(degreeCurricularPlan);
            bean.setExecutionDegree(executionDegree);
        }

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("executionDegreeBean", bean);
        request.setAttribute("usernameBean", new VariantBean());
        request.setAttribute("members", executionDegree == null ? null : executionDegree.getScientificCommissionMembers());

        if (isResponsible(request, executionDegree)) {
            request.setAttribute("responsible", true);
        }

        if (hasUpdatedContactFlag(request)) {
            request.setAttribute("updateContactConfirmation", true);
        }

        return mapping.findForward("viewScientificCommission");
    }

    private boolean hasUpdatedContactFlag(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("membersContacts");

        if (viewState == null) {
            return false;
        }

        if (!viewState.isValid()) {
            return false;
        }

        if (viewState.skipUpdate()) {
            return false;
        }

        return true;
    }

    private boolean isResponsible(HttpServletRequest request, ExecutionDegree executionDegree) {
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(getLoggedPerson(request));

        return coordinator != null && coordinator.isResponsible();
    }

    public ActionForward addMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VariantBean bean = getRenderedObject("usernameChoice");
        if (bean != null) {
            String istUsername = bean.getString();

            Person person = Person.readPersonByIstUsername(istUsername);
            Employee employee = person == null ? null : person.getEmployee();
            if (employee == null) {
                addActionMessage("addError", request, "error.coordinator.scientificComission.employee.doesNotExist");
            } else {
                ExecutionDegree executionDegree = getExecutionDegree(request);

                try {
                    AddScientificCommission.runAddScientificCommission(executionDegree.getExternalId(), employee.getPerson());
                    RenderUtils.invalidateViewState("usernameChoice");
                } catch (DomainException e) {
                    addActionMessage("addError", request, e.getKey(), e.getArgs());
                }
            }
        }

        return manage(mapping, actionForm, request, response);
    }

    public ActionForward removeMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String memberId = request.getParameter("memberID");

        if (memberId != null) {
            ExecutionDegree executionDegree = getExecutionDegree(request);

            for (ScientificCommission commission : executionDegree.getScientificCommissionMembers()) {
                if (commission.getExternalId().equals(memberId)) {
                    try {
                        DeleteScientificCommission.runDeleteScientificCommission(executionDegree.getExternalId(), commission);
                    } catch (DomainException e) {
                        addActionMessage("addError", request, e.getKey(), e.getArgs());
                    }
                }
            }
        }

        return manage(mapping, actionForm, request, response);
    }

}
