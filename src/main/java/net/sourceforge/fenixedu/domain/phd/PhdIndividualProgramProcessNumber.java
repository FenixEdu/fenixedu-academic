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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class PhdIndividualProgramProcessNumber extends PhdIndividualProgramProcessNumber_Base implements
        Comparable<PhdIndividualProgramProcessNumber> {

    public static Comparator<PhdIndividualProgramProcessNumber> COMPARATOR_BY_NUMBER =
            new Comparator<PhdIndividualProgramProcessNumber>() {
                @Override
                public int compare(PhdIndividualProgramProcessNumber left, PhdIndividualProgramProcessNumber right) {
                    int comparationResult = left.getNumber().compareTo(right.getNumber());
                    return (comparationResult == 0) ? left.getExternalId().compareTo(right.getExternalId()) : comparationResult;
                }
            };

    protected PhdIndividualProgramProcessNumber() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdIndividualProgramProcessNumber(Integer number, Integer year, Integer phdStudentNumber) {
        this();
        init(number, year, phdStudentNumber);
    }

    private void init(Integer number, Integer year, Integer phdStudentNumber) {
        checkParameters(number, year, phdStudentNumber);
        super.setNumber(number);
        super.setYear(year);
        super.setPhdStudentNumber(phdStudentNumber);
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.cannot.modify.year");
    }

    @Override
    public void setNumber(Integer number) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.cannot.modify.number");
    }

    @Override
    public void setPhdStudentNumber(Integer phdStudentNumber) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.cannot.modify.phdStudentNumber");
    }

    private void checkParameters(Integer number, Integer year, Integer phdStudentNumber) {
        String[] args = {};
        if (number == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.number.cannot.be.null", args);
        }
        String[] args1 = {};
        if (year == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.year.cannot.be.null", args1);
        }

        if (phdStudentNumber != null && hasProcessWithPhdStudentNumber(phdStudentNumber)) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.phdStudentNumber.exists");
        }
    }

    public void edit(PhdIndividualProgramProcessBean bean) {
        if (bean.getPhdStudentNumber() == getPhdStudentNumber()) {
            return;
        }

        if (bean.getPhdStudentNumber() == null) {
            super.setPhdStudentNumber(null);
            return;
        }

        PhdIndividualProgramProcessNumber number = readByPhdStudentNumber(bean.getPhdStudentNumber());

        if (number == null || number == this) {
            super.setPhdStudentNumber(bean.getPhdStudentNumber());
            return;
        }

        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.phdStudentNumber.exists");
    }

    static public PhdIndividualProgramProcessNumber generateNextForYear(final Integer year, final Integer phdStudentNumber) {
        final PhdIndividualProgramProcessNumber maxByYear = readMaxByYear(year);
        final Integer number = maxByYear != null ? maxByYear.getNumber() + 1 : 1;

        return new PhdIndividualProgramProcessNumber(number, year, phdStudentNumber);
    }

    static public PhdIndividualProgramProcessNumber readMaxByYear(final Integer year) {
        final Set<PhdIndividualProgramProcessNumber> processes = readByYear(year);

        return processes.isEmpty() ? null : Collections.max(processes, COMPARATOR_BY_NUMBER);
    }

    static public Set<PhdIndividualProgramProcessNumber> readByYear(final Integer year) {
        final Set<PhdIndividualProgramProcessNumber> result = new HashSet<PhdIndividualProgramProcessNumber>();

        for (final PhdIndividualProgramProcessNumber each : Bennu.getInstance().getPhdIndividualProcessNumbersSet()) {
            if (each.getYear().intValue() == year) {
                result.add(each);
            }
        }

        return result;
    }

    static public PhdIndividualProgramProcessNumber readByPhdStudentNumber(Integer number) {
        for (final PhdIndividualProgramProcessNumber each : Bennu.getInstance().getPhdIndividualProcessNumbersSet()) {
            if (number.equals(each.getPhdStudentNumber())) {
                return each;
            }
        }

        return null;
    }

    static public boolean hasProcessWithPhdStudentNumber(Integer number) {
        return readByPhdStudentNumber(number) != null;
    }

    public String getFullProcessNumber() {
        return getNumber() + "/" + getYear();
    }

    @Override
    public int compareTo(PhdIndividualProgramProcessNumber other) {
        int res = -1 * getYear().compareTo(other.getYear());
        return (res != 0) ? res : -1 * getNumber().compareTo(other.getNumber());
    }

    public boolean belongsTo(final ExecutionYear year) {
        return year.belongsToCivilYear(getYear());
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

    @Deprecated
    public boolean hasPhdStudentNumber() {
        return getPhdStudentNumber() != null;
    }

}
