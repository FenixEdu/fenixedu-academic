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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

public class WrittenEvaluationsSearchByDegreeAndYear extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);
        final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        final Object[] args = { ExecutionPeriod.class, infoExecutionPeriod.getIdInternal() };
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(userView, "ReadDomainObject", args);

        final MessageResources enumMessages = MessageResources.getMessageResources("ServidorApresentacao/EnumerationResources");
        final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/PublicDegreeInformation");

        final List<LabelValueBean> executionDegreeLabelValueBeans = new ArrayList<LabelValueBean>();
        for (final IExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
            final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final IDegree degree = degreeCurricularPlan.getDegree();
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

        final IUserView userView = SessionUtils.getUserView(request);

        final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        final Object[] args = { ExecutionPeriod.class, infoExecutionPeriod.getIdInternal() };
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(userView, "ReadDomainObject", args);
        request.setAttribute("executionPeriod", executionPeriod);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final Boolean selectAllCurricularYears = (Boolean) dynaActionForm.get("selectAllCurricularYears");
        final String[] selectedCurricularYears = (String[]) dynaActionForm.get("selectedCurricularYears");
        final String executionDegreeID = (String) dynaActionForm.get("executionDegreeID");

        final Set<Integer> years = new HashSet<Integer>();
        for (final String yearString : selectedCurricularYears) {
        	years.add(Integer.valueOf(yearString));
        }

        final Map<IExecutionDegree, Map<ICurricularYear, Set<IExecutionCourse>>> executionCoursesByCurricularYearByExecutionDegree =
        		new TreeMap<IExecutionDegree, Map<ICurricularYear, Set<IExecutionCourse>>>(new Comparator<IExecutionDegree>() {
					public int compare(IExecutionDegree executionDegree1, IExecutionDegree executionDegree2) {
						final IDegree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
						final IDegree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();
						return (degree1.getTipoCurso() == degree2.getTipoCurso()) ? 
								degree1.getNome().compareTo(degree2.getNome()) :
								degree1.getTipoCurso().compareTo(degree2.getTipoCurso());
					}}); 
        for (final IExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
            if (executionDegreeID == null || executionDegreeID.length() == 0 || executionDegreeID.equals(executionDegree.getIdInternal().toString())) {
            	final Map<ICurricularYear, Set<IExecutionCourse>> executionCoursesByCurricularYear = new TreeMap<ICurricularYear, Set<IExecutionCourse>>(new Comparator<ICurricularYear>() {
					public int compare(final ICurricularYear curricularYear1, final ICurricularYear curricularYear2) {
						return curricularYear1.getYear().compareTo(curricularYear2.getYear());
					}});
            	executionCoursesByCurricularYearByExecutionDegree.put(executionDegree, executionCoursesByCurricularYear);
                for (final ICurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCourses()) {
                	for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod)) {
                		final ICurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
           				final ICurricularYear curricularYear = curricularSemester.getCurricularYear();
           				final Integer year = curricularYear.getYear();
           				if (curricularSemester.getSemester().equals(executionPeriod.getSemester()) &&
           						(selectAllCurricularYears != null && selectAllCurricularYears.booleanValue()) || years.contains(year)) {
           					final Set<IExecutionCourse> executionCourses;
           					if (!executionCoursesByCurricularYear.containsKey(curricularYear)) {
           						executionCourses = new TreeSet<IExecutionCourse>(new Comparator<IExecutionCourse>() {
									public int compare(final IExecutionCourse executionCourse1, final IExecutionCourse executionCourse2) {
										return executionCourse1.getNome().compareTo(executionCourse2.getNome());
									}});
           						executionCoursesByCurricularYear.put(curricularYear, executionCourses);
           					} else {
           						executionCourses = executionCoursesByCurricularYear.get(curricularYear);
           					}
                            for (final IExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                               	if (executionPeriod == executionCourse.getExecutionPeriod()) {
                               		executionCourses.add(executionCourse);
                               	}
                           }
           				}
                	}
                }
            }
        }
        request.setAttribute("executionCoursesByCurricularYearByExecutionDegree", executionCoursesByCurricularYearByExecutionDegree);

        return mapping.findForward("showMap");
    }

}