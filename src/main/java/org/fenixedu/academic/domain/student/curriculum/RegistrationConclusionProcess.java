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
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;

public class RegistrationConclusionProcess extends RegistrationConclusionProcess_Base {

    private RegistrationConclusionProcess(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        final Registration registration = bean.getRegistration();
        final ExecutionYear conclusionYear = bean.getConclusionYear();
        String[] args = {};

        if (registration == null) {
            throw new DomainException("error.RegistrationConclusionProcess.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusionYear == null) {
            throw new DomainException("error.RegistrationConclusionProcess.conclusionYear.cannot.be.null", args1);
        }

        super.setRegistration(registration);
        super.setConclusionYear(conclusionYear);
        addVersions(bean);
    }

    @Override
    public void update(final RegistrationConclusionBean bean) {
        if (!bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.is.not.concluded");
        }

        addVersions(bean);
    }

    @Override
    final public void update(final Person responsible, final Grade finalGrade, final Grade rawGrade,
            final Grade descriptiveGrade, final LocalDate conclusionDate, final String notes) {
        addVersions(new RegistrationConclusionBean(getRegistration(), getGroup()));
        getLastVersion().update(responsible, finalGrade, rawGrade, descriptiveGrade, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setRegistration(Registration registration) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public LocalizedString getName() {
        return getDegree().getDegreeType().getName();
    }

}
