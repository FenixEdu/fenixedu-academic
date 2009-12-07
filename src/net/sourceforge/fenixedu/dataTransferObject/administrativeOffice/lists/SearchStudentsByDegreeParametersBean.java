package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.commons.DegreeByExecutionYearBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.RegistrationRegimeType;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByDegreeParametersBean extends DegreeByExecutionYearBean {

    private List<RegistrationAgreement> registrationAgreements = new ArrayList<RegistrationAgreement>();

    private List<RegistrationStateType> registrationStateTypes = new ArrayList<RegistrationStateType>();

    private List<StudentStatuteType> studentStatuteTypes = new ArrayList<StudentStatuteType>();

    private boolean activeEnrolments = false;

    private boolean standaloneEnrolments = false;

    private RegistrationRegimeType regime = null;

    private Country nationality = null;

    public SearchStudentsByDegreeParametersBean(Set<DegreeType> administratedDegreeTypes, Set<Degree> administratedDegrees) {
	super(administratedDegreeTypes, administratedDegrees);
    }

    public List<RegistrationAgreement> getRegistrationAgreements() {
	return registrationAgreements;
    }

    public void setRegistrationAgreements(List<RegistrationAgreement> registrationAgreements) {
	this.registrationAgreements = registrationAgreements;
    }

    public List<RegistrationStateType> getRegistrationStateTypes() {
	return registrationStateTypes;
    }

    public void setRegistrationStateTypes(List<RegistrationStateType> registrationStateTypes) {
	this.registrationStateTypes = registrationStateTypes;
    }

    public List<StudentStatuteType> getStudentStatuteTypes() {
	return studentStatuteTypes;
    }

    public void setStudentStatuteTypes(List<StudentStatuteType> studentStatuteTypes) {
	this.studentStatuteTypes = studentStatuteTypes;
    }

    public boolean hasAnyStudentStatuteType() {
	return this.studentStatuteTypes != null && !this.studentStatuteTypes.isEmpty();
    }

    public boolean getActiveEnrolments() {
	return activeEnrolments;
    }

    public void setActiveEnrolments(boolean activeEnrolments) {
	this.activeEnrolments = activeEnrolments;
    }

    public boolean getStandaloneEnrolments() {
	return standaloneEnrolments;
    }

    public void setStandaloneEnrolments(boolean standaloneEnrolments) {
	this.standaloneEnrolments = standaloneEnrolments;
    }

    public RegistrationRegimeType getRegime() {
	return regime;
    }

    public void setRegime(RegistrationRegimeType regime) {
	this.regime = regime;
    }

    public Country getNationality() {
	return nationality;
    }

    public void setNationality(Country nationality) {
	this.nationality = nationality;
    }

}
