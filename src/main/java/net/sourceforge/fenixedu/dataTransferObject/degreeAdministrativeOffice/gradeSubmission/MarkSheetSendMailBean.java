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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class MarkSheetSendMailBean extends MarkSheetManagementBaseBean {

    Collection<MarkSheetToConfirmSendMailBean> markSheetToConfirmSendMailBean;
    Collection<GradesToSubmitExecutionCourseSendMailBean> gradesToSubmitExecutionCourseSendMailBean;
    String from;
    String subject;
    String message;
    String cc;

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Collection<MarkSheetToConfirmSendMailBean> getMarkSheetToConfirmSendMailBean() {
        return markSheetToConfirmSendMailBean;
    }

    public void setMarkSheetToConfirmSendMailBean(Collection<MarkSheetToConfirmSendMailBean> markSheetToConfirmSendMailBean) {
        this.markSheetToConfirmSendMailBean = markSheetToConfirmSendMailBean;
    }

    public Collection<MarkSheet> getMarkSheetToConfirmSendMailBeanToSubmit() {
        Collection<MarkSheet> result = new ArrayList<MarkSheet>();
        for (MarkSheetToConfirmSendMailBean sendMailBean : this.markSheetToConfirmSendMailBean) {
            if (sendMailBean.isToSubmit()) {
                result.add(sendMailBean.getMarkSheet());
            }
        }
        return result;
    }

    public Collection<GradesToSubmitExecutionCourseSendMailBean> getGradesToSubmitExecutionCourseSendMailBean() {
        return gradesToSubmitExecutionCourseSendMailBean;
    }

    public void setGradesToSubmitExecutionCourseSendMailBean(
            Collection<GradesToSubmitExecutionCourseSendMailBean> gradesToSubmitExecutionCourseSendMailBean) {
        this.gradesToSubmitExecutionCourseSendMailBean = gradesToSubmitExecutionCourseSendMailBean;
    }

    public Collection<ExecutionCourse> getGradesToSubmitExecutionCourseSendMailBeanToSubmit() {
        Collection<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (GradesToSubmitExecutionCourseSendMailBean sendMailBean : this.gradesToSubmitExecutionCourseSendMailBean) {
            if (sendMailBean.isToSubmit()) {
                result.add(sendMailBean.getExecutionCourse());
            }
        }
        return result;
    }

}
