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
package org.fenixedu.academic.domain;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.util.Comparator;

public class ProjectSubmission extends ProjectSubmission_Base {

    static {
        getRelationProjectSubmissionProject().addListener(new ProjectSubmissionProjectListener());
    }

    private static class ProjectSubmissionProjectListener extends RelationAdapter<ProjectSubmission, Project> {

        @Override
        public void beforeAdd(final ProjectSubmission projectSubmission, final Project project) {
            if (project != null && projectSubmission != null) {
                if (!project.isSubmissionPeriodOpen()) {
                    throw new DomainException("error.project.submissionPeriodAlreadyExpired");
                }
                final StudentGroup studentGroup = projectSubmission.getStudentGroup();
                if (studentGroup.getGrouping() != project.getGrouping()) {
                    throw new DomainException("error.project.studentGroupDoesNotBelongToProjectGrouping");
                }
                if (!project.canAddNewSubmissionWithoutExceedLimit(studentGroup)) {
                    project.getOldestProjectSubmissionForStudentGroup(studentGroup).delete();
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

    public ProjectSubmission(final Project project, final StudentGroup studentGroup, final Attends attends,
                             final ProjectSubmissionFile projectSubmissionFile) {
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
        final org.joda.time.DateTime dt = getSubmissionDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setSubmission(final java.util.Date date) {
        if (date == null) {
            setSubmissionDateTime(null);
        } else {
            setSubmissionDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
