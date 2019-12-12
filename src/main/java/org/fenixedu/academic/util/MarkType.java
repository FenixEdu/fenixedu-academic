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
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.GradeScaleEnum;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

@Deprecated
public class MarkType extends FenixUtil {

    public static final int TYPE20 = 20;

    public static final int TYPE5 = 5;

    public static final MarkType TYPE20_OBJ = new MarkType(20);

    public static final MarkType TYPE5_OBJ = new MarkType(5);

    public static final Object[] REP_MARKS = { GradeScaleEnum.RE };

    public static final Object[] NA_MARKS = { GradeScaleEnum.NA };

    public static final Object[] OTHER_MARKS = { GradeScaleEnum.RE, GradeScaleEnum.NA, GradeScaleEnum.AP };

    public static final Object[] TYPE20_MARKS = { "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };

    public static final Object[] TYPE20EVALUATION_MARKS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public static final Object[] TYPE5_MARKS = { "3", "4", "5" };

    public static final Object[] AP_MARKS = { GradeScaleEnum.AP };

    private final Integer type;

    public MarkType(int type) {
        this.type = new Integer(type);
    }

    public MarkType(Integer state) {
        this.type = state;
    }

    /**
     * @return Type of Marks
     */
    public Integer getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MarkType) {
            MarkType aux = (MarkType) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    public static Collection getMarks(MarkType markType) {
        if (markType.equals(MarkType.TYPE20_OBJ)) {
            return MarkType.getType20Marks();
        }
        if (markType.equals(MarkType.TYPE5_OBJ)) {
            return MarkType.getType5Marks();
        }
        return null;
    }

    public static Collection getMarksEvaluation(MarkType markType) {
        if (markType.equals(MarkType.TYPE20_OBJ)) {
            return MarkType.getType20EvaluationMarks();
        }
        if (markType.equals(MarkType.TYPE5_OBJ)) {
            return MarkType.getType5Marks();
        }
        return null;
    }

    public static Collection getRepMarks() {
        List repMarks = new ArrayList();
        repMarks.addAll(Arrays.asList(REP_MARKS));
        return repMarks;
    }

    public static Collection getNaMarks() {
        List naMarks = new ArrayList();
        naMarks.addAll(Arrays.asList(NA_MARKS));
        return naMarks;
    }

    private static Collection getType20Marks() {
        Collection result = new ArrayList();

        result.addAll(Arrays.asList(MarkType.OTHER_MARKS));
        result.addAll(Arrays.asList(MarkType.TYPE20_MARKS));

        return result;
    }

    private static Collection getType5Marks() {
        Collection result = new ArrayList();

        result.addAll(Arrays.asList(MarkType.OTHER_MARKS));
        result.addAll(Arrays.asList(MarkType.TYPE5_MARKS));

        return result;
    }

    private static Collection getType20EvaluationMarks() {
        Collection result = new ArrayList();
        result.addAll(Arrays.asList(MarkType.REP_MARKS));
        result.addAll(Arrays.asList(MarkType.NA_MARKS));
        result.addAll(Arrays.asList(MarkType.TYPE20EVALUATION_MARKS));
        result.addAll(Arrays.asList(MarkType.TYPE20_MARKS));

        return result;
    }

}