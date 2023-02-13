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
import java.util.Comparator;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class ConclusionProcessVersion extends ConclusionProcessVersion_Base {
    public static final String CREATE_SIGNAL = "academic.conclusion.process.version.create.signal";

    static final private Comparator<ConclusionProcessVersion> COMPARATOR_BY_CREATION_DATE_TIME =
            new Comparator<ConclusionProcessVersion>() {
                @Override
                public int compare(ConclusionProcessVersion o1, ConclusionProcessVersion o2) {
                    return o1.getCreationDateTime().compareTo(o2.getCreationDateTime());
                }
            };

    static final public Comparator<ConclusionProcessVersion> COMPARATOR_BY_CREATION_DATE_TIME_AND_ID =
            new Comparator<ConclusionProcessVersion>() {
                @Override
                final public int compare(ConclusionProcessVersion o1, ConclusionProcessVersion o2) {
                    final ComparatorChain chain = new ComparatorChain();
                    chain.addComparator(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME);
                    chain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
                    return chain.compare(o1, o2);
                }
            };

    protected ConclusionProcessVersion(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setCreationDateTime(new DateTime());
        super.setResponsible(AccessControl.getPerson());

        final YearMonthDay conclusion = bean.calculateConclusionDate();
        final Grade finalGrade = bean.getCalculatedFinalGrade();
        final Grade rawGrade = bean.getCalculatedRawGrade();
        final Double ectsCredits = bean.calculateCredits();
        final ExecutionYear ingressionYear = bean.calculateIngressionYear();
        final ExecutionYear conclusionYear = bean.calculateConclusionYear();

        if (finalGrade == null || rawGrade == null || ectsCredits == null || ingressionYear == null || conclusionYear == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null");
        }

        super.setConclusionDate(conclusion.toLocalDate());
        super.setFinalGrade(finalGrade);
        super.setRawGrade(rawGrade);
        super.setCredits(BigDecimal.valueOf(ectsCredits));
        super.setCurriculum(bean.getCurriculumForConclusion().toString());
        super.setIngressionYear(ingressionYear);
        super.setConclusionYear(conclusionYear);
        super.setActive(true);
        Signal.emit(ConclusionProcessVersion.CREATE_SIGNAL, new DomainObjectEvent<ConclusionProcessVersion>(this));
    }

    protected void update(final Person responsible, final Grade finalGrade, final Grade rawGrade, final Grade descriptiveGrade,
            final LocalDate conclusionDate, final String notes) {

        if (finalGrade == null || rawGrade == null || conclusionDate == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null");
        }

        super.setResponsible(responsible);
        super.setFinalGrade(finalGrade);
        super.setRawGrade(rawGrade);
        super.setDescriptiveGrade(descriptiveGrade);
        super.setConclusionDate(conclusionDate);
        super.setNotes(StringUtils.isEmpty(notes) ? null : notes);
    }

    private boolean isDeletable() {
        return getLastVersionConclusionProcess() == null || !isActive();
    }

    public void delete() {
        if (!isDeletable()) {
            throw new DomainException("error.ConclusionProcessVersion.delete.impossible");
        }

        super.getLastVersionConclusionProcess();
        super.setConclusionProcess(null);
        super.setConclusionYear(null);
        super.setDissertationEnrolment(null);
        super.setIngressionYear(null);
        super.setResponsible(null);

        super.setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Override
    public Enrolment getDissertationEnrolment() {
        //FIXME: remove when the framework enables read-only slots
        return super.getDissertationEnrolment();
    }

    @Override
    public Grade getRawGrade() {
        //FIXME: remove when the framework enables read-only slots
        return super.getRawGrade();
    }

    @Override
    public Grade getFinalGrade() {
        //FIXME: remove when the framework enables read-only slots
        return super.getFinalGrade();
    }

    @Override
    public Grade getDescriptiveGrade() {
        //FIXME: remove when the framework enables read-only slots
        return super.getDescriptiveGrade();
    }

    @Override
    public void setConclusionProcess(final ConclusionProcess conclusionProcess) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setLastVersionConclusionProcess(ConclusionProcess lastVersionConclusionProcess) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCreationDateTime(DateTime creationDateTime) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setResponsible(Person responsible) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setConclusionDate(LocalDate conclusionDate) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCredits(BigDecimal credits) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCurriculum(String curriculum) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setIngressionYear(ExecutionYear ingressionYear) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setNotes(String notes) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    public boolean isActive() {
        return super.getActive();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    @Deprecated
    public java.util.Date getCreation() {
        org.joda.time.DateTime dt = getCreationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreation(java.util.Date date) {
        if (date == null) {
            setCreationDateTime(null);
        } else {
            setCreationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    public void deleteNumber() {
        setNumber(null);
    }

}
