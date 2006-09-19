/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
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

    public ActionForward chooseDegreeCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        DynaActionForm actionForm = (DynaActionForm) form;
        String executionYearID = (String) actionForm.get("executionYearID");
        IUserView userView = SessionUtils.getUserView(request);
        List executionYears = null;
        List degreeCurricularPlans = null;

        try {
            executionYears = (List) ServiceUtils.executeService(userView, "ReadNotClosedExecutionYears",
                    new Object[] {});
            if (executionYearID != null && executionYearID.length() > 0) {
                Object[] args = new Object[] { new Integer(executionYearID) };
                degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                        "ReadActiveDegreeCurricularPlansByExecutionYear", args);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        
        List labelValueDegreeCurricularPlans = null; 
        
        if (degreeCurricularPlans != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
            comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
            comparatorChain.addComparator(new BeanComparator("name"));
            Collections.sort(degreeCurricularPlans, comparatorChain);

            labelValueDegreeCurricularPlans = (List) CollectionUtils.collect(degreeCurricularPlans,
                    new Transformer() {
                        public Object transform(Object obj) {
                            InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) obj;
                            LabelValueBean valueBean = new LabelValueBean(infoDegreeCurricularPlan
                                    .getInfoDegree().getNome()
                                    + " - " + infoDegreeCurricularPlan.getName(),
                                    infoDegreeCurricularPlan.getIdInternal().toString());
                            return valueBean;
                        }
                    });
        }
                
        request.setAttribute("degreeCurricularPlans", labelValueDegreeCurricularPlans);
        request.setAttribute("executionYears", executionYears);
        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        DynaActionForm actionForm = (DynaActionForm) form;
        String executionYearID = (String) actionForm.get("executionYearID");
        request.setAttribute("executionYearID", executionYearID);
        String degreeCurricularPlanID = (String) actionForm.get("degreeCurricularPlanID");
        Object[] args2 = { new Integer(degreeCurricularPlanID), new Integer(executionYearID) };

        final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>)
                executeService(request, "ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", args2);
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