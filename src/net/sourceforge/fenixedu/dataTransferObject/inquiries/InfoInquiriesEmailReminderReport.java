/*
 * Created on 7/Jun/2005 - 17:11:47
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoInquiriesEmailReminderReport extends InfoObject implements
		Comparable {

	private InfoExecutionDegree executionDegree;
	private Integer numberSentEmails;
	private Integer numberUnansweredInquiries;
	private Integer totalNumberInquiries;
	private Integer numberDegreeStudents;
	private Integer numberStudentsWithEmail;
	
	
	
	public InfoInquiriesEmailReminderReport() {
		numberSentEmails = 0;
		numberUnansweredInquiries = 0;
		totalNumberInquiries = 0;
		numberDegreeStudents = 0;
		numberStudentsWithEmail = 0;
	}

	public InfoInquiriesEmailReminderReport(Integer idInternal) {
		super(idInternal);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the executionDegree.
	 */
	public InfoExecutionDegree getExecutionDegree() {
		return executionDegree;
	}
	/**
	 * @param executionDegree The executionDegree to set.
	 */
	public void setExecutionDegree(InfoExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}
	

	/**
	 * @return Returns the numberDegreeStudents.
	 */
	public Integer getNumberDegreeStudents() {
		return numberDegreeStudents;
	}
	/**
	 * @param numberDegreeStudents The numberDegreeStudents to set.
	 */
	public void setNumberDegreeStudents(Integer numberDegreeStudents) {
		this.numberDegreeStudents = numberDegreeStudents;
	}
	/**
	 * @param numberDegreeStudents The numberDegreeStudents to add.
	 */
	public void addDegreeStudents(Integer numberDegreeStudents) {
		this.numberDegreeStudents += numberDegreeStudents;
	}
	



	/**
	 * @return Returns the numberSentEmails.
	 */
	public Integer getNumberSentEmails() {
		return numberSentEmails;
	}
	/**
	 * @param numberSentEmails The numberSentEmails to set.
	 */
	public void setNumberSentEmails(Integer numberSentEmails) {
		this.numberSentEmails = numberSentEmails;
	}
	/**
	 * @param numberSentEmails The numberSentEmails to add.
	 */
	public void addSentEmails(Integer numberSentEmails) {
		this.numberSentEmails += numberSentEmails;
	}
	



	/**
	 * @return Returns the numberUnansweredInquiries.
	 */
	public Integer getNumberUnansweredInquiries() {
		return numberUnansweredInquiries;
	}
	/**
	 * @param numberUnansweredInquiries The numberUnansweredInquiries to set.
	 */
	public void setNumberUnansweredInquiries(Integer numberUnansweredInquiries) {
		this.numberUnansweredInquiries = numberUnansweredInquiries;
	}
	/**
	 * @param numberUnansweredInquiries The numberUnansweredInquiries to add.
	 */
	public void addUnansweredInquiries(Integer numberUnansweredInquiries) {
		this.numberUnansweredInquiries += numberUnansweredInquiries;
	}
	


	/**
	 * @return Returns the totalNumberInquiries.
	 */
	public Integer getTotalNumberInquiries() {
		return totalNumberInquiries;
	}
	/**
	 * @param totalNumberInquiries The totalNumberInquiries to set.
	 */
	public void setTotalNumberInquiries(Integer totalNumberInquiries) {
		this.totalNumberInquiries = totalNumberInquiries;
	}
	/**
	 * @param totalNumberInquiries The totalNumberInquiries to set.
	 */
	public void addNumberInquiries(Integer totalNumberInquiries) {
		this.totalNumberInquiries += totalNumberInquiries;
	}
	

	/**
	 * @return Returns the numberStudentsWithEmail.
	 */
	public Integer getNumberStudentsWithEmail() {
		return numberStudentsWithEmail;
	}
	/**
	 * @param numberStudentsWithEmail The numberStudentsWithEmail to set.
	 */
	public void setNumberStudentsWithEmail(Integer numberFailedEmails) {
		this.numberStudentsWithEmail = numberFailedEmails;
	}
	/**
	 * @param numberStudentsWithEmail The numberStudentsWithEmail to set.
	 */
	public void addStudentsWithEmail(Integer numberStudentsWithEmail) {
		this.numberStudentsWithEmail += numberStudentsWithEmail;
	}

	
	public int compareTo(Object arg0) {

		return 0;
	}

}
