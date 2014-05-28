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
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ProjectSubmission extends ProjectSubmission_Base {

    static {
        getRelationProjectSubmissionProject().addListener(new ProjectSubmissionProjectListener());
    }

    private static class ProjectSubmissionProjectListener extends RelationAdapter<ProjectSubmission, Project> {

        @Override
        public void beforeAdd(ProjectSubmission projectSubmission, Project project) {

            if (project != null && projectSubmission != null) {
                if (!project.isSubmissionPeriodOpen()) {
                    throw new DomainException("error.project.submissionPeriodAlreadyExpired");
                }

                if (!project.canAddNewSubmissionWithoutExceedLimit(projectSubmission.getStudentGroup())) {
                    project.getOldestProjectSubmissionForStudentGroup(projectSubmission.getStudentGroup()).delete();
                }

                if (projectSubmission.getStudentGroup().getGrouping() != project.getGrouping()) {
                    throw new DomainException("error.project.studentGroupDoesNotBelongToProjectGrouping");
                }
            }

            super.beforeAdd(projectSubmission, project);
        }

    }

    public static Comparator<ProjectSubmission> COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE = new Comparator<ProjectSubmission>() {
        @Override
        public int compare(ProjectSubmission projectSubmission, ProjectSubmission otherProjectSubmission) {
            int comparationResult =
                    projectSubmission.getSubmissionDateTime().compareTo(otherProjectSubmission.getSubmissionDateTime());
            return (comparationResult == 0) ? projectSubmission.getExternalId().compareTo(otherProjectSubmission.getExternalId()) : -(comparationResult);
        }
    };

    public static Comparator COMPARATOR_BY_GROUP_NUMBER = new BeanComparator("studentGroup.groupNumber");

    public static Comparator COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE = new ComparatorChain();

    static {
        ((ComparatorChain) COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE).addComparator(COMPARATOR_BY_GROUP_NUMBER);
        ((ComparatorChain) COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE)
                .addComparator(COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
    }

    public ProjectSubmission(Project project, StudentGroup studentGroup, Attends attends,
            ProjectSubmissionFile projectSubmissionFile) {
        super();

        setRootDomainObject(Bennu.getInstance());
        setSubmissionDateTime(new DateTime());
        setStudentGroup(studentGroup);
        setAttends(attends);
        setProjectSubmissionFile(projectSubmissionFile);
        setProject(project);

    }

    public void delete() {
        getProjectSubmissionFile().delete();
        setAttends(null);
        setProject(null);
        setStudentGroup(null);
        setRootDomainObject(null);
        super.deleteDomainObject();

    }

    public boolean isTeacherObservationAvailable() {
        return !StringUtils.isEmpty(this.getTeacherObservation());
    }

    @Deprecated
    public java.util.Date getSubmission() {
        org.joda.time.DateTime dt = getSubmissionDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setSubmission(java.util.Date date) {
        if (date == null) {
            setSubmissionDateTime(null);
        } else {
            setSubmissionDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasProjectSubmissionFile() {
        return getProjectSubmissionFile() != null;
    }

    @Deprecated
    public boolean hasTeacherObservation() {
        return getTeacherObservation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSubmissionDateTime() {
        return getSubmissionDateTime() != null;
    }

    @Deprecated
    public boolean hasProject() {
        return getProject() != null;
    }

    @Deprecated
    public boolean hasAttends() {
        return getAttends() != null;
    }

    @Deprecated
    public boolean hasStudentGroup() {
        return getStudentGroup() != null;
    }

}
