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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree;
import net.sourceforge.fenixedu.commons.collections.Table;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showExecutionCourseSites", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-exeution-course-site-list", path = "df.page.showDegreeCurricularPlanSites") })
public class ShowExecutionCourseSitesDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextUtils.setExecutionPeriodContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward listSites(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Degree degree = ShowDegreeSiteAction.getDegree(request);

        // degreeID
        final String degreeOID = FenixContextDispatchAction.getFromRequest("degreeID", request);
        getDegreeAndSetInfoDegree(request, degreeOID);
        final List<ExecutionCourseView> executionCourseViews = getExecutionCourseViews(request, degree);
        final InfoExecutionPeriod infoExecutionPeriod = getPreviousExecutionPeriod(request);

        organizeExecutionCourseViews(request, executionCourseViews, infoExecutionPeriod);

        return mapping.findForward("show-exeution-course-site-list");
    }

    private Degree getDegreeAndSetInfoDegree(HttpServletRequest request, String degreeOID) {
        final Degree degree = FenixFramework.getDomainObject(degreeOID);

        final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        request.setAttribute("infoDegree", infoDegree);

        return degree;
    }

    private InfoExecutionPeriod getPreviousExecutionPeriod(HttpServletRequest request) {
        final InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        final ExecutionSemester executionSemester = infoExecutionPeriod.getExecutionPeriod();
        final ExecutionSemester nextExecutionSemester = executionSemester.getNextExecutionPeriod();
        final InfoExecutionPeriod previousInfoExecutionPeriod;
        if (nextExecutionSemester != null && nextExecutionSemester.getState().equals(PeriodState.OPEN)) {
            previousInfoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(nextExecutionSemester);
        } else {
            previousInfoExecutionPeriod = infoExecutionPeriod.getPreviousInfoExecutionPeriod();
        }

        request.setAttribute("previousInfoExecutionPeriod", previousInfoExecutionPeriod);
        return previousInfoExecutionPeriod;
    }

    private List<ExecutionCourseView> getExecutionCourseViews(HttpServletRequest request, Degree degree)
            throws FenixServiceException {

        List<ExecutionCourseView> result = new ArrayList(ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree.run(degree));
        Collections.sort(result, ExecutionCourseView.COMPARATOR_BY_NAME);

        return result;
    }

    private void organizeExecutionCourseViews(HttpServletRequest request, List<ExecutionCourseView> executionCourseViews,
            InfoExecutionPeriod previousExecutionPeriod) {
        Table executionCourseViewsTableCurrent1_2 = new Table(2);
        Table executionCourseViewsTableCurrent3_4 = new Table(2);
        Table executionCourseViewsTableCurrent5 = new Table(1);
        Table executionCourseViewsTablePrevious1_2 = new Table(2);
        Table executionCourseViewsTablePrevious3_4 = new Table(2);
        Table executionCourseViewsTablePrevious5 = new Table(1);

        for (ExecutionCourseView executionCourseView : executionCourseViews) {
            int curricularYear = executionCourseView.getCurricularYear();
            boolean previousExecPeriod =
                    previousExecutionPeriod != null
                            && executionCourseView.getExecutionPeriodOID().equals(previousExecutionPeriod.getExternalId());

            switch (curricularYear) {
            case 1:
            case 2:
                if (previousExecPeriod) {
                    executionCourseViewsTablePrevious1_2.appendToColumn(curricularYear - 1, executionCourseView);
                } else {
                    executionCourseViewsTableCurrent1_2.appendToColumn(curricularYear - 1, executionCourseView);
                }
                break;
            case 3:
            case 4:
                if (previousExecPeriod) {
                    executionCourseViewsTablePrevious3_4.appendToColumn(curricularYear - 3, executionCourseView);
                } else {
                    executionCourseViewsTableCurrent3_4.appendToColumn(curricularYear - 3, executionCourseView);
                }
                break;
            case 5:
                if (previousExecPeriod) {
                    executionCourseViewsTablePrevious5.appendToColumn(curricularYear - 5, executionCourseView);
                } else {
                    executionCourseViewsTableCurrent5.appendToColumn(curricularYear - 5, executionCourseView);
                }
                break;
            }
        }

        request.setAttribute("executionCourseViewsTableCurrent1_2", executionCourseViewsTableCurrent1_2);
        request.setAttribute("executionCourseViewsTableCurrent3_4", executionCourseViewsTableCurrent3_4);
        request.setAttribute("executionCourseViewsTableCurrent5", executionCourseViewsTableCurrent5);

        request.setAttribute("executionCourseViewsTablePrevious1_2", executionCourseViewsTablePrevious1_2);
        request.setAttribute("executionCourseViewsTablePrevious3_4", executionCourseViewsTablePrevious3_4);
        request.setAttribute("executionCourseViewsTablePrevious5", executionCourseViewsTablePrevious5);
    }

}