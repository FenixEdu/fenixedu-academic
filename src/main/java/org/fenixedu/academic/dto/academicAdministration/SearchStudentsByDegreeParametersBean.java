/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.academicAdministration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByDegreeParametersBean extends DegreeByExecutionYearBean {

    private List<RegistrationProtocol> registrationProtocols = new ArrayList<RegistrationProtocol>();

    private List<RegistrationStateType> registrationStateTypes = new ArrayList<RegistrationStateType>();

    private List<StatuteType> statuteTypes = new ArrayList<StatuteType>();

    private boolean ingressedInChosenYear = false;

    private boolean concludedInChosenYear = false;

    private boolean activeEnrolments = false;

    private boolean standaloneEnrolments = false;

    private RegistrationRegimeType regime = null;

    private Country nationality = null;

    private IngressionType ingressionType = null;

    public SearchStudentsByDegreeParametersBean(Set<DegreeType> administratedDegreeTypes, Set<Degree> administratedDegrees) {
        super(administratedDegreeTypes, administratedDegrees);
    }

    public IngressionType getIngressionType() {
        return ingressionType;
    }

    public void setIngressionType(IngressionType ingressionType) {
        this.ingressionType = ingressionType;
    }

    public List<RegistrationProtocol> getRegistrationProtocols() {
        return registrationProtocols;
    }

    public void setRegistrationProtocols(List<RegistrationProtocol> registrationProtocols) {
        this.registrationProtocols = registrationProtocols;
    }

    public List<RegistrationStateType> getRegistrationStateTypes() {
        return registrationStateTypes;
    }

    public void setRegistrationStateTypes(List<RegistrationStateType> registrationStateTypes) {
        this.registrationStateTypes = registrationStateTypes;
    }

    public List<StatuteType> getStudentStatuteTypes() {
        return statuteTypes;
    }

    public void setStudentStatuteTypes(List<StatuteType> studentStatuteTypes) {
        this.statuteTypes = studentStatuteTypes;
    }

    public boolean hasAnyRegistrationProtocol() {
        return this.registrationProtocols != null && !this.registrationProtocols.isEmpty();
    }

    @Deprecated
    public boolean hasAnyRegistrationAgreements() {
        return hasAnyRegistrationProtocol();
    }

    public boolean hasAnyRegistrationStateTypes() {
        return this.registrationStateTypes != null && !this.registrationStateTypes.isEmpty();
    }

    public boolean hasAnyStudentStatuteType() {
        return this.statuteTypes != null && !this.statuteTypes.isEmpty();
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

    public void setIngressedInChosenYear(boolean ingressedInChosenYear) {
        this.ingressedInChosenYear = ingressedInChosenYear;
    }

    public boolean isIngressedInChosenYear() {
        return ingressedInChosenYear;
    }

    public void setConcludedInChosenYear(boolean concludedInChosenYear) {
        this.concludedInChosenYear = concludedInChosenYear;
    }

    public boolean isConcludedInChosenYear() {
        return concludedInChosenYear;
    }

}
