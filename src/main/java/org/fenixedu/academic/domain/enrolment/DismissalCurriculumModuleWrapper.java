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
package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;

public class DismissalCurriculumModuleWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 12L;
    private Dismissal dismissal;

    public DismissalCurriculumModuleWrapper(final Dismissal dismissal, final ExecutionSemester executionSemester) {
        super(dismissal.getCurriculumGroup(), executionSemester);
        setDismissal(dismissal);
    }

    private Dismissal getDismissal() {
        return this.dismissal;
    }

    private void setDismissal(Dismissal dismissal) {
        this.dismissal = dismissal;
    }

    private boolean hasDismissal() {
        return getDismissal() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DismissalCurriculumModuleWrapper) {
            final DismissalCurriculumModuleWrapper other = (DismissalCurriculumModuleWrapper) obj;
            return getDismissal().equals(other.getDismissal());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDismissal().hashCode();
    }

    @Override
    public boolean canCollectRules() {
        return true;
    }

    @Override
    public boolean isAnnualCurricularCourse(ExecutionYear executionYear) {
        if (hasDegreeModule() && getDegreeModule().isLeaf()) {
            return ((CurricularCourse) getDegreeModule()).isAnual(executionYear);
        }
        return false;
    }

    @Override
    public boolean isDissertation() {
        if (hasDismissal()) {
            return getDismissal().hasCurricularCourse() ? getDismissal().getCurricularCourse().isDissertation() : false;
        }
        return false;
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

}
