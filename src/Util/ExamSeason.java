package Util;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 22/Abr/2003
 */
public class ExamSeason extends FenixUtil {

    public static final int FIRST_SEASON = 1;

    public static final int SECOND_SEASON = 2;

    public static final int SPECIAL_SEASON = 3;

    public static final ExamSeason FIRST_SEASON_OBJ = new ExamSeason(
            ExamSeason.FIRST_SEASON);

    public static final ExamSeason SECOND_SEASON_OBJ = new ExamSeason(
            ExamSeason.SECOND_SEASON);

    public static final ExamSeason SPECIAL_SEASON_OBJ = new ExamSeason(
            ExamSeason.SPECIAL_SEASON);

    private Integer type;

    public ExamSeason() {
    }

    public ExamSeason(int state) {
        this.type = new Integer(state);
    }

    public ExamSeason(Integer state) {
        this.type = state;
    }

    public java.lang.Integer getType() {
        return type;
    }

    public void setType(Integer state) {
        this.type = state;
    }

    public boolean equals(Object o) {
        if (o instanceof ExamSeason) {
            ExamSeason aux = (ExamSeason) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    public String toString() {

        int value = this.type.intValue();
        String values = null;

        switch (value) {
        case FIRST_SEASON:
            values = "FIRST_SEASON";
            break;
        case SECOND_SEASON:
            values = "SECOND_SEASON";
            break;
        case SPECIAL_SEASON:
            values = "SPECIAL_SEASON";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + values + "]";
    }
}