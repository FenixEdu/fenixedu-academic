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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateEnrolmentPeriods;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentInstructions;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ReingressionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodType;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

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

            EnrolmentPeriodInClasses.class,

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
            semesters.addAll(rootDomainObject.getExecutionPeriodsSet());
            return semesters;
        }

        public List<EnrolmentPeriodTypeConfiguration> getConfigurations() {
            List<EnrolmentPeriodTypeConfiguration> configurations = new ArrayList<EnrolmentPeriodTypeConfiguration>();
            if (semester != null) {
                Map<Class<? extends EnrolmentPeriod>, EnrolmentPeriodTypeConfiguration> map =
                        new HashMap<Class<? extends EnrolmentPeriod>, EnrolmentPeriodTypeConfiguration>();
                for (final EnrolmentPeriod period : semester.getEnrolmentPeriod()) {
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

        @Override
        public SortedSet<DegreeCurricularPlan> getPossibleScope() {
            SortedSet<DegreeCurricularPlan> possible =
                    new TreeSet<DegreeCurricularPlan>(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);
            if (degreeType != null && type != null && semester != null) {
                if (degreeType.isBolonhaType()) {
                    for (ExecutionDegree execution : semester.getExecutionYear().getExecutionDegreesByType(degreeType)) {
                        DegreeCurricularPlan dcp = execution.getDegreeCurricularPlan();
                        addIfNotUsedInPeriod(possible, dcp);
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
            Function<EnrolmentPeriod, String> f = new Function<EnrolmentPeriod, String>() {
                @Override
                public String apply(EnrolmentPeriod period) {
                    return period.getExternalId();
                }
            };

            return Joiner.on(':').join(Iterables.transform(periods, f));
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