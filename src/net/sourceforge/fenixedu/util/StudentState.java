package net.sourceforge.fenixedu.util;

/**
 * @author dcs-rjao 25/Mar/2003
 */
public class StudentState extends FenixUtil {

    public static final int BASE = 1;

    public static final int INSCRITO = 2;

    public static final int PRESCRITO = 3;

    public static final int INTERRUPCAO = 4;

    public static final int AUSENTE = 5;

    public static final int LICENCIADO = 6;

    public static final StudentState BASE_OBJ = new StudentState(BASE);

    public static final StudentState INSCRITO_OBJ = new StudentState(INSCRITO);

    public static final StudentState PRESCRITO_OBJ = new StudentState(PRESCRITO);

    public static final StudentState INTERRUPCAO_OBJ = new StudentState(INTERRUPCAO);

    public static final StudentState AUSENTE_OBJ = new StudentState(AUSENTE);

    public static final StudentState LICENCIADO_OBJ = new StudentState(LICENCIADO);

    private Integer state;

    /** Creates a new instance of StudentState */
    public StudentState() {
    }

    public StudentState(int state) {
        this.state = new Integer(state);
    }

    public StudentState(Integer state) {
        this.state = state;
    }

    /**
     * Getter for property state.
     * 
     * @return Value of property state.
     */
    public java.lang.Integer getState() {
        return state;
    }

    /**
     * Setter for property state.
     * 
     * @param state
     *            New value of property state.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    public boolean equals(Object o) {
        if (o instanceof StudentState) {
            StudentState aux = (StudentState) o;
            return this.state.equals(aux.getState());
        }

        return false;

    }

    public String toString() {

        int value = this.state.intValue();
        String valueS = null;

        switch (value) {
        case BASE:
            valueS = "BASE";
            break;
        case INSCRITO:
            valueS = "INSCRITO";
            break;
        case PRESCRITO:
            valueS = "PRESCRITO";
            break;
        case INTERRUPCAO:
            valueS = "INTERRUPCAO";
            break;
        case AUSENTE:
            valueS = "AUSENTE";
            break;
        case LICENCIADO:
            valueS = "LICENCIADO";
            break;
        default:
            break;
        }

        return valueS;
    }
}