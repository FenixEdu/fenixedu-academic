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

import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.io.servlets.FileDownloadServlet;

public class ProjectSubmissionFile extends ProjectSubmissionFile_Base {

    public ProjectSubmissionFile(String filename, String displayName, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(FileUtils.cleanupUserInputFilename(filename));
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(FileUtils.cleanupUserInputFileDisplayName(displayName));
    }

    @Override
    public boolean isAccessible(User user) {
        ExecutionCourse executionCourse = getProjectSubmission().getAttends().getExecutionCourse();
        if (user != null && user.getPerson().getTeacher() != null) {
            final Teacher teacher = user.getPerson().getTeacher();
            final Department department = teacher.getDepartment();
            if (department != null && getProjectSubmission().getProject().getDeparmentsSet().contains(department)) {
                return true;
            }
            for (final Professorship professorship : teacher.getProfessorshipsSet()) {
                if (professorship.getExecutionCourse().equals(executionCourse)) {
                    return true;
                }
            }
        }
        if (user != null && user.getPerson().getStudent() != null) {
            for (final Attends attends : getProjectSubmission().getStudentGroup().getAttendsSet()) {
                if (attends.getRegistration().getStudent().equals(user.getPerson().getStudent())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Delete jsp usages and delete this method
    @Deprecated
    public String getDownloadUrl() {
        return FileDownloadServlet.getDownloadUrl(this);
    }

    @Override
    public void delete() {
        setProjectSubmission(null);
        super.delete();
    }

}
