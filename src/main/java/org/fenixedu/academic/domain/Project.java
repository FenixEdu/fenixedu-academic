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
/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.GroupEnrolment;
import org.fenixedu.academic.domain.util.icalendar.EvaluationEventBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.util.*;
import java.util.stream.Collectors;

public class Project extends Project_Base {

    private static final Logger logger = LoggerFactory.getLogger(Project.class);

    static {
        getRelationProjectGrouping().addListener(new RelationAdapter<Project, Grouping>() {
            @Override
            public void afterAdd(Project project, Grouping grouping) {
                if (project != null && grouping != null) {
                    if (grouping.getAutomaticEnrolment() && grouping.getStudentGroupsSet().isEmpty()) {
                        int groupCount = 0;
                        for (final ExecutionCourse executionCourse : project.getAssociatedExecutionCoursesSet()) {
                            for (Attends attend : executionCourse.getAttendsSet()) {
                                try {
                                    GroupEnrolment.run(grouping.getExternalId(), null, ++groupCount, new ArrayList<String>(),
                                            attend.getRegistration().getStudent().getPerson().getUsername());
                                } catch (FenixServiceException e) {
                                    // TODO Auto-generated catch block
                                    logger.error(e.getMessage(), e);
                                    groupCount--;
                                }
                            }
                        }
                        grouping.setGroupMaximumNumber(groupCount);
                    }
                }
            }
        });
    }

    private Project() {
        super();
    }

    public Project(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, ExecutionCourse executionCourse) {
        this();
        if (name == null || begin == null || end == null || executionCourse == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }

        this.setName(name);
        this.setBegin(begin);
        this.setEnd(end);
        this.setDescription(description != null ? description : "");
        this.addAssociatedExecutionCourses(executionCourse);
        this.setGradeScale(GradeScale.TYPE20);

        setOnlineSubmissionProperties(onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping);
    }

    public Project(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, ExecutionCourse executionCourse, GradeScale gradeScale) {

        this(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping, executionCourse);

        if (gradeScale != null) {
            this.setGradeScale(gradeScale);
        }

        logCreate();
    }

    public void edit(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, GradeScale gradeScale, List<Department> departments) {
        if (name == null || begin == null || end == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }
        setName(name);
        setBegin(begin);
        setEnd(end);
        setGradeScale(gradeScale);
        setDescription((description != null) ? description : "");

        if (!getProjectSubmissionsSet().isEmpty()) {
            if (getGrouping() != grouping || !getOnlineSubmissionsAllowed().equals(onlineSubmissionsAllowed)
                    || !getMaxSubmissionsToKeep().equals(maxSubmissionsToKeep)) {
                throw new DomainException("error.project.onlineSubmissionOptionsCannotBeChangedBecauseSubmissionsAlreadyExist");
            }

        }

        setOnlineSubmissionProperties(onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping);
        final Collection<Department> departmentsList = getDeparmentsSet();
        departmentsList.clear();
        departmentsList.addAll(departments);

        logEdit();
    }

    private void setOnlineSubmissionProperties(Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Grouping grouping) {

        setOnlineSubmissionsAllowed(onlineSubmissionsAllowed);

        if (onlineSubmissionsAllowed == true) {
            if ((maxSubmissionsToKeep == null) || (maxSubmissionsToKeep == 0) || (grouping == null)) {
                throw new DomainException("error.project.maxSubmissionsAndGroupingRequiredForOnlineSubmissions");
            } else {
                setMaxSubmissionsToKeep(maxSubmissionsToKeep);
                setGrouping(grouping);
            }
        } else {
            setMaxSubmissionsToKeep(null);
            setGrouping(null);

        }
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.PROJECT_TYPE;
    }

