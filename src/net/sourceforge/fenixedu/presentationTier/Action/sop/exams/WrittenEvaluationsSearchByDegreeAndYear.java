package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class WrittenEvaluationsSearchByDegreeAndYear extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());

        final MessageResources enumMessages = MessageResources.getMessageResources("resources/EnumerationResources");
        final MessageResources messages = MessageResources.getMessageResources("resources/PublicDegreeInformation");

        final List<LabelValueBean> executionDegreeLabelValueBeans = new ArrayList<LabelValueBean>();
        for (final ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            executionDegreeLabelValueBeans.add(new LabelValueBean(StringAppender.append(
                    enumMessages.getMessage(getLocale(request), degree.getTipoCurso().toString()),
                    " ",
                    messages.getMessage(getLocale(request), "public.degree.information.label.in"),
                    " ",
                    degree.getNome()),
                    executionDegree.getIdInternal().toString()));
        }
        Collections.sort(executionDegreeLabelValueBeans, new BeanComparator("label"));
        request.setAttribute("executionDegreeLabelValueBeans", executionDegreeLabelValueBeans);

        return mapping.findForward("showForm");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
    	prepareInformationToList(form, request);
        return mapping.findForward("showMap");
    }
    
    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	prepareInformationToList(form, request);
        return mapping.findForward("printMap");
    }
    
    private void prepareInformationToList(final ActionForm form, final HttpServletRequest request) {
    	
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionPeriodId = Integer.valueOf(dynaActionForm.getString(SessionConstants.EXECUTION_PERIOD_OID));        
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        request.setAttribute("executionPeriod", executionPeriod);
        
        final Boolean selectAllCurricularYears = (Boolean) dynaActionForm.get("selectAllCurricularYears");
        final String[] selectedCurricularYears = (String[]) dynaActionForm.get("selectedCurricularYears");
        final String executionDegreeID = (String) dynaActionForm.get("executionDegreeID");

        final Set<Integer> years = new HashSet<Integer>();
        for (final String yearString : selectedCurricularYears) {
        	years.add(Integer.valueOf(yearString));
        }

        final Map<ExecutionDegree, Map<Integer, Set<ExecutionCourse>>> executionCoursesByCurricularYearByExecutionDegree =
        		new TreeMap<ExecutionDegree, Map<Integer, Set<ExecutionCourse>>>(new Comparator<ExecutionDegree>() {
					public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
						final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
						final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();
						return (degree1.getTipoCurso() == degree2.getTipoCurso()) ? 
								degree1.getNome().compareTo(degree2.getNome()) :
								degree1.getTipoCurso().compareTo(degree2.getTipoCurso());
					}}); 
        for (final ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
            if (executionDegreeID == null || executionDegreeID.length() == 0 || executionDegreeID.equals(executionDegree.getIdInternal().toString())) {
            	final Map<Integer, Set<ExecutionCourse>> executionCoursesByCurricularYear = new TreeMap<Integer, Set<ExecutionCourse>>(new Comparator<Integer>() {
					public int compare(final Integer curricularYear1, final Integer curricularYear2) {
						return curricularYear1.compareTo(curricularYear2);
					}});
            	executionCoursesByCurricularYearByExecutionDegree.put(executionDegree, executionCoursesByCurricularYear);
                for (final CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCourses()) {
                	for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                        if(degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)) {
                    		final Integer curricularSemester = degreeModuleScope.getCurricularSemester();
               				final Integer curricularYear = degreeModuleScope.getCurricularYear();               				
               				if (curricularSemester.equals(executionPeriod.getSemester()) &&
                                    (selectAllCurricularYears != null && selectAllCurricularYears.booleanValue()) || years.contains(curricularYear)) {
               					
                                final Set<ExecutionCourse> executionCourses;
               					if (!executionCoursesByCurricularYear.containsKey(curricularYear)) {
               						executionCourses = new TreeSet<ExecutionCourse>(new Comparator<ExecutionCourse>() {
    									public int compare(final ExecutionCourse executionCourse1, final ExecutionCourse executionCourse2) {
    										return executionCourse1.getNome().compareTo(executionCourse2.getNome());
    									}});
               						executionCoursesByCurricularYear.put(curricularYear, executionCourses);
               					} else {
               						executionCourses = executionCoursesByCurricularYear.get(curricularYear);
               					}
                                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                                   	if (executionPeriod == executionCourse.getExecutionPeriod()) {
                                   		executionCourses.add(executionCourse);
                                   	}
                               }
               				}
                    	}
                    }
                }
            }
        }
        request.setAttribute("executionCoursesByCurricularYearByExecutionDegree", executionCoursesByCurricularYearByExecutionDegree);    	
    }
    
}