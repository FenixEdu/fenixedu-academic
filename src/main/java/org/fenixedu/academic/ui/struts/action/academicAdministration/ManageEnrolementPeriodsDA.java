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
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EmptyDegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentInstructions;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.EnrolmentPeriodInClassesMobility;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.EnrolmentPeriodInExtraordinarySeasonEvaluations;
import org.fenixedu.academic.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ReingressionPeriod;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.enrolmentPeriods.EnrolmentPeriodType;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.service.services.manager.CreateEnrolmentPeriods;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "manage-enrolment-periods",
        titleKey = "title.manage.enrolement.period", accessGroup = "academic(MANAGE_ENROLMENT_PERIODS)")
@Mapping(module = "academicAdministration", path = "/manageEnrolementPeriods",
        input = "/manageEnrolementPeriods.do?method=prepare&page=0", formBean = "enrolementPeriodsForm")
@Forwards({
        @Forward(name = "editEnrolmentInstructions",
                path = "/academicAdministration/enrolmentPeriodManagement/editEnrolmentInstructions.jsp"),
        @Forward(name = "showEnrolementPeriods", path = "/academicAdministration/enrolmentPeriodManagement/enrolementPeriods.jsp"),
        @Forward(name = "createPeriod", path = "/academicAdministration/enrolmentPeriodManagement/createPeriod.jsp"),
        @Forward(name = "changePeriodValues", path = "/academicAdministration/enrolmentPeriodManagement/changePeriodValues.jsp") })
public class ManageEnrolementPeriodsDA extends FenixDispatchAction {

    static List<Class<? extends EnrolmentPeriod>> VALID_ENROLMENT_PERIODS = Arrays.<Class<? extends EnrolmentPeriod>> asList(
            EnrolmentPeriodInCurricularCourses.class,

            EnrolmentPeriodInSpecialSeasonEvaluations.class,

            EnrolmentPeriodInExtraordinarySeasonEvaluations.class,

            EnrolmentPeriodInClasses.class,

            EnrolmentPeriodInClassesMobility.class,

            EnrolmentPeriodInImprovementOfApprovedEnrolment.class,

            EnrolmentPeriodInCurricularCoursesSpecialSeason.class,

            EnrolmentPeriodInCurricularCoursesFlunkedSeason.class,

            ReingressionPeriod.class);

    public static class EnrolmentPeriodBean implements Serializable {
        private ExecutionSemester semester;

        public EnrolmentPeriodBean() {
        }

        public ExecutionSemester getSemester() {
            return semester;
        }

        public void setSemester(ExecutionSemester semester) {
            this.semester = semester;
        }

        public SortedSet<ExecutionSemester> getSemesters() {
            TreeSet<ExecutionSemester> semesters =
                    new TreeSet<ExecutionSemester>(new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
            semesters.addAll(Bennu.getInstance().getExecutionPeriodsSet());
            return semesters;
        }

        public List<EnrolmentPeriodTypeConfiguration> getConfigurations() {
            List<EnrolmentPeriodTypeConfiguration> configurations = new ArrayList<EnrolmentPeriodTypeConfiguration>();
            if (semester != null) {
                Map<Class<? extends EnrolmentPeriod>, EnrolmentPeriodTypeConfiguration> map =
                        new HashMap<Class<? extends EnrolmentPeriod>, EnrolmentPeriodTypeConfiguration>();
                for (final EnrolmentPeriod period : semester.getEnrolmentPeriodSet()) {
                    if (VALID_ENROLMENT_PERIODS.contains(period.getClass())) {
                        if (!AcademicPredicates.MANAGE_ENROLMENT_PERIODS.evaluate(period.getDegree())) {
                            continue;
                        }

                        if (!map.containsKey(period.getClass())) {
                            map.put(period.getClass(), new EnrolmentPeriodTypeConfiguration(period.getClass(), semester));
                        }
                        map.get(period.getClass()).addPeriod(period);
                    }
                }
                configurations.addAll(map.values());
                Collections.sort(configurations);
            }
            return configurations;
        }
    }

    public static class EnrolmentPeriodTypeConfiguration implements Serializable, Comparable<EnrolmentPeriodTypeConfiguration> {
        protected Class<? extends EnrolmentPeriod> type;

        protected ExecutionSemester semester;

        protected Map<Interval, EnrolmentPeriodConfigurationForEdit> configurations =
                new HashMap<Interval, EnrolmentPeriodConfigurationForEdit>();

        public EnrolmentPeriodTypeConfiguration(Class<? extends EnrolmentPeriod> type, ExecutionSemester semester) {
            this.type = type;
            this.semester = semester;
        }

        public void addPeriod(EnrolmentPeriod period) {
            if (!configurations.containsKey(period.getInterval())) {
                configurations.put(period.getInterval(), new EnrolmentPeriodConfigurationForEdit(period.getInterval(), semester));
            }
            configurations.get(period.getInterval()).addPeriod(period);
        }

