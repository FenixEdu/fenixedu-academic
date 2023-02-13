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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class ConclusionProcess extends ConclusionProcess_Base {

    final protected Stream<ConclusionProcessVersion> versions() {
        return getVersionsSet().stream().filter(ConclusionProcessVersion::isActive);
    }

    private Optional<ConclusionProcessVersion> getFirstVersion() {
        return versions().min(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID);
    }

    public DateTime getCreationDateTime() {
        return getFirstVersion().map(ConclusionProcessVersion::getCreationDateTime).orElse(null);
    }

    public DateTime getLastModificationDateTime() {
        return getLastVersion().getCreationDateTime();
    }

    public Person getResponsible() {
        return getFirstVersion().map(ConclusionProcessVersion::getResponsible).orElse(null);
    }

    public Person getLastResponsible() {
        return getLastVersion().getResponsible();
    }

    public Grade getFinalGrade() {
        return getLastVersion().getFinalGrade();
    }

    public Grade getRawGrade() {
        return getLastVersion().getRawGrade();
    }

    public Grade getDescriptiveGrade() {
        return getLastVersion().getDescriptiveGrade();
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

    abstract public void update(final Person responsible, final Grade finalGrade, final Grade rawGrade,
            final Grade qualitativeGrade, final LocalDate conclusionDate, final String notes);

    abstract public void update(final RegistrationConclusionBean bean);

    final protected void addVersions(final RegistrationConclusionBean bean) {
        super.addVersions(new ConclusionProcessVersion(bean));
        updateLastVersion();
        addSpecificVersionInfo();
    }

    final public void disableLastVersion() {
        getLastVersion().setActive(false);
        updateLastVersion();
    }

    final private void updateLastVersion() {
        super.setLastVersion(versions().max(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID).orElse(null));
        super.setConclusionYear(getLastVersion() == null ? null : getLastVersion().getConclusionYear());
    }

    abstract protected void addSpecificVersionInfo();

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

    public LocalizedString getName() {
        return getGroup().getName();
    }

    public boolean isActive() {
        return getLastVersion() != null && getLastVersion().isActive();
    }

    private boolean isDeletable() {
        return !isActive();
    }

    public void delete() {
        if (!isDeletable()) {
            throw new DomainException("error.ProgramConclusionProcess.delete.impossible");
        }

        while (!getVersionsSet().isEmpty()) {
            getVersionsSet().iterator().next().delete();
        }

        super.setGroup(null);
        super.setConclusionYear(null);
        super.setLastVersion(null);
        super.setRootDomainObject(null);

        super.deleteDomainObject();
    }

    public static Set<ConclusionProcess> findAll() {
        return Bennu.getInstance().getConclusionProcessesSet();
    }

    public boolean isNumberInvalid(String number) {
        return findAll().stream().filter(cp -> cp.getNumber() != null).anyMatch(cp -> cp.getNumber() == number);
    }

    @Override
    public void setNumber(String number) {
        boolean numberInvalid = isNumberInvalid(number);
        if (numberInvalid) {
            throw new DomainException("error.ProgramConclusionProcess.addNumber.numberAlreadyExists");
        }
        super.setNumber(number);
    }

    public void deleteNumber() {
        this.setNumber(null);
    }

}
