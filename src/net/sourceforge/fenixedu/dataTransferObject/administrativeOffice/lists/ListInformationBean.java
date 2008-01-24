/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author - Angela
 * 
 */
public class ListInformationBean extends  ExecutionDegreeListBean{

	private static final long serialVersionUID = 1L;

	private List<RegistrationAgreement>  registrationAgreement;

	private List<RegistrationStateType> registrationStateType;
	
	private EnrolmentEvaluationState enrolmentEvaluationState;
	
	private EnrollmentState enrollmentState;
	
	private DomainReference<RegistrationState> registrationState;
	
	private Collection<DegreeModuleScope> degreeModuleScope;
	
	private Boolean with_equivalence;
	
	

	public Collection<DegreeModuleScope> getDegreeModuleScope() {
		return this.degreeModuleScope;
	}

	public void setDegreeModuleScope(Collection<DegreeModuleScope> degreeModuleScope) {
		this.degreeModuleScope = degreeModuleScope;
	}

	public ListInformationBean() {
		super();

		this.registrationAgreement = new ArrayList<RegistrationAgreement>();
		this.registrationStateType = new ArrayList<RegistrationStateType>();
		this.degreeModuleScope = new ArrayList<DegreeModuleScope>();
		this.with_equivalence = new Boolean(false);
		this.enrolmentEvaluationState = null;
		this.enrollmentState = null;

	}

	public List<RegistrationAgreement> getRegistrationAgreement() {
		return this.registrationAgreement;
	}

	public void setRegistrationAgreement(
			List<RegistrationAgreement> registrationAgreement) {
		this.registrationAgreement = registrationAgreement;
	}

	public void clearAgreement() {
		this.registrationAgreement = null;
	}

	public List<RegistrationStateType> getRegistrationStateType() {
		return this.registrationStateType;
	}

	public void setRegistrationStateType(
			List<RegistrationStateType> registrationStateType) {
		this.registrationStateType = registrationStateType;
	}

	public void clearRegistrationStateType() {
		this.registrationStateType = null;
	}

	public Boolean getWith_equivalence() {
		return with_equivalence;
	}

	public void setWith_equivalence(Boolean with_equivalence) {
		this.with_equivalence = with_equivalence;
	}
	public RegistrationState getRegistrationState() {
		return (this.registrationState == null) ? null : this.registrationState.getObject();
	}

	public void setRegistrationState(RegistrationState registrationState) {
		this.registrationState = (registrationState != null) ? new DomainReference<RegistrationState>(registrationState)
				: null;
	}

	public EnrollmentState getEnrollmentState() {
		return this.enrollmentState;
	}

	public void setEnrollmentState(EnrollmentState enrollmentState) {
		this.enrollmentState = enrollmentState;
	}

	public EnrolmentEvaluationState getEnrolmentEvaluationState() {
		return this.enrolmentEvaluationState;
	}

	public void setEnrolmentEvaluationState(
			EnrolmentEvaluationState enrolmentEvaluationState) {
		this.enrolmentEvaluationState = enrolmentEvaluationState;
	}

}
