package DataBeans;

import java.util.Date;

import Util.GratuityState;
import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGratuity extends InfoObject {

    private State state;

    private GratuityState gratuityState;

    private Date date;

    private String remarks;

    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    public InfoGratuity() {
    }

    public String toString() {
        String result = "Gratuity ";
        result += ", Internal Code =" + getIdInternal();
        result += ", State =" + state;
        result += ", Gratuity State =" + gratuityState;
        result += ", Date =" + date;
        result += ", Remarks =" + remarks;
        result += ", Student Curricular Plan =" + infoStudentCurricularPlan;
        return result;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return
     */
    public GratuityState getGratuityState() {
        return gratuityState;
    }

    /**
     * @param gratuityState
     */
    public void setGratuityState(GratuityState gratuityState) {
        this.gratuityState = gratuityState;
    }

    /**
     * @return
     */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    /**
     * @param infoStudentCurricularPlan
     */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    /**
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

}