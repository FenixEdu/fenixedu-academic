/*
 * Created on 13/Nov/2003
 *
 */
package Dominio.reimbursementGuide;

import java.util.Calendar;

import Dominio.DomainObject;
import Dominio.IEmployee;
import Util.ReimbursementGuideState;
import Util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *13/Nov/2003
 *
 */
public class ReimbursementGuideSituation extends DomainObject implements IReimbursementGuideSituation
{

    protected State state;
    protected IReimbursementGuide reimbursementGuide;
    protected String remarks;
    protected IEmployee employee;
    protected Calendar modificationDate;
    protected ReimbursementGuideState reimbursementGuideState;

    private Integer keyReimbursementGuide;
    private Integer keyEmployee;

    /**
     * 
     */
    public ReimbursementGuideSituation()
    {
    }

    /**
     * @return
     */
    public IEmployee getEmployee()
    {
        return employee;
    }

    /**
     * @param employee
     */
    public void setEmployee(IEmployee employee)
    {
        this.employee = employee;
    }

    /**
     * @return
     */
    public Calendar getModificationDate()
    {
        return modificationDate;
    }

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    /**
     * @return
     */
    public IReimbursementGuide getReimbursementGuide()
    {
        return reimbursementGuide;
    }

    /**
     * @param reimbursementGuide
     */
    public void setReimbursementGuide(IReimbursementGuide reimbursementGuide)
    {
        this.reimbursementGuide = reimbursementGuide;
    }

    /**
     * @return
     */
    public ReimbursementGuideState getReimbursementGuideState()
    {
        return reimbursementGuideState;
    }

    /**
     * @param reimbursementGuideState
     */
    public void setReimbursementGuideState(ReimbursementGuideState reimbursementGuideState)
    {
        this.reimbursementGuideState = reimbursementGuideState;
    }

    /**
     * @return
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * @return
     */
    public State getState()
    {
        return state;
    }

    /**
     * @param state
     */
    public void setState(State state)
    {
        this.state = state;
    }

    /**
     * @return
     */
    public Integer getKeyEmployee()
    {
        return keyEmployee;
    }

    /**
     * @param keyEmployee
     */
    public void setKeyEmployee(Integer keyEmployee)
    {
        this.keyEmployee = keyEmployee;
    }

    /**
     * @return
     */
    public Integer getKeyReimbursementGuide()
    {
        return keyReimbursementGuide;
    }

    /**
     * @param keyReimbursementGuide
     */
    public void setKeyReimbursementGuide(Integer keyReimbursementGuide)
    {
        this.keyReimbursementGuide = keyReimbursementGuide;
    }

}
