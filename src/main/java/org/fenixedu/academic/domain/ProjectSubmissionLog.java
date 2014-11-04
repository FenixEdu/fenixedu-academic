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

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ProjectSubmissionLog extends ProjectSubmissionLog_Base {

    public static Comparator<ProjectSubmissionLog> COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE =
            new Comparator<ProjectSubmissionLog>() {
                @Override
                public int compare(ProjectSubmissionLog leftProjectSubmissionLog, ProjectSubmissionLog rightProjectSubmissionLog) {
                    int comparationResult =
                            leftProjectSubmissionLog.getSubmissionDateTime().compareTo(
                                    rightProjectSubmissionLog.getSubmissionDateTime());
                    return (comparationResult == 0) ? leftProjectSubmissionLog.getExternalId().compareTo(
                            rightProjectSubmissionLog.getExternalId()) : -(comparationResult);
                }
            };

    public ProjectSubmissionLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ProjectSubmissionLog(DateTime submissionDateTime, String filename, String fileMimeType, String fileChecksum,
            String fileChecksumAlgorithm, Integer fileSize, StudentGroup studentGroup, Attends attends, Project project) {
        this();
        setSubmissionDateTime(submissionDateTime);
        setFilename(filename);
        setFileMimeType(fileMimeType);
        setFileChecksum(fileChecksum);
        setFileChecksumAlgorithm(fileChecksumAlgorithm);
        setFileSize(fileSize);
        setStudentGroup(studentGroup);
        setAttends(attends);
        setProject(project);
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

}
