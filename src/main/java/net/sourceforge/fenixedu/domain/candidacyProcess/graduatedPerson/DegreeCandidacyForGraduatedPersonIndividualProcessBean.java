/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

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
        initializeFormation(process.getCandidacy().getFormations());
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
