package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.text.Collator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        final Object[] args = { ExecutionPeriod.class };
        final List<ExecutionPeriod> executionPeriods = (List<ExecutionPeriod>) executeService(request, "ReadAllDomainObjects", args);
        final Set<ExecutionPeriod> sortedExecutionPeriods = new TreeSet<ExecutionPeriod>(executionPeriods);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final Integer executionPeriodID = getExecutionPeriodID(dynaActionForm);
        final ExecutionPeriod selectedExecutionPeriod = findExecutionPeriod(executionPeriods, executionPeriodID);
        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());

        final String degreeCurricularPlanIDString = (String) dynaActionForm.get("degreeCurricularPlanID");
        final Integer degreeCurricularPlanID = degreeCurricularPlanIDString == null || degreeCurricularPlanIDString.length() == 0 ?
        		null : Integer.valueOf(degreeCurricularPlanIDString);
        final Object[] args2 = { DegreeCurricularPlan.class, degreeCurricularPlanID };
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) executeService(request, "ReadDomainObject", args2);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        final Map<CurricularSemester, Set<ExecutionCourse>> executionCoursesMap = new TreeMap<CurricularSemester, Set<ExecutionCourse>>();
        if (degreeCurricularPlan != null) {
        	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        		for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
        			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
        			final Set<ExecutionCourse> executionCourses;
        			if (executionCoursesMap.containsKey(curricularSemester)) {
        				executionCourses = executionCoursesMap.get(curricularSemester);
        			} else {
        				executionCourses = new TreeSet<ExecutionCourse>(new BeanComparator("nome", Collator.getInstance()));
        				executionCoursesMap.put(curricularSemester, executionCourses);
        			}

        			for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
        				executionCourses.add(executionCourse);
        			}
        		}
        	}
        }
        request.setAttribute("executionCoursesMap", executionCoursesMap);

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private Integer getExecutionPeriodID(final DynaActionForm dynaActionForm) {
        final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : Integer.valueOf(exeutionPeriodIDString);
    }

    private ExecutionPeriod findExecutionPeriod(final List<ExecutionPeriod> executionPeriods, final Integer executionPeriodID) {
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            if (executionPeriodID == null && executionPeriod.getState().equals(PeriodState.CURRENT)) {
                return executionPeriod;
            }
            if (executionPeriodID != null && executionPeriod.getIdInternal().equals(executionPeriodID)) {
                return executionPeriod;
            }
        }
        return null;
    }

}
