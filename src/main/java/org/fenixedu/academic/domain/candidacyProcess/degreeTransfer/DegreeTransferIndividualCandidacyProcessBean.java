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
package org.fenixedu.academic.domain.candidacyProcess.degreeTransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.joda.time.LocalDate;

public class DegreeTransferIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private Degree selectDegree;

    public DegreeTransferIndividualCandidacyProcessBean() {
        setCandidacyDate(new LocalDate());
        initializeDocumentUploadBeans();
        setObservations("");
    }

    public DegreeTransferIndividualCandidacyProcessBean(final DegreeTransferIndividualCandidacyProcess process) {
        setIndividualCandidacyProcess(process);
        setCandidacyProcess(process.getCandidacyProcess());
        setCandidacyDate(process.getCandidacyDate());
        setSelectedDegree(process.getCandidacySelectedDegree());
        setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getPrecedentDegreeInformation()));
        setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(process.getCandidacy()));
        initializeFormation(process.getCandidacy().getFormationsSet());
        setObservations(process.getCandidacy().getObservations());
        setProcessChecked(process.getProcessChecked());
        setPaymentChecked(process.getPaymentChecked());
        setUtlStudent(process.getCandidacy().getUtlStudent());

    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
        return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
        return true;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public List<StudentCurricularPlan> getPrecedentStudentCurricularPlans() {
        final Student student = getStudent();
        if (student == null) {
            return Collections.emptyList();
        }

        final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isBolonha()) {
                final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

                for (final CycleType cycleType : getValidPrecedentCycleTypes()) {
                    if (studentCurricularPlan.hasCycleCurriculumGroup(cycleType)) {
                        final CycleCurriculumGroup cycle = studentCurricularPlan.getCycle(cycleType);

                        if (!cycle.isConclusionProcessed() && !cycle.isConcluded()) {
                            studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
                            break;
                        }
                    }
                }

            } else if (isPreBolonhaPrecedentDegreeAllowed()) {
                if (!registration.isConcluded() && !registration.isRegistrationConclusionProcessed()) {
                    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
                }
            }
        }

        return studentCurricularPlans;
    }

    @Override
    public DegreeTransferCandidacyProcess getCandidacyProcess() {
        return (DegreeTransferCandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getSelectedDegree() {
        return this.selectDegree;
    }

    public void setSelectedDegree(final Degree selectDegree) {
        this.selectDegree = selectDegree;
    }

    @Override
    protected void initializeDocumentUploadBeans() {
        setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    @Override
    public boolean isDegreeTransfer() {
        return true;
    }

}