    @Deprecated
    public java.util.Date getBegin() {
        final org.joda.time.DateTime dt = getProjectBeginDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setBegin(java.util.Date date) {
        setProjectBeginDateTime(new org.joda.time.DateTime(date.getTime()));
    }

    @Deprecated
    public java.util.Date getEnd() {
        final org.joda.time.DateTime dt = getProjectEndDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEnd(java.util.Date date) {
        setProjectEndDateTime(new org.joda.time.DateTime(date.getTime()));
    }

    @Override
    public void setMaxSubmissionsToKeep(Integer maxSubmissionsToKeep) {
        if (maxSubmissionsToKeep != null && maxSubmissionsToKeep > 99) {
            throw new DomainException("error.project.maxSubmissionsToKeepMustBeLessThan", "99");
        }

        super.setMaxSubmissionsToKeep(maxSubmissionsToKeep);
    }

    public boolean canAddNewSubmissionWithoutExceedLimit(StudentGroup studentGroup) {
        return studentGroup.getProjectSubmissionsSet().size() <= getMaxSubmissionsToKeep()
                && !(studentGroup.wasDeleted());
    }

    public boolean isSubmissionPeriodOpen() {
        final DateTime now = new DateTime();
        return !getProjectBeginDateTime().isAfter(now) && !getProjectEndDateTime().isBefore(now);
    }

    public boolean isCanComment() {
        for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            final Professorship professorship = executionCourse.getProfessorshipForCurrentUser();
            if (professorship != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete() {
        if (!getProjectSubmissionsSet().isEmpty()) {
            throw new DomainException("error.project.cannotDeleteBecauseHasSubmissionsAssociated");
        }
        if (getProjectDepartmentGroup() != null) {
            throw new DomainException("error.project.cannotDeleteProjectUsedInAccessControl");
        }

        logRemove();
        setGrouping(null);
        getDeparmentsSet().clear();
        super.delete();
    }

    public List<ProjectSubmission> getProjectSubmissionsByStudentGroup(final StudentGroup studentGroup) {
        final List<ProjectSubmission> result = new ArrayList<ProjectSubmission>(studentGroup.getProjectSubmissionsSet());
        Collections.sort(result, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        return result;
    }

    public ProjectSubmission getOldestProjectSubmissionForStudentGroup(final StudentGroup studentGroup) {
        return studentGroup.getProjectSubmissionsSet().stream()
                .max(ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE).orElse(null);
    }

    public Collection<ProjectSubmission> getLastProjectSubmissionForEachStudentGroup() {
        return getProjectSubmissionsSet().stream()
                .filter(submission -> !submission.getStudentGroup().wasDeleted())
                .collect(Collectors.toMap(
                        submission -> submission.getStudentGroup(),
                        submission -> submission,
                        (s1, s2) -> s1.getSubmissionDateTime().compareTo(s2.getSubmissionDateTime()) >= 0 ? s1 : s2
                )).values();
    }

    public Collection<ProjectSubmission> getLastProjectSubmissionForEachDeletedStudentGroup() {
        return getProjectSubmissionsSet().stream()
                .filter(submission -> submission.getStudentGroup().wasDeleted())
                .collect(Collectors.toMap(
                        submission -> submission.getStudentGroup(),
                        submission -> submission,
                        (s1, s2) -> s1.getSubmissionDateTime().compareTo(s2.getSubmissionDateTime()) >= 0 ? s1 : s2
                )).values();
    }

    public ProjectSubmission getLastProjectSubmissionForStudentGroup(final StudentGroup group) {
        return group.getProjectSubmissionsSet().stream()
                .max((s1, s2) -> s1.getSubmissionDateTime().compareTo(s2.getSubmissionDateTime()))
                .orElse(null);
    }

    public List<ProjectSubmissionLog> getProjectSubmissionLogsByStudentGroup(final StudentGroup studentGroup) {
        final List<ProjectSubmissionLog> result = new ArrayList<>(studentGroup.getProjectSubmissionLogsSet());
        Collections.sort(result, ProjectSubmissionLog.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        return result;
    }

    public List<EvaluationEventBean> getAllEvents(ExecutionCourse executionCourse) {
        List<EvaluationEventBean> result = new ArrayList<EvaluationEventBean>();
        result.add(new EvaluationEventBean("Inicio " + this.getName() + " : " + executionCourse.getNome(), this
                .getProjectBeginDateTime(), this.getProjectBeginDateTime().plusHours(1), false, null, null,
                this.getDescription(), Collections.singleton(executionCourse)));
        if (this.getOnlineSubmissionsAllowed()) {
            String url = CoreConfiguration.getConfiguration().applicationUrl() + "/login";
            result.add(new EvaluationEventBean("Fim " + this.getName() + " : " + executionCourse.getNome(), this
                    .getProjectEndDateTime().minusHours(1), this.getProjectEndDateTime(), false, null, url,
                    this.getDescription(), Collections.singleton(executionCourse)));
        } else {
            result.add(new EvaluationEventBean("Fim " + this.getName() + " : " + executionCourse.getNome(), this
                    .getProjectEndDateTime().minusHours(1), this.getProjectEndDateTime(), false, null, null, this
                    .getDescription(), Collections.singleton(executionCourse)));
        }
        return result;
    }

    @Deprecated
    public java.util.Date getProjectBegin() {
        final org.joda.time.DateTime dt = getProjectBeginDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setProjectBegin(java.util.Date date) {
        if (date == null) {
            setProjectBeginDateTime(null);
        } else {
            setProjectBeginDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getProjectEnd() {
        final org.joda.time.DateTime dt = getProjectEndDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setProjectEnd(final java.util.Date date) {
        if (date == null) {
            setProjectEndDateTime(null);
        } else {
            setProjectEndDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.project") + " " + getName();
    }

    @Override
    public Date getEvaluationDate() {
        return getBegin();
    }

}
