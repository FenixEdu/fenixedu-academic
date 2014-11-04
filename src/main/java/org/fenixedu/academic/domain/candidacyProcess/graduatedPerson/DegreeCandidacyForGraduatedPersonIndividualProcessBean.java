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
package org.fenixedu.academic.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacyProcess.FormationBean;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPersonIndividualProcessBean extends
        IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private Degree selectedDegree;

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean() {
        setCandidacyDate(new LocalDate());
        setFormationConcludedBeanList(new ArrayList<FormationBean>());
        initializeDocumentUploadBeans();
        setObservations("");
        setPrecedentDegreeType(PrecedentDegreeType.EXTERNAL_DEGREE);
    }

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean(final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
        setIndividualCandidacyProcess(process);
        setCandidacyDate(process.getCandidacyDate());
        setSelectedDegree(process.getCandidacySelectedDegree());
        setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(process.getCandidacy()));
        setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getPrecedentDegreeInformation()));
        initializeFormation(process.getCandidacy().getFormationsSet());
        setObservations(process.getCandidacy().getObservations());
        setProcessChecked(process.getProcessChecked());
        setPaymentChecked(process.getPaymentChecked());
        setUtlStudent(process.getCandidacy().getUtlStudent());
    }

    @Override
    public DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess() {
        return (DegreeCandidacyForGraduatedPersonProcess) super.getCandidacyProcess();
    }

    @Override
    protected double getMinimumEcts(final CycleType cycleType) {
        if (cycleType.equals(CycleType.FIRST_CYCLE)) {
            return 150d;
        } else if (cycleType.equals(CycleType.SECOND_CYCLE)) {
            return 90d;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
        return Arrays.asList(CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
        return true;
    }

    public Degree getSelectedDegree() {
        return this.selectedDegree;
    }

    public void setSelectedDegree(Degree selectedDegree) {
        this.selectedDegree = selectedDegree;
    }

    @Override
    public boolean isDegreeCandidacyForGraduatedPerson() {
        return true;
    }
}
