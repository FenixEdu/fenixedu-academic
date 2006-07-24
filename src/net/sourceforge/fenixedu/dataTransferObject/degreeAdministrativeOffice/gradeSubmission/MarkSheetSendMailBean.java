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
	public void setMarkSheetToConfirmSendMailBean(
			Collection<MarkSheetToConfirmSendMailBean> markSheetToConfirmSendMailBean) {
		this.markSheetToConfirmSendMailBean = markSheetToConfirmSendMailBean;
	}
	
	public Collection<MarkSheet> getMarkSheetToConfirmSendMailBeanToSubmit() {
		Collection<MarkSheet> result = new ArrayList<MarkSheet>();
		for (MarkSheetToConfirmSendMailBean sendMailBean : this.markSheetToConfirmSendMailBean) {
			if(sendMailBean.isToSubmit()) {
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
			if(sendMailBean.isToSubmit()) {
				result.add(sendMailBean.getExecutionCourse());
			}
		}
		return result;
	}

}
