package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class PaymentType extends FenixUtil {

    public static final int CASH = 1;

    public static final int ATM = 2;

    public static final int CHEQUE = 3;

    public static final int NIB_TRANSFER = 4;

    public static final int POSTAL = 5;

    public static final PaymentType CASH_TYPE = new PaymentType(CASH);

    public static final PaymentType ATM_TYPE = new PaymentType(ATM);

    public static final PaymentType CHEQUE_TYPE = new PaymentType(CHEQUE);

    public static final PaymentType NIB_TRANSFER_TYPE = new PaymentType(NIB_TRANSFER);

    public static final PaymentType POSTAL_TYPE = new PaymentType(POSTAL);

    public static final String CASH_STRING = "Dinheiro";

    public static final String ATM_STRING = "MultiBanco";

    public static final String CHEQUE_STRING = "Cheque";

    public static final String NIB_TRANSFER_STRING = "Transferência Bancária";

    public static final String POSTAL_STRING = "Vale de Correio";

    public static final String DEFAULT_STRING = "[Escolha uma Opção]";

    private Integer type;

    public PaymentType() {
    }

    public PaymentType(int type) {
        this.type = new Integer(type);
    }

    public PaymentType(Integer type) {
        this.type = type;
    }

    public PaymentType(String type) {
        if (type.equals(PaymentType.CASH_STRING))
            this.type = new Integer(PaymentType.CASH);
        if (type.equals(PaymentType.ATM_STRING))
            this.type = new Integer(PaymentType.ATM);
        if (type.equals(PaymentType.CHEQUE_STRING))
            this.type = new Integer(PaymentType.CHEQUE);
        if (type.equals(PaymentType.NIB_TRANSFER_STRING))
            this.type = new Integer(PaymentType.NIB_TRANSFER);
        if (type.equals(PaymentType.POSTAL_STRING))
            this.type = new Integer(PaymentType.POSTAL);

    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof PaymentType) {
            PaymentType ds = (PaymentType) obj;
            resultado = this.getType().equals(ds.getType());
        }
        return resultado;
    }

    public static List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(PaymentType.DEFAULT_STRING, PaymentType.DEFAULT_STRING));
        result.add(new LabelValueBean(PaymentType.CASH_STRING, PaymentType.CASH_STRING));
        result.add(new LabelValueBean(PaymentType.ATM_STRING, PaymentType.ATM_STRING));
        result.add(new LabelValueBean(PaymentType.CHEQUE_STRING, PaymentType.CHEQUE_STRING));
        result.add(new LabelValueBean(PaymentType.NIB_TRANSFER_STRING, PaymentType.NIB_TRANSFER_STRING));
        result.add(new LabelValueBean(PaymentType.POSTAL_STRING, PaymentType.POSTAL_STRING));
        //		result.add(PaymentType.CASH_STRING);
        //		result.add(PaymentType.ATM_STRING);
        //		result.add(PaymentType.CHEQUE_STRING);
        //		result.add(PaymentType.NIB_TRANSFER_STRING);
        //		result.add(PaymentType.POSTAL_STRING);
        //			
        return result;
    }

    public String toString() {
        if (type.intValue() == PaymentType.CASH)
            return PaymentType.CASH_STRING;
        if (type.intValue() == PaymentType.ATM)
            return PaymentType.ATM_STRING;
        if (type.intValue() == PaymentType.CHEQUE)
            return PaymentType.CHEQUE_STRING;
        if (type.intValue() == PaymentType.NIB_TRANSFER)
            return PaymentType.NIB_TRANSFER_STRING;
        if (type.intValue() == PaymentType.POSTAL)
            return PaymentType.POSTAL_STRING;
        return "ERRO!"; // Nunca e atingido
    }

    /**
     * @return
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param integer
     */
    public void setType(Integer integer) {
        type = integer;
    }

}