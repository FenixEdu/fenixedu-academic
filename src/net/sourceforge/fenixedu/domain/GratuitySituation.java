/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuitySituation extends GratuitySituation_Base {

	private ExemptionGratuityType exemptionType;

    private IStudentCurricularPlan studentCurricularPlan;
    
	private Date when;

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
	 * @return Returns the student.
	 */
	public IStudentCurricularPlan getStudentCurricularPlan() {
		return this.studentCurricularPlan;
	}

	/**
	 * @param student
	 *            The student to set.
	 */
	public void setStudentCurricularPlan(
			IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;		
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
