/*
 * Created on 13/Nov/2003
 *
 */
package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *13/Nov/2003
 *
 */
public class ReimbursementGuideState
{

    public static final int ISSUED = 1;
    public static final int APPROVED = 2;
    public static final int PAYED = 3;
    public static final int ANNULLED = 4;

    public static final ReimbursementGuideState ISSUED_TYPE = new ReimbursementGuideState(ISSUED);
    public static final ReimbursementGuideState APPROVED_TYPE = new ReimbursementGuideState(APPROVED);
    public static final ReimbursementGuideState PAYED_TYPE = new ReimbursementGuideState(PAYED);
    public static final ReimbursementGuideState ANNULLED_TYPE = new ReimbursementGuideState(ANNULLED);

    public static final String ISSUED_STRING = "Emitida";
    public static final String APPROVED_STRING = "Aprovada";
    public static final String PAYED_STRING = "Paga";
    public static final String ANNULLED_STRING = "Anulada";
    public static final String DEFAULT = "[Escolha uma Situação]";

    private Integer reimbursementGuideState;

    public ReimbursementGuideState()
    {
    }

    public ReimbursementGuideState(int type)
    {
        this.reimbursementGuideState = new Integer(type);
    }

    public ReimbursementGuideState(Integer type)
    {
        this.reimbursementGuideState = type;
    }

    public ReimbursementGuideState(String type)
    {
        if (type.equals(ReimbursementGuideState.ANNULLED_STRING))
        {
            this.reimbursementGuideState = new Integer(ReimbursementGuideState.ANNULLED);
        }
        if (type.equals(ReimbursementGuideState.PAYED_STRING))
        {
            this.reimbursementGuideState = new Integer(ReimbursementGuideState.PAYED);
        }
        if (type.equals(ReimbursementGuideState.APPROVED_STRING))
        {
            this.reimbursementGuideState = new Integer(ReimbursementGuideState.APPROVED);
        }
        if (type.equals(ReimbursementGuideState.ISSUED_STRING))
        {
            this.reimbursementGuideState = new Integer(ReimbursementGuideState.ISSUED);
        }
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof ReimbursementGuideState)
        {
            ReimbursementGuideState ds = (ReimbursementGuideState) obj;
            resultado = this.getReimbursementGuideState().equals(ds.getReimbursementGuideState());
        }
        return resultado;
    }

    public static ArrayList toArrayList()
    {
        ArrayList result = new ArrayList();
        result.add(new LabelValueBean(ReimbursementGuideState.DEFAULT, null));
        result.add(
            new LabelValueBean(
                ReimbursementGuideState.ANNULLED_STRING,
                ReimbursementGuideState.ANNULLED_STRING));
        result.add(
            new LabelValueBean(
                ReimbursementGuideState.PAYED_STRING,
                ReimbursementGuideState.PAYED_STRING));
        //		result.add(
        //			new LabelValueBean(
        //				ReimbursementGuideState.APPROVED_STRING,
        //				ReimbursementGuideState.APPROVED_STRING));
        result.add(
            new LabelValueBean(
                ReimbursementGuideState.ISSUED_STRING,
                ReimbursementGuideState.ISSUED_STRING));
        return result;
    }

    public String toString()
    {
        if (reimbursementGuideState.intValue() == ReimbursementGuideState.ANNULLED)
        {
            return ReimbursementGuideState.ANNULLED_STRING;
        }
        if (reimbursementGuideState.intValue() == ReimbursementGuideState.PAYED)
        {
            return ReimbursementGuideState.PAYED_STRING;
        }
        if (reimbursementGuideState.intValue() == ReimbursementGuideState.APPROVED)
        {
            return ReimbursementGuideState.APPROVED_STRING;
        }
        if (reimbursementGuideState.intValue() == ReimbursementGuideState.ISSUED)
        {
            return ReimbursementGuideState.ISSUED_STRING;
        }
        return "ERRO!"; // Nunca e atingido
    }

    /**
     * @return
     */
    public Integer getReimbursementGuideState()
    {
        return this.reimbursementGuideState;
    }

    /**
     * @param integer
     */
    public void setReimbursementGuideState(Integer situation)
    {
        this.reimbursementGuideState = situation;
    }

}