        public Class<? extends EnrolmentPeriod> getType() {
            return type;
        }

        public Collection<EnrolmentPeriodConfigurationForEdit> getConfigurations() {
            return configurations.values();
        }

        @Override
        public int compareTo(EnrolmentPeriodTypeConfiguration o) {
            return getType().getSimpleName().compareTo(o.getType().getSimpleName());
        }
    }

    public static abstract class AbstractEnrolmentPeriodConfiguration implements Serializable {
        protected DateTime start;

        protected DateTime end;

        protected ExecutionSemester semester;

        protected List<DegreeCurricularPlan> scope = new ArrayList<DegreeCurricularPlan>();

        public AbstractEnrolmentPeriodConfiguration(Interval interval, ExecutionSemester semester) {
            if (interval != null) {
                this.start = interval.getStart();
                this.end = interval.getEnd();
            }
            this.semester = semester;
        }

        public Interval getInterval() {
            return new Interval(start, end);
        }

        public DateTime getStart() {
            return start;
        }

        public void setStart(DateTime start) {
            this.start = start;
        }

        public DateTime getEnd() {
            return end;
        }

        public void setEnd(DateTime end) {
            this.end = end;
        }

        public ExecutionSemester getSemester() {
            return semester;
        }

        public List<DegreeCurricularPlan> getScope() {
            return scope;
        }

        public void setScope(List<DegreeCurricularPlan> scope) {
            this.scope = scope;
        }

        public abstract SortedSet<DegreeCurricularPlan> getPossibleScope();

        public abstract void save();
    }

    public static class EnrolmentPeriodConfigurationForCreation extends AbstractEnrolmentPeriodConfiguration {
        private DegreeType degreeType;

        private EnrolmentPeriodType type;

        public EnrolmentPeriodConfigurationForCreation(ExecutionSemester semester) {
            super(null, semester);
        }

        public DegreeType getDegreeType() {
            return degreeType;
        }

        public void setDegreeType(DegreeType degreeType) {
            this.degreeType = degreeType;
        }

        public EnrolmentPeriodType getType() {
            return type;
        }

        public void setType(EnrolmentPeriodType type) {
            this.type = type;
        }

        public List<DegreeType> getDegreeTypes() {
            return DegreeType.all().sorted().collect(Collectors.toList());
        }

        @Override
        public SortedSet<DegreeCurricularPlan> getPossibleScope() {
            SortedSet<DegreeCurricularPlan> possible =
                    new TreeSet<DegreeCurricularPlan>(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);
            if (degreeType != null && type != null && semester != null) {
                if (degreeType.isEmpty() && EmptyDegreeCurricularPlan.getInstance() != null) {
                    addIfNotUsedInPeriod(possible, EmptyDegreeCurricularPlan.getInstance());
                } else if (degreeType.isBolonhaType()) {
                    for (ExecutionDegree execution : semester.getExecutionYear().getExecutionDegreesByType(degreeType)) {
                        DegreeCurricularPlan dcp = execution.getDegreeCurricularPlan();
                        addIfNotUsedInPeriod(possible, dcp);
                    }
                    //add curricular plans that still need improvement period and that have transitioned the year before to a new one 
                    if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_IMPROVEMENT_OF_APPROVED_ENROLMENT.equals(type)
                            && semester.getPreviousExecutionPeriod().getExecutionYear() != semester.getExecutionYear()) {
                        for (ExecutionDegree execution : semester.getPreviousExecutionPeriod().getExecutionYear()
                                .getExecutionDegreesByType(degreeType)) {
                            DegreeCurricularPlan dcp = execution.getDegreeCurricularPlan();
                            if (!possible.contains(dcp)) {
                                addIfNotUsedInPeriod(possible, dcp);
                            }
                        }
                    }

                    if (EnrolmentPeriodType.REINGRESSION.equals(type)) {
                        Bennu.getInstance().getDegreeCurricularPlansSet().stream()
                                .filter(dcp -> dcp.getDegreeType().equals(degreeType))
                                .filter(dcp -> !possible.contains(dcp))
                                .forEach(dcp -> addIfNotUsedInPeriod(possible, dcp));
                    }
                } else {
                    for (DegreeCurricularPlan dcp : DegreeCurricularPlan.readPreBolonhaDegreeCurricularPlans()) {
                        addIfNotUsedInPeriod(possible, dcp);
                    }
                }
            }
            return possible;
        }

        private void addIfNotUsedInPeriod(SortedSet<DegreeCurricularPlan> possible, DegreeCurricularPlan dcp) {
            boolean found = false;
            boolean hasAccess = AcademicPredicates.MANAGE_ENROLMENT_PERIODS.evaluate(dcp.getDegree());
            for (EnrolmentPeriod period : dcp.getEnrolmentPeriodsSet()) {
                if (type.is(period) && period.getExecutionPeriod().equals(semester)) {
                    found = true;
                    break;
                }
            }
            if (!found && hasAccess) {
                possible.add(dcp);
            }
        }

