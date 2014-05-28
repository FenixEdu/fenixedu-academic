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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasDegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;

public class ExternalSupervisorViewsBean implements Serializable, HasExecutionYear, HasDegreeType {

    private Person student;

    private ExecutionDegree executionDegree;
    private DegreeType degreeType;
    private ExecutionYear executionYear;

    private List<Person> students;

    private RegistrationProtocol protocol;
    private Boolean megavisor;

    public ExternalSupervisorViewsBean() {
        super();
        this.megavisor = false;
    }

    public ExternalSupervisorViewsBean(RegistrationProtocol protocol) {
        this();
        this.protocol = protocol;
    }

    public ExternalSupervisorViewsBean(ExecutionYear executionYear) {
        this();
        this.executionYear = executionYear;
    }

    public ExternalSupervisorViewsBean(Person student) {
        this();
        this.student = student;
    }

    public ExternalSupervisorViewsBean(Person student, RegistrationProtocol protocol) {
        this();
        this.student = student;
        this.protocol = protocol;
    }

    public ExternalSupervisorViewsBean(ExecutionYear executionYear, RegistrationProtocol protocol) {
        this();
        this.executionYear = executionYear;
        this.protocol = protocol;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    @Override
    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<Person> getStudents() {
        return students;
    }

    public void setStudents(List<Person> students) {
        this.students = students;
    }

    public RegistrationProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(RegistrationProtocol protocol) {
        this.protocol = protocol;
    }

    public Boolean getMegavisor() {
        return megavisor;
    }

    public void setMegavisor(Boolean megavisor) {
        this.megavisor = megavisor;
    }

    public void generateStudentsFromYear() {
        Set<Registration> theWholeSet = protocol.getRegistrationsSet();
        students = new ArrayList<Person>();

        for (Registration iterator : theWholeSet) {
            if (iterator.hasAnyActiveState(executionYear)) {
                students.add(iterator.getPerson());
            }
        }
        Collections.sort(students, DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public void generateStudentsFromDegree() {
        Set<Registration> theWholeSet = protocol.getRegistrationsSet();
        students = new ArrayList<Person>();

        for (Registration iterator : theWholeSet) {
            if (iterator.hasAnyActiveState(executionYear)
                    && iterator.getLastDegreeCurricularPlan() == executionDegree.getDegreeCurricularPlan()) {
                students.add(iterator.getPerson());
            }
        }
        Collections.sort(students, DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public boolean supervisorHasPermission() {
        Collection<Registration> allRegistrations = student.getStudent().getRegistrations();
        for (Registration iterator : allRegistrations) {
            if (iterator.getRegistrationProtocol() == protocol) {
                return true;
            }
        }
        return false;
    }

    public boolean supervisorHasPermission(boolean isOmnipotent, Set<RegistrationProtocol> jurisdictions) {
        if (isOmnipotent) {
            Collection<Registration> allRegistrations = student.getStudent().getRegistrations();
            for (Registration regIter : allRegistrations) {
                for (RegistrationProtocol protIter : jurisdictions) {
                    if (regIter.getRegistrationProtocol() == protIter) {
                        return true;
                    }
                }
            }
            return false;

        } else {
            return supervisorHasPermission();
        }
    }

    public boolean noCurriculum() {
        if (student.getStudent().getLastActiveRegistration() != null) {
            return false;
        }
        Collection<Registration> registrations = student.getStudent().getRegistrations();
        for (Registration iterator : registrations) {
            if (iterator.getNumberOfCurriculumEntries() > 0) {
                return false;
            }
        }
        return true;

    }

    public List<StudentCurricularPlan> generateAllStudentCurricularPlans() {
        List<StudentCurricularPlan> curricularPlans = new ArrayList<StudentCurricularPlan>();
        for (Person person : students) {
            for (Registration registration : person.getStudent().getRegistrations()) {
                if (registration.getRegistrationAgreement() == protocol.getRegistrationAgreement()
                        && registration.hasAnyActiveState(executionYear)) {
                    if (executionDegree != null) {
                        if (registration.getDegree() != executionDegree.getDegree()) {
                            continue;
                        }
                    }
                    for (StudentCurricularPlan curricularPlan : registration.getStudentCurricularPlans()) {
                        curricularPlans.add(curricularPlan);
                    }
                }
            }
        }
        Collections.sort(curricularPlans, StudentCurricularPlan.DATE_COMPARATOR);
        return curricularPlans;
    }

}
