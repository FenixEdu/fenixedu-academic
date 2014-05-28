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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import pt.ist.fenixframework.Atomic;

public class AuditProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourseAudit executionCourseAudit;
    private String measuresToTake;
    private String conclusions;
    private Boolean approvedByTeacher;
    private Boolean approvedByStudent;
    private transient InputStream inputStream;

    public AuditProcessBean(ExecutionCourseAudit executionCourseAudit) {
        setExecutionCourseAudit(executionCourseAudit);
        setMeasuresToTake(executionCourseAudit.getMeasuresToTake());
        setConclusions(executionCourseAudit.getConclusions());
        setApprovedByTeacher(executionCourseAudit.getApprovedByTeacher());
        setApprovedByStudent(executionCourseAudit.getApprovedByStudent());
    }

    public ExecutionCourseAudit getExecutionCourseAudit() {
        return executionCourseAudit;
    }

    public void setExecutionCourseAudit(ExecutionCourseAudit executionCourseAudit) {
        this.executionCourseAudit = executionCourseAudit;
    }

    public String getMeasuresToTake() {
        return measuresToTake;
    }

    public void setMeasuresToTake(String measuresToTake) {
        this.measuresToTake = measuresToTake;
    }

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public Boolean getApprovedByTeacher() {
        return approvedByTeacher;
    }

    public void setApprovedByTeacher(Boolean approvedByTeacher) {
        this.approvedByTeacher = approvedByTeacher;
    }

    public Boolean getApprovedByStudent() {
        return approvedByStudent;
    }

    public void setApprovedByStudent(Boolean approvedByStudent) {
        this.approvedByStudent = approvedByStudent;
    }

    @Atomic
    public void saveComments() {
        getExecutionCourseAudit().setMeasuresToTake(getMeasuresToTake());
        getExecutionCourseAudit().setConclusions(getConclusions());
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
