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
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowClassesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer degreeOID = new Integer(request.getParameter("degreeID"));
        request.setAttribute("degreeID", degreeOID);
        request.setAttribute("degree", rootDomainObject.readDegreeByOID(degreeOID));
        
        getInfoDegreeCurricularPlan(request, degreeOID);

        final Integer executionPeriodID = ((InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD)).getIdInternal();
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

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

        final SortedSet<SchoolClass> schoolClasses = new TreeSet<SchoolClass>(new BeanComparator("nome"));
        for (final SchoolClass schoolClass : executionPeriod.getSchoolClasses()) {
        	final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        	if (degreeCurricularPlan.getDegree() == degree) {
        		schoolClasses.add(schoolClass);
        	}
        }

        for (final SchoolClass schoolClass : schoolClasses) {
        	classViewsTable.appendToColumn(schoolClass.getAnoCurricular().intValue() - 1, newClassView(schoolClass));
        }

        return classViewsTable;
    }

    private ClassView newClassView(final SchoolClass schoolClass) {
    	return new ClassView(schoolClass);
	}

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Integer degreeOID) throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };
        InfoDegree infoDegree = (InfoDegree) ServiceManagerServiceFactory.executeService(null, "ReadDegreeByOID", args);
        request.setAttribute("infoDegree", infoDegree);
    }

}
