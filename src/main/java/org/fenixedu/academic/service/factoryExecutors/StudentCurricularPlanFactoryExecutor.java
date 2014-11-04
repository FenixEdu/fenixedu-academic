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
/**
 * 
 */
package org.fenixedu.academic.service.factoryExecutors;

import java.io.Serializable;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlanFactoryExecutor {

    @SuppressWarnings("serial")
    public static class StudentCurricularPlanCreator implements FactoryExecutor, Serializable {

        private Registration registration;

        private DegreeCurricularPlan degreeCurricularPlan;

        private CycleType cycleType;

        public StudentCurricularPlanCreator(Registration registration) {
            super();
            this.registration = registration;
        }

        @Override
        public Object execute() {

            if (getRegistration().getStudentCurricularPlan(getDegreeCurricularPlan()) != null) {
                throw new DomainException("error.registrationAlreadyHasSCPWithGivenDCP");
            }

            return StudentCurricularPlan.createBolonhaStudentCurricularPlan(getRegistration(), getDegreeCurricularPlan(),
                    new YearMonthDay(), ExecutionSemester.readActualExecutionSemester(), getCycleType());
        }

        public DegreeCurricularPlan getDegreeCurricularPlan() {
            return this.degreeCurricularPlan;
        }

        public Registration getRegistration() {
            return this.registration;
        }

        public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
            this.degreeCurricularPlan = degreeCurricularPlan;
        }

        public void setRegistration(Registration registration) {
            this.registration = registration;
        }

        public Degree getDegree() {
            return getRegistration().getDegree();
        }

        public CycleType getCycleType() {
            return cycleType;
        }

        public void setCycleType(CycleType cycleType) {
            this.cycleType = cycleType;
        }

    }

}
