/*
 * Season.java
 *
 * Created on 2003/03/26
 */

/**
 * @author Luis Cruz & Sara Ribeiro
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class Season extends FenixUtil {

    public static final int SEASON1 = 1;

    public static final int SEASON2 = 2;

    public static final int SPECIAL_SEASON = 3;

    public static final String SEASON1_STRING = "1ª Época";

    public static final String SEASON2_STRING = "2ª Época";

    public static final String SPECIAL_SEASON_STRING = "Época Especial";

    public static final String DEFAULT = "[Escolha a Epoca]";

    private Integer season;

    /** Creates a new instance of EpocaExame */
    public Season() {
    }

    public Season(int season) {
        this.season = new Integer(season);
    }

    public Season(Integer season) {
        this.season = season;
    }

    public Season(String season) {
        if (season.equals(Season.SEASON1_STRING))
            this.season = new Integer(Season.SEASON1);
        if (season.equals(Season.SEASON2_STRING))
            this.season = new Integer(Season.SEASON2);
        if (season.equals(Season.SPECIAL_SEASON_STRING))
            this.season = new Integer(Season.SPECIAL_SEASON);
    }

    public boolean equals(Object o) {
        if (o instanceof Season) {
            Season aux = (Season) o;
            return this.season.equals(aux.getseason());
        }

        return false;

    }

    public List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(Season.DEFAULT, null));
        result.add(new LabelValueBean(Season.SEASON1_STRING, Season.SEASON1_STRING));
        result.add(new LabelValueBean(Season.SEASON2_STRING, Season.SEASON2_STRING));
        result.add(new LabelValueBean(Season.SPECIAL_SEASON_STRING, Season.SPECIAL_SEASON_STRING));
        return result;
    }

    public String toString() {
        if (season.intValue() == Season.SEASON1)
            return Season.SEASON1_STRING;
        if (season.intValue() == Season.SEASON2)
            return Season.SEASON2_STRING;
        if (season.intValue() == Season.SPECIAL_SEASON)
            return Season.SPECIAL_SEASON_STRING;
        return "ERRO!"; // Nunca e atingido
    }

    /**
     * Getter for property season.
     * 
     * @return Value of property season.
     *  
     */
    public java.lang.Integer getseason() {
        return season;
    }

    public java.lang.Integer getSeason() {
        return season;
    }

    /**
     * Setter for property season.
     * 
     * @param season
     *            New value of property season.
     *  
     */
    public void setSeason(java.lang.Integer season) {
        this.season = season;
    }

}