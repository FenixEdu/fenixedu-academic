package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.GradeScale;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class MarkType extends FenixUtil {

    public static final int TYPE20 = 20;

    public static final int TYPE5 = 5;

    public static final MarkType TYPE20_OBJ = new MarkType(20);

    public static final MarkType TYPE5_OBJ = new MarkType(5);

    public static final Object[] REP_MARKS = { GradeScale.RE };

    public static final Object[] NA_MARKS = { GradeScale.NA };

    public static final Object[] OTHER_MARKS = { GradeScale.RE, GradeScale.NA, GradeScale.AP };

    public static final Object[] TYPE20_MARKS = { "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20" };

    public static final Object[] TYPE20EVALUATION_MARKS = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9" };

    public static final Object[] TYPE5_MARKS = { "3", "4", "5" };

    public static final Object[] AP_MARKS = { GradeScale.AP };

    private Integer type;

    public MarkType() {
    }

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

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o instanceof MarkType) {
            MarkType aux = (MarkType) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    public static Collection getMarks(MarkType markType) {
        if (markType.equals(MarkType.TYPE20_OBJ))
            return MarkType.getType20Marks();
        if (markType.equals(MarkType.TYPE5_OBJ))
            return MarkType.getType5Marks();
        return null;
    }

    public static Collection getMarksEvaluation(MarkType markType) {
        if (markType.equals(MarkType.TYPE20_OBJ))
            return MarkType.getType20EvaluationMarks();
        if (markType.equals(MarkType.TYPE5_OBJ))
            return MarkType.getType5Marks();
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