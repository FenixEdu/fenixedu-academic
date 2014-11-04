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
package org.fenixedu.academic.domain.phd.serviceRequests;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.spaces.domain.Space;

abstract public class PhdAcademicServiceRequest extends PhdAcademicServiceRequest_Base {

    public PhdAcademicServiceRequest() {
        super();
    }

    protected void init(final PhdAcademicServiceRequestCreateBean bean) {
        checkParameters(bean);

        super.setPhdIndividualProgramProcess(bean.getPhdIndividualProgramProcess());
        super.init(bean, getPhdIndividualProgramProcess().getPhdProgram().getAdministrativeOffice());
    }

    private void checkParameters(final PhdAcademicServiceRequestCreateBean bean) {
        if (bean.getPhdIndividualProgramProcess() == null) {
            throw new DomainException("error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.null");
        }
    }

    @Override
    public AcademicProgram getAcademicProgram() {
        return getPhdIndividualProgramProcess().getPhdProgram();
    }

    @Override
    public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        throw new DomainException("error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.immutable");
    }

    @Override
    public boolean isRequestForPhd() {
        return true;
    }

    @Override
    public Person getPerson() {
        return getPhdIndividualProgramProcess().getPerson();
    }

    public Student getStudent() {
        return getPhdIndividualProgramProcess().getStudent();
    }

    public Space getCampus() {
//        return Campus.readActiveCampusByName("Alameda");
        return SpaceUtils.getDefaultCampus();
    }

}
