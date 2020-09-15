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
package org.fenixedu.academic.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.LastDigitSplitRuleVerifier;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class LastDigitSplitRule extends LastDigitSplitRule_Base {

    public LastDigitSplitRule(final DegreeModule toApplyRule, final CourseGroup contextCourseGroup, final Integer semester,
            final AcademicPeriod academicPeriod, final Boolean firstHalf, final ExecutionSemester begin, final ExecutionSemester end) {
        super();
        checkParameters(toApplyRule, semester, academicPeriod, firstHalf);
        init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.LAST_DIGIT_SPLIT);
        setFirstHalf(firstHalf);
        setCurricularPeriodOrder(semester);
        setAcademicPeriod(academicPeriod);
    }

    private void checkParameters(final DegreeModule toApplyRule, final Integer semester, final AcademicPeriod academicPeriod,
            final Boolean firstHalf) {
        if (semester == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (firstHalf == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (academicPeriod == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (!toApplyRule.isLeaf()) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.in", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.semester." + getCurricularPeriodOrder(), true));
        labelList.add(new GenericPair<Object, Boolean>(", ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.only.students", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        String digitSplit = getFirstHalf() ? "firstHalf" : "secondHalf";
        labelList.add(new GenericPair<Object, Boolean>("label." + digitSplit, true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.can.be.enroled", true));

        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
    }

    @Override
    public boolean appliesToContext(Context context) {
        return super.appliesToContext(context) && appliesToPeriod(context);
    }

    private boolean appliesToPeriod(Context context) {
        return context == null
                || context.getCurricularPeriod().hasCurricularPeriod(getAcademicPeriod(), getCurricularPeriodOrder());

    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return new LastDigitSplitRuleVerifier();
    }

    public String getLastDigitSplitString() {
        return new LocalizedString(org.fenixedu.academic.util.LocaleUtils.PT, BundleUtil.getString(Bundle.ACADEMIC, new Locale("pt", "PT"),
                "label." + (getFirstHalf() ? "firstHalf" : "secondHalf"))).getContent();
    }

}
