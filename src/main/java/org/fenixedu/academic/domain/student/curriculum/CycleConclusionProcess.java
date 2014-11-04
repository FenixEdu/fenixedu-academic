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
package org.fenixedu.academic.domain.student.curriculum;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

public class CycleConclusionProcess extends CycleConclusionProcess_Base {

    private CycleConclusionProcess(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        final CycleCurriculumGroup cycle = bean.getCycleCurriculumGroup();
        final ExecutionYear conclusionYear = bean.getConclusionYear();
        String[] args = {};

        if (cycle == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusionYear == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null", args1);
        }

        super.setCycle(cycle);
        super.setConclusionYear(conclusionYear);
        addVersions(bean);
    }

    @Override
    public boolean isCycleConclusionProcess() {
        return true;
    }

    public static void conclude(final RegistrationConclusionBean bean) {
        if (bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.already.concluded.must.update");
        }

        new CycleConclusionProcess(bean);
    }

    @Override
    public void update(RegistrationConclusionBean bean) {
        if (!bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.is.not.concluded");
        }

        addVersions(bean);

    }

    @Override
    final public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
            final String notes) {
        addVersions(new RegistrationConclusionBean(getRegistration(), getCycle()));
        getLastVersion().update(responsible, finalAverage, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
        if (getCycleType() == CycleType.SECOND_CYCLE) {
            getLastVersion().setDissertationEnrolment(getRegistration().getDissertationEnrolment());
        }
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setCycle(CycleCurriculumGroup cycle) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    public CycleType getCycleType() {
        return getCycle().getCycleType();
    }

    @Override
    public Registration getRegistration() {
        return getCycle().getRegistration();
    }

}
