/*
 * StudentCurricularState.java Created on 20 de Dezembro de 2002, 14:12
 */

package Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes & Joana Mota
 */
public class StudentCurricularPlanState extends FenixUtil {

    public static final int ACTIVE = 1;

    public static final int CONCLUDED = 2;

    public static final int INCOMPLETE = 3;

    public static final int SCHOOLPARTCONCLUDED = 4;

    public static final int INACTIVE = 5;

    // For use in migration process only
    public static final int PAST = 6;

    public static final StudentCurricularPlanState ACTIVE_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.ACTIVE);

    public static final StudentCurricularPlanState CONCLUDED_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.CONCLUDED);

    public static final StudentCurricularPlanState INCOMPLETE_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.INCOMPLETE);

    public static final StudentCurricularPlanState SCHOOLPARTCONCLUDED_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.SCHOOLPARTCONCLUDED);

    public static final StudentCurricularPlanState INACTIVE_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.INACTIVE);

    public static final StudentCurricularPlanState PAST_OBJ = new StudentCurricularPlanState(
            StudentCurricularPlanState.PAST);

    public static final String ACTIVE_STRING = "Activo";

    public static final String CONCLUDED_STRING = "Concluido";

    public static final String INCOMPLETE_STRING = "Incompleto";

    public static final String SCHOOLPARTCONCLUDED_STRING = "Parte Escolar Concluida";

    public static final String INACTIVE_STRING = "Inactivo";

    private Integer state;

    public StudentCurricularPlanState() {
    }

    public StudentCurricularPlanState(int state) {
        this.state = new Integer(state);
    }

    public StudentCurricularPlanState(Integer state) {
        this.state = state;
    }

    public StudentCurricularPlanState(String state) {
        if (state.equals(StudentCurricularPlanState.ACTIVE_STRING))
            this.state = new Integer(StudentCurricularPlanState.ACTIVE);
        if (state.equals(StudentCurricularPlanState.CONCLUDED_STRING))
            this.state = new Integer(StudentCurricularPlanState.CONCLUDED);
        if (state.equals(StudentCurricularPlanState.INCOMPLETE_STRING))
            this.state = new Integer(StudentCurricularPlanState.INCOMPLETE);
        if (state.equals(StudentCurricularPlanState.SCHOOLPARTCONCLUDED_STRING))
            this.state = new Integer(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
        if (state.equals(StudentCurricularPlanState.INACTIVE_STRING))
            this.state = new Integer(StudentCurricularPlanState.INACTIVE);
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = new Integer(state);
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof StudentCurricularPlanState) {
            StudentCurricularPlanState state = (StudentCurricularPlanState) obj;
            resultado = getState().equals(state.getState());
        }
        return resultado;
    }

    public String toString() {
        int value = this.state.intValue();
        String valueS = null;
        switch (value) {
        case ACTIVE:
            valueS = "ACTIVE";
            break;
        case CONCLUDED:
            valueS = "CONCLUDED";
            break;
        case INCOMPLETE:
            valueS = "INCOMPLETE";
            break;
        case SCHOOLPARTCONCLUDED:
            valueS = "SCHOOLPARTCONCLUDED";
            break;
        case INACTIVE:
            valueS = "INACTIVE";
            break;
        case PAST:
            valueS = "PAST";
            break;
        default:
            break;
        }
        return valueS;
    }

    public String getStringPt() {
        int value = this.state.intValue();
        String valueS = null;
        switch (value) {
        case ACTIVE:
            valueS = "Activo";
            break;
        case CONCLUDED:
            valueS = "Concluido";
            break;
        case INCOMPLETE:
            valueS = "Incompleto";
            break;
        case SCHOOLPARTCONCLUDED:
            valueS = "Parte Escolar Concluida";
            break;
        case INACTIVE:
            valueS = "Inactivo";
            break;
        default:
            break;
        }
        return valueS;
    }

    public static List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(StudentCurricularPlanState.ACTIVE_STRING,
                StudentCurricularPlanState.ACTIVE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.INACTIVE_STRING,
                StudentCurricularPlanState.INACTIVE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.CONCLUDED_STRING,
                StudentCurricularPlanState.CONCLUDED_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.INCOMPLETE_STRING,
                StudentCurricularPlanState.INCOMPLETE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.SCHOOLPARTCONCLUDED_STRING,
                StudentCurricularPlanState.SCHOOLPARTCONCLUDED_STRING));
        return result;
    }

    public static List toOrderedArrayList(StudentCurricularPlanState state) {
        List result = new ArrayList();
        result.add(new LabelValueBean(state.getStringPt(), state.getStringPt()));
        result.add(new LabelValueBean(StudentCurricularPlanState.ACTIVE_STRING,
                StudentCurricularPlanState.ACTIVE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.INACTIVE_STRING,
                StudentCurricularPlanState.INACTIVE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.CONCLUDED_STRING,
                StudentCurricularPlanState.CONCLUDED_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.INCOMPLETE_STRING,
                StudentCurricularPlanState.INCOMPLETE_STRING));
        result.add(new LabelValueBean(StudentCurricularPlanState.SCHOOLPARTCONCLUDED_STRING,
                StudentCurricularPlanState.SCHOOLPARTCONCLUDED_STRING));
        return result;
    }
}