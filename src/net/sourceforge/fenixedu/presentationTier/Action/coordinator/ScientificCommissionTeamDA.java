package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificCommissionTeamDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        
        ExecutionDegree executionDegree = getExecutionDegree(request);
        if (executionDegree != null) {
            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionDegreeId", executionDegree.getIdInternal());
        } 
        else {
            request.setAttribute("executionDegreeId", "");
        }
        
        return super.execute(mapping, actionForm, request, response);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        Integer id = getId(request.getParameter("degreeCurricularPlanID"));
        if (id == null) {
            return null;
        }
        else {
            return RootDomainObject.getInstance().readDegreeCurricularPlanByOID(id);
        }
    }
    
    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        Integer id = getId(request.getParameter("executionDegreeID"));
        if (id == null) {
            return getDefaultExecutionDegree(request);
        }
        else {
            return RootDomainObject.getInstance().readExecutionDegreeByOID(id);
        }
    }

    private ExecutionDegree getDefaultExecutionDegree(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        Set<ExecutionDegree> executionDegrees = new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        executionDegrees.addAll(degreeCurricularPlan.getExecutionDegrees());
        
        return executionDegrees.isEmpty() ? null : executionDegrees.iterator().next();
//        for (ExecutionDegree executionDegree : executionDegrees) {
//            if (isResponsible(request, executionDegree)) {
//                return executionDegree;
//            }
//        }
    }
    
    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ActionForward manage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        ExecutionDegree executionDegree = getExecutionDegree(request);

        ExecutionDegreeBean bean = (ExecutionDegreeBean) getRenderedObject("executionDegreeChoice");
        if (bean != null) {
            executionDegree = bean.getExecutionDegree();
        }
        else {
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
        
        if (! viewState.isValid()) {
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

    public ActionForward addMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        VariantBean bean = (VariantBean) getRenderedObject("usernameChoice");
        if (bean != null) {
            Integer number = bean.getInteger();
            
            Employee employee = Employee.readByNumber(number);
            if (employee == null) {
                addActionMessage("addError", request, "error.coordinator.scientificComission.employee.doesNotExist");
            }
            else {
                ExecutionDegree executionDegree = getExecutionDegree(request);
                
                try {
                    executeService("AddScientificCommission", executionDegree.getIdInternal(), employee.getPerson());
                    RenderUtils.invalidateViewState("usernameChoice");
                } catch (DomainException e) {
                    addActionMessage("addError", request, e.getKey(), e.getArgs());
                }
            }
        }
        
        return manage(mapping, actionForm, request, response);
    }

    public ActionForward removeMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer memberId = getId(request.getParameter("memberID"));
        
        if (memberId != null) {
            ExecutionDegree executionDegree = getExecutionDegree(request);
            
            for (ScientificCommission commission : executionDegree.getScientificCommissionMembers()) {
                if (commission.getIdInternal().equals(memberId)) {
                    try {
                        executeService("DeleteScientificCommission", executionDegree.getIdInternal(), commission);
                    } catch (DomainException e) {
                        addActionMessage("addError", request, e.getKey(), e.getArgs());
                    }
                }
            }
        }
        
        return manage(mapping, actionForm, request, response);
    }

}