        @Override
        public void save() {
            CreateEnrolmentPeriods.run(semester, degreeType, type, start, end, scope);
        }
    }

    public static class EnrolmentPeriodConfigurationForEdit extends AbstractEnrolmentPeriodConfiguration {
        protected Set<EnrolmentPeriod> periods = new HashSet<EnrolmentPeriod>();

        public EnrolmentPeriodConfigurationForEdit(Interval interval, ExecutionSemester semester) {
            super(interval, semester);
        }

        public void addPeriod(EnrolmentPeriod period) {
            if (!AcademicPredicates.MANAGE_ENROLMENT_PERIODS.evaluate(period.getDegree())) {
                return;
            }
            periods.add(period);
            scope.add(period.getDegreeCurricularPlan());
        }

        @Override
        public SortedSet<DegreeCurricularPlan> getPossibleScope() {
            SortedSet<DegreeCurricularPlan> possible =
                    new TreeSet<DegreeCurricularPlan>(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);
            for (EnrolmentPeriod period : periods) {
                possible.add(period.getDegreeCurricularPlan());
            }
            return possible;
        }

        public Collection<String> getDegrees() {
            List<String> degrees = new ArrayList<String>();
            for (EnrolmentPeriod period : periods) {
                degrees.add(period.getDegree().getPresentationName(semester.getExecutionYear()));
            }
            Collections.sort(degrees);
            return degrees;
        }

        public String getPeriodOids() {
            return periods.stream().map(EnrolmentPeriod::getExternalId).collect(Collectors.joining(":"));
        }

        @Override
        @Atomic
        public void save() {
            for (EnrolmentPeriod period : periods) {
                if (scope.contains(period.getDegreeCurricularPlan())) {
                    period.setStartDateDateTime(start);
                    period.setEndDateDateTime(end);
                }
            }
        }
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ExecutionSemester semester = getDomainObject(request, "semester");
        EnrolmentPeriodBean bean = new EnrolmentPeriodBean();
        if (semester != null) {
            bean.setSemester(semester);
        }
        request.setAttribute("executionSemester", bean);
        return mapping.findForward("showEnrolementPeriods");
    }

    public ActionForward selectSemester(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        EnrolmentPeriodBean bean = getRenderedObject("executionSemester");
        RenderUtils.invalidateViewState();
        request.setAttribute("executionSemester", bean);
        return mapping.findForward("showEnrolementPeriods");
    }

    public ActionForward prepareEditEnrolmentInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionSemester semester = getDomainObject(request, "semester");
        EnrolmentInstructions.createIfNecessary(semester);
        request.setAttribute("executionSemester", semester);

        return mapping.findForward("editEnrolmentInstructions");
    }

    public ActionForward prepareChangePeriodValues(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ExecutionSemester semester = getDomainObject(request, "semester");
        String periodOids = request.getParameter("periods");
        EnrolmentPeriodConfigurationForEdit conf = null;
        for (String periodOid : periodOids.split(":")) {
            EnrolmentPeriod period = FenixFramework.getDomainObject(periodOid);
            if (conf == null) {
                conf = new EnrolmentPeriodConfigurationForEdit(period.getInterval(), semester);
                conf.addPeriod(period);
            } else if (conf.getInterval().equals(period.getInterval())) {
                conf.addPeriod(period);
            } else {
                // something went wrong, most likely someone changed a date concurrently. we just leave it out of the edit
            }
        }
        request.setAttribute("configuration", conf);
        return mapping.findForward("changePeriodValues");
    }

    public ActionForward changePeriodValues(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EnrolmentPeriodConfigurationForEdit configuration = getRenderedObject("configuration");
        configuration.save();
        EnrolmentPeriodBean bean = new EnrolmentPeriodBean();
        bean.setSemester(configuration.getSemester());
        request.setAttribute("executionSemester", bean);
        return mapping.findForward("showEnrolementPeriods");
    }

    public ActionForward prepareCreatePeriod(final ActionMapping mapping, final ActionForm form, HttpServletRequest request,
            final HttpServletResponse response) {
        ExecutionSemester semester = getDomainObject(request, "semester");
        EnrolmentPeriodConfigurationForCreation conf = new EnrolmentPeriodConfigurationForCreation(semester);
        request.setAttribute("configuration", conf);
        return mapping.findForward("createPeriod");
    }

    public ActionForward selectType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        EnrolmentPeriodConfigurationForCreation configuration = getRenderedObject("configuration");
        RenderUtils.invalidateViewState();
        request.setAttribute("configuration", configuration);
        return mapping.findForward("createPeriod");
    }

    public ActionForward createPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EnrolmentPeriodConfigurationForCreation configuration = getRenderedObject("configuration");
        configuration.save();
        EnrolmentPeriodBean bean = new EnrolmentPeriodBean();
        bean.setSemester(configuration.getSemester());
        request.setAttribute("executionSemester", bean);
        return mapping.findForward("showEnrolementPeriods");
    }
}
