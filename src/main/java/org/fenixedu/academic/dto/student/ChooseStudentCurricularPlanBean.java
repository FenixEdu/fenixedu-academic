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
package org.fenixedu.academic.dto.student;

import java.io.Serializable;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationNumber;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ChooseStudentCurricularPlanBean implements Serializable {

    private Integer number;

    private Student student;

    private Registration registration;

    private StudentCurricularPlan studentCurricularPlan;

    public ChooseStudentCurricularPlanBean(StudentCurricularPlan studentCurricularPlan) {
        setStudentCurricularPlan(studentCurricularPlan);
        setRegistration(studentCurricularPlan.getRegistration());
        setStudent(studentCurricularPlan.getRegistration().getStudent());
    }

    public ChooseStudentCurricularPlanBean() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        if (registration == null) {
            this.registration = null;
            this.studentCurricularPlan = null;
        } else {
            this.registration = registration;
        }
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan == null ? null : studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
        if (number != null) {
            setStudent(readRegistration().getStudent());
        }
    }

    private final Registration readRegistration() {
//        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
//            if (registrationNumber.getNumber().intValue() == getNumber().intValue()) {
//                final Registration registration = registrationNumber.getRegistration();
//                if (registration.getDegreeType().isPreBolonhaDegree()) {
//                    return registration;
//                }
//            }
//        }
        return null;
    }

}
