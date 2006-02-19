package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.util.PeriodState;

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

        final List<Attends> attends = new ArrayList<Attends>();
        for (final Student student : getUserView(request).getPerson().getStudents()) {
            for (final Attends attend : student.getAssociatedAttends()) {
                final ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
                if (executionCourse.getExecutionPeriod() == selectedExecutionPeriod) {
                    attends.add(attend);
                }
            }
        }
        request.setAttribute("attends", attends);

        request.setAttribute("weeklyWorkLoadBean", new WeeklyWorkLoadBean());

        return mapping.findForward("showWeeklyWorkLoad");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        final WeeklyWorkLoadBean weeklyWorkLoadBean = getWeeklyWorkLoadBean(request);

        final Integer attendsID = weeklyWorkLoadBean.getAttendsID();
        final Integer contact = weeklyWorkLoadBean.getContact();
        final Integer autonomousStudy = weeklyWorkLoadBean.getAutonomousStudy();
        final Integer other = weeklyWorkLoadBean.getOther();

        create(request, attendsID, contact, autonomousStudy, other);

        return prepare(mapping, form, request, response);
    }

    public ActionForward createFromForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
    	final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final Integer attendsID = getInteger(dynaActionForm, "attendsID");
        final Integer contact = getInteger(dynaActionForm, "contact");
        final Integer autonomousStudy = getInteger(dynaActionForm, "autonomousStudy");
        final Integer other = getInteger(dynaActionForm, "other");

        create(request, attendsID, contact, autonomousStudy, other);

        return prepare(mapping, form, request, response);
    }

	public void create(final HttpServletRequest request, final Integer attendsID, final Integer contact, final Integer autonomousStudy, final Integer other)
    		throws FenixFilterException, FenixServiceException {
        final Object[] args = { attendsID, contact, autonomousStudy, other };
        executeService(request, "CreateWeeklyWorkLoad", args);
    }

    private WeeklyWorkLoadBean getWeeklyWorkLoadBean(final HttpServletRequest request) {
        ViewState viewState = (ViewState) request.getAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        return (WeeklyWorkLoadBean) viewState.getMetaObject().getObject();
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
