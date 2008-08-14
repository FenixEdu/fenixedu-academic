package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import javax.faces.component.html.HtmlInputHidden;

import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class EvaluationManagementMenuBackingBean extends FenixBackingBean {

    protected Integer executionCourseID;

    private HtmlInputHidden executionCourseIdHidden;

    public Integer getExecutionCourseID() {
	if (this.executionCourseID == null) {
	    if (this.executionCourseIdHidden != null) {
		this.executionCourseID = Integer.valueOf(this.executionCourseIdHidden.getValue().toString());
	    } else {
		this.executionCourseID = Integer.valueOf(this.getRequestParameter("executionCourseID"));
	    }
	}
	return this.executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseId) {
	this.executionCourseID = executionCourseId;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
	if (this.executionCourseIdHidden == null) {
	    Integer executionCourseId = this.getExecutionCourseID();

	    this.executionCourseIdHidden = new HtmlInputHidden();
	    this.executionCourseIdHidden.setValue(executionCourseId);
	}
	return this.executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
	if (executionCourseIdHidden != null) {
	    this.executionCourseID = Integer.valueOf(executionCourseIdHidden.getValue().toString());
	}
	this.executionCourseIdHidden = executionCourseIdHidden;
    }

}
