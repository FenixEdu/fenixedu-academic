package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.collections.Table;
import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowClassesDispatchAction extends FenixContextDispatchAction {

    public Degree getDegree(HttpServletRequest request) throws FenixActionException {
	final Degree degree = ShowDegreeSiteAction.getDegree(request);
	if (degree != null) {
	    request.setAttribute("degreeID", degree.getIdInternal());
	    request.setAttribute("degree", degree);
	}
	return degree;
    }

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Degree degree = getDegree(request);

        getInfoDegreeCurricularPlan(request, degree);

        final Integer executionPeriodID = ((InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD)).getIdInternal();
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        if (executionPeriod != null) {
        	final ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
        	final ExecutionPeriod otherExecutionPeriodToShow =
        		(nextExecutionPeriod != null && nextExecutionPeriod .getState() != PeriodState.NOT_OPEN) ?
        				nextExecutionPeriod : executionPeriod.getPreviousExecutionPeriod();
        	organizeClassViewsNext(request, degree, executionPeriod, otherExecutionPeriodToShow);
        	request.setAttribute("nextInfoExecutionPeriod", InfoExecutionPeriod.newInfoFromDomain(otherExecutionPeriodToShow));
        }

        return mapping.findForward("show-classes-list");
    }

    private void organizeClassViewsNext(final HttpServletRequest request, final Degree degree,
    		final ExecutionPeriod executionPeriod, final ExecutionPeriod otherExecutionPeriodToShow) {
        request.setAttribute("classViewsTableCurrent", organizeClassViews(degree, executionPeriod));
        request.setAttribute("classViewsTableNext", organizeClassViews(degree, otherExecutionPeriodToShow));
    }

    private Table organizeClassViews(final Degree degree, final ExecutionPeriod executionPeriod) {
        final Table classViewsTable = new Table(degree.buildFullCurricularYearList().size());

        final DegreeCurricularPlan degreeCurricularPlan = findMostRecentDegreeCurricularPlan(degree, executionPeriod);

        final SortedSet<SchoolClass> schoolClasses = new TreeSet<SchoolClass>(new BeanComparator("nome"));
        for (final SchoolClass schoolClass : executionPeriod.getSchoolClasses()) {
        	final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        	if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
        		schoolClasses.add(schoolClass);
        	}
        }

        for (final SchoolClass schoolClass : schoolClasses) {
        	classViewsTable.appendToColumn(schoolClass.getAnoCurricular().intValue() - 1, newClassView(schoolClass));
        }

        return classViewsTable;
    }

    private DegreeCurricularPlan findMostRecentDegreeCurricularPlan(final Degree degree, final ExecutionPeriod executionPeriod) {
	DegreeCurricularPlan result = null;
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    if (hasExecutionDegreeForExecutionPeriod(degreeCurricularPlan, executionPeriod)) {
		if (result == null || degreeCurricularPlan.getInitialDateYearMonthDay().compareTo(result.getInitialDateYearMonthDay()) > 0) {
		    result = degreeCurricularPlan;
		}
	    }
	}
	return result;
    }

    private boolean hasExecutionDegreeForExecutionPeriod(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionPeriod executionPeriod) {
	final ExecutionYear executionYear = executionPeriod.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    if (degreeCurricularPlan == executionDegree.getDegreeCurricularPlan()) {
		return true;
	    }
	}
	return false;
    }

    private ClassView newClassView(final SchoolClass schoolClass) {
    	return new ClassView(schoolClass);
	}

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Degree degree) throws FenixServiceException, FenixFilterException {
        Object[] args = { degree.getIdInternal() };
        InfoDegree infoDegree = (InfoDegree) ServiceManagerServiceFactory.executeService(null, "ReadDegreeByOID", args);
        request.setAttribute("infoDegree", infoDegree);
    }

}
