/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * @author - Angela
 * 
 */
public class ListInformationBean extends  ExecutionDegreeListBean{

	private static final long serialVersionUID = 1L;

	private RegistrationAgreement registrationAgreement;

	private RegistrationStateType registrationStateType;
	private Collection<DegreeModuleScope> degreeModuleScope;

	public Collection<DegreeModuleScope> getDegreeModuleScope() {
		return this.degreeModuleScope;
	}

	public void setDegreeModuleScope(Collection<DegreeModuleScope> degreeModuleScope) {
		this.degreeModuleScope = degreeModuleScope;
	}

	public ListInformationBean() {
		super();

		this.registrationAgreement = null;
		this.registrationStateType = null;
		this.degreeModuleScope = new ArrayList<DegreeModuleScope>();

	}

	public RegistrationAgreement getRegistrationAgreement() {
		return this.registrationAgreement;
	}

	public void setRegistrationAgreement(
			RegistrationAgreement registrationAgreement) {
		this.registrationAgreement = registrationAgreement;
	}

	public void clearAgreement() {
		this.registrationAgreement = null;
	}

	public RegistrationStateType getRegistrationStateType() {
		return this.registrationStateType;
	}

	public void setRegistrationStateType(
			RegistrationStateType registrationStateType) {
		this.registrationStateType = registrationStateType;
	}

	public void clearRegistrationStateType() {
		this.registrationStateType = null;
	}

}
