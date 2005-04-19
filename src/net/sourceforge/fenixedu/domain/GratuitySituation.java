/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.util.ExemptionGratuityType;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuitySituation extends GratuitySituation_Base {

	private ExemptionGratuityType exemptionType;

	private IGratuityValues gratuityValues;

	private IStudentCurricularPlan studentCurricularPlan;

	private Date when;

	private IEmployee employee;

	private List transactionList;

	/**
	 * @return Returns the employee.
	 */
	public IEmployee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            The employee to set.
	 */
	public void setEmployee(IEmployee employee) {
		this.employee = employee;
	}

	/**
	 * @return Returns the when.
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when
	 *            The when to set.
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return Returns the exemptionType.
	 */
	public ExemptionGratuityType getExemptionType() {
		return exemptionType;
	}

	/**
	 * @param exemptionType
	 *            The exemptionType to set.
	 */
	public void setExemptionType(ExemptionGratuityType exemptionType) {
		this.exemptionType = exemptionType;
	}

	/**
	 * @return Returns the gratuity.
	 */
	public IGratuityValues getGratuityValues() {
		return gratuityValues;
	}

	/**
	 * @param gratuity
	 *            The gratuity to set.
	 */
	public void setGratuityValues(IGratuityValues gratuity) {
		this.gratuityValues = gratuity;
	}

	/**
	 * @return Returns the student.
	 */
	public IStudentCurricularPlan getStudentCurricularPlan() {
		return studentCurricularPlan;
	}

	/**
	 * @param student
	 *            The student to set.
	 */
	public void setStudentCurricularPlan(
			IStudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * @return Returns the transactionList.
	 */
	public List getTransactionList() {
		return transactionList;
	}

	/**
	 * @param transactionList
	 *            The transactionList to set.
	 */
	public void setTransactionList(List transactionList) {
		this.transactionList = transactionList;
	}

	/*
	 * public String toString() { //TODO: to make return null; }
	 */

	/*
	 * public boolean equals(Object object) { //TODO: to make return true; }
	 */

	/*
	 * public String toString() { //TODO: to make return null; }
	 */

	/*
	 * public boolean equals(Object object) { //TODO: to make return true; }
	 */
}
