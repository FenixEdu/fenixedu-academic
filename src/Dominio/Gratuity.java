package Dominio;

import java.util.Date;

import Util.GratuityState;
import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Gratuity extends DomainObject implements IGratuity {

    private State state;

    private GratuityState gratuityState;

    private Date date;

    private String remarks;

    private IStudentCurricularPlan studentCurricularPlan;

    private Integer keyStudentCurricularPlan;

    public Gratuity() {
    }

    public String toString() {
        String result = "Gratuity ";
        result += ", Internal Code =" + getIdInternal();
        result += ", State =" + state;
        result += ", Gratuity State =" + gratuityState;
        result += ", Date =" + date;
        result += ", Remarks =" + remarks;
        result += ", Student Curricular Plan =" + studentCurricularPlan;
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
    public Integer getKeyStudentCurricularPlan() {
        return keyStudentCurricularPlan;
    }

    /**
     * @param keyStudentCurricularPlan
     */
    public void setKeyStudentCurricularPlan(Integer keyStudentCurricularPlan) {
        this.keyStudentCurricularPlan = keyStudentCurricularPlan;
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

    /**
     * @return
     */
    public IStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    /**
     * @param studentCurricularPlan
     */
    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

}