package Util;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentEvaluationType implements Serializable {

    public static final int NORMAL = 1; // primeira avaliacao (engloba 1ª e 2ª época)
    public static final int IMPROVEMENT = 2; // avaliacao por melhoria
    public static final int SPECIAL_SEASON = 3; // avaliacao na epoca especial
    public static final int EXTERNAL = 4; // avaliacao feita fora do IST
    public static final int EQUIVALENCE = 5; // avaliacao feita por equivalencia
    public static final int CLOSED = 6; // inscricao com avaliacao fechada

    public static final int FIRST_SEASON = 7;
    // só para a classe Enrolment Evaluation, usada apenas na migracao dos Enrolments para guardar histórico, não usar para mais nada!
    public static final int SECOND_SEASON = 8;
    // só para a classe Enrolment Evaluation, usada apenas na migracao dos Enrolments para guardar histórico, não usar para mais nada!
    public static final int NO_SEASON = 9;
    // só para a classe Enrolment Evaluation, usada apenas na migracao dos Enrolments para guardar histórico, não usar para mais nada!

    public static final EnrolmentEvaluationType NORMAL_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
    public static final EnrolmentEvaluationType IMPROVEMENT_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
    public static final EnrolmentEvaluationType SPECIAL_SEASON_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
    public static final EnrolmentEvaluationType EXTERNAL_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.EXTERNAL);
    public static final EnrolmentEvaluationType EQUIVALENCE_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE);
    public static final EnrolmentEvaluationType CLOSED_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED);
    public static final EnrolmentEvaluationType FIRST_SEASON_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.FIRST_SEASON);
    public static final EnrolmentEvaluationType SECOND_SEASON_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.SECOND_SEASON);
    public static final EnrolmentEvaluationType NO_SEASON_OBJ =
        new EnrolmentEvaluationType(EnrolmentEvaluationType.NO_SEASON);

    public static final String NORMAL_STRING = "Epoca Normal";
    public static final String IMPROVEMENT_STRING = "Melhoria de Nota";
    public static final String SPECIAL_SEASON_STRING = "Epoca Especial";
    public static final String EQUIVALENCE_STRING = "Por Equivalência";

    private Integer type;

    public EnrolmentEvaluationType()
    {
    }

    public EnrolmentEvaluationType(int state)
    {
        this.type = new Integer(state);
    }

    public EnrolmentEvaluationType(Integer state)
    {
        this.type = state;
    }

    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.Integer getType()
    {
        return type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(Integer state)
    {
        this.type = state;
    }

    public boolean equals(Object o)
    {
        if (o instanceof EnrolmentEvaluationType)
        {
            EnrolmentEvaluationType aux = (EnrolmentEvaluationType) o;
            return this.type.equals(aux.getType());
        } else
        {
            return false;
        }
    }

    public String toString()
    {

        int value = this.type.intValue();
        String valueS = null;

        switch (value)
        {
            case NORMAL :
                valueS = "NORMAL";
                break;
            case IMPROVEMENT :
                valueS = "IMPROVEMENT";
                break;
            case SPECIAL_SEASON :
                valueS = "SPECIAL_SEASON";
                break;
            case EXTERNAL :
                valueS = "EXTERNAL";
                break;
            case EQUIVALENCE :
                valueS = "EQUIVALENCE";
                break;
            case CLOSED :
                valueS = "CLOSED";
                break;
            case FIRST_SEASON :
                valueS = "FIRST_SEASON";
                break;
            case SECOND_SEASON :
                valueS = "SECOND_SEASON";
                break;
            case NO_SEASON :
                valueS = "NO_SEASON";
                break;
            default :
                break;
        }

        return valueS;
    }

    public ArrayList toArrayList()
    {
        ArrayList result = new ArrayList();
        //		   result.add(new LabelValueBean(EnrolmentEvaluationType.DEFAULT, null));
        result.add(
            new LabelValueBean(
                EnrolmentEvaluationType.IMPROVEMENT_STRING,
                String.valueOf(EnrolmentEvaluationType.IMPROVEMENT)));
        result.add(
            new LabelValueBean(
                EnrolmentEvaluationType.NORMAL_STRING,
                String.valueOf(EnrolmentEvaluationType.NORMAL)));
        result.add(
            new LabelValueBean(
                EnrolmentEvaluationType.SPECIAL_SEASON_STRING,
                String.valueOf(EnrolmentEvaluationType.SPECIAL_SEASON)));
        result.add(
            new LabelValueBean(
                EnrolmentEvaluationType.EQUIVALENCE_STRING,
                String.valueOf(EnrolmentEvaluationType.EQUIVALENCE)));
        return result;
    }

}
