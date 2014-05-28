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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.OptionalDismissalLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;

public class OptionalDismissal extends OptionalDismissal_Base {

    public OptionalDismissal() {
        super();
    }

    public OptionalDismissal(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse,
            Double ectsCredits) {
        init(credits, curriculumGroup, optionalCurricularCourse, ectsCredits);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void init(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse,
            Double ectsCredits) {
        init(credits, curriculumGroup, optionalCurricularCourse);
        checkCredits(ectsCredits);
        setEctsCredits(ectsCredits);
    }

    private void checkCredits(final Double ectsCredits) {
        if (ectsCredits == null || ectsCredits.doubleValue() == 0) {
            throw new DomainException("error.OptionalDismissal.invalid.credits");
        }
    }

    @Override
    public OptionalCurricularCourse getCurricularCourse() {
        return (OptionalCurricularCourse) super.getCurricularCourse();
    }

    @Override
    public void setDegreeModule(final DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof OptionalCurricularCourse)) {
            throw new DomainException("error.optionalDismissal.DegreeModuleCanOnlyBeOptionalCurricularCourse");
        }
        super.setDegreeModule(degreeModule);
    }

    @Override
    public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[OD ").append(hasDegreeModule() ? getDegreeModule().getName() : "").append(" ]\n");
        return builder;
    }

    @Override
    public boolean isSimilar(Dismissal dismissal) {
        return dismissal instanceof OptionalDismissal && super.isSimilar(dismissal)
                && hasSameEctsCredits((OptionalDismissal) dismissal);
    }

    private boolean hasSameEctsCredits(OptionalDismissal dismissal) {
        return getEctsCredits().equals(dismissal.getEctsCredits());
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new OptionalDismissalLog(action, getRegistration(), getCurricularCourse(), getCredits(), getEctsCredits(),
                getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

}
