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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

public class ProgramConclusionProcess extends ProgramConclusionProcess_Base {

    public ProgramConclusionProcess(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        final CurriculumGroup group = bean.getCurriculumGroup();
        final ExecutionYear conclusionYear = bean.getConclusionYear();
        if (group == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null");
        }
        if (conclusionYear == null) {
            throw new DomainException("error.CycleConclusionProcess.argument.must.not.be.null");
        }

        super.setGroup(group);
        super.setConclusionYear(conclusionYear);
        addVersions(bean);
    }

    @Override
    public void update(RegistrationConclusionBean bean) {
        addVersions(bean);
    }

    @Override
    final public void update(final Person responsible, final Grade finalGrade, final Grade rawGrade, final Grade descriptiveGrade,
            final LocalDate conclusionDate, final String notes) {
        addVersions(new RegistrationConclusionBean(getGroup().getStudentCurricularPlan(),
                getGroup().getDegreeModule().getProgramConclusion()));
        getLastVersion().update(responsible, finalGrade, rawGrade, descriptiveGrade, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
        Enrolment dissertationEnrolment = getDissertationEnrolment();
        if (dissertationEnrolment != null) {
            getLastVersion().setDissertationEnrolment(dissertationEnrolment);
        }
    }

    private Enrolment getDissertationEnrolment() {
        Stream<Enrolment> enrolments = getGroup().getEnrolmentsSet().stream().filter(Enrolment::isDissertation);

        List<Dismissal> dismissalsList = new ArrayList<>();
        getGroup().collectDismissals(dismissalsList);

        Stream<Enrolment> dismissals = dismissalsList.stream().flatMap(d -> d.getSourceIEnrolments().stream())
                .filter(e -> e.isEnrolment() && ((Enrolment) e).isDissertation()).map(ie -> (Enrolment) ie);
        return Stream.concat(enrolments, dismissals).max(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID).orElse(null);
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public Registration getRegistration() {
        return getGroup().getRegistration();
    }

}
