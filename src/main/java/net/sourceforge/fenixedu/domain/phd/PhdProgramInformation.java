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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import pt.ist.fenixframework.Atomic;

public class PhdProgramInformation extends PhdProgramInformation_Base {

    protected PhdProgramInformation() {
        super();
    }

    protected PhdProgramInformation(final PhdProgramInformationBean bean) {
        init(bean);
    }

    protected void init(final PhdProgramInformationBean bean) {
        checkParameters(bean);

        setBeginDate(bean.getBeginDate());
        setMinThesisEctsCredits(bean.getMinThesisEctsCredits());
        setMaxThesisEctsCredits(bean.getMaxThesisEctsCredits());
        setMinStudyPlanEctsCredits(bean.getMinStudyPlanEctsCredits());
        setMaxStudyPlanEctsCredits(bean.getMaxStudyPlanEctsCredits());
        setNumberOfYears(bean.getNumberOfYears());
        setNumberOfSemesters(bean.getNumberOfSemesters());
        setPhdProgram(bean.getPhdProgram());

    }

    protected void checkParameters(final PhdProgramInformationBean bean) {
        if (bean.getBeginDate() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.beginDate.required");
        }

        if (bean.getMinThesisEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.MinThesisEctsCredits.required");
        }

        if (bean.getMaxThesisEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.maxThesisEctsCredits.required");
        }

        if (bean.getMinStudyPlanEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.minStudyPlanEctsCredits.required");
        }

        if (bean.getMaxStudyPlanEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.maxStudyPlanEctsCredits.required");
        }

        if (bean.getNumberOfYears() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.numberOfYears");
        }

        if (bean.getNumberOfSemesters() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.numberOfSemesters");
        }

        if (hasSomePhdProgramInformationBeanWithSameBeginDate(bean)) {
            throw new PhdDomainOperationException("error.phd.PhdProgramInformation.other.information.with.same.beginDate");
        }
    }

    private boolean hasSomePhdProgramInformationBeanWithSameBeginDate(final PhdProgramInformationBean bean) {
        for (PhdProgramInformation information : bean.getPhdProgram().getPhdProgramInformations()) {
            if (this == information) {
                continue;
            }

            if (information.getBeginDate().equals(bean.getBeginDate())) {
                return true;
            }
        }

        return false;
    }

    @Atomic
    public void edit(final PhdProgramInformationBean bean) {
        checkParameters(bean);

        setBeginDate(bean.getBeginDate());
        setMinThesisEctsCredits(bean.getMinThesisEctsCredits());
        setMaxThesisEctsCredits(bean.getMaxThesisEctsCredits());
        setMinStudyPlanEctsCredits(bean.getMinStudyPlanEctsCredits());
        setMaxStudyPlanEctsCredits(bean.getMaxStudyPlanEctsCredits());
        setNumberOfYears(bean.getNumberOfYears());
        setNumberOfSemesters(bean.getNumberOfSemesters());
    }

    @Atomic
    public static PhdProgramInformation createInformation(final PhdProgramInformationBean bean) {
        return new PhdProgramInformation(bean);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumberOfYears() {
        return getNumberOfYears() != null;
    }

    @Deprecated
    public boolean hasMaxStudyPlanEctsCredits() {
        return getMaxStudyPlanEctsCredits() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasNumberOfSemesters() {
        return getNumberOfSemesters() != null;
    }

    @Deprecated
    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

    @Deprecated
    public boolean hasMinThesisEctsCredits() {
        return getMinThesisEctsCredits() != null;
    }

    @Deprecated
    public boolean hasMinStudyPlanEctsCredits() {
        return getMinStudyPlanEctsCredits() != null;
    }

    @Deprecated
    public boolean hasMaxThesisEctsCredits() {
        return getMaxThesisEctsCredits() != null;
    }

}
