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

import java.math.BigDecimal;
import java.util.Collections;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class ConclusionProcess extends ConclusionProcess_Base {

    private ConclusionProcessVersion getFirstVersion() {
        return Collections.min(getVersionsSet(), ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID);
    }

    public DateTime getCreationDateTime() {
        return getFirstVersion().getCreationDateTime();
    }

    public DateTime getLastModificationDateTime() {
        return getLastVersion().getCreationDateTime();
    }

    public Person getResponsible() {
        return getFirstVersion().getResponsible();
    }

    public Person getLastResponsible() {
        return getLastVersion().getResponsible();
    }

    public Integer getFinalAverage() {
        return getLastVersion().getFinalAverage();
    }

    public BigDecimal getAverage() {
        return getLastVersion().getAverage();
    }

    public LocalDate getConclusionDate() {
        return getLastVersion().getConclusionDate();
    }

    @Deprecated
    public YearMonthDay getConclusionYearMonthDay() {
        return new YearMonthDay(getConclusionDate());
    }

    public String getNotes() {
        return getLastVersion().getNotes();
    }

    public ExecutionYear getIngressionYear() {
        return getLastVersion().getIngressionYear();
    }

    public BigDecimal getCredits() {
        return getLastVersion().getCredits();
    }

    abstract public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
            final String notes);

    abstract public void update(final RegistrationConclusionBean bean);

    final protected void addVersions(final RegistrationConclusionBean bean) {
        super.addVersions(new ConclusionProcessVersion(bean));
        super.setLastVersion(Collections.max(getVersionsSet(), ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID));
        super.setConclusionYear(getLastVersion().getConclusionYear());

        addSpecificVersionInfo();
    }

    abstract protected void addSpecificVersionInfo();

    public boolean isCycleConclusionProcess() {
        return false;
    }

    public boolean isRegistrationConclusionProcess() {
        return false;
    }

    @Override
    final public void addVersions(final ConclusionProcessVersion versions) {
        throw new DomainException("error.ConclusionProcess.must.use.addVersions.with.bean");
    }

    @Override
    public void removeVersions(ConclusionProcessVersion versions) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setLastVersion(ConclusionProcessVersion lastVersion) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    abstract public Registration getRegistration();

    public Degree getDegree() {
        return getRegistration().getDegree();
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

}
