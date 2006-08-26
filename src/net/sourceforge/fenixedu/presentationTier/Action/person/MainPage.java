package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Interval;

public final class MainPage extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        final Person person = getLoggedPerson(request);

    	final List advisories = new ArrayList(person.getAdvisories());
    	Collections.sort(advisories, new ReverseComparator(new BeanComparator("created")));
    	request.setAttribute(SessionConstants.LIST_ADVISORY, advisories);

        addStudentNotifications(person, advisories);

    	if (!advisories.isEmpty()) {
    		final String selectedOidString = request.getParameter("activeAdvisory");
    		final Integer selectedOid = (selectedOidString != null) ?
    				Integer.valueOf(selectedOidString) : getIdInternal(advisories.get(0));
    		request.setAttribute("activeAdvisory", selectedOid);
    	}

        return mapping.findForward("ShowWelcomePage");
    }

    private Integer getIdInternal(Object object) {
        if (object instanceof Advisory) {
            return ((Advisory) object).getIdInternal();
        } else if (object instanceof AdvisoryBean) {
            return ((AdvisoryBean) object).getIdInternal();
        } else {
            return null;
        }
    }

    private void addStudentNotifications(final Person person, final List<Advisory> advisories) throws FenixActionException {
        for (final Registration registration : person.getStudents()) {
            for (final Attends attends : registration.getAssociatedAttends()) {
                //if (attends.getEnrolment() != null) {
                    final ExecutionPeriod executionPeriod = attends.getDisciplinaExecucao().getExecutionPeriod();
                    if (executionPeriod.getState().equals(PeriodState.CURRENT) && attends.hasEnrolment()) {
                        final Interval responseWeek = attends.getResponseWeek();
                        if (responseWeek != null) {
                            boolean hasResponse = false;
                            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
                                if (weeklyWorkLoad.getInterval().equals(responseWeek)) {
                                    hasResponse = true;
                                }
                            }
                            if (!hasResponse) {
                                addAdvisory(advisories, attends, responseWeek);
                            }
                        }
                    }
                //}
            }
        }
    }

    private void addAdvisory(final List advisories, final Attends attends, final Interval responseWeek) throws FenixActionException {
        final Object maxAdvisoryByIdInternal;
        if (!advisories.isEmpty()) {
            maxAdvisoryByIdInternal = Collections.max(advisories, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                final Integer idInternal1 = getIdInternal(o1);
                final Integer idInternal2 = getIdInternal(o2);
                return idInternal1.compareTo(idInternal2);
            }});
        } else {
            maxAdvisoryByIdInternal = null;
        }

        final int maxId;
        if (maxAdvisoryByIdInternal == null) {
            maxId = 1;
        } else if (maxAdvisoryByIdInternal instanceof Advisory) {
            maxId = ((Advisory) maxAdvisoryByIdInternal).getIdInternal().intValue();
        } else if (maxAdvisoryByIdInternal instanceof AdvisoryBean) {
            maxId = ((AdvisoryBean) maxAdvisoryByIdInternal).getIdInternal().intValue();
        } else {
            throw new FenixActionException("unsupported.advisory.type");
        }

        advisories.add(0, new AdvisoryBean(maxId + 1, attends, responseWeek));
    }

}