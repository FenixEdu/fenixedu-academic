/*
 * StudentCurricularState.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package Util;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */
public class StudentCurricularPlanState {
    public static final int ACTIVE = 1;
    public static final int CONCLUDED = 2;
    public static final int INCOMPLETE = 3;

    public static final String ACTIVE_STRING = "Activo";
    public static final String CONCLUDED_STRING = "Concluído";
    public static final String INCOMPLETE_STRING = "Incompleto";
	

    private Integer state;    
    
    public StudentCurricularPlanState() {
    }
    
    public StudentCurricularPlanState(int state) {
        this.state = new Integer(state);
    }

    public StudentCurricularPlanState(Integer state) {
        this.state = state;
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
            StudentCurricularPlanState state = (StudentCurricularPlanState)obj;
            resultado = getState().equals(state.getState());
        }
        return resultado;
    }
    
    public String toString() {
    	int value = this.state.intValue();
    	switch (value) {
    		case ACTIVE : return ACTIVE_STRING;
    		case CONCLUDED : return CONCLUDED_STRING;
    		case INCOMPLETE : return INCOMPLETE_STRING;
    	}
        return "Error: Invalid Student Curricular Plan State type";
    }
    
}