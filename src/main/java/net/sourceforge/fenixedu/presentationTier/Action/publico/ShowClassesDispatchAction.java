/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadDegreeByOID;
import net.sourceforge.fenixedu.commons.collections.Table;
import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showClasses", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-classes-list", path = "df.page.showClassesList") })
public class ShowClassesDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextUtils.setExecutionPeriodContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public Degree getDegree(HttpServletRequest request) throws FenixActionException {
        final Degree degree = ShowDegreeSiteAction.getDegree(request);
        if (degree != null) {
            request.setAttribute("degreeID", degree.getExternalId());
            request.setAttribute("degree", degree);
        }
        return degree;
    }

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Degree degree = getDegree(request);

        getInfoDegreeCurricularPlan(request, degree);

        final String executionPeriodID =
                ((InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD)).getExternalId();
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);

        if (executionSemester != null) {
            final ExecutionSemester nextExecutionPeriod = executionSemester.getNextExecutionPeriod();
            ExecutionSemester otherExecutionPeriodToShow = null;
            if (nextExecutionPeriod != null) {
                final User userview = Authenticate.getUser();
                if (nextExecutionPeriod.getState() == PeriodState.OPEN) {
                    otherExecutionPeriodToShow = nextExecutionPeriod;
                } else if (userview != null && nextExecutionPeriod.getState() == PeriodState.NOT_OPEN) {
                    if (userview.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)
                            || userview.getPerson().hasRole(RoleType.COORDINATOR)) {
                        otherExecutionPeriodToShow = nextExecutionPeriod;
                    }
                }
            }
            if (otherExecutionPeriodToShow == null) {
                otherExecutionPeriodToShow = executionSemester.getPreviousExecutionPeriod();
            }
            organizeClassViewsNext(request, degree, executionSemester, otherExecutionPeriodToShow);
            request.setAttribute("nextInfoExecutionPeriod", InfoExecutionPeriod.newInfoFromDomain(otherExecutionPeriodToShow));
        }
        return mapping.findForward("show-classes-list");
    }

    private void organizeClassViewsNext(final HttpServletRequest request, final Degree degree,
            final ExecutionSemester executionSemester, final ExecutionSemester otherExecutionPeriodToShow) {
        request.setAttribute("classViewsTableCurrent", organizeClassViews(degree, executionSemester));
        request.setAttribute("classViewsTableNext", organizeClassViews(degree, otherExecutionPeriodToShow));
    }

    private Table organizeClassViews(final Degree degree, final ExecutionSemester executionSemester) {
        final Table classViewsTable = new Table(degree.buildFullCurricularYearList().size());

        final DegreeCurricularPlan degreeCurricularPlan = findMostRecentDegreeCurricularPlan(degree, executionSemester);

        final SortedSet<SchoolClass> schoolClasses = new TreeSet<SchoolClass>(new BeanComparator("nome"));
        for (final SchoolClass schoolClass : executionSemester.getSchoolClasses()) {
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

    private DegreeCurricularPlan findMostRecentDegreeCurricularPlan(final Degree degree, final ExecutionSemester executionSemester) {
        DegreeCurricularPlan result = null;
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            if (hasExecutionDegreeForExecutionPeriod(degreeCurricularPlan, executionSemester)) {
                if (result == null
                        || degreeCurricularPlan.getInitialDateYearMonthDay().compareTo(result.getInitialDateYearMonthDay()) > 0) {
                    result = degreeCurricularPlan;
                }
            }
        }
        return result;
    }

    private boolean hasExecutionDegreeForExecutionPeriod(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester) {
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
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

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Degree degree) {

        InfoDegree infoDegree = ReadDegreeByOID.run(degree.getExternalId());
        request.setAttribute("infoDegree", infoDegree);
    }

}
