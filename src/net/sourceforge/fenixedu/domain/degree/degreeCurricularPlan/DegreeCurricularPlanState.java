package net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan;


/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public enum DegreeCurricularPlanState {

    ACTIVE,
    
    NOT_ACTIVE,
    
    CONCLUDED,
    
    //// For use in migration process only
    PAST;
    
    public String getName() {
        return name();
    }
    
    /*
    public static final int ACTIVE = 1;

    public static final int NOT_ACTIVE = 2;

    public static final int CONCLUDED = 3;

    // For use in migration process only
    public static final int PAST = 4;

    public static final DegreeCurricularPlanState ACTIVE_OBJ = new DegreeCurricularPlanState(
            DegreeCurricularPlanState.ACTIVE);

    public static final DegreeCurricularPlanState NOT_ACTIVE_OBJ = new DegreeCurricularPlanState(
            DegreeCurricularPlanState.NOT_ACTIVE);

    public static final DegreeCurricularPlanState CONCLUDED_OBJ = new DegreeCurricularPlanState(
            DegreeCurricularPlanState.CONCLUDED);

    public static final DegreeCurricularPlanState PAST_OBJ = new DegreeCurricularPlanState(
            DegreeCurricularPlanState.PAST);

    private Integer state;

    public DegreeCurricularPlanState() {
    }

    public DegreeCurricularPlanState(int state) {
        this.state = new Integer(state);
    }

    public DegreeCurricularPlanState(Integer state) {
        this.state = state;
    }

    public Integer getDegreeState() {
        return this.state;
    }

    public void setDegreeState(Integer state) {
        this.state = state;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof DegreeCurricularPlanState) {
            DegreeCurricularPlanState ds = (DegreeCurricularPlanState) obj;
            resultado = this.getDegreeState().equals(ds.getDegreeState());
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
        case NOT_ACTIVE:
            valueS = "NOT_ACTIVE";
            break;
        case CONCLUDED:
            valueS = "CONCLUDED";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }*/

}