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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class ConclusionProcessVersion extends ConclusionProcessVersion_Base {

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
        final Integer finalAverage = bean.calculateFinalAverage();
        final BigDecimal average = bean.calculateAverage();
        final Double ectsCredits = bean.calculateCredits();
        final ExecutionYear ingressionYear = bean.calculateIngressionYear();
        final ExecutionYear conclusionYear = bean.calculateConclusionYear();
        String[] args = {};

        if (finalAverage == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (average == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args1);
        }
        String[] args2 = {};
        if (ectsCredits == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args2);
        }
        String[] args3 = {};
        if (ingressionYear == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args3);
        }
        String[] args4 = {};
        if (conclusionYear == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args4);
        }

        super.setConclusionDate(conclusion.toLocalDate());
        super.setFinalAverage(finalAverage);
        super.setAverage(average);
        super.setCredits(BigDecimal.valueOf(ectsCredits));
        super.setCurriculum(bean.getCurriculumForConclusion().toString());
        super.setIngressionYear(ingressionYear);
        super.setConclusionYear(conclusionYear);
    }

    protected void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate, final String notes) {
        String[] args = {};
        if (finalAverage == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusionDate == null) {
            throw new DomainException("error.ConclusionProcessVersion.argument.must.not.be.null", args1);
        }

        super.setResponsible(responsible);
        super.setFinalAverage(finalAverage);
        super.setConclusionDate(conclusionDate);
        super.setNotes(StringUtils.isEmpty(notes) ? null : notes);
    }

    protected void update(final Person responsible, final Integer finalAverage, BigDecimal average,
            final LocalDate conclusionDate, final String notes) {
        update(responsible, finalAverage, conclusionDate, notes);
        super.setAverage(average);
    }

    @Override
    public void setDissertationEnrolment(final Enrolment dissertationEnrolment) {
        if (getConclusionProcess().isCycleConclusionProcess()) {
            super.setDissertationEnrolment(dissertationEnrolment);
        } else {
            throw new DomainException("error.ConclusionProcessVersion.wrong.method.usage");
        }
    }

    @Override
    public void setMasterDegreeThesis(MasterDegreeThesis masterDegreeThesis) {
        if (getConclusionProcess().isRegistrationConclusionProcess()) {
            super.setMasterDegreeThesis(masterDegreeThesis);
        } else {
            throw new DomainException("error.ConclusionProcessVersion.wrong.method.usage");
        }
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
    public void setFinalAverage(Integer finalAverage) {
        throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setAverage(BigDecimal average) {
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

    @Deprecated
    public boolean hasResponsible() {
        return getResponsible() != null;
    }

    @Deprecated
    public boolean hasLastVersionConclusionProcess() {
        return getLastVersionConclusionProcess() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeThesis() {
        return getMasterDegreeThesis() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

    @Deprecated
    public boolean hasCreationDateTime() {
        return getCreationDateTime() != null;
    }

    @Deprecated
    public boolean hasConclusionDate() {
        return getConclusionDate() != null;
    }

    @Deprecated
    public boolean hasConclusionYear() {
        return getConclusionYear() != null;
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasConclusionProcess() {
        return getConclusionProcess() != null;
    }

    @Deprecated
    public boolean hasDissertationEnrolment() {
        return getDissertationEnrolment() != null;
    }

    @Deprecated
    public boolean hasCurriculum() {
        return getCurriculum() != null;
    }

    @Deprecated
    public boolean hasFinalAverage() {
        return getFinalAverage() != null;
    }

    @Deprecated
    public boolean hasIngressionYear() {
        return getIngressionYear() != null;
    }

    @Deprecated
    public boolean hasAverage() {
        return getAverage() != null;
    }

}
