package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class MarkSheetToConfirmSendMailBean implements Serializable{
	
	private DomainReference<MarkSheet> markSheet;
	private boolean toSubmit;
	
	public MarkSheetToConfirmSendMailBean(MarkSheet markSheet, boolean toSubmit) {
		setMarkSheet(markSheet);
		setToSubmit(toSubmit);
	}
	
	public MarkSheet getMarkSheet() {
		return (this.markSheet != null) ? this.markSheet.getObject() : null;
	}
	
	public void setMarkSheet(MarkSheet markSheet) {
		this.markSheet = ( markSheet != null ) ? new DomainReference<MarkSheet>(markSheet) : null;
	}
	
	public boolean isToSubmit() {
		return toSubmit;
	}
	
	public void setToSubmit(boolean toSubmit) {
		this.toSubmit = toSubmit;
	} 	
}
