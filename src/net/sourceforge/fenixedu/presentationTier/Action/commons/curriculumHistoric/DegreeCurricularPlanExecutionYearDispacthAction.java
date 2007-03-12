/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author nmgo
 * @author lmre
 */
public class DegreeCurricularPlanExecutionYearDispacthAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        List executionYears = null;
        try {
            executionYears = (List) ServiceUtils.executeService(userView, "ReadNotClosedExecutionYears",
                    new Object[] {});
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("executionYears", executionYears);
        request.setAttribute("degreeCurricularPlans", null);
        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegreeCurricularPlan(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {

	final String executionYearOID = (String) ((DynaActionForm) form).get("executionYearID");
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearOID));
        final IUserView userView = SessionUtils.getUserView(request);
        
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("degree.degreeType"));
        comparatorChain.addComparator(new BeanComparator("degree.name"));
        comparatorChain.addComparator(new BeanComparator("name"));
        
        final SortedSet<DegreeCurricularPlan> degreeCurricularPlans = new TreeSet<DegreeCurricularPlan>(comparatorChain);
        degreeCurricularPlans.addAll(executionYear.getDegreeCurricularPlans());

        final List<LabelValueBean> labelValueDegreeCurricularPlans = new ArrayList<LabelValueBean>(degreeCurricularPlans.size()); 
        for (final DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            labelValueDegreeCurricularPlans.add(
        	    new LabelValueBean(
        		    degreeCurricularPlan.getDegree().getName() + " - " + degreeCurricularPlan.getName(),
        		    degreeCurricularPlan.getIdInternal().toString()));
	}
        
        request.setAttribute("degreeCurricularPlans", labelValueDegreeCurricularPlans);
        request.setAttribute("executionYears", ServiceUtils.executeService(userView, "ReadNotClosedExecutionYears", new Object[] {}));

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        DynaActionForm actionForm = (DynaActionForm) form;
        String executionYearID = (String) actionForm.get("executionYearID");
        request.setAttribute("executionYearID", executionYearID);
        String degreeCurricularPlanID = (String) actionForm.get("degreeCurricularPlanID");
        Object[] args = { Integer.valueOf(degreeCurricularPlanID), Integer.valueOf(executionYearID) };

        final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>)
                executeService(request, "ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", args);
        final ActionErrors errors = new ActionErrors();
        if (degreeModuleScopes.isEmpty()) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.nonExisting.AssociatedCurricularCourses"));
            saveErrors(request, errors);            
        } else {
            request.setAttribute("degreeModuleScopes", degreeModuleScopes);
        }

        request.setAttribute("degreeCurricularPlan", rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(degreeCurricularPlanID)));

        return mapping.findForward("showActiveCurricularCourses");
    }

}