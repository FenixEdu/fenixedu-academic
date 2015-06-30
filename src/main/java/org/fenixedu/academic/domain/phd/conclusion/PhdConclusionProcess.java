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
package org.fenixedu.academic.domain.phd.conclusion;

import java.math.BigDecimal;
import java.util.Comparator;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdConclusionProcess extends PhdConclusionProcess_Base {

    public static final Comparator<PhdConclusionProcess> VERSION_COMPARATOR = new Comparator<PhdConclusionProcess>() {
        @Override
        public int compare(PhdConclusionProcess o1, PhdConclusionProcess o2) {
            return o1.getVersion().compareTo(o2.getVersion()) * -1;
        }
    };

    protected PhdConclusionProcess() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdConclusionProcess(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
        this();
        init(conclusionProcessBean, responsible);
    }

    protected void init(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
        PhdIndividualProgramProcess process = conclusionProcessBean.getPhdIndividualProgramProcess();
        LocalDate conclusionDate = conclusionProcessBean.getConclusionDate();
        PhdThesisFinalGrade grade = conclusionProcessBean.getGrade();
        BigDecimal thesisEctsCredits = conclusionProcessBean.getThesisEctsCredits();
        BigDecimal studyPlanEctsCredits = conclusionProcessBean.getStudyPlanEctsCredits();

        if (!process.isConcluded()) {
            throw new DomainException("error.phd.PhdConclusionProcess.process.is.not.concluded");
        }

        if (responsible == null) {
            throw new DomainException("error.phd.PhdConclusionProcess.responsible.is.required");
        }

        if (conclusionDate == null) {
            throw new DomainException("error.phd.PhdConclusionProcess.conclusionDate.is.required");
        }

        if (grade == null) {
            throw new DomainException("error.phd.PhdConclusionProcess.grade.is.required");
        }

        // TODO: phd-refactor
        if (process.getRegistration() != null
                && !process.getRegistration().getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                        .isConclusionProcessed()) {
            throw new DomainException("error.phd.PhdConclusionProcess.registration.must.be.concluded.first");
        }

        if (thesisEctsCredits == null) {
            throw new DomainException("error.phd.PhdConclusionProcess.thesisEctsCredits.is.required");
        }

        if (!process.getCandidacyProcess().isStudyPlanExempted()) {
            if (studyPlanEctsCredits == null) {
                throw new DomainException("error.phd.PhdConclusionProcess.studyPlanEctsCredits.is.required");
            }
        }

        setResponsible(responsible);
        setConclusionDate(conclusionDate);
        setVersion(process.getLastConclusionProcess() != null ? process.getLastConclusionProcess().getVersion() + 1 : 1);
        setPhdProcess(process);
        setWhenCreated(new DateTime());
        setFinalGrade(grade);
        setThesisEctsCredits(thesisEctsCredits);
        setStudyPlanEctsCredits(studyPlanEctsCredits);
    }

    public static PhdConclusionProcess create(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
        return new PhdConclusionProcess(conclusionProcessBean, responsible);
    }

    public BigDecimal getTotalEctsCredits() {
        if (getPhdProcess().getCandidacyProcess().isStudyPlanExempted()) {
            return getThesisEctsCredits();
        }

        return getThesisEctsCredits().add(getStudyPlanEctsCredits());
    }

}
