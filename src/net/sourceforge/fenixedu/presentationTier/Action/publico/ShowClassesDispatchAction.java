/*
 * Created 2004/11/7
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz
 */
public class ShowClassesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer degreeOID = new Integer(request.getParameter("degreeOID"));
        request.setAttribute("degreeID", degreeOID);

        final String language = getLocaleLanguageFromRequest(request);
        getInfoDegreeCurricularPlan(request, degreeOID, language);

        final IUserView userView = SessionUtils.getUserView(request);
        final Integer executionPeriodID = ((InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD)).getIdInternal();
        final Object[] args1 = { ExecutionPeriod.class, executionPeriodID };
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView, "ReadDomainObject", args1);

        final Object[] args2 = { Degree.class, degreeOID };
        final Degree degree = (Degree) ServiceUtils.executeService(userView, "ReadDomainObject", args2);

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
        final Table classViewsTable = new Table(5);

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
    	final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
    	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
    	final Degree degree = degreeCurricularPlan.getDegree();

    	final ClassView classView = new ClassView();
    	classView.setClassName(schoolClass.getNome());
    	classView.setClassOID(schoolClass.getIdInternal());
    	classView.setCurricularYear(schoolClass.getAnoCurricular());
    	classView.setDegreeCurricularPlanID(degreeCurricularPlan.getIdInternal());
    	classView.setDegreeInitials(degree.getSigla());
    	classView.setExecutionPeriodOID(schoolClass.getExecutionPeriod().getIdInternal());
    	classView.setNameDegreeCurricularPlan(degreeCurricularPlan.getName());
    	classView.setSemester(schoolClass.getExecutionPeriod().getSemester());
    	return classView;
	}

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Integer degreeOID,String language)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        InfoDegree infoDegree = (InfoDegree) ServiceManagerServiceFactory.executeService(null,
                "ReadDegreeByOID", args);
        infoDegree.prepareEnglishPresentation(language);
        request.setAttribute("infoDegree", infoDegree);
    }

    private String getLocaleLanguageFromRequest(HttpServletRequest request) {
        Locale locale = (Locale) request.getSession(false).getAttribute(Globals.LOCALE_KEY);
        return  locale.getLanguage();

    }
}