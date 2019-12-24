/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.dto.resourceAllocationManager.ContextSelectionBean;
import org.fenixedu.academic.dto.resourceAllocationManager.StudentContextSelectionBean;
import org.fenixedu.academic.ui.struts.action.base.FenixContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz &amp; Sara Ribeiro
 */

@StrutsFunctionality(app = RAMSchedulesApp.class, path = "manage", titleKey = "link.schedules.chooseContext")
@Mapping(path = "/chooseExecutionPeriod", module = "resourceAllocationManager")
@Forwards({
        @Forward(name = "toggleFirstYearShiftsCapacity",
                path = "/resourceAllocationManager/toggleFirstYearShiftsCapacity_bd.jsp"),
        @Forward(name = "showForm", path = "/resourceAllocationManager/chooseExecutionPeriod_bd.jsp") })
public class ExecutionPeriodDA extends FenixContextDispatchAction {

    static private final Integer FIRST_CURRICULAR_YEAR = Integer.valueOf(1);

    private static final Comparator<ExecutionDegree> executionDegreeComparator = new Comparator<ExecutionDegree>() {
        @Override
        public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
            final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
            final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();

            int degreeTypeComparison = degree1.getDegreeType().compareTo(degree2.getDegreeType());
            return (degreeTypeComparison != 0) ? degreeTypeComparison : degree1.getNome().compareTo(degree2.getNome());
        }
    };

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return prepare(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ContextSelectionBean contextSelectionBean =
                (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);

        final StudentContextSelectionBean studentContextSelectionBean =
                new StudentContextSelectionBean(contextSelectionBean.getAcademicInterval());
        request.setAttribute("studentContextSelectionBean", studentContextSelectionBean);

        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(
                ExecutionDegree.filterByAcademicInterval(contextSelectionBean.getAcademicInterval()));
        Collections.sort(executionDegrees, executionDegreeComparator);
        request.setAttribute("executionDegrees", executionDegrees);
        ExecutionInterval executionSemester =
                (ExecutionInterval) ExecutionInterval.getExecutionInterval(contextSelectionBean.getAcademicInterval());
        request.setAttribute("executionSemester", executionSemester);

        AcademicCalendarEntry academicCalendarEntry = contextSelectionBean.getAcademicInterval().getAcademicCalendarEntry();
        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
            if (academicCalendarEntry instanceof AcademicYearCE) {
                ExecutionYear year = ExecutionYear.getExecutionYear((AcademicYearCE) academicCalendarEntry);
                request.setAttribute("executionYear", year);
                break;
            } else {
                academicCalendarEntry = academicCalendarEntry.getParentEntry();
            }
        }

        if (!executionSemester.isCurrent()) {
            request.setAttribute("noEditionAllowed", true);
        }

        return mapping.findForward("showForm");
    }

    public ActionForward toggleFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("executionYear", getDomainObject(request, "executionYearId"));
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    public ActionForward blockFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear executionYear = getDomainObject(request, "executionYearId");
        Map<ExecutionDegree, Integer> modified = setFirstYearShiftsCapacity(true, executionYear);

        request.setAttribute("affectedDegrees", getAffectedDegreesInfo(modified));
        request.setAttribute("executionYear", executionYear);
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    public ActionForward unblockFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear executionYear = getDomainObject(request, "executionYearId");
        Map<ExecutionDegree, Integer> modified = setFirstYearShiftsCapacity(false, executionYear);
        request.setAttribute("affectedDegrees", getAffectedDegreesInfo(modified));
        request.setAttribute("executionYear", executionYear);
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    private List<String> getAffectedDegreesInfo(Map<ExecutionDegree, Integer> modified) {
        List<String> modifiedDescription = new ArrayList<String>();

        for (Map.Entry<ExecutionDegree, Integer> entry : modified.entrySet()) {
            modifiedDescription.add(new String(entry.getKey().getPresentationName() + " :  " + entry.getValue()));
        }
        Collections.sort(modifiedDescription);
        return modifiedDescription;
    }

    @Atomic
    private Map<ExecutionDegree, Integer> setFirstYearShiftsCapacity(Boolean toBlock, ExecutionYear executionYear) {

        final ExecutionInterval executionInterval = executionYear.getFirstExecutionPeriod();

        final Map<Shift, Set<ExecutionDegree>> shiftsDegrees = new HashMap<Shift, Set<ExecutionDegree>>();
        final Set<Shift> shifts = new HashSet<Shift>();
        final Map<ExecutionDegree, Integer> modified = new HashMap<ExecutionDegree, Integer>();

        for (final Degree degree : Degree
                .readAllMatching(DegreeType.oneOf(DegreeType::isBolonhaDegree, DegreeType::isIntegratedMasterDegree))) {
            for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
                final ExecutionDegree executionDegree = degreeCurricularPlan
                        .getExecutionDegreeByAcademicInterval(executionInterval.getExecutionYear().getAcademicInterval());

                if (executionDegree != null) {
                    for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
                        if (schoolClass.getAnoCurricular().equals(FIRST_CURRICULAR_YEAR)
                                && schoolClass.getExecutionInterval() == executionInterval) {
                            for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
                                Set<ExecutionDegree> executionDegrees = shiftsDegrees.get(shift);
                                if (executionDegrees == null) {
                                    executionDegrees = new HashSet<ExecutionDegree>();
                                }
                                executionDegrees.add(executionDegree);
                                shiftsDegrees.put(shift, executionDegrees);
                                shifts.add(shift);
                            }
                        }
                    }
                }
            }
        }

        for (final Shift shift : shifts) {
            int capacity = shift.getLotacao().intValue();

            if (toBlock && capacity > 0) {
                shift.setLotacao(capacity * -1);
            } else if (!toBlock && capacity < 0) {
                shift.setLotacao(capacity * -1);
            } else {
                continue;
            }

            for (ExecutionDegree executionDegree : shiftsDegrees.get(shift)) {
                if (modified.containsKey(executionDegree)) {
                    modified.put(executionDegree, modified.get(executionDegree) + 1);
                } else {
                    modified.put(executionDegree, 1);
                }
            }
        }

        return modified;
    }

}
