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
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.domain.student.StatuteType;

/**
 *
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
@SuppressWarnings("serial")
public class SearchStudentsByDegreeParametersBean extends DegreeByExecutionYearBean {

    private List<RegistrationProtocol> registrationProtocols = new ArrayList<RegistrationProtocol>();

    private List<StatuteType> statuteTypes = new ArrayList<StatuteType>();

    private List<ProgramConclusion> programConclusions = new ArrayList<ProgramConclusion>();

    private boolean ingressedInChosenYear = false;

    private boolean activeEnrolments = false;

    private boolean standaloneEnrolments = false;

    private boolean includeConcludedWithoutConclusionProcess = false;

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

    public boolean hasAnyStudentStatuteType() {
        return this.statuteTypes != null && !this.statuteTypes.isEmpty();
    }

    public boolean hasAnyProgramConclusion() {
        return this.programConclusions != null && !this.programConclusions.isEmpty();
    }

    public List<ProgramConclusion> getProgramConclusions() {
        return programConclusions;
    }

    public void setProgramConclusions(List<ProgramConclusion> programConclusions) {
        this.programConclusions = programConclusions;
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

    public boolean isIncludeConcludedWithoutConclusionProcess() {
        return includeConcludedWithoutConclusionProcess;
    }

    public void setIncludeConcludedWithoutConclusionProcess(boolean includeConcludedWithoutConclusionProcess) {
        this.includeConcludedWithoutConclusionProcess = includeConcludedWithoutConclusionProcess;
    }
}
